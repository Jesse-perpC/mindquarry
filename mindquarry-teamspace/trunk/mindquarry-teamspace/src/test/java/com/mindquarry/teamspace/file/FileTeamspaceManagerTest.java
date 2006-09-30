package com.mindquarry.teamspace.file;

import java.util.List;

import junit.framework.TestCase;

import com.mindquarry.teamspace.TeamspaceAlreadyExistsException;
import com.mindquarry.teamspace.TeamspaceRO;
import com.mindquarry.teamspace.file.FileTeamspaceManager;

public class FileTeamspaceManagerTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty(
				FileTeamspaceManager.REPOS_BASE_PATH_PROPERTY, "target");
		
	}
	
	public void testFileProjectManager() throws TeamspaceAlreadyExistsException {
		
		String projectName = "mindquarry";
		
		FileTeamspaceManager projectMgr = new FileTeamspaceManager();
		projectMgr.create(projectName);
		
		boolean exists = false;
		List<TeamspaceRO> projects = projectMgr.list();
		int nProjects = projects.size();
		assertTrue(0 < nProjects);
		for (TeamspaceRO project : projectMgr.list()) {
			if (projectName.equals(project.getName())) {
				exists = true;
				break;
			}
		}
		assertTrue(exists);
		
		projectMgr.remove(projectName);
		assertEquals((nProjects-1), projectMgr.list().size());
	}
}
