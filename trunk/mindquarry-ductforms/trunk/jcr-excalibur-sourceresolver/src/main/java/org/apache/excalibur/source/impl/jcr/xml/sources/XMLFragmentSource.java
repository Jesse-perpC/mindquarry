/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package org.apache.excalibur.source.impl.jcr.xml.sources;

import java.io.IOException;
import java.io.OutputStream;

import javax.jcr.Session;

import org.apache.excalibur.source.ModifiableSource;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.impl.jcr.xml.JCRSourceFactory;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * @author alexander.klimetschek@mindquarry.com
 *
 */
public class XMLFragmentSource extends AbstractJCRNodeSource
        implements ModifiableSource, XMLizable {

    /**
     * @param factory
     * @param session
     * @param path
     * @throws SourceException 
     */
    public XMLFragmentSource(JCRSourceFactory factory, Session session,
            String path) throws SourceException {
        super(factory, session, path);
        // TODO Auto-generated constructor stub
    }

    // =========================================================================
    // XMLizable interface
    // =========================================================================

    /* (non-Javadoc)
     * @see org.apache.excalibur.xml.sax.XMLizable#toSAX(org.xml.sax.ContentHandler)
     */
    public void toSAX(ContentHandler handler) throws SAXException {
        // TODO Auto-generated method stub

    }

    // =========================================================================
    // Source interface
    // =========================================================================

    /* (non-Javadoc)
     * @see org.apache.excalibur.source.Source#getContentLength()
     */
    public long getContentLength() {
        // TODO Auto-generated method stub
        return 0;
    }

    // =========================================================================
    // ModifiableSource interface
    // =========================================================================

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

}
