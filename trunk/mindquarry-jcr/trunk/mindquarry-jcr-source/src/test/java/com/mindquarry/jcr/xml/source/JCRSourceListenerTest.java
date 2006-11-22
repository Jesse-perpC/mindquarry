/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.xml.source;

import java.io.OutputStream;
import java.util.Collection;

import org.apache.excalibur.source.ModifiableSource;

/**
 * Test cases for the JCRNodeWrapperSource implementation.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JCRSourceListenerTest extends JCRSourceTestBase {

    public void testQueryInLargeRepository() throws Exception {
        JCRNodeWrapperSource src = (JCRNodeWrapperSource) resolveSource(BASE_URL
                + "users/1");
        OutputStream os = ((ModifiableSource) src).getOutputStream();
        String dummyContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><user><id>1</id></user>";
        os.write(dummyContent.getBytes());
        os.flush();
        os.close();
        
        src = (JCRNodeWrapperSource) resolveSource(BASE_URL
                + "users/2");
        os = ((ModifiableSource) src).getOutputStream();
        dummyContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><user><id>2</id></user>";
        os.write(dummyContent.getBytes());
        os.flush();
        os.close();
        
        src = (JCRNodeWrapperSource) resolveSource(BASE_URL
                + "users/2");
        os = ((ModifiableSource) src).getOutputStream();
        dummyContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><user><id>2</id><name>dummy</name></user>";
        os.write(dummyContent.getBytes());
        os.flush();
        os.close();
        
        src.delete();
        
        // create and delete folder
        src = (JCRNodeWrapperSource) resolveSource(BASE_URL
                + "users/test");
        src.makeCollection();
        src = (JCRNodeWrapperSource) resolveSource(BASE_URL
                + "users/test");
        src.delete();
    }
}
