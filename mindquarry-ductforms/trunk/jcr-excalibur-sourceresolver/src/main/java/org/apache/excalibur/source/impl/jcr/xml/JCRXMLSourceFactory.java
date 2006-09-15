/*
 * Copyright 2006 Mindquarry GmbH, Potsdam, Germany
 * Copyright 2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.excalibur.source.impl.jcr.xml;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Map;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
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

/**
 * This implementation extends <code>JCRSourceFactory</code> to provide an
 * XML-izable <code>JCRXMLNodeSource</code>s.
 * 
 * <p>
 * An URI for this source is either (i) a direct path in the repository or (ii)
 * an enclosed JCR query. The path to the repository would be given simply as
 * <code>jcr://root/folder/file</code>. For the queries, three different
 * possibilities exist:
 * <ul>
 * <li>XPATH: <code>jcr://xpath!//folder/file</code> (which maps to the xpath
 * query <code>//folder/file</code>)</li>
 * <li>XPATH: <code>jcr://!//folder/file</code> (shorthand notation, same
 * query as above)</li>
 * <li>SQL:
 * <code>jcr://sql!select * from nt:base where jcr:path = '/root/myfile'</code>
 * (well, which maps to the sql query
 * <code>SELECT * FROM nt:base WHERE jcr:path = '/root/myfile'</code>)</li>
 * </ul>
 * Note that this implementation does only support queries that return a
 * content-node (with no children). If the result contains multiple nodes, the
 * first one is used. See also the
 * {@link #executeQuery(Session,String,String) executeQuery()} method.
 * 
 */
public class JCRXMLSourceFactory implements ThreadSafe, SourceFactory,
        Configurable, Serviceable {

    /**
     * A hash containing all XML elements that should be versionized
     */
    protected HashSet versionizedElements;

    /**
     * The reference to the JCR Repository to use as interface.
     */
    protected Repository repo;

    /**
     * Scheme, lazily computed at the first call to getSource()
     */
    protected String scheme;

    /**
     * Used to resolve other components
     */
    protected ServiceManager manager;

    // =========================================================================
    // Servicable interface
    // =========================================================================

    /**
     * Called at startup of this component.
     */
    public void service(ServiceManager manager) throws ServiceException {
        this.manager = manager;
        // this.repo is lazily initialized to avoid a circular dependency
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
     */
    public void configure(Configuration config) throws ConfigurationException {
        this.versionizedElements = new HashSet();

        Configuration[] children = config.getChildren();

        for (int i = 0; i < children.length; i++) {
            Configuration child = children[i];
            String name = child.getName();

            if ("versionize".equals(name)) {
                this.versionizedElements.add(child.getAttribute("element"));
            }
        }
    }

    // =========================================================================
    // SourceFactory interface
    // =========================================================================

    /**
     * Retrieves a <code>Source</code> for the given uri. The URI can also be
     * an Xpath or SQL query conforming to the JCR query support.
     */
    public Source getSource(String uri, Map parameters) throws IOException,
            MalformedURLException {
        lazyInit();

        if (this.scheme == null) {
            this.scheme = SourceUtil.getScheme(uri);
        }

        Session session;
        try {
            // TODO: accept a different workspace?
            session = repo.login();
        } catch (LoginException e) {
            throw new SourceException("Login to repository failed", e);
        } catch (RepositoryException e) {
            throw new SourceException("Cannot access repository", e);
        }

        // Compute the path
        String path = SourceUtil.getSpecificPart(uri);
        if (!path.startsWith("//")) {
            throw new MalformedURLException("Expecting " + this.scheme
                    + "://path and got " + uri);
        }

        // check for query syntax (eg. 'jcr://xpath!//element(*, nt:folder)' )

        if (path.startsWith("//!")) {
            return executeQuery(session, path.substring(3), Query.XPATH);
        } else if (path.startsWith("//xpath!")) {
            return executeQuery(session, path.substring(8), Query.XPATH);
        } else if (path.startsWith("//sql!")) {
            return executeQuery(session, path.substring(6), Query.SQL);
        }

        // standard direct hierarchy-resolving

        // Remove first '/'
        path = path.substring(1);
        int pathLen = path.length();
        if (pathLen > 1) {
            // Not root: ensure there's no trailing '/'
            if (path.charAt(pathLen - 1) == '/') {
                path = path.substring(0, pathLen - 1);
            }
        }

        return createSource(session, path);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.excalibur.source.SourceFactory#release(org.apache.excalibur.source.Source)
     */
    public void release(Source source) {
        // nothing to do here
    }

    // =========================================================================
    // Custom public interface
    // =========================================================================

    /**
     * Creates a new source given its parent and its node
     * 
     * @param parent the parent
     * @param node the node
     * @return a new source
     * @throws SourceException
     */
    public JCRXMLNodeSource createSource(JCRXMLNodeSource parent, Node node)
            throws SourceException {
        return new JCRXMLNodeSource(parent, node);
    }

    /**
     * Creates a new source given a session and a path
     * 
     * @param session the session
     * @param path the absolute path
     * @return a new source
     * @throws SourceException
     */
    public JCRXMLNodeSource createSource(Session session, String path)
            throws SourceException {
        return new JCRXMLNodeSource(this, session, path);
    }

    // =========================================================================
    // Internal methods
    // =========================================================================

    /**
     * Retrieves the reference of the JCR <code>Repository</code> to use.
     * 
     */
    protected void lazyInit() {
        // check if already initialized
        if (this.repo != null) {
            return;
        }

        try {
            this.repo = (Repository) manager.lookup(Repository.class.getName());
        } catch (Exception e) {
            throw new CascadingRuntimeException("Cannot lookup repository", e);
        }
    }

    /**
     * Executes an XPath Query on the JCR repository. This implementation only
     * allows queries that return one node with no children (because
     * JCRNodeSource is a simple, non-xmlizable Source, that does not know what
     * to do if someone wants to read (call getInputStream()) from a collection
     * (or folder-type) node). If the result contains multiple nodes, only the
     * first one is returned.
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
        if (queryLang == Query.SQL
                && !session.getRepository().getDescriptor(
                        Repository.OPTION_QUERY_SQL_SUPPORTED).equals("true")) {
            throw new SourceException(
                    "JCR Repository does not support SQL queries.");
        }

        try {
            QueryManager queryManager = session.getWorkspace()
                    .getQueryManager();
            Query query = queryManager.createQuery(statement, queryLang);
            QueryResult result = query.execute();

            NodeIterator nodeIter = result.getNodes();

            if (nodeIter.getSize() == 0) {
                return null;
            }

            // return only the first result.
            return new JCRXMLNodeSource(this, nodeIter.nextNode());
        } catch (RepositoryException e) {
            throw new SourceException("Cannot execute query '" + statement
                    + "'", e);
        }
    }

    /**
     * Returns the scheme (probably <code>jcr</code>) for the URIs we handle.
     * 
     */
    public String getScheme() {
        return this.scheme;
    }
}
