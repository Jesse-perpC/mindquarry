/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.user.manager;

import java.util.LinkedList;
import java.util.List;

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
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public final class UserManager implements UserAdmin, Authentication {

    static final String ADMIN_USER_ID = "admin";
    static final String ADMIN_PWD = "admin";
    static final String ADMIN_NAME = "Administrator";
    
    private SessionFactory sessionFactory_;
    
    /**
     * Setter for sessionFactory.
     *
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        sessionFactory_ = sessionFactory;
    }
    
    public void initialize() {        
        if (! existsAdminUser())
            createUser(ADMIN_USER_ID, ADMIN_PWD, ADMIN_NAME, "", null, null);
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
        List queryResult = session.query(query, new Object[] {id});
        if (queryResult.size() == 1)
            return (EntityBase) queryResult.get(0);            
        else
            return null;
    }
    
    private boolean existsAdminUser() {
        return null != queryUserById(ADMIN_USER_ID);
    }
    
    private Session currentSession() {
        return sessionFactory_.currentSession();
    }
    
    public final boolean isValidUserId(String userId) {
        return (null != userId) && (! "".equals(userId));
    }

    public User createUser(String id, String password, 
            String name, String surname, String email, String skills) {
        
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setPassword(password);
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

    public User userById(String userId) {
        return queryUserById(userId);
    }

    public List<UserRO> allUsers() {
        Session session = currentSession();
        
        List<Object> queriedUsers = session.query(
                "getAllUsers", new Object[0]);
        
        List<UserRO> result = new LinkedList<UserRO>();        
        
        for (Object userObj: queriedUsers) {
            UserRO user = (UserRO) userObj;
            result.add(user);
        }
        return result;
    }

    /**
     * @see com.mindquarry.user.Authentication#authenticate(java.lang.String, java.lang.String)
     */
    public boolean authenticate(String userId, String password) {
        UserEntity user = queryUserById(userId);
        return ((user != null) && (user.getPassword().equals(password)));
    }
    
    public boolean changePassword(String userId, String oldPwd, String newPwd) {
        UserEntity userEntity = queryUserById(userId);
        
        boolean succeeded = false;
        if (userEntity.getPassword().equals(oldPwd)) {
            userEntity.setPassword(newPwd);
            succeeded = true;
        }
        
        updateEntity(userEntity);
        
        return succeeded;
    }
    
    /**
     * @returns an user object if it can be found otherwise null
     */
    private UserEntity queryUserById(String id) {
        return (UserEntity) queryEntityById("getUserById", id);
    }

    public List<UserRO> queryMembersForTeamspace(String teamspaceId) {
        return queryUsersForTeamspace("getMembersForTeamspace", teamspaceId);
    }

    private List<UserRO> queryUsersForTeamspace(
            String queryKey, String teamspaceId) {
        
        Session session = currentSession();
        
        List<Object> queryResult = session.query(
                queryKey, new String[] {teamspaceId});
        
        List<UserRO> result = new LinkedList<UserRO>();
        
        for (Object userObj: queryResult) {
            UserRO user = (UserRO) userObj;
            result.add(user);
        }
        
        session.commit();
        
        return result;
    }
    
    public UserRO removeUserFromTeamspace(
            UserRO user, String teamspaceId) {
        
        UserEntity userEntity = (UserEntity) user;        
        userEntity.teamspaceReferences.remove(teamspaceId);
        
        updateEntity(userEntity);
        return userEntity;
    }
    
    public UserRO addUserToTeamspace(
            UserRO user, String teamspaceId) {
        
        UserEntity userEntity = (UserEntity) user;        
        userEntity.teamspaceReferences.add(teamspaceId);
        
        updateEntity(userEntity);
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
