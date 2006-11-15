package com.mindquarry.teamspace.manager;

import java.util.List;

import org.apache.avalon.framework.service.ServiceException;

import com.mindquarry.teamspace.Membership;
import com.mindquarry.teamspace.TeamspaceAdmin;
import com.mindquarry.teamspace.TeamspaceAlreadyExistsException;
import com.mindquarry.teamspace.TeamspaceDefinition;
import com.mindquarry.teamspace.TeamspaceListener;
import com.mindquarry.teamspace.TeamspaceListenerRegistry;
import com.mindquarry.teamspace.TeamspaceRO;
import com.mindquarry.teamspace.UserRO;

public class TeamspaceManagerTest extends TeamspaceTestBase {
      
	public void testCreateAndRemoveTeamspace() 
        throws ServiceException, TeamspaceAlreadyExistsException {
        
        TeamspaceAdmin admin = lookupTeamspaceAdmin();
        
        String userId = "mindquarry-user";
        UserRO creator = admin.createUser(userId, "aSecretPassword", 
                "Mindquarry User", "surname", "an email", "the skills");
        
        String teamspaceId = "mindquarry-teamspace";
		admin.createTeamspace(teamspaceId, "Mindquarry Teamspace", 
                "a greate description", creator);
        
        List<TeamspaceRO> teamspaces = admin.teamspacesForUser(userId);
        assertEquals(1, teamspaces.size());
        assertEquals(1, teamspaces.get(0).getUsers().size());
        
        admin.removeTeamspace(teamspaceId);
        
        assertEquals(0, admin.teamspacesForUser(userId).size());
	}
    
    public void testCreateAndRemoveTeamspaceAsAdmin() throws ServiceException,
            TeamspaceAlreadyExistsException {

        TeamspaceAdmin admin = lookupTeamspaceAdmin();
        
        String userId = "admin";
        UserRO creator = admin.userForId(userId);

        String teamspaceId = "mindquarry-teamspace";
        admin.createTeamspace(teamspaceId, "Mindquarry Teamspace",
                "a greate description", creator);

        List<TeamspaceRO> teamspaces = admin.teamspacesForUser(userId);
        assertEquals(1, teamspaces.size());
        assertEquals(1, teamspaces.get(0).getUsers().size());

        admin.removeTeamspace(teamspaceId);

        assertEquals(0, admin.teamspacesForUser(userId).size());
    }
    
    public void testProperties() 
        throws ServiceException, TeamspaceAlreadyExistsException {
        
        TeamspaceAdmin admin = lookupTeamspaceAdmin();
        
        String userId = "mindquarry-user";
        UserRO creator = admin.createUser(userId, "aSecretPassword", 
                "Mindquarry User", "surname", "an email", "the skills");
        
        String teamspaceId = "mindquarry-teamspace";
        TeamspaceDefinition teamspace = admin.createTeamspace(teamspaceId, 
                "Mindquarry Teamspace", "a greate description", creator);
        
        String propKey = "workspaceUri";
        String propValue = "/tmp/repos/mindquarry";
        
        teamspace.setProperty(propKey, propValue);
        admin.updateTeamspace(teamspace);
        
        TeamspaceDefinition updatedTeamspace = 
            admin.teamspaceDefinitionForId(teamspaceId);
        
        assertEquals(propValue, updatedTeamspace.getProperty(propKey));
                
        admin.removeTeamspace(teamspaceId);
    }
    
    public void testTeamspaceRegistry() 
        throws ServiceException, TeamspaceAlreadyExistsException {
        
        TestTeamspaceListener testListener = new TestTeamspaceListener();
        lookupTeamspaceListenerRegistry().addListener(testListener);
        
        TeamspaceAdmin admin = lookupTeamspaceAdmin();
        
        String userId = "mindquarry-user";
        UserRO creator = admin.createUser(userId, "aSecretPassword", 
                "Mindquarry User", "surname", "an email", "the skills");
        
        String teamspaceId = "mindquarry-teamspace";
        admin.createTeamspace(teamspaceId, 
                "Mindquarry Teamspace", "a greate description", creator);
        
        //assertTrue(testListener.wasCalled);
                
        admin.removeTeamspace(teamspaceId);
    }
    
    public void testCreateAndRemoveUser() 
        throws ServiceException, TeamspaceAlreadyExistsException {
        // please note, an admin users is created within the initialize method
        
        TeamspaceAdmin admin = lookupTeamspaceAdmin();
        
        String userId = "mindquarry-user";
        admin.createUser(userId, "aSecretPassword",
                "Mindquarry User", "surname", "an email", "the skills");
        
        List<UserRO> users = admin.allUsers();
        assertEquals(2, users.size());
        
        admin.removeUser(userId);
        assertEquals(1, admin.allUsers().size());
    }
    
    public void testAddUserToTeamspace() 
        throws ServiceException, TeamspaceAlreadyExistsException {
        // please note, an admin users is created within the initialize method
        
        TeamspaceAdmin admin = lookupTeamspaceAdmin();
        
        String userId = "mindquarry-user";
        UserRO creator = admin.createUser(userId, "aSecretPassword",
                "Mindquarry User", "surname", "an email", "the skills");
        
        admin.createUser("newUser", "aSecretPassword", 
                "Mindquarry User", "surname", "an email", "the skills");
        
        String teamspaceId = "mindquarry-teamspace";
        TeamspaceRO teamspace = admin.createTeamspace(teamspaceId, 
                "Mindquarry Teamspace", "a greate description", creator);
        
        Membership membership = admin.membership(teamspace);
        assertEquals(1, membership.getMembers().size());
        assertEquals(2, membership.getNonMembers().size());
        
        
        membership.addMember(membership.getNonMembers().get(0));
        admin.updateMembership(membership);
        
        
        Membership updatedMembership = admin.membership(teamspace);
        assertEquals(2, updatedMembership.getMembers().size());
        assertEquals(1, updatedMembership.getNonMembers().size());
        
        UserRO memberToRemove = updatedMembership.getMembers().get(0);
        updatedMembership.removeMember(memberToRemove);
        admin.updateMembership(updatedMembership);
        
        Membership originalMembership = admin.membership(teamspace);
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
        
        public void afterTeamspaceRemoved(TeamspaceDefinition teamspace) {
            // TODO Auto-generated method stub
            
        }

        public void beforeTeamspaceCreated(TeamspaceDefinition teamspace) {
            wasCalled = true;
        }
        
    }
}
