/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.jackrabbit;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import javax.jcr.NamespaceException;
import javax.jcr.NamespaceRegistry;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;

import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.context.ContextException;
import org.apache.avalon.framework.context.Contextualizable;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.cocoon.components.treeprocessor.variables.VariableResolver;
import org.apache.cocoon.components.treeprocessor.variables.VariableResolverFactory;
import org.apache.cocoon.sitemap.PatternException;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.source.SourceResolver;
import org.apache.jackrabbit.core.nodetype.InvalidNodeTypeDefException;
import org.apache.jackrabbit.core.nodetype.NodeTypeDef;
import org.apache.jackrabbit.core.nodetype.NodeTypeManagerImpl;
import org.apache.jackrabbit.core.nodetype.NodeTypeRegistry;
import org.apache.jackrabbit.core.nodetype.compact.CompactNodeTypeDefReader;
import org.apache.jackrabbit.core.nodetype.compact.ParseException;

/**
 * Component that initializes the Jackrabbit repository. This means registration
 * of node types as well as default repository structure.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JackrabbitInitializer implements Serviceable, Configurable,
        Initializable, Contextualizable {

    public static final String MINDQUARRY_JCR_XML_NAMESPACE_PREFIX = "xt";

    public static final String MINDQUARRY_JCR_XML_NAMESPACE_URI = "http://mindquarry.com/ns/cnd/xt";

    public static final String MINDQUARRY_JCR_XML_NODETYPES_FILE = "resource://com/mindquarry/jcr/jackrabbit/node-types.txt";

    public static final String ROLE = JackrabbitInitializer.class.getName();

    private ServiceManager manager;

    private Configuration config;

    private Context ctx;

    private Source defSource;

    private Source additionalDefSource;

    private VariableResolver loginResolver;

    private VariableResolver passwordResolver;

    /**
     * @see org.apache.avalon.framework.configuration.Configurable#configure(org.apache.avalon.framework.configuration.Configuration)
     */
    public void configure(Configuration config) throws ConfigurationException {
        this.config = config;

        Configuration credentials = config.getChild("credentials", false);
        if (credentials != null) {
            String login = credentials.getAttribute("login");
            String password = credentials.getAttribute("password");

            try {
                loginResolver = VariableResolverFactory.getResolver(login,
                        manager);
            } catch (PatternException e) {
                throw new ConfigurationException(
                        "Invalid expression for 'login' at "
                                + credentials.getLocation(), e);
            }

            try {
                passwordResolver = VariableResolverFactory.getResolver(
                        password, manager);
            } catch (PatternException e) {
                if (loginResolver instanceof Disposable) {
                    ((Disposable) loginResolver).dispose();
                }
                loginResolver = null;
                throw new ConfigurationException(
                        "Invalid expression for 'password' at "
                                + credentials.getLocation(), e);
            }
        }

        // get node definitions (for additional definitions)
        Configuration defs = config.getChild("definitions", false);
        if (defs != null) {
            try {
                SourceResolver resolver = (SourceResolver) manager
                        .lookup(SourceResolver.ROLE);
                additionalDefSource = resolver.resolveURI(defs
                        .getAttribute("src"));
            } catch (Exception e) {
                throw new ConfigurationException(
                        "Definitions file not found at "
                                + credentials.getLocation());
            }
        }

        // get source for default node defintions
        try {
            SourceResolver resolver = (SourceResolver) this.manager
                    .lookup(SourceResolver.ROLE);
            defSource = resolver.resolveURI(MINDQUARRY_JCR_XML_NODETYPES_FILE);
        } catch (Exception e) {
            throw new ConfigurationException(
                    "Cannot find internal configuration resource "
                            + MINDQUARRY_JCR_XML_NODETYPES_FILE);
        }
    }

    /**
     * @see org.apache.avalon.framework.service.Serviceable#service(org.apache.avalon.framework.service.ServiceManager)
     */
    public void service(ServiceManager manager) throws ServiceException {
        this.manager = manager;
    }

    /**
     * @see org.apache.avalon.framework.context.Contextualizable#contextualize(org.apache.avalon.framework.context.Context)
     */
    public void contextualize(Context ctx) throws ContextException {
        this.ctx = ctx;
    }

    /**
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() throws Exception {
        // Map objectModel;
        // try {
        // objectModel = ContextHelper.getObjectModel(ctx);
        // } catch (Exception e) {
        // throw new LoginException("No objectModel to evaluate credentials",
        // e);
        // }
        Configuration credentials = config.getChild("credentials", false);
        String login = credentials.getAttribute("login");
        String password = credentials.getAttribute("password");

        // TODO us variable resolver for login & password retrieval

        // try {
        // login = loginResolver.resolve(objectModel);
        // password = passwordResolver.resolve(objectModel);
        // } catch (PatternException e) {
        // throw new LoginException("Failed to evaluate credentials", e);
        // }

        Repository repo = (Repository) manager.lookup(Repository.class
                .getName());
        Session session = repo.login(new SimpleCredentials(login, password
                .toCharArray()));

        setupNamespacesAndNodeTypes(session.getWorkspace());
        setupInitialRepositoryStructure(session);
        session.save();
    }

    private void setupNamespacesAndNodeTypes(Workspace workspace)
            throws ParseException, RepositoryException,
            InvalidNodeTypeDefException, SourceNotFoundException, IOException {
        // register xt:* namespace
        NamespaceRegistry nsRegistry = workspace.getNamespaceRegistry();
        try {
            // check if the namespace already exists
            nsRegistry.getURI(MINDQUARRY_JCR_XML_NAMESPACE_PREFIX);
        } catch (NamespaceException ne) {
            nsRegistry.registerNamespace(MINDQUARRY_JCR_XML_NAMESPACE_PREFIX,
                    MINDQUARRY_JCR_XML_NAMESPACE_URI);
        }

        // Get the NodeTypeManager from the Workspace. Note that it must be
        // casted from the generic JCR NodeTypeManager to the
        // Jackrabbit-specific implementation.
        NodeTypeManagerImpl ntmgr = (NodeTypeManagerImpl) workspace
                .getNodeTypeManager();

        // Acquire the NodeTypeRegistry
        NodeTypeRegistry ntreg = ntmgr.getNodeTypeRegistry();

        // register the default node types (xt:element etc.)
        registerNodeTypesFromTextFile(defSource, ntreg);

        if (this.additionalDefSource != null) {
            registerNodeTypesFromTextFile(this.additionalDefSource, ntreg);
        }
    }

    private void registerNodeTypesFromTextFile(Source source,
            NodeTypeRegistry ntreg) throws IOException,
            SourceNotFoundException, ParseException,
            InvalidNodeTypeDefException, RepositoryException {
        InputStreamReader reader = new InputStreamReader(source
                .getInputStream());

        // Create a CompactNodeTypeDefReader
        CompactNodeTypeDefReader cndReader = new CompactNodeTypeDefReader(
                reader, source.getURI());

        // Get the List of NodeTypeDef objects
        List ntdList = cndReader.getNodeTypeDefs();

        registerNodeTypes(ntdList, ntreg);
    }

    private void registerNodeTypes(List ntdList, NodeTypeRegistry ntreg)
            throws InvalidNodeTypeDefException, RepositoryException {
        // Loop through the prepared NodeTypeDefs
        for (Iterator i = ntdList.iterator(); i.hasNext();) {
            // Get the NodeTypeDef...
            NodeTypeDef ntd = (NodeTypeDef) i.next();

            // ...and register it
            ntreg.registerNodeType(ntd);
        }
    }

    private void setupInitialRepositoryStructure(Session session)
            throws RepositoryException {
        // add users folder
        Node root = session.getRootNode();
        root.addNode("users", "nt:folder");
        root.addNode("teamspaces", "nt:folder");
        root.addNode("tags", "nt:folder");
    }
}
