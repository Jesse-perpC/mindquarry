/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.user.manager;

import java.util.LinkedList;
import java.util.List;

import com.mindquarry.user.UserRO;

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
    
    // the ANONYMOUS_USER is not always part of the default users
    // the mindquarry server admin can configure 
    // whether or not anonymous should be enabled    
    public static List<String[]> defaultUsers() {
        List<String[]> result = new LinkedList<String[]>();
        
        result.add(ADMIN_USER);
        result.add(INDEX_USER);
        
        if (isAnonymousEnabled())
            result.add(ANONYMOUS_USER);
        
        return result;
    }

    public static final String login(String[] userProfile) {
        return userProfile[LOGIN];
    }
    
    public static final String password(String[] userProfile) {
        return userProfile[PWD];
    }
    
    public static final String username(String[] userProfile) {
        return userProfile[NAME];
    }
    
    public static boolean isAnonymousEnabled() {
        // the parse method returns true if the property is not null
        // and is equal, ignoring case, to string 'true'; otherwise false.
        return Boolean.parseBoolean(enableAnonymousUserProperty());
    }
    
    public static boolean isAnonymousDisabled() {
        return ! isAnonymousEnabled();
    }
    
    private static String enableAnonymousUserProperty() {
        String value = 
            System.getProperty("mindquarry.teams.enableAnonymousUser");
        
        if (value == null)
            return "";
        else
            return value.trim();
    }

    public static boolean isAdminUser(UserRO user) {
        return login(ADMIN_USER).equals(user.getId());
    }

    public static boolean isIndexUser(UserRO user) {
        return login(INDEX_USER).equals(user.getId());
    }

    public static boolean isIndexUser(String userId) {
        return INDEX_USER.equals(userId);
    }

    public static boolean isAnonymousUser(UserRO user) {
        return login(ANONYMOUS_USER).equals(user.getId());
    }

    public static boolean isAnonymousUser(String userId) {
        return login(ANONYMOUS_USER).equals(userId);
    }
}
