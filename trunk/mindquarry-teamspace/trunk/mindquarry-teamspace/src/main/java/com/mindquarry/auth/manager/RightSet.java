/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.auth.manager;

import java.util.HashSet;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public final class RightSet 
        extends HashSet<RightEntity> {

    private static final long serialVersionUID = -870194348397015787L;
    
    RightEntity rightForOperation(String operation) {
        RightEntity result = null;
        for (RightEntity right : this) {
            if (right.operation.equals(operation)) {
                result = right;
                break;
            }
        }
        return result;
    }
}
