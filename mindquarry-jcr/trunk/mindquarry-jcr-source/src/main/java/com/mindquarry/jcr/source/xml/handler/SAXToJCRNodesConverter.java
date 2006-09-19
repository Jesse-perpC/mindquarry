/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.source.xml.handler;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * A SAX content handler implementation for converting SAX event to JCR nodes.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class SAXToJCRNodesConverter implements ContentHandler {
    /**
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        // TODO Auto-generated method stub
    }

    /**
     * @see org.xml.sax.ContentHandler#endDocument()
     */
    public void endDocument() throws SAXException {
        // TODO Auto-generated method stub
    }

    /**
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {
        // TODO Auto-generated method stub
    }

    /**
     * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
     */
    public void endPrefixMapping(String prefix) throws SAXException {
        // TODO Auto-generated method stub
    }

    /**
     * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
     */
    public void ignorableWhitespace(char[] ch, int start, int length)
            throws SAXException {
        // TODO Auto-generated method stub
    }

    /**
     * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String,
     *      java.lang.String)
     */
    public void processingInstruction(String target, String data)
            throws SAXException {
        // TODO Auto-generated method stub
    }

    /**
     * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
     */
    public void setDocumentLocator(Locator locator) {
        // TODO Auto-generated method stub
    }

    /**
     * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
     */
    public void skippedEntity(String name) throws SAXException {
        // TODO Auto-generated method stub
    }

    /**
     * @see org.xml.sax.ContentHandler#startDocument()
     */
    public void startDocument() throws SAXException {
        // TODO Auto-generated method stub
    }

    /**
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
     *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String namespaceURI, String localName,
            String qName, Attributes atts) throws SAXException {
        // TODO Auto-generated method stub
    }

    /**
     * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String,
     *      java.lang.String)
     */
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        // TODO Auto-generated method stub
    }
}
