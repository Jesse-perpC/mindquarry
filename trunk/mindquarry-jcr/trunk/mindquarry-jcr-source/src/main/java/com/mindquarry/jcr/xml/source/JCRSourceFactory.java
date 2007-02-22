/*
 * Copyright (C) 2006-2007 MindQuarry GmbH, All Rights Reserved
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
package com.mindquarry.jcr.xml.source;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.components.source.SourceUtil;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceFactory;

import com.mindquarry.common.index.IndexClient;
import com.mindquarry.events.EventBroker;
import com.mindquarry.events.exception.EventAlreadyRegisteredException;
import com.mindquarry.events.exception.UnknownEventException;
import com.mindquarry.jcr.client.JCRClient;
import com.mindquarry.jcr.jackrabbit.xpath.JaxenQueryHandler;
import com.mindquarry.jcr.xml.source.events.UrlResolvedEvent;

/**
 * This SourceFactory provides access to a Java Content Repository (JCR)
 * including an XML-to-JCR-nodes binding. The source implementation returned is
 * either a JCRNodeWrapperSource for direct node requests or a QueryResultSource
 * for a query result (including multiple nodes).
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
public class JCRSourceFactory extends JCRClient implements ThreadSafe,
        SourceFactory, Configurable, Serviceable {
    /**
     * Scheme, lazily computed at the first call to getSource().
     */
    protected String scheme;

    /**
     * The namespace-prefix mappings for this factory.
     */
    public static Map<String, String> configuredMappings;

    /**
     * The list of index exclude patterns.
     */
    public static List<String> iExcludes;

    /**
     * Index client to be used for change notifications
     */
    protected IndexClient iClient;

    /**
     * Event broker for publishing inter-application events.
     */
    protected EventBroker broker;

    // =========================================================================
    // Servicable interface
    // =========================================================================

    /**
     * Called at startup of this component.
     * 
     * @see org.apache.avalon.framework.service.Serviceable#service(org.apache.avalon.framework.service.ServiceManager)
     */
    public void service(ServiceManager manager) throws ServiceException {
        super.service(manager);

        iClient = (IndexClient) manager.lookup(IndexClient.ROLE);
        broker = (EventBroker) manager.lookup(EventBroker.ROLE);
        try {
            broker.registerEvent(UrlResolvedEvent.ID);
        } catch (EventAlreadyRegisteredException e) {
            // event was already registered, so we have nothing more to do here
        }

        // the repository is lazily initialized to avoid circular dependency
        // between SourceResolver and JackrabbitRepository that leads to a
        // StackOverflowError at initialization time
    }

    public ServiceManager getServiceManager() {
        return this.manager;
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
        super.configure(config);

        if (null == JCRSourceFactory.configuredMappings) {
            // load namespace mappings
            JCRSourceFactory.configuredMappings = new HashMap<String, String>();

            Configuration mappings = config.getChild("mappings"); //$NON-NLS-1$
            for (Configuration mapping : mappings.getChildren("mapping")) { //$NON-NLS-1$
                String namespace = mapping.getAttribute("namespace"); //$NON-NLS-1$
                String prefix = mapping.getAttribute("prefix"); //$NON-NLS-1$
                JCRSourceFactory.configuredMappings.put(namespace, prefix);
            }
            // load index excludes
            JCRSourceFactory.iExcludes = new ArrayList<String>();

            Configuration index = config.getChild("index"); //$NON-NLS-1$
            Configuration excludes = index.getChild("excludes"); //$NON-NLS-1$
            for (Configuration exclude : excludes.getChildren("exclude")) { //$NON-NLS-1$
                JCRSourceFactory.iExcludes.add(exclude.getValue());
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

        // extract protocol identifier
        if (scheme == null) {
            scheme = SourceUtil.getScheme(uri);
        }
        // init session
        Session session;
        try {
            session = login();
        } catch (LoginException e) {
            throw new SourceException("Login to repository failed.", e);
        } catch (RepositoryException e) {
            throw new SourceException("Cannot access repository.", e);
        }

        String path = SourceUtil.getPath(uri);
        
        // check for query or revision
		if (uri.indexOf("?") != -1) { //$NON-NLS-1$
            try {
                // publish URL resolved event
                broker.publishEvent(new UrlResolvedEvent(this, uri, false),
                        false);
            } catch (UnknownEventException e) {
                // we can't publish the event, something went totally wrong,
                // because we registered the event during initialization
            }
            String query = SourceUtil.getQuery(uri);
            if (query.startsWith("revision=")) {
                // revision node
            	return (JCRNodeWrapperSource) createSource(session, path, query);
            } else {
                // check for query syntax (eg. 'jcr:///users?//name' interpreted
                // as 'jcr:///jcr:root/users//name')
				return (QueryResultSource) executeQuery(session, path, query);
            }
        } else {
            try {
                // publish URL resolved event
                broker.publishEvent(new UrlResolvedEvent(this, uri, true),
                        false);
            } catch (UnknownEventException e) {
                // we can't publish the event, something went totally wrong,
                // because we registered the event during initialization
            }
            // standard direct hierarchy-resolving
            return (JCRNodeWrapperSource) createSource(session, path);
        }
    }

    /**
     * @see org.apache.excalibur.source.SourceFactory#release(org.apache.excalibur.source.Source)
     */
    public void release(Source source) {
// not sure yet, committing this in a later version for separate testing
//        if (source instanceof AbstractJCRNodeSource) {
//            AbstractJCRNodeSource nodeSource = (AbstractJCRNodeSource) source;
//            nodeSource.getSession().logout();
//        }
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
        return createSource(session, path, null);
    }
    
    
    /**
     * Creates a new source given a session and a path and a revision
     * 
     * @param session the session
     * @param path the absolute path
     * @return a new source
     * @throws SourceException
     */
    public AbstractJCRNodeSource createSource(Session session, String path, String revision)
            throws SourceException {
    	String myrevision = (revision==null) ? null : revision.substring("revision=".length());
        return new JCRNodeWrapperSource(this, session, path, iClient, myrevision);
    }

    /**
     * Returns the scheme (probably <code>jcr</code>) for the URIs we handle.
     */
    public String getScheme() {
        return scheme;
    }
    
    // =========================================================================
    // Helper methods
    // =========================================================================

    public static boolean hadType(Node node, String type) throws RepositoryException {
        return node.isNodeType(JCRConstants.NT_FROZENNODE)
            && node.getProperty(JCRConstants.JCR_FROZENPRIMARYTYPE).getString().equals(type);
    }
    
    public static boolean hasOrHadType(Node node, String type) throws RepositoryException {
        return node.isNodeType(type) || hadType(node, type);
    }
    
    public static boolean isFolder(Node node) throws RepositoryException {
        return hasOrHadType(node, JCRConstants.NT_FOLDER);
    }
    
    public static boolean isFile(Node node) throws RepositoryException {
        return hasOrHadType(node, JCRConstants.NT_FILE);
    }
    
    public static boolean isElement(Node child) throws RepositoryException {
        return hasOrHadType(child, JCRConstants.XT_ELEMENT);
    }

    public static boolean isText(Node child) throws RepositoryException {
        return hasOrHadType(child, JCRConstants.XT_TEXT);
    }
    
    /**
     * Checks whether the given node is a nt:file and contains binary content,
     * aka jcr:content node with type nt:resource.
     */
    public static boolean hasBinaryContent(Node node) throws RepositoryException {
        return (isFile(node) &&
                node.hasNode(JCRConstants.JCR_CONTENT) &&
                hasOrHadType(node.getNode(JCRConstants.JCR_CONTENT), JCRConstants.NT_RESOURCE));
    }

    /**
     * Checks whether the given node is a nt:file and contains XML content,
     * aka jcr:content node with type xt:document.
     */
    public static boolean hasXMLContent(Node node) throws RepositoryException {
        return (isFile(node) &&
                node.hasNode(JCRConstants.JCR_CONTENT) &&
                hasOrHadType(node.getNode(JCRConstants.JCR_CONTENT), JCRConstants.XT_DOCUMENT));
    }

    /**
     * Checks whether the given node is a jcr:content and contains binary
     * content, aka has the type nt:resource.
     */
    public static boolean isBinaryResource(Node node) throws RepositoryException {
        return (node.getName().equals(JCRConstants.JCR_CONTENT) &&
                hasOrHadType(node, JCRConstants.NT_RESOURCE));
    }

    /**
     * Checks whether the given node is a jcr:content and contains XML
     * content, aka has the type xt:document.
     */
    public static boolean isXMLResource(Node node) throws RepositoryException {
        return (node.getName().equals(JCRConstants.JCR_CONTENT) &&
                hasOrHadType(node, JCRConstants.XT_DOCUMENT));
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
            Query query = queryManager.createQuery(statement,
                    JaxenQueryHandler.FULL_XPATH);
            QueryResult result = query.execute();

            return new QueryResultSource(this, session, result.getNodes(),
                    iClient);
        } catch (RepositoryException e) {
            throw new SourceException("Cannot execute query '" + statement
                    + "'", e); //$NON-NLS-1$
        }
    }
}
