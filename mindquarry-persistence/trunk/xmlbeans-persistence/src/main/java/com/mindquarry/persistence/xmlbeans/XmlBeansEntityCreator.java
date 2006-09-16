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

    private Map<Class, Method> addNewEntityMethods_;
    
    XmlBeansEntityCreator() {
        Map<Class, Method> emMap = new HashMap<Class, Method>();
        addNewEntityMethods_ = Collections.synchronizedMap(emMap);        
    }
    
    XmlObject newEntityFor(Class entityClazz, XmlObject document) {
        
        Class documentClazz = document.getClass();
        Method addNewEntityMethod = addNewEntityMethods_.get(documentClazz);
        if (null == addNewEntityMethod) {
            addNewEntityMethod = loadAddNewEntityMethod(
                    entityClazz, documentClazz);
            addNewEntityMethods_.put(documentClazz, addNewEntityMethod);
        }
        
        return invokeAddNewEntityMethod(
                document, addNewEntityMethod, entityClazz);
    }

    private XmlObject invokeAddNewEntityMethod(XmlObject document, 
            Method addNewEntityMethod, Class clazz) {
        try {
            Object result = addNewEntityMethod.invoke(document, new Object[0]);
            return (XmlObject) result;
        } catch (IllegalArgumentException e) {
            throw XmlBeansPersistenceException.addNewMethodFailed(
                    addNewEntityMethod.getName(), clazz, e);
        } catch (IllegalAccessException e) {
            throw XmlBeansPersistenceException.addNewMethodFailed(
                    addNewEntityMethod.getName(), clazz, e);
        } catch (InvocationTargetException e) {
            throw XmlBeansPersistenceException.addNewMethodFailed(
                    addNewEntityMethod.getName(), clazz, e);
        }
    }

    private Method loadAddNewEntityMethod(
            Class entityClazz, Class documentClazz) {

        String addNewMethodName = 
            Constants.ADD_NEW_METHOD_PREFIX + entityClazz.getSimpleName();
        try {            
            return documentClazz.getMethod(addNewMethodName, new Class[0]);
        } catch (SecurityException e) {
            throw XmlBeansPersistenceException.gotNoAddNewMethod(
                    addNewMethodName, documentClazz, e);
        } catch (NoSuchMethodException e) {
            throw XmlBeansPersistenceException.gotNoAddNewMethod(
                    addNewMethodName, documentClazz, e);
        }
    }
}
