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
package com.mindquarry.persistence.castor;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import com.mindquarry.persistence.config.PersistenceConfiguration;
import com.mindquarry.persistence.castor.source.JcrSourceResolverBase;
import com.mindquarry.persistence.castor.source.JcrSourceResolverStandalone;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class CastorSessionFactoryStandalone 
    extends CastorSessionFactoryBase implements Configurable {

    private Configuration jcrConfiguration_;
    
    /**
     * @see com.mindquarry.common.persistence.SessionFactory#currentSession()
     */
    @Override
    public CastorSessionStandalone currentSession() {
        CastorSessionStandalone result = new CastorSessionStandalone(
                mapping_, configuration_, jcrSourceResolver_);
        result.enableLogging(getLogger());
        return result;
    }
    
    /**
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    @Override
    public void initialize() throws Exception {
        super.initialize();
        jcrSourceResolver_ = newJcrSourceResolverCocoon();
    }

    
    /**
     * @see org.apache.avalon.framework.configuration.Configurable#configure(org.apache.avalon.framework.configuration.Configuration)
     */
    public void configure(Configuration configuration) throws ConfigurationException {
        jcrConfiguration_ = configuration;        
    }

    @Override
    public PersistenceConfiguration makeConfiguration() {
        PersistenceConfiguration result = new PersistenceConfiguration();
        result.initialize();
        return result;
    }
    
    private JcrSourceResolverBase newJcrSourceResolverCocoon() {
        return new JcrSourceResolverStandalone(jcrConfiguration_);
    }
}
