package com.mindquarry.user.manager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;

import com.mindquarry.common.persistence.EntityBase;
import com.mindquarry.teamspace.TeamspaceRO;
import com.mindquarry.user.User;

public final class UserEntity extends EntityBase implements User {
    
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
        password = encodePwd("");
        name = "";
        surname = "";
        email = "";
        skills = "";
        teamspaceReferences = new HashSet<String>();
    }
    
    /**
     * setter for password, only required for persistence layer.
     * This method is not part of the contract, please use instead of
     * the methods authenticate and changePassword.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * getter for password, only required for persistence layer.
     * This method is not part of the contract, please use instead of
     * the methods authenticate and changePassword.
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * checks if the specified equals the internal one
     * @return true if the password matches otherwise false
     */
    public boolean authenticate(String pwd) {
        return (pwd != null) && this.password.equals( encodePwd(pwd));
    }
    
    /**
     * Changes the current password to new value
     * if and only if the oldPwd param matches the current one.
     * 
     * @param oldPwd has to match to the current internal pwd value.
     * @param newPwd the new value for the password, must not be null or empty.
     * 
     * @return true if the oldPwd matches and thus 
     * the new value is set otherwise false
     */
    public boolean changePassword(String oldPwd, String newPwd) {
        if (this.password.equals(encodePwd(oldPwd)) && isValidPwd(newPwd)) {
            this.password = encodePwd(newPwd);
            return true;
        } else {
            return false;
        }
    }
    
    private String encodePwd(String pwd) {
        return base64Encode(md5(pwd));
    }
    
    private String md5(String input) {
        MessageDigest md5Digest;
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new UserException("could not get a md5 message digest", e);
        }
        md5Digest.update(input.getBytes());
        return new String(md5Digest.digest());
    }
    
    private String base64Encode(String input) {
        return new String(Base64.encodeBase64(input.getBytes()));
    }
    
    private boolean isValidPwd(String pwd) {
        return (pwd != null) && (pwd.length() != 0);        
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    /**
     * getter for teamspaceReferences, only required for persistence layer.
     * This method is not part of the contract and 
     * thus must not be used by any clients except the persistence layer.
     */
    public Set<String> getTeamspaceReferences() {
        return teamspaceReferences;
    }
    
    /**
     * setter for teamspaceReferences, only required for persistence layer.
     * This method is not part of the contract and 
     * thus must not be used by any clients except the persistence layer.
     */
    public void setTeamspaceReferences(Set<String> teamspaces) {
        this.teamspaceReferences = teamspaces;
    }
    
    /**
     * @see com.mindquarry.user.UserRO#teamspaces()
     */
    public Set<String> teamspaces() {
        return Collections.unmodifiableSet(this.teamspaceReferences);
    }

    /**
     * @see com.mindquarry.user.UserRO#isMemberOf(com.mindquarry.teamspace.TeamspaceRO)
     */
    public boolean isMemberOf(TeamspaceRO teamspace) {
        return isMemberOf(teamspace.getId());
    }

    /**
     * @see com.mindquarry.user.UserRO#isMemberOf(java.lang.String)
     */
    public boolean isMemberOf(String teamspaceId) {
        return teamspaceReferences.contains(teamspaceId);
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
