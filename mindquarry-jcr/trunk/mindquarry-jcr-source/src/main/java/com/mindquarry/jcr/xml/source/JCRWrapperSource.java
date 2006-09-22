/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.xml.source;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.excalibur.source.ModifiableTraversableSource;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;


/**
 * Source wrapper for all JCR sources.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JCRWrapperSource extends AbstractJCRNodeSource implements
        ModifiableTraversableSource, XMLizable {
    /**
     * Default contructor. Passes all parameters to the constructor of the super
     * class.
     * 
     * {@link AbstractJCRNodeSource#AbstractJCRNodeSource(JCRSourceFactory, Session, String)}
     */
    public JCRWrapperSource(JCRSourceFactory factory, Session session,
            String path) throws SourceException {
        super(factory, session, path);
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

        return null;
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
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.ModifiableSource#cancel(java.io.OutputStream)
     */
    public void cancel(OutputStream stream) throws IOException {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.ModifiableSource#delete()
     */
    public void delete() throws SourceException {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.ModifiableSource#getOutputStream()
     */
    public OutputStream getOutputStream() throws IOException {
        return null;
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
                return factory.createSource(session, node.getNode(name).getPath());
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
    public Source getParent() throws SourceException {
        if (this.path.length() == 1) {
            // we are the root node
            return null;
        }
        int lastPos = path.lastIndexOf('/');
        String parentPath = lastPos == 0 ? "/" : path.substring(0, lastPos);
        return factory.createSource(session, parentPath);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.TraversableSource#isCollection()
     */
    public boolean isCollection() {
        try {
            if (node.isNodeType("nt:folder")) {
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
                JCRWrapperSource parent = (JCRWrapperSource) getParent();
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

    public void toSAX(ContentHandler arg0) throws SAXException {

    }
}
