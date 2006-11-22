/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.common.index;

import java.io.InputStream;


/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class SolrIndexClient extends AbstractAsyncIndexClient {
    /**
     * @see com.mindquarry.common.index.AbstractAsyncIndexer#indexInternal(java.io.InputStream, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    protected void indexInternal(InputStream content, String name,
            String location, String type) {
        // do nothing
        System.out.println("solr indexing");
    }
}
