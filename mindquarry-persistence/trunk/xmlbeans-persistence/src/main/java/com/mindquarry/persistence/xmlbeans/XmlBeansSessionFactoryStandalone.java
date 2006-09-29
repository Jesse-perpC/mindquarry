/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.persistence.xmlbeans.config.PersistenceConfigFileLoader;
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
public class XmlBeansSessionFactoryStandalone 
    extends XmlBeansSessionFactoryBase {

    /**
     * @see com.mindquarry.common.persistence.SessionFactory#currentSession()
     */
    @Override
    public Session currentSession() {
        return new XmlBeansSession(configuration_, documentCreator_, 
                entityCreator_, jcrSourceResolver_);
    }
    
    /**
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    @Override
    public void initialize() throws Exception {
        super.initialize();
        jcrSourceResolver_ = newJcrSourceResolverCocoon();
    }
    
    private JcrSourceResolverBase newJcrSourceResolverCocoon() {
        return JcrSourceResolverBase.newStandaloneSourceResolver();
    }
    
    /**
     * @see com.mindquarry.persistence.xmlbeans.XmlBeansSessionFactoryBase#makeConfigLoader()
     */
    @Override
    protected PersistenceConfigFileLoader makeConfigLoader() {
        return new PersistenceConfigFileLoader();
    }
}
