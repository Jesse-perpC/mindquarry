/*
 * Copyright (C) 2006-2007 MindQuarry GmbH, All Rights Reserved
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
package com.mindquarry.common.persistence;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">your full name</a>
 */
public abstract class EntityBase {

    protected String id;

    public EntityBase() {}
    
    /**
     * Getter for id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for id.
     *
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
}
