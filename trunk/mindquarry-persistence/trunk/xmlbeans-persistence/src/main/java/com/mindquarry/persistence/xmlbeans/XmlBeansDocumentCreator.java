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
class XmlBeansDocumentCreator {

    private Map<Class, Method> documentFactoryMethods_;    
    
    XmlBeansDocumentCreator() {        
        Map<Class, Method> fmMap = new HashMap<Class, Method>();
        documentFactoryMethods_ = Collections.synchronizedMap(fmMap);
    }
    
    /**
     * @param entityClazz
     */
    XmlObject newDocumentFor(Class entityClazz) {
        Method factoryMethod = documentFactoryMethod(entityClazz);
        return invokeDocumentFactoryMethod(entityClazz, factoryMethod);
    }

    private XmlObject invokeDocumentFactoryMethod(
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
    
    private Method documentFactoryMethod(Class entityClazz) {
        Method result = documentFactoryMethods_.get(entityClazz);
        if (null == result) {
            result = loadDocumentFactoryMethod(entityClazz);
            documentFactoryMethods_.put(entityClazz, result);
        }
        return result;
    }
    
    private Method loadDocumentFactoryMethod(Class entityClazz) {        
        Class documentClazz = loadDocumentClazz(entityClazz);        
        Class documentFactoryClazz = documentFactoryClazz(documentClazz);
        try {
            return documentFactoryClazz.getMethod(Constants.FACTORY_METHOD, new Class[0]);
        } catch (SecurityException e) {
            throw XmlBeansPersistenceException.gotNoFactoryMethod(
                    documentFactoryClazz, e);
        } catch (NoSuchMethodException e) {
            throw XmlBeansPersistenceException.gotNoFactoryMethod(
                    documentFactoryClazz, e);
        }
    }
    
    private Class documentFactoryClazz(Class documentClazz) {
        assert isValidDocumentClass(documentClazz);        
        return documentClazz.getClasses()[0];
    }
    
    private boolean isValidDocumentClass(Class documentClazz) {
        Class[] innerClasses = documentClazz.getClasses();
        if (1 != innerClasses.length)
            return false;
        
        String firstInnerClassName = innerClasses[0].getSimpleName();
        if (! Constants.DOCUMENT_FACTORY_CLASS.equals(firstInnerClassName))
            return false;
        
        return true;
    }
    
    private Class loadDocumentClazz(Class entityClazz) {        
        String documentClazzName = 
            entityClazz.getName() + Constants.DOCUMENT_CLASS_SUFFIX;
        try {
            return classLoader().loadClass(documentClazzName);
        } catch (ClassNotFoundException e) {
            throw XmlBeansPersistenceException.classNotFound(e);
        }
    }
    
    private ClassLoader classLoader() {
        return getClass().getClassLoader();
    }
}
