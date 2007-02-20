/*
 * Copyright (C) 2006-2007 MindQuarry GmbH, All Rights Reserved
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

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlOptions;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import com.mindquarry.common.init.InitializationException;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public class PersistenceConfigLoader {
	
	static protected Log log = LogFactory.getLog(PersistenceConfigLoader.class);  
	
    public Configuration findAndLoad(String persistenceConfigFile) {
        InputStream configIn = resolveConfig(persistenceConfigFile);
        return parse(configIn);
    }
    
    protected InputStream resolveConfig(String configFile) {
        log.info("lookup xmlbeans persistence " +
                "configuration file at: " + configFile);
        return getClass().getResourceAsStream(configFile);
    }

    protected Configuration parse(InputStream configIn) {
        log.debug("parse xmlbeans persistence configuration");
        try {
            InputStreamReader configReader = new InputStreamReader(configIn);
            return (Configuration) Configuration.unmarshal(configReader);
        } catch (MarshalException e) {
            throw new InitializationException("castor could " +
                    "not parse persistence configuration file", e);
        } catch (ValidationException e) {
            throw new InitializationException("castor could " +
                    "not parse persistence configuration file", e);
        }
    }

    protected XmlOptions makeParserXmlOptions() {
        XmlOptions result = new XmlOptions();
        result.setCompileDownloadUrls();
        return result;
    }

}