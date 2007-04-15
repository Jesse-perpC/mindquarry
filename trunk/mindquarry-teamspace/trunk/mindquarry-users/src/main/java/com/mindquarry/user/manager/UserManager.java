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

import static com.mindquarry.user.manager.DefaultUsers.isAdminUser;
import static com.mindquarry.user.manager.DefaultUsers.isIndexUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.mindquarry.persistence.api.Session;
import com.mindquarry.persistence.api.SessionFactory;
import com.mindquarry.user.AbstractUserRO;
import com.mindquarry.user.Authentication;
import com.mindquarry.user.RoleRO;
import com.mindquarry.user.User;
import com.mindquarry.user.UserAdmin;
import com.mindquarry.user.UserRO;

/**
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public final class UserManager implements UserAdmin, Authentication {
    
    private SessionFactory sessionFactory_;

    /**
     * Setter for sessionFactory bean, set by spring at object creation
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        sessionFactory_ = sessionFactory;
    }

    private void persistEntity(Object entity) {
        Session session = currentSession();
        session.persist(entity);
        session.commit();
    }

    private void deleteEntity(Object entity) {
        Session session = currentSession();
        session.delete(entity);
        session.commit();
    }

    private void updateEntity(Object entity) {
        Session session = currentSession();
        session.update(entity);
        session.commit();
    }

    /**
     * @returns an entity object if it can be found otherwise null
     */
    private Object queryEntityById(String query, String id) {
        Session session = currentSession();
        List queryResult = session.query(query, id);
        if (queryResult.size() == 1)
            return queryResult.get(0);
        else
            return null;
    }

    private Session currentSession() {
        return sessionFactory_.currentSession();
    }

    public final boolean isValidUserId(String userId) {
        return (null != userId) && (!"".equals(userId));
    }

    public User createUser(String id, String password, String name,
            String surname, String email, String skills) {
        UserEntity user = new UserEntity();
        user.setId(id);
        // the default constructor sets an empty string password
        user.changePassword("", password);
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setSkills(skills);

        persistEntity(user);
        return user;
    }

    public void updateUser(User user) {
        UserEntity userEntity = (UserEntity) user;
        updateEntity(userEntity);
    }

    public void deleteUser(User user) {
        UserEntity userEntity = (UserEntity) user;
        deleteEntity(userEntity);
    }

    public UserEntity userById(String userId) {
        return (UserEntity) queryEntityById(UserEntity.BY_ID, userId);
    }

    public Collection<? extends UserRO> allUsers() {
        Session session = currentSession();

        List<Object> queriedUsers = session.query(UserEntity.ALL);
        List<UserRO> result = new LinkedList<UserRO>();

        for (Object userObj : queriedUsers) {
            UserRO user = (UserRO) userObj;

            if ((!isAdminUser(user)) && (!isIndexUser(user)))
                result.add(user);
        }
        return result;
    }

    /**
     * @see com.mindquarry.user.Authentication#authenticate(java.lang.String,
     *      java.lang.String)
     */
    public boolean authenticate(String userId, String password) {
        UserEntity user = userById(userId);
        return (user != null) && user.authenticate(password);
    }

    public RoleRO createRole(String roleId) {
        RoleEntity result = new RoleEntity();
        result.setId(roleId);
        persistEntity(result);
        return result;
    }

    public void deleteRole(RoleRO role) {
        RoleEntity roleEntity = (RoleEntity) role;
        deleteEntity(roleEntity);
    }

    public RoleEntity roleById(String roleId) {
        return (RoleEntity) queryEntityById(RoleEntity.BY_ID, roleId);
    }

    public Collection<RoleEntity> allRoles() {
        
        Session session = currentSession();
        List<Object> queriedUsers = session.query(RoleEntity.ALL);
        Collection<RoleEntity> result = new ArrayList<RoleEntity>();

        for (Object obj : queriedUsers) {
            result.add((RoleEntity) obj);
        }
        return result;
    }

    public void addUser(AbstractUserRO user, RoleRO role) {
        RoleEntity roleEntity = (RoleEntity) role;
        roleEntity.add(user);
        updateEntity(roleEntity);
    }

    public void removeUser(AbstractUserRO user, RoleRO role) {
        RoleEntity roleEntity = (RoleEntity) role;
        roleEntity.remove(user);
        updateEntity(roleEntity);
    }
}
