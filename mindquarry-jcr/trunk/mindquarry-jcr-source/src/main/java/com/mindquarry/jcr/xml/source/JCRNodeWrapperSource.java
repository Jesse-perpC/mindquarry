/*
 * Copyright (C) 2006-2007 MindQuarry GmbH, All Rights Reserved
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
import java.util.List;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;

import org.apache.excalibur.source.ModifiableTraversableSource;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.TimeStampValidity;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.mindquarry.common.index.IndexClient;
import com.mindquarry.jcr.xml.source.helper.FileSourceHelper;
import com.mindquarry.jcr.xml.source.helper.XMLFileSourceHelper;
import com.mindquarry.jcr.xml.source.helper.stream.JCROutputStream;

/**
 * Source wrapper for all JCR sources.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JCRNodeWrapperSource extends AbstractJCRNodeSource implements
        ModifiableTraversableSource, XMLizable {
    /**
     * Default contructor. Passes all parameters to the constructor of the super
     * class.
     * 
     * {@link AbstractJCRNodeSource#AbstractJCRNodeSource(JCRSourceFactory, Session, String)}
     */
    public JCRNodeWrapperSource(JCRSourceFactory factory, Session session,
            String path, IndexClient iClient) throws SourceException {
        super(factory, session, path, iClient);
    }

    // =========================================================================
    // Source interface
    // =========================================================================

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.Source#getInputStream()
     */
    public InputStream getInputStream() throws IOException,
            SourceNotFoundException {
        if (exists()) {
            try {
                if (node.isNodeType("nt:folder")) {
                    return null;
                } else if (node.isNodeType("nt:file")) {
                    Node child = node.getNode("jcr:content");
                    if (child.isNodeType("xt:document")) {
                        return XMLFileSourceHelper.getInputStream(
                                factory.getServiceManager(), node);
                    } else if (child.isNodeType("nt:resource")) {
                        return FileSourceHelper.getInputStream(node);
                    } else {
                        throw new IOException(
                                "Unable to get an input stream for node type: "
                                        + child.getPrimaryNodeType().getName());
                    }
                } else {
                    throw new SourceNotFoundException("Resource does not exist");
                }
            } catch (RepositoryException e) {
                throw new IOException("Unable to retrieve node: "
                        + e.getLocalizedMessage());
            }
        }
        throw new SourceNotFoundException("Source does not exist");
    }


    /**
     * {@inheritDoc}
     * 
     * @see com.mindquarry.jcr.xml.source.AbstractJCRNodeSource#getValidity()
     */
    @Override
    public SourceValidity getValidity() {
        long lastModified = this.getLastModified();
        if (lastModified == 0) {
            return null;
        } else {
            return new TimeStampValidity(lastModified);
        }
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
                if (node.isNodeType("nt:folder")) {
                    return false;
                } else if (node.isNodeType("nt:file")) {
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
                if (node.isNodeType("nt:folder")) {
                    return;
                } else if (node.isNodeType("nt:file")) {
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
            try {
                if (node.isNodeType("nt:folder")) {
                    return null;
                } else if (node.isNodeType("nt:file")) {
                    Node child = node.getNode("jcr:content");
                    if (child.isNodeType("xt:document")
                            || child.isNodeType("nt:resource")) {
                        return new JCROutputStream(node, session, iClient,
                                getURI());
                    } else {
                        throw new IOException(
                                "Unable to get an output stream for node type: "
                                        + child.getPrimaryNodeType().getName());
                    }
                } else {
                    throw new SourceNotFoundException(
                            "Resource is neither file nor folder");
                }
            } catch (RepositoryException e) {
                throw new IOException("Unable to retrieve node: "
                        + e.getLocalizedMessage());
            }
        } else {
            if (!getParent().exists()) {
                getParent().makeCollection();
            }
            try {
                node = getParent().node.addNode(getName(), "nt:file");
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
    public JCRNodeWrapperSource getParent() throws SourceException {
        if (path.length() == 1) {
            // we are the root node
            return null;
        }
        int lastPos = path.lastIndexOf('/');
        String parentPath = lastPos == 0 ? "/" : path.substring(0, lastPos);
        return (JCRNodeWrapperSource) factory.createSource(session, parentPath);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.TraversableSource#isCollection()
     */
    public boolean isCollection() {
        try {
            if (node.isNodeType("nt:folder") || path.equals("/")) {
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
            if (!isCollection()) {
                throw new SourceException(
                        "Cannot make a collection with existing node at "
                                + getURI());
            }
        } else {
            try {
                // Ensure parent exists
                JCRNodeWrapperSource parent = (JCRNodeWrapperSource) getParent();
                if (parent == null) {
                    throw new SourceException(
                            "Problem: root node does not exist!!");
                }
                parent.makeCollection();
                Node parentNode = parent.node;

                node = parentNode.addNode(getName(), "nt:folder");
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
            if ((node.isNodeType("nt:file"))
                    && (node.getNode("jcr:content").isNodeType("xt:document"))) {
                XMLFileSourceHelper.toSAX(handler, node);
            }
        } catch (PathNotFoundException e) {
            throw new SAXException("Repository path not found.", e);
        } catch (RepositoryException e) {
            throw new SAXException("Repository not accessable.", e);
        }
    }
}
