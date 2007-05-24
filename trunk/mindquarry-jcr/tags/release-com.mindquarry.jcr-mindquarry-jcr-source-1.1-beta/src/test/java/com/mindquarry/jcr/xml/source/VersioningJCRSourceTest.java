/*
 * Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.cocoon.components.source.VersionableSource;
import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.core.nodetype.InvalidNodeTypeDefException;
import org.apache.jackrabbit.core.nodetype.compact.ParseException;
import org.custommonkey.xmlunit.Diff;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * Test cases for the JCRNodeWrapperSource implementation.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class VersioningJCRSourceTest extends JCRSourceTestBase {
    
    private static final String CONTENT_FILE = "/com/mindquarry/jcr/xml/source/ComplexContent.xml";
    private static final String CONTENT2_FILE = "/com/mindquarry/jcr/xml/source/ComplexContentV2.xml";
    
    public void testIsVersionableSource() throws ServiceException, IOException {
    	JCRNodeSource emptySource = loadTestSource();
    	assertTrue(emptySource instanceof VersionableSource);
    }
    
    public void testGetSourceRevisionBranch() throws ServiceException, IOException {
    	JCRNodeSource emptySource = loadTestSource();
    	assertNotNull(emptySource.getSourceRevisionBranch());
    }
    
    public void testIsVersioned() throws ServiceException, IOException {
    	JCRNodeSource emptySource = loadTestSource();
    	assertTrue(emptySource.isVersioned());
    	
    	OutputStream sourceOut = emptySource.getOutputStream();
        assertNotNull(sourceOut);

        InputStream contentIn = getClass().getResourceAsStream(CONTENT_FILE);
        try {
            IOUtils.copy(contentIn, sourceOut);        
            sourceOut.flush();
        } finally {
            sourceOut.close();
            contentIn.close();
        }
        
        assertTrue(emptySource.isVersioned());
    }
    
    public void testRevcounter() throws ServiceException, IOException {
    	JCRNodeSource emptySource = loadTestSource();

    	for (int i=0;i<3;i++) {
	    	OutputStream sourceOut = emptySource.getOutputStream();
	
	        InputStream contentIn = getClass().getResourceAsStream(CONTENT_FILE);
	        try {
	            IOUtils.copy(contentIn, sourceOut);        
	            sourceOut.flush();
	        } finally {
	            sourceOut.close();
	            contentIn.close();
	        }
	        System.out.println(emptySource.getLatestSourceRevision());
    	}
    	
    	String testSourceUri = BASE_URL + "users/lars.trieloff?revision=1.1";
    	JCRNodeSource secondSource = (JCRNodeSource) resolveSource(testSourceUri);
    	
    	System.out.println("Created at: " + secondSource.getSourceRevision());
    	
    	for (int i=0;i<3;i++) {
	    	OutputStream sourceOut = emptySource.getOutputStream();
	
	        InputStream contentIn = getClass().getResourceAsStream(CONTENT_FILE);
	        try {
	            IOUtils.copy(contentIn, sourceOut);        
	            sourceOut.flush();
	        } finally {
	            sourceOut.close();
	            contentIn.close();
	        }
	        System.out.println(emptySource.getLatestSourceRevision());
    	}
    	

    	System.out.println("Read again at:" + secondSource.getSourceRevision());
        
    	assertNotNull(emptySource.getSourceRevision());
    }
    
    public void testGetOldVersion() throws ServiceException, IOException, SAXException, ParserConfigurationException {
    	JCRNodeSource emptySource = loadTestSource();

    	for (int i=0;i<3;i++) {
	    	OutputStream sourceOut = emptySource.getOutputStream();
	
	        InputStream contentIn = getClass().getResourceAsStream(CONTENT_FILE);
	        try {
	            IOUtils.copy(contentIn, sourceOut);        
	            sourceOut.flush();
	        } finally {
	            sourceOut.close();
	            contentIn.close();
	        }
    	}
    	
    	String testSourceUri = BASE_URL + "users/lars.trieloff?revision=1.1";
    	JCRNodeSource secondSource = (JCRNodeSource) resolveSource(testSourceUri);
    	
    	System.out.println("Read again at:" + secondSource.getSourceRevision());
        
    	InputStream expected = emptySource.getInputStream();
    	InputStream actual = secondSource.getInputStream();
    	assertTrue(isXmlEqual(expected, actual));
    }
    
    public void testCreateNewXMLFile() throws InvalidNodeTypeDefException,
            ParseException, Exception {
         
        JCRNodeSource emptySource = loadTestSource();        
        assertEquals(false, emptySource.exists());

        OutputStream sourceOut = emptySource.getOutputStream();
        assertNotNull(sourceOut);

        InputStream contentIn = getClass().getResourceAsStream(CONTENT_FILE);
        try {
            IOUtils.copy(contentIn, sourceOut);        
            sourceOut.flush();
        } finally {
            sourceOut.close();
            contentIn.close();
        }        
        InputStream contentIn2 = getClass().getResourceAsStream(CONTENT2_FILE);
        sourceOut = emptySource.getOutputStream();
        try {
            IOUtils.copy(contentIn2, sourceOut);        
            sourceOut.flush();
        } finally {
            sourceOut.close();
            contentIn2.close();
        }   
        
        
        
        InputStream expected = getClass().getResourceAsStream(CONTENT2_FILE);
        
        JCRNodeSource persistentSource = loadTestSource();
        assertEquals(true, persistentSource.exists());
        InputStream actual = persistentSource.getInputStream();
       
        try {
            assertTrue(isXmlEqual(expected, actual));
        } finally {
            expected.close();
            actual.close();
        }
        JCRNodeSource tmpSrc = (JCRNodeSource) resolveSource(BASE_URL + "users/alexander.saar");
        persistentSource.delete();
        tmpSrc.delete();
    }
    
    private boolean isXmlEqual(InputStream expected, InputStream actual)
        throws SAXException, IOException, ParserConfigurationException {
        
        InputSource expectedSource = new InputSource(expected);
        InputSource actualSource = new InputSource(actual);
        Diff xmlDiff = new Diff(expectedSource, actualSource);
        return xmlDiff.similar();
    }
    
    private JCRNodeSource loadTestSource()
        throws ServiceException, IOException {
        
        JCRNodeSource result;
        
        String testSourceUri = BASE_URL + "users/lars.trieloff"; 
        result = (JCRNodeSource) resolveSource(testSourceUri);
        assertNotNull(result);
        
        return result;
    }
}
