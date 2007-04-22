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

import dax.Transformer;

public abstract class SubversionHandler extends Transformer {

	/**
	 * Namespace for Subversion properties
	 */
	private static final String SVN_NS = "http://subversion.tigris.org/xmlns/dav/";
	/**
	 * Namepace for DAV properties
	 */
	protected static final String DAV_NS = "DAV:";
	/**
	 * The output document tree
	 */
	protected Document output;
	/**
	 * The context node of the output document
	 */
	protected Node current;
	/**
	 * The servlet response
	 */
	protected HttpServletResponse response;
	/**
	 * The servlet request
	 */
	protected HttpServletRequest request;

	public SubversionHandler() {
		super();
	}
	
	abstract boolean canHandle(HttpServletRequest request);

	public boolean handle(HttpServletRequest request, HttpServletResponse response) throws DocumentException,
			IOException {
				if (!canHandle(request)) {
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

	protected String getReposBasePath() {
		return request.getContextPath()+request.getServletPath();
	}

	protected String getRepoPath() {
		return getReposBasePath()+getRepoName();
	}

	protected String getRepoName() {
		return request.getPathInfo().substring(1, request.getPathInfo().substring(1).indexOf("/")+1);
	}

	/**
	 * Copies the current input element into the result tree. The element copied
	 * to the output tree will be the current output tree node.
	 * @param input
	 */
	protected void copy(Node input) {
		Element prop = current().addElement(((Element) input).getQName());
		this.current = prop;
	}

	/**
	 * Returns the current element in the output tree.
	 * @return context element of the output tree
	 */
	protected Element current() {
		return ((Element) this.current);
	}

	protected void pop() {
		this.current = current().getParent();
	}

	protected String getBaseLineRelativePath() {
		return request.getPathInfo().substring(request.getPathInfo().substring(1).indexOf("/")+1);
	}

	protected void error(String message) {
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