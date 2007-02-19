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

import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;

import org.apache.cocoon.components.source.VersionableSource;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceValidity;

import com.mindquarry.common.index.IndexClient;

/**
 * Base class for all JCR Node Sources as well as the wrapper source.
 * 
 * @author <a
 *         href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">Alexander
 *         Klimetschek</a>
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public abstract class AbstractJCRNodeSource implements Source, VersionableSource {
    /**
     * The factory that created this Source.
     */
    protected final JCRSourceFactory factory;

    /**
     * The session this source is bound to.
     */
    protected final Session session;

    /**
     * The node pointed to by this source (can be null).
     */
    protected Node node;
    
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
    
    protected final boolean isVersioned;
    
    protected String baseRevision;

    /**
     * Basic constructor for initializing what every JCRNodeSource must have.
     * Checks if the given path represents a node. If not a SourceException is
     * thrown.
     * 
     * @param factory The factory that manages us.
     * @param session The current JCR session in use.
     * @throws SourceException
     */
    public AbstractJCRNodeSource(JCRSourceFactory factory, Session session,
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
    public AbstractJCRNodeSource(JCRSourceFactory factory, Session session,
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
    public AbstractJCRNodeSource(JCRSourceFactory factory, Session session,
            String path, IndexClient iClient, boolean makeVersionable, String revison) throws SourceException {
        this.factory = factory;
        this.session = session;
        this.path = path;
        this.iClient = iClient;

        this.baseRevision = null;
        
        try {
            Item item = session.getItem(path);
            if (!item.isNode()) {
                throw new SourceException("Path '" + path
                        + "' is a property (should be a node)");
            } else {
                // check if it is a file, a folder or the root node
                // (nt:file and nt:folder extend nt:hierarchyNode, others too)
                Node tmp = (Node) item;
                if (tmp.isNodeType("nt:hierarchyNode")
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
					this.baseRevision = revison;
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
     * {@inheritDoc}
     * 
     * Uses the standard jcr:lastModified property.
     * 
     * @see org.apache.excalibur.source.Source#getLastModified()
     */
    public long getLastModified() {
        if (!exists()) {
            return 0;
        }
        try {
            Property prop = node.getNode("jcr:content").getProperty(
                    "jcr:lastModified");
            return prop == null ? 0 : prop.getDate().getTime().getTime();
        } catch (RepositoryException e) {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * Uses the standard jcr:mimeType property.
     * 
     * @see org.apache.excalibur.source.Source#getMimeType()
     */
    public String getMimeType() {
        if (!exists()) {
            return null;
        }
        try {
            Property prop = node.getNode("jcr:content").getProperty(
                    "jcr:mimeType");
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
            computedURI = factory.getScheme() + "://" + path;
        }
        return computedURI;
    }

    /**
     * Returns null, aka non-cacheable. Overwrite this in subclasses.
     * 
     * @see org.apache.excalibur.source.Source#getValidity()
     */
    public SourceValidity getValidity() {
        return null;
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
     * @see org.apache.excalibur.source.Source#getContentLength()
     */
    public long getContentLength() {
        return -1;
    }
    
    
    
    /*
     * Versionable Source
     */
    

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
			if ((this.baseRevision!=null)&&(this.node.isNodeType("nt:frozenNode"))) {
				return this.baseRevision;
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
				return this.node.isNodeType("mix:versionable");
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
}
