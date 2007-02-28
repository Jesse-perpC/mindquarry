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

import com.mindquarry.user.UserRO;


/**
 * Provides methods to manage teamspaces (e.g. create and delete).
 *
 * @author 
 * <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 *
 */
public interface TeamspaceAdmin extends TeamspaceQuery {

    public static final String ROLE = TeamspaceAdmin.class.getName();
    
    /**
     * creates a new teamspace and the associated workspace (svn repo)
     * @param teamspaceId
     * @param name of the teamspace
     * @param a String describing the teamspace
     * @param the user that triggered the creation of the new teamspace 
     * @throws CouldNotCreateTeamspaceException 
     */
	Teamspace createTeamspace(String id, String name, 
                String description, UserRO teamspaceCreator) throws CouldNotCreateTeamspaceException;
	
    Teamspace teamspaceById(String teamspaceId);
    
    void updateTeamspace(Teamspace teamspace);
    
	void deleteTeamspace(Teamspace teamspace) throws CouldNotRemoveTeamspaceException;
}
