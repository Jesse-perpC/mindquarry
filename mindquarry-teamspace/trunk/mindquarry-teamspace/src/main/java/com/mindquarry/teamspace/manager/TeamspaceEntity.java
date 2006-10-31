/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.manager;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mindquarry.common.persistence.EntityBase;
import com.mindquarry.teamspace.TeamspaceDefinition;
import com.mindquarry.teamspace.UserRO;


/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">your full name</a>
 */
public class TeamspaceEntity extends EntityBase implements TeamspaceDefinition {

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
     * @see com.mindquarry.teamspace.TeamspaceDefinition#setDescription(java.lang.String)
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
     * @see com.mindquarry.teamspace.TeamspaceDefinition#setName(java.lang.String)
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
     * @see com.mindquarry.teamspace.TeamspaceDefinition#getProperty(java.lang.String)
     */
    public String getProperty(String key) {
        return properties.get(key);
    }

    /**
     * @see com.mindquarry.teamspace.TeamspaceDefinition#setProperty(java.lang.String, java.lang.String)
     */
    public void setProperty(String key, String value) {
        properties.put(key, value);
    }
}
