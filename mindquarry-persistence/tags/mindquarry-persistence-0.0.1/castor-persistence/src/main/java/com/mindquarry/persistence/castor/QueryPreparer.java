/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.castor;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:your-email-address">your full name</a>
 */
class QueryPreparer {

    private StringBuilder querySB_;
    private Object[] params_;
    
    QueryPreparer(String queryString, Object[] params) {
        querySB_ = new StringBuilder(queryString);
        params_ = params;
    }
    
    String prepare() {        
        for (Object param : params_) {
            int start = querySB_.indexOf("{$".intern());
            int end = querySB_.indexOf("}".intern(), start);            
            querySB_.replace(start, end + 1 , param.toString());
        }        
        return querySB_.toString();
    }
    
    public String toString() {
        return querySB_.toString();
    }
}
