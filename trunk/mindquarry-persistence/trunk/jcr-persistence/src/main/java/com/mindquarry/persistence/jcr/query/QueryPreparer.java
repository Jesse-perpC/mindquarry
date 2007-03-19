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

    private StringBuilder querySB_;
    private Object[] queryParameters_;
    
    QueryPreparer(String queryString, Object[] queryParameters) {
        querySB_ = new StringBuilder(queryString);
        queryParameters_ = queryParameters;
    }
    
    String prepare() {        
        for (Object param : queryParameters_) {
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
