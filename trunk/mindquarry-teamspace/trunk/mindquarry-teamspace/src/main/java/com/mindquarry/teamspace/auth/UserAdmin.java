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
public interface UserAdmin {

    User createUser(String userId);
    
    User userById(String userId);
    
    void deleteUser(User user);
    
    
    Group createGroup(String groupId);
    
    Group groupById(String groupId);
    
    void deleteGroup(Group group);
    
    
    void addUser(AbstractUser user, Group group);
    
    void removeUser(AbstractUser user, Group group);
    
    
    void addAllowance(AbstractRight right, AbstractUser user);
    
    void removeAllowance(AbstractRight right, AbstractUser user);
    
    void addDenial(AbstractRight right, AbstractUser user);
    
    void removeDenial(AbstractRight right, AbstractUser user);
}
