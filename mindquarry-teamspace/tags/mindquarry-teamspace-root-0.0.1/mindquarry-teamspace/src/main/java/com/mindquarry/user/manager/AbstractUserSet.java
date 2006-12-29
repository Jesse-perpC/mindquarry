/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
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
