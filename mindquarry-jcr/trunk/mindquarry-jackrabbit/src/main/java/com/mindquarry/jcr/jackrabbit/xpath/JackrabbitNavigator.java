package com.mindquarry.jcr.jackrabbit.xpath;

import java.util.Iterator;

import javax.jcr.AccessDeniedException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.NamespaceException;
import javax.jcr.Node;
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

	public String getAttributeName(Object arg0) {
		if (arg0 instanceof Property) {
			Property property = (Property) arg0;
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

	public String getAttributeNamespaceUri(Object arg0) {
		if (arg0 instanceof Property) {
			Property property = (Property) arg0;
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

	public String getAttributeQName(Object arg0) {
		if (arg0 instanceof Property) {
			Property property = (Property) arg0;
			try {
				return property.getName();
			} catch (RepositoryException e) {
				return null;
			}
		}
		return null;
	}

	public String getAttributeStringValue(Object arg0) {
		if (arg0 instanceof Property) {
			Property property = (Property) arg0;
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

	public String getCommentStringValue(Object arg0) {
		// there are no comments in JCR
		return null;
	}

	public String getElementName(Object arg0) {
		if (arg0 instanceof Node) {
			Node node = (Node) arg0;
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

	public String getElementNamespaceUri(Object arg0) {
		if (arg0 instanceof Node) {
			Node node = (Node) arg0;
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

	public String getElementQName(Object arg0) {
		if (arg0 instanceof Node) {
			Node node = (Node) arg0;
			try {
				return node.getName();
			} catch (RepositoryException e) {
				return null;
			}
		}
		return null;
	}

	public String getElementStringValue(Object arg0) {
		return null;
	}

	public String getNamespacePrefix(Object arg0) {
		// there are no namespace nodes in JCR, but anyway...
		return null;
	}

	public String getNamespaceStringValue(Object arg0) {
		// there are no namespace nodes in JCR, but anyway...
		return null;
	}

	public String getTextStringValue(Object arg0) {
		// there are no text nodes in JCR
		return null;
	}

	public boolean isAttribute(Object arg0) {
		return arg0 instanceof Property;
	}

	public boolean isComment(Object arg0) {
		// there are no comment nodes
		return false;
	}

	public boolean isDocument(Object arg0) {
		if (arg0 instanceof Node) {
			Node node = (Node) arg0;
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

    public boolean isElement(Object arg0) {
		return (arg0 instanceof Node) && !isDocument(arg0);
	}

	public boolean isNamespace(Object arg0) {
		// there are no namespace nodes
		return false;
	}

	public boolean isProcessingInstruction(Object arg0) {
		// there are no processing instruction nodes
		return false;
	}

	public boolean isText(Object arg0) {
		// there are no text nodess in JCR
		return false;
	}

	public XPath parseXPath(String arg0) throws SAXPathException {
		return new BaseXPath(arg0, this);
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

	private String getNamespaceUri(String qname)
			throws NamespaceException, RepositoryException {
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
