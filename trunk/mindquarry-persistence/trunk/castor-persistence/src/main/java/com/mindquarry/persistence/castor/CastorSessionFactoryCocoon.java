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

import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.persistence.config.PersistenceConfiguration;
import com.mindquarry.persistence.castor.source.JcrSourceResolverBase;
import com.mindquarry.persistence.castor.source.JcrSourceResolverCocoon;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
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
