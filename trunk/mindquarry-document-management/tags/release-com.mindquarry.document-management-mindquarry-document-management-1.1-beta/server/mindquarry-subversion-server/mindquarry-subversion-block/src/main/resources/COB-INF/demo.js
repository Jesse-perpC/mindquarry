importClass(org.tmatesoft.svn.core.io.SVNRepository);
importClass(org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory);
importClass(org.tmatesoft.svn.core.wc.SVNRevision);
importClass(org.tmatesoft.svn.core.io.SVNRepositoryFactory);
importClass(org.tmatesoft.svn.core.SVNURL);

function inspectResource() {
  var uuid = "getting uuid for " + cocoon.parameters.repository;
  
  FSRepositoryFactory.setup(); 
  var svnurl = SVNURL.parseURIEncoded("file:///Users/lars/Documents/Software/Mindquarry%20Workspace/mindquarry-dma-javasvn/src/test/resources/com/mindquarry/dma/javasvn/repo");
  var repo = SVNRepositoryFactory.create(svnurl);
  uuid = repo.getRepositoryUUID(true);
  
  cocoon.sendPage("internal/pipe/propfindresource", 
    {
      repository: cocoon.parameters.repository,
      resource: cocoon.parameters.resource,
      uuid: uuid
    }
  );
}
