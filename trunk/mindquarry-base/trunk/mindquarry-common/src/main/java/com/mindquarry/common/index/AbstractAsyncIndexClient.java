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
public abstract class AbstractAsyncIndexClient implements IndexClient {
    /**
     * Starts a new thread for asynchronous indexing. The thread calls the
     * {@link indexInternal()} function which must be overridden by child
     * classes. Within this class the indexing functionality is implemented.
     * 
     * @see com.mindquarry.common.index.Indexer#index(java.io.InputStream,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    public void index(final InputStream content, final String name,
            final String location, final String type) {
        Thread thread = new Thread(new Runnable() {
            /**
             * Calls the internal indexing function.
             * 
             * @see java.lang.Runnable#run()
             */
            public void run() {
                indexInternal(content, name, location, type);
            }
        });
        thread.start();
    }

    /**
     * Abstract index function to be overridden by child classes of this
     * abstract indexer. Child classes shall implement indexing functionality
     * within this function.
     * 
     * @param content
     * @param name
     * @param location
     * @param type
     */
    protected abstract void indexInternal(final InputStream content,
            final String name, final String location, final String type);
}
