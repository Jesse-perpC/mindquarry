/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.castor.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;

import com.mindquarry.common.init.InitializationException;
import com.mindquarry.common.persistence.EntityBase;
import com.mindquarry.persistence.config.Configuration;
import com.mindquarry.persistence.config.Entity;
import com.mindquarry.persistence.config.QueryInfo;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public class PersistenceConfiguration extends AbstractLogEnabled 
    implements Serviceable, Initializable {

    private ServiceManager serviceManager_;
    
    private Map<String, Class> entityClazzMap_;
    private Map<Class, Entity> entityMap_;
    private Map<String, QueryInfo> queryInfoMap_;


    public void service(ServiceManager serviceManager) {
        serviceManager_ = serviceManager;
    }

    public void initialize() {
        Configuration xmlBeansConfig = makeConfigLoader().findAndLoad();
        entityMap_ = makeEntityMap(xmlBeansConfig);
        queryInfoMap_ = makeQueryInfoMap(xmlBeansConfig);        
        entityClazzMap_ = makeEntityClazzMap(entityMap_.keySet());
    }
    
    public boolean existsEntityClazz(Class entityClazz) {
        return entityMap_.containsKey(entityClazz);
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
                getLogger().error("resultEntityClass for query with key: " 
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
        PersistenceConfigLoader result;
        if (null != serviceManager_)
            result = new PersistenceConfigResourceLoader(serviceManager_);
        else 
            result = new PersistenceConfigFileLoader();
        
        result.enableLogging(getLogger());
        return result;
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
