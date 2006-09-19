package com.mindquarry.project.file;

import java.util.List;

import junit.framework.TestCase;

import com.mindquarry.project.ProjectAlreadyExistsException;
import com.mindquarry.project.ProjectRO;

public class FileProjectManagerTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty(
				FileProjectManager.REPOS_BASE_PATH_PROPERTY, "target");
		
	}
	
	public void testFileProjectManager() throws ProjectAlreadyExistsException {
		
		String projectName = "mindquarry";
		
		FileProjectManager projectMgr = new FileProjectManager();
		projectMgr.create(projectName);
		
		boolean exists = false;
		List<ProjectRO> projects = projectMgr.list();
		int nProjects = projects.size();
		assertTrue(0 < nProjects);
		for (ProjectRO project : projectMgr.list()) {
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
