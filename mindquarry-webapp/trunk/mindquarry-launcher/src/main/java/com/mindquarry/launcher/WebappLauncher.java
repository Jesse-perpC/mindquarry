/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.launcher;

import java.io.File;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.NCSARequestLog;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.handler.HandlerCollection;
import org.mortbay.jetty.handler.RequestLogHandler;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.thread.BoundedThreadPool;
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
    	XmlConfiguration configuration = new XmlConfiguration(new File("mindquarry.xml").toURL()); //or use new XmlConfiguration(new FileInputStream("myJetty.xml"));
    	configuration.configure(server);
    	server.start();

        System.out.println("Jetty started."); //$NON-NLS-1$
    }
}
