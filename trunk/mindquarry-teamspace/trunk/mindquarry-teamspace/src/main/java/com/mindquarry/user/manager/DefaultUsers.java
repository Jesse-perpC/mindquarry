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
package com.mindquarry.user.manager;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class DefaultUsers {
    
    private static final int LOGIN = 0;
    private static final int PWD = 0;
    private static final int NAME = 0;
    
    // loginname, password, UserName
    static final String[] ADMIN_USER = {"admin", "admin", "Administrator"};
    static final String[] INDEX_USER = {"solr", "solr", "Index User"};    
    static final String[] ANONYMOUS_USER = 
                        {"anonymous", "anonymous", "Anonymous User"};
    
    static final String[][] defaultUsers = 
                        {ADMIN_USER, INDEX_USER, ANONYMOUS_USER};

    static final String login(String[] userProfile) {
        return userProfile[LOGIN];
    }
    
    static final String password(String[] userProfile) {
        return userProfile[PWD];
    }
    
    static final String username(String[] userProfile) {
        return userProfile[NAME];
    }
}
