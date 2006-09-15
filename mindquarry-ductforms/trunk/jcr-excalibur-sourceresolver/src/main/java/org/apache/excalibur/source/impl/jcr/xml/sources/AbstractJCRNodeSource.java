/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package org.apache.excalibur.source.impl.jcr.xml.sources;

import java.io.IOException;
import java.io.InputStream;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.source.SourceValidity;

/**
 * @author alexander.klimetschek@mindquarry.com
 *
 */
public class AbstractJCRNodeSource implements Source {

    /* (non-Javadoc)
     * @see org.apache.excalibur.source.Source#exists()
     */
    public boolean exists() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.apache.excalibur.source.Source#getContentLength()
     */
    public long getContentLength() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.apache.excalibur.source.Source#getInputStream()
     */
    public InputStream getInputStream() throws IOException,
            SourceNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.excalibur.source.Source#getLastModified()
     */
    public long getLastModified() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.apache.excalibur.source.Source#getMimeType()
     */
    public String getMimeType() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.excalibur.source.Source#getScheme()
     */
    public String getScheme() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.excalibur.source.Source#getURI()
     */
    public String getURI() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.excalibur.source.Source#getValidity()
     */
    public SourceValidity getValidity() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.excalibur.source.Source#refresh()
     */
    public void refresh() {
        // TODO Auto-generated method stub

    }

}
