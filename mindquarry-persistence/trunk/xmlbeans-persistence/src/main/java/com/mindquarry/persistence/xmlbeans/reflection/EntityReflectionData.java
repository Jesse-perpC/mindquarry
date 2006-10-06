/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans.reflection;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class EntityReflectionData {

    private Map<String, Class> entityClazzes_;
    private Map<Class, Method> getIdMethods_;
    private Map<Class, Method> entityFactoryMethods_;
    private Map<Class, Method> getEntityMethods_;
    
    public EntityReflectionData(Set<Class> entityClazzes) {
        
        entityClazzes_ = new HashMap<String, Class>();
        for (Class clazz : entityClazzes) {
            assert isValidEntityClass(clazz);
            entityClazzes_.put(clazz.getName(), clazz);
        }
        
        getIdMethods_ = new HashMap<Class, Method>();
        loadGetIdMethods(entityClazzes);
        
        entityFactoryMethods_ = new HashMap<Class, Method>();
        loadEntityFactoryMethods(entityClazzes);
        
        getEntityMethods_ = new HashMap<Class, Method>();
        loadGetEntityMethods(entityClazzes);
    }
    
    public Method getIdMethod(Class entityClazz) {
        return getIdMethods_.get(entityClazz);
    }
    
    public Method getEntityMethod(Class entityClazz) {
        return getEntityMethods_.get(entityClazz);
    }
    
    public Class classForName(String clazzName) {
        return entityClazzes_.get(clazzName);
    }
    
    private void loadGetIdMethods(Set<Class> entityClazzes) {
        for (Class entityClazz : entityClazzes) {
            Method getIdMethod = loadGetIdMethod(entityClazz);
            getIdMethods_.put(entityClazz, getIdMethod);
        }
    }
    
    private void loadGetEntityMethods(Set<Class> entityClazzes) {
        for (Class entityClazz : entityClazzes) {
            Method getEntityMethod = loadGetEntityMethod(entityClazz);
            getEntityMethods_.put(entityClazz, getEntityMethod);
        }
    }
    
    private Method loadGetIdMethod(Class entityClazz) {
        String methodName = "getId";
        try {
            return entityClazz.getMethod(methodName, new Class[0]);
        } catch (SecurityException e) {
            throw XmlBeansPersistenceReflectionException.gotNoGetIdMethod(
                    methodName, entityClazz, e);
        } catch (NoSuchMethodException e) {
            throw XmlBeansPersistenceReflectionException.gotNoGetIdMethod(
                    methodName, entityClazz, e);
        }
    }
    
    private Method loadGetEntityMethod(Class entityClazz) {
        // load Methods like setUser(User user)
        String methodName = "get" + entityClazz.getSimpleName();
        Class documentClazz = loadDocumentClazz(entityClazz);
        try {
            return documentClazz.getMethod(methodName, new Class[0]);
        } catch (SecurityException e) {
            throw XmlBeansPersistenceReflectionException.gotNoGetEntityMethod(
                    methodName, documentClazz, e);
        } catch (NoSuchMethodException e) {
            throw XmlBeansPersistenceReflectionException.gotNoGetEntityMethod(
                    methodName, documentClazz, e);
        }
    }
    
    private Class loadDocumentClazz(Class entityClazz) {        
        String documentClazzName = 
            entityClazz.getName() + ReflectionConstants.DOCUMENT_CLASS_SUFFIX;
        try {
            return Class.forName(documentClazzName);
        } catch (ClassNotFoundException e) {
            throw XmlBeansPersistenceReflectionException.classNotFound(e);
        }
    }
    
    public Method entityFactoryMethod(Class entityClazz) {
        return entityFactoryMethods_.get(entityClazz);
    }
    
    private void loadEntityFactoryMethods(Set<Class> entityClazzes) {
        for (Class entityClazz : entityClazzes) {
            Method entityFactoryMethod = loadEntityFactoryMethod(entityClazz);
            entityFactoryMethods_.put(entityClazz, entityFactoryMethod);
        }
    }
    
    private Method loadEntityFactoryMethod(Class entityClazz) {                
        Class entityFactoryClazz = entityFactoryClazz(entityClazz);
        try {
            return entityFactoryClazz.getMethod(
                    ReflectionConstants.FACTORY_METHOD, new Class[0]);
        } catch (SecurityException e) {
            throw XmlBeansPersistenceReflectionException.gotNoFactoryMethod(
                    entityClazz, e);
        } catch (NoSuchMethodException e) {
            throw XmlBeansPersistenceReflectionException.gotNoFactoryMethod(
                    entityClazz, e);
        }
    }
    
    private Class entityFactoryClazz(Class entityClazz) {
        return entityClazz.getClasses()[0];
    }
    
    private boolean isValidEntityClass(Class entityClazz) {
        Class[] innerClasses = entityClazz.getClasses();
        if (1 != innerClasses.length)
            return false;
        
        String firstInnerClassName = innerClasses[0].getSimpleName();
        if (! ReflectionConstants.FACTORY_CLASS.equals(firstInnerClassName))
            return false;
        
        return true;
    }
}
