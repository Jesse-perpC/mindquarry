package com.mindquarry.user.manager;

import java.util.HashSet;
import java.util.Set;

import com.mindquarry.common.persistence.EntityBase;
import com.mindquarry.teamspace.TeamspaceRO;
import com.mindquarry.user.UserRO;

public final class UserEntity extends EntityBase implements UserRO {
    
    private String password;
    
    private String name;
    
    private String surname;
    
    private String email;
    
    private String skills;
    
    Set<String> teamspaceReferences;


    /**
     * 
     */
    public UserEntity() {
        id = "";
        password = "";
        name = "";
        surname = "";
        email = "";
        skills = "";
        teamspaceReferences = new HashSet<String>();
    }
    
    /**
     * setter for password
     * 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @see com.mindquarry.user.UserRO#getPassword()
     */
    public String getPassword() {
        return password;
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
     * @see com.mindquarry.teamspace.User#setSurname(java.lang.String)
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @see com.mindquarry.user.UserRO#getSurname()
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @see com.mindquarry.types.teamspace.User#getTeamspaces()
     */
    public Set<String> getTeamspaceReferences() {
        return teamspaceReferences;
    }

    /**
     * Setter for teamspaceReferences.
     *
     * @param teamspaceReferences the teamspaceReferences to set
     */
    public void setTeamspaceReferences(Set<String> teamspaces) {
        this.teamspaceReferences = teamspaces;
    }

    /**
     * @see com.mindquarry.user.UserRO#isMemberOf(com.mindquarry.teamspace.TeamspaceRO)
     */
    public boolean isMemberOf(TeamspaceRO teamspace) {
        return teamspaceReferences.contains(teamspace.getId());
    }

    /**
     * Getter for email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for email.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Getter for skills.
     *
     * @return the skills
     */
    public String getSkills() {
        return skills;
    }

    /**
     * Setter for skills.
     *
     * @param email the skills to set
     */
    public void setSkills(String skills) {
        this.skills = skills;
    }
    
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof UserEntity))
            return false;
        
        UserEntity otherUser = (UserEntity) other;
        return this.id.equals(otherUser.id);
    }
    
    public int hashCode() {
        int result = 1;
        result = result * 35 + this.id.hashCode();
        return result;
    }
}
