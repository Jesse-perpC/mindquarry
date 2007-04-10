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
package com.mindquarry.auth.manager;

import com.mindquarry.auth.ProfileRO;
import com.mindquarry.auth.manager.RightSet;
import com.mindquarry.persistence.api.Entity;



/**
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
@Entity(parentFolder="profiles")
public final class ProfileEntity extends AbstractRight implements ProfileRO {

    public RightSet rights;
    
    public ProfileEntity() { }
    
    public ProfileEntity(String id) {
        super(id);
        rights = new RightSet();
    }
    
    void add(RightEntity right) {
        rights.add(right);
    }
    
    void remove(RightEntity right) {
        rights.remove(right);
    }
}
