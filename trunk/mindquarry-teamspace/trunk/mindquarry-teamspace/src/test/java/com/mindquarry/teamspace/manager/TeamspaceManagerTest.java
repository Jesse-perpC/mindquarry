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
        UserRO creator = admin.createUser(userId, "Mindquarry User", "an email");
        
        String teamspaceId = "mindquarry-teamspace";
		admin.createTeamspace(teamspaceId, "Mindquarry Teamspace", 
                "a greate description", creator);
        
        admin.createTeamspace(teamspaceId + "2", "Mindquarry Teamspace", 
                "a greate description", creator);
        
        List<TeamspaceRO> teamspaces = admin.allTeamspaces();
        assertEquals(2, teamspaces.size());
        assertEquals(teamspaceId, teamspaces.get(0).getId());
                
        admin.removeTeamspace(teamspaceId + "2");
        assertEquals(1, admin.allTeamspaces().size());
        
        admin.removeTeamspace(teamspaceId);
        assertEquals(0, admin.allTeamspaces().size());
	}
    
    public void testCreateAndRemoveUser() 
        throws ServiceException, TeamspaceAlreadyExistsException {
        
        TeamspaceAdmin admin = lookupTeamspaceAdmin();
        
        String userId = "mindquarry-user";
        admin.createUser(userId, "Mindquarry User", "an email");
        
        List<UserRO> users = admin.allUsers();
        assertEquals(1, users.size());
        assertEquals(userId, users.get(0).getId());
        
        admin.removeUser(userId);
        assertEquals(0, admin.allTeamspaces().size());
    }
    
    public void testAddUserToTeamspace() 
        throws ServiceException, TeamspaceAlreadyExistsException {
        
        TeamspaceAdmin admin = lookupTeamspaceAdmin();
        
        String userId = "mindquarry-user";
        UserRO creator = admin.createUser(userId, "Mindquarry User", "an email");
        
        admin.createUser(
                "newUser", "Mindquarry User", "an email");
        
        String teamspaceId = "mindquarry-teamspace";
        TeamspaceRO teamspace = admin.createTeamspace(teamspaceId, 
                "Mindquarry Teamspace", "a greate description", creator);
        
        Membership membership = admin.membership(teamspace);
        assertEquals(1, membership.getMembers().size());
        assertEquals(1, membership.getNonMembers().size());
        
        
        membership.addMember(membership.getNonMembers().get(0));
        admin.updateMembership(membership);
        
        
        Membership updatedMembership = admin.membership(teamspace);
        assertEquals(2, updatedMembership.getMembers().size());
        assertEquals(0, updatedMembership.getNonMembers().size());
        
        UserRO memberToRemove = updatedMembership.getMembers().get(0);
        updatedMembership.removeMember(memberToRemove);
        admin.updateMembership(updatedMembership);
        
        Membership originalMembership = admin.membership(teamspace);
        assertEquals(1, originalMembership.getMembers().size());
        assertEquals(1, updatedMembership.getNonMembers().size());
    }
    
    private TeamspaceAdmin lookupTeamspaceAdmin() throws ServiceException {
        return (TeamspaceAdmin) lookup(TeamspaceAdmin.class.getName());
    }
}
