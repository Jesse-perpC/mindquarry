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

import org.junit.Test;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class QueryPrepareTest extends TestCase {

    @Test
    public void testTooFewParameters() {
        String query = "/users/{$login}";
        Object[] queryParameters = new Object[0]; 
        try {
            QueryPreparer.prepare(query, queryParameters);
        } 
        catch (QueryException e) {
            return;
        }
        fail("query preparer did not detect too few parameters");
    }
    
    @Test
    public void testTooManyParameters() {
        String query = "/users/{$login}";
        Object[] queryParameters = new Object[] {"foo", "bar"}; 
        try {
            QueryPreparer.prepare(query, queryParameters);
        } 
        catch (QueryException e) {
            return;
        }
        fail("query preparer did not detect too many parameters");
    }
}
