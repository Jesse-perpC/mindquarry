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
                + "users?/*[.//user/id='67']");

        long testTime = System.currentTimeMillis() - startTime;
        System.out.println("Test time: " + testTime + " ms");
        assertNotNull(qResult);

        Collection results = qResult.getChildren();
        assertEquals(1, results.size());
    }
}
