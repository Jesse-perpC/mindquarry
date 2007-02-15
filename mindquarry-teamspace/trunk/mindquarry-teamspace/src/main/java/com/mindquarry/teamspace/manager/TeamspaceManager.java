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

import com.mindquarry.auth.AuthorizationAdmin;
import com.mindquarry.auth.ProfileRO;
import com.mindquarry.auth.RightRO;
import com.mindquarry.cache.JcrCache;
import com.mindquarry.common.init.InitializationException;
import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;
import com.mindquarry.common.resources.ResourceDoesNotExistException;
import com.mindquarry.teamspace.Authorisation;
import com.mindquarry.teamspace.CouldNotCreateTeamspaceException;
import com.mindquarry.teamspace.CouldNotRemoveTeamspaceException;
import com.mindquarry.teamspace.Membership;
import com.mindquarry.teamspace.Teamspace;
import com.mindquarry.teamspace.TeamspaceAdmin;
import com.mindquarry.teamspace.TeamspaceRO;
import com.mindquarry.user.GroupRO;
import com.mindquarry.user.UserRO;
import com.mindquarry.user.manager.UserManager;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public final class TeamspaceManager implements TeamspaceAdmin, Authorisation {

    static final String ADMIN_USER_ID = "admin";

    static final String ADMIN_PWD = "admin";

    static final String ADMIN_NAME = "Administrator";

    /**
     * helds the constructor of a dynamic generated proxy for interface
     * java.util.List. a proxy is used for lazy loading of team members within a
     * teamspace entity object.
     */
    private static Constructor listProxyConstructor_;

    static {
        prepareLazyListLoading();
    }

    /**
     * creates a proxy class for the java.util.List interface and stores the
     * default constructor of this class in a member variable.
     */
    static private void prepareLazyListLoading() {
        Class proxyClazz = Proxy.getProxyClass(TeamspaceManager.class
                .getClassLoader(), new Class[] { List.class });

        Class[] constrParamType = new Class[] { InvocationHandler.class };
        try {
            listProxyConstructor_ = proxyClazz.getConstructor(constrParamType);
        } catch (SecurityException e) {
            throw new InitializationException(
                    "could not get constructor method from dynamic proxy", e);
        } catch (NoSuchMethodException e) {
            throw new InitializationException(
                    "could not get constructor method from dynamic proxy", e);
        }
    }

    private SessionFactory sessionFactory_;

    private DefaultListenerRegistry listenerRegistry_;

    private UserManager userManager_;

    private AuthorizationAdmin authAdmin_;
    
    private JcrCache jcrCache_;

    /**
     * Setter for listenerRegistry bean, set by spring at object creation
     */
    public void setListenerRegistry(DefaultListenerRegistry listenerRegistry) {
        listenerRegistry_ = listenerRegistry;
    }

    /**
     * Setter for sessionFactory bean, set by spring at object creation
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        sessionFactory_ = sessionFactory;
    }

    /**
     * Setter for userManager bean, set by spring at object creation
     */
    public void setUserManager(UserManager userManager) {
        userManager_ = userManager;
    }

    /**
     * Setter for authAdmin bean, set by spring at object creation
     */
    public void setAuthAdmin(AuthorizationAdmin authAdmin) {
        authAdmin_ = authAdmin;
    }    

    /**
     * Setter for jcrCache bean, set by spring at object creation
     */
    public void setJcrCache(JcrCache jcrCache) {
        jcrCache_ = jcrCache;
    }

    public void initialize() {
        prepareLazyListLoading();

        for (TeamspaceRO teamspace : queryAllTeamspaces()) {
            GroupRO teamGroup = userManager_.groupById(teamspace.getId());
            if (teamGroup == null) {
                createGroupForTeam(teamspace);
            }
        }
    }

    public Teamspace createTeamspace(String teamspaceId, String name,
            String description, UserRO teamspaceCreator)
            throws CouldNotCreateTeamspaceException {

        TeamspaceEntity teamspace = new TeamspaceEntity();
        teamspace.setId(teamspaceId);
        teamspace.setName(name);
        teamspace.setDescription(description);

        try {
            listenerRegistry_.signalBeforeTeamspaceCreated(teamspace);
        } catch (Exception e) {
            throw new CouldNotCreateTeamspaceException(
                    "Teamspace creation failed in listener: " + e.getMessage(),
                    e);
        }
        
        // there is no need to make the admin a member of the teamspace,
        // at most its somewhat confusing if the admin user appears as member
        // within the teamspace views
        // Nevertheless she/he is not only allowed to create and teamspaces
        // but also to edit existing ones.
        if (! userManager_.isAdminUser(teamspaceCreator))
            userManager_.addUserToTeamspace(teamspaceCreator, teamspace.getId());

        // create the teams default group
        createGroupForTeam(teamspace);
        jcrCache_.removeFromCache(allTeamsCacheKey());
        
        Session session = currentSession();
        session.persist(teamspace);
        session.commit();
        return teamspace;
    }
    
    private void createGroupForTeam(TeamspaceRO teamspace) {
        GroupRO teamGroup = userManager_.createGroup(teamspace.getId());
        List<UserRO> teamMembers = membersForTeamspace(teamspace);

        for (UserRO teamMember : teamMembers)
            userManager_.addUser(teamMember, teamGroup);

        String teamspaceUri = "/teamspaces/" + teamspace.getId();
        RightRO rRight = authAdmin_.createRight(teamspaceUri, "READ");
        RightRO wRight = authAdmin_.createRight(teamspaceUri, "WRITE");

        String profileName = teamspace.getId() + "-user";
        ProfileRO profile = authAdmin_.createProfile(profileName);
        authAdmin_.addRight(rRight, profile);
        authAdmin_.addRight(wRight, profile);

        authAdmin_.addAllowance(profile, teamGroup);
    }
    
    private String teamByIdCacheKey(String teamId) {
        return "Mindquarry.TeamspaceManager.TEAM_ID-" + teamId;
    }

    public void updateTeamspace(Teamspace teamspace) {
        Session session = currentSession();
        session.update(teamspace);
        session.commit();
        
        jcrCache_.removeFromCache(teamByIdCacheKey(teamspace.getId()));
        jcrCache_.removeFromCache(allTeamsCacheKey());
    }

    public void deleteTeamspace(Teamspace teamspace)
            throws CouldNotRemoveTeamspaceException {

        Session session = currentSession();
        session.delete(teamspace);
        session.commit();
               
        jcrCache_.removeFromCache(teamByIdCacheKey(teamspace.getId()));
        jcrCache_.removeFromCache(allTeamsCacheKey());

        List<UserRO> users = membersForTeamspace(teamspace);
        for (UserRO user : users)
            userManager_.removeUserFromTeamspace(user, teamspace.getId());

        try {
            listenerRegistry_.signalAfterTeamspaceRemoved(teamspace);
        } catch (Exception e) {
            throw new CouldNotRemoveTeamspaceException(
                    "Teamspace removal failed in listener " + e.getMessage(), e);
        }
    }

    public List<TeamspaceRO> teamspacesForUser(String userId) {
        assert userManager_.isValidUserId(userId) : "the userId: " + userId
                + " is not valid";

        List<TeamspaceRO> result;
        
        if (ADMIN_USER_ID.equals(userId))
            result = allTeamspaces();
        else
            result = queryTeamspacesForUser(userId);
        
        return result;
    }
    
    private String allTeamsCacheKey() {
        return "Mindquarry.TeamspaceManager.ALL_TEAMS";
    }

    private List<TeamspaceRO> allTeamspaces() {
        String cacheKey = allTeamsCacheKey();
        List<TeamspaceRO> result = (List<TeamspaceRO>) jcrCache_.resultFromCache(cacheKey);
        
        if (result == null) {
            result = queryAllTeamspaces();
            if (result != null)
                jcrCache_.putResultInCache(cacheKey, result);
        }
        
        return result;
    }

    private List<TeamspaceRO> queryAllTeamspaces() {
        Session session = currentSession();

        List<Object> queriedTeamspaces = session.query("getAllTeamspaces",
                new Object[0]);

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
        
        List<TeamspaceRO> result = new LinkedList<TeamspaceRO>();

        UserRO user = userManager_.userById(userId);
        if (user != null) {
            for (String teamRef : user.teamspaces()) {
                TeamspaceEntity teamspace = queryTeamspaceById(teamRef);
                result.add(teamspace);
            }
        }
        
        return result;
    }

    List<UserRO> membersForTeamspace(TeamspaceRO teamspace) {
        return userManager_.membersForTeamspace(teamspace.getId());
    }

    /**
     * @see com.mindquarry.teamspace.TeamspaceQuery#teamspaceById(java.lang.String)
     */
    public Teamspace teamspaceById(String teamspaceId) {        
        return internalTeamspaceById(teamspaceId);
    }

    private Teamspace internalTeamspaceById(String teamspaceId) {        
        String cacheKey = teamByIdCacheKey(teamspaceId);
        Teamspace result = (Teamspace) jcrCache_.resultFromCache(cacheKey);
        
        if (result == null) {
            result = queryTeamspaceById(teamspaceId);
            if (result != null)
                jcrCache_.putResultInCache(cacheKey, result);
        }
        
        return result;
    }

    /**
     * @returns a teamspace object if it can be found otherwise null
     */
    private TeamspaceEntity queryTeamspaceById(String id) {
        Session session = currentSession();
        TeamspaceEntity result = null;
        List queryResult = session.query("getTeamspaceById",
                new Object[] { id });
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

        Object[] params = new Object[] { invocationHandler };
        try {
            Object proxy = listProxyConstructor_.newInstance(params);
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

    private Session currentSession() {
        return sessionFactory_.currentSession();
    }

    public Membership membership(TeamspaceRO teamspace) {
        List<UserRO> users = userManager_.allUsers();
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

        String teamspaceId = membership.teamspace.getId();

        // check old members for removal from teamspace
        for (UserRO user : membership.getRemovedMembers()) {
            userManager_.removeUserFromTeamspace(user, teamspaceId);
            jcrCache_.removeFromCache(allTeamsCacheKey());
        }

        // check new members for adding to teamspace
        for (UserRO user : membership.getAddedMembers()) {
            userManager_.addUserToTeamspace(user, teamspaceId);
            jcrCache_.removeFromCache(allTeamsCacheKey());
        }

        // return a new calculated membership
        return membership(membership.teamspace);
    }

    /**
     * Simple authorisation implementation. Considers 'userId' and 'uri',
     * 'method' is completely ignored.
     * 
     * <ul>
     * <li>'admin' can do anything</li>
     * <li>all other users can do anything in the teams they belong to</li>
     * <li><code>ResourceNotFoundException</code> if the uri contains a
     * non-existing team</li>
     * </ul>
     * 
     * The uri containing the team-id that a user might belong to should have
     * the form <code>jcr:///teamspaces/&lt;team-id&gt;/[sub-resource]</code>.
     * If the uri is different, only the 'admin' will be allowed to do
     * something.
     * 
     */
    public boolean authorise(String userId, String uri, String method) {
        
        UserRO user = userManager_.userById(userId);
        
        // always check if the teamspace does exist (even for admin)
        
        // simple regular expression that looks for the teamspace name...
        Pattern p = Pattern.compile("jcr:///teamspaces/([^/]*)/(.*)");
        Matcher m = p.matcher(uri);
        if (m.matches()) {
            String requestedTeamspaceID = m.group(1); // "mindquarry";
            // ...check if that teamspace exists
            if (this.teamspaceById(requestedTeamspaceID) == null) {
                throw new ResourceDoesNotExistException(uri, "Teamspace '"
                        + requestedTeamspaceID + "' does not exist.");
            }

            // admin can do everything
            if (userManager_.isAdminUser(user) || user.isMemberOf(requestedTeamspaceID)) {
                return true;
            }
        } else {
            // shorter or different URL (eg. jcr:///users) is only for admin
            if (userManager_.isAdminUser(user)) {
                return true;
            }
        }
        
        return false;
    }
}
