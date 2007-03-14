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

import com.mindquarry.persistence.jcr.annotations.Entity;
import com.mindquarry.persistence.jcr.annotations.Id;

@Entity(folder="teamspaces")
public class Team  {
    
    @Id
    private String name;
    
    private String title;
    
    private String description;
    
    /**
     * Getter for description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * Setter for description.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * Getter for name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * Setter for name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Getter for title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    /**
     * Setter for title.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    
}
