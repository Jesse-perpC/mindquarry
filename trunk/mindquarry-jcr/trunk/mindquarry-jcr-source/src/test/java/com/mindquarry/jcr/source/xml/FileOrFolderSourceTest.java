/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.source.xml;

import com.mindquarry.jcr.source.xml.sources.FileOrFolderSource;

/**
 * Test cases for the FileOrFolderSource implementation.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class FileOrFolderSourceTest extends JCRSourceTestBase {
    private FileOrFolderSource source;

    protected void setUp() throws Exception {
        super.setUp();
        source = (FileOrFolderSource) resolveSource(BASE_URL);
    }
    
    public void testTheTest() {
        
    }
}
