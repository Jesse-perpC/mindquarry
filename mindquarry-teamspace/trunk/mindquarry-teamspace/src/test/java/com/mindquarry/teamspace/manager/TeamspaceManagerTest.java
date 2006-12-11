package com.mindquarry.teamspace.manager;

import java.util.List;

import org.apache.avalon.framework.service.ServiceException;

import com.mindquarry.teamspace.CouldNotCreateTeamspaceException;
import com.mindquarry.teamspace.CouldNotRemoveTeamspaceException;
import com.mindquarry.teamspace.Membership;
import com.mindquarry.teamspace.TeamspaceAdmin;
import com.mindquarry.teamspace.TeamspaceAlreadyExistsException;
import com.mindquarry.teamspace.Teamspace;
import com.mindquarry.teamspace.TeamspaceListener;
import com.mindquarry.teamspace.TeamspaceListenerRegistry;
import com.mindquarry.teamspace.TeamspaceRO;
import com.mindquarry.teamspace.TeamspaceTestBase;
import com.mindquarry.user.UserAdmin;
import com.mindquarry.user.UserRO;

public class TeamspaceManagerTest extends TeamspaceTestBase {

    public void testCreateAndRemoveTeamspace() throws ServiceException,
            TeamspaceAlreadyExistsException, CouldNotCreateTeamspaceException,
            CouldNotRemoveTeamspaceException {

        TeamspaceAdmin teamsAdmin = lookupTeamspaceAdmin();
        UserAdmin userAdmin = lookupUserAdmin();

        String userId = "mindquarry-user";
        UserRO creator = userAdmin.createUser(userId, "aSecretPassword",
                "Mindquarry User", "surname", "an email", "the skills");

        String teamspaceId = "mindquarry-teamspace";
        Teamspace teamspace = teamsAdmin.createTeamspace(
                teamspaceId, "Mindquarry Teamspace",
                "a greate description", creator);

        List<TeamspaceRO> teamspaces = teamsAdmin.teamspacesForUser(userId);
        assertEquals(1, teamspaces.size());
        assertEquals(1, teamspaces.get(0).getUsers().size());

        teamsAdmin.deleteTeamspace(teamspace);

        assertEquals(0, teamsAdmin.teamspacesForUser(userId).size());
    }

    public void testCreateAndRemoveTeamspaceAsAdmin() throws ServiceException,
            TeamspaceAlreadyExistsException, CouldNotCreateTeamspaceException,
            CouldNotRemoveTeamspaceException {

        TeamspaceAdmin teamsAdmin = lookupTeamspaceAdmin();
        UserAdmin userAdmin = lookupUserAdmin();

        String userId = "admin";
        UserRO creator = userAdmin.userById(userId);

        String teamspaceId = "mindquarry-teamspace";
        Teamspace teamspace = teamsAdmin.createTeamspace(
                teamspaceId, "Mindquarry Teamspace",
                "a greate description", creator);

        List<TeamspaceRO> teamspaces = teamsAdmin.teamspacesForUser(userId);
        assertEquals(1, teamspaces.size());
        assertEquals(1, teamspaces.get(0).getUsers().size());

        teamsAdmin.deleteTeamspace(teamspace);

        assertEquals(0, teamsAdmin.teamspacesForUser(userId).size());
    }

    public void testProperties() throws ServiceException,
            TeamspaceAlreadyExistsException, CouldNotCreateTeamspaceException,
            CouldNotRemoveTeamspaceException {

        TeamspaceAdmin teamsAdmin = lookupTeamspaceAdmin();
        UserAdmin userAdmin = lookupUserAdmin();

        String userId = "mindquarry-user";
        UserRO creator = userAdmin.createUser(userId, "aSecretPassword",
                "Mindquarry User", "surname", "an email", "the skills");

        String teamspaceId = "mindquarry-teamspace";
        Teamspace teamspace = teamsAdmin.createTeamspace(teamspaceId,
                "Mindquarry Teamspace", "a greate description", creator);

        String propKey = "workspaceUri";
        String propValue = "/tmp/repos/mindquarry";

        teamspace.setProperty(propKey, propValue);
        teamsAdmin.updateTeamspace(teamspace);

        Teamspace updatedTeamspace = teamsAdmin.teamspaceById(teamspaceId);

        assertEquals(propValue, updatedTeamspace.getProperty(propKey));

        teamsAdmin.deleteTeamspace(updatedTeamspace);
    }

    public void testTeamspaceRegistry() throws ServiceException,
            TeamspaceAlreadyExistsException, CouldNotCreateTeamspaceException,
            CouldNotRemoveTeamspaceException {

        TestTeamspaceListener testListener = new TestTeamspaceListener();
        lookupTeamspaceListenerRegistry().addListener(testListener);

        TeamspaceAdmin teamsAdmin = lookupTeamspaceAdmin();
        UserAdmin userAdmin = lookupUserAdmin();

        String userId = "mindquarry-user";
        UserRO creator = userAdmin.createUser(userId, "aSecretPassword",
                "Mindquarry User", "surname", "an email", "the skills");

        String teamspaceId = "mindquarry-teamspace";
        Teamspace teamspace = teamsAdmin.createTeamspace(
                teamspaceId, "Mindquarry Teamspace",
                "a greate description", creator);

        assertTrue(testListener.wasCalled);

        teamsAdmin.deleteTeamspace(teamspace);
    }

    public void testAddUserToTeamspace() throws ServiceException,
            TeamspaceAlreadyExistsException, CouldNotCreateTeamspaceException {
        // please note, an admin users is created within the initialize method

        TeamspaceAdmin teamsAdmin = lookupTeamspaceAdmin();
        UserAdmin userAdmin = lookupUserAdmin();

        String userId = "mindquarry-user";
        UserRO creator = userAdmin.createUser(userId, "aSecretPassword",
                "Mindquarry User", "surname", "an email", "the skills");

        userAdmin.createUser("newUser", "aSecretPassword", "Mindquarry User",
                "surname", "an email", "the skills");

        String teamspaceId = "mindquarry-teamspace";
        TeamspaceRO teamspace = teamsAdmin.createTeamspace(teamspaceId,
                "Mindquarry Teamspace", "a greate description", creator);

        Membership membership = teamsAdmin.membership(teamspace);
        assertEquals(1, membership.getMembers().size());
        assertEquals(2, membership.getNonMembers().size());

        membership.addMember(membership.getNonMembers().get(0));
        teamsAdmin.updateMembership(membership);

        Membership updatedMembership = teamsAdmin.membership(teamspace);
        assertEquals(2, updatedMembership.getMembers().size());
        assertEquals(1, updatedMembership.getNonMembers().size());

        UserRO memberToRemove = updatedMembership.getMembers().get(0);
        updatedMembership.removeMember(memberToRemove);
        teamsAdmin.updateMembership(updatedMembership);

        Membership originalMembership = teamsAdmin.membership(teamspace);
        assertEquals(1, originalMembership.getMembers().size());
        assertEquals(2, updatedMembership.getNonMembers().size());
    }

    private TeamspaceListenerRegistry lookupTeamspaceListenerRegistry()
            throws ServiceException {

        String name = DefaultListenerRegistry.class.getName();

        return (TeamspaceListenerRegistry) lookup(name);
    }

    static class TestTeamspaceListener implements TeamspaceListener {

        boolean wasCalled = false;

        public void afterTeamspaceRemoved(Teamspace teamspace) {
            // TODO Auto-generated method stub

        }

        public void beforeTeamspaceCreated(Teamspace teamspace) {
            wasCalled = true;
        }

    }
}
