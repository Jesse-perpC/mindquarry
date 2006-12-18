/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.launcher;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.NCSARequestLog;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.deployer.WebAppDeployer;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.handler.HandlerCollection;
import org.mortbay.jetty.handler.RequestLogHandler;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.thread.BoundedThreadPool;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class WebappLauncher {
    public static void main(String[] args) throws Exception {
        Server server = new Server();

        BoundedThreadPool threadPool = new BoundedThreadPool();
        threadPool.setMaxThreads(100);
        server.setThreadPool(threadPool);

        Connector connector = new SelectChannelConnector();
        connector.setPort(8080);
        server.setConnectors(new Connector[] { connector });

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        
        RequestLogHandler requestLogHandler = new RequestLogHandler();
        NCSARequestLog requestLog = new NCSARequestLog("./jetty.log"); //$NON-NLS-1$
        requestLog.setExtended(false);
        requestLogHandler.setRequestLog(requestLog);
        
        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[] { contexts, new DefaultHandler(),
                requestLogHandler });
        server.setHandler(handlers);

        server.setStopAtShutdown(true);
        server.setSendServerVersion(true);

        server.start();

        System.out.println("Jetty started."); //$NON-NLS-1$

//        ContextDeployer deployer = new ContextDeployer();
//        deployer.setContexts((ContextHandlerCollection) server
//                .getChildHandlerByClass(ContextHandlerCollection.class));
//        deployer.setDirectory("webapps");
//        deployer.setScanInterval(1000);
//        deployer.start();

        WebAppDeployer deployer = new WebAppDeployer();
        deployer.setContexts((ContextHandlerCollection) server
                .getChildHandlerByClass(ContextHandlerCollection.class));
        deployer.setWebAppDir("../mindquarry-webapplication/target"); //$NON-NLS-1$
        deployer.start();
    }
}
