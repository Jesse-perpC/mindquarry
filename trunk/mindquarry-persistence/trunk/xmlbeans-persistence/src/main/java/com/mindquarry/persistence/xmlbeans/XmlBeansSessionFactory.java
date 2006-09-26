/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import com.mindquarry.common.init.InitializationException;
import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;
import com.mindquarry.persistence.xmlbeans.config.Configuration;
import com.mindquarry.persistence.xmlbeans.config.ConfigurationDocument;
import com.mindquarry.persistence.xmlbeans.config.Entity;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public class XmlBeansSessionFactory extends AbstractLogEnabled 
    implements SessionFactory, Serviceable {
        
    private static final String CONFIG_PATH = "/com/mindquarry/persistence/xmlbeans/config.xml";
    
    private Map<Class, Entity> entityMap_;
    private ServiceManager serviceManager_;
    
    public XmlBeansSessionFactory() {
        Configuration configuration = loadConfigurationFile();
        entityMap_ = makeEntityMap(configuration);
    }
    
    /**
     * @see com.mindquarry.common.persistence.SessionFactory#currentSession()
     */
    public Session currentSession() {
        return new XmlBeansSession(serviceManager_, entityMap_);
    }
    
    /**
     * @see org.apache.avalon.framework.service.Serviceable#service(org.apache.avalon.framework.service.ServiceManager)
     */
    public void service(ServiceManager serviceManager) throws ServiceException {
        serviceManager_ = serviceManager;
    }
    
    private Map<Class, Entity> makeEntityMap(Configuration configuration) {
        Map<Class, Entity> result = new HashMap<Class, Entity>();
        Entity[] entities = configuration.getEntityArray();
        for (Entity entity : configuration.getEntityArray()) {
            Class clazz = loadClass(entity.getClassName());
            result.put(clazz, entity);
        }
        return result;
    }
    
    private Class loadClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new InitializationException(
                    "could not load entity class", e);
        }
    }    
    
    private Configuration loadConfigurationFile() {
        URL configUrl = getClass().getResource(CONFIG_PATH);
        try {
            XmlOptions xmlOptions = new XmlOptions();
            xmlOptions.setCompileDownloadUrls();
            xmlOptions.setValidateOnSet();
            return ConfigurationDocument.Factory.parse(configUrl, xmlOptions).getConfiguration();
        } catch (XmlException e) {
            throw new InitializationException("config file " +
                    "for xmlbeans persistence seems to be invalid", e);
        } catch (IOException e) {
            throw new InitializationException("a config file " +
                    "for xmlbeans persistence is not availabel", e);
        }
    }
}
