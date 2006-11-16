/**
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package com.mindquarry.webapp.acting;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.acting.ServiceableAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;

import com.mindquarry.teamspace.Authorisation;

/**
 * An action that handles REST-style authorisation.
 * 
 * <p>
 * REST-style authorization uses an URI to identify the resource which is the
 * object of interest and a HTTP method that will be executed on this resource.
 * The principle is <i>not</i> to call an internal pipeline <i>inside</i> the
 * act element but to throw an exception if the authorisation fails, so that an
 * exception handler in the sitemap can show the corresponding error page. If
 * authorisation was successful, the normal pipeline <i>after</i> the act is
 * called.
 * </p>
 * 
 * <p>
 * Usage: The src attribute is optional, if not given, the URI of the current
 * request is used. The parameter "method" is also optional, if not given, the
 * HTTP method of the current request is used.
 * </p>
 * 
 * Super-simple usage (reading everything from the request):
 * 
 * <pre>
 *     &lt;act type=&quot;authorise&quot;/&gt;
 * </pre>
 * 
 * Fully customized usage:
 * 
 * <pre>
 *     &lt;act type=&quot;authorise&quot; src=&quot;http://some/rest/uri/for/the/resource&quot;&gt;
 *         &lt;parameter name=&quot;method&quot; value=&quot;PUT&quot; /&gt;
 *     &lt;/act&gt;
 * </pre>
 * 
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 * 
 */
public class AuthorisationAction extends ServiceableAction implements ThreadSafe {

    private static final String PARAM_METHOD = "method";

    public Map act(Redirector redirector, SourceResolver resolver,
            Map objectModel, String src, Parameters params) throws Exception {

        Request request = ObjectModelHelper.getRequest(objectModel);

        // first: get URL from src attribute
        String uri = src;
        if (uri == null || uri.equals("")) {
            // second: get URL from request
            uri = request.getRequestURI();
        }

        // first: get method (aka operation, read or write, but HTTP style: GET,
        // PUT, etc.)
        String method = params.getParameter(PARAM_METHOD, null);
        if (method == null) {
            // second: get request method
            method = request.getMethod();
        }

        String username = (String) request.getAttribute("username");
        
        if (username == null || username.equals("")) {
            throw new Exception("Authorisation failed because no authenticated user avaiable.");
        }
        
        if (lookupAuthorisation().authorise(username, uri, method)) {
            // authorised. just return without entering a sub-pipeline
            return null;
        }

        // not authorised. throw exception
        throw new Exception("Authorisation failed for user '" + username
                + "' with method '" + method + "' on '" + uri + "'");
    }

    private Authorisation lookupAuthorisation() throws ServiceException {
        return (Authorisation) manager.lookup(Authorisation.class.getName());
    }
}
