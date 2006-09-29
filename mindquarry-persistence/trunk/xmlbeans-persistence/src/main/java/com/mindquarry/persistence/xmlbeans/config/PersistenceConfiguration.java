/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.mindquarry.common.init.InitializationException;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public class PersistenceConfiguration {

    private Map<Class, Entity> entityMap_;
    private Map<String, QueryInfo> queryInfoMap_;
    
    public PersistenceConfiguration(
            PersistenceConfigLoader configFileLoader) {
        
        Configuration xmlBeansConfig = configFileLoader.findAndLoad();
        entityMap_ = makeEntityMap(xmlBeansConfig);
        queryInfoMap_ = makeQueryInfoMap(xmlBeansConfig);
    }
    
    public Set<Class> entityClazzes() {
        return entityMap_.keySet();
    }
    
    public String entityBasePath(Class entityClazz) {
        return entityMap_.get(entityClazz).getPath();
    }
    
    public QueryInfo queryInfo(String queryKey) {
        return queryInfoMap_.get(queryKey);
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
