/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.search.solr.request;

import java.io.IOException;
import java.io.Writer;

import org.apache.solr.request.JSONResponseWriter;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrQueryResponse;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class QuickSearchResponseWriter extends JSONResponseWriter {
    /**
     * @see org.apache.solr.request.QueryResponseWriter#write(java.io.Writer,
     *      org.apache.solr.request.SolrQueryRequest,
     *      org.apache.solr.request.SolrQueryResponse)
     */
    @Override
    public void write(Writer writer, SolrQueryRequest req, SolrQueryResponse rsp)
            throws IOException {
        new QuickSearchSearchJSONWriter(writer, req, rsp).writeResponse();
    }
}
