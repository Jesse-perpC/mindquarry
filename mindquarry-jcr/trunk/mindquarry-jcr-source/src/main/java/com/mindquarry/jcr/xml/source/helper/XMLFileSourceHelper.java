/*
 * Copyright (C) 2006-2007 MindQuarry GmbH, All Rights Reserved
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 */
package com.mindquarry.jcr.xml.source.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.jcr.Node;

import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.avalon.framework.logger.NullLogger;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.CascadingIOException;
import org.apache.cocoon.serialization.Serializer;
import org.apache.cocoon.serialization.XMLSerializer;
import org.apache.excalibur.source.SourceNotFoundException;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.mindquarry.common.init.InitializationException;
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

        Serializer serializer = createSerializer();

        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            serializer.setOutputStream(outputStream);

            XMLFileSourceHelper.toSAX(serializer, node);

            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (SAXException e) {
            throw new CascadingIOException(e);
        } finally {
            if (null != outputStream)
                outputStream.close();
        }
    }
    
    private static Serializer createSerializer() {
        // we use Cocoons XMLSerializer here which is configurable in terms
        // of the Transformer Implementation to use (some are buggy, so we
        // want to ensure which one is used)
        
        DefaultConfiguration transformerConfig = 
            new DefaultConfiguration("transformer-factory");
        transformerConfig.setValue(
                "org.apache.xalan.xsltc.trax.TransformerFactoryImpl");
        
        DefaultConfiguration serializerConfig = 
            new DefaultConfiguration("serializer");
        serializerConfig.setAttribute("class", "org.apache.cocoon.serialization.XMLSerializer");
        serializerConfig.addChild(transformerConfig);
        
        
        XMLSerializer serializer = new XMLSerializer();
        serializer.enableLogging(new NullLogger());
        try {
            serializer.configure(serializerConfig);
        } catch (ConfigurationException e) {
            throw new InitializationException(
                    "could not confiure XMLSerializer", e);
        }
        
        return serializer;
    }
}
