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

import com.mindquarry.teamspace.CouldNotCreateTeamspaceException;
import com.mindquarry.teamspace.Membership;
import com.mindquarry.teamspace.Teamspace;
import com.mindquarry.teamspace.TeamspaceAdmin;
import com.mindquarry.teamspace.TeamspaceAlreadyExistsException;
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
            TeamspaceAlreadyExistsException, CouldNotCreateTeamspaceException {
        // please note, an admin users is created during setup
        TeamspaceAdmin teamsAdmin = lookupTeamspaceAdmin();
        
        Teamspace team = teamsAdmin.teamspaceById(teamspaceId);

        Membership membership = teamsAdmin.membership(team);
        assertEquals(0, membership.getMembers().size());
        assertEquals(1, membership.getNonMembers().size());

        membership.addMember(membership.getNonMembers().get(0));
        teamsAdmin.updateMembership(membership);

        Membership updatedMembership = teamsAdmin.membership(team);
        assertEquals(1, updatedMembership.getMembers().size());
        assertEquals(0, updatedMembership.getNonMembers().size());

        
        UserRO memberToRemove = updatedMembership.getMembers().get(0);
        updatedMembership.removeMember(memberToRemove);
        teamsAdmin.updateMembership(updatedMembership);

        Membership originalMembership = teamsAdmin.membership(team);
        assertEquals(0, originalMembership.getMembers().size());
        assertEquals(1, updatedMembership.getNonMembers().size());
    }
}
