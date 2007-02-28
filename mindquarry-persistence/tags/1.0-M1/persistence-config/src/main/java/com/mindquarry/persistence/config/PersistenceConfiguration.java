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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mindquarry.common.init.InitializationException;
import com.mindquarry.common.persistence.EntityBase;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public class PersistenceConfiguration {

	static protected Log log = LogFactory.getLog(PersistenceConfiguration.class);
    
    private Map<String, Class> entityClazzMap_;
    private Map<Class, Entity> entityMap_;
    private Map<String, QueryInfo> queryInfoMap_;
    
    private String persistenceConfigFile_;
    
    /**
     * to configure with spring property mechanism
     */
    public void setPersistenceConfigFile(String configFile) {
    	persistenceConfigFile_ = configFile;
    }

    public void initialize() {
        Configuration xmlBeansConfig = 
        	makeConfigLoader().findAndLoad(persistenceConfigFile_);
        
        entityMap_ = makeEntityMap(xmlBeansConfig);
        queryInfoMap_ = makeQueryInfoMap(xmlBeansConfig);        
        entityClazzMap_ = makeEntityClazzMap(entityMap_.keySet());
    }
    
    public boolean existsEntityClazz(Class entityClazz) {
        return entityMap_.containsKey(entityClazz);
    }
    
    public String entityPath(Class entityClazz) {
        return entityMap_.get(entityClazz).getPath();
    }
    
    /**
     * @param queryKey, specified mindquarry-persistence configuration file
     * @return the query string if a query with the key is configured
     *         otherwise null
     */
    public String query(String queryKey) {
        if (queryInfoMap_.containsKey(queryKey))
            return queryInfoMap_.get(queryKey).getQuery();
        else
            return null;
    }
    
    /**
     * @param queryKey, specified mindquarry-persistence configuration file
     * @return the class of query result entities 
     *         if a query with the key is configured otherwise null
     */
    public String queryResultClass(String queryKey) {
        if (queryInfoMap_.containsKey(queryKey))
            return queryInfoMap_.get(queryKey).getResultEntityClass();
        else 
            return null;
    }
    
    public Collection<Class> entityClazzes() {
        return entityClazzMap_.values();
    }
    
    public Class classForName(String clazzName) {
        return entityClazzMap_.get(clazzName);
    }
    
    private Map<String, Class> makeEntityClazzMap(Set<Class> clazzes) {
        
        Map<String, Class> result = new HashMap<String, Class>();
        for (Class clazz : clazzes) {
            result.put(clazz.getName(), clazz);
        }
        return result;
    }
    
    private Map<String, QueryInfo> makeQueryInfoMap(
            Configuration configuration) {
        
        Map<String, QueryInfo> result = new HashMap<String, QueryInfo>();
        for (QueryInfo query : configuration.getQueryInfo()) {
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
                log.error("resultEntityClass for query with key: " 
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
        for (Entity entity : configuration.getEntity()) {
            Class clazz = loadEntityClass(entity.getClassName());
            result.put(clazz, entity);
        }
        return result;
    }
    
    private Class loadEntityClass(String name) {
        Class<?> clazz = loadClass(name);
        try {
            return clazz.asSubclass(EntityBase.class);
        } catch (ClassCastException e) {
            throw new InitializationException("the class " + clazz +
                    " , configured within mindquarry-persistence.xml" + 
                    " is is not a valid entity class; " +
                    " it have to subclass " + EntityBase.class);
        }
    }
    
    private Class loadClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new InitializationException(
                    "could not a load class configured within " +
                    "mindquarry-persistence.xml", e);
        }
    }
    
    private PersistenceConfigLoader makeConfigLoader() {
    	return new PersistenceConfigLoader();
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
