/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.activity.Initializable;
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
import com.mindquarry.persistence.xmlbeans.config.QueryInfo;

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
    implements SessionFactory, Serviceable, Initializable {
        
    private static final String CONFIG_PATH = "/com/mindquarry/persistence/xmlbeans/mindquarry-persistence.xml";
    
    private Map<Class, Entity> entityMap_;
    private Map<String, QueryInfo> queryInfoMap_;
    
    private ServiceManager serviceManager_;
    
    public XmlBeansSessionFactory() {
        Configuration configuration = loadConfigurationFile();
        entityMap_ = makeEntityMap(configuration);
        queryInfoMap_ = makeQueryInfoMap(configuration);
    }
    
    /**
     * @see com.mindquarry.common.persistence.SessionFactory#currentSession()
     */
    public Session currentSession() {
        return new XmlBeansSession(serviceManager_, entityMap_, queryInfoMap_);
    }
    
    /**
     * @see org.apache.avalon.framework.service.Serviceable#service(org.apache.avalon.framework.service.ServiceManager)
     */
    public void service(ServiceManager serviceManager) throws ServiceException {
        serviceManager_ = serviceManager;
    }

    /**
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() throws Exception {
        //new ConfigFileLoader(serviceManager_).findAndLoad();
    }
    
    private Map<String, QueryInfo> makeQueryInfoMap(
            Configuration configuration) {
        
        Map<String, QueryInfo> result = new HashMap<String, QueryInfo>();
        for (QueryInfo query : configuration.getQueryInfoArray()) {
            result.put(query.getKey(), query);
        }
        return result;
    }
    
    private Map<Class, Entity> makeEntityMap(Configuration configuration) {
        Map<Class, Entity> result = new HashMap<Class, Entity>();
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
            return ConfigurationDocument.Factory.parse(
                    configUrl, xmlOptions).getConfiguration();
        } catch (XmlException e) {
            throw new InitializationException("config file " +
                    "for xmlbeans persistence seems to be invalid", e);
        } catch (IOException e) {
            throw new InitializationException("a config file " +
                    "for xmlbeans persistence is not availabel", e);
        }
    }
}
