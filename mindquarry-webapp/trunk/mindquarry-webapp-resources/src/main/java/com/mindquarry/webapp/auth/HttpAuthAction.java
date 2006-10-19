package com.mindquarry.webapp.auth;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.cocoon.acting.ServiceableAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Response;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.http.HttpResponse;
import org.apache.commons.codec.binary.Base64;

import com.mindquarry.teamspace.Authentication;

public class HttpAuthAction extends ServiceableAction {

	public Map act(Redirector redirect, SourceResolver resolve, Map objectModel, String source,
			Parameters params) throws Exception {
		Request request = ObjectModelHelper.getRequest(objectModel);
		Response response = ObjectModelHelper.getResponse(objectModel);
		String realm = params.getParameter("realm", "Protected Resource");

		//always send an authentication request
		response.setHeader("WWW-Authenticate", "BASIC realm=\""+ realm +"\"");
		
		String auth = request.getHeader("Authorization");
		String user = null;
		if ((auth!=null)&&(auth.toUpperCase().startsWith("BASIC"))) {
			user = getAuthenticatedUser(auth.substring(6));
			
		}
		
		if (user==null) {
			//not authenticated. the browser sends an authentication request
			if (response instanceof HttpResponse) {
				HttpResponse httpresponse = (HttpResponse) response;
				httpresponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
			return EMPTY_MAP;
		} else {
			//authenticated. the username is now available from the sitemap
			Map<String, String> responsemap = new HashMap<String, String>();
			responsemap.put("username", user);
			return responsemap;
		}
	}
	
	private String getAuthenticatedUser(String encodedAuth) throws ServiceException {
		String decodedAuth = new String(Base64.decodeBase64(encodedAuth.getBytes()));
		String[] auths = decodedAuth.split(":");
		
        String authenticatedUser = null;
        if (auths.length==2) {
			String username = auths[0];
			String password = auths[1];
			
            if (lookupAuthentication().authenticate(username, password))
                authenticatedUser = username;
		}
		return authenticatedUser;
	}

    private Authentication lookupAuthentication() throws ServiceException {
        return (Authentication) manager.lookup(Authentication.class.getName());
    }
}
