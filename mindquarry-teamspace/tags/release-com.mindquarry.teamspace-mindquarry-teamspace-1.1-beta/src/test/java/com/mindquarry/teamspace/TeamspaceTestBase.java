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

import java.util.List;

import org.apache.avalon.framework.service.ServiceException;

import com.mindquarry.user.UserAdmin;
import com.mindquarry.user.UserTestBase;
import com.mindquarry.user.webapp.CurrentUser;

/**
 * @author <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public abstract class TeamspaceTestBase extends UserTestBase {
    
    protected List<String> springConfigClasspathResources() {        
        List<String> result = super.springConfigClasspathResources();
        result.add("META-INF/cocoon/spring/teamspace-context.xml");
        return result;
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        ((CurrentUser) lookup(CurrentUser.ROLE)).setId("admin");
    }
    
    protected UserAdmin lookupUserAdmin() throws ServiceException {
        return (UserAdmin) lookup(UserAdmin.class.getName());
    }
    
    protected TeamspaceAdmin lookupTeamspaceAdmin() throws ServiceException {
        return (TeamspaceAdmin) lookup(TeamspaceAdmin.class.getName());
    }
}
