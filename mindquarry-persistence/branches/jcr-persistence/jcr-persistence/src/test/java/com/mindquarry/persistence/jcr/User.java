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
package com.mindquarry.persistence.jcr;

import java.util.List;

import com.mindquarry.persistence.api.Entity;
import com.mindquarry.persistence.api.Id;
import com.mindquarry.persistence.api.NamedQueries;
import com.mindquarry.persistence.api.NamedQuery;

@Entity(folder="users")
@NamedQueries({ 
    @NamedQuery(name="userByLogin", query="/users/{$userId}"),
    @NamedQuery(name="allUsers", query="/users/*[local-name() != 'photos']")
})
public class User  {
    
    @Id
    private String login;
    private String pwd;
    
    public String firstname;
    public String lastname;
    
    private List<String> skills;
    private String[] skillsArray;
    
    private List<Team> teams;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * Getter for skills.
     *
     * @return the skills
     */
    public List<String> getSkills() {
        return skills;
    }

    /**
     * Setter for skills.
     *
     * @param skills the skills to set
     */
    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    /**
     * Getter for skillsArray.
     *
     * @return the skillsArray
     */
    public String[] getSkillsArray() {
        return skillsArray;
    }

    /**
     * Setter for skillsArray.
     *
     * @param skillsArray the skillsArray to set
     */
    public void setSkillsArray(String[] skillsArray) {
        this.skillsArray = skillsArray;
    }

    /**
     * Getter for teams.
     *
     * @return the teams
     */
    public List<Team> getTeams() {
        return teams;
    }

    /**
     * Setter for teams.
     *
     * @param teams the teams to set
     */
    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}
