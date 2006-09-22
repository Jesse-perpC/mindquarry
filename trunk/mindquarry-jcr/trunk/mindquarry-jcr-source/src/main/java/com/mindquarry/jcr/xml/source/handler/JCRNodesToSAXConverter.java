/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.xml.source.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

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
     * Converts a JCR node with jcr:content type xt:document into SAX events.
     * 
     * @param node the node to be converted
     * @param handler the SAX handler to be used
     * @throws SAXException thrown if something goes wrong during processing of
     *         the JCR nodes
     */
    public static void convertToSAX(Node node, ContentHandler handler)
            throws SAXException {
        handler.startDocument();

        try {
            convertChildsToSAX(node.getNode("jcr:content"), handler);
        } catch (Exception e) {
            throw new SAXException(
                    "An error occured while converting JCR nodes to SAX.", e);
        }
        handler.endDocument();
    }

    private static void convertChildsToSAX(Node node, ContentHandler handler)
            throws RepositoryException, SAXException, IOException {
        NodeIterator nit = node.getNodes();
        while (nit.hasNext()) {
            Node child = nit.nextNode();
            if (child.isNodeType("xt:element")) {
                AttributesImpl atts = new AttributesImpl();
                PropertyIterator pit = child.getProperties();
                while (pit.hasNext()) {
                    Property prop = pit.nextProperty();
                    if (prop.getName().startsWith("jcr")) {
                        continue;
                    }
                    atts.addAttribute("", prop.getName(), prop.getName(),
                            "CDATA", prop.getString());
                }
                handler
                        .startElement("", child.getName(), child.getName(),
                                atts);

                convertChildsToSAX(child, handler);
                handler.endElement("", child.getName(), child.getName());
            } else if (child.isNodeType("xt:text")) {
                InputStream is = child.getProperty("xt:characters").getStream();
                ByteArrayOutputStream os = new ByteArrayOutputStream();

                int b;
                while ((b = is.read()) != -1) {
                    os.write(b);
                }
                char[] characters = new String(os.toByteArray()).toCharArray();
                handler.characters(characters, 0, characters.length);
            }
        }
    }
}
