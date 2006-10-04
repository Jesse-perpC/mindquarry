/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.xml.source.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.jcr.Node;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.CascadingIOException;
import org.apache.cocoon.serialization.Serializer;
import org.apache.cocoon.serialization.XMLSerializer;
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
     * 
     * @param manager
     */
    public static InputStream getInputStream(ServiceManager manager, Node node)
            throws IOException, SourceNotFoundException {

        ByteArrayInputStream inputStream = null;
        Serializer serializer = null;
        try {
            // we use Cocoons XMLSerializer here which is configurable in terms
            // of the Transformer Implementation to use (some are buggy, so we
            // want to ensure which one is used)
            serializer = (Serializer) manager.lookup(XMLSerializer.ROLE);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            serializer.setOutputStream(outputStream);

            XMLFileSourceHelper.toSAX(serializer, node);

            inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        } catch (ServiceException se) {
            throw new CascadingIOException(se);
        } catch (SAXException e) {
            throw new CascadingIOException(e);
        } finally {
            manager.release(serializer);
        }

        return inputStream;
    }
}
