/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.auth;


/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class Right extends AbstractRight {
       
    final Resource resource;
    final String operation;
    
    /**
     * @param name
     */
    Right(String name, Resource resource, String operation) {    
        super(name);
        this.resource = resource;
        this.operation = operation;
    }
}
