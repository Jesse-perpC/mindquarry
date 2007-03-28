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
package com.mindquarry.persistence.castor;

import java.util.Collection;
import java.util.LinkedList;

import com.mindquarry.common.persistence.EntityBase;

public class UserEntity extends EntityBase {
    
    private String name;
    
    private String surname;
    
    private Collection<String> teamspaceReferences;


    /**
     * 
     */
    public UserEntity() {
        id = "".intern();
        name = "".intern();
        teamspaceReferences = new LinkedList<String>();
    }

    /**
     * @see com.mindquarry.types.teamspace.User#getName()
     */
    public String getName() {
        return name;
    }

    /**
     * @see com.mindquarry.types.teamspace.User#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }

    
    /**
     * Getter for surname.
     *
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Setter for surname.
     *
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @see com.mindquarry.types.teamspace.User#getTeamspaces()
     */
    public Collection<String> getTeamspaceReferences() {
        return teamspaceReferences;
    }

    /**
     * Setter for teamspaceReferences.
     *
     * @param teamspaceReferences the teamspaceReferences to set
     */
    public void setTeamspaceReferences(Collection<String> teamspaces) {
        this.teamspaceReferences = teamspaces;
    }

    public void addTeamspaceReference(TeamspaceEntity teamspace) {
        this.teamspaceReferences.add(teamspace.getId());
    }

    public void removeTeamspaceReference(TeamspaceEntity teamspace) {
        this.teamspaceReferences.remove(teamspace.getId());
    }
}
