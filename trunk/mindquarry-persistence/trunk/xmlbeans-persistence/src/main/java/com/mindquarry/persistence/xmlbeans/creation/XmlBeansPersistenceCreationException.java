/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans.creation;

import java.lang.reflect.Method;

import com.mindquarry.common.persistence.PersistenceException;

/**
 *
 * @author <a href="your email address">your full name</a>
 */
class XmlBeansPersistenceCreationException extends PersistenceException {

    private static final long serialVersionUID = -4158659740686570627L;

    /**
     * @param message
     */
    XmlBeansPersistenceCreationException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    XmlBeansPersistenceCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    static XmlBeansPersistenceCreationException factoryMethodFailed(
            Class clazz, Exception cause) {
        return new XmlBeansPersistenceCreationException(
                "invoke of factory method failed. could not create " +
                "new document for class " + clazz, cause);
    }

    static XmlBeansPersistenceCreationException setEntityMethodFailed(
            String methodName, Class documentClazz, Exception cause) {
        return new XmlBeansPersistenceCreationException(
                "invoke of '" + methodName + "' failed. could not set entity " +
                "at document with class " + documentClazz, cause);
    }

    static XmlBeansPersistenceCreationException getEntityMethodFailed(
            String methodName, Class documentClazz, Exception cause) {
        return new XmlBeansPersistenceCreationException(
                "invoke of '" + methodName + "' failed. could not get entity " +
                "from document with class " + documentClazz, cause);
    }

    static XmlBeansPersistenceCreationException parseMethodFailed(
            Method parseMethod, Exception cause) {
        return new XmlBeansPersistenceCreationException(
                "invoke of method '" + parseMethod.getName() + "' failed. " +
                "entity document class: " + parseMethod.getClass() + 
                " could not parse jcr stream.", cause);
    }

    static XmlBeansPersistenceCreationException addNewMethodFailed(
            String methodName, Class clazz, Exception cause) {
        return new XmlBeansPersistenceCreationException(
                "invoke of method " + methodName + " failed. " +
                "could not create " + "new document for " +
                "class " + clazz, cause);
    }
}
