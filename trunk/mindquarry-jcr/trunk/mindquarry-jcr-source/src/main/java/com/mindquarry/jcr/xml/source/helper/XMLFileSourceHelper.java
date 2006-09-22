/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.xml.source.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.jcr.Node;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.excalibur.source.SourceNotFoundException;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.mindquarry.jcr.xml.source.handler.JCRNodesToSAXConverter;

/**
 * Source helper for nodes that represents a file (nt:file) with XML content.
 * 
 * @author <a href="mailto:lars(dot)trieloff(at)mindquarry(dot)com">Lars
 *         Trieloff</a>
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class XMLFileSourceHelper {
    // =========================================================================
    // XMLizable interface
    // =========================================================================

    /**
     * Delegate for toSAX.
     */
    public static void toSAX(ContentHandler handler, Node node)
            throws SAXException {
        JCRNodesToSAXConverter.convertToSAX(node, handler);
    }

    // =========================================================================
    // Source interface
    // =========================================================================

    /**
     * Delegate for getInputStream
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
            throw new IOException("Unable to configure Transformer Factory: "
                    + e.getLocalizedMessage());
        } catch (SAXException e) {
            throw new IOException("Unable to serialize XML results: "
                    + e.getLocalizedMessage());
        }
    }
}
