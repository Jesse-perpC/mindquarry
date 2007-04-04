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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mindquarry.persistence.api.Entity;
import com.mindquarry.persistence.api.Id;
import com.mindquarry.persistence.api.NamedQueries;
import com.mindquarry.persistence.api.NamedQuery;

@Entity(parentFolder="teamspaces", asComposite=true)
@NamedQueries({ 
    @NamedQuery(name="teamById", query="/teamspaces/{$teamId}"),
    @NamedQuery(name="allTeams", query="/teamspaces/*")
})
public class Team  {
    
    @Id
    public String name = "";
    
    public String title = "";
    
    public String description = "";
    
    public Map<String, List<String>> fooMap = new HashMap<String, List<String>>();    
}
