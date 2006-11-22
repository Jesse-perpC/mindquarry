/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.common.index;

import java.util.List;


/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public interface IndexClient {
    public static final String ROLE = IndexClient.class.getName();
    
    public void index(List<String> changedPaths, List<String> deletedPaths);
}
