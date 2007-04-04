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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mindquarry.common.source.SerializationName;
import com.mindquarry.persistence.api.Entity;
import com.mindquarry.persistence.api.Id;
import com.mindquarry.persistence.api.NamedQueries;
import com.mindquarry.persistence.api.NamedQuery;
import com.mindquarry.teamspace.Teamspace;
import com.mindquarry.user.UserRO;


/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
@SerializationName("teamspace")
@Entity(parentFolder="teamspaces", asComposite=true)
@NamedQueries({
    @NamedQuery(name="getTeamById", query="/teamspaces/{$teamId}"),
    @NamedQuery(name="getAllTeams", query="/teamspaces/*"),
    @NamedQuery(name="getTeamsForUser", query="/teamspaces/*[jcr:content/users/item/@reference = /users/{$userId}/@jcr:uuid]")
})
public class TeamspaceEntity implements Teamspace {
    
    @Id private String id;
    private String name;
    private String description;
    
    public Set<UserRO> users;
    
    public Map<String, String> properties;

    
    /**
     * 
     */
    public TeamspaceEntity() {
        this.id = "".intern();
        this.name = "".intern();
        this.description = "".intern();
        this.properties = new HashMap<String, String>();
        this.users = new HashSet<UserRO>();
    }
    
    /**
     * @see com.mindquarry.teamspace.TeamspaceRO#getId()
     */
    public String getId() {
        return id;
    }
    
    /**
     * @see com.mindquarry.teamspace.Teamspace#setId(java.lang.String)
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * @see com.mindquarry.teamspace.TeamspaceRO#getDescription()
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * @see com.mindquarry.teamspace.Teamspace#setDescription(java.lang.String)
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @see com.mindquarry.teamspace.TeamspaceRO#getName()
     */
    public String getName() {
        return name;
    }
    
    
    /**
     * @see com.mindquarry.teamspace.Teamspace#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }
    
    
    public Set<? extends UserRO> getUsers() {
        return users;
    }

    /**
     * @see com.mindquarry.teamspace.Teamspace#getProperty(java.lang.String)
     */
    public String getProperty(String key) {
        return properties.get(key);
    }

    /**
     * @see com.mindquarry.teamspace.Teamspace#setProperty(java.lang.String, java.lang.String)
     */
    public void setProperty(String key, String value) {
        properties.put(key, value);
    }
    
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof TeamspaceEntity))
            return false;
        
        TeamspaceEntity otherTeam = (TeamspaceEntity) other;
        return this.id.equals(otherTeam.id);
    }
    
    public String toString() {
        return this.id;
    }
    
    public void addUser(UserRO user) {
        users.add(user);
    }
    
    public void removeUser(UserRO user) {
        users.remove(user);
    }
}
