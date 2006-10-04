/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.persistence.xmlbeans.config.PersistenceConfiguration;
import com.mindquarry.persistence.xmlbeans.source.JcrSourceResolverBase;
import com.mindquarry.persistence.xmlbeans.source.JcrSourceResolverStandalone;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class XmlBeansSessionFactoryStandalone 
    extends XmlBeansSessionFactoryBase implements Configurable {

    private Configuration jcrConfiguration_;
    
    /**
     * @see com.mindquarry.common.persistence.SessionFactory#currentSession()
     */
    @Override
    public Session currentSession() {
        return new XmlBeansSession(configuration_, jcrSourceResolver_);
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
        result.enableLogging(getLogger());
        result.initialize();
        return result;
    }
    
    private JcrSourceResolverBase newJcrSourceResolverCocoon() {
        return new JcrSourceResolverStandalone(jcrConfiguration_);
    }
}
