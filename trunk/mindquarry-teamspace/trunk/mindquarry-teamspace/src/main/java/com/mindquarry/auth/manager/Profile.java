/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.auth.manager;

import com.mindquarry.auth.manager.RightSet;



/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public final class Profile extends AbstractRight {

    private final RightSet rights;
    
    public Profile(String id) {
        super(id);
        rights = new RightSet();
    }
    
    void add(RightEntity right) {
        rights.add(right);
    }
    
    void remove(RightEntity right) {
        rights.remove(right);
    }
}
