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
import com.mindquarry.teamspace.TeamspaceListener;
import com.mindquarry.teamspace.TeamspaceListenerRegistry;
import com.mindquarry.teamspace.TeamspaceTestBase;
import com.mindquarry.user.User;
import com.mindquarry.user.UserAdmin;

public class TeamspaceRegistryTest extends TeamspaceTestBase {

    public void testTeamspaceRegistry() throws ServiceException,
            TeamspaceAlreadyExistsException, TeamspaceException {

        TestTeamspaceListener testListener = new TestTeamspaceListener();
        lookupTeamspaceListenerRegistry().addListener(testListener);

        TeamspaceAdmin teamsAdmin = lookupTeamspaceAdmin();
        UserAdmin userAdmin = lookupUserAdmin();
        User admin = userAdmin.userById("admin");

        String teamspaceId = "mindquarry-teamspace";
        Teamspace teamspace = teamsAdmin.createTeamspace(
                teamspaceId, "Mindquarry Teamspace",
                "a greate description", admin);

        assertTrue(testListener.wasCalled);

        teamsAdmin.deleteTeamspace(teamspace);
    }

    private TeamspaceListenerRegistry lookupTeamspaceListenerRegistry()
            throws ServiceException {

        String name = DefaultListenerRegistry.class.getName();

        return (TeamspaceListenerRegistry) lookup(name);
    }

    static class TestTeamspaceListener implements TeamspaceListener {

        boolean wasCalled = false;

        public void beforeTeamspaceRemoved(Teamspace teamspace) {
            // TODO Auto-generated method stub

        }

        public void afterTeamspaceCreated(Teamspace teamspace) {
            wasCalled = true;
        }

    }
}
