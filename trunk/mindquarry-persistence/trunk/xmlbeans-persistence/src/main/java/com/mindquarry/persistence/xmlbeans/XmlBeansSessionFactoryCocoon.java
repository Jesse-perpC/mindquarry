/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.persistence.xmlbeans.config.PersistenceConfigLoader;
import com.mindquarry.persistence.xmlbeans.config.PersistenceConfigResourceLoader;
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
public class XmlBeansSessionFactoryCocoon extends XmlBeansSessionFactoryBase 
    implements Serviceable {
    
    private ServiceManager serviceManager_;
        
    /**
     * @see com.mindquarry.common.persistence.SessionFactory#currentSession()
     */
    @Override
    public Session currentSession() {
        return new XmlBeansSession(configuration_, documentCreator_, 
                entityCreator_, jcrSourceResolver_);
    }
    
    /**
     * @see org.apache.avalon.framework.service.Serviceable#service(org.apache.avalon.framework.service.ServiceManager)
     */
    public void service(ServiceManager serviceManager) throws ServiceException {
        serviceManager_ = serviceManager;        
        jcrSourceResolver_ = newJcrSourceResolverCocoon(serviceManager);
    }
    
    private JcrSourceResolverBase newJcrSourceResolverCocoon(
            ServiceManager serviceManager) {
        return JcrSourceResolverBase.newCocoonSourceResolver(serviceManager);
    }    
    
    @Override
    protected PersistenceConfigLoader makeConfigLoader() {
        return new PersistenceConfigResourceLoader(serviceManager_);
    }
}
