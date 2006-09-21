/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.source.xml.sources;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.excalibur.source.ModifiableSource;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.mindquarry.jcr.source.xml.JCRXMLSourceFactory;
import com.mindquarry.jcr.source.xml.handler.JCRNodesToSAXConverter;
import com.mindquarry.jcr.source.xml.sources.stream.XMLFileOutputStream;

/**
 * Source for a node that represents a file (xt:document).
 * 
 * @author <a
 *         href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">Alexander
 *         Klimetschek</a>
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class XMLFileSource extends AbstractJCRNodeSource implements
        ModifiableSource, XMLizable {
    /**
     * Default contructor. Passes all parameters to the constructor of the super
     * class.
     * 
     * {@link AbstractJCRNodeSource#AbstractJCRNodeSource(JCRXMLSourceFactory, Session, String)}
     */
    public XMLFileSource(JCRXMLSourceFactory factory, Session session,
            String path) throws SourceException {
        super(factory, session, path);
    }

    // =========================================================================
    // XMLizable interface
    // =========================================================================

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.xml.sax.XMLizable#toSAX(org.xml.sax.ContentHandler)
     */
    public void toSAX(ContentHandler handler) throws SAXException {
        JCRNodesToSAXConverter.convertToSAX(node, handler);
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
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        os.write(xmlHead.getBytes());

        try {
            getBytes(node.getNode("jcr:content"), os);
        } catch (Exception e) {
            throw new IOException("Error while reading repository content.");
        }
        return new ByteArrayInputStream(os.toByteArray());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.Source#getContentLength()
     */
    public long getContentLength() {
        return -1;
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
        if (stream instanceof XMLFileOutputStream) {
            return ((XMLFileOutputStream) stream).canCancel();
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.ModifiableSource#cancel(java.io.OutputStream)
     */
    public void cancel(OutputStream stream) throws IOException {
        if (canCancel(stream)) {
            ((XMLFileOutputStream) stream).cancel();
        } else {
            throw new IllegalArgumentException("Stream cannot be cancelled");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.ModifiableSource#delete()
     */
    public void delete() throws SourceException {
        try {
            node.remove();
            node = null;
            session.save();
        } catch (Exception e) {
            throw new SourceException("Can not remove node due to version"
                    + " or constraint problems.", e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.ModifiableSource#getOutputStream()
     */
    public OutputStream getOutputStream() throws IOException {
        return new XMLFileOutputStream(node, session);
    }

    // =========================================================================
    // private methods
    // =========================================================================

    private void getBytes(Node node, ByteArrayOutputStream os)
            throws IOException {
        try {
            NodeIterator nit = node.getNodes();
            while (nit.hasNext()) {
                Node child = nit.nextNode();
                if (child.isNodeType("xt:text")) {
                    InputStream is = child.getProperty("xt:characters")
                            .getStream();

                    int b;
                    while ((b = is.read()) != -1) {
                        os.write(b);
                    }
                } else if (child.isNodeType("xt:element")) {
                    os.write("<".getBytes());
                    os.write(child.getName().getBytes());

                    PropertyIterator pit = child.getProperties();
                    while (pit.hasNext()) {
                        Property prop = pit.nextProperty();
                        if (prop.getName().startsWith("jcr")) {
                            continue;
                        }
                        String att = " " + prop.getName() + "=\""
                                + prop.getString() + "\"";
                        os.write(att.getBytes());
                    }
                    os.write(">".getBytes());

                    getBytes(child, os);

                    os.write("<".getBytes());
                    os.write(child.getName().getBytes());
                    os.write(">".getBytes());
                }
            }
        } catch (RepositoryException e) {
            throw new IOException("Error while reading repository content.");
        }
    }
}
