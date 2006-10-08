/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.manager;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;

import com.mindquarry.common.init.InitializationException;
import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;
import com.mindquarry.teamspace.TeamspaceAdmin;
import com.mindquarry.teamspace.TeamspaceRO;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">your full name</a>
 */
class TeamspaceManager implements TeamspaceAdmin,
    Serviceable, Configurable, Initializable {

    static final String REPOS_BASE_PATH_PROPERTY = "mindquarry.reposbasepath";
    
    private String reposBasePath_;
    
    private SessionFactory sessionFactory_;
    
    private File reposBaseDirectory_;
    
    /**
     * {@inheritDoc}
     * @see org.apache.avalon.framework.service.Serviceable#service(org.apache.avalon.framework.service.ServiceManager)
     */
    public void service(ServiceManager serviceManager) throws ServiceException {
        String name = SessionFactory.class.getName();
        sessionFactory_ = (SessionFactory) serviceManager.lookup(name);
    }

    /**
     * {@inheritDoc}
     * @see org.apache.avalon.framework.configuration.Configurable#configure(org.apache.avalon.framework.configuration.Configuration)
     */
    public void configure(Configuration configuration) 
        throws ConfigurationException {
        
        reposBasePath_ = configuration.getAttribute("reposbasepath", null);

        if (null != System.getProperty(REPOS_BASE_PATH_PROPERTY))
            reposBasePath_ = System.getProperty(REPOS_BASE_PATH_PROPERTY);
        
        if (null == reposBasePath_) 
            throw new ConfigurationException(
                    "'mindquarry.reposbasepath' is not set, whether as " +
                    "container configuration nor as system property. " +
                    "It must be set to a valid, " +
                    "existing base directory for repositories");
    }

    /**
     * @throws Exception
     */
    public void initialize() throws Exception {
        
        reposBaseDirectory_ = new File(reposBasePath_);       
        
        if (! reposBaseDirectory_.exists() 
                || ! reposBaseDirectory_.isDirectory()) 
            throw new InitializationException(
                    "'mindquarry.reposbasepath' is not set to a valid, " +
                    "existing base directory for repositories.");        
    }

    public void create(String id, String name, String description) {
        TeamspaceEntity teamspace = new TeamspaceEntity();
        teamspace.setId(id);
        teamspace.setName(name);
        teamspace.setDescription(description);
        Session session = currentSession();
        session.persist(teamspace);
        session.commit();
    }

    public void remove(String id) {
        Session session = currentSession();
        TeamspaceEntity teamspace = queryTeamspaceById(session, id);
        session.delete(teamspace);
        session.commit();
    }

    public List<TeamspaceRO> allTeamspaces() {
        Session session = currentSession();
        List queryResult = session.query("getAllTeamspaces", new Object[0]);
        
        List<TeamspaceRO> result = new LinkedList<TeamspaceRO>();
        for (Object object : queryResult)
            result.add((TeamspaceEntity) object);
        
        session.commit();
        return result;
    }

    public List<TeamspaceRO> teamspacesForUser(String userId) {
        Session session = currentSession();
        UserEntity user = queryUserById(session, userId);
        
        List<TeamspaceRO> result = new LinkedList<TeamspaceRO>();

        for (String teamRef : user.getTeamspaceReferences()) {
            TeamspaceEntity teamspace = queryTeamspaceById(session, teamRef);
            result.add(teamspace);
        }
        
        session.commit();
        return result;
    }
    
    private TeamspaceEntity queryTeamspaceById(Session session, String id) {
        List queryResult = session.query("getTeamspaceById", new Object[] {id});
        return (TeamspaceEntity) queryResult.get(0);
    }
    
    private UserEntity queryUserById(Session session, String id) {
        List queryResult = session.query("getUserById", new Object[] {id});
        return (UserEntity) queryResult.get(0);
    }

    public String workspaceUri(String id) {
        Session session = currentSession();
        TeamspaceEntity teamspace = queryTeamspaceById(session, id);
        String result = teamspace.getWorkspaceUri();
        session.commit();
        return result;
    }
    
    private Session currentSession() {
        return sessionFactory_.currentSession();
    }

}
