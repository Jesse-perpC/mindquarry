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
    private Map<Class, Method> setEntityMethods_;
    
    XmlBeansDocumentCreator() {        
        Map<Class, Method> fmMap = new HashMap<Class, Method>();
        documentFactoryMethods_ = Collections.synchronizedMap(fmMap);
        
        Map<Class, Method> seMap = new HashMap<Class, Method>();
        setEntityMethods_ = Collections.synchronizedMap(seMap);
    }
    
    /**
     * @param entityClazz
     */
    XmlObject newDocumentFor(XmlObject entity, Class entityClazz) {
        XmlObject document = makeEmptyDocument(entityClazz);
        return setEntityInDocument(entity, entityClazz, document);
    }
    
    private XmlObject setEntityInDocument(
            XmlObject entity, Class entityClazz, XmlObject document) {
        
        Method setEntityMethod = setEntityMethod(entityClazz, document);
        invokeSetEntityMethod(entity, document, setEntityMethod);
        return document;
    }

    private void invokeSetEntityMethod(
            XmlObject entity, XmlObject document, Method setEntityMethod) {
        try {
            Object[] params = new Object[] {entity};
            setEntityMethod.invoke(document, params);
        } catch (IllegalArgumentException e) {
            throw XmlBeansPersistenceException.setEntityMethodFailed(
                    setEntityMethod.getName(), document.getClass(),e);
        } catch (IllegalAccessException e) {
            throw XmlBeansPersistenceException.setEntityMethodFailed(
                    setEntityMethod.getName(), document.getClass(),e);
        } catch (InvocationTargetException e) {
            throw XmlBeansPersistenceException.setEntityMethodFailed(
                    setEntityMethod.getName(), document.getClass(),e);
        }
    }
    
    private Method setEntityMethod(Class entityClazz, XmlObject document) {
        Method result = setEntityMethods_.get(entityClazz);
        if (null == result) {
            result = loadSetEntityMethod(entityClazz, document);
            setEntityMethods_.put(entityClazz, result);
        }
        return result;
    }
    
    private Method loadSetEntityMethod(Class entityClazz, XmlObject document) {
        // load Methods like setUser(User user)
        String methodName = "set" + entityClazz.getSimpleName();
        try {            
            Class[] paramTypes = new Class[] {entityClazz};
            return document.getClass().getMethod(methodName, paramTypes);
        } catch (SecurityException e) {
            throw XmlBeansPersistenceException.gotNoSetEntityMethod(
                    methodName, document.getClass(), e);
        } catch (NoSuchMethodException e) {
            throw XmlBeansPersistenceException.gotNoSetEntityMethod(
                    methodName, document.getClass(), e);
        }
    }
    
    private XmlObject makeEmptyDocument(Class entityClazz) {
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
        if (! Constants.FACTORY_CLASS.equals(firstInnerClassName))
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
