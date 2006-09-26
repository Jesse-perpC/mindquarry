/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.common.persistence;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public interface SessionFactory {

    public static final String ROLE = "com.mindquarry.common.persistence.SessionFactory";
    
    Session currentSession();
}
