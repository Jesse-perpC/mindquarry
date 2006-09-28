/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;
import com.mindquarry.persistence.xmlbeans.config.PersistenceConfigLoader;
import com.mindquarry.persistence.xmlbeans.config.PersistenceConfigResourceLoader;
import com.mindquarry.persistence.xmlbeans.config.PersistenceConfiguration;
import com.mindquarry.persistence.xmlbeans.source.JcrSourceResolver;

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
public class XmlBeansSessionFactoryCocoon extends AbstractLogEnabled 
    implements SessionFactory, Serviceable, Initializable {
    
    private PersistenceConfiguration configuration_;
    
    private JcrSourceResolver jcrSourceResolver_;
    private ServiceManager serviceManager_;
        
    /**
     * @see com.mindquarry.common.persistence.SessionFactory#currentSession()
     */
    public Session currentSession() {
        return new XmlBeansSession(jcrSourceResolver_, 
                configuration_.getEntityMap(), configuration_.getQueryInfoMap());
    }
    
    /**
     * @see org.apache.avalon.framework.service.Serviceable#service(org.apache.avalon.framework.service.ServiceManager)
     */
    public void service(ServiceManager serviceManager) throws ServiceException {
        jcrSourceResolver_ = newJcrSourceResolverCocoon(serviceManager);
        serviceManager_ = serviceManager;
    }
    
    private JcrSourceResolver newJcrSourceResolverCocoon(
            ServiceManager serviceManager) {
        return JcrSourceResolver.newCocoonSourceResolver(serviceManager);
    }

    /**
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() throws Exception {
        configuration_ = new PersistenceConfiguration(makeConfigLoader());
    }
    
    private PersistenceConfigLoader makeConfigLoader() {
        return new PersistenceConfigResourceLoader(serviceManager_);
    }
}
