/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
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
