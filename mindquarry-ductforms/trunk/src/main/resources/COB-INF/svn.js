function showRepository() {
	var svnBean = cocoon.getComponent("svn");
	cocoon.sendPage("repository", { "url" : svnBean.repositoryUrl, 
									"entries" : svnBean.repositoryEntries });
}
