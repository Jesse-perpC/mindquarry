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

public class TeamspaceManagerTest extends TeamspaceTestBase {
    
    private User queryUserById(String userId) throws ServiceException {
        UserAdmin userAdmin = lookupUserAdmin();
        return userAdmin.userById(userId);
    }
    
    public void testCreateAndRemoveTeamspace() throws ServiceException,
            TeamspaceAlreadyExistsException, TeamspaceException {

        TeamspaceAdmin teamsAdmin = lookupTeamspaceAdmin();
        UserAdmin userAdmin = lookupUserAdmin();

        String userId = "mindquarry-user";
        userAdmin.createUser(userId, "aSecretPassword",
                "Mindquarry User", "surname", "an email", "the skills");

        String teamspaceId = "mindquarry-teamspace";
        Teamspace teamspace = teamsAdmin.createTeamspace(
                teamspaceId, "Mindquarry Teamspace",
                "a greate description", queryUserById(userId));

        assertEquals(1, teamspace.getUsers().size());

        teamsAdmin.deleteTeamspace(teamspace);
        userAdmin.deleteUser(queryUserById(userId));
    }

    public void testCreateAndRemoveTeamspaceAsAdmin() throws ServiceException,
            TeamspaceAlreadyExistsException, TeamspaceException {

        TeamspaceAdmin teamsAdmin = lookupTeamspaceAdmin();
        UserAdmin userAdmin = lookupUserAdmin();

        String userId = "admin";
        UserRO creator = userAdmin.userById(userId);

        String teamspaceId = "mindquarry-teamspace";
        Teamspace team = teamsAdmin.createTeamspace(
                teamspaceId, "Mindquarry Teamspace",
                "a greate description", creator);

        assertEquals(0, team.getUsers().size());
        teamsAdmin.deleteTeamspace(team);
    }
}
