/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.castor;

import com.mindquarry.common.persistence.PersistenceException;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class CastorPersistenceException extends PersistenceException {

    private static final long serialVersionUID = -4158659740686570627L;

    /**
     * @param message
     */
    CastorPersistenceException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    CastorPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    static CastorPersistenceException newInstanceFailed(
            Class entityClazz, Exception cause) {
        return new CastorPersistenceException(
                "invoke of default constructor failed; " +
                "could not create instance from " + entityClazz, cause);
    }
}
