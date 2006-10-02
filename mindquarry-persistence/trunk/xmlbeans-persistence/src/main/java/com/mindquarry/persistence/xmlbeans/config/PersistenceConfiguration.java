/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans.config;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.avalon.framework.logger.Logger;

import com.mindquarry.common.init.InitializationException;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public class PersistenceConfiguration {

    private Logger logger_;
    private Map<Class, Entity> entityMap_;
    private Map<String, QueryInfo> queryInfoMap_;
    
    public PersistenceConfiguration(Logger logger,
            PersistenceConfigLoader configFileLoader) {
    
        logger_ = logger;
        Configuration xmlBeansConfig = configFileLoader.findAndLoad();
        entityMap_ = makeEntityMap(xmlBeansConfig);
        queryInfoMap_ = makeQueryInfoMap(xmlBeansConfig);
    }
    
    public boolean existsEntity(Class entityClazz) {
        return entityMap_.containsKey(entityClazz);
    }
    
    public Set<Class> entityClazzes() {
        return entityMap_.keySet();
    }
    
    public String entityBasePath(Class entityClazz) {
        return entityMap_.get(entityClazz).getPath();
    }
    
    public String query(String queryKey) {
        return queryInfoMap_.get(queryKey).getQuery();
    }
    
    public String queryResultClass(String queryKey) {
        return queryInfoMap_.get(queryKey).getResultEntityClass();
    }
    
    private Map<String, QueryInfo> makeQueryInfoMap(
            Configuration configuration) {
        
        Map<String, QueryInfo> result = new HashMap<String, QueryInfo>();
        for (QueryInfo query : configuration.getQueryInfoArray()) {
            result.put(query.getKey(), query);
        }
        assert isQueryInfoMapValid(result);
        return result;
    }
    
    private boolean isQueryInfoMapValid(
            Map<String, QueryInfo> queryInfoMap) {
        
        List<String> entityClassNames = new LinkedList<String>();
        for (Class clazz : entityMap_.keySet()) 
            entityClassNames.add(clazz.getName());
        
        boolean isValid = true;
        for (QueryInfo queryInfo : queryInfoMap.values()) {
            String resultEntityClazzName = queryInfo.getResultEntityClass();
            
            if (! entityClassNames.contains(resultEntityClazzName)) {
                logger_.error("resultEntityClass for query with key: " 
                        + queryInfo.getKey() 
                        + " is not defined as entity class");

                isValid = false;
                
                // continue loop to get all possible configuration errors
                // and maximize feedback
            }   
        }
        return isValid;
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
    
    public String toString() {
        StringBuilder resultSB = new StringBuilder();
        resultSB.append("PersistenceConfiguration: ");
        resultSB.append("entityMap=");
        resultSB.append(entityMap_);
        resultSB.append(' ');
        resultSB.append("queryInfoMap=");
        resultSB.append(queryInfoMap_);
        return resultSB.toString();
    }
}
