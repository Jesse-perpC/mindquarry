/*
 * Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
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
package com.mindquarry.auth.manager;

import com.mindquarry.user.AbstractUserRO;
import com.mindquarry.user.manager.AbstractUserSet;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
abstract class AbstractRight {

    private String id;
    private final AbstractUserSet allowed;
    private final AbstractUserSet denied;

    public AbstractRight() {
        this("");
    }
    
    public AbstractRight(String id) {
        this.id = id;
        this.allowed = new AbstractUserSet();
        this.denied = new AbstractUserSet();
    }
    
    
    
    final void allowAccessTo(AbstractUserRO user) {
        allowed.add(user);
    }
    
    final void removeAllowanceFor(AbstractUserRO user) {
        allowed.remove(user);
    }
    
    final void denyAccessTo(AbstractUserRO user) {
        denied.add(user);
    }
    
    final void removeDenialFor(AbstractUserRO user) {
        denied.remove(user);
    }
    
    boolean isAccessAllowed(AbstractUserRO user) {
        return this.allowed.contains(user);
    }
    
    boolean isAccessDenied(AbstractUserRO user) {
        return this.denied.contains(user);
    }
    
    public final boolean equals(Object other) {
        if (this == other)
            return true;
        if (! (other instanceof AbstractRight))
            return false;
        
        AbstractRight otherRight = (AbstractRight) other;
        return this.id.equals(otherRight.id);
    }
    
    public final int hashCode() {
        int result = 1;
        result = result * 42 + this.id.hashCode();
        return result;
    }
}
