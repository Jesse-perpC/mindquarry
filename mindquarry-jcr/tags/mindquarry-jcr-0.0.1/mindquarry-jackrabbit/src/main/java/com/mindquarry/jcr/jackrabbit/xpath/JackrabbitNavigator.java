package com.mindquarry.jcr.jackrabbit.xpath;

import java.util.Iterator;

import javax.jcr.AccessDeniedException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.NamespaceException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

import org.apache.jackrabbit.name.NamespaceResolver;
import org.jaxen.BaseXPath;
import org.jaxen.DefaultNavigator;
import org.jaxen.Navigator;
import org.jaxen.UnsupportedAxisException;
import org.jaxen.XPath;
import org.jaxen.saxpath.SAXPathException;

public class JackrabbitNavigator extends DefaultNavigator implements Navigator {

    private static final long serialVersionUID = -2215449271829411396L;

    private NamespaceResolver nr;

    public String translateNamespacePrefixToUri(String prefix, Object element) {
        try {
            return nr.getURI(prefix);
        } catch (NamespaceException e) {
            return super.translateNamespacePrefixToUri(prefix, element);
        }
    }

    public JackrabbitNavigator(NamespaceResolver registry) {
        super();
        this.nr = registry;
    }

    public String getAttributeName(Object object) {
        if (object instanceof Property) {
            Property property = (Property) object;
            try {
                return getLocalName(property.getName());
            } catch (NamespaceException e) {
                return null;
            } catch (RepositoryException e) {
                return null;
            }
        }
        return null;
    }

    public String getAttributeNamespaceUri(Object object) {
        if (object instanceof Property) {
            Property property = (Property) object;
            try {
                return getNamespaceUri(property.getName());
            } catch (NamespaceException e) {
                return null;
            } catch (RepositoryException e) {
                return null;
            } catch (NullPointerException npe) {
                return null;
            }
        }
        return null;
    }

    public String getAttributeQName(Object object) {
        if (object instanceof Property) {
            Property property = (Property) object;
            try {
                return property.getName();
            } catch (RepositoryException e) {
                return null;
            }
        }
        return null;
    }

    public String getAttributeStringValue(Object object) {
        if (object instanceof Property) {
            Property property = (Property) object;
            try {
                return property.getValue().getString();
            } catch (ValueFormatException e) {
                return null;
            } catch (IllegalStateException e) {
                return null;
            } catch (RepositoryException e) {
                return null;
            }
        }
        return null;
    }

    public String getCommentStringValue(Object object) {
        // there are no comments in JCR
        return null;
    }

    public String getElementName(Object object) {
        if (object instanceof Node) {
            Node node = (Node) object;
            try {
                return getLocalName(node.getName());
            } catch (NamespaceException e) {
                return null;
            } catch (RepositoryException e) {
                return null;
            }
        }
        return null;
    }

    public String getElementNamespaceUri(Object object) {
        if (object instanceof Node) {
            Node node = (Node) object;
            try {
                return getNamespaceUri(node.getName());
            } catch (NamespaceException e) {
                return null;
            } catch (RepositoryException e) {
                return null;
            }
        }
        return null;
    }

    public String getElementQName(Object object) {
        if (object instanceof Node) {
            Node node = (Node) object;
            try {
                return node.getName();
            } catch (RepositoryException e) {
                return null;
            }
        }
        return null;
    }

    public String getElementStringValue(Object object) {
        // the string-value of an element node is the concatenation of
        // all text nodes of this element
        if (object instanceof Node) {
            StringBuffer stringValue = new StringBuffer();
            try {
                Node node = (Node) object;
                NodeIterator nit;
                nit = node.getNodes();
                while (nit.hasNext()) {
                    Node child = nit.nextNode();
                    if (child.isNodeType("xt:text")) {
                        stringValue.append(child.getProperty("xt:characters")
                                .getString());
                    }
                }
            } catch (RepositoryException e) {
                return "";
            }

            return stringValue.toString();
        }
        return null;
    }

    public String getNamespacePrefix(Object object) {
        // there are no namespace nodes in JCR, but anyway...
        return null;
    }

    public String getNamespaceStringValue(Object object) {
        // there are no namespace nodes in JCR, but anyway...
        return null;
    }

    public String getTextStringValue(Object object) {
        if (object instanceof Node) {
            Node node = (Node) object;
            try {
                if (node.hasProperty("xt:characters")) {
                    return node.getProperty("xt:characters").getString();
                }
            } catch (RepositoryException e) {
                return null;
            }
        }
        return null;
    }

    public boolean isAttribute(Object object) {
        return object instanceof Property;
    }

    public boolean isComment(Object object) {
        // there are no comment nodes
        return false;
    }

    public boolean isDocument(Object object) {
        if (object instanceof Node) {
            Node node = (Node) object;
            try {
                if (node.getSession().getRootNode() == node) {
                    return true;
                }
            } catch (RepositoryException e) {
                return false;
            }
        }
        return false;
    }

    public Object getDocumentNode(Object contextNode) {
        Node node = (Node) contextNode;
        try {
            return node.getSession().getRootNode();
        } catch (RepositoryException e) {
            return null;
        }
    }

    public boolean isElement(Object object) {
        return (object instanceof Node) && !isDocument(object);
    }

    public boolean isNamespace(Object object) {
        // there are no namespace nodes
        return false;
    }

    public boolean isProcessingInstruction(Object object) {
        // there are no processing instruction nodes
        return false;
    }

    public boolean isText(Object object) {
        if (object instanceof Node) {
            Node node = (Node) object;
            try {
                return node.getPrimaryNodeType().getName().equals("xt:text");
            } catch (RepositoryException e) {
                return false;
            }
        }
        return false;
    }

    public XPath parseXPath(String object) throws SAXPathException {
        return new BaseXPath(object, this);
    }

    public Iterator getChildAxisIterator(Object contextNode)
            throws UnsupportedAxisException {
        if (contextNode instanceof Node) {
            Node node = (Node) contextNode;
            try {
                return node.getNodes();
            } catch (RepositoryException e) {
                return new SimpleIterator();
            }
        }
        return new SimpleIterator();
    }

    public Iterator getAttributeAxisIterator(Object contextNode)
            throws UnsupportedAxisException {
        if (contextNode instanceof Node) {
            Node node = (Node) contextNode;
            try {
                return node.getProperties();
            } catch (RepositoryException e) {
                return new SimpleIterator();
            }
        }
        return new SimpleIterator();
    }

    public Iterator getParentAxisIterator(Object contextNode)
            throws UnsupportedAxisException {
        if (contextNode instanceof Node) {
            Node node = (Node) contextNode;
            try {
                return new SimpleIterator(node.getParent());
            } catch (ItemNotFoundException e) {
                return new SimpleIterator();
            } catch (AccessDeniedException e) {
                return new SimpleIterator();
            } catch (RepositoryException e) {
                return new SimpleIterator();
            }
        }
        return new SimpleIterator();
    }

    private String getNamespaceUri(String qname) throws NamespaceException,
            RepositoryException {
        return this.nr.getURI(getPrefix(qname));
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
