/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;
import com.mindquarry.persistence.xmlbeans.config.PersistenceConfigFileLoader;
import com.mindquarry.persistence.xmlbeans.config.PersistenceConfiguration;
import com.mindquarry.persistence.xmlbeans.source.JcrSourceResolverBase;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public class XmlBeansSessionFactoryStandalone extends AbstractLogEnabled 
    implements SessionFactory, Initializable {
    
    private PersistenceConfiguration configuration_;
    private JcrSourceResolverBase jcrSourceResolver_;

    /**
     * @see com.mindquarry.common.persistence.SessionFactory#currentSession()
     */
    public Session currentSession() {
        return null;//new XmlBeansSession(jcrSourceResolver_, configuration_);
    }
    
    /**
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() throws Exception {
        jcrSourceResolver_ = newJcrSourceResolverCocoon();
        configuration_ = new PersistenceConfiguration(makeConfigLoader());
    }
    
    private JcrSourceResolverBase newJcrSourceResolverCocoon() {
        return JcrSourceResolverBase.newStandaloneSourceResolver();
    }
    
    private PersistenceConfigFileLoader makeConfigLoader() {
        return new PersistenceConfigFileLoader();
    }
}
