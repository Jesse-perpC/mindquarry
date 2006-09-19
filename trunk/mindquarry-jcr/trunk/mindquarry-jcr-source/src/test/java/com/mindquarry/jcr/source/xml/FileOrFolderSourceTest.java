/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.source.xml;

import java.io.IOException;
import java.io.InputStream;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.TraversableSource;
import org.apache.excalibur.source.impl.validity.NOPValidity;

import com.mindquarry.jcr.source.xml.sources.FileOrFolderSource;

/**
 * Test cases for the FileOrFolderSource implementation.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class FileOrFolderSourceTest extends JCRSourceTestBase {

    private static final int CONTENT_LENGTH = 25487;

    private FileOrFolderSource source;

    protected void setUp() throws Exception {
        super.setUp();
        source = (FileOrFolderSource) resolveSource(BASE_URL);
    }

    public void testExists() {
        assertTrue(source.exists());
    }

    public void testGetContentLength() {
        assertEquals(CONTENT_LENGTH, source.getContentLength());
    }

    public void testGetInputStream() throws SourceNotFoundException,
            IOException {
        InputStream is = source.getInputStream();
        assertNotNull(is);
        assertEquals(CONTENT_LENGTH, is.available());
    }

    public void testGetLastModified() {
        assertEquals(1142713394155l, source.getLastModified());
    }

    public void testGetMimeType() {
        assertEquals("text/plain", source.getMimeType());
    }

    public void testGetScheme() {
        assertEquals(SCHEME, source.getScheme());
    }

    public void testGetURI() {
        assertEquals(BASE_URL, source.getURI());
    }

    public void testGetValidity() {
        SourceValidity val = source.getValidity();
        assertTrue(val instanceof NOPValidity);
    }

    public void testGetChild() {
        try {
            assertNull(source.getChild(null));
        } catch (SourceException e) {
            fail();
        }
    }

    public void testGetChildren() {
        try {
            assertNull(source.getChildren());
        } catch (SourceException e) {
            fail();
        }
    }

    public void testGetName() {
        assertEquals(BASE_URL, source.getName());
    }

    public void testGetParent() throws SourceException {
        Source parent = source.getParent();
        assertTrue(parent instanceof TraversableSource);
        assertEquals("tests", ((TraversableSource) parent).getName());
    }

    public void testIsCollection() {
        assertFalse(source.isCollection());
    }
}
