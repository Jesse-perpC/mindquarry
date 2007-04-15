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
import com.mindquarry.user.UserQuery;
import com.mindquarry.user.UserRO;
import com.mindquarry.user.manager.DefaultUsers;


/**
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class Authorization implements AuthorizationAdmin {
    
    private UserQuery userQuery;
    
    private SessionFactory sessionFactory_;
    
    /**
     * Setter for userQuery bean, set by spring at object creation
     */
    public void setUserQuery(UserQuery userQuery) {
        this.userQuery = userQuery;
    }

    /**
     * Setter for sessionFactory bean, set by spring at object creation
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
        session.commit();
        
        if (queryResult.isEmpty())
            return createAndInitializeResourceRoot();
        else
            return (ResourceEntity) queryResult.get(0);
    }
    
    private ResourceEntity createAndInitializeResourceRoot() {
        ResourceEntity root = new ResourceEntity("root", "", null);
        currentSession().persist(root);
        currentSession().commit();
        
        // allow admin to execute default operations on all resources
        // the right list will become non-empty. hence, other users
        // will be unauthorized by default
        UserRO admin = userQuery.userById(DefaultUsers.adminLogin());
        for (String operation : defaultOperations()) {
            ActionRO action = createAction(root, operation);
            addAllowance(action, admin);
        }
        return root;
    }

    public boolean mayPerform(
            String resourceUri, String operation, String userId) {
        
        AbstractUserRO user = this.userQuery.userById(userId);
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
        
        currentSession().commit();
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
        currentSession().commit();
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
        
        String id = concat(resourceEntity.id, "_", operation);                
        ActionEntity action = new ActionEntity(id, resourceEntity, operation);        
        resourceEntity.addAction(action);
        
        Session session = currentSession();
        session.persist(action);
        currentSession().update(resource);
        currentSession().commit();
        
        return action;
    }
    
    public void deleteAction(ActionRO action) {        
        ActionEntity actionEntity = (ActionEntity) action;        
        ResourceEntity resource = actionEntity.getResource();
        resource.removeRight(actionEntity);
        
        currentSession().delete(action);        
        //findAndDeleteObsoleteResources(resource);
        
        currentSession().commit();
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
        currentSession().commit();
    }

    public void removeAllowance(ActionRO action, AbstractUserRO user) {
        ((ActionEntity) action).removeAllowanceFor(user);
        currentSession().update(action);
        currentSession().commit();
    }

    public void addDenial(ActionRO action, AbstractUserRO user) {
        ((ActionEntity) action).denyAccessTo(user);
        currentSession().update(action);
        currentSession().commit();
    }

    public void removeDenial(ActionRO action, AbstractUserRO user) {
        ((ActionEntity) action).removeDenialFor(user);
        currentSession().update(action);
        currentSession().commit();
    }
}