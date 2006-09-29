/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.xml.source.handler;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.jcr.NamespaceRegistry;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Workspace;

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
    
    private static final String MQ_PREFIX_START = "mq";
    
    private static final int ELEMENT_DEPTH_START = 0;
    private static final String ELEMENT_DEPTH = "SAXToJCRNodesConverter_ElementDepth";

    private Stack<Map<String, String>> prefixMapStack;
    
    private int nextPrefixNumber = 0;
    
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
        
        prefixMapStack = new Stack<Map<String,String>>();
        pushNewPrefixMap();
        
        //prefixMapStack
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
         
        String elementPrefix = prefix(qName);
        String jcrElementQName = buildJcrQName(elementPrefix, localName);
                
        try {
            node = node.addNode(jcrElementQName, "xt:element");
            for (int i = 0; i < atts.getLength(); i++) {                
                String jcrAttributeQName = buildJcrAttributeQName(i, atts);
                node.setProperty(jcrAttributeQName, atts.getValue(i));
            }
        } catch (Exception e) {
            throw new SAXException("Error while storing content.", e);
        }
        
        incrementElementDepthCounter();
    }
    
    private String buildJcrAttributeQName(int index, Attributes attributes) {
        String prefix = prefix(attributes.getQName(index));
        String localname = attributes.getLocalName(index);
        return buildJcrQName(prefix, localname);
    }
    
    private String buildJcrQName(String prefix, String localname) {
        String jcrPrefix = topPrefixMap().get(prefix);
        
        // for elements without namespace
        if (null == jcrPrefix) 
            return localname;
        
        StringBuilder resultSB = new StringBuilder();
        resultSB.append(jcrPrefix);
        resultSB.append(':');
        resultSB.append(localname);
        return resultSB.toString();
    }
    
    private String prefix(String qName) {
        if (-1 == qName.indexOf(':')) 
            return "".intern();
        else
            return qName.substring(0, qName.indexOf(':'));
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
        
        decrementElementDepthCounter();
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

    /**
     * @see org.xml.sax.helpers.DefaultHandler#startPrefixMapping(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        
        try {
            Workspace ws = node.getSession().getWorkspace();
            NamespaceRegistry nr = ws.getNamespaceRegistry();

                
            String jcrNodePrefix;
            List<String> registeredUris = Arrays.asList(nr.getURIs());
            if (registeredUris.contains(uri)) {
                jcrNodePrefix = nr.getPrefix(uri);
            }
            else {
                int maxPrefixNumber = maxUsedPrefixNumber(nr.getPrefixes());
                if (maxPrefixNumber > nextPrefixNumber)
                    nextPrefixNumber = maxPrefixNumber;
                
                jcrNodePrefix = MQ_PREFIX_START + nextPrefixNumber++;
                nr.registerNamespace(jcrNodePrefix, uri);
            }
            
            if (topPrefixMap().containsKey(prefix))
                pushNewPrefixMap();                

            topPrefixMap().put(prefix, jcrNodePrefix);
            
        } catch (RepositoryException e) {
            throw new SAXException(e);
        }
    }
    
    private int maxUsedPrefixNumber(String[] prefixes) {
        int result = 0;
        for (String prefix : prefixes) {
            if (prefix.startsWith(MQ_PREFIX_START)) {
                String prefixEnd = prefix.substring(MQ_PREFIX_START.length());
                int number = Integer.valueOf(prefixEnd);
                if (number >= nextPrefixNumber) {
                    result = number + 1;
                }
            }
        }
        return result;
    }
    
    /**
     * @see org.xml.sax.helpers.DefaultHandler#endPrefixMapping(java.lang.String)
     */
    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        if (ELEMENT_DEPTH_START == getElementDepthCounter())
            prefixMapStack.pop();
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

    private void decrementElementDepthCounter() {
        setElementDepthCounter(getElementDepthCounter() - 1);
    }

    private void incrementElementDepthCounter() {
        setElementDepthCounter(getElementDepthCounter() + 1);
    }
    
    private int getElementDepthCounter() {
        Map<String, String> prefixMap = topPrefixMap();
        return Integer.valueOf(prefixMap.get(ELEMENT_DEPTH));
    }
    
    private void setElementDepthCounter(int value) {
        Map<String, String> prefixMap = topPrefixMap();
        prefixMap.put(ELEMENT_DEPTH, String.valueOf(value));
    }
    
    private Map<String, String> topPrefixMap() {
        return prefixMapStack.peek();
    }
    
    private Map<String, String> pushNewPrefixMap() {
        Map<String, String> newTopPrefixMap = new HashMap<String, String>();
        if (! prefixMapStack.isEmpty())
            newTopPrefixMap.putAll(topPrefixMap());

        newTopPrefixMap.put(ELEMENT_DEPTH, String.valueOf(ELEMENT_DEPTH_START));
        return prefixMapStack.push(newTopPrefixMap);
    }
}
