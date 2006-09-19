/**
 * 
 */
package com.mindquarry.project.file;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mindquarry.common.init.InitializationException;
import com.mindquarry.dma.admin.DmaAdmin;
import com.mindquarry.dma.admin.DmaAdminFactory;
import com.mindquarry.project.ProjectAdmin;
import com.mindquarry.project.ProjectAlreadyExistsException;
import com.mindquarry.project.ProjectException;
import com.mindquarry.project.ProjectQuery;
import com.mindquarry.project.ProjectRO;

/**
 *  
 * @author <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public class FileProjectManager implements ProjectAdmin, ProjectQuery {

	static final String REPOS_BASE_PATH_PROPERTY = "mindquarry.reposbasepath";
	
	private File reposBaseDirectory_;
	private Map<String, Project> projects_;
	
	public FileProjectManager() {		
		String path = System.getProperty(REPOS_BASE_PATH_PROPERTY);
		if (null != path)
			reposBaseDirectory_ = new File(path);		
		
		if (null == path || ! reposBaseDirectory_.exists() 
				|| ! reposBaseDirectory_.isDirectory()) 
			throw new InitializationException("system property " +
					"'mindquarry.reposbasepath' is not set to a valid, " +
					"existing base directory for repositories");
		
		init();
	}
	
	private void init() {
		projects_ = new HashMap<String, Project>();
		for (File child : reposBaseDirectory_.listFiles(svnRepositoryFilter())) {
			String name = child.getName();
			projects_.put(name, new Project(name));
		}
	}
	
	private FileFilter svnRepositoryFilter() {
		return new FileFilter() {
			public boolean accept(File file) {
				boolean isSvnRepoDir = false;
				if (file.isDirectory()) {
					isSvnRepoDir = new File(file, "format").exists();
				}
				return isSvnRepoDir;
			}
		};
	}
	
	public void create(String name) throws ProjectAlreadyExistsException {
		if (projects_.containsKey(name))
			throw new ProjectAlreadyExistsException();
		
		dmaAdmin(name).createRepository();

		projects_.put(name, new Project(name));
	}
	
	private DmaAdmin dmaAdmin(String projectName) {
		StringBuilder repoPathSB = new StringBuilder();
		repoPathSB.append(reposBaseDirectory_.getAbsolutePath());
		repoPathSB.append(File.separatorChar);
		repoPathSB.append(projectName);
		repoPathSB.append(File.separatorChar);
		return DmaAdminFactory.createAdmin(repoPathSB.toString());
	}
	
	public List<ProjectRO> list() {
		return new LinkedList<ProjectRO>(projects_.values());
	}
	
	/**
	 * @param name, the name (id) of the project
	 * @return the absolute path to the dma repository 
	 * (without concluding separator)
	 */
	public String repositoryPath(String name) {
		validateExistence(name);
		return new File(reposBaseDirectory_, name).toURI().getPath();
	}

	public void remove(String name) {
		validateExistence(name);
		dmaAdmin(name).removeRepository();
		projects_.remove(name);
	}
	
	private void validateExistence(String name) {
		if (! projects_.containsKey(name))
			throw new ProjectException("a project with name: " + name 
									 + " does not exists.");		
	}
}
