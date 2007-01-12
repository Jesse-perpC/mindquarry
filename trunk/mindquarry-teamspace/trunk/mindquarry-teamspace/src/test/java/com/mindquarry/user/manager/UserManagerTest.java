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
package com.mindquarry.user.manager;

import java.util.List;

import org.apache.avalon.framework.service.ServiceException;

import com.mindquarry.teamspace.TeamspaceTestBase;
import com.mindquarry.user.Authentication;
import com.mindquarry.user.GroupRO;
import com.mindquarry.user.User;
import com.mindquarry.user.UserAdmin;
import com.mindquarry.user.UserRO;

public class UserManagerTest extends TeamspaceTestBase {
    
    public void testMd5Password() throws ServiceException {
        // please note, an admin users is created within the initialize method

        UserAdmin userAdmin = lookupUserAdmin();
        
        String userId = "mindquarry-user";
        User mqUser = userAdmin.createUser(userId, "aSecretPassword",
                "Mindquarry User", "surname", "an email", "the skills");
        
        mqUser.changePassword("aSecretPassword", "newPwd");
        userAdmin.updateUser(mqUser);
        
        Authentication auth = (Authentication) lookup(Authentication.ROLE);
        assertTrue(auth.authenticate(userId, "newPwd"));
    }
    
    public void testCreateAndRemoveUser() throws ServiceException {
        // please note, an admin users is created within the initialize method

        UserAdmin userAdmin = lookupUserAdmin();
        
        String userId = "mindquarry-user";
        UserRO mqUser = userAdmin.createUser(userId, "aSecretPassword",
                "Mindquarry User", "surname", "an email", "the skills");
        
        List<UserRO> users = userAdmin.allUsers();
        assertEquals(1, users.size());
        
        userAdmin.deleteUser((User) mqUser);
        assertEquals(0, userAdmin.allUsers().size());
    }
    
    public void testGroupPersistence() throws ServiceException {
        // please note, an admin users is created within the initialize method

        UserAdmin userAdmin = lookupUserAdmin();
    
        String userId = "mindquarry-user";
        UserRO mqUser = userAdmin.createUser(userId, "aSecretPassword",
            "Mindquarry User", "surname", "an email", "the skills");
    
        GroupRO group = userAdmin.createGroup("mindquarry");    
        userAdmin.addUser(mqUser, group);
        
        GroupRO persistentGroup = userAdmin.groupById("mindquarry");
        assertTrue(persistentGroup.contains(mqUser));
        userAdmin.removeUser(mqUser, persistentGroup);
        
        userAdmin.deleteGroup(group);        
        userAdmin.deleteUser((User) mqUser);
    }
}