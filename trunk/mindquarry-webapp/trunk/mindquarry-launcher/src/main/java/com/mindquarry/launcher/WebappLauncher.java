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
package com.mindquarry.launcher;

import java.io.File;

import org.mortbay.jetty.Server;
import org.mortbay.xml.XmlConfiguration;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class WebappLauncher {
    public static void main(String[] args) throws Exception {
        System.out.println("Running in: " + System.getProperty("user.dir"));
        System.out.println("Jetty's HOME is: " + System.getProperty("jetty.home"));
        if(args.length < 1) {
            args = new String[1];
            args[0] = "mindquarry-web.xml"; //$NON-NLS-1$
            
            System.out.println("No web config file found. Using default."); //$NON-NLS-1$
        }
        Server server = new Server();
        XmlConfiguration configuration = new XmlConfiguration(new File(
                args[0]).toURL());
        configuration.configure(server);
        server.start();
    }
}
