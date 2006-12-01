/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.search.solr.request;

import java.io.IOException;
import java.io.Writer;

import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrQueryResponse;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public class JSONResponseWriter extends
        org.apache.solr.request.JSONResponseWriter {
    /**
     * @see org.apache.solr.request.JSONResponseWriter#write(java.io.Writer,
     *      org.apache.solr.request.SolrQueryRequest,
     *      org.apache.solr.request.SolrQueryResponse)
     */
    @Override
    public void write(Writer writer, SolrQueryRequest req, SolrQueryResponse rsp)
            throws IOException {
        new JSONWriter(writer, req, rsp).writeResponse();
    }
}
