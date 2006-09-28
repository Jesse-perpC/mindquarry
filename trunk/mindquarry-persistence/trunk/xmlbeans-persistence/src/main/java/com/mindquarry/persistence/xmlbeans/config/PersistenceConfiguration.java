/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans.config;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.mindquarry.common.init.InitializationException;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public class PersistenceConfiguration {

    private Map<Class, Entity> entityMap_;
    private Map<String, QueryInfo> queryInfoMap_;
    
    private Map<Class, Method> documentFactoryMethods_;
    private Map<Class, Method> setEntityMethods_;
    
    public PersistenceConfiguration(
            PersistenceConfigLoader configFileLoader) {
        
        Configuration xmlBeansConfig = configFileLoader.findAndLoad();
        entityMap_ = makeEntityMap(xmlBeansConfig);
        queryInfoMap_ = makeQueryInfoMap(xmlBeansConfig);
    }
    
    public Map<Class, Entity> getEntityMap() {
        return entityMap_;
    }
    
    public Map<String, QueryInfo> getQueryInfoMap() {
        return queryInfoMap_;
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
}
