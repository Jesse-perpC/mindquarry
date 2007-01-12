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
package com.mindquarry.persistence.castor;

import junit.framework.TestCase;

public class QueryPreparerTest extends TestCase {

    private static final String QUERY = "select {$column} from {$table}";
    private static final String COLUMN = "testColumn";
    private static final String TABLE = "testTable";
    
    
    private static final String PREPARED_QUERY = 
        "select " + COLUMN + " from " + TABLE;
    
    public void testPrepare() {
        Object[] params = new Object[] {COLUMN, TABLE};
        QueryPreparer testQuery = new QueryPreparer(QUERY, params);
        assertEquals(PREPARED_QUERY, testQuery.prepare());
    }

}
