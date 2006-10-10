/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.castor.source;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;

import com.mindquarry.common.init.InitializationException;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public class JcrSourceResolverCocoon extends JcrSourceResolverBase {

    private ServiceManager serviceManager_;
    
    public JcrSourceResolverCocoon(ServiceManager serviceManager) {
        serviceManager_ = serviceManager;
    }
    
    @Override
    protected Source resolveJcrSourceInternal(String jcrUri) {
        
        SourceResolver sourceResolver = lookupSourceResolver();
        try {
            return sourceResolver.resolveURI(jcrUri);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private SourceResolver lookupSourceResolver() {
        try {
            return (SourceResolver) serviceManager_.lookup(SourceResolver.ROLE);
        } catch (ServiceException e) {
            throw new InitializationException(
                    "lookup of SourceResolver failed");
        }
    }

}
