/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright 2004 The Apache Software Foundation 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.mindquarry.search.cocoon.filters.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.slide.extractor.AbstractContentExtractor;
import org.apache.slide.extractor.ExtractorException;
import org.apache.slide.extractor.SimpleXmlExtractor;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Content extractor that simply extracts all text content of an
 * XML document.
 */
public class XmlContentExtractor extends AbstractContentExtractor
{
    private final SAXParserFactory parserFactory;
    private final EntityResolverImpl entityResolver = new EntityResolverImpl();
    
    public XmlContentExtractor(String uri, String contentType)
    {
        super(uri, contentType);
        parserFactory = SAXParserFactory.newInstance();
        parserFactory.setValidating(false);
    }

    public XmlContentExtractor(String uri, String contentType, String namespace)
    {
        super(uri, contentType, namespace);
        parserFactory = SAXParserFactory.newInstance();
        parserFactory.setValidating(false);
    }

    public Reader extract(InputStream content) throws ExtractorException
    {
        TextCollector collector = new TextCollector();

        try {
            SAXParser parser = parserFactory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(collector);
            reader.setErrorHandler(collector);
            reader.setEntityResolver(this.entityResolver);
            
            InputSource source = new InputSource(content);
            source.setSystemId("/slide");
            reader.parse(source);
        } catch (ParserConfigurationException e) {
            throw new ExtractorException(e.toString());
        } catch (SAXException e) {
            throw new ExtractorException(e.toString());
        } catch (IOException e) {
            throw new ExtractorException(e.toString());
        }
        
        if (collector.exception != null) {
            throw new ExtractorException(collector.exception.toString());
        }
        
        return new StringReader(collector.buffer.toString());
    }
    
    private static class TextCollector extends DefaultHandler {
        StringBuffer buffer = new StringBuffer();
        SAXParseException exception = null;
        
        public void characters(char[] ch, int start, int length)
                throws SAXException
        {
            this.buffer.append(ch, start, length);
        }
        public void endElement(String uri, String localName, String qName)
                throws SAXException
        {
            // each end tag breaks words, TODO make this configurable
            this.buffer.append(' ');
        }
        public void error(SAXParseException e) throws SAXException
        {
            this.exception = e;
        }
        public void fatalError(SAXParseException e) throws SAXException
        {
            this.exception = e;
        }
    }
    
    private static class EntityResolverImpl implements EntityResolver {
        
        public InputSource resolveEntity(String publicId, String systemId)
                throws SAXException, IOException
        {
            return new InputSource(new StringReader(""));
        }
    }

    // HACK: copied from latest version of SimpleXmlExtractor
    
    static final String CONTENT_TYPE_XML = "text/xml";
    static final String CONTENT_TYPE_XHTML = "application/xhtml+xml";
    //html also because xhtml can and most often has the html content type
    static final String CONTENT_TYPE_HTML = "text/html";
    static final String CONTENT_TYPE_XML_ALL_CSV = CONTENT_TYPE_XML+","+CONTENT_TYPE_XHTML+","+CONTENT_TYPE_HTML;
    
    /* (non-Javadoc)
     * @see org.apache.slide.extractor.Extractor#getContentType()
     */
    public String getContentType() {
        if(super.getContentType()==null){
            return CONTENT_TYPE_XML_ALL_CSV;
        }   
        return super.getContentType();
    }
}
