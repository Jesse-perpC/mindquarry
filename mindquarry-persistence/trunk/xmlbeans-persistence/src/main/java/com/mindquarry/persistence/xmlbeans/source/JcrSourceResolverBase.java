/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.xmlbeans.source;

import org.apache.excalibur.source.ModifiableTraversableSource;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public abstract class JcrSourceResolverBase {

    private static final String JCR_SOURCE_PREFIX = "jcr://";

    public ModifiableTraversableSource resolveJcrSource(String path) {
        String jcrUri = JCR_SOURCE_PREFIX + path;
        return resolveJcrSourceInternal(jcrUri);
    }
    
    protected abstract ModifiableTraversableSource resolveJcrSourceInternal(String jcrPath);
}
