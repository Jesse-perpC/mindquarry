package com.mindquarry.teamspace.manager;

import java.util.Collection;
import java.util.LinkedList;

import com.mindquarry.common.persistence.EntityBase;
import com.mindquarry.teamspace.TeamspaceRO;
import com.mindquarry.teamspace.User;

public class UserEntity extends EntityBase implements User {
    
    private String name;
    
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

    public void addTeamspaceReference(TeamspaceRO teamspace) {
        this.teamspaceReferences.add(teamspace.getId());
    }

    public void removeTeamspaceReference(TeamspaceRO teamspace) {
        this.teamspaceReferences.remove(teamspace.getId());
    }
}
