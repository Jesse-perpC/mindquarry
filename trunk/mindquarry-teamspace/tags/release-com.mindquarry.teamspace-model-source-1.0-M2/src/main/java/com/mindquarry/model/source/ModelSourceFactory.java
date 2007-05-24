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
package com.mindquarry.model.source;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceFactory;

/**
 * 
 * @author 
 * <a href="mailto:bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public class ModelSourceFactory implements SourceFactory, 
	Configurable, Serviceable {
	
	/**
	 * identifier for component lookup
	 */
	public static final String ROLE = ModelSourceFactory.class.getName();
	
	private static final Log LOG = LogFactory.getLog(ModelSourceFactory.class);
	
	private static Log getLog() {
		return LOG;
	}
	
	
	private ContainerUtil containerUtil_;
	
	public void service(ServiceManager serviceManager) 
        	throws ServiceException {
        containerUtil_ = new ContainerUtil(serviceManager);
    }
	
    
	private Map<String, String> componentShorthands_;

	public void configure(Configuration configuration)
			throws ConfigurationException {
		
		componentShorthands_ = new HashMap<String, String>();
		
		Configuration shorthandConfiguration = 
			configuration.getChild("component-shorthands");
		
		for (Configuration component : shorthandConfiguration.getChildren()) {
			String shorthand = component.getAttribute("shorthand");
			String fullname = component.getAttribute("fullname");
			componentShorthands_.put(shorthand, fullname);
		}
	}
	
	private String componentFullname(String shorthand) {
		if (componentShorthands_.containsKey(shorthand))
			return componentShorthands_.get(shorthand);
		else
			return shorthand;
	}
	
	private String removeProtocol(String url) {
		return url.substring(url.lastIndexOf('/') + 1);
	}

    public Source getSource(String url, Map parameters) 
        throws SourceException, MalformedURLException {
    	
    	getLog().debug("Creating source object for " + url);
    	
    	String resource = removeProtocol(url);
    	String[] resourceParts = resource.split("#");
    	
    	String componentName = componentFullname(resourceParts[0]);        
        Collection<String> statements = parseStatements(resourceParts[1]);
        
    	ModelSourceInterpreter interpreter = 
    		new ModelSourceInterpreter(containerUtil_, componentName, statements);
    	
        
    	ModelSource result = new ModelSource(url, containerUtil_, interpreter);
    	result.initialize();
    	return result;
    }
    
    private Collection<String> parseStatements(String unparsedStatements) {
        StringBuilder remainingStatements = 
            new StringBuilder(unparsedStatements);
        
        Collection<String> result = new LinkedList<String>();
        int i;
        while ( (i = remainingStatements.indexOf(").")) != -1) {
            String statement = remainingStatements.substring(0, i + 1);
            remainingStatements = remainingStatements.delete(0, i + 2);
            result.add(statement);
        }
        result.add(remainingStatements.toString());
        return result;
    }

    /**
     * the release call is only logged, there is nothing to do.
     */
    public void release(Source source) {
        if (null != source) {
        	getLog().debug("released source: " + source.getURI());
        }
    }
}
