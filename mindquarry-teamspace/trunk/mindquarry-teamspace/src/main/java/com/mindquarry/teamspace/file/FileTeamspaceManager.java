/**
 * 
 */
package com.mindquarry.teamspace.file;


/**
 *  
 * @author <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public class FileTeamspaceManager  {

//	static final String REPOS_BASE_PATH_PROPERTY = "mindquarry.reposbasepath";
//	
//	private File reposBaseDirectory_;
//	
//	public FileTeamspaceManager() {		
//		String path = System.getProperty(REPOS_BASE_PATH_PROPERTY);
//		if (null != path)
//			reposBaseDirectory_ = new File(path);		
//		
//		if (null == path || ! reposBaseDirectory_.exists() 
//				|| ! reposBaseDirectory_.isDirectory()) 
//			throw new InitializationException("system property " +
//					"'mindquarry.reposbasepath' is not set to a valid, " +
//					"existing base directory for repositories");
//	}
//	
//	private FileFilter svnRepositoryFilter() {
//		return new FileFilter() {
//			public boolean accept(File file) {
//				boolean isSvnRepoDir = false;
//				if (file.isDirectory()) {
//					isSvnRepoDir = new File(file, "format").exists();
//				}
//				return isSvnRepoDir;
//			}
//		};
//	}
//    
//    private Teamspace makeProject(String name) {
//        return new Teamspace(name);
//    }
//	
//	public void create(String name) throws TeamspaceAlreadyExistsException {
//		if (getProjects().containsKey(name))
//			throw new TeamspaceAlreadyExistsException();
//		
//		dmaAdmin(name).create();
//	}
//	
//	private DmaRepository dmaAdmin(String projectName) {
//		StringBuilder repoPathSB = new StringBuilder();
//		repoPathSB.append(reposBaseDirectory_.getAbsolutePath());
//		repoPathSB.append(File.separatorChar);
//		repoPathSB.append(projectName);
//		repoPathSB.append(File.separatorChar);
//		return new SvnRepositoryFactory().newRepository(repoPathSB.toString());
//	}
//	
//	public List<Object> list() {
//		return new LinkedList<Object>(getProjects().values());
//	}
//	
//	/**
//	 * @param name, the name (id) of the project
//	 * @return the absolute path to the dma repository 
//	 * (without concluding separator)
//	 */
//	public String workspaceUri(String name) {
//		validateExistence(name);
//		return resolveRepositoryPath(name);
//	}
//    
//    private String resolveRepositoryPath(String name) {
//        return new File(reposBaseDirectory_, name).toURI().getPath();
//    }
//
//	public void remove(String name) {
//		validateExistence(name);
//		dmaAdmin(name).removeRepository();
//	}
//	
//	private void validateExistence(String name) {
//		if (! getProjects().containsKey(name))
//			throw new TeamspaceException("a project with name: " + name 
//									 + " does not exists.");		
//	}
//
//    private Map<String, Teamspace> getProjects() {
//        Map<String, Teamspace> result = new HashMap<String, Teamspace>();
//        for (File child : reposBaseDirectory_.listFiles(svnRepositoryFilter())) {
//            String name = child.getName();
//            result.put(name, makeProject(name));
//        }
//        return result;
//    }
//
//    public void create(String id, String name, String description) {
//        // TODO Auto-generated method stub
//        
//    }
}
