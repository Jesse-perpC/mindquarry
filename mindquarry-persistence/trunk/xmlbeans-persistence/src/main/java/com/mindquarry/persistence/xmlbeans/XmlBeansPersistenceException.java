/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans;

import java.lang.reflect.Method;

import com.mindquarry.common.persistence.PersistenceException;

/**
 *
 * @author <a href="your email address">your full name</a>
 */
class XmlBeansPersistenceException extends PersistenceException {

    private static final long serialVersionUID = -4158659740686570627L;

    /**
     * @param message
     */
    XmlBeansPersistenceException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    XmlBeansPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    static XmlBeansPersistenceException classNotFound(
            ClassNotFoundException cause) {
        return new XmlBeansPersistenceException(
                "could not load genereated xmlbean class", cause);
    }

    static XmlBeansPersistenceException gotNoFactoryMethod(
            Class documentFactoryClazz, Exception cause) {
        return new XmlBeansPersistenceException(
                "could not get method '" + Constants.FACTORY_METHOD + "' " +
                "from " + documentFactoryClazz, cause);
    }

    static XmlBeansPersistenceException gotNoSetEntityMethod(
            String methodName, Class documentClazz, Exception cause) {
        return new XmlBeansPersistenceException(
                "could not get method '" + methodName + "' " +
                "from " + documentClazz, cause);
    }

    static XmlBeansPersistenceException gotNoAddNewMethod(
            String methodName, Class documentClazz, Exception cause) {
        return new XmlBeansPersistenceException(
                "could not get method '" + methodName + "' " +
                "from " + documentClazz, cause);
    }

    static XmlBeansPersistenceException factoryMethodFailed(
            Class clazz, Exception cause) {
        return new XmlBeansPersistenceException(
                "invoke of factory method failed. could not create " +
                "new document for class " + clazz, cause);
    }

    static XmlBeansPersistenceException setEntityMethodFailed(
            String methodName, Class documentClazz, Exception cause) {
        return new XmlBeansPersistenceException(
                "invoke of '" + methodName + "' failed. could not set entity " +
                "at document with class " + documentClazz, cause);
    }

    static XmlBeansPersistenceException parseMethodFailed(
            Method parseMethod, Exception cause) {
        return new XmlBeansPersistenceException(
                "invoke of method '" + Constants.PARSE_METHOD + "' failed. " +
                "entity document class: " + parseMethod.getClass() + 
                " could not parse jcr stream.", cause);
    }

    static XmlBeansPersistenceException addNewMethodFailed(
            String methodName, Class clazz, Exception cause) {
        return new XmlBeansPersistenceException(
                "invoke of method " + methodName + " failed. " +
                "could not create " + "new document for " +
                "class " + clazz, cause);
    }
}
