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
package com.mindquarry.teamspace.manager;

import org.apache.avalon.framework.service.ServiceException;

import com.mindquarry.teamspace.Teamspace;
import com.mindquarry.teamspace.TeamspaceAdmin;
import com.mindquarry.teamspace.TeamspaceAlreadyExistsException;
import com.mindquarry.teamspace.TeamspaceException;
import com.mindquarry.teamspace.TeamspaceTestBase;
import com.mindquarry.user.User;
import com.mindquarry.user.UserAdmin;
import com.mindquarry.user.UserRO;

public class TeamspaceAddMemberTest extends TeamspaceTestBase {
    
    private static final String teamspaceId = "mindquarry-teamspace";
    private static final String userId = "mindquarry-user";
    
    protected void setUp() throws Exception {
        super.setUp();
        UserAdmin userAdmin = lookupUserAdmin();
        TeamspaceAdmin teamsAdmin = lookupTeamspaceAdmin();
        User admin = userAdmin.userById("admin");        
        teamsAdmin.createTeamspace(teamspaceId,
                "Mindquarry Teamspace", "a greate description", admin);
        
        userAdmin.createUser(userId, "aSecretPassword",
                "Mindquarry User", "surname", "an email", "the skills");
    }
    
    protected void tearDown() throws Exception {
        UserAdmin userAdmin = lookupUserAdmin();
        TeamspaceAdmin teamAdmin = lookupTeamspaceAdmin();
        
        Teamspace team = teamAdmin.teamspaceById(teamspaceId);
        teamAdmin.deleteTeamspace(team);

        User user = userAdmin.userById(userId);  
        userAdmin.deleteUser(user);
        
        super.tearDown();
    }

    public void testAddUserToTeamspace() throws ServiceException,
            TeamspaceAlreadyExistsException, TeamspaceException {
        // please note, an admin users is created during setup
        TeamspaceAdmin teamsAdmin = lookupTeamspaceAdmin();
        UserAdmin userAdmin = lookupUserAdmin();
        
        Teamspace team = teamsAdmin.teamspaceById(teamspaceId);
        assertEquals(0, team.getUsers().size());

        teamsAdmin.addMember(userAdmin.allUsers().iterator().next(), team);

        Teamspace oneMemberTeam = teamsAdmin.teamspaceById(teamspaceId);
        assertEquals(1, oneMemberTeam.getUsers().size());
       
        UserRO memberToRemove = oneMemberTeam.getUsers().iterator().next();
        teamsAdmin.removeMember(memberToRemove, oneMemberTeam);
        
        Teamspace emptyTeam = teamsAdmin.teamspaceById(teamspaceId);
        assertEquals(0, emptyTeam.getUsers().size());
    }
}
