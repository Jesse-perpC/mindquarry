/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.webapp.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Loads the Mindquarry system properties from a properties file.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class ConfigLoaderListener implements ServletContextListener {
    public static final String PROP_FILE_NAME = "mindquarry-webapplication.properties"; //$NON-NLS-1$
    
    private Log log = LogFactory.getLog(ConfigLoaderListener.class);

    /**
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent ctxEvent) {
        // nothing to do here
    }

    /**
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent ctxEvent) {
        try {
            // load props file
            File propFile = new File(PROP_FILE_NAME);
            FileInputStream is = new FileInputStream(propFile);

            // load props
            Properties props = new Properties();
            props.load(is);
            
            // add props to system props
            Properties sysProps = System.getProperties();
            sysProps.putAll(props);
            System.setProperties(sysProps);
        } catch (Exception e) {
            log.error("An error occured while loading system properties.", e);
            System.exit(-1);
        }
    }
}
