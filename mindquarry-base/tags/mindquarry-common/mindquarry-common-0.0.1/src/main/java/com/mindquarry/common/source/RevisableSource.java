/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.common.source;

import org.apache.excalibur.source.Source;

/**
 * A source that exists in different versions
 * 
 * @author 
 * <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public interface RevisableSource extends Source {

    /** 
     * Get the current revision of the source
     */
    String revision();
    
    /** 
     * if the revision is head of all revision
     */
    boolean isHeadRevision();
}

