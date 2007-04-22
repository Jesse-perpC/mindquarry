package com.mindquarry.dma.webapp;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;

import dax.Path;
import dax.Transformer;

public class PropfindHandler extends Transformer implements RequestHandler{
	/**
	 * Namespace for Subversion properties
	 */
	private static final String SVN_NS = "http://subversion.tigris.org/xmlns/dav/";
	/**
	 * Namepace for DAV properties
	 */
	private static final String DAV_NS = "DAV:";
	/**
	 * The output document tree
	 */
	private Document output;
	/**
	 * The context node of the output document
	 */
	private Node current;
	/**
	 * The servlet response
	 */
	private HttpServletResponse response;
	/**
	 * The servlet request
	 */
	private HttpServletRequest request;

	public boolean handle(HttpServletRequest request,
			HttpServletResponse response) throws DocumentException, IOException {
		String method = request.getMethod();
		if (!method.equals("PROPFIND")) {
			return false;
		}
		SAXReader reader = new SAXReader();
		Document input = reader.read(request.getInputStream());
		this.output = DocumentFactory.getInstance().createDocument();
		this.current = this.output;
		this.response = response;
		this.request = request;
		this.execute(input);
		this.setNamespace("dav", DAV_NS);
		this.setNamespace("svn", SVN_NS);
		Writer writer = response.getWriter();
		output.write(writer);
		writer.close();
		return true;
	}
	
	@Path("/dav:propfind")
	public void propfind(Node input) {
		//multistatus
		this.response.setStatus(207);
		Element multistatus = this.output.addElement("dav:multistatus", DAV_NS);
		//nested response element
		Element response = multistatus.addElement("dav:response", DAV_NS);
		//href points to the reuqested resource
		Element href = response.addElement("dav:href", DAV_NS);
		href.setText(getReposBasePath()+request.getPathInfo());
		//propstat will contain the result
		Element propstat = response.addElement("dav:propstat", DAV_NS);
		//wait for the properties actually requested
		this.current = propstat;
		applyTemplates();
		//add a status element
		Element status = response.addElement("dav:status", DAV_NS);
		status.setText("HTTP/1.1 200 OK");
	}
	
	private String getReposBasePath() {
		return request.getContextPath()+request.getServletPath();
	}
	
	private String getRepoPath() {
		return getReposBasePath()+getRepoName();
	}
	
	private String getRepoName() {
		return request.getPathInfo().substring(1, request.getPathInfo().substring(1).indexOf("/")+1);
	}

	@Path("dav:prop")
	public void prop(Node input) {
		copy(input);
		applyTemplates();
	}
	/**
	 * Copies the current input element into the result tree. The element copied
	 * to the output tree will be the current output tree node.
	 * @param input
	 */
	private void copy(Node input) {
		Element prop = current().addElement(((Element) input).getQName());
		this.current = prop;
	}
	
	/**
	 * Returns the current element in the output tree.
	 * @return context element of the output tree
	 */
	private Element current() {
		return ((Element) this.current);
	}
	
	@Path("dav:version-controlled-configuration")
	public void versionControlledConfiguration(Node input) {
		copy(input);
		Element href = current().addElement("dav:href",DAV_NS);
		href.setText(getRepoPath()+"!svn/vcc/default");
		pop();
	}

	private void pop() {
		this.current = current().getParent();
	}
	
	@Path("dav:resourcetype")
	public void resourceType(Node input) {
		copy(input);
		pop();
	}
	
	@Path("svn:baseline-relative-path")
	public void baselineRelativePath(Node input) {
		copy(input);
		current().setText(getBaseLineRelativePath());
		pop();
	}

	private String getBaseLineRelativePath() {
		return request.getPathInfo().substring(request.getPathInfo().indexOf("/"));
	}
	@Path("svn:repository-uuid")
	public void repositoryUuid(Node input) {
		copy(input);
		try {
			current.setText(getRepository(getRepoName()).getRepositoryUUID(true));
		} catch (Exception e) {
			error(e.getLocalizedMessage());
		}
		pop();
	}
	
	private void error(String message) {
		try {
			this.response.sendError(500, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public SVNRepository getRepository(String name) throws SVNException {
		FSRepositoryFactory.setup();
		SVNURL repourl = SVNURL.parseURIEncoded("file:///Users/lars/Documents/Software/Mindquarry%20Workspace/mindquarry-dma-javasvn/src/test/resources/com/mindquarry/dma/javasvn/"+name);
		return FSRepositoryFactory.create(repourl);
	}
}
