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
        
        String teamspaceId = "mindquarry-teamspace";
		admin.createTeamspace(teamspaceId, "Mindquarry Teamspace", "a greate description");
        
        admin.createTeamspace(teamspaceId + "2", "Mindquarry Teamspace", "a greate description");
        
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
        admin.createUser(userId, "Mindquarry User");
        
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
        admin.createUser(userId, "Mindquarry User");
        
        String teamspaceId = "mindquarry-teamspace";
        TeamspaceRO teamspace = admin.createTeamspace(
                teamspaceId, "Mindquarry Teamspace", "a greate description");
        
        Membership membership = admin.membership(teamspace);
        UserRO nonMember = membership.getNonMembers().get(0);
        membership.addMember(nonMember);
        
        admin.updateMembership(membership);
        
        
        Membership updatedMembership = admin.membership(teamspace);
        assertEquals(1, updatedMembership.getMembers().size());
        assertEquals(0, updatedMembership.getNonMembers().size());
        
        UserRO newMember = updatedMembership.getMembers().get(0);
        updatedMembership.removeMember(newMember);
        admin.updateMembership(updatedMembership);
        
        Membership originalMembership = admin.membership(teamspace);
        assertEquals(0, originalMembership.getMembers().size());
        assertEquals(1, updatedMembership.getNonMembers().size());
    }
    
    private TeamspaceAdmin lookupTeamspaceAdmin() throws ServiceException {
        return (TeamspaceAdmin) lookup(TeamspaceAdmin.class.getName());
    }
}
