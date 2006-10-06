/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans.reflection;

import com.mindquarry.common.persistence.PersistenceException;

/**
 *
 * @author <a href="your email address">your full name</a>
 */
class XmlBeansPersistenceReflectionException extends PersistenceException {

    private static final long serialVersionUID = -4158659740686570627L;

    /**
     * @param message
     */
    XmlBeansPersistenceReflectionException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    XmlBeansPersistenceReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    static XmlBeansPersistenceReflectionException documentSuffix(Class clazz) {
        return new XmlBeansPersistenceReflectionException(
                "the class: " + clazz + " is not a valid entity class." +
                "you must not use the xmlbeans generated " +
                "'*" + ReflectionConstants.DOCUMENT_CLASS_SUFFIX + "' classes. " +
                "please find documentation about usage of the " +
                "XmlBeans persistence component in the mindquarry wiki.");
    }

    static XmlBeansPersistenceReflectionException classNotFound(
            ClassNotFoundException cause) {
        return new XmlBeansPersistenceReflectionException(
                "could not load genereated xmlbean class", cause);
    }

    static XmlBeansPersistenceReflectionException gotNoGetIdMethod(
            String methodName, Class entityClazz, Exception cause) {
        return new XmlBeansPersistenceReflectionException(
                "could not get method '" + methodName + "' " +
                "from " + entityClazz, cause);
    }

    static XmlBeansPersistenceReflectionException gotNoFactoryMethod(
            Class documentFactoryClazz, Exception cause) {
        return new XmlBeansPersistenceReflectionException(
                "could not get method '" + ReflectionConstants.FACTORY_METHOD + "' " +
                "from " + documentFactoryClazz, cause);
    }

    static XmlBeansPersistenceReflectionException gotNoSetEntityMethod(
            String methodName, Class documentClazz, Exception cause) {
        return new XmlBeansPersistenceReflectionException(
                "could not get method '" + methodName + "' " +
                "from " + documentClazz, cause);
    }

    static XmlBeansPersistenceReflectionException gotNoGetEntityMethod(
            String methodName, Class documentClazz, Exception cause) {
        return new XmlBeansPersistenceReflectionException(
                "could not get method '" + methodName + "' " +
                "from " + documentClazz, cause);
    }

    static XmlBeansPersistenceReflectionException gotNoAddNewMethod(
            String methodName, Class documentClazz, Exception cause) {
        return new XmlBeansPersistenceReflectionException(
                "could not get method '" + methodName + "' " +
                "from " + documentClazz, cause);
    }
}
