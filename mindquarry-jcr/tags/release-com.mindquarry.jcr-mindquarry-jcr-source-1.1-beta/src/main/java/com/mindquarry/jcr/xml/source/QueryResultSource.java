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
 * A virtual source containing all matching jcr nodes of the query as children.
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
        JCRNodeSource result = null;
        try {
            while (nit.hasNext()) {
                Node node = nit.nextNode();

                if (node.getName().equals(name)) {
                    result = new JCRNodeSource(factory, session, node
                            .getPath(), iClient);
                }
            }
            return result;
        } catch (RepositoryException e) {
            return null;
        }
    }

    /**
     * Returns the matching nodes of the query.
     * 
     * @see org.apache.excalibur.source.TraversableSource#getChildren()
     */
    public Collection getChildren() throws SourceException {
        try {
            Collection<JCRNodeSource> result = new ArrayList<JCRNodeSource>();
            while (nit.hasNext()) {
                result.add(new JCRNodeSource(factory, session, nit
                        .nextNode().getPath(), iClient));
            }
            return result;
        } catch (RepositoryException e) {
            return null;
        }
    }

    /**
     * A query is purely virtual, thus has no name.
     * 
     * @see org.apache.excalibur.source.TraversableSource#getName()
     */
    public String getName() {
        return null;
    }

    /**
     * A query result is virtual thus has no parent.
     * 
     * @see org.apache.excalibur.source.TraversableSource#getParent()
     */
    public Source getParent() throws SourceException {
        return null;
    }

    /**
     * A query result is a collection of matching nodes.
     * 
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
     * Content length cannot be computed for query results.
     * 
     * @see org.apache.excalibur.source.Source#getContentLength()
     */
    public long getContentLength() {
        return -1;
    }

    /**
     * A query result has no data, only children.
     * 
     * @see org.apache.excalibur.source.Source#getInputStream()
     */
    public InputStream getInputStream() throws IOException,
            SourceNotFoundException {
        return null;
    }

    /**
     * Query results are purely virtual, thus cannot be modified.
     * 
     * @see org.apache.excalibur.source.Source#getLastModified()
     */
    public long getLastModified() {
        return 0;
    }

    /**
     * Queries have no mimetype.
     * 
     * @see org.apache.excalibur.source.Source#getMimeType()
     */
    public String getMimeType() {
        return null;
    }

    /**
     * Same scheme as defined in JCRSourceFactory.
     * 
     * @see org.apache.excalibur.source.Source#getScheme()
     */
    public String getScheme() {
        return factory.getScheme();
    }

    /**
     * A virtual query has no uri.
     * 
     * @see org.apache.excalibur.source.Source#getURI()
     */
    public String getURI() {
        return null;
    }

    /**
     * A query cannot be cached.
     * 
     * @see org.apache.excalibur.source.Source#getValidity()
     */
    public SourceValidity getValidity() {
        return null;
    }

    /**
     * @see org.apache.excalibur.source.Source#refresh()
     */
    public void refresh() {
        // nothing to do here
    }
}
