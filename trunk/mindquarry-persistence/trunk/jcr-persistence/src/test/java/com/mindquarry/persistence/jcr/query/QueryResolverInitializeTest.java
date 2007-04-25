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

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.mindquarry.persistence.api.JavaConfiguration;
import com.mindquarry.persistence.api.NamedQuery;

/**
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class QueryResolverInitializeTest extends TestCase {

    private QueryResolver queryResolver_;
    
    @Before
    public void setUp() {
        queryResolver_ = new QueryResolver();
    }
    
    @Test
    public void testDuplicateQueries() {        
        JavaConfiguration configuration = new JavaConfiguration();
        configuration.addClass(User.class);
        configuration.addNamedQuery("userquery", "a user query");
        
        try {
            queryResolver_.initialize(configuration);
        }
        catch (QueryException e) {
            return;
        }
        fail("query resolver did not detect duplicate query names");
    }
    
    @Test
    public void testDuplicateQueriesWithClasses() {        
        JavaConfiguration configuration = new JavaConfiguration();
        configuration.addClass(User.class);
        configuration.addClass(UserDuplicate.class);
        
        try {
            queryResolver_.initialize(configuration);
        }
        catch (QueryException e) {
            return;
        }
        fail("query resolver did not detect duplicate query names");
    }

    @NamedQuery(name="userquery", query="a user query")
    public static class User {        
    }
    
    @NamedQuery(name="userquery", query="a user query with duplicate name")
    public static class UserDuplicate {
    }
}
