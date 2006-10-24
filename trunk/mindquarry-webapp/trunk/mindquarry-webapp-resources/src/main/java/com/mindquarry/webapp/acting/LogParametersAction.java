/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package com.mindquarry.webapp.acting;

import java.util.Enumeration;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
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

        Request req = ObjectModelHelper.getRequest(objectModel);
        Enumeration names = req.getParameterNames();
        if (!names.hasMoreElements()) {
        System.err.println("logging zero parameters... ");
        }
        while(names.hasMoreElements()) {
            String name = names.nextElement().toString();
            String[] values = req.getParameterValues(name);
            for (int i = 0; i < values.length; i++) {
                System.err.println("LogParameters: " + name + " = " + values[i]);                
            }
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
