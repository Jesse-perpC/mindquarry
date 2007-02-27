/*
 * Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
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
package com.mindquarry.persistence.castor.source;

import org.apache.excalibur.source.Source;

/**
 * Simple base class which allows different source resolving implementations
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 * @author 
 * <a href="mailto:alexander.klimetschek(at)mindquarry.com">Alexander Klimetschek</a>
 */
public abstract class JcrSourceResolverBase {

    private static final String JCR_SOURCE_PREFIX = "jcr://";

    public Source resolveJcrSource(String path) {
        String jcrUri = JCR_SOURCE_PREFIX + path;
        return resolveJcrSourceInternal(jcrUri);
    }
    
    public abstract void releaseJcrSource(Source source);
    
    protected abstract Source resolveJcrSourceInternal(String jcrPath);
}
