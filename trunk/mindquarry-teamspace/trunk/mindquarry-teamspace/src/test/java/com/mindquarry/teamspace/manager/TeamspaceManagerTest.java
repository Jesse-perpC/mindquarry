package com.mindquarry.teamspace.manager;

import java.io.File;

import org.apache.avalon.framework.service.ServiceException;

import com.mindquarry.teamspace.TeamspaceAdmin;
import com.mindquarry.teamspace.TeamspaceAlreadyExistsException;
import com.mindquarry.teamspace.manager.TeamspaceManager;

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
    
	public void testCreateAndRemove() throws ServiceException, TeamspaceAlreadyExistsException {
        TeamspaceAdmin admin = lookupTeamspaceAdmin();
		admin.create("mindquarry", "Mindquarry", "a greate description");
        assertEquals(1, admin.allTeamspaces().size());
        admin.remove("mindquarry");
        assertEquals(0, admin.allTeamspaces().size());
	}
    
    private TeamspaceAdmin lookupTeamspaceAdmin() throws ServiceException {
        return (TeamspaceAdmin) lookup(TeamspaceAdmin.class.getName());
    }
}
