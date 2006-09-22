/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.xml.source.handler;

import java.util.GregorianCalendar;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A SAX content handler implementation for converting SAX event to JCR nodes.
 * 
 * @author <a
 *         href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">Alexander
 *         Klimetschek</a>
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class SAXToJCRNodesConverter extends DefaultHandler {
    private Node node;

    /**
     * Default constructor.
     * 
     * @param node the node representing the nt:file parent of this document
     * @throws PathNotFoundException
     * @throws RepositoryException
     */
    public SAXToJCRNodesConverter(Node node) throws PathNotFoundException,
            RepositoryException {
        this.node = node.getNode("jcr:content");
    }

    // =========================================================================
    // ContentHandler interface methods
    // =========================================================================

    /**
     * {@inheritDoc}
     * 
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
     *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException {
        try {
            node = node.addNode(qName, "xt:element");
            for (int i = 0; i < atts.getLength(); i++) {
                node.setProperty(atts.getLocalName(i), atts.getValue(i));
            }
        } catch (Exception e) {
            throw new SAXException("Error while storing content.", e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        try {
            Node text = node.addNode("text", "xt:text");
            text.setProperty("xt:characters", new String(getTextContent(ch,
                    start, length)));
        } catch (Exception e) {
            throw new SAXException("Error while storing content.", e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        try {
            node = node.getParent();
        } catch (Exception e) {
            throw new SAXException("Error while storing content.", e);
        }
    }

    /**
     * @see org.xml.sax.helpers.DefaultHandler#endDocument()
     */
    @Override
    public void endDocument() throws SAXException {
        try {
            node.setProperty("jcr:lastModified", new GregorianCalendar());
        } catch (Exception e) {
            throw new SAXException("Error while storing content.", e);
        }
    }

    // =========================================================================
    // private methods
    // =========================================================================

    public static char[] getTextContent(char[] a, int start, int length) {
        char[] result = new char[length];
        for (int i = 0; i < length; i++) {
            result[i] = a[start + i];
        }
        return result;
    }
}
