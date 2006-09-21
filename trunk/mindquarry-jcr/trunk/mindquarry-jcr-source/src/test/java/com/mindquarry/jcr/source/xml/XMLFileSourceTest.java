/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.source.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.avalon.framework.service.ServiceException;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.mindquarry.jcr.source.xml.sources.XMLFileSource;

/**
 * Test cases for the XMLFileSource implementation.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class XMLFileSourceTest extends JCRSourceTestBase {
    private XMLFileSource source;

    public void testXMLFileRetrieval() throws ServiceException, IOException {
        source = (XMLFileSource) resolveSource(BASE_URL
                + "users/alexander.saar");
        assertNotNull(source);
    }

    public void testReadXMLFile() throws ServiceException, IOException {
        source = (XMLFileSource) resolveSource(BASE_URL
                + "users/alexander.saar");
        assertNotNull(source);

        InputStream is = source.getInputStream();
        assertNotNull(is);

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        int b;
        while ((b = is.read()) != -1) {
            os.write(b);
        }
        System.out.println(new String(os.toByteArray()));
    }

    public void testXMLizableXMLFileSource() throws ServiceException,
            IOException, SAXException, TransformerFactoryConfigurationError, TransformerException {
        source = (XMLFileSource) resolveSource(BASE_URL
                + "users/alexander.saar");
        assertNotNull(source);
        
        SAXTransformerFactory stfactory =
            (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        TransformerHandler handler = stfactory.newTransformerHandler();
        handler.setResult(new StreamResult(System.out));
        source.toSAX(handler);
    }
}
