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
public class Configurator {

    private Map<Class, Entity> entities_;    
    private Map<Class, Method> documentFactoryMethods_;
    private Map<Class, Method> setEntityMethods_;
    
    public Configurator(Configuration configuration) {
        entities_ = makeEntityMap(configuration);
    }
    
    public Map<Class, Entity> getEntityMap() {
        return entities_;
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
