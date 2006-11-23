/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.common.index;

import java.util.List;

import org.apache.avalon.framework.logger.AbstractLogEnabled;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public abstract class AbstractAsyncIndexClient extends AbstractLogEnabled
        implements IndexClient {
    /**
     * Starts a new thread for asynchronous indexing. The thread calls the
     * {@link indexInternal()} function which must be overridden by child
     * classes. Within this class the indexing functionality is implemented.
     * 
     * @see com.mindquarry.common.index.Indexer#index(List<String>, List<String>)
     */
    public void index(final List<String> changedPaths,
            final List<String> deletedPaths) {
        Thread thread = new Thread(new Runnable() {
            /**
             * Calls the internal indexing function.
             * 
             * @see java.lang.Runnable#run()
             */
            public void run() {
                try {
                    indexInternal(changedPaths, deletedPaths);
                } catch (Exception e) {
                    getLogger()
                            .error(
                                    "An error occured while indexing client processes the list of changed paths.",
                                    e);
                }
            }
        });
        thread.start();
    }

    /**
     * Abstract index function to be overridden by child classes of this
     * abstract indexer. Child classes shall implement indexing functionality
     * within this function.
     */
    protected abstract void indexInternal(List<String> changedPaths,
            List<String> deletedPaths) throws Exception;
    
    public void indexSynch(final List<String> changedPaths,
            final List<String> deletedPaths) {
        try {
            indexInternal(changedPaths, deletedPaths);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
