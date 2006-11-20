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
class Group extends AbstractUser {

    private final AbstractUserSet members;
    
    Group(String groupId) {
        super(groupId);
        members = new AbstractUserSet();
    }
    
    final void add(AbstractUser user) {
        members.add(user);
    }
    
    final void remove(AbstractUser user) {
        members.remove(user);
    }
    
    final boolean contains(AbstractUser user) {
        return members.contains(user);
    }
}
