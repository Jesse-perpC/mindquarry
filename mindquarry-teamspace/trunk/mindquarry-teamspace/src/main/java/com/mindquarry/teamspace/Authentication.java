/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public interface Authentication {
    
    /**
     * tries to authenticate an user
     *  
     * @param userId
     * @param password
     * @returns true if a user matches the arguments, otherwise false
     */
    boolean authenticate(String userId, String password);
}
