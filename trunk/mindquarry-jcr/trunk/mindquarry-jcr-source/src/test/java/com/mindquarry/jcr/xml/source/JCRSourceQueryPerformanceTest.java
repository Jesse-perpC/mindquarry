/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.xml.source;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.apache.excalibur.source.ModifiableSource;
import org.apache.jackrabbit.core.nodetype.InvalidNodeTypeDefException;
import org.apache.jackrabbit.core.nodetype.compact.ParseException;

/**
 * Test cases for the JCRNodeWrapperSource implementation.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JCRSourceQueryPerformanceTest extends JCRSourceTestBase {

    public void testQueryInLargeRepository() throws Exception {

        for (int i = 0; i < 100; i++) {
            JCRNodeWrapperSource dummySource = (JCRNodeWrapperSource) resolveSource(BASE_URL
                    + "users/" + i);
            assertNotNull(dummySource);

            OutputStream os = ((ModifiableSource) dummySource)
                    .getOutputStream();
            assertNotNull(os);

            String dummyContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><user><id>"
                    + i + "</id></user>";
            os.write(dummyContent.getBytes());
            os.flush();
            os.close();
        }
        long startTime = System.currentTimeMillis();
        QueryResultSource qResult = (QueryResultSource) resolveSource(BASE_URL
                + "users?/*[.//id/text/@xt:characters='67']");

        long testTime = System.currentTimeMillis() - startTime;
        System.out.println("Test time: " + testTime + " ms");
        assertNotNull(qResult);

        Collection results = qResult.getChildren();
        assertEquals(1, results.size());
    }
}
