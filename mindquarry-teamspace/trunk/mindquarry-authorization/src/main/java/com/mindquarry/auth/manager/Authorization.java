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

import static com.mindquarry.common.lang.StringUtil.concat;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.mindquarry.auth.ActionRO;
import com.mindquarry.auth.AuthorizationAdmin;
import com.mindquarry.persistence.api.Session;
import com.mindquarry.persistence.api.SessionFactory;
import com.mindquarry.user.AbstractUserRO;
import com.mindquarry.user.UserQuery;


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
    
    private ResourceEntity getRoot() {
        Session session = currentSession();        
        List<Object> queryResult = session.query("getResourceById", "root");
        
        if (queryResult.isEmpty()) {
            ResourceEntity root = new ResourceEntity("root", "root");
            session.persist(root);
            return root;
        }
        else {
            return (ResourceEntity) queryResult.get(0);
        }
    }

    public boolean mayPerform(
            String resourceUri, String operation, String userId) {
        
        AbstractUserRO user = this.userQuery.userById(userId);
        return mayPerform(resourceUri, operation, user);
    }

    public boolean mayPerform(
            String resourceUri, String operation, AbstractUserRO user) {
        
        Iterator<String> pathItemsIt = pathItems(resourceUri).iterator();
        
        ResourceEntity resource = getRoot();;
        boolean result = true;
        boolean isEmptyRightList = true;
        
        while (pathItemsIt.hasNext()) {
            String pathItem = pathItemsIt.next();            
            if (! resource.hasChild(pathItem)) {
                break;
            }
            
            ResourceEntity child = resource.getChild(pathItem);
            if (isEmptyRightList && child.hasActions()) {
                isEmptyRightList = false;
                result = false;
            }
            
            ActionEntity right = child.actionForOperation(operation);
            if (right != null) {
                if (right.isAccessAllowed(user)) {
                    result = true;
                }
                else if (result && right.isAccessDenied(user)) {
                    result = false;
                }
            }
            resource = child;
        }
        
        currentSession().commit();
        return result;
    }

    public ActionEntity createAction(String resourceUri, String operation) {
        return createAction(null, resourceUri, operation);
    }

    public ActionEntity createAction(
            String id, String resourceUri, String operation) {        
                
        ResourceEntity resource = navigateToResource(resourceUri);
        
        if (id == null)
            id = concat(resource.id, "_", operation);
                
        ActionEntity result = new ActionEntity(id, resource, operation);
        resource.addAction(result);
        
        currentSession().update(resource);
        currentSession().commit();
        return result;
    }
    
    public void deleteAction(ActionRO action) {
        currentSession().delete(action);
        currentSession().commit();
    }
    
    private List<String> pathItems(String resourceUri) {
        String preparedUri = resourceUri.replaceFirst("/", "");
        return Arrays.asList(preparedUri.split("/"));
    }
    
    private ResourceEntity navigateToResource(String resourceUri) {
        Iterator<String> pathItemsIt = pathItems(resourceUri).iterator();        
        return navigateToResource(getRoot(), resourceUri, pathItemsIt);
    }
    
    private ResourceEntity navigateToResource(ResourceEntity parent, 
            String resourceUri, Iterator<String> pathItemsIt) {
        
        ResourceEntity result;
        if (! pathItemsIt.hasNext()) { 
            result = parent;
        }
        else {
            String pathItem = pathItemsIt.next();
            
            ResourceEntity child;
            if (parent.hasChild(pathItem)) {
                child = parent.getChild(pathItem);
            }
            else {
                child = parent.addChild(pathItem);
                currentSession().update(parent);
            }
            
            result = navigateToResource(child, resourceUri, pathItemsIt);
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