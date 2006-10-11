/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.manager;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
import com.mindquarry.teamspace.Membership;
import com.mindquarry.teamspace.Teamspace;
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
                
        List<TeamspaceRO> result = new LinkedList<TeamspaceRO>();
        for (Object teamspaceObj : queriedTeamspaces) {            
            TeamspaceEntity teamspace = (TeamspaceEntity) teamspaceObj;
            result.add(teamspace);
        }
        
        session.commit();
        return result;
    }

    public List<Teamspace> teamspacesForUser(String userId) {
        Session session = currentSession();
        UserEntity user = queryUserById(session, userId);
              
        List<Teamspace> result = new LinkedList<Teamspace>();

        for (String teamRef : user.getTeamspaceReferences()) {
            TeamspaceEntity teamspace = queryTeamspaceById(session, teamRef);
            List<UserRO> users = queryMembersForTeamspace(
                    session, teamspace);
            teamspace.setUsers(users);
            result.add(teamspace);
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

    public TeamspaceRO teamspaceForId(String teamspaceId) {
        Session session = currentSession();
        return queryTeamspaceById(session, teamspaceId);
    }

    public Membership membership(TeamspaceRO teamspace) {
        List<UserRO> users = allUsers();
        List<UserRO> members = new LinkedList<UserRO>();
        List<UserRO> nonMembers = new LinkedList<UserRO>();
        
        for (UserRO user : users) {
            if (user.isMemberOf(teamspace))
                members.add(user);
            else
                nonMembers.add(user);
        }
        
        return new Membership(teamspace, 
                Collections.unmodifiableList(members), 
                Collections.unmodifiableList(nonMembers));
    }

    public void updateMembership(Membership membership) {

        List<UserRO> updatedUsers = new LinkedList<UserRO>();
        
        // check old members for removal from teamspace
        for (UserRO user : membership.members) {            
            if (! membership.newMembers.contains(user)) {                
                removeUserFromTeamspace(user, membership.teamspace);                
                updatedUsers.add(user);
            }
        }
        
        // check new members for adding to teamspae 
        for (UserRO user : membership.newMembers) {
            if (! membership.members.contains(user)) {
                addUserToTeamspace(user, membership.teamspace);
                updatedUsers.add(user);
            }
        }
        
        Session session = currentSession();
        for (UserRO updatedUser : updatedUsers) {
            session.update(updatedUser);
        }
        session.commit();
    }
    
    private void removeUserFromTeamspace(
            UserRO user, TeamspaceRO teamspace) {
        
        UserEntity userEntity = (UserEntity) user;
        TeamspaceEntity teamspaceEntity = (TeamspaceEntity) teamspace;
        
        userEntity.teamspaceReferences.remove(teamspace.getId());
        teamspaceEntity.users.remove(user);
    }
    
    private void addUserToTeamspace(
            UserRO user, TeamspaceRO teamspace) {
        
        UserEntity userEntity = (UserEntity) user;
        TeamspaceEntity teamspaceEntity = (TeamspaceEntity) teamspace;
        
        userEntity.teamspaceReferences.add(teamspace.getId());
        teamspaceEntity.users.add(user);
    }
}
