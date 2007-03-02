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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mindquarry.common.persistence.EntityBase;
import com.mindquarry.common.source.SerializationName;
import com.mindquarry.teamspace.Teamspace;
import com.mindquarry.user.UserRO;


/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
@SerializationName("teamspace")
public class TeamspaceEntity extends EntityBase implements Teamspace {

    private String name;
    private String description;
    private List<UserRO> users;
    private Map<String, String> properties;

    
    /**
     * 
     */
    public TeamspaceEntity() {
        this.id = "".intern();
        this.name = "".intern();
        this.description = "".intern();
        this.properties = new HashMap<String, String>();
        this.users = Collections.emptyList();
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
    
    
    public List<UserRO> getUsers() { 
        return Collections.unmodifiableList(users);
    }
    
    void setUsers(List<UserRO> value) {
        this.users = value;
    }

    /**
     * Getter for properties.
     *
     * @return the properties
     */
    public Map<String, String> getProperties() {
        return properties;
    }

    /**
     * Setter for properties.
     *
     * @param properties the properties to set
     */
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
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
}
