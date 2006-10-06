/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans;

import com.mindquarry.common.persistence.PersistenceException;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
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

    static XmlBeansPersistenceException getIdMethodFailed(
            String methodName, Object entity, Exception cause) {
        
        return new XmlBeansPersistenceException("invoke of '" + methodName + 
                "' failed, at " + entity, cause);
    }
}
