/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.user;

import java.util.List;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public interface UserQuery {

    public static final String ROLE = UserQuery.class.getName();
    
    UserRO userById(String userId);
    
    List<UserRO> allUsers();
    
    GroupRO groupById(String groupId);
}
