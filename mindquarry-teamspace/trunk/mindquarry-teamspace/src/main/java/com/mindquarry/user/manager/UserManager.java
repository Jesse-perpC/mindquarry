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

import static com.mindquarry.user.manager.DefaultUsers.ADMIN_USER;
import static com.mindquarry.user.manager.DefaultUsers.ANONYMOUS_USER;
import static com.mindquarry.user.manager.DefaultUsers.INDEX_USER;
import static com.mindquarry.user.manager.DefaultUsers.defaultUsers;
import static com.mindquarry.user.manager.DefaultUsers.login;
import static com.mindquarry.user.manager.DefaultUsers.password;
import static com.mindquarry.user.manager.DefaultUsers.username;

import java.util.LinkedList;
import java.util.List;

import com.mindquarry.cache.JcrCache;
import com.mindquarry.common.persistence.EntityBase;
import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;
import com.mindquarry.user.AbstractUserRO;
import com.mindquarry.user.Authentication;
import com.mindquarry.user.GroupRO;
import com.mindquarry.user.User;
import com.mindquarry.user.UserAdmin;
import com.mindquarry.user.UserRO;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public final class UserManager implements UserAdmin, Authentication {
    
    private SessionFactory sessionFactory_;
    
    private JcrCache jcrCache_;

    /**
     * Setter for sessionFactory bean, set by spring at object creation
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        sessionFactory_ = sessionFactory;
    } 

    /**
     * Setter for jcrCache bean, set by spring at object creation
     */
    public void setJcrCache(JcrCache jcrCache) {
        jcrCache_ = jcrCache;
    }   

    public JcrCache getJcrCache() {
        return jcrCache_;
    }  

    public void initialize() {
        for (String[] userProfile : defaultUsers()) {
            if (!existsUser(userProfile))
                createUser(userProfile);
        }
    }

    private boolean existsUser(String[] userProfile) {
        return null != internalUserById(login(userProfile));
    }

    private void createUser(String[] userProfile) {
        createUser(login(userProfile), password(userProfile), 
                   username(userProfile), "", null, null);
    }

    private void persistEntity(EntityBase entity) {
        Session session = currentSession();
        session.persist(entity);
        session.commit();
    }

    private void deleteEntity(EntityBase entity) {
        Session session = currentSession();
        session.delete(entity);
        session.commit();
    }

    private void updateEntity(EntityBase entity) {
        Session session = currentSession();
        session.update(entity);
        session.commit();
    }

    /**
     * @returns an entity object if it can be found otherwise null
     */
    private EntityBase queryEntityById(String query, String id) {
        Session session = currentSession();
        List queryResult = session.query(query, new Object[] { id });
        if (queryResult.size() == 1)
            return (EntityBase) queryResult.get(0);
        else
            return null;
    }

    private Session currentSession() {
        return sessionFactory_.currentSession();
    }

    public boolean isAdminUser(UserRO user) {
        return user.getId().equals(login(ADMIN_USER));
    }

    public boolean isIndexUser(UserRO user) {
        return user.getId().equals(login(INDEX_USER));
    }

    public boolean isAnonymousUser(UserRO user) {
        return user.getId().equals(login(ANONYMOUS_USER));
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
        
        jcrCache_.removeFromCache(userByIdCacheKey(user.getId()));
    }

    public void deleteUser(User user) {
        UserEntity userEntity = (UserEntity) user;
        deleteEntity(userEntity);
        
        jcrCache_.removeFromCache(userByIdCacheKey(user.getId()));
    }

    public User userById(String userId) {
        return internalUserById(userId);
    }

    public List<UserRO> allUsers() {
        Session session = currentSession();

        List<Object> queriedUsers = session.query("getAllUsers", new Object[0]);
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
        UserEntity user = internalUserById(userId);
        return (user != null) && user.authenticate(password);
    }
    
    private String userByIdCacheKey(String userId) {
        return "Mindquarry.UserManager.USER_ID-" + userId;
    }

    private UserEntity internalUserById(String id) {        
        String cacheKey = userByIdCacheKey(id);
        UserEntity result = (UserEntity) jcrCache_.resultFromCache(cacheKey);
        
        if (result == null) {
            result = (UserEntity) queryUserById(id);
            if (result != null)
                jcrCache_.putResultInCache(cacheKey, result);
        }
        
        return result;
    }
    
    private UserEntity queryUserById(String id) {
        return (UserEntity) queryEntityById("getUserById", id);
    }

    
    
    public List<UserRO> membersForTeamspace(String teamspaceId) {
        return internalMembersForTeamspace(teamspaceId);
    }
    
    private String membersCacheKey(String teamspaceId) {
        return "Mindquarry.UserManager.MEMBERs-" + teamspaceId;
    }

    private List<UserRO> internalMembersForTeamspace(String teamspaceId) {
        String cacheKey = membersCacheKey(teamspaceId);
        List<UserRO> result = (List<UserRO>) jcrCache_.resultFromCache(cacheKey);
        
        if (result == null) {
            result = (List<UserRO>) queryUsersForTeamspace("getMembersForTeamspace", teamspaceId);
            if (result != null)
                jcrCache_.putResultInCache(cacheKey, result);
        }
        
        return result;
    }

    private List<UserRO> queryUsersForTeamspace(String queryKey,
            String teamspaceId) {
        Session session = currentSession();

        List<Object> queryResult = session.query(queryKey,
                new String[] { teamspaceId });

        List<UserRO> result = new LinkedList<UserRO>();

        for (Object userObj : queryResult) {
            UserRO user = (UserRO) userObj;
            result.add(user);
        }
        session.commit();
        return result;
    }

    public UserRO removeUserFromTeamspace(UserRO user, String teamspaceId) {
        UserEntity userEntity = (UserEntity) user;
        userEntity.teamspaceReferences.remove(teamspaceId);
        
        updateEntity(userEntity);
        
        jcrCache_.removeFromCache(userByIdCacheKey(user.getId()));
        jcrCache_.removeFromCache(membersCacheKey(teamspaceId));
        
        return userEntity;
    }

    public UserRO addUserToTeamspace(UserRO user, String teamspaceId) {
        UserEntity userEntity = (UserEntity) user;
        userEntity.teamspaceReferences.add(teamspaceId);

        updateEntity(userEntity);

        jcrCache_.removeFromCache(userByIdCacheKey(user.getId()));
        jcrCache_.removeFromCache(membersCacheKey(teamspaceId));
        
        return userEntity;
    }

    public GroupRO createGroup(String groupId) {
        GroupEntity result = new GroupEntity();
        result.setId(groupId);
        persistEntity(result);
        return result;
    }

    public GroupRO groupById(String groupId) {
        return queryGroupById(groupId);
    }

    public void deleteGroup(GroupRO group) {
        GroupEntity groupEntity = (GroupEntity) group;
        deleteEntity(groupEntity);
    }

    /**
     * @returns a group object if it can be found otherwise null
     */
    private GroupEntity queryGroupById(String id) {
        return (GroupEntity) queryEntityById("getGroupById", id);
    }

    public void addUser(AbstractUserRO user, GroupRO group) {
        GroupEntity groupEntity = (GroupEntity) group;
        groupEntity.add(user);
        updateEntity(groupEntity);
    }

    public void removeUser(AbstractUserRO user, GroupRO group) {
        GroupEntity groupEntity = (GroupEntity) group;
        groupEntity.remove(user);
        updateEntity(groupEntity);
    }
}
