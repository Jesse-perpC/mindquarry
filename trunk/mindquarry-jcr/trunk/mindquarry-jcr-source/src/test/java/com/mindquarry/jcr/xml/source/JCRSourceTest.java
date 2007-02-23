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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.excalibur.source.ModifiableSource;
import org.apache.excalibur.source.ModifiableTraversableSource;
import org.apache.jackrabbit.core.nodetype.InvalidNodeTypeDefException;
import org.apache.jackrabbit.core.nodetype.compact.ParseException;
import org.xml.sax.SAXException;


/**
 * Test cases for the JCRNodeWrapperSource implementation.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JCRSourceTest extends JCRSourceTestBase {
    private JCRNodeSource source;

    public void testExists() throws ServiceException, IOException {
        // test with no existing file
        source = (JCRNodeSource) resolveSource(BASE_URL
                + "users/lars.trieloff");
        assertNotNull(source);
        assertEquals(false, source.exists());

        // test with existing file
        source = (JCRNodeSource) resolveSource(BASE_URL
                + "users/alexander.saar");
        assertNotNull(source);
        assertEquals(true, source.exists());

        // test with not existing directory
        source = (JCRNodeSource) resolveSource(BASE_URL + "users/newDir");
        assertNotNull(source);
        assertEquals(false, source.exists());

        // test with existing directory
        source = (JCRNodeSource) resolveSource(BASE_URL
                + "users/alexander.saar");
        assertNotNull(source);
        assertEquals(true, source.exists());
    }

    public void testUri() throws ServiceException, IOException {
        // test if an absolute uri is the same as the requested one (file)
        String requestedAbsoluteUri = BASE_URL + "users/lars.trieloff";
        source = (JCRNodeSource) resolveSource(requestedAbsoluteUri);
        assertNotNull(source);
        assertEquals(requestedAbsoluteUri, source.getURI());

        // test if an absolute uri is the same as the requested one (directory)
        requestedAbsoluteUri = BASE_URL + "users";
        source = (JCRNodeSource) resolveSource(requestedAbsoluteUri);
        assertNotNull(source);
        assertEquals(requestedAbsoluteUri, source.getURI());
    }
    
    public void testMakeCollection() throws Exception {
        // test folder creation
        source = (JCRNodeSource) resolveSource(BASE_URL
                + "newDirectory/subDirectory");
        assertNotNull(source);
        
        ((ModifiableTraversableSource) source).makeCollection();
        
        assertEquals(true, source.exists());
        
        source = (JCRNodeSource) resolveSource(BASE_URL
                + "newDirectory/subDirectory");
        assertEquals(true, source.exists());
    }

    public void testDelete() throws ServiceException, IOException {
        // test file deletion
        source = (JCRNodeSource) resolveSource(BASE_URL
                + "users/alexander.saar");
        assertNotNull(source);
        assertEquals(true, source.exists());
        source.delete();

        source = (JCRNodeSource) resolveSource(BASE_URL
                + "users/alexander.saar");
        assertNotNull(source);
        assertEquals(false, source.exists());

        // test folder deletion
        source = (JCRNodeSource) resolveSource(BASE_URL + "users");
        assertNotNull(source);
        assertEquals(true, source.exists());
        source.delete();

        source = (JCRNodeSource) resolveSource(BASE_URL + "users");
        assertNotNull(source);
        assertEquals(false, source.exists());
    }

    public void testReadXMLFile() throws ServiceException, IOException {
        source = (JCRNodeSource) resolveSource(BASE_URL
                + "users/alexander.saar");
        assertNotNull(source);

        InputStream is = source.getInputStream();
        assertNotNull(is);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        int b;
        while ((b = is.read()) != -1) {
            os.write(b);
        }
        System.out.println(new String(os.toByteArray()));
    }

    public void testWriteXMLFile() throws ServiceException, IOException,
            TransformerConfigurationException, SAXException, LoginException,
            RepositoryException {
        source = (JCRNodeSource) resolveSource(BASE_URL
                + "users/alexander.saar");
        assertNotNull(source);

        OutputStream os = ((ModifiableSource) source).getOutputStream();
        assertNotNull(os);

        String newContent = "<user><name>Alexander Saar der Zweite</name></user>";
        os.write(newContent.getBytes());
        os.flush();
        os.close();

        source = (JCRNodeSource) resolveSource(BASE_URL
                + "users/alexander.saar");
        assertNotNull(source);

        SAXTransformerFactory stfactory = (SAXTransformerFactory) SAXTransformerFactory
                .newInstance();
        TransformerHandler handler = stfactory.newTransformerHandler();
        handler.setResult(new StreamResult(System.out));
        source.toSAX(handler);

        // need to do this for better output formatting
        System.out.println();
    }

    public void testCreateNewXMLFile() throws InvalidNodeTypeDefException,
            ParseException, Exception {
        source = (JCRNodeSource) resolveSource(BASE_URL
                + "users/lars.trieloff");
        assertNotNull(source);
        assertEquals(false, source.exists());

        OutputStream os = source.getOutputStream();
        assertNotNull(os);

        String newContent = "<user><name>Lars Trieloff</name><mail type=\"business\">lars.trieloff@mindquarry.com</mail></user>";
        os.write(newContent.getBytes());
        os.flush();
        os.close();

        source = (JCRNodeSource) resolveSource(BASE_URL
                + "users/lars.trieloff");
        assertNotNull(source);
        assertEquals(true, source.exists());

        SAXTransformerFactory stfactory = (SAXTransformerFactory) SAXTransformerFactory
                .newInstance();
        TransformerHandler handler = stfactory.newTransformerHandler();
        handler.setResult(new StreamResult(System.out));
        source.toSAX(handler);

        // need to do this for better output formatting
        System.out.println();
    }

    public void testCreateNewXMLFileInNewDirectory()
            throws InvalidNodeTypeDefException, ParseException, Exception {
        source = (JCRNodeSource) resolveSource(BASE_URL + "foo/bar");
        assertNotNull(source);
        assertEquals(false, source.exists());

        OutputStream os = source.getOutputStream();
        assertNotNull(os);

        String newContent = "<foo>bar</foo>";
        os.write(newContent.getBytes());
        os.flush();
        os.close();

        source = (JCRNodeSource) resolveSource(BASE_URL + "foo/bar");
        assertNotNull(source);
        assertEquals(true, source.exists());

        SAXTransformerFactory stfactory = (SAXTransformerFactory) SAXTransformerFactory
                .newInstance();
        TransformerHandler handler = stfactory.newTransformerHandler();
        handler.setResult(new StreamResult(System.out));
        source.toSAX(handler);

        // need to do this for better output formatting
        System.out.println();
    }

    public void testCreateNewBinaryFileInNewDirectory()
            throws InvalidNodeTypeDefException, ParseException, Exception {
        source = (JCRNodeSource) resolveSource(BASE_URL + "foo/bar");
        assertNotNull(source);
        assertEquals(false, source.exists());

        OutputStream os = source.getOutputStream();
        assertNotNull(os);

        String newContent = "foo is a bar";
        os.write(newContent.getBytes());
        os.flush();
        os.close();

        source = (JCRNodeSource) resolveSource(BASE_URL + "foo/bar");
        assertNotNull(source);
        assertEquals(true, source.exists());

        InputStream is = source.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            int b;
            while ((b = is.read()) != -1) {
                bos.write(b);
            }
            System.out.println(new String(bos.toByteArray()));
        } finally {
            is.close();
            bos.close();
        }
    }

    public void testXMLizableSource() throws ServiceException, IOException,
            SAXException, TransformerFactoryConfigurationError,
            TransformerException {
        source = (JCRNodeSource) resolveSource(BASE_URL
                + "users/alexander.saar");
        assertNotNull(source);

        SAXTransformerFactory stfactory = (SAXTransformerFactory) SAXTransformerFactory
                .newInstance();
        TransformerHandler handler = stfactory.newTransformerHandler();
        handler.setResult(new StreamResult(System.out));
        source.toSAX(handler);

        // need to do this for better output formatting
        System.out.println();
    }

    public void testGetFileInFolder() throws ServiceException, IOException,
            TransformerConfigurationException, SAXException {
        // test with no existing file
        source = (JCRNodeSource) resolveSource(BASE_URL + "users");
        assertNotNull(source);
        assertEquals(true, source.exists());

        source = (JCRNodeSource) source.getChild("alexander.saar");
        assertEquals(true, source.exists());

        SAXTransformerFactory stfactory = (SAXTransformerFactory) SAXTransformerFactory
                .newInstance();
        TransformerHandler handler = stfactory.newTransformerHandler();
        handler.setResult(new StreamResult(System.out));
        source.toSAX(handler);

        // need to do this for better output formatting
        System.out.println();
    }

    public void testGetFilesFromFolder() throws ServiceException, IOException,
            TransformerConfigurationException, SAXException {
        // test with no existing file
        source = (JCRNodeSource) resolveSource(BASE_URL + "users");
        assertNotNull(source);
        assertEquals(true, source.exists());

        Collection childs = source.getChildren();
        assertEquals(1, childs.size());

        Iterator it = childs.iterator();
        while (it.hasNext()) {
            source = (JCRNodeSource) it.next();
            assertEquals(true, source.exists());

            SAXTransformerFactory stfactory = (SAXTransformerFactory) SAXTransformerFactory
                    .newInstance();
            TransformerHandler handler = stfactory.newTransformerHandler();
            handler.setResult(new StreamResult(System.out));
            source.toSAX(handler);
        }
        // need to do this for better output formatting
        System.out.println();
    }
}
