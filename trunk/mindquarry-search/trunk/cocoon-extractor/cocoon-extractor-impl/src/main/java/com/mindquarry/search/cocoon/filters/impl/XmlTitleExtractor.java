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
package com.mindquarry.search.cocoon.filters.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.slide.extractor.ExtractorException;
import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.mindquarry.search.cocoon.filters.TitleExtractor;

/**
 * Finds the first <code>title</code> element in an XML document and returns
 * that as the title for the document.
 * 
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 *
 */
public class XmlTitleExtractor implements TitleExtractor {

    private final SAXParserFactory parserFactory;

    private final EntityResolverImpl entityResolver = new EntityResolverImpl();

    public XmlTitleExtractor() {
        parserFactory = SAXParserFactory.newInstance();
        parserFactory.setValidating(false);
    }

    public Reader extract(InputStream stream) throws ExtractorException {
        TitleCollector collector = new TitleCollector();

        try {
            SAXParser parser = parserFactory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(collector);
            reader.setErrorHandler(collector);
            reader.setEntityResolver(this.entityResolver);

            InputSource source = new InputSource(stream);
            //source.setSystemId("/slide");
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

    private static class TitleCollector extends DefaultHandler {
        StringBuffer buffer = new StringBuffer();

        SAXParseException exception = null;
        
        boolean recordCharacters = false;
        boolean done = false;
        
        public boolean isTitleElement(String uri, String localName, String qName) {
            return ("title".equalsIgnoreCase(localName) || "title".equalsIgnoreCase(qName));
        }

        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (!this.done && isTitleElement(uri, localName, qName)) {
                this.recordCharacters = true;
            }
        }

        public void characters(char[] ch, int start, int length)
                throws SAXException {
            if (this.recordCharacters) {
                this.buffer.append(ch, start, length);                
            }
        }

        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            if (isTitleElement(uri, localName, qName)) {
                this.recordCharacters = false;
                this.done = true;
            } else if (this.recordCharacters) {
                // if the title element contains elements itself...
                // each end tag breaks words
                this.buffer.append(' ');
            }
        }

        public void error(SAXParseException e) throws SAXException {
            this.exception = e;
        }

        public void fatalError(SAXParseException e) throws SAXException {
            this.exception = e;
        }
    }

    private static class EntityResolverImpl implements EntityResolver {

        public InputSource resolveEntity(String publicId, String systemId)
                throws SAXException, IOException {
            return new InputSource(new StringReader(""));
        }
    }
}
