package com.mindquarry.persistence.xmlbeans;

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
