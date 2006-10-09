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
public class JCRSourceQueryTest extends JCRSourceTestBase {
    
    public void testSimpleQuery() throws Exception {
        JCRNodeWrapperSource dummySource = (JCRNodeWrapperSource) resolveSource(BASE_URL
                + "users/alexander.klimetschek");
        assertNotNull(dummySource);

        OutputStream os = ((ModifiableSource) dummySource).getOutputStream();
        assertNotNull(os);

        String dummyContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><user><id>alexander</id><teamspace>cyclr</teamspace><teamspace>mindquarryTooLong</teamspace></user>";
        os.write(dummyContent.getBytes());
        os.flush();
        os.close();

        JCRNodeWrapperSource source = (JCRNodeWrapperSource) resolveSource(BASE_URL
                + "users/bastian");
        assertNotNull(source);

        os = ((ModifiableSource) source).getOutputStream();
        assertNotNull(os);

        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><user><id>bastian</id><teamspace>mindquarry</teamspace></user>";
        os.write(content.getBytes());
        os.flush();
        os.close();
        
        QueryResultSource qResult = (QueryResultSource) resolveSource(BASE_URL
                + "users?/*[.//teamspace/text/@xt:characters='mindquarry']");
        
        assertNotNull(qResult);

        Collection results = qResult.getChildren();
        assertEquals(1, results.size());

        Iterator it = results.iterator();
        JCRNodeWrapperSource rSrc = (JCRNodeWrapperSource) it.next();
        InputStream rSrcIn = rSrc.getInputStream();

        ByteArrayOutputStream actualOut = new ByteArrayOutputStream();
        IOUtils.copy(rSrcIn, actualOut);
        rSrcIn.close();

        assertEquals(content, actualOut.toString());
        actualOut.close();

        rSrc.delete();
    }

    public void testQueryForBinary() throws InvalidNodeTypeDefException,
            ParseException, Exception {

        JCRNodeWrapperSource source = (JCRNodeWrapperSource) resolveSource(BASE_URL
                + "images/photo.png");

        assertNotNull(source);
        assertEquals(false, source.exists());

        OutputStream os = source.getOutputStream();
        assertNotNull(os);

        String content = "foo is a bar";
        os.write(content.getBytes());
        os.flush();
        os.close();

        QueryResultSource qResult = (QueryResultSource) resolveSource(BASE_URL
                + "images?/*[contains(local-name(.), 'photo')]");
        assertNotNull(qResult);

        Collection results = qResult.getChildren();
        assertEquals(1, results.size());

        Iterator it = results.iterator();
        JCRNodeWrapperSource rSrc = (JCRNodeWrapperSource) it.next();
        InputStream rSrcIn = rSrc.getInputStream();

        ByteArrayOutputStream actualOut = new ByteArrayOutputStream();
        IOUtils.copy(rSrcIn, actualOut);
        rSrcIn.close();

        assertEquals(content, actualOut.toString());
        actualOut.close();

        rSrc.delete();
    }
}
