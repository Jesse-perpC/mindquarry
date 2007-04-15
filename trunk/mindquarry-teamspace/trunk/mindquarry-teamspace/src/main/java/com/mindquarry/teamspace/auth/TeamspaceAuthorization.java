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
package com.mindquarry.teamspace.auth;

import static com.mindquarry.common.lang.StringUtil.concat;

import static com.mindquarry.auth.Operations.WRITE;
import static com.mindquarry.auth.Operations.READ;
import static com.mindquarry.auth.Operations.defaultOperations;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.mindquarry.auth.ActionRO;
import com.mindquarry.auth.AuthorizationAdmin;
import com.mindquarry.auth.cocoon.AuthorizationException;
import com.mindquarry.teamspace.Teamspace;
import com.mindquarry.teamspace.TeamspaceAdmin;
import com.mindquarry.teamspace.TeamspaceException;
import com.mindquarry.teamspace.TeamspaceRO;
import com.mindquarry.teamspace.manager.TeamspaceManager;
import com.mindquarry.user.RoleRO;
import com.mindquarry.user.User;
import com.mindquarry.user.UserAdmin;
import com.mindquarry.user.UserRO;
import com.mindquarry.user.webapp.CurrentUser;

/**
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class TeamspaceAuthorization implements 
    TeamspaceAdmin, BeanFactoryAware {

    private AuthorizationAdmin authAdmin_;
    
    private UserAdmin userAdmin_;
    
    private TeamspaceManager teamsManager_;
    
    private BeanFactory beanFactory_;
    
    /**
     * set by spring at object creation
     */
    public void setAuthAdmin(AuthorizationAdmin authAdmin) {
        authAdmin_ = authAdmin;
    }

    /**
     * set by spring at object creation
     */
    public void setUserAdmin(UserAdmin userAdmin) {
        userAdmin_ = userAdmin;
    }

    /**
     * set by spring at object creation
     */
    public void setTeamspaceManager(TeamspaceManager teamsManager) {
        teamsManager_ = teamsManager;
    }

    /**
     * set by spring at object creation
     */
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        beanFactory_ = beanFactory;
    }
    
    private String currentUserIdFromRequest() {
        CurrentUser currentUser = 
            (CurrentUser) beanFactory_.getBean(CurrentUser.ROLE);
        String userId = currentUser.getId();
        
        boolean isValidUserId = userId != null && userId.length() != 0;
        if (! isValidUserId) {
            throw new AuthorizationException("");
        }
        
        return userId;
    }
    
    private String teamContainerResourcePath() {
        return "/teamspaces";
    }
    
    public String teamResourcePath(String teamId) {
        return concat(teamContainerResourcePath(), "/", teamId);
    }
    
    private void authorizeTeamContainerWriteAccess(String user) {
        String resourceUri = teamContainerResourcePath();
        if (! authAdmin_.mayPerform(resourceUri, WRITE, user)) {
            // not authorised. throw exception
            throw new AuthorizationException("user '" + user + "' is" +
                    " not allowed to create or delete teams.");
        }
    }
    
    private void authorizeTeamAccess(String teamId, 
            String user, String operation) {
        
        String resourceUri = teamResourcePath(teamId);
        if (! authAdmin_.mayPerform(resourceUri, operation, user)) {
            // not authorised. throw exception
            throw new AuthorizationException("user: '" + user + "' is not " +
                    "allowed to " + operation + " team: " + teamId);
        }
    }
    
    private String roleId(TeamspaceRO team) {
        return "members_of_" + team.getId();
    }
    
    public Teamspace createTeamspace(String id, String name, 
            String description, UserRO teamspaceCreator) 
            throws TeamspaceException {
        
        String userId = currentUserIdFromRequest();        
        authorizeTeamContainerWriteAccess(userId);
        
        Teamspace team = teamsManager_.createTeamspace(
                id, name, description, teamspaceCreator);
        
        try {
            RoleRO teamRole = createMemberRole(team, userId);
            grantTeamMembersFullAccess(team, teamRole);
        } catch (AuthorizationException e) {
            // revert the creation of the team, because the creation
            // could not be completed
            teamsManager_.deleteTeamspace(team);
            throw e;
        }        
        return team;
    }
    
    private void grantTeamMembersFullAccess(Teamspace team, RoleRO teamRole) {
        String teamUri = teamResourcePath(team.getId());
        for (String operation : defaultOperations()) {
            ActionRO action = authAdmin_.createAction(teamUri, operation);
            authAdmin_.addAllowance(action, teamRole);
        }
    }
    
    private RoleRO createMemberRole(Teamspace team, String userId) {
        User user = userAdmin_.userById(userId);
        RoleRO teamRole = userAdmin_.createRole(roleId(team));
        userAdmin_.addUser(user, teamRole);
        return teamRole;
    }

    public void deleteTeamspace(Teamspace team) throws TeamspaceException {
        String userId = currentUserIdFromRequest();        
        authorizeTeamContainerWriteAccess(userId);
        
        teamsManager_.deleteTeamspace(team);
        
        RoleRO role = userAdmin_.roleById(roleId(team));
        userAdmin_.deleteRole(role);
    }

    public Teamspace teamspaceById(String teamId) {
        authorizeTeamAccess(teamId, currentUserIdFromRequest(), READ);
        return teamsManager_.teamspaceById(teamId);
    }

    public void updateTeamspace(Teamspace team) {
        authorizeTeamAccess(team.getId(), currentUserIdFromRequest(), WRITE);
        teamsManager_.updateTeamspace(team);
    }

    public Collection<? extends TeamspaceRO> teamspacesForUser(String userId) {
        return teamsManager_.teamspacesForUser(userId);
    }
    
    public void addMember(UserRO user, TeamspaceRO team) {
        authorizeTeamAccess(team.getId(), currentUserIdFromRequest(), WRITE);        
        RoleRO teamMemberRole = userAdmin_.roleById(roleId(team));
        userAdmin_.addUser(user, teamMemberRole);        
        teamsManager_.addMember(user, team);
    }

    public void removeMember(UserRO user, TeamspaceRO team) {
        authorizeTeamAccess(team.getId(), currentUserIdFromRequest(), WRITE);
        RoleRO teamMemberRole = userAdmin_.roleById(roleId(team));
        userAdmin_.removeUser(user, teamMemberRole);
        teamsManager_.removeMember(user, team);
    }
}
