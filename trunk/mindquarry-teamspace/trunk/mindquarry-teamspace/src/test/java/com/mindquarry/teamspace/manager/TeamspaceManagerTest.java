package com.mindquarry.teamspace.manager;

import java.io.File;
import java.util.List;

import org.apache.avalon.framework.service.ServiceException;

import com.mindquarry.teamspace.Membership;
import com.mindquarry.teamspace.TeamspaceAdmin;
import com.mindquarry.teamspace.TeamspaceAlreadyExistsException;
import com.mindquarry.teamspace.TeamspaceRO;
import com.mindquarry.teamspace.UserRO;

public class TeamspaceManagerTest extends TeamspaceTestBase {
	
    private static final String TEST_REPOS_PATH = "target/workspace-repos";
    
    protected void setUp() throws Exception {
        super.setUp();
        File reposDir = new File(TEST_REPOS_PATH);
        if (! reposDir.exists()) 
            reposDir.mkdirs();
        
        System.setProperty(
                TeamspaceManager.REPOS_BASE_PATH_PROPERTY, TEST_REPOS_PATH);
    }
    
	public void testCreateAndRemoveTeamspace() 
        throws ServiceException, TeamspaceAlreadyExistsException {
        
        TeamspaceAdmin admin = lookupTeamspaceAdmin();
        
        String userId = "mindquarry-user";
        UserRO creator = admin.createUser(userId, "aSecretPassword", 
                "Mindquarry User", "surname", "an email", "the skills");
        
        String teamspaceId = "mindquarry-teamspace";
		admin.createTeamspace(teamspaceId, "Mindquarry Teamspace", 
                "a greate description", creator);
        
        admin.createTeamspace(teamspaceId + "2", "Mindquarry Teamspace", 
                "a greate description", creator);
        
        List<TeamspaceRO> teamspaces = admin.teamspacesForUser(userId);
        assertEquals(2, teamspaces.size());
                
        admin.removeTeamspace(teamspaceId);        
        admin.removeTeamspace(teamspaceId + "2");
        
        assertEquals(0, admin.teamspacesForUser(userId).size());
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
    
    private TeamspaceAdmin lookupTeamspaceAdmin() throws ServiceException {
        return (TeamspaceAdmin) lookup(TeamspaceAdmin.class.getName());
    }
}
