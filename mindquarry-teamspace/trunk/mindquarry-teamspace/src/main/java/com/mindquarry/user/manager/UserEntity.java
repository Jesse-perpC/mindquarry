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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

import com.mindquarry.common.source.SerializationIgnore;
import com.mindquarry.common.source.SerializationName;
import com.mindquarry.persistence.api.Entity;
import com.mindquarry.persistence.api.Id;
import com.mindquarry.persistence.api.NamedQueries;
import com.mindquarry.persistence.api.NamedQuery;
import com.mindquarry.user.User;

@SerializationName("user")
@Entity(parentFolder="users")
@NamedQueries({
    @NamedQuery(name=UserEntity.BY_ID, query="/users/{$userId}"),
    @NamedQuery(name=UserEntity.ALL, query="/users/*[local-name() != 'photos']")
})
public final class UserEntity implements User {
    
    static final String BY_ID = "getUserById";
    static final String ALL = "getAllUsers";
    
    @Id private String id;
    
    private String password;
    
    private String name;
    
    private String surname;
    
    private String email;
    
    private String skills;


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
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
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
    @SerializationIgnore
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
