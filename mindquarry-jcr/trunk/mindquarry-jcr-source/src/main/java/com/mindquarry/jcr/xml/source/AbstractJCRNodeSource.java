/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.xml.source;

import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceValidity;


/**
 * Base class for all JCR Node Sources as well as the wrapper source.
 * 
 * @author <a
 *         href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">Alexander
 *         Klimetschek</a>
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public abstract class AbstractJCRNodeSource implements Source {
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

    /**
     * The node path (cannot be changed later).
     */
    protected final String path;

    /**
     * The full URI of this node including scheme identifier.
     */
    protected String computedURI;

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
            String path) throws SourceException {
        this.factory = factory;
        this.session = session;
        this.path = path;

        try {
            Item item = session.getItem(path);
            if (!item.isNode()) {
                throw new SourceException("Path '" + path
                        + "' is a property (should be a node)");
            } else {
                node = (Node) item;
            }
        } catch (PathNotFoundException e) {
            // node does not exist
            node = null;
        } catch (RepositoryException e) {
            throw new SourceException("Cannot lookup repository path: " + path,
                    e);
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
            Property prop = node.getProperty("jcr:lastModified");
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
            Property prop = node.getProperty("jcr:mimeType");
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
            computedURI = factory.getScheme() + ":/" + path;
        }
        return computedURI;
    }

    /**
     * {@inheritDoc}
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
}
