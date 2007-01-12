/*
 * Copyright (C) 2006-2007 MindQuarry GmbH, All Rights Reserved
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
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
