/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.source.xml.sources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.excalibur.source.ModifiableTraversableSource;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceNotFoundException;

import com.mindquarry.jcr.source.xml.JCRSourceFactory;

/**
 * Source for a node that represents a file (nt:file) or a folder (nt:folder).
 * 
 * @author <a
 *         href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">Alexander
 *         Klimetschek</a>
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class FileOrFolderSource extends AbstractJCRNodeSource implements
        ModifiableTraversableSource {
    /**
     * Default contructor. Passes all parameters to the constructor of the super
     * class.
     * 
     * {@link AbstractJCRNodeSource#AbstractJCRNodeSource(JCRSourceFactory, Session, String)}
     */
    public FileOrFolderSource(JCRSourceFactory factory, Session session,
            String path) throws SourceException {
        super(factory, session, path);
    }

    // =========================================================================
    // Source interface
    // =========================================================================

    /**
     * @see org.apache.excalibur.source.Source#getInputStream()
     */
    public InputStream getInputStream() throws IOException,
            SourceNotFoundException {
        return null;
    }

    // =========================================================================
    // ModifiableTraversableSource interface
    // =========================================================================

    /**
     * @see org.apache.excalibur.source.ModifiableTraversableSource#makeCollection()
     */
    public void makeCollection() throws SourceException {
        // nothing to do
    }

    // =========================================================================
    // ModifiableSource interface
    // =========================================================================

    /**
     * @see org.apache.excalibur.source.ModifiableSource#canCancel(java.io.OutputStream)
     */
    public boolean canCancel(OutputStream stream) {
        return false;
    }

    /**
     * @see org.apache.excalibur.source.ModifiableSource#cancel(java.io.OutputStream)
     */
    public void cancel(OutputStream stream) throws IOException {
    }

    /**
     * @see org.apache.excalibur.source.ModifiableSource#delete()
     */
    public void delete() throws SourceException {
    }

    /**
     * @see org.apache.excalibur.source.ModifiableSource#getOutputStream()
     */
    public OutputStream getOutputStream() throws IOException {
        return null;
    }

    // =========================================================================
    // TraversableSource interface
    // =========================================================================

    /**
     * @see org.apache.excalibur.source.TraversableSource#getChild(java.lang.String)
     */
    public Source getChild(String name) throws SourceException {
        String path;
        JCRSourceFactory factory = new JCRSourceFactory();
        try {
            Node child = node.getNode(name);
            path = child.getPath();
        } catch (Exception e) {
            throw new SourceException("Child node not available.", e);
        }
        return factory.createSource(session, path);
    }

    /**
     * @see org.apache.excalibur.source.TraversableSource#getChildren()
     */
    public Collection getChildren() throws SourceException {
        List<Source> childs = new ArrayList<Source>();
        try {
            NodeIterator nit = node.getNodes();
            while (nit.hasNext()) {
                Node child = nit.nextNode();
                childs.add(getChild(child.getName()));
            }
        } catch (RepositoryException e) {
            throw new SourceException("A child node was not available.", e);
        }
        return childs;
    }

    /**
     * @see org.apache.excalibur.source.TraversableSource#getName()
     */
    public String getName() {
        try {
            return node.getName();
        } catch (RepositoryException e) {
            return "";
        }
    }

    /**
     * @see org.apache.excalibur.source.TraversableSource#getParent()
     */
    public Source getParent() throws SourceException {
        String path;
        JCRSourceFactory factory = new JCRSourceFactory();
        try {
            Node child = node.getParent();
            path = child.getPath();
        } catch (Exception e) {
            throw new SourceException("Parent node not available.", e);
        }
        return factory.createSource(session, path);
    }

    /**
     * @see org.apache.excalibur.source.TraversableSource#isCollection()
     */
    public boolean isCollection() {
        try {
            if(node.isNodeType("nt:folder")) {
                return true;
            } else {
                return false;
            }
        } catch (RepositoryException e) {
            return false;
        }
    }
}
