/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.source.xml;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import javax.jcr.Item;
import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.avalon.framework.CascadingRuntimeException;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceFactory;
import org.apache.excalibur.source.SourceUtil;

import com.mindquarry.jcr.source.xml.sources.AbstractJCRNodeSource;
import com.mindquarry.jcr.source.xml.sources.FileOrFolderSource;
import com.mindquarry.jcr.source.xml.sources.QueryResultSource;
import com.mindquarry.jcr.source.xml.sources.XMLFileSource;

/**
 * This implementation extends <code>JCRSourceFactory</code> to provide an
 * XML-izable <code>JCRXMLNodeSource</code>s.
 * 
 * <p>
 * An URI for this source is either (i) a direct path in the repository or (ii)
 * an enclosed JCR query. The path to the repository would be given simply as
 * <code>jcr://root/folder/file</code>. For the queries:
 * <ul>
 * <li>XPATH: <code>jcr://users/*#//name</code> (which maps to the xpath
 * query <code>//name</code> executed in all documents under /users)</li>
 * </ul>
 * 
 * @author <a
 *         href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">Alexander
 *         Klimetschek</a>
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JCRXMLSourceFactory implements ThreadSafe, SourceFactory,
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
            session = repo.login(new SimpleCredentials("alexander.saar",
                    "mypwd".toCharArray()));
        } catch (LoginException e) {
            throw new SourceException("Login to repository failed", e);
        } catch (RepositoryException e) {
            throw new SourceException("Cannot access repository", e);
        }
        // Compute the path
        String path = SourceUtil.getSpecificPart(uri);
        if (!path.startsWith("//")) {
            throw new MalformedURLException("Expecting " + scheme
                    + "://path and got " + uri);
        }
        // check for query syntax (eg. 'jcr://root/users/*#//name' interpreted
        // as 'jcr://root/users/*//name')
        if (path.indexOf("#") != -1) {
            path = path.replace("#", "");
            return executeQuery(session, path, Query.XPATH);
        } else {
            // standard direct hierarchy-resolving
            path = removeLeadingSlash(path);
            return createSource(session, path);
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
        AbstractJCRNodeSource result;
        
        // is the requested node really a node?
        try {
            Item item = session.getItem(path);
            if (!item.isNode()) {
                throw new SourceException("Path '" + path
                        + "' is a property (should be a node)");
            }
            // it is a node, try to analyse the node type
            Node node = (Node) item;
            if (node.isNodeType("nt:folder")) {
                result = new FileOrFolderSource(this, session, path);
            } else if (node.isNodeType("nt:file")) {
                // check the content type of the file
                Node contentNode = node.getNode("jcr:content");
                if (contentNode.isNodeType("xt:document")) {
                    // it is an XML document
                    result = new XMLFileSource(this, session, path);
                } else {
                    // it is no XML document
                    result = new FileOrFolderSource(this, session, path);
                }
            } else {
                throw new SourceException("Unsupported primary node type. "
                        + "Must be one of nt:file or nt:folder.");
            }
        } catch (Exception e) {
            throw new SourceException("An error occured.", e);
        }
        return result;
    }

    // =========================================================================
    // Internal methods
    // =========================================================================

    /**
     * Retrieves the reference of the JCR <code>Repository</code> to use.
     */
    protected void lazyInitRepository() {
        // check if already initialized
        if (repo != null) {
            return;
        }
        // otherwise try to lookup the repository
        try {
            repo = (Repository) manager.lookup(Repository.class.getName());
        } catch (Exception e) {
            throw new CascadingRuntimeException("Cannot lookup repository", e);
        }
    }

    /**
     * Executes an XPath Query on the JCR repository and returns a node
     * representing the query result.
     * 
     * <p>
     * Subclasses that use an extended, XMLizable JCRNodeSource should override
     * this method and return all nodes in the result.
     * 
     * @param session the session
     * @param statement the Xpath query statement
     * @param queryLang the language to use (should be Query.SQL or Query.XPATH)
     * @throws IOException when the query is wrong or the result was empty
     */
    protected Source executeQuery(Session session, String statement,
            String queryLang) throws IOException {
        try {
            QueryManager queryManager = session.getWorkspace()
                    .getQueryManager();
            Query query = queryManager.createQuery(statement, queryLang);
            QueryResult result = query.execute();

            NodeIterator nit = result.getNodes();
            if (nit.getSize() == 0) {
                return null;
            }
            return new QueryResultSource(this, nit);
        } catch (RepositoryException e) {
            throw new SourceException("Cannot execute query '" + statement
                    + "'", e);
        }
    }

    /**
     * Remove the leading slash from the given path for hierarchy-resolving.
     */
    private String removeLeadingSlash(String path) {
        // Remove first '/'
        path = path.substring(1);
        int pathLen = path.length();
        if (pathLen > 1) {
            // Not root: ensure there's no trailing '/'
            if (path.charAt(pathLen - 1) == '/') {
                path = path.substring(0, pathLen - 1);
            }
        }
        return path;
    }

    /**
     * Returns the scheme (probably <code>jcr</code>) for the URIs we handle.
     */
    public String getScheme() {
        return scheme;
    }
}
