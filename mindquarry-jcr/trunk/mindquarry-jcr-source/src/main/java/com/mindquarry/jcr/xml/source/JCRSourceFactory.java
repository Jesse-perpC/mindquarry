/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.xml.source;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.components.source.SourceUtil;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceFactory;
import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;

import com.mindquarry.common.index.Indexer;
import com.mindquarry.common.init.InitializationException;
import com.mindquarry.jcr.jackrabbit.xpath.JaxenQueryHandler;

/**
 * This implementation extends <code>JCRSourceFactory</code> to provide an
 * XML-izable <code>JCRXMLNodeSource</code>s.
 * 
 * <p>
 * An URI for this source is either (i) a direct path in the repository or (ii)
 * an enclosed JCR query. The path to the repository would be given simply as
 * <code>jcr://root/folder/file</code>. For the queries:
 * <ul>
 * <li>XPATH: <code>jcr:///users?//name</code> (which maps to the xpath query
 * <code>//name</code> executed in all documents under /users)</li>
 * </ul>
 * 
 * @author <a
 *         href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">Alexander
 *         Klimetschek</a>
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JCRSourceFactory extends AbstractLogEnabled implements ThreadSafe, SourceFactory,
        Configurable, Serviceable {
    /**
     * The reference to the JCR Repository to use as interface.
     */
    protected Repository repo;

    /**
     * Scheme, lazily computed at the first call to getSource().
     */
    protected String scheme;

    /**
     * Used to resolve other components.
     */
    protected ServiceManager manager;

    /**
     * Configuration for this component.
     */
    protected Configuration config;

    /**
     * The namespace-prefix mappings for this factory.
     */
    public static Map<String, String> configuredMappings;
    
    /**
     * The indexer to be used by this class.
     */
    protected Indexer indexer;

    // =========================================================================
    // Servicable interface
    // =========================================================================

    /**
     * Called at startup of this component.
     * 
     * @see org.apache.avalon.framework.service.Serviceable#service(org.apache.avalon.framework.service.ServiceManager)
     */
    public void service(ServiceManager manager) throws ServiceException {
        this.manager = manager;
        
        try {
            indexer = (Indexer) manager.lookup(Indexer.ROLE);
        } catch (Exception e) {
            getLogger().info("Indexer could not be found!", e);
        }

        // the repository is lazily initialized to avoid circular dependency
        // between SourceResolver and JackrabbitRepository that leads to a
        // StackOverflowError at initialization time
    }

    // =========================================================================
    // Configurable interface
    // =========================================================================

    /**
     * Configures this component based on some <code>Configuration</code> read
     * from an XML config file. The configurable elements should be defined in
     * the class javadoc comment!
     * 
     * @see org.apache.avalon.framework.configuration.Configurable#configure(org.apache.avalon.framework.configuration.Configuration)
     */
    public void configure(Configuration config) throws ConfigurationException {
        this.config = config;
        if (null == JCRSourceFactory.configuredMappings) {
            JCRSourceFactory.configuredMappings = new HashMap<String, String>();

            Configuration mappings = config.getChild("mappings"); //$NON-NLS-1$
            for (Configuration mapping : mappings.getChildren("mapping")) { //$NON-NLS-1$
                String namespace = mapping.getAttribute("namespace");
                String prefix = mapping.getAttribute("prefix");
                JCRSourceFactory.configuredMappings.put(namespace, prefix); //$NON-NLS-1$
            }
        }
    }

    // =========================================================================
    // SourceFactory interface
    // =========================================================================

    /**
     * Retrieves a <code>Source</code> for the given URI. The URI can also be
     * an Xpath or SQL query conforming to the JCR query support.
     * 
     * @see org.apache.excalibur.source.SourceFactory#getSource(java.lang.String,
     *      java.util.Map)
     */
    public Source getSource(String uri, Map parameters) throws IOException,
            MalformedURLException {
        lazyInitRepository();

        // extract protocol identifier
        if (scheme == null) {
            scheme = SourceUtil.getScheme(uri);
        }
        // init session
        Session session;
        try {
            session = repo.login(new SimpleCredentials(config
                    .getAttribute("login"), config.getAttribute("password")
                    .toCharArray()));
        } catch (LoginException e) {
            throw new SourceException("Login to repository failed.", e);
        } catch (RepositoryException e) {
            throw new SourceException("Cannot access repository.", e);
        } catch (ConfigurationException e) {
            throw new SourceException("Cannot access configuration data.", e);
        }
        // check for query syntax (eg. 'jcr:///users#//name' interpreted
        // as 'jcr:///jcr:root/users//name')
        if (uri.indexOf("?") != -1) {
            return (QueryResultSource) executeQuery(session, SourceUtil
                    .getPath(uri), SourceUtil.getQuery(uri));
        } else {
            // standard direct hierarchy-resolving
            return (JCRNodeWrapperSource) createSource(session, SourceUtil
                    .getPath(uri));
        }
    }

    /**
     * @see org.apache.excalibur.source.SourceFactory#release(org.apache.excalibur.source.Source)
     */
    public void release(Source source) {
        // nothing to do here
    }

    // =========================================================================
    // Custom public interface
    // =========================================================================

    /**
     * Creates a new source given a session and a path
     * 
     * @param session the session
     * @param path the absolute path
     * @return a new source
     * @throws SourceException
     */
    public AbstractJCRNodeSource createSource(Session session, String path)
            throws SourceException {
        return new JCRNodeWrapperSource(this, session, path);
    }

    /**
     * Returns the scheme (probably <code>jcr</code>) for the URIs we handle.
     */
    public String getScheme() {
        return scheme;
    }

    // =========================================================================
    // Internal methods
    // =========================================================================

    /**
     * Executes an XPath Query on the JCR repository and returns a node
     * representing the query result.
     * 
     * <p>
     * Subclasses that use an extended, XMLizable JCRNodeSource should override
     * this method and return all nodes in the result.
     * 
     * @param session the session
     * @param path the path that is used as root of the query
     * @param statement the Xpath query statement
     * @param queryLang the language to use (should be Query.SQL or Query.XPATH)
     * @throws IOException when the query is wrong or the result was empty
     */
    protected Source executeQuery(Session session, String path, String statement)
            throws IOException {
        try {
            // modify path for query execution
            statement = path + statement;

            QueryManager queryManager = session.getWorkspace()
                    .getQueryManager();
            Query query = queryManager.createQuery(statement, JaxenQueryHandler.FULL_XPATH);
            QueryResult result = query.execute();

            return new QueryResultSource(this, session, result.getNodes());
        } catch (RepositoryException e) {
            throw new SourceException("Cannot execute query '" + statement
                    + "'", e);
        }
    }

    /**
     * Retrieves the reference of the JCR <code>Repository</code> to use.
     */
    protected void lazyInitRepository() {
        // check if already initialized
        if (repo != null) {
            return;
        }
        // otherwise try to lookup the repository
        String remoteRepoUrl = config.getAttribute("rmi", null);
        boolean isRemoteRepositoryConfigured = null != remoteRepoUrl;

        if (isRemoteRepositoryConfigured)
            repo = createClientRepository(remoteRepoUrl);
        else
            repo = lookupLocalRepository();
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
                    + "jcr repository for repository url:" + remoteRepoUrl, e);
        } catch (MalformedURLException e) {
            throw new InitializationException("could not create client "
                    + "repository for repository url:" + remoteRepoUrl, e);
        } catch (RemoteException e) {
            throw new InitializationException("could not create client "
                    + "repository for repository url:" + remoteRepoUrl, e);
        } catch (NotBoundException e) {
            throw new InitializationException("could not create client "
                    + "repository for repository url:" + remoteRepoUrl, e);
        }
    }
}
