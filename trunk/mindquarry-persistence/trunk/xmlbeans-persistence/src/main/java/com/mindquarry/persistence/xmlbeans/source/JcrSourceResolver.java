/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans.source;

import org.apache.avalon.framework.service.ServiceManager;
import org.apache.excalibur.source.ModifiableSource;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public abstract class JcrSourceResolver {

    private static final String JCR_SOURCE_PREFIX = "jcr://";

    public ModifiableSource resolveJcrSource(String path) {
        String jcrUri = JCR_SOURCE_PREFIX + path;
        return resolveJcrSourceInternal(jcrUri);
    }
    
    protected abstract ModifiableSource resolveJcrSourceInternal(String jcrPath);
    
    public static JcrSourceResolver newCocoonSourceResolver(
            ServiceManager serviceManager_) {
        
        return new JcrSourceResolverCocoon(serviceManager_);
    }
    
    public static JcrSourceResolver newStandaloneSourceResolver() {        
        return new JcrSourceResolverStandalone();
    }
}
