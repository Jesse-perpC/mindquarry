/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.source.xml.sources;

import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceValidity;

import com.mindquarry.jcr.source.xml.JCRXMLSourceFactory;

/**
 * Base class for all JCR Node Sources.
 * 
 * @author <a
 *         href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">Alexander
 *         Klimetschek</a>
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public abstract class AbstractJCRNodeSource implements Source {

    /** The factory that created this Source */
    protected final JCRXMLSourceFactory factory;

    /** The session this source is bound to */
    protected final Session session;

    /** The node pointed to by this source (can be null) */
    protected Node node;

    /** The node path (cannot be changed later) */
    protected final String path;

    /**
     * Basic constructor for initializing what every JCRNodeSource must have.
     * 
     * @param factory The factory that manages us.
     * @param session The current JCR session in use.
     * @throws SourceException
     */
    public AbstractJCRNodeSource(JCRXMLSourceFactory factory, Session session,
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
                this.node = (Node) item;
            }
        } catch (PathNotFoundException e) {
            // Not found
            this.node = null;
        } catch (RepositoryException e) {
            throw new SourceException("Cannot lookup repository path " + path,
                    e);
        }
    }

    // =========================================================================
    // Source interface
    // =========================================================================

    /**
     * @see org.apache.excalibur.source.Source#exists()
     */
    public boolean exists() {
        return this.node != null;
    }

    /**
     * Uses the standard jcr:lastModified property
     * 
     * @see org.apache.excalibur.source.Source#getLastModified()
     */
    public long getLastModified() {
        if (!exists()) {
            return 0;
        }
        try {
            Property prop = this.node.getProperty("jcr:lastModified");
            return prop == null ? 0 : prop.getDate().getTime().getTime();
        } catch (RepositoryException re) {
            return 0;
        }
    }

    /**
     * @see org.apache.excalibur.source.Source#getMimeType()
     */
    public String getMimeType() {
        return null;
    }

    /**
     * @see org.apache.excalibur.source.Source#getScheme()
     */
    public String getScheme() {
        return this.factory.getScheme();
    }

    /**
     * @see org.apache.excalibur.source.Source#getURI()
     */
    public String getURI() {
        return null;
    }

    /**
     * @see org.apache.excalibur.source.Source#getValidity()
     */
    public SourceValidity getValidity() {
        return null;
    }

    /**
     * @see org.apache.excalibur.source.Source#refresh()
     */
    public void refresh() {
    }
}
