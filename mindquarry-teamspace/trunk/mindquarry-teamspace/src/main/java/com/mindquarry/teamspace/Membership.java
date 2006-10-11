/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace;

import java.util.LinkedList;
import java.util.List;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class Membership {

    public final TeamspaceRO teamspace;
    
    /** unmodifiable view of current members */
    public final List<UserRO> members;
    
    /** unmodifiable view of current non-members */
    public final List<UserRO> nonMembers;
    
    /** a list of all users that should participate the teamspace*/
    public final List<UserRO> newMembers;
    
    public Membership(TeamspaceRO teamspace, 
            List<UserRO> members, List<UserRO> nonMembers) {
        
        this.teamspace = teamspace;
        this.members = members;
        this.nonMembers = nonMembers;
        
        this.newMembers = new LinkedList<UserRO>(members);
    }
}

