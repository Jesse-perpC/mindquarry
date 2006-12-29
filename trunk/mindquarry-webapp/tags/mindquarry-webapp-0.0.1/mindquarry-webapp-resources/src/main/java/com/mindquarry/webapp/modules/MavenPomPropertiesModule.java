/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package com.mindquarry.webapp.modules;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.cocoon.components.modules.input.AbstractInputModule;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;

/**
 * Gets the properties from a Maven pom.properties file included in jars.
 * The format for the attribute names is:
 * <pre>
 *  groupId/artifactId/property
 * </pre>
 * 
 * For example, to retrieve the version of the mindquarry-common project:
 * <pre>
 *  {pom-property:com.mindquarry.common/mindquarry-common/version}
 * </pre>
 * 
 * All properties are cached since it is assumed that the pom.properties file
 * inside a jar does not change during runtime.
 * 
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 *
 */
public class MavenPomPropertiesModule extends AbstractInputModule {
    
    private Map<String, String> propertyCache = new HashMap<String, String>();

    private final ServletContextResourcePatternResolver resolver =
        new ServletContextResourcePatternResolver(new DefaultResourceLoader());
    
    @Override
    public Object getAttribute(final String name, Configuration config, Map objectModel) throws ConfigurationException {
        // check cache first
        if (propertyCache.containsKey(name)) {
            return propertyCache.get(name);
        }
        
        int splitPos = name.lastIndexOf('/');
        if (splitPos < 0) {
            return null;
        }
        
        String pomPath = name.substring(0, splitPos);
        String propertyName = name.substring(splitPos+1);
        
        // we are using Spring's resource lib for access to the stuff below
        // META-INF where maven puts the pom.properties during jar packaging
        pomPath = "classpath:/META-INF/maven/" + pomPath + "/pom.properties";
        
        Properties pomProperties = new Properties();
        try {
            Resource resource = resolver.getResource(pomPath);
            InputStream stream = resource.getInputStream();
            
            if (stream != null) {
                pomProperties.load(stream);
            }
        } catch (IOException e) {
            throw new ConfigurationException("Could not load pom.properties (" + pomPath + ")", e);
        }
        
        String value = pomProperties.getProperty(propertyName);
        
        if (value != null) {
            // store this value in the cache
            propertyCache.put(name, value);
        }
        
        return value;
    }

}
