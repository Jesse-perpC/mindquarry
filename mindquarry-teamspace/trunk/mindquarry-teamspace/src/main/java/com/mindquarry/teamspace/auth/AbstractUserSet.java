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
public class AbstractUserSet 
        extends HashSet<AbstractUser> {

    private static final long serialVersionUID = 3344089061613086165L;

    public final boolean contains(AbstractUser user) {
        boolean contains = false;
        for (AbstractUser member : this) {
            if (member instanceof User && member == user) {
                contains = true;
            }
            else if (member instanceof Group) {
                Group groupMember = (Group) member; 
                contains = groupMember.contains(user);
            }
            
            if (contains) break;
        }
        return contains;
    }
}
