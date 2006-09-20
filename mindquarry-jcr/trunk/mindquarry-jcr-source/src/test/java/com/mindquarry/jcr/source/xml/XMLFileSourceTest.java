/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.source.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.avalon.framework.service.ServiceException;

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
        while((b = is.read()) != -1) {
            os.write(b);
        }
        System.out.println(new String(os.toByteArray()));
    }
}
