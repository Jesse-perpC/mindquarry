/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.xml.source.helper;

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
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.excalibur.source.ModifiableSource;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.mindquarry.jcr.xml.source.AbstractJCRNodeSource;
import com.mindquarry.jcr.xml.source.JCRSourceFactory;
import com.mindquarry.jcr.xml.source.handler.JCRNodesToSAXConverter;
import com.mindquarry.jcr.xml.source.helper.stream.XMLFileOutputStream;

/**
 * Source for a node that represents a file (xt:document).
 * 
 * @author <a
 *         href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">Alexander
 *         Klimetschek</a>
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class XMLFileSourceHelper {


    // =========================================================================
    // XMLizable interface
    // =========================================================================

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.xml.sax.XMLizable#toSAX(org.xml.sax.ContentHandler)
     */
    public static void toSAX(ContentHandler handler, Node node) throws SAXException {
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
    public static InputStream getInputStream(Node node) throws IOException,
            SourceNotFoundException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        SAXTransformerFactory stfactory = (SAXTransformerFactory) SAXTransformerFactory
        .newInstance();
        TransformerHandler handler;
		try {
			handler = stfactory.newTransformerHandler();
			handler.setResult(new StreamResult(os));
	        XMLFileSourceHelper.toSAX(handler, node);
	        
	        return new ByteArrayInputStream(os.toByteArray());
		} catch (TransformerConfigurationException e) {
			throw new IOException("Unable to configure Transformer Factory: " + e.getLocalizedMessage());
		} catch (SAXException e) {
			throw new IOException("Unable to serialize XML results: "  + e.getLocalizedMessage());
		}
    }

    // =========================================================================
    // ModifiableSource interface
    // =========================================================================

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.excalibur.source.ModifiableSource#canCancel(java.io.OutputStream)
     */
    public static boolean canCancel(OutputStream stream) {
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
    public static void cancel(OutputStream stream) throws IOException {
        if (canCancel(stream)) {
            ((XMLFileOutputStream) stream).cancel();
        } else {
            throw new IllegalArgumentException("Stream cannot be cancelled");
        }
    }
}
