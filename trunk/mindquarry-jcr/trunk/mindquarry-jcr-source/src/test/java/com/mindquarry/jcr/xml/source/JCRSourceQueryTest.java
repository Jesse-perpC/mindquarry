/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.xml.source;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

import org.apache.excalibur.source.ModifiableSource;

/**
 * Test cases for the JCRNodeWrapperSource implementation.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JCRSourceQueryTest extends JCRSourceTestBase {
    public void testSimpleQuery() throws Exception {
        JCRNodeWrapperSource source = (JCRNodeWrapperSource) resolveSource(BASE_URL
                + "users/foo.bar");
        assertNotNull(source);

        OutputStream os = ((ModifiableSource) source).getOutputStream();
        assertNotNull(os);

        String newContent = "<user><id>foo.bar</id></user>";
        os.write(newContent.getBytes());
        os.flush();
        os.close();

        QueryResultSource qResult = (QueryResultSource) resolveSource(BASE_URL
                + "users?//user[//id='foo.bar']");
        assertNotNull(qResult);
        
        Collection results = qResult.getChildren();
        assertEquals(1, results.size());
        
        Iterator it = results.iterator();
        while(it.hasNext()) {
            JCRNodeWrapperSource rSrc = (JCRNodeWrapperSource)it.next();
            assertNotNull(rSrc);
        }
    }
}
