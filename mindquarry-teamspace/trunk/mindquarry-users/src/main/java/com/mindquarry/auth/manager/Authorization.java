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
package com.mindquarry.auth.manager;

import static com.mindquarry.auth.Operations.defaultOperations;
import static com.mindquarry.auth.manager.ResourceUtil.pathItems;
import static com.mindquarry.common.lang.StringUtil.concat;

import java.util.Iterator;
import java.util.List;

import com.mindquarry.auth.ActionRO;
import com.mindquarry.auth.AuthorizationAdmin;
import com.mindquarry.auth.ResourceRO;
import com.mindquarry.persistence.api.Session;
import com.mindquarry.persistence.api.SessionFactory;
import com.mindquarry.user.AbstractUserRO;
import com.mindquarry.user.UserRO;
import com.mindquarry.user.manager.UserManager;
import com.mindquarry.user.util.DefaultUsers;


/**
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class Authorization implements AuthorizationAdmin {
    
    private UserManager userManager_;
    
    private SessionFactory sessionFactory_;
    
    /**
     * set by spring at object creation
     */
    public void setUserManager(UserManager userManager) {
        userManager_ = userManager;
    }

    /**
     * set by spring at object creation
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        sessionFactory_ = sessionFactory;
    }
    
    private Session currentSession() {
        return sessionFactory_.currentSession();
    }
    
    public ResourceEntity getResourceRoot() {
        Session session = currentSession();
        List<Object> queryResult = session.query("getResourceById", "root");
        
        if (queryResult.isEmpty())
            return initializeAuthorization();
        else
            return (ResourceEntity) queryResult.get(0);
    }
    
    private ResourceEntity initializeAuthorization() {
        ResourceEntity root = new ResourceEntity("root", "", null);
        currentSession().persist(root);
        
        // allow admin to execute default operations on all resources
        // the right list will become non-empty. hence, other users
        // will be unauthorized by default
        UserRO admin = userManager_.userById(DefaultUsers.adminLogin());
        for (String operation : defaultOperations()) {
            ActionRO action = createAction(root, operation);
            addAllowance(action, admin);
        }        
        return root;
    }

    public boolean mayPerform(
            String resourceUri, String operation, String userId) {
        
        AbstractUserRO user = userManager_.userById(userId);
        return mayPerform(resourceUri, operation, user);
    }

    public boolean mayPerform(
            String resourceUri, String operation, AbstractUserRO user) {
        
        Iterator<ResourceEntity> resourceIt = 
            getResourceRoot().iteratePath(resourceUri);
        
        boolean result = true;
        boolean isEmptyRightList = true;
        
        while (resourceIt.hasNext()) {
            
            ResourceEntity resource = resourceIt.next();
            
            if (isEmptyRightList && resource.hasActions()) {
                isEmptyRightList = false;
                result = false;
            }
            
            ActionEntity right = resource.actionForOperation(operation);
            if (right != null) {
                if (right.isAccessAllowed(user)) {
                    result = true;
                }
                else if (result && right.isAccessDenied(user)) {
                    result = false;
                }
            }
        }
        
        return result;
    }
    
    public void deleteResource(String resourceUri) {
        ResourceEntity resource = findResource(resourceUri);
        if (resource != null) {
            Session session = currentSession();            
            for (ResourceEntity child : resource.getChildrenDeep()) {
                for (ActionRO action : child.getActions())
                    session.delete(action);
                session.delete(child);
            }

            for (ActionRO action : resource.getActions())
                session.delete(action);
            
            ResourceEntity parent = resource.parent;
            session.delete(resource);
            
            if (parent != null)
                findAndDeleteObsoleteResources(parent);
        }
    }
    
    private void findAndDeleteObsoleteResources(ResourceEntity resource) {
        while (resource.isObsolete()) {
            // remove child entry from parent 
            // ensuring integrity
            ResourceEntity parent = resource.parent;
            currentSession().delete(resource);
            
            if (parent == null) {
                break;
            }
            else {
                currentSession().update(parent);            
                resource = parent;
            }
        }
    }

    public ActionEntity createAction(String resourceUri, String operation) {
        ResourceEntity resource = findOrCreateResource(resourceUri);
        return createAction(resource, operation);
    }

    public ActionEntity createAction(ResourceRO resource, String operation) {
        
        ResourceEntity resourceEntity = (ResourceEntity) resource;
        
        String id = actionId(resourceEntity, operation);
        ActionEntity action = new ActionEntity(id, resourceEntity, operation);        
        resourceEntity.addAction(action);
        
        Session session = currentSession();
        session.persist(action);
        currentSession().update(resource);
        
        return action;
    }
    
    public ActionEntity actionBy(String resourceUri, String operation) {
        ResourceEntity resource = findOrCreateResource(resourceUri);
        ActionEntity action = resource.actionForOperation(operation);
        
        if (action == null) {
            String id = actionId(resource, operation);
            action = new ActionEntity(id, resource, operation);        
            resource.addAction(action);
            
            currentSession().persist(action);
            currentSession().update(resource);
        }
        
        return action;
    }
    
    public void deleteAction(ActionRO action) {        
        ActionEntity actionEntity = (ActionEntity) action;        
        ResourceEntity resource = actionEntity.getResource();
        resource.removeRight(actionEntity);
        
        currentSession().delete(action);        
        findAndDeleteObsoleteResources(resource);
    }

    private String actionId(ResourceEntity resource, String operation) {
        return concat(resource.id, "_", operation);
    }
    
    private ResourceEntity findResource(String resourceUri) {
        Iterator<String> pathItemsIt = pathItems(resourceUri).iterator();        
        
        ResourceEntity result = getResourceRoot();
        while (pathItemsIt.hasNext()) {
            String pathItem = pathItemsIt.next();
            
            if (result.hasChild(pathItem)) {
                result = result.getChild(pathItem);
            }
            else {
                result = null;
                break;
            }
        }
        return result;
    }
    
    private ResourceEntity findOrCreateResource(String resourceUri) {
        Iterator<String> pathItemsIt = pathItems(resourceUri).iterator();
        
        ResourceEntity result = getResourceRoot();
        while (pathItemsIt.hasNext()) {
            String pathItem = pathItemsIt.next();
            
            if (result.hasChild(pathItem)) {
                result = result.getChild(pathItem);
            }
            else {
                ResourceEntity child = result.addChild(pathItem);
                currentSession().persist(child);
                currentSession().update(result);
                result = child;
            }
        }
        return result;
    }

    public void addAllowance(ActionRO action, AbstractUserRO user) {
        ((ActionEntity) action).allowAccessTo(user);
        currentSession().update(action);
    }

    public void removeAllowance(ActionRO action, AbstractUserRO user) {
        ActionEntity actionEntity = (ActionEntity) action;
        actionEntity.removeAllowanceFor(user);
        currentSession().update(action);
        
        if (actionEntity.isObsolete()) {
            deleteAction(actionEntity);
        }        
    }

    public void addDenial(ActionRO action, AbstractUserRO user) {
        ((ActionEntity) action).denyAccessTo(user);
        currentSession().update(action);
    }

    public void removeDenial(ActionRO action, AbstractUserRO user) {
        ActionEntity actionEntity = (ActionEntity) action;
        actionEntity.removeDenialFor(user);
        currentSession().update(action);
        
        if (actionEntity.isObsolete()) {
            deleteAction(actionEntity);
        } 
    }
}