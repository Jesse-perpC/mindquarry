/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.auth;

import java.util.HashSet;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class RightSet 
        extends HashSet<Right> {

    private static final long serialVersionUID = -870194348397015787L;
    
    public final Right rightForOperation(String operation) {
        Right result = null;
        for (Right right : this) {
            if (right.operation.equals(operation)) {
                result = right;
                break;
            }
        }
        return result;
    }
}
