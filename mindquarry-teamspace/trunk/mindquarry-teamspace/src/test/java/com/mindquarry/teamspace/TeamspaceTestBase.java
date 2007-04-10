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

import static com.mindquarry.user.manager.DefaultUsers.defaultUsers;
import static com.mindquarry.user.manager.DefaultUsers.login;
import static com.mindquarry.user.manager.DefaultUsers.password;
import static com.mindquarry.user.manager.DefaultUsers.username;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;

import com.mindquarry.auth.manager.ProfileEntity;
import com.mindquarry.auth.manager.ResourceEntity;
import com.mindquarry.auth.manager.RightEntity;
import com.mindquarry.common.test.AvalonSpringContainerTestBase;
import com.mindquarry.persistence.api.JavaConfiguration;
import com.mindquarry.persistence.api.SessionFactory;
import com.mindquarry.teamspace.manager.TeamspaceEntity;
import com.mindquarry.user.User;
import com.mindquarry.user.UserAdmin;
import com.mindquarry.user.manager.GroupEntity;
import com.mindquarry.user.manager.UserEntity;

/**
 * @author <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public abstract class TeamspaceTestBase extends AvalonSpringContainerTestBase {
    
    protected List<String> springConfigClasspathResources() {
        
        System.setProperty("mindquarry.jcr.path",
                new File("target/repository").toURI().toString());
    
        System.setProperty("mindquarry.jcr.login", "admin");
        System.setProperty("mindquarry.jcr.pwd", "admin");
    
        List<String> result = super.springConfigClasspathResources();
        result.add("META-INF/cocoon/spring/jcr-repository-context.xml");
        result.add("META-INF/cocoon/spring/jcr-session-context.xml");
        result.add("META-INF/cocoon/spring/jcr-rmi-server-context.xml");
    
        result.add("META-INF/cocoon/spring/jcr-persistence-context.xml");        

        result.add("META-INF/cocoon/spring/teamspace-context.xml");
        return result;
    }

    protected void setUp() throws Exception {
        super.setUp();
        
        JavaConfiguration configuration = new JavaConfiguration();
        configuration.addClass(UserEntity.class);
        configuration.addClass(GroupEntity.class);
        configuration.addClass(TeamspaceEntity.class);

        configuration.addClass(RightEntity.class);
        configuration.addClass(ProfileEntity.class);
        configuration.addClass(ResourceEntity.class);
        
        SessionFactory sessionFactory = 
            (SessionFactory) lookup(SessionFactory.ROLE);        
        sessionFactory.configure(configuration);
        
        UserAdmin userAdmin = lookupUserAdmin();
        for (String[] userProfile : defaultUsers()) {
            userAdmin.createUser(login(userProfile), password(userProfile), 
                    username(userProfile), "", null, null);
        }
    }
    
    protected void tearDown() throws Exception {
        UserAdmin userAdmin = lookupUserAdmin();
        for (String[] userProfile : defaultUsers()) {
            User user = userAdmin.userById(login(userProfile));
            userAdmin.deleteUser(user);
        }
        super.tearDown();
    }
    
    protected Source resolveSource(String uri) throws ServiceException, IOException {
        SourceResolver resolver = (SourceResolver) lookup(SourceResolver.ROLE);
        return resolver.resolveURI(uri);
    }

    /**
     * Initializes the ComponentLocator
     * 
     * The configuration file is determined by the class name plus .xtest
     * appended, all '.' replaced by '/' and loaded as a resource via classpath
     */
    protected void prepare() throws Exception {
        String className = TeamspaceTestBase.class.getName();
        String xtestResourceName = className.replace('.', '/') + ".xtest";

        URL xtestResource = classLoader().getResource(xtestResourceName);
        this.prepare(xtestResource.openStream());
    }
    
    protected UserAdmin lookupUserAdmin() throws ServiceException {
        return (UserAdmin) lookup(UserAdmin.class.getName());
    }
    
    protected TeamspaceAdmin lookupTeamspaceAdmin() throws ServiceException {
        return (TeamspaceAdmin) lookup(TeamspaceAdmin.class.getName());
    }
    
    private ClassLoader classLoader() {
        return getClass().getClassLoader();
    }
}
