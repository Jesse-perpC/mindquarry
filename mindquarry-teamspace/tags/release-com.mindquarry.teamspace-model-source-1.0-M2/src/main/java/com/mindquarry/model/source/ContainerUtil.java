/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.model.source;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class ContainerUtil {

    private ServiceManager serviceManager_;
    
    ContainerUtil(ServiceManager serviceManager) {
        serviceManager_ = serviceManager;
    }
    
    Object lookupComponent(String name) {
        try {
            return serviceManager_.lookup(name);
        }
        catch (ServiceException e) {
            throw new ModelSourceException("could not resolve component " +
                    "for name: " + name, e);
        }
    }
}
