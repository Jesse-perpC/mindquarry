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
package com.mindquarry.user.manager;

import java.util.HashSet;

import com.mindquarry.user.AbstractUserRO;
import com.mindquarry.user.GroupRO;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class AbstractUserSet 
        extends HashSet<AbstractUserRO> {

    private static final long serialVersionUID = 3344089061613086165L;

    public final boolean contains(AbstractUserRO user) {
        boolean contains = false;
        for (AbstractUserRO member : this) {
            if (member instanceof UserEntity && member.equals(user)) {
                contains = true;
            }
            else if (member instanceof GroupRO) {
                GroupRO groupMember = (GroupRO) member; 
                contains = groupMember.contains(user);
            }
            
            if (contains) break;
        }
        return contains;
    }
}
