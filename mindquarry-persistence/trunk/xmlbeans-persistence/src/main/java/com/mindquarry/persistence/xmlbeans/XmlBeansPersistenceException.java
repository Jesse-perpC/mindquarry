/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans;

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

    static XmlBeansPersistenceException documentSuffix(Class clazz) {
        return new XmlBeansPersistenceException(
                "the class: " + clazz + " is not a valid entity class." +
                "you must not use the xmlbeans generated " +
                "'*" + Constants.DOCUMENT_CLASS_SUFFIX + "' classes. " +
                "please find documentation about usage of the " +
                "XmlBeans persistence component in the mindquarry wiki.");
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

    static XmlBeansPersistenceException addNewMethodFailed(
            String methodName, Class clazz, Exception cause) {
        return new XmlBeansPersistenceException(
                "invoke of method " + methodName + " failed. " +
                "could not create " + "new document for " +
                "class " + clazz, cause);
    }
}
