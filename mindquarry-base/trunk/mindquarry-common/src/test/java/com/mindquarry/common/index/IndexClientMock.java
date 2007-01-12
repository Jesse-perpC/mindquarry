/*
 * Copyright (C) 2006-2007 MindQuarry GmbH, All Rights Reserved
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
