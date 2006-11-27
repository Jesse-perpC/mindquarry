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
public interface User extends UserRO {
    
    boolean changePassword(String oldPwd, String newPwd);
    
    void setName(String name);
    
    void setSurname(String surname);
    
    void setEmail(String email);
    
    void setSkills(String skills);
}
