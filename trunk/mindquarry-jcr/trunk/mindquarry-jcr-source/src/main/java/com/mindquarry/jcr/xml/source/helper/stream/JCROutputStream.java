/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.xml.source.helper.stream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;

import javax.jcr.AccessDeniedException;
import javax.jcr.InvalidItemStateException;
import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mindquarry.jcr.xml.source.handler.SAXToJCRNodesConverter;
import com.mindquarry.jcr.xml.source.helper.XMLFileSourceHelper;

/**
 * OutputStream to be used for writing to the {@link XMLFileSourceHelper}.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JCROutputStream extends ByteArrayOutputStream {
    private boolean isClosed = false;

    private final Node node;

    private final Session session;

    public JCROutputStream(Node node, Session session) {
        this.node = node;
        this.session = session;
    }

    /**
     * @see java.io.ByteArrayOutputStream#close()
     */
    @Override
    public void close() throws IOException {
        if (!isClosed) {
            super.close();
            isClosed = true;
            try {
                // node.lock(true, true);
                try {
                    node.getNode("jcr:content");
                    if (isXML()) {
                        deleteChildren();
                        if (canParse()) {
                            writeXML();
                        } else {
                            throw new IOException("XML is not well-formed");
                        }
                    } else {
                        writeBinary();
                    }
                } catch (PathNotFoundException e) {
                    boolean isXML = canParse();
                    if (isXML) {
                        createXML();
                    } else {
                        createBinary();
                    }
                }
                // don't forget to commit
                // node.unlock();
                session.save();
            } catch (RepositoryException e) {
                throw new IOException("Unable to write to repository "
                        + e.getLocalizedMessage());
            }
        }
    }

    private void createBinary() throws IOException {
        try {
            node.addNode("jcr:content", "nt:resource");
            writeBinary();
        } catch (ItemExistsException e) {
            throw new IOException("Content node already exists: "
                    + e.getLocalizedMessage());
        } catch (PathNotFoundException e) {
            throw new IOException("Path not found: " + e.getLocalizedMessage());
        } catch (NoSuchNodeTypeException e) {
            throw new IOException("Node type does not exist: "
                    + e.getLocalizedMessage());
        } catch (LockException e) {
            throw new IOException("Resource is locked: "
                    + e.getLocalizedMessage());
        } catch (VersionException e) {
            throw new IOException("Invalid version: " + e.getLocalizedMessage());
        } catch (ConstraintViolationException e) {
            throw new IOException("Constraints are violated: "
                    + e.getLocalizedMessage());
        } catch (RepositoryException e) {
            throw new IOException("Unable to write to repository: "
                    + e.getLocalizedMessage());
        }
    }

    private void createXML() throws IOException {
        try {
            node.addNode("jcr:content", "xt:document");
            writeXML();
        } catch (ItemExistsException e) {
            throw new IOException("Content node already exists: "
                    + e.getLocalizedMessage());
        } catch (PathNotFoundException e) {
            throw new IOException("Path not found: " + e.getLocalizedMessage());
        } catch (NoSuchNodeTypeException e) {
            throw new IOException("Node type does not exist: "
                    + e.getLocalizedMessage());
        } catch (LockException e) {
            throw new IOException("Resource is locked: "
                    + e.getLocalizedMessage());
        } catch (VersionException e) {
            throw new IOException("Invalid version: " + e.getLocalizedMessage());
        } catch (ConstraintViolationException e) {
            throw new IOException("Constraints are violated: "
                    + e.getLocalizedMessage());
        } catch (RepositoryException e) {
            throw new IOException("Unable to write to repository: "
                    + e.getLocalizedMessage());
        }
    }

    private void deleteChildren() throws IOException {
        // remove old content
        try {
            NodeIterator nit = node.getNode("jcr:content").getNodes();
            while (nit.hasNext()) {
                nit.nextNode().remove();
            }
        } catch (UnsupportedRepositoryOperationException e) {
            throw new IOException("Locking is not supported by repository: "
                    + e.getLocalizedMessage());
        } catch (LockException e) {
            throw new IOException("Unable to get lock: "
                    + e.getLocalizedMessage());
        } catch (AccessDeniedException e) {
            throw new IOException("Access to repository denied: "
                    + e.getLocalizedMessage());
        } catch (InvalidItemStateException e) {
            throw new IOException("Item state is invalid: "
                    + e.getLocalizedMessage());
        } catch (RepositoryException e) {
            throw new IOException("Unable to write to repository "
                    + e.getLocalizedMessage());
        }
    }

    private boolean canParse() {
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(
                    new ByteArrayInputStream(this.toByteArray()),
                    new DefaultHandler());
        } catch (SAXException e) {
            return false;
        } catch (IOException e) {
            return false;
        } catch (ParserConfigurationException e) {
            return false;
        }
        return true;
    }

    private void writeXML() throws IOException {
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(
                    new ByteArrayInputStream(this.toByteArray()),
                    new SAXToJCRNodesConverter(node));
        } catch (PathNotFoundException e) {
            throw new IOException("Path not found: " + e.getLocalizedMessage());
        } catch (SAXException e) {
            throw new IOException("Unable to parse: " + e.getLocalizedMessage());
        } catch (ParserConfigurationException e) {
            throw new IOException("Unable to configure parser: "
                    + e.getLocalizedMessage());
        } catch (RepositoryException e) {
            throw new IOException("Unable to write to repository: "
                    + e.getLocalizedMessage());
        }
    }

    private void writeBinary() throws IOException {
        try {
            node.getNode("jcr:content").setProperty("jcr:data",
                    new ByteArrayInputStream(this.toByteArray()));
            node.getNode("jcr:content").setProperty("jcr:mimeType",
                    "application/octetstream");
            node.getNode("jcr:content").setProperty("jcr:lastModified",
                    new GregorianCalendar());
        } catch (ValueFormatException e) {
            throw new IOException("Invalid value format: "
                    + e.getLocalizedMessage());
        } catch (VersionException e) {
            throw new IOException("Invalid Version" + e.getLocalizedMessage());
        } catch (LockException e) {
            throw new IOException("Resource is locked"
                    + e.getLocalizedMessage());
        } catch (ConstraintViolationException e) {
            throw new IOException("Constrains violated: "
                    + e.getLocalizedMessage());
        } catch (PathNotFoundException e) {
            throw new IOException("Path not found: " + e.getLocalizedMessage());
        } catch (RepositoryException e) {
            throw new IOException("Unable to write to repository: "
                    + e.getLocalizedMessage());
        }
    }

    private boolean isXML() throws IOException {
        try {
            return node.getNode("jcr:content").isNodeType("xt:document");
        } catch (PathNotFoundException e) {
            throw new IOException(
                    "Path not found, cannot determine content type of node: "
                            + e.getLocalizedMessage());
        } catch (RepositoryException e) {
            throw new IOException(
                    "Reading data from repository failed, cannot determine content of node: "
                            + e.getLocalizedMessage());
        }
    }

    public boolean canCancel() {
        return !isClosed;
    }

    public void cancel() throws IOException {
        if (isClosed) {
            throw new IllegalStateException("Cannot cancel: "
                    + "outputstrem is already closed");
        }
    }
}
