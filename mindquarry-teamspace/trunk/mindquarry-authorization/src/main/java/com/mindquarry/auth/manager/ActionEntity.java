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

import com.mindquarry.auth.ActionRO;
import com.mindquarry.persistence.api.Entity;
import com.mindquarry.persistence.api.Id;
import com.mindquarry.user.AbstractUserRO;
import com.mindquarry.user.manager.AbstractUserSet;



/**
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
@Entity(parentFolder="actions")
public final class ActionEntity implements ActionRO {
    
    @Id public String id;
    
    public String operation;    
    public ResourceEntity resource;
    
    public  AbstractUserSet allowed;
    public  AbstractUserSet denied;
    
    public ActionEntity() { }
    
    public ActionEntity(String id, ResourceEntity resource, String operation) {    
        this.id = id;
        this.resource = resource;
        this.operation = operation;        
        this.allowed = new AbstractUserSet();
        this.denied = new AbstractUserSet();
    }

    public String getOperation() {
        return operation;
    }

    public ResourceEntity getResource() {
        return resource;
    }

    final void allowAccessTo(AbstractUserRO user) {
        allowed.add(user);
    }
    
    final void removeAllowanceFor(AbstractUserRO user) {
        allowed.remove(user);
    }
    
    final void denyAccessTo(AbstractUserRO user) {
        denied.add(user);
    }
    
    final void removeDenialFor(AbstractUserRO user) {
        denied.remove(user);
    }
    
    boolean isAccessAllowed(AbstractUserRO user) {
        return this.allowed.contains(user);
    }
    
    boolean isAccessDenied(AbstractUserRO user) {
        return this.denied.contains(user);
    }
    
    public final boolean equals(Object other) {
        if (this == other)
            return true;
        if (! (other instanceof ActionEntity))
            return false;
        
        ActionEntity otherAction = (ActionEntity) other;
        return id.equals(otherAction.id);
    }
    
    public final int hashCode() {
        int result = 1;
        result = result * 42 + this.id.hashCode();
        return result;
    }
}
