/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.user;



/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public interface UserAdmin extends UserQuery {

    public static final String ROLE = UserAdmin.class.getName();
    
    /**
     * creates a new user account
     * @param userId
     * @param password
     * @param first name of the new user
     * @param last name of the new user
     * @param the user's email address
     * @param the skills of the user
     */
    User createUser(String id, String password, 
            String name, String surName, String email, String skills);
    
    User userById(String userId);
    
    void deleteUser(User user);
    
    
    GroupRO createGroup(String groupId);
    
    void deleteGroup(GroupRO group);
    
    
    void addUser(AbstractUserRO user, GroupRO group);
    
    void removeUser(AbstractUserRO user, GroupRO group);
}
