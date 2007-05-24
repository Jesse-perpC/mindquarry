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
