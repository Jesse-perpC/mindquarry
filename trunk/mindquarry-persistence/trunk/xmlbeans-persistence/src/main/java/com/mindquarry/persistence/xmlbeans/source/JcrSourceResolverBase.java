/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans.source;

import org.apache.excalibur.source.ModifiableSource;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public abstract class JcrSourceResolverBase {

    private static final String JCR_SOURCE_PREFIX = "jcr://";

    public ModifiableSource resolveJcrSource(String path) {
        String jcrUri = JCR_SOURCE_PREFIX + path;
        return resolveJcrSourceInternal(jcrUri);
    }
    
    protected abstract ModifiableSource resolveJcrSourceInternal(String jcrPath);
}
