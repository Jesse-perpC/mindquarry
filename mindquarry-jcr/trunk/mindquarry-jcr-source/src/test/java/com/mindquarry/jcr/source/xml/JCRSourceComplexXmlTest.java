/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.source.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.core.nodetype.InvalidNodeTypeDefException;
import org.apache.jackrabbit.core.nodetype.compact.ParseException;
import org.custommonkey.xmlunit.Diff;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mindquarry.jcr.xml.source.JCRNodeWrapperSource;


/**
 * Test cases for the JCRNodeWrapperSource implementation.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JCRSourceComplexXmlTest extends JCRSourceTestBase {
    
    private static final String CONTENT_FILE = "/com/mindquarry/jcr/source/xml/ComplexContent.xml";
    
    public void testCreateNewXMLFile() throws InvalidNodeTypeDefException,
            ParseException, Exception {
         
        JCRNodeWrapperSource emptySource = loadTestSource();        
        assertEquals(false, emptySource.exists());

        OutputStream sourceOut = emptySource.getOutputStream();
        assertNotNull(sourceOut);

        InputStream contentIn = getClass().getResourceAsStream(CONTENT_FILE);
        try {
            IOUtils.copy(contentIn, sourceOut);        
            sourceOut.flush();
        } finally {
            sourceOut.close();
            contentIn.close();
        }        
        
        InputStream expected = getClass().getResourceAsStream(CONTENT_FILE);
        
        JCRNodeWrapperSource persistentSource = loadTestSource();
        assertEquals(true, persistentSource.exists());
        InputStream actual = persistentSource.getInputStream();
       
        try {
            assertTrue(isXmlEqual(expected, actual));
        } finally {
            expected.close();
            actual.close();
        }        
    }
    
    private boolean isXmlEqual(InputStream expected, InputStream actual)
        throws SAXException, IOException, ParserConfigurationException {
        
        InputSource expectedSource = new InputSource(expected);
        InputSource actualSource = new InputSource(actual);
        Diff xmlDiff = new Diff(expectedSource, actualSource);
        return xmlDiff.similar();
    }
    
    private JCRNodeWrapperSource loadTestSource()
        throws ServiceException, IOException {
        
        JCRNodeWrapperSource result;
        
        String testSourceUri = BASE_URL + "users/lars.trieloff"; 
        result = (JCRNodeWrapperSource) resolveSource(testSourceUri);
        assertNotNull(result);
        
        return result;
    }
}
