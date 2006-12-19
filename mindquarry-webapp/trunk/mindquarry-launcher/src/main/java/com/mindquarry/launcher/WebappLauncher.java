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
        Server server = new Server();
        XmlConfiguration configuration = new XmlConfiguration(new File(
                "mindquarry-web.xml").toURL()); //$NON-NLS-1$
        configuration.configure(server);
        server.start();
    }
}
