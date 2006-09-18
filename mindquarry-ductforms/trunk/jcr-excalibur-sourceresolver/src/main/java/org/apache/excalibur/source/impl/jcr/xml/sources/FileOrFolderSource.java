/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package org.apache.excalibur.source.impl.jcr.xml.sources;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import javax.jcr.Session;

import org.apache.excalibur.source.ModifiableTraversableSource;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.impl.jcr.xml.JCRSourceFactory;

/**
 * Represents a file (nt:file) or a folder (nt:folder) in a node
 * 
 * @author alexander.klimetschek@mindquarry.com
 * 
 */
public class FileOrFolderSource extends AbstractJCRNodeSource implements
        ModifiableTraversableSource {

    /**
     * @param factory
     * @param session
     * @param path
     * @throws SourceException
     */
    public FileOrFolderSource(JCRSourceFactory factory, Session session,
            String path) throws SourceException {
        super(factory, session, path);
        // TODO Auto-generated constructor stub
    }

    // =========================================================================
    // Source interface
    // =========================================================================

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.excalibur.source.Source#getContentLength()
     */
    public long getContentLength() {
        // TODO Auto-generated method stub
        return 0;
    }

    // =========================================================================
    // ModifiableTraversableSource interface
    // =========================================================================

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.excalibur.source.ModifiableTraversableSource#makeCollection()
     */
    public void makeCollection() throws SourceException {
        // TODO Auto-generated method stub

    }

    // =========================================================================
    // ModifiableSource interface
    // =========================================================================

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.excalibur.source.ModifiableSource#canCancel(java.io.OutputStream)
     */
    public boolean canCancel(OutputStream stream) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.excalibur.source.ModifiableSource#cancel(java.io.OutputStream)
     */
    public void cancel(OutputStream stream) throws IOException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.excalibur.source.ModifiableSource#delete()
     */
    public void delete() throws SourceException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.excalibur.source.ModifiableSource#getOutputStream()
     */
    public OutputStream getOutputStream() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    // =========================================================================
    // TraversableSource interface
    // =========================================================================

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.excalibur.source.TraversableSource#getChild(java.lang.String)
     */
    public Source getChild(String name) throws SourceException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.excalibur.source.TraversableSource#getChildren()
     */
    public Collection getChildren() throws SourceException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.excalibur.source.TraversableSource#getName()
     */
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.excalibur.source.TraversableSource#getParent()
     */
    public Source getParent() throws SourceException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.excalibur.source.TraversableSource#isCollection()
     */
    public boolean isCollection() {
        // TODO Auto-generated method stub
        return false;
    }

}
