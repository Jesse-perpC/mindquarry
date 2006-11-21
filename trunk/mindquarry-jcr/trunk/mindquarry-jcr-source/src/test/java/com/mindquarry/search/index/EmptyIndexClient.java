/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.search.index;

import java.io.InputStream;

import com.mindquarry.common.index.AbstractAsyncIndexClient;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class EmptyIndexClient extends AbstractAsyncIndexClient {
    /**
     * @see com.mindquarry.common.index.AbstractAsyncIndexer#indexInternal(java.io.InputStream, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    protected void indexInternal(InputStream content, String name,
            String location, String type) {
        // do nothing
    }
}
