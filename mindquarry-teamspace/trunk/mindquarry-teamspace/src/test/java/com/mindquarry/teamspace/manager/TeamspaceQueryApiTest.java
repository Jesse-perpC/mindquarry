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
package com.mindquarry.teamspace.manager;

import org.apache.avalon.framework.service.ServiceException;

import com.mindquarry.teamspace.TeamspaceAdmin;
import com.mindquarry.teamspace.TeamspaceTestBase;

public class TeamspaceQueryApiTest extends TeamspaceTestBase {

    public void testTeamspacesForUser() throws ServiceException {
                
        TeamspaceAdmin admin = lookupTeamspaceAdmin();
        // test with user that does not exists
        assertEquals(0, admin.teamspacesForUser("foo").size());
        
        // test with ivnalid userId
        admin.teamspacesForUser("");
    }

}
