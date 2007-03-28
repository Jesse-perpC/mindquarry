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
package com.mindquarry.persistence.jcr.query;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert@mindquarry.com">Bastian Steinert</a>
 */
class QueryPreparer {

    private String query_;
    private Object[] queryParameters_;
    
    private QueryPreparer(String queryString, Object[] queryParameters) {
        query_ = new String(queryString);
        queryParameters_ = queryParameters;
    }
    
    private String prepare() {
        int nPlaceholders = numberOfPlaceholders(query_);
        if (queryParameters_.length > nPlaceholders) {
            throw new QueryException("there are too many query parameters " +
                    "for the query: " + query_);
        }
        else if (queryParameters_.length < nPlaceholders) {
            throw new QueryException("there are too less query parameters " +
                    "for the query: " + query_);
        }
        
        StringBuilder querySB = new StringBuilder(query_);
        for (Object param : queryParameters_) {
            int start = querySB.indexOf("{$".intern());
            int end = querySB.indexOf("}".intern(), start);            
            querySB.replace(start, end + 1 , param.toString());
        }
        return querySB.toString();
    }
    
    private int numberOfPlaceholders(String query) {
        final String pattern = "{$";
        int index = 0;
        int nPlaceholders = 0;
        while ((index = query.indexOf(pattern, index)) != -1 ) {
            nPlaceholders++;
            index += pattern.length();
        }
        return nPlaceholders;
    }
    
    public String toString() {
        return "QueryPreparer: query=" + query_;
    }
    
    public static String prepare(String query, Object[] queryParameters) {
        return new QueryPreparer(query, queryParameters).prepare();
    }
}
