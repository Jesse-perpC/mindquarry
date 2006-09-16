/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans;

import com.mindquarry.common.persistence.PersistenceException;

/**
 *
 * @author <a href="your email address">your full name</a>
 */
public class XmlBeansPersistenceException extends PersistenceException {

    private static final long serialVersionUID = -4158659740686570627L;

    /**
     * @param message
     */
    public XmlBeansPersistenceException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public XmlBeansPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

}
