/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.manager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mindquarry.common.init.InitializationException;
import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;
import com.mindquarry.teamspace.Authentication;
import com.mindquarry.teamspace.Authorisation;
import com.mindquarry.teamspace.Membership;
import com.mindquarry.teamspace.TeamspaceAdmin;
import com.mindquarry.teamspace.TeamspaceDefinition;
import com.mindquarry.teamspace.TeamspaceException;
import com.mindquarry.teamspace.TeamspaceRO;
import com.mindquarry.teamspace.UserRO;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class TeamspaceManager implements TeamspaceAdmin, Authentication,
    Authorisation {
    
    static final String ADMIN_USER_ID = "admin";
    static final String ADMIN_PWD = "admin";
    static final String ADMIN_NAME = "Administrator";
    
    private SessionFactory sessionFactory_;    
    
    private DefaultListenerRegistry listenerRegistry_;
    
    private Constructor membersListProxyConstructor_;

    /**
     * Setter for listenerRegistry.
     *
     * @param listenerRegistry the listenerRegistry to set
     */
    public void setListenerRegistry(DefaultListenerRegistry listenerRegistry) {
        listenerRegistry_ = listenerRegistry;
    }

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
        
        Class proxyClazz = Proxy.getProxyClass(
                this.getClass().getClassLoader(), 
                new Class[] {List.class});
        
        Class[] constrParamType = new Class[] {InvocationHandler.class};
        try {
            membersListProxyConstructor_ = 
                proxyClazz.getConstructor(constrParamType);
        } catch (SecurityException e) {
            throw new InitializationException(
                    "could not get constructor method from dynamic proxy", e);
        } catch (NoSuchMethodException e) {
            throw new InitializationException(
                    "could not get constructor method from dynamic proxy", e);
        }
    }
    
    private boolean existsAdminUser() {
        Session session = currentSession();
        return null != queryUserById(session, ADMIN_USER_ID);
    }

    public TeamspaceDefinition createTeamspace(String teamspaceId, 
            String name, String description, UserRO teamspaceCreator) {
        
        TeamspaceEntity teamspace = new TeamspaceEntity();
        teamspace.setId(teamspaceId);
        teamspace.setName(name);
        teamspace.setDescription(description);
        
        listenerRegistry_.signalBeforeTeamspaceCreated(teamspace);
        
        addUserToTeamspace(teamspaceCreator, teamspace);
        
        Session session = currentSession();
        session.persist(teamspace);
        session.update(teamspaceCreator);
        session.commit();
        return teamspace;
    }
    
    public void updateTeamspace(TeamspaceDefinition teamspace) {
        Session session = currentSession();
        session.update(teamspace);
        session.commit();
    }

    public void removeTeamspace(String teamspaceId) {
        
        Session session = currentSession();
        TeamspaceEntity teamspace = queryTeamspaceById(session, teamspaceId);
        
        if (null == teamspace)
            throw new TeamspaceException(
                    "the teamspace " + teamspaceId + " does not exist."  );
        
        List<UserRO> users = queryMembersForTeamspace(session, teamspace);
        for (UserRO user : users) {
            removeUserFromTeamspace(user, teamspace);
            session.update(user);
        }
        session.delete(teamspace);
        session.commit();
        
        listenerRegistry_.signalAfterTeamspaceRemoved(teamspace);
    }

    public List<TeamspaceRO> teamspacesForUser(String userId) {
        assert isValidUserId(userId) : 
            "the userId: " + userId + " is not valid";
        
        Session session = currentSession();        
        List<TeamspaceRO> result;
        
        if (ADMIN_USER_ID.equals(userId))
            result = queryAllTeamspaces();
        else
            result = queryTeamspacesForUser(userId);
        
        session.commit();
        return result;
    }
    
    private boolean isValidUserId(String userId) {
        return (null != userId) && (! "".equals(userId));
    }

    private List<TeamspaceRO> queryAllTeamspaces() {
        Session session = currentSession();
        
        List<Object> queriedTeamspaces = session.query(
                "getAllTeamspaces", new Object[0]);
                
        List<TeamspaceRO> result = new LinkedList<TeamspaceRO>();
        for (Object teamspaceObj : queriedTeamspaces) {            
            TeamspaceEntity teamspace = (TeamspaceEntity) teamspaceObj;
            teamspace.setUsers(createLazyLoadMembersList(teamspace));
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
    
    List<UserRO> queryMembersForTeamspace(TeamspaceRO teamspace) {
        Session session = currentSession();
        List<UserRO> result = queryMembersForTeamspace(session, teamspace);
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
        TeamspaceEntity result = null;
        List queryResult = session.query("getTeamspaceById", new Object[] {id});
        if (queryResult.size() == 1) {
            result = (TeamspaceEntity) queryResult.get(0);
            result.setUsers(createLazyLoadMembersList(result));
        }
        return result;
    }
    
    private List<UserRO> createLazyLoadMembersList(TeamspaceRO teamspace) {
        
        ListLoading<UserRO> loader = new MembersLoading(teamspace, this);
        
        LazyLoadListInvocationHandler<UserRO> invocationHandler;
        invocationHandler = new LazyLoadListInvocationHandler<UserRO>(loader);
        
        Object[] params = new Object[] {invocationHandler};
        try {
            Object proxy = membersListProxyConstructor_.newInstance(params);
            return (List<UserRO>) proxy;
        } catch (IllegalArgumentException e) {
            throw new InitializationException(
                    "could not create lazyLoadList proxy", e);
        } catch (InstantiationException e) {
            throw new InitializationException(
                    "could not create lazyLoadList proxy", e);
        } catch (IllegalAccessException e) {
            throw new InitializationException(
                    "could not create lazyLoadList proxy", e);
        } catch (InvocationTargetException e) {
            throw new InitializationException(
                    "could not create lazyLoadList proxy", e);
        }
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
    
    private Session currentSession() {
        return sessionFactory_.currentSession();
    }

    public UserRO userForId(String userId) {
        Session session = currentSession();
        return queryUserById(session, userId);
    }
    
    /**
     * @see com.mindquarry.teamspace.TeamspaceQuery#teamspaceForId(java.lang.String)
     */
    public TeamspaceRO teamspaceForId(String teamspaceId) {
        return teamspaceDefinitionForId(teamspaceId);
    }
    
    /**
     * @see com.mindquarry.teamspace.TeamspaceAdmin#teamspaceDefinitionForId(java.lang.String)
     */
    public TeamspaceDefinition teamspaceDefinitionForId(String teamspaceId) {
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

    /**
     * @see com.mindquarry.teamspace.Authorisation#authorise(java.lang.String, java.lang.String, java.lang.String)
     */
    public boolean authorise(String userId, String uri, String method) {
        // check if user is in teamspace

        // simple regular expression that looks for the teamspace name...
        Pattern p = Pattern.compile("jcr:///teamspaces/([^/]*)/(.*)");
        Matcher m = p.matcher(uri);
        if (m.matches()) {
            String requestedTeamspaceID = m.group(1); //"mindquarry";
            // ...and checks if the user is in that teamspace
            for (TeamspaceRO teamspace: teamspacesForUser(userId)) {
                if (teamspace.getId().equals(requestedTeamspaceID)) {
                    return true;
                }
            }
        }
        return false;
    }
}
