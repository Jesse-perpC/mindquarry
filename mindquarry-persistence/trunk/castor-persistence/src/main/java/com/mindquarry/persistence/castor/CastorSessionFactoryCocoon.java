/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.castor;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.persistence.castor.config.PersistenceConfiguration;
import com.mindquarry.persistence.castor.source.JcrSourceResolverBase;
import com.mindquarry.persistence.castor.source.JcrSourceResolverCocoon;

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
public class CastorSessionFactoryCocoon extends CastorSessionFactoryBase 
    implements Serviceable {
    
    private ServiceManager serviceManager_;
        
    /**
     * @see com.mindquarry.common.persistence.SessionFactory#currentSession()
     */
    @Override
    public Session currentSession() {
        CastorSession result = new CastorSession(
                mapping_, configuration_, jcrSourceResolver_);
        result.enableLogging(getLogger());
        return result;
    }
    
    /**
     * @see org.apache.avalon.framework.service.Serviceable#service(org.apache.avalon.framework.service.ServiceManager)
     */
    public void service(ServiceManager serviceManager) throws ServiceException {
        serviceManager_ = serviceManager;        
        jcrSourceResolver_ = newJcrSourceResolverCocoon(serviceManager);

        configuration_ = new PersistenceConfiguration();
    }

    @Override
    public PersistenceConfiguration makeConfiguration() throws ServiceException {
        String name = PersistenceConfiguration.class.getName();
        return (PersistenceConfiguration) serviceManager_.lookup(name);
    }

    private JcrSourceResolverBase newJcrSourceResolverCocoon(
            ServiceManager serviceManager) {
        return new JcrSourceResolverCocoon(serviceManager_);
    }
}
