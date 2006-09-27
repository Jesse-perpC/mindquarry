/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlObject;

/**
 *
 * @author <a href="your email address">your full name</a>
 */
class XmlBeansEntityCreator {

    private Map<Class, Method> entityFactoryMethods_;
    
    XmlBeansEntityCreator() {        
        Map<Class, Method> fmMap = new HashMap<Class, Method>();
        entityFactoryMethods_ = Collections.synchronizedMap(fmMap);
    }
    
    XmlObject newEntityFor(Class entityClazz) {
        Method factoryMethod = entityFactoryMethod(entityClazz);
        return invokeEntityFactoryMethod(entityClazz, factoryMethod);
    }

    private XmlObject invokeEntityFactoryMethod(
            Class clazz, Method factoryMethod) {
        try {
            return (XmlObject) factoryMethod.invoke(null, new Object[0]);
        } catch (IllegalArgumentException e) {
            throw XmlBeansPersistenceException.factoryMethodFailed(clazz, e);
        } catch (IllegalAccessException e) {
            throw XmlBeansPersistenceException.factoryMethodFailed(clazz, e);
        } catch (InvocationTargetException e) {
            throw XmlBeansPersistenceException.factoryMethodFailed(clazz, e);
        }
    }
    
    private Method entityFactoryMethod(Class entityClazz) {
        Method result = entityFactoryMethods_.get(entityClazz);
        if (null == result) {
            result = loadEntityFactoryMethod(entityClazz);
            entityFactoryMethods_.put(entityClazz, result);
        }
        return result;
    }
    
    private Method loadEntityFactoryMethod(Class entityClazz) {                
        Class entityFactoryClazz = entityFactoryClazz(entityClazz);
        try {
            return entityFactoryClazz.getMethod(Constants.FACTORY_METHOD, new Class[0]);
        } catch (SecurityException e) {
            throw XmlBeansPersistenceException.gotNoFactoryMethod(
                    entityClazz, e);
        } catch (NoSuchMethodException e) {
            throw XmlBeansPersistenceException.gotNoFactoryMethod(
                    entityClazz, e);
        }
    }
    
    private Class entityFactoryClazz(Class entityClazz) {
        assert isValidEntityClass(entityClazz);        
        return entityClazz.getClasses()[0];
    }
    
    private boolean isValidEntityClass(Class entityClazz) {
        Class[] innerClasses = entityClazz.getClasses();
        if (1 != innerClasses.length)
            return false;
        
        String firstInnerClassName = innerClasses[0].getSimpleName();
        if (! Constants.FACTORY_CLASS.equals(firstInnerClassName))
            return false;
        
        return true;
    }
}
