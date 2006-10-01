/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.excalibur.source.SourceResolver;

import com.mindquarry.common.init.InitializationException;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public class PersistenceConfigResourceLoader extends PersistenceConfigLoader {
 
    private static final String CONFIG_RESOURCE = "resource://com/mindquarry/persistence/xmlbeans/mindquarry-persistence.xml";
    
    private ServiceManager serviceManager_;
    
    public PersistenceConfigResourceLoader(ServiceManager serviceManager) {
        serviceManager_ = serviceManager;
    }
    
    @Override
    protected InputStream resolveConfig() {
        getLogger().info("lookup xmlbeans persistence " +
                "configuration file at: " + CONFIG_RESOURCE);
        
        SourceResolver resolver = lookupSourceResolver();
        try {
            return resolver.resolveURI(CONFIG_RESOURCE).getInputStream();
        } catch (MalformedURLException e) {
            throw new InitializationException(
                    "could not load persistence configuration file", e);
        } catch (IOException e) {
            throw new InitializationException(
                    "could not load persistence configuration file", e);
        }
    }
    
    private SourceResolver lookupSourceResolver() {
        try {
            return (SourceResolver) serviceManager_.lookup(SourceResolver.ROLE);
        } catch (ServiceException e) {
            throw new InitializationException(
                    "lookup of " + SourceResolver.ROLE + " failed", e);
        }
    }
}
