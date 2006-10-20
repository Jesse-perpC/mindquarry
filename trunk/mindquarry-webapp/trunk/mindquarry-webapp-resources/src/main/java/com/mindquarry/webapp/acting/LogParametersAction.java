/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package com.mindquarry.webapp.acting;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;

/**
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 * 
 */
public class LogParametersAction extends AbstractAction implements ThreadSafe {

    /**
     * An action that logs all active sitemap parameters.
     * 
     */
    public Map act(Redirector redirector, SourceResolver resolver,
            Map objectModel, String src, Parameters par) throws Exception {

        Map requestParams = ObjectModelHelper.getRequest(objectModel).getParameters();
        System.err.println("logging parameters... " + requestParams.size());
        for (Object key : requestParams.keySet()) {
            String name = (String) key;
            System.err.println("LogParameters: " + name + " = " + requestParams.get(name));
            
        }
//        String[] paramNames = par.getNames();
//        System.err.println("logging parameters... " + paramNames.length);
//        for (String name : paramNames) {
//            System.err.println("LogParameters: " + name + " = " + par.getParameter(name));
//            getLogger().info("LogParameters: " + name + " = " + par.getParameter(name));            
//        }

        return null;
    }

}
