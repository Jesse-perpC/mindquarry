/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.user;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public interface Authentication {
    
    public static final String ROLE = Authentication.class.getName();
    
    /**
     * tries to authenticate an user
     *  
     * @param userId
     * @param password
     * @returns true if a user matches the arguments, otherwise false
     */
    boolean authenticate(String userId, String password);
}
