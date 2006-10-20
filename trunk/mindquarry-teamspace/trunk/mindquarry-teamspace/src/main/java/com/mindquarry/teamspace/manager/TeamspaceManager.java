/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.manager;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;

import com.mindquarry.common.init.InitializationException;
import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;
import com.mindquarry.teamspace.Authentication;
import com.mindquarry.teamspace.Membership;
import com.mindquarry.teamspace.TeamspaceAdmin;
import com.mindquarry.teamspace.TeamspaceRO;
import com.mindquarry.teamspace.UserRO;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">your full name</a>
 */
class TeamspaceManager implements TeamspaceAdmin, Authentication, 
    Serviceable, Configurable, Initializable {

    static final String REPOS_BASE_PATH_PROPERTY = "mindquarry.reposbasepath";
    
    static final String ADMIN_USER_ID = "admin";
    static final String ADMIN_PWD = "admin";
    static final String ADMIN_NAME = "Administrator";
    
    private String reposBasePath_;
    
    private SessionFactory sessionFactory_;
    
    private File reposBaseDirectory_;
    
    /**
     * {@inheritDoc}
     * @see org.apache.avalon.framework.service.Serviceable#service(org.apache.avalon.framework.service.ServiceManager)
     */
    public void service(ServiceManager serviceManager) throws ServiceException {
        String name = SessionFactory.class.getName();
        sessionFactory_ = (SessionFactory) serviceManager.lookup(name);
    }

    /**
     * {@inheritDoc}
     * @see org.apache.avalon.framework.configuration.Configurable#configure(org.apache.avalon.framework.configuration.Configuration)
     */
    public void configure(Configuration configuration) 
        throws ConfigurationException {
        
        reposBasePath_ = configuration.getAttribute("reposbasepath", null);

        if (null != System.getProperty(REPOS_BASE_PATH_PROPERTY))
            reposBasePath_ = System.getProperty(REPOS_BASE_PATH_PROPERTY);
        
        if (null == reposBasePath_) 
            throw new ConfigurationException(
                    "'mindquarry.reposbasepath' is not set, whether as " +
                    "container configuration nor as system property. " +
                    "It must be set to a valid, " +
                    "existing base directory for repositories");
    }

    /**
     * @throws Exception
     */
    public void initialize() throws Exception {
        
        reposBaseDirectory_ = new File(reposBasePath_);       
        
        if (! reposBaseDirectory_.exists() 
                || ! reposBaseDirectory_.isDirectory()) 
            throw new InitializationException(
                    "'mindquarry.reposbasepath' is not set to a valid, " +
                    "existing base directory for repositories.");
        
        if (! existsAdminUser())
            createUser(ADMIN_USER_ID, ADMIN_PWD, ADMIN_NAME, "", null, null);
    }
    
    private boolean existsAdminUser() {
        Session session = currentSession();
        return null != queryUserById(session, ADMIN_USER_ID);
    }

    public TeamspaceRO createTeamspace(String id, String name, 
            String description, UserRO teamspaceCreator) {
        
        TeamspaceEntity teamspace = new TeamspaceEntity();
        teamspace.setId(id);
        teamspace.setName(name);
        teamspace.setDescription(description);
        addUserToTeamspace(teamspaceCreator, teamspace);
        
        Session session = currentSession();
        session.persist(teamspace);
        session.update(teamspaceCreator);
        session.commit();
        return teamspace;
    }

    public void removeTeamspace(String teamspaceId) {
        Session session = currentSession();
        TeamspaceEntity teamspace = queryTeamspaceById(session, teamspaceId);
        List<UserRO> users = queryMembersForTeamspace(session, teamspace);
        for (UserRO user : users) {
            removeUserFromTeamspace(user, teamspace);
            session.update(user);
        }
        session.delete(teamspace);
        session.commit();
    }

    public List<TeamspaceRO> teamspacesForUser(String userId) {
        Session session = currentSession();
        
        List<TeamspaceRO> result;
        
        if (ADMIN_USER_ID.equals(userId))
            result = queryAllTeamspaces();
        else
            result = queryTeamspacesForUser(userId);
        
        
        for (TeamspaceRO teamspace : result) {
            TeamspaceEntity teamspaceEntity = (TeamspaceEntity) teamspace;
            List<UserRO> users = queryMembersForTeamspace(
                session, teamspaceEntity);
            teamspaceEntity.setUsers(users);
        }
        
        session.commit();
        return result;
    }

    private List<TeamspaceRO> queryAllTeamspaces() {
        Session session = currentSession();
        
        List<Object> queriedTeamspaces = session.query(
                "getAllTeamspaces", new Object[0]);
                
        List<TeamspaceRO> result = new LinkedList<TeamspaceRO>();
        for (Object teamspaceObj : queriedTeamspaces) {            
            TeamspaceEntity teamspace = (TeamspaceEntity) teamspaceObj;
            result.add(teamspace);
        }
        
        session.commit();
        return result;
    }
    
    private List<TeamspaceRO> queryTeamspacesForUser(String userId) {
        Session session = currentSession();
        
        List<TeamspaceRO> result = new LinkedList<TeamspaceRO>();
        
        UserRO user = queryUserById(session, userId);
        if (user != null) {
            for (String teamRef : user.getTeamspaceReferences()) {
                TeamspaceEntity teamspace = queryTeamspaceById(session, teamRef);
                result.add(teamspace);
            }            
        }
        
        session.commit();
        return result;
    }

    private List<UserRO> queryMembersForTeamspace(
            Session session, TeamspaceRO teamspace) {
        
        return queryUsersForTeamspace(
                "getMembersForTeamspace", session, teamspace);
    }

    private List<UserRO> queryUsersForTeamspace(
            String queryKey, Session session, TeamspaceRO teamspace) {
        
        List<Object> queryResult = session.query(
                queryKey, new String[] {teamspace.getId()});
        
        List<UserRO> result = new LinkedList<UserRO>();
        
        for (Object userObj: queryResult) {
            UserRO user = (UserRO) userObj;
            result.add(user);
        }
        return result;
    }

    public UserRO createUser(String id, String password, 
            String name, String surname, String email, String skills) {
        
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setSkills(skills);
        Session session = currentSession();
        session.persist(user);
        session.commit();
        return user;
    }
    
    public boolean changePassword(String userId, String oldPwd, String newPwd) {
        Session session = currentSession();
        UserEntity userEntity = queryUserById(session, userId);
        
        boolean succeeded = false;
        if (userEntity.getPassword().equals(oldPwd)) {
            userEntity.setPassword(newPwd);
            succeeded = true;
        }
        
        session.update(userEntity);
        session.commit();
        
        return succeeded;
    }

    public void removeUser(String userId) {
        Session session = currentSession();
        UserEntity user = queryUserById(session, userId);
        session.delete(user);
        session.commit();
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
     * @returns a teamspace object if it can be found otherwise null
     */
    private TeamspaceEntity queryTeamspaceById(Session session, String id) {
        List queryResult = session.query("getTeamspaceById", new Object[] {id});
        if (queryResult.size() == 1)
            return (TeamspaceEntity) queryResult.get(0);
        else
            return null;
    }
    
    /**
     * @returns an user object if it can be found otherwise null
     */
    private UserEntity queryUserById(Session session, String id) {
        List queryResult = session.query("getUserById", new Object[] {id});
        if (queryResult.size() == 1)
            return (UserEntity) queryResult.get(0);            
        else
            return null;
    }

    public String workspaceUri(String id) {
        Session session = currentSession();
        TeamspaceRO teamspace = queryTeamspaceById(session, id);
        String result = teamspace.getWorkspaceUri();
        session.commit();
        return result;
    }
    
    private Session currentSession() {
        return sessionFactory_.currentSession();
    }

    public UserRO userForId(String userId) {
        Session session = currentSession();
        return queryUserById(session, userId);
    }
    
    public TeamspaceRO teamspaceForId(String teamspaceId) {
        Session session = currentSession();
        return queryTeamspaceById(session, teamspaceId);
    }

    public Membership membership(TeamspaceRO teamspace) {
        List<UserRO> users = allUsers();
        Set<UserRO> members = new HashSet<UserRO>();
        Set<UserRO> nonMembers = new HashSet<UserRO>();
        
        for (UserRO user : users) {
            if (user.isMemberOf(teamspace))
                members.add(user);
            else
                nonMembers.add(user);
        }
        
        return new Membership(teamspace, members, nonMembers);
    }
    
    public Membership refreshMembership(Membership oldMembership) {
        Membership result = membership(oldMembership.teamspace);
        for (UserRO user : oldMembership.getAddedMembers()) {
            result.addMember(user);
        }
        for (UserRO user : oldMembership.getRemovedMembers()) {
            result.removeMember(user);
        }
        return result;
    }

    
    // TODO think about requerying or reattaching of users and teamspaces 
    // to new session, because most of the times the object was queried
    // in transactions and sessions before the current one
    public Membership updateMembership(Membership membership) {

        List<UserRO> updatedUsers = new LinkedList<UserRO>();
        
        // check old members for removal from teamspace
        for (UserRO user : membership.getRemovedMembers()) {            
            removeUserFromTeamspace(user, membership.teamspace);                
            updatedUsers.add(user);
        }
        
        // check new members for adding to teamspace
        for (UserRO user : membership.getAddedMembers()) {
            addUserToTeamspace(user, membership.teamspace);
            updatedUsers.add(user);
        }
        
        Session session = currentSession();
        for (UserRO updatedUser : updatedUsers) {
            session.update(updatedUser);
        }
        session.commit();
        
        TeamspaceRO teamspace = membership.teamspace;
        
        // return a new calculated membership
        return membership(teamspace);
    }
    
    private void removeUserFromTeamspace(
            UserRO user, TeamspaceRO teamspace) {
        
        UserEntity userEntity = (UserEntity) user;        
        userEntity.teamspaceReferences.remove(teamspace.getId());
    }
    
    private void addUserToTeamspace(
            UserRO user, TeamspaceRO teamspace) {
        
        UserEntity userEntity = (UserEntity) user;        
        userEntity.teamspaceReferences.add(teamspace.getId());
    }

    /**
     * @see com.mindquarry.teamspace.Authentication#authenticate(java.lang.String, java.lang.String)
     */
    public boolean authenticate(String userId, String password) {
        Session session = currentSession();
        UserEntity user = queryUserById(session, userId);
        if ((null != user) && (user.getPassword().equals(password)))
            return true;
        else
            return false;
    }
}
