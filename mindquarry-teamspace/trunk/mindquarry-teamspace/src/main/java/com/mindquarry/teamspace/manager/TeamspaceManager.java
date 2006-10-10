/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.manager;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import com.mindquarry.teamspace.TeamspaceAdmin;
import com.mindquarry.teamspace.TeamspaceRO;
import com.mindquarry.teamspace.UserRO;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">your full name</a>
 */
class TeamspaceManager implements TeamspaceAdmin,
    Serviceable, Configurable, Initializable {

    static final String REPOS_BASE_PATH_PROPERTY = "mindquarry.reposbasepath";
    
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
    }

    public TeamspaceRO createTeamspace(
            String id, String name, String description) {
        
        TeamspaceEntity teamspace = new TeamspaceEntity();
        teamspace.setId(id);
        teamspace.setName(name);
        teamspace.setDescription(description);
        Session session = currentSession();
        session.persist(teamspace);
        session.commit();
        return teamspace;
    }

    public void removeTeamspace(String id) {
        Session session = currentSession();
        TeamspaceRO teamspace = queryTeamspaceById(session, id);
        session.delete(teamspace);
        session.commit();
    }

    public List<TeamspaceRO> allTeamspaces() {
        Session session = currentSession();
        
        List<Object> queriedTeamspaces = session.query(
                "getAllTeamspaces", new Object[0]);
        
        Map<String, Set<UserRO>> userMap = loadUserMap();
        
        List<TeamspaceRO> result = new LinkedList<TeamspaceRO>();
        for (Object teamspaceObj : queriedTeamspaces) {
            
            TeamspaceEntity teamspace = (TeamspaceEntity) teamspaceObj;
            if (userMap.containsKey(teamspace.getId())) {
                Set<UserRO> users = queryUsersForTeamspace(teamspace); //userMap.get(teamspace.getId());
                teamspace.setUsers(users);                
            }
            result.add(teamspace);
        }
        
        session.commit();
        return result;
    }

    public List<TeamspaceRO> teamspacesForUser(String userId) {
        Session session = currentSession();
        UserEntity user = queryUserById(session, userId);
        
        Map<String, Set<UserRO>> userMap = loadUserMap();        
        List<TeamspaceRO> result = new LinkedList<TeamspaceRO>();

        for (String teamRef : user.getTeamspaceReferences()) {
            TeamspaceEntity teamspace = queryTeamspaceById(session, teamRef);
            if (userMap.containsKey(teamspace.getId())) {
                Set<UserRO> users = queryUsersForTeamspace(teamspace);
                teamspace.setUsers(users);                
            }
            result.add(teamspace);
        }
        
        session.commit();
        return result;
    } 

    private Map<String, Set<UserRO>> loadUserMap() {
        
        Session session = currentSession();
        
        List<Object> queriedUsers = session.query("getAllUsers", new Object[0]);
        
        Map<String, Set<UserRO>> userMap = new HashMap<String, Set<UserRO>>();
        
        for (Object userObj: queriedUsers) {
            UserRO user = (UserRO) userObj;
            for (String teamspaceId : user.getTeamspaceReferences()) {
                
                if (! userMap.containsKey(teamspaceId))
                    userMap.put(teamspaceId, new HashSet<UserRO>());
                    
                userMap.get(teamspaceId).add(user);
            }
        }
        return userMap;
    }

    private Set<UserRO> queryUsersForTeamspace(TeamspaceRO teamspace) {
        
        Session session = currentSession();
        
        List<Object> queryResult = session.query(
                "getUsersForTeamspace", new String[] {teamspace.getId()});
        
        Set<UserRO> result = new HashSet<UserRO>();
        
        for (Object userObj: queryResult) {
            UserRO user = (UserRO) userObj;
            result.add(user);
        }
        return result;
    }

    public UserRO createUser(String id, String name) {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setName(name);
        Session session = currentSession();
        session.persist(user);
        session.commit();
        return user;
    }

    public void removeUser(String id) {
        Session session = currentSession();
        UserRO user = queryUserById(session, id);
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
    
    private TeamspaceEntity queryTeamspaceById(Session session, String id) {
        List queryResult = session.query("getTeamspaceById", new Object[] {id});
        return (TeamspaceEntity) queryResult.get(0);
    }
    
    private UserEntity queryUserById(Session session, String id) {
        List queryResult = session.query("getUserById", new Object[] {id});
        return (UserEntity) queryResult.get(0);
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

    public void addUserToTeamspace(UserRO user, TeamspaceRO teamspace) {
        UserEntity userEntity = (UserEntity) user;
        TeamspaceEntity teamspaceEntity = (TeamspaceEntity) teamspace;
        
        userEntity.addTeamspaceReference(teamspace);
        teamspaceEntity.addUser(user);
        
        Session session = currentSession();
        session.update(userEntity);
        session.update(teamspaceEntity);
        
        session.commit();
    }
}
