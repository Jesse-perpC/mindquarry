/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package com.mindquarry.jcr.client;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;

import com.mindquarry.common.init.InitializationException;

/**
 * Component that has everything to act as a client for a JCR repository. It is
 * configured with the credentials (username and password) and the optional
 * URL for an RMI remote repository (if none is specified, a local JCR will be
 * used via a lookup for a component under the javax.jcr.Repository name).
 * 
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 * 
 */
public class JCRClient extends AbstractLogEnabled implements
        Configurable, Serviceable {

    /**
     * The reference to the JCR Repository to use as interface.
     */
    protected Repository repo;

    /**
     * Used to resolve other components.
     */
    protected ServiceManager manager;

    /**
     * Configuration: the url of an remote RMI repository (optional)
     */
    private String remoteRepoUrl;

    /**
     * Configuration: the username to log into the repository
     */
    private String username;

    /**
     * Configuration: the password to log into the repository
     */
    private String password;

    public void configure(Configuration config) throws ConfigurationException {
        this.username = config.getAttribute("login");
        this.password = config.getAttribute("password");
        this.remoteRepoUrl = config.getAttribute("rmi", null);
    }

    public void service(ServiceManager manager) throws ServiceException {
        this.manager = manager;
    }

    // =========================================================================
    // Public interface
    // =========================================================================

    /**
     * Logs into the repository and returns a JCR session if successfull.
     * @throws RepositoryException 
     * @throws LoginException 
     */
    public Session login() throws LoginException, RepositoryException {
        lazyInitRepository();
        
        return repo.login(new SimpleCredentials(username, password.toCharArray()));
    }

    // =========================================================================
    // Internal methods
    // =========================================================================

    /**
     * Retrieves the reference of the JCR <code>Repository</code> to use.
     */
    private void lazyInitRepository() {
        // check if already initialized
        if (repo != null) {
            return;
        }
        // otherwise try to lookup the repository
        boolean isRemoteRepositoryConfigured = null != this.remoteRepoUrl;

        if (isRemoteRepositoryConfigured) {
            repo = createClientRepository(remoteRepoUrl);
        } else {
            repo = lookupLocalRepository();
        }
    }

    private Repository lookupLocalRepository() {
        try {
            return (Repository) manager.lookup(Repository.class.getName());
        } catch (ServiceException e) {
            throw new InitializationException(
                    "Cannot lookup local jcr repository.", e);
        }
    }

    private Repository createClientRepository(String remoteRepoUrl) {
        ClientRepositoryFactory factory = new ClientRepositoryFactory();
        try {
            return factory.getRepository(remoteRepoUrl);
        } catch (ClassCastException e) {
            throw new InitializationException("could not create client "
                    + "jcr repository for repository URL:" + remoteRepoUrl, e);
        } catch (MalformedURLException e) {
            throw new InitializationException("could not create client "
                    + "repository for repository URL:" + remoteRepoUrl, e);
        } catch (RemoteException e) {
            throw new InitializationException("could not create client "
                    + "repository for repository URL:" + remoteRepoUrl, e);
        } catch (NotBoundException e) {
            throw new InitializationException("could not create client "
                    + "repository for repository URL:" + remoteRepoUrl, e);
        }
    }
}