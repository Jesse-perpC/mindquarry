/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.xml.source;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.TraversableSource;

import com.mindquarry.common.index.IndexClient;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class QueryResultSource implements TraversableSource {
    private JCRSourceFactory factory;

    private Session session;

    private NodeIterator nit;

    private IndexClient iClient;

    public QueryResultSource(JCRSourceFactory factory, Session session,
            NodeIterator nit, IndexClient iClient) {
        this.factory = factory;
        this.session = session;
        this.nit = nit;
        this.iClient = iClient;
    }

    /**
     * @see org.apache.excalibur.source.TraversableSource#getChild(java.lang.String)
     */
    public Source getChild(String name) throws SourceException {
        JCRNodeWrapperSource result = null;
        try {
            while (nit.hasNext()) {
                Node node = nit.nextNode();

                if (node.getName().equals(name)) {
                    result = new JCRNodeWrapperSource(factory, session, node
                            .getPath(), iClient);
                }
            }
            return result;
        } catch (RepositoryException e) {
            return null;
        }
    }

    /**
     * @see org.apache.excalibur.source.TraversableSource#getChildren()
     */
    public Collection getChildren() throws SourceException {
        try {
            Collection<JCRNodeWrapperSource> result = new ArrayList<JCRNodeWrapperSource>();
            while (nit.hasNext()) {
                result.add(new JCRNodeWrapperSource(factory, session, nit
                        .nextNode().getPath(), iClient));
            }
            return result;
        } catch (RepositoryException e) {
            return null;
        }
    }

    /**
     * @see org.apache.excalibur.source.TraversableSource#getName()
     */
    public String getName() {
        return null;
    }

    /**
     * @see org.apache.excalibur.source.TraversableSource#getParent()
     */
    public Source getParent() throws SourceException {
        return null;
    }

    /**
     * @see org.apache.excalibur.source.TraversableSource#isCollection()
     */
    public boolean isCollection() {
        return true;
    }

    /**
     * @see org.apache.excalibur.source.Source#exists()
     */
    public boolean exists() {
        return true;
    }

    /**
     * @see org.apache.excalibur.source.Source#getContentLength()
     */
    public long getContentLength() {
        return -1;
    }

    /**
     * @see org.apache.excalibur.source.Source#getInputStream()
     */
    public InputStream getInputStream() throws IOException,
            SourceNotFoundException {
        return null;
    }

    /**
     * @see org.apache.excalibur.source.Source#getLastModified()
     */
    public long getLastModified() {
        return 0;
    }

    /**
     * @see org.apache.excalibur.source.Source#getMimeType()
     */
    public String getMimeType() {
        return null;
    }

    public String getScheme() {
        return factory.getScheme();
    }

    /**
     * @see org.apache.excalibur.source.Source#getURI()
     */
    public String getURI() {
        return null;
    }

    /**
     * @see org.apache.excalibur.source.Source#getValidity()
     */
    public SourceValidity getValidity() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.apache.excalibur.source.Source#refresh()
     */
    public void refresh() {
        // nothing to do here
    }
}
