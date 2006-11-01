/**
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package com.mindquarry.webapp.acting;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;

/**
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com"> Alexander
 *         Saar</a>
 * 
 */
public class HttpOperationEvaluatorAction extends AbstractAction implements
        ThreadSafe {
    
    public Map act(Redirector redirector, SourceResolver resolver,
            Map objectModel, String src, Parameters par) throws Exception {
        Request request = ObjectModelHelper.getRequest(objectModel);
        String op = request.getMethod();
        
        Map<String, String> sitemapParams = new HashMap<String, String>();
        sitemapParams.put("httpOp", op); //$NON-NLS-1$

        return sitemapParams;
    }
}
