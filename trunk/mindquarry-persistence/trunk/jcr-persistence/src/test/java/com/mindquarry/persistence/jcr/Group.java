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

import com.mindquarry.persistence.api.Entity;
import com.mindquarry.persistence.api.Id;
import com.mindquarry.persistence.api.NamedQuery;

@Entity(parentFolder="groups")
@NamedQuery(name="groupById", query="/groups/{$groupId}")
public class Group  {
    
    @Id
    public String id;
    
    public UserSet members;
    
    public Group() {
        id = "";
        members = new UserSet();
    }
}
