/*
 * Copyright 2006 Mindquarry GmbH, Potsdam, Germany
 */
package org.apache.excalibur.source.impl.jcr.xml;

import javax.jcr.Node;
import javax.jcr.Session;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Converts SAX events (aka XML) provided via the <code>ContentHandler</code>
 * interface into write operations an JCR repository. Will support versioning.
 * 
 * @author alexander.klimetschek@mindquarry.com
 *
 */
public class SAXtoJCRNodesConverter implements ContentHandler {
    
    protected Session session;
    
    protected Node node;

    public SAXtoJCRNodesConverter(Session session, Node node) {
        this.session = session;
        this.node = node;
    }

    public void startDocument() throws SAXException {
        // TODO Auto-generated method stub

    }

    public void endDocument() throws SAXException {
        // TODO Auto-generated method stub

    }

    public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException {
        // TODO Auto-generated method stub
        
        // 1. find node or create it (qName vs. localName vs. JCR namespaces?)
        // 2. put node on stack
        // 3. for each attribute
        //  a. find it or create it

    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        // TODO Auto-generated method stub

    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        // TODO Auto-generated method stub

    }

    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        // TODO Auto-generated method stub

    }

    public void endPrefixMapping(String prefix) throws SAXException {
        // TODO Auto-generated method stub

    }

    public void setDocumentLocator(Locator locator) {
        // not of interest for us
    }

    public void ignorableWhitespace(char[] ch, int start, int length)
            throws SAXException {
        // not of interest for us
    }

    public void processingInstruction(String target, String data)
            throws SAXException {
        // not of interest for us
    }

    public void skippedEntity(String name) throws SAXException {
        // not of interest for us
    }

}
