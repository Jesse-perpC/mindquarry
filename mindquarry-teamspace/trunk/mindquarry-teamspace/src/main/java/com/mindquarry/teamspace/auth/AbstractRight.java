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
abstract class AbstractRight {

    final String id;
    private final AbstractUserSet allowed;
    private final AbstractUserSet denied;

    AbstractRight(String id) {
        this.id = id;
        this.allowed = new AbstractUserSet();
        this.denied = new AbstractUserSet();
    }
    
    final void allowAccessTo(AbstractUser user) {
        allowed.add(user);
    }
    
    final void removeAllowanceFor(AbstractUser user) {
        allowed.remove(user);
    }
    
    final void denyAccessTo(AbstractUser user) {
        denied.add(user);
    }
    
    final void removeDenialFor(AbstractUser user) {
        denied.remove(user);
    }
    
    boolean isAccessAllowed(AbstractUser user) {
        return this.allowed.contains(user);
    }
    
    boolean isAccessDenied(AbstractUser user) {
        return this.denied.contains(user);
    }
    
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (! (other instanceof AbstractRight))
            return false;
        
        AbstractRight otherRight = (AbstractRight) other;
        return this.id.equals(otherRight.id);
    }
    
    public int hashCode() {
        int result = 1;
        result = result * 42 + this.id.hashCode();
        return result;
    }
}
