/*
 * Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
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
 * An action that logs all active sitemap parameters.
 * 
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 * 
 */
public class LogParametersAction extends AbstractAction implements ThreadSafe {
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
