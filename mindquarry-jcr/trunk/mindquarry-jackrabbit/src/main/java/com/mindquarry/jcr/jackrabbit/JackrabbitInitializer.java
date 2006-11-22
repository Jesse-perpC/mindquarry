/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.jackrabbit;

import java.io.InputStreamReader;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;

import com.mindquarry.common.index.IndexClient;

/**
 * Component that initializes the Jackrabbit repository. This means registration
 * of node types as well as default repository structure.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JackrabbitInitializer implements Serviceable, Configurable,
        Initializable {

    public static final String MQ_JCR_XML_NAMESPACE_PREFIX = "xt"; //$NON-NLS-1$

    public static final String MQ_JCR_XML_NAMESPACE_URI = "http://mindquarry.com/ns/cnd/xt"; //$NON-NLS-1$

    public static final String MQ_JCR_XML_NODETYPES_FILE = "resource://com/mindquarry/jcr/jackrabbit/node-types.txt"; //$NON-NLS-1$

    public static final String ROLE = JackrabbitInitializer.class.getName();

    private Session session;

    private ServiceManager manager;

    private Configuration config;

    private Source nodeTypeDefSource;

    /**
     * @see org.apache.avalon.framework.configuration.Configurable#configure(org.apache.avalon.framework.configuration.Configuration)
     */
    public void configure(Configuration config) throws ConfigurationException {
        this.config = config;

        // get source for default node defintions
        try {
            SourceResolver resolver = (SourceResolver) this.manager
                    .lookup(SourceResolver.ROLE);
            nodeTypeDefSource = resolver.resolveURI(MQ_JCR_XML_NODETYPES_FILE);
        } catch (Exception e) {
            throw new ConfigurationException(
                    "Cannot find internal configuration resource "
                            + MQ_JCR_XML_NODETYPES_FILE);
        }
    }

    /**
     * @see org.apache.avalon.framework.service.Serviceable#service(org.apache.avalon.framework.service.ServiceManager)
     */
    public void service(ServiceManager manager) throws ServiceException {
        this.manager = manager;
    }

    /**
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() throws Exception {
        // load configuration
        Configuration credentials = config.getChild("credentials", false);
        String login = credentials.getAttribute("login");
        String password = credentials.getAttribute("password");

        // connect to repository
        Repository repo = (Repository) manager.lookup(Repository.class
                .getName());
        session = repo.login(new SimpleCredentials(login, password
                .toCharArray()));

        // get note type definitions
        InputStreamReader nodeTypeDefReader = new InputStreamReader(
                nodeTypeDefSource.getInputStream());

        // retrieve
        IndexClient iClient = (IndexClient) manager.lookup(IndexClient.ROLE);
        JackrabbitInitializerHelper.setupRepository(session, nodeTypeDefReader,
                "", iClient);
        session.save();
    }
}
