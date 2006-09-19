/*
 * Copyright 2006 Mindquarry GmbH, Potsdam, Germany
 * Copyright 2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.excalibur.source.impl.jcr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.excalibur.source.ModifiableSource;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.mindquarry.source.jcr.xml.JCRXMLSourceFactory;

/**
 * A source for an XML-izable JCR Node.
 * 
 */
public class JCRXMLNodeSource implements ModifiableSource, XMLizable {

    /** The full URI */
    protected String computedURI;

    /** The node path */
    protected final String path;

    /** The factory that created this Source */
    protected final JCRXMLSourceFactory factory;

    /** The session this source is bound to */
    protected final Session session;

    /** The node pointed to by this source (can be null) */
    protected Node node;

    public JCRXMLNodeSource(JCRXMLSourceFactory factory, Session session,
            String path) throws SourceException {
        this.factory = factory;
        this.session = session;
        this.path = path;

        try {
            Item item = session.getItem(path);
            if (!item.isNode()) {
                throw new SourceException("Path '" + path
                        + "' is a property (should be a node)");
            } else {
                this.node = (Node) item;
            }
        } catch (PathNotFoundException e) {
            // Not found
            this.node = null;
        } catch (RepositoryException e) {
            throw new SourceException("Cannot lookup repository path " + path,
                    e);
        }
    }

    public JCRXMLNodeSource(JCRXMLSourceFactory parent, Node node)
            throws SourceException {
        this.factory = parent;
        this.node = node;

        try {
            this.session = node.getSession();
            this.path = node.getPath();
        } catch (RepositoryException e) {
            throw new SourceException("Cannot get node's informations", e);
        }
    }

    public JCRXMLNodeSource(JCRXMLNodeSource parent, Node node)
            throws SourceException {
        this.factory = parent.factory;
        this.session = parent.session;
        this.node = node;

        try {
            this.path = getChildPath(parent.path, node.getName());

        } catch (RepositoryException e) {
            throw new SourceException("Cannot get name of child of "
                    + parent.getURI(), e);
        }
    }

    // =========================================================================
    // Source interface
    // =========================================================================

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.cocoon.jcr.source.JCRNodeSource#getInputStream()
     */
    public InputStream getInputStream() throws IOException,
            SourceNotFoundException {
        // used when the source is non-xml, aka text or binary
        // return super.getInputStream();
        return null;
    }

    public boolean exists() {
        return this.node != null;
    }

    public long getContentLength() {
        // TODO: if content length is known because it is a binary/text
        // content return it here
        
        // we don't know the content length due to dynamic content of xml subtree
        return -1;
    }

    public long getLastModified() {
        // TODO: getLastModified()

        // example code for setting the last modified property
//      contentNode.setProperty(info.lastModifiedProp, new GregorianCalendar());

        return 0;
    }

    public String getMimeType() {
        if (!exists()) {
            return null;
        }
        // TODO: getMimeType(): use some node property
        return null;
//        try {
//            Property prop = this.factory.getMimeTypeProperty(this.node);
//            if (prop == null) {
//                return null;
//            } else {
//                String value = prop.getString();
//                return value.length() == 0 ? null : value;
//            }
//        } catch (RepositoryException re) {
//            return null;
//        }
    }

    public String getScheme() {
        return this.factory.getScheme();
    }

    public String getURI() {
        if (this.computedURI == null) {
            this.computedURI = this.factory.getScheme() + ":/" + this.path;
        }
        return this.computedURI;
    }

    public SourceValidity getValidity() {
        if (!exists()) {
            return null;
        }
        // TODO: getValidity()
        return null;
//        try {
//            Property prop = this.factory.getValidityProperty(this.node);
//            return prop == null ? null : new JCRNodeSourceValidity(prop.getValue());
//        } catch (RepositoryException re) {
//            return null;
//        }
    }

    public void refresh() {
        // nothing to do here
    }

    // =========================================================================
    // XMLizable interface
    // =========================================================================

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.excalibur.xml.sax.XMLizable#toSAX()
     */
    public void toSAX(ContentHandler handler) throws SAXException {
        // use the JCR export feature
        try {
            this.session.exportDocumentView(this.path, handler, false, false);
        } catch (PathNotFoundException e) {
            throw new SAXException("Source not found: " + this.getURI());
        } catch (RepositoryException e) {
            throw new SAXException(
                    "Cannot export repository path as document view sax events: "
                            + this.path, e);
        }
    }

    // =========================================================================
    // ModifiableSource interface
    // =========================================================================

    /**
     * Write into this source, can be XML.
     */
    public OutputStream getOutputStream() throws IOException {
        // if this node is a leaf node with direct content,
        // write the content directly as in the subclass into
        // some content property of the node (see JCRSourceOutputStream)
        // else if this node has no children yet, parse xml and create
        // substructure
        //
        // ??? How to overwrite existing XML structure ???
        // a) structure the same, different content
        // b) only a part
        // c) with additional elements
        // d) mixed up

        return new JCRXMLSourceOutputStream(this.path);
    }

    public boolean canCancel(OutputStream stream) {
        return false;
//        if (os instanceof JCRSourceOutputStream) {
//            return ((JCRSourceOutputStream) os).canCancel();
//        } else {
//            return false;
//        }
    }

    public void cancel(OutputStream stream) throws IOException {
//        if (canCancel(os)) {
//            ((JCRSourceOutputStream) os).cancel();
//        } else {
//            throw new IllegalArgumentException("Stream cannot be cancelled");
//        }
    }

    public void delete() throws SourceException {
        if (exists()) {
            try {
                this.node.remove();
                this.node = null;
                this.session.save();
            } catch (RepositoryException e) {
                throw new SourceException("Cannot delete " + getURI(), e);
            }
        }
    }

    // =========================================================================
    // Internal methods / classes
    // =========================================================================

    /**
     * Utility method that calculates the correct child path.
     */
    private String getChildPath(String path, String name) {
        StringBuffer pathBuf = new StringBuffer(path);
        // Append '/' only if the parent isn't the root (it's path is "/" in
        // that case)
        if (pathBuf.length() > 1)
            pathBuf.append('/');
        pathBuf.append(name);
        return pathBuf.toString();
    }

    /**
     * An outputStream that will import the stream as XML upon close, and
     * discard it upon cancel.
     */
    private class JCRXMLSourceOutputStream extends ByteArrayOutputStream {
        private boolean isClosed = false;

        private final String parentAbsPath;

        public JCRXMLSourceOutputStream(String parentAbsPath) {
            this.parentAbsPath = parentAbsPath;
        }

        public void close() throws IOException {
            if (isClosed) {
                return;
            }

            super.close();
            this.isClosed = true;

            // SAXParser
            try {
                JCRXMLNodeSource.this.session
                        .getWorkspace()
                        .importXML(
                                this.parentAbsPath,
                                new ByteArrayInputStream(this.toByteArray()),
                                ImportUUIDBehavior.IMPORT_UUID_COLLISION_REMOVE_EXISTING);
            } catch (RepositoryException e) {
                IOException ioe = new IOException("Cannot save content to "
                        + getURI());
                ioe.initCause(e);
                throw ioe;
            }
        }

        public boolean canCancel() {
            return !isClosed;
        }

        public void cancel() throws IOException {
            if (isClosed) {
                throw new IllegalStateException(
                        "Cannot cancel : outputstrem is already closed");
            }
        }
    }
}
