package com.mindquarry.dma.webapp;



import javax.servlet.http.HttpServletRequest;

import org.dom4j.Element;
import org.dom4j.Node;

import dax.Path;

public class PropfindHandler extends SubversionHandler implements RequestHandler{
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
	
	@Path("dav:prop")
	public void prop(Node input) {
		copy(input);
		applyTemplates();
	}
	@Path("dav:version-controlled-configuration")
	public void versionControlledConfiguration(Node input) {
		copy(input);
		Element href = current().addElement("dav:href",DAV_NS);
		href.setText(getVcc());
		pop();
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

	@Override
	boolean canHandle(HttpServletRequest request) {
		return request.getMethod().equals("PROPFIND");
	}
}
