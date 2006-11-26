/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.auth;

import com.mindquarry.user.AbstractUserRO;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public interface AuthorizationCheck {

    boolean mayPerform(String resource, String operation, String userId);
    
    boolean mayPerform(String resource, String operation, AbstractUserRO user);
}
