/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.events.exception;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public class EventAlreadyRegisteredException extends Exception {
    /**
     * Generated serialVersionUID. 
     */
    private static final long serialVersionUID = -7515973185457438094L;
    
    public EventAlreadyRegisteredException(String message) {
        super(message);
    }
}
