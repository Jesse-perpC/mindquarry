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

        JackrabbitInitializerHelper.setupRepository(session, nodeTypeDefReader,
                "");
        session.save();
    }
}
