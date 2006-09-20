/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.source.xml;

import java.io.IOException;

import org.apache.avalon.framework.service.ServiceException;

import com.mindquarry.jcr.source.xml.sources.FileOrFolderSource;

/**
 * Test cases for the FileOrFolderSource implementation.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class FileOrFolderSourceTest extends JCRSourceTestBase {
    private FileOrFolderSource source;

    public void testFolderRetrieval() throws ServiceException, IOException {
        source = (FileOrFolderSource) resolveSource(BASE_URL + "users");
        assertNotNull(source);
    }

    public void testFileRetrieval() throws ServiceException, IOException {
        source = (FileOrFolderSource) resolveSource(BASE_URL
                + "users/alexander.saar");
        assertNotNull(source);
    }
}
