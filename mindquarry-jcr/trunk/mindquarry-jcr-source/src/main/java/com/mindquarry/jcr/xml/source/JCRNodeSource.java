/*
 * Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 */
package com.mindquarry.jcr.xml.source;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.jcr.Item;
import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.Version;
import javax.jcr.version.VersionException;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;

import org.apache.cocoon.components.source.VersionableSource;
import org.apache.cocoon.components.source.helpers.SourceProperty;
import org.apache.excalibur.source.ModifiableTraversableSource;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;
import org.apache.excalibur.source.impl.validity.TimeStampValidity;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.mindquarry.common.index.IndexClient;
import com.mindquarry.common.source.Change;
import com.mindquarry.common.source.ChangeableSource;
import com.mindquarry.common.source.RevisedPath;
import com.mindquarry.jcr.xml.source.helper.FileSourceHelper;
import com.mindquarry.jcr.xml.source.helper.XMLFileSourceHelper;
import com.mindquarry.jcr.xml.source.helper.stream.JCROutputStream;

/**
 * Base class for all JCR Node Sources as well as the wrapper source.
 * 
 * @author <a
 *         href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">Alexander
 *         Klimetschek</a>
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JCRNodeSource implements Source, ModifiableTraversableSource,
        /*InspectableSource,*/ VersionableSource, ChangeableSource, XMLizable {
    /**
     * The factory that created this Source.
     */
    protected final JCRSourceFactory factory;

    /**
     * The session this source is bound to.
     */
    protected final Session session;

    /**
     * The node pointed to by this source (can be null). It contains the actual
     * data (as on versionized sources this differs from the baseNode)
     */
    protected Node node;
    
    /**
     * The node that holds the version information and representing the HEAD
     * version.
     */
    protected Node baseNode;

    /**
     * The node path (cannot be changed later).
     */
    protected final String path;

    /**
     * The full URI of this node including scheme identifier.
     */
    protected String computedURI;

    /**
     * Indexing client to be used for update notifications.
     */
    protected IndexClient iClient;
    
    /**
     * Indicates whether this node gets versioned.
     */
    protected final boolean isVersioned;
    
    /**
     * The revision this node represents or null if non-versionized or HEAD
     * version.
     */
    protected String revision;

    /**
     * Basic constructor for initializing what every JCRNodeSource must have.
     * Checks if the given path represents a node. If not a SourceException is
     * thrown.
     * 
     * @param factory The factory that manages us.
     * @param session The current JCR session in use.
     * @throws SourceException
     */
    public JCRNodeSource(JCRSourceFactory factory, Session session,
            String path, IndexClient iClient) throws SourceException {
    	this(factory, session, path, iClient, true, null);
    }
    
    /**
     * Basic constructor for initializing what every JCRNodeSource must have.
     * Checks if the given path represents a node. If not a SourceException is
     * thrown.
     * 
     * @param factory The factory that manages us.
     * @param session The current JCR session in use.
     * @throws SourceException
     */
    public JCRNodeSource(JCRSourceFactory factory, Session session,
            String path, IndexClient iClient, String revision) throws SourceException {
    	this(factory, session, path, iClient, true, revision);
    }
    
    /**
     * Basic constructor for initializing what every JCRNodeSource must have.
     * Checks if the given path represents a node. If not a SourceException is
     * thrown.
     * 
     * @param factory The factory that manages us.
     * @param session The current JCR session in use.
     * @param makeVersionable Make the node versionable if it is not already
     * @throws SourceException
     */
    public JCRNodeSource(JCRSourceFactory factory, Session session,
            String path, IndexClient iClient, boolean makeVersionable, String revison) throws SourceException {
        this.factory = factory;
        this.session = session;
        this.path = path;
        this.iClient = iClient;

        this.revision = null;
        
        try {
            Item item = session.getItem(path);
            if (!item.isNode()) {
                throw new SourceException("Path '" + path
                        + "' is a property (should be a node)");
            } else {
                // check if it is a file, a folder or the root node
                // (nt:file and nt:folder extend nt:hierarchyNode, others too)
                Node tmp = (Node) item;
                if (tmp.isNodeType(JCRConstants.NT_HIERARCHYNODE)
                        || tmp.getPath().equals("/")) {
                    node = (Node) item;
                    this.baseNode = node;
                    getRevisionNode(revison);
                } else {
                    throw new SourceException("Path '" + path
                            + "' should be a nt:file or nt:folder");
                }
            }
        } catch (PathNotFoundException e) {
            // node does not exist
            node = null;
        } catch (RepositoryException e) {
            throw new SourceException("Cannot lookup repository path: " + path,
                    e);
        }
        this.isVersioned = makeVersionable;
    }

	private void getRevisionNode(String revison) throws SourceException, UnsupportedRepositoryOperationException, RepositoryException {
		if (isVersioned()&&(revison!=null)) {
			VersionHistory history = this.baseNode.getVersionHistory();
			VersionIterator hit = history.getAllVersions();
			while(hit.hasNext()) {
				Version version = hit.nextVersion();
				if (version.getName().equals(revison)) {
					node = version.getNodes().nextNode();
					this.revision = revison;
					break;
				}
			}
		}
	}

    // =========================================================================
    // Source interface
    // =========================================================================

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.Source#exists()
     */
    public boolean exists() {
        return node != null;
    }

    /**
     * Uses the standard jcr:lastModified property.
     * 
     * @see org.apache.excalibur.source.Source#getLastModified()
     */
    public long getLastModified() {
        if (!exists()) {
            return 0;
        }
        try {
            Property prop = node.getNode(JCRConstants.JCR_CONTENT).getProperty(
                    JCRConstants.JCR_LASTMODIFIED);
            return prop == null ? 0 : prop.getDate().getTime().getTime();
        } catch (RepositoryException e) {
            return 0;
        }
    }

    /**
     * Uses the standard jcr:mimeType property.
     * 
     * @see org.apache.excalibur.source.Source#getMimeType()
     */
    public String getMimeType() {
        if (!exists()) {
            return null;
        }
        try {
            Property prop = node.getNode(JCRConstants.JCR_CONTENT).getProperty(
                    JCRConstants.JCR_MIMETYPE);
            return prop == null ? null : prop.getString();
        } catch (RepositoryException re) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.Source#getScheme()
     */
    public String getScheme() {
        return factory.getScheme();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.Source#getURI()
     */
    public String getURI() {
        if (computedURI == null) {
            if (revision != null) {
                computedURI = factory.getScheme() + "://" + path + "?revision=" + revision;
            } else {
                computedURI = factory.getScheme() + "://" + path;
            }
        }
        return computedURI;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.Source#getValidity()
     */
    public SourceValidity getValidity() {
        try {
            // frozen nodes never change
            if (node.isNodeType(JCRConstants.NT_FROZENNODE)) {
                return new NOPValidity();
            }
        } catch (RepositoryException re) {
            return null;
        }
        long lastModified = this.getLastModified();
        if (lastModified == 0) {
            return null;
        } else {
            return new TimeStampValidity(lastModified);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.Source#refresh()
     */
    public void refresh() {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.Source#getInputStream()
     */
    public InputStream getInputStream() throws IOException, SourceNotFoundException {
        if (exists()) {
            try {
                if (JCRSourceFactory.isFolder(node)) {
                    return null;
                } else if (JCRSourceFactory.isFile(node)) {
                    Node child = node.getNode(JCRConstants.JCR_CONTENT);
                    
                    if (JCRSourceFactory.isXMLResource(child)) {
                        return XMLFileSourceHelper.getInputStream(
                                factory.getServiceManager(), child);
                        
                    } else if (JCRSourceFactory.isBinaryResource(child)) {
                        return FileSourceHelper.getInputStream(child);
                        
                    } else {
                        throw new IOException(
                                "Unable to get an input stream for node type: "
                                        + child.getPrimaryNodeType().getName());
                    }
                } else {
                    throw new SourceNotFoundException("Resource has no readable contents");
                }
            } catch (RepositoryException e) {
                throw new IOException("Unable to retrieve node: "
                        + e.getLocalizedMessage());
            }
        }
        throw new SourceNotFoundException("Source does not exist");
    }

    /**
     * Content length can only be given for nodes containing binary data, not
     * for xml documents.
     * 
     * @see org.apache.excalibur.source.Source#getContentLength()
     */
    public long getContentLength() {
        try {
            if (JCRSourceFactory.hasBinaryContent(node)) {
                return node.getNode(JCRConstants.JCR_CONTENT).getProperty(JCRConstants.JCR_DATA).getLength();
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }
    
    // =========================================================================
    // ModifiableSource interface
    // =========================================================================
    
    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.ModifiableSource#canCancel(java.io.OutputStream)
     */
    public boolean canCancel(OutputStream stream) {
        if (exists()) {
            try {
                if (JCRSourceFactory.isFolder(node)) {
                    return false;
                } else if (JCRSourceFactory.isFile(node)) {
                    return ((JCROutputStream) stream).canCancel();
                }
            } catch (RepositoryException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.ModifiableSource#cancel(java.io.OutputStream)
     */
    public void cancel(OutputStream stream) throws IOException {
        if (exists()) {
            try {
                if (JCRSourceFactory.isFolder(node)) {
                    return;
                } else if (JCRSourceFactory.isFile(node)) {
                    ((JCROutputStream) stream).cancel();
                }
            } catch (RepositoryException e) {
                throw new IOException("Unable to retrieve node: "
                        + e.getLocalizedMessage());
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.ModifiableSource#delete()
     */
    public void delete() throws SourceException {
        // check if the node really exists
        if (node == null) {
            throw new SourceException(
                    "Can not remove a node which does not exist.");
        }
        // try to remove the node
        try {
            node.remove();
            node = null;
            session.save();
        } catch (Exception e) {
            throw new SourceException("Can not remove node due to version"
                    + " or constraint problems.", e);
        }
        // use index client to notify the indexer about the delete
        List<String> changedPaths = new ArrayList<String>();
        List<String> deletedPaths = new ArrayList<String>();
        deletedPaths.add(getURI());
        iClient.index(changedPaths, deletedPaths);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.ModifiableSource#getOutputStream()
     */
    public OutputStream getOutputStream() throws IOException {
        if (exists()) {
            if (!isHeadRevision()) {
                throw new SourceException("Source is read-only. Try changing the head revision");
            }
            
            try {
                // check for current type here (must not be NT_FROZENNODE)
                if (node.isNodeType(JCRConstants.NT_FILE)) {
                    Node child = node.getNode(JCRConstants.JCR_CONTENT);
                    if (JCRSourceFactory.isBinaryResource(child)
                            || JCRSourceFactory.isXMLResource(child)) {
                        return new JCROutputStream(node, session, iClient,
                                getURI());
                    } else {
                        throw new IOException(
                                "Unable to get an output stream for node type: "
                                        + child.getPrimaryNodeType().getName());
                    }
                } else {
                    // cannot write into folders
                    // cannot write into frozen = old versions of a node
                    // cannot write into anything else
                    return null;
                }
            } catch (RepositoryException e) {
                throw new IOException("Unable to retrieve node: "
                        + e.getLocalizedMessage());
            }
        } else {
            // node does not yet exist, create the parent path
            if (!getParent().exists()) {
                getParent().makeCollection();
            }
            try {
                node = getParent().node.addNode(getName(), JCRConstants.NT_FILE);
                if (this.isVersioned) {
                	node.addMixin(JCRConstants.MIX_VERSIONABLE);
                }
                return new JCROutputStream(node, session, iClient, getURI());
            } catch (ItemExistsException e) {
                throw new IOException("Resource already exists: "
                        + e.getLocalizedMessage());
            } catch (PathNotFoundException e) {
                throw new IOException("Path not found: "
                        + e.getLocalizedMessage());
            } catch (NoSuchNodeTypeException e) {
                throw new IOException("No such node type: "
                        + e.getLocalizedMessage());
            } catch (LockException e) {
                throw new IOException("Unable to get lock: "
                        + e.getLocalizedMessage());
            } catch (VersionException e) {
                throw new IOException("Versioning not possible: "
                        + e.getLocalizedMessage());
            } catch (ConstraintViolationException e) {
                throw new IOException("Violated constrains: "
                        + e.getLocalizedMessage());
            } catch (RepositoryException e) {
                throw new IOException("Unable to write to repository: "
                        + e.getLocalizedMessage());
            }
        }
    }

    // =========================================================================
    // TraversableSource interface
    // =========================================================================
    
    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.TraversableSource#getChild(java.lang.String)
     */
    public Source getChild(String name) throws SourceException {
        if (isCollection()) {
            try {
                return factory.createSource(session, node.getNode(name)
                        .getPath());
            } catch (Exception e) {
                throw new SourceException("Could not get child node.", e);
            }
        } else {
            throw new SourceException("Not a collection: " + getURI());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.TraversableSource#getChildren()
     */
    public Collection getChildren() throws SourceException {
        if (!isCollection()) {
            return Collections.EMPTY_LIST;
        } else {
            ArrayList<Source> children = new ArrayList<Source>();
            try {
                NodeIterator nodes = this.node.getNodes();
                while (nodes.hasNext()) {
                    children.add(factory.createSource(session, nodes.nextNode()
                            .getPath()));
                }
            } catch (RepositoryException e) {
                throw new SourceException("Cannot get child nodes for "
                        + getURI(), e);
            }
            return children;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.TraversableSource#getName()
     */
    public String getName() {
        return path.substring(path.lastIndexOf('/') + 1);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.TraversableSource#getParent()
     */
    public JCRNodeSource getParent() throws SourceException {
        if (path.length() == 1) {
            // we are the root node
            return null;
        }
        int lastPos = path.lastIndexOf('/');
        String parentPath = lastPos == 0 ? "/" : path.substring(0, lastPos);
        return (JCRNodeSource) factory.createSource(session, parentPath);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.TraversableSource#isCollection()
     */
    public boolean isCollection() {
        try {
            if (JCRSourceFactory.isFolder(node) || path.equals("/")) {
                return true;
            } else {
                return false;
            }
        } catch (RepositoryException e) {
            return false;
        }
    }

    // =========================================================================
    // ModifiableTraversableSource interface
    // =========================================================================
    
    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.ModifiableTraversableSource#makeCollection()
     */
    public void makeCollection() throws SourceException {
        if (exists()) {
            if (!isHeadRevision()) {
                throw new SourceException("Source is read-only. Try changing the head revision");
            }
            
            if (!isCollection()) {
                throw new SourceException(
                        "Cannot make a collection with existing node at "
                                + getURI());
            }
        } else {
            try {
                // Ensure parent exists
                JCRNodeSource parent = (JCRNodeSource) getParent();
                if (parent == null) {
                    throw new SourceException(
                            "Problem: root node does not exist!!");
                }
                parent.makeCollection();
                Node parentNode = parent.node;
                node = parentNode.addNode(getName(), JCRConstants.NT_FOLDER);
                session.save();
            } catch (RepositoryException e) {
                throw new SourceException("Cannot make collection " + getURI(),
                        e);
            }
        }
    }

    // =========================================================================
    // XMLizable interface
    // =========================================================================
    
    public void toSAX(ContentHandler handler) throws SAXException {
        try {
            if (JCRSourceFactory.hasXMLContent(node)) {
                XMLFileSourceHelper.toSAX(handler, node.getNode(JCRConstants.JCR_CONTENT));
            }
        } catch (PathNotFoundException e) {
            throw new SAXException("Repository path not found.", e);
        } catch (RepositoryException e) {
            throw new SAXException("Repository not accessable.", e);
        }
    }

    // =========================================================================
    // VersionableSource interface
    // =========================================================================
    
    public String getLatestSourceRevision() throws SourceException {
        if (isVersioned()&&exists()) {
            try {
                return node.getBaseVersion().getName();
            } catch (UnsupportedRepositoryOperationException e) {
                throw new SourceException("Unable to access version history", e);
            } catch (RepositoryException e) {
                throw new SourceException("Unable to access underlying node", e);
            }
        }
        return null;
    }

    public String getSourceRevision() throws SourceException {
        try {
            if ((this.revision!=null)&&(this.node.isNodeType(JCRConstants.NT_FROZENNODE))) {
                return this.revision;
            }
        } catch (RepositoryException e) {
            throw new SourceException("Unable to access underlying node", e);
        }
        if (isVersioned()&&exists()) {
            try {
                return node.getBaseVersion().getName();
            } catch (UnsupportedRepositoryOperationException e) {
                throw new SourceException("Unable to access version history", e);
            } catch (RepositoryException e) {
                throw new SourceException("Unable to access underlying node", e);
            }
        }
        return null;
    }

    public String getSourceRevisionBranch() throws SourceException {
        return "";
    }

    public boolean isVersioned() throws SourceException {
        if (exists()) {
            try {
                return this.node.isNodeType(JCRConstants.MIX_VERSIONABLE);
            } catch (RepositoryException e) {
                throw new SourceException("Unable to access underlying node", e);
            }
        }
        return this.isVersioned;
    }

    public void setSourceRevision(String revision) throws SourceException {
        try {
            getRevisionNode(revision);
        } catch (UnsupportedRepositoryOperationException e) {
            throw new SourceException("Unable to get version from repository",e);
        } catch (RepositoryException e) {
            throw new SourceException("Unable to access underlying node",e);
        }
    }

    public void setSourceRevisionBranch(String branch) throws SourceException {}

    // =========================================================================
    // ChangeableSource interface
    // =========================================================================
    
    public List<Change> changesFrom(long startRevision, long nMaxChanges) throws SourceException {
        List<Change> changes = new Vector<Change>();
        if (isVersioned()) {
            try {
                VersionHistory history = this.baseNode.getVersionHistory();
                VersionIterator hit = history.getAllVersions();
                int revision = 0;
                while(hit.hasNext()) {
                    Version version = hit.nextVersion();
                    if ((revision>=startRevision)) {
                        changes.add(makeChange(version));
                    }
                    if ((nMaxChanges>0)&&(changes.size()>=nMaxChanges)) {
                        break;
                    }
                    revision++;
                }
            } catch (RepositoryException re) {
                throw new SourceException("Unable to access repository", re);
            }
        }
        return new Vector<Change>();
    }

    // =========================================================================
    // RevisableSource interface
    // =========================================================================
    
    public boolean isHeadRevision() {
        try {
            return !this.node.isNodeType(JCRConstants.NT_FROZENNODE);
        } catch (RepositoryException e) {
            return true;
        }
    }

    public String revision() {
        try {
            return this.getSourceRevision();
        } catch (SourceException e) {
            return null;
        }
    }

    // =========================================================================
    // InspectableSource interface
    // =========================================================================
    
    /**
     * {@inheritDoc}
     * 
     * @see org.apache.cocoon.components.source.InspectableSource#getSourceProperties()
     */
    public SourceProperty[] getSourceProperties() throws SourceException {
        // TODO: read source properties directly from node        
        // JCR_AUTHOR
        // JCR_MESSAGE
        
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.cocoon.components.source.InspectableSource#getSourceProperty(java.lang.String, java.lang.String)
     */
    public SourceProperty getSourceProperty(String namespace, String name) throws SourceException {
        // TODO: read requested source property directly from node        
        // JCR_AUTHOR
        // JCR_MESSAGE
        
        return null;
    }

    /**
     * Not supported for jcr nodes.
     * 
     * @see org.apache.cocoon.components.source.InspectableSource#removeSourceProperty(java.lang.String, java.lang.String)
     */
    public void removeSourceProperty(String namespace, String name) throws SourceException {
        throw new UnsupportedOperationException("Cannot remove source properties on JCR node sources.");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.cocoon.components.source.InspectableSource#setSourceProperty(org.apache.cocoon.components.source.helpers.SourceProperty)
     */
    public void setSourceProperty(SourceProperty property) throws SourceException {
        if (exists()) {
            if (!isHeadRevision()) {
                throw new SourceException("Source is read-only. Try changing the head revision");
            }
        }
        // TODO: a) write property directly to node OR b) remember prop and set on output stream
        // for a) need to create node if it does not exist yet - check with getOutputStream()
        // for b) remembering needs another getter method for JCROutputStream
        // JCR_AUTHOR
        // JCR_MESSAGE
    }

    // =========================================================================
    // custom methods
    // =========================================================================
    
    private Change makeChange(Version version) {
        try {
            return new Change(version.getCreated().getTime(), "unknown author", "no message", version.getName(), new RevisedPath[] {new RevisedPath(path, 'M')});
        } catch (RepositoryException e) {
            return new Change(new Date(), "unknown author", "no message", "-1", new RevisedPath[] {new RevisedPath(path, 'M')});
        }
    }

    public Session getSession() {
        return session;
    }

}
