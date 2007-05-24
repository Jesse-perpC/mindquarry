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
package com.mindquarry.teamspace;

import java.util.Set;

import com.mindquarry.user.UserRO;


/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public interface TeamspaceRO {

    /**
     * Getter for description.
     *
     * @return the description
     */
    public String getDescription();

    /**
     * Getter for id.
     *
     * @return the id
     */
    public String getId();

    /**
     * Getter for name.
     *
     * @return the name
     */
    public String getName();
    
    /**
     * Getter for users.
     *
     * @return an umodifiable view of the users participating this project
     */
    public Set<? extends UserRO> getUsers();
}