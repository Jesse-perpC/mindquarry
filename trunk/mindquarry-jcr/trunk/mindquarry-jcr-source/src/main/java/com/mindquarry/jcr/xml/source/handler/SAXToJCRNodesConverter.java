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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mindquarry.jcr.xml.source.JCRConstants;
import com.mindquarry.jcr.xml.source.JCRSourceFactory;

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
    
    // used to handle namespace (prefix) declarations
    private static final int ELEMENT_DEPTH_START = 0;
    private static final String ELEMENT_DEPTH = "SAXToJCRNodesConverter_ElementDepth";

    private Stack<Map<String, String>> prefixMapStack;
    
    private int nextPrefixNumber = 0;
    
    private Node node;
    
    /**
     * Default constructor.
     * 
     * @param node the node representing the jcr:content node
     * @param configured namespace mappings
     * @throws PathNotFoundException
     * @throws RepositoryException
     */
    public SAXToJCRNodesConverter(Node contentNode) {
        
        prefixMapStack = new Stack<Map<String,String>>();
        pushNewPrefixMap();
        
        this.node = contentNode;
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
            node = node.addNode(jcrElementQName, JCRConstants.XT_ELEMENT);
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
        
        // if element is in default namespace
        // return localname only
        if (null == jcrPrefix || "".intern().equals(jcrPrefix)) 
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
            Node text = node.addNode(JCRConstants.TEXT, JCRConstants.XT_TEXT);
            text.setProperty(JCRConstants.XT_CHARACTERS, new String(getTextContent(ch,
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
        
        if (ELEMENT_DEPTH_START == getElementDepthCounter())
            prefixMapStack.pop();
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
            NamespaceRegistry nsReg = getNamespaceRegistry();
            
            String jcrNodePrefix;
            List<String> registeredUris = 
                Arrays.asList(nsReg.getURIs());
            
            if (registeredUris.contains(uri)) {
                jcrNodePrefix = nsReg.getPrefix(uri);
            }
            else {                
                if (configuredMappings().containsKey(uri)) {
                    jcrNodePrefix = configuredMappings().get(uri);
                } else {                    
                    jcrNodePrefix = generateUniquePrefix(nsReg);
                }                
                nsReg.registerNamespace(jcrNodePrefix, uri);
            }
            
            if (topPrefixMap().containsKey(prefix))
                pushNewPrefixMap();                

            topPrefixMap().put(prefix, jcrNodePrefix);
            
        } catch (RepositoryException e) {
            throw new SAXException(e);
        }
    }

    private String generateUniquePrefix(NamespaceRegistry nsReg) 
        throws RepositoryException {

        int maxPrefixNumber = maxUsedPrefixNumber(nsReg.getPrefixes());
        if (maxPrefixNumber >= nextPrefixNumber)
            nextPrefixNumber = maxPrefixNumber + 1;

        return MQ_PREFIX_START + nextPrefixNumber++;
    }
    
    private Map<String, String> configuredMappings() {
        return JCRSourceFactory.configuredMappings;
    }
    
    private NamespaceRegistry getNamespaceRegistry() throws RepositoryException {
        return node.getSession().getWorkspace().getNamespaceRegistry();
    }
    
    private int maxUsedPrefixNumber(String[] prefixes) {
        int result = 0;
        for (String prefix : prefixes) {
            if (prefix.startsWith(MQ_PREFIX_START)) {
                String prefixEnd = prefix.substring(MQ_PREFIX_START.length());
                int number = Integer.valueOf(prefixEnd);
                if (number >= nextPrefixNumber && number > result) {
                    result = number;
                }
            }
        }
        return result;
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
