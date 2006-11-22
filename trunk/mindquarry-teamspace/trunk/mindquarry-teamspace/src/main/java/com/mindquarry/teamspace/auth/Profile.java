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
final class Profile extends AbstractRight {

    private final RightSet rights;
    
    Profile(String id) {
        super(id);
        rights = new RightSet();
    }
    
    final void add(Right right) {
        rights.add(right);
    }
    
    final void remove(Right right) {
        rights.remove(right);
    }
}
