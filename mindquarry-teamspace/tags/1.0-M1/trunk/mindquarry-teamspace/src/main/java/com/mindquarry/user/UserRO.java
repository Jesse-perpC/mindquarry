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

import java.util.Set;

import com.mindquarry.teamspace.TeamspaceRO;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public interface UserRO extends AbstractUserRO {

    /**
     * Getter for name.
     *
     * @return the name
     */
    String getName();
    
    /**
     * Getter for surname.
     *
     * @return the surname
     */
    String getSurname();

    /**
     * returns an unmodifiable set view of names 
     * for the teamspaces this user participates in
     */
    Set<String> teamspaces();
    
    /**
     * determines if this user is a member of 
     * the specified teamspace
     */
    boolean isMemberOf(TeamspaceRO teamspace);
    
    /**
     * determines if this user is a member of 
     * the teamspace with the specified id
     */
    boolean isMemberOf(String teamspaceId);

    /**
     * Getter for email.
     *
     * @return the email
     */
    String getEmail();
    
    /**
     * Getter for skills.
     *
     * @return the skills
     */
    String getSkills();
}