function showRepository() {
	var svnBean = cocoon.getComponent("svn-block");
	cocoon.sendPage("repository-jx", { "url" : svnBean.repositoryUrl, 
									"entries" : svnBean.repositoryEntries });
}
