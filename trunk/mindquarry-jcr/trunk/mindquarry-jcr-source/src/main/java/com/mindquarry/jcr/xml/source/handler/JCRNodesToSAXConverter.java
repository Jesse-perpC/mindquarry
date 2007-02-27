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
package com.mindquarry.jcr.xml.source.handler;

import java.io.IOException;

import javax.jcr.NamespaceException;
import javax.jcr.NamespaceRegistry;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.mindquarry.jcr.xml.source.JCRConstants;
import com.mindquarry.jcr.xml.source.JCRSourceFactory;

/**
 * Converter for JCR nodes to SAX events.
 * 
 * @author <a
 *         href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">Alexander
 *         Klimetschek</a>
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JCRNodesToSAXConverter {
    /**
     * Converts a JCR node of type xt:document into SAX events.
     * 
     * @param node the jcr:content node to be converted
     * @param handler the SAX handler to be used
     * @throws SAXException thrown if something goes wrong during processing of
     *         the JCR nodes
     */
    public static void convertToSAX(Node content, ContentHandler handler)
            throws SAXException {
        handler.startDocument();

        try {
            Session session = content.getSession();
            Workspace ws = session.getWorkspace();
            NamespaceRegistry nr = ws.getNamespaceRegistry();

            convertChildsToSAX(content, handler, nr);
        } catch (Exception e) {
            throw new SAXException(
                    "An error occured while converting JCR nodes to SAX.", e);
        }
        handler.endDocument();
    }

    private static void convertChildsToSAX(Node node, ContentHandler handler,
            NamespaceRegistry nr) throws RepositoryException, SAXException,
            IOException {
        NodeIterator nit = node.getNodes();
        while (nit.hasNext()) {
            Node child = nit.nextNode();

            if (JCRSourceFactory.isElement(child)) {
                // handle an XML element
                
                // handle the attributes
                
                AttributesImpl atts = new AttributesImpl();
                PropertyIterator pit = child.getProperties();

                while (pit.hasNext()) {
                    Property prop = pit.nextProperty();
                    // ignore the standard jcr properties (jcr:something)
                    if (prop.getName().startsWith("jcr")) {
                        continue;
                    }
                    String qName = prop.getName();
                    String namespaceURI = "";
                    String localName = "";

                    // ensure correct namespaces on attribute
                    String prefix = getPrefix(prop.getName());
                    if (prefix != null) {
                        localName = getLocalName(prop.getName());
                        namespaceURI = getNamespace(prefix, nr);
                        qName = prefix + ":" + localName;
                    } else {
                        localName = prop.getName();
                    }
                    atts.addAttribute(namespaceURI, localName, qName, "CDATA",
                            prop.getString());
                }
                
                // handle the element itself
                
                String qName = child.getName();
                String namespaceURI = "";
                String localName = "";

                // ensure correct namespaces on element
                String prefix = getPrefix(child.getName());
                if (prefix != null) {
                    localName = getLocalName(child.getName());
                    namespaceURI = getNamespace(prefix, nr);
                    qName = prefix + ":" + localName;
                } else {
                    localName = child.getName();
                }
                handler.startElement(namespaceURI, localName, qName, atts);

                // recursively handle child elements
                convertChildsToSAX(child, handler, nr);

                handler.endElement(namespaceURI, localName, qName);
                
            } else if (JCRSourceFactory.isText(child)) {
                // handle a text node
                String str = child.getProperty(JCRConstants.XT_CHARACTERS).getString();
                char[] characters = str.toCharArray();
                handler.characters(characters, 0, characters.length);
            }
        }
    }

	private static String getNamespace(String prefix, NamespaceRegistry nr)
            throws NamespaceException, RepositoryException {
        return nr.getURI(prefix);
    }

    private static String getLocalName(String name) throws NamespaceException,
            RepositoryException {
        String[] parts = name.split(":");
        if (parts.length == 2) {
            return parts[1];
        } else {
            return parts[0];
        }
    }

    private static String getPrefix(String name) throws NamespaceException,
            RepositoryException {
        String[] parts = name.split(":");
        if (parts.length == 2) {
            return parts[0];
        } else {
            return null;
        }
    }
}
