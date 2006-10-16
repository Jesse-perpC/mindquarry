package com.mindquarry.webapp.auth;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Response;
import org.apache.cocoon.environment.SourceResolver;
import javax.servlet.http.HttpServletResponse;
import org.apache.cocoon.environment.http.HttpResponse;
import org.apache.commons.codec.binary.Base64;

public class HttpAuthAction extends AbstractAction {

	public Map act(Redirector redirect, SourceResolver resolve, Map objectModel, String source,
			Parameters params) throws Exception {
		Request request = ObjectModelHelper.getRequest(objectModel);
		Response response = ObjectModelHelper.getResponse(objectModel);
		
		String auth = request.getHeader("Authorization");
		if (auth.toUpperCase().startsWith("BASIC")) {
			String user = getAuthenticatedUser(auth.substring(6));
			if (user==null) {
				//not authenticated. the browser sends an authentication request
				response.setHeader("WWW-Authenticate", "BASIC realm=\""+ params.getParameter("realm") +"\"");
				if (response instanceof HttpResponse) {
					HttpResponse httpresponse = (HttpResponse) response;
					httpresponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				}
			} else {
				//authenticated. the username is now available from the sitemap
				Map responsemap = EMPTY_MAP;
				responsemap.put("username", user);
				return responsemap;
			}
		}
		
		return null;
	}
	
	private String getAuthenticatedUser(String encodedAuth) {
		String decodedAuth = new String(Base64.decodeBase64(encodedAuth.getBytes()));
		String[] auths = decodedAuth.split(":");
		if (auths.length==2) {
			String username = auths[0];
			String password = auths[1];
			//TODO: lookup the user manager and verify the password
		}
		return null;
	}

}
