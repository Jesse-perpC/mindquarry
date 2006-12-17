/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.common.index;

import java.util.List;

/**
 * Used within test cases to increase performance and avoid stupid log messages
 * and stack traces at console output
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class IndexClientMock extends AbstractAsyncIndexClient {

    @Override
    public void index(final List<String> changedPaths,
            final List<String> deletedPaths) {
        // nothing to do here, just for test cases
    }

    @Override
    protected void indexInternal(List<String> changedPaths,
            List<String> deletedPaths) throws Exception {
        // nothing to do here, just for test cases
    }
}
