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
            if (right.getOperation().equals(operation)) {
                result = right;
                break;
            }
        }
        return result;
    }
}
