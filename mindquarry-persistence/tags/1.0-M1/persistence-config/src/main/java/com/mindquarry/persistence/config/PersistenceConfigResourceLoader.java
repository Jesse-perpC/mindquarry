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
package com.mindquarry.persistence.config;

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
    
    private ServiceManager serviceManager_;
    
    public PersistenceConfigResourceLoader(ServiceManager serviceManager) {
        serviceManager_ = serviceManager;
    }
    
    @Override
    protected InputStream resolveConfig(String configFile) {
    	String configResource = "resource:/" + configFile;
        log.info("lookup xmlbeans persistence " +
                "configuration file at: " + configResource);
        
        SourceResolver resolver = lookupSourceResolver();
        try {
            return resolver.resolveURI(configResource).getInputStream();
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
