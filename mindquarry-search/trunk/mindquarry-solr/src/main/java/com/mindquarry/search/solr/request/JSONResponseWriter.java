/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.search.solr.request;

import java.io.IOException;
import java.io.Writer;

import org.apache.solr.request.QueryResponseWriter;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrQueryResponse;
import org.apache.solr.util.NamedList;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public class JSONResponseWriter implements QueryResponseWriter {
    private static String CONTENT_TYPE_JSON_UTF8 = "text/x-json; charset=UTF-8"; //$NON-NLS-1$

    /**
     * @see org.apache.solr.request.QueryResponseWriter#init(org.apache.solr.util.NamedList)
     */
    public void init(NamedList n) {
    }

    /**
     * @see org.apache.solr.request.QueryResponseWriter#write(java.io.Writer,
     *      org.apache.solr.request.SolrQueryRequest,
     *      org.apache.solr.request.SolrQueryResponse)
     */
    public void write(Writer writer, SolrQueryRequest req, SolrQueryResponse rsp)
            throws IOException {
        JSONWriter w = new CustomSearchJSONWriter(writer, req, rsp);
        w.writeResponse();
    }

    /**
     * @see org.apache.solr.request.QueryResponseWriter#getContentType(org.apache.solr.request.SolrQueryRequest,
     *      org.apache.solr.request.SolrQueryResponse)
     */
    public String getContentType(SolrQueryRequest request,
            SolrQueryResponse response) {
        // using the text/plain allows this to be viewed in the browser easily

        // use this for DEBUGGING only
        return CONTENT_TYPE_TEXT_UTF8;

        // use this for production environment
        // return CONTENT_TYPE_JSON_UTF8;
    }
}
