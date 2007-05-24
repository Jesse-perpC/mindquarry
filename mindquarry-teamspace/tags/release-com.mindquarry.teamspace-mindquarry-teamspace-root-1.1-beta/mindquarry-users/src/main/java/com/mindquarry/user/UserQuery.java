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
package com.mindquarry.user;

import java.util.Collection;

/**
 * Provides methods to query user related data.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public interface UserQuery {

    public static final String ROLE = UserQuery.class.getName();
    
    UserRO userById(String userId);
    /**
     * returns a list of all users excluding 
     * the default users admin and solr/index
     */
    Collection<? extends UserRO> allUsers();
    
    RoleRO roleById(String roleId);
    Collection<? extends RoleRO> allRoles();
}
