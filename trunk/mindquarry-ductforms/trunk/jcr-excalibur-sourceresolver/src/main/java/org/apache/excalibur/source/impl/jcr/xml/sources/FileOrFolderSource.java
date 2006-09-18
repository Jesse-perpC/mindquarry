/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package org.apache.excalibur.source.impl.jcr.xml.sources;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import org.apache.excalibur.source.ModifiableTraversableSource;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;

/**
 * @author alexander.klimetschek@mindquarry.com
 *
 */
public class FileOrFolderSource extends AbstractJCRNodeSource implements ModifiableTraversableSource {

    /* (non-Javadoc)
     * @see org.apache.excalibur.source.ModifiableTraversableSource#makeCollection()
     */
    public void makeCollection() throws SourceException {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.apache.excalibur.source.ModifiableSource#canCancel(java.io.OutputStream)
     */
    public boolean canCancel(OutputStream stream) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.apache.excalibur.source.ModifiableSource#cancel(java.io.OutputStream)
     */
    public void cancel(OutputStream stream) throws IOException {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.apache.excalibur.source.ModifiableSource#delete()
     */
    public void delete() throws SourceException {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.apache.excalibur.source.ModifiableSource#getOutputStream()
     */
    public OutputStream getOutputStream() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.excalibur.source.TraversableSource#getChild(java.lang.String)
     */
    public Source getChild(String name) throws SourceException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.excalibur.source.TraversableSource#getChildren()
     */
    public Collection getChildren() throws SourceException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.excalibur.source.TraversableSource#getName()
     */
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.excalibur.source.TraversableSource#getParent()
     */
    public Source getParent() throws SourceException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.excalibur.source.TraversableSource#isCollection()
     */
    public boolean isCollection() {
        // TODO Auto-generated method stub
        return false;
    }

}
