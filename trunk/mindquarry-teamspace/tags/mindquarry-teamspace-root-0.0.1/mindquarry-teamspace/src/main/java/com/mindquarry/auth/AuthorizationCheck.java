/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.auth;

import com.mindquarry.user.AbstractUserRO;

/**
 * Check if a particular user is privileged to fulfil 
 * an operation at a resource.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public interface AuthorizationCheck {

    boolean mayPerform(String resource, String operation, String userId);
    
    boolean mayPerform(String resource, String operation, AbstractUserRO user);
}
