/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.source.xml;

import com.mindquarry.jcr.source.xml.sources.XMLFileSource;

/**
 * Test cases for the XMLFileSource implementation.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class XMLFileSourceTest extends JCRSourceTestBase {
    private XMLFileSource source;

    protected void setUp() throws Exception {
        super.setUp();
        source = (XMLFileSource) resolveSource(BASE_URL);
    }
}
