/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
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
