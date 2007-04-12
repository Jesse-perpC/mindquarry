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
package com.mindquarry.user.manager;

import com.mindquarry.persistence.api.Entity;
import com.mindquarry.persistence.api.Id;
import com.mindquarry.persistence.api.NamedQueries;
import com.mindquarry.persistence.api.NamedQuery;
import com.mindquarry.user.AbstractUserRO;
import com.mindquarry.user.RoleRO;


/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
@Entity(parentFolder="roles")
@NamedQueries({
    @NamedQuery(name=RoleEntity.BY_ID, query="/roles/{$id}"),
    @NamedQuery(name=RoleEntity.ALL, query="/roles/*[local-name() != 'photos']")
})
public final class RoleEntity implements RoleRO {
    
    static final String BY_ID = "getRoleById";
    static final String ALL = "getAllRoles";

    @Id private String id;
    public AbstractUserSet members;
    
    public RoleEntity() {
        this.id = "";
        this.members = new AbstractUserSet();
    }    
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public AbstractUserSet getMembers() {
        return members;
    }

    public void addMembers(AbstractUserRO member) {
        this.members.add(member);
    }

    final void add(AbstractUserRO user) {
        members.add(user);
    }
    
    final void remove(AbstractUserRO user) {
        members.remove(user);
    }
    
    public final boolean contains(AbstractUserRO user) {
        return members.contains(user);
    }
    
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof RoleEntity))
            return false;
        
        RoleEntity otherGroup = (RoleEntity) other;
        return this.id.equals(otherGroup.id);
    }
    
    public int hashCode() {
        int result = 1;
        result = result * 33 + this.id.hashCode();
        return result;
    }
}
