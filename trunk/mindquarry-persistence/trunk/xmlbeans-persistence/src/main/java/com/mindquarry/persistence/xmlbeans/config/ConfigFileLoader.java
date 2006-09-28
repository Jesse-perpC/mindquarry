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
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import com.mindquarry.common.init.InitializationException;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public class ConfigFileLoader {
 
    private static final String CONFIG_FILE = "resource://**/mindquarry-persistence.xml";
    private ServiceManager serviceManager_;
    
    public ConfigFileLoader(ServiceManager serviceManager) {
        serviceManager_ = serviceManager;
    }
    
    public Configuration findAndLoad() {
        InputStream configIn = resolveConfigFile();
        return parse(configIn);
    }
    
    private Configuration parse(InputStream configIn) {
        try {
            return ConfigurationDocument.Factory.parse(
                    configIn, makeParserXmlOptions()).getConfiguration();
        } catch (XmlException e) {
            throw new InitializationException("xmlbeans could " +
                    "not parse persistence configuration file", e);
        } catch (IOException e) {
            throw new InitializationException("xmlbeans could " +
                    "not parse persistence configuration file", e);
        }
    }
    
    private XmlOptions makeParserXmlOptions() {
        XmlOptions result = new XmlOptions();
        result.setCompileDownloadUrls();
        return result;
    }
    
    private InputStream resolveConfigFile() {
        SourceResolver resolver = lookupSourceResolver();
        try {
            return resolver.resolveURI(CONFIG_FILE).getInputStream();
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
