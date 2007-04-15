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
package com.mindquarry.teamspace;

import java.util.Collection;

/**
 * Provides methods to query teamspace related data.
 *
 * @author 
 * <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public interface TeamspaceQuery {
    
    public static final String ROLE = TeamspaceQuery.class.getName();
    
    /**
     * return the list of all teamspaces the user participates in;
     * each teamspace in the result list contains also
     * a list of all participating users. 
     */
    Collection<? extends TeamspaceRO> teamspacesForUser(String userId);
    
    TeamspaceRO teamspaceById(String teamspaceId);
}
