/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class Membership {

    public final TeamspaceRO teamspace;
    
    private final Set<UserRO> members;
    
    private final Set<UserRO> nonMembers;
    
    private final Set<UserRO> addedMembers;
    
    private final Set<UserRO> removedMembers;
    
    public Membership(TeamspaceRO teamspace, 
            Set<UserRO> members, Set<UserRO> nonMembers) {
        
        this.teamspace = teamspace;
        this.members = members;
        this.nonMembers = nonMembers;
        
        this.addedMembers = new HashSet<UserRO>();
        this.removedMembers = new HashSet<UserRO>();
    }
    
    /** returns a calculated list of current members */
    public List<UserRO> getMembers() {
        List<UserRO> result = new LinkedList<UserRO>(members);
        for (UserRO addedMember : addedMembers)
            result.add(addedMember);
        return result;
    }
    
    /** returns a calcualted list of current nonMembers */
    public List<UserRO> getNonMembers() {
        List<UserRO> result = new LinkedList<UserRO>(nonMembers);
        for (UserRO removedMember : removedMembers)
            result.add(removedMember);
        return result;
    }
    
    /** adds an user to the added members list if  
     * the user is in the nonMembers list;
     * if the user is in the removed member list it is removed from this */
    public void addMember(UserRO user) {
        if (nonMembers.contains(user)) {
            addedMembers.add(user);            
        }
        else if (removedMembers.contains(user)) {
            removedMembers.add(user);
        }
    }
    
    /** adds an user to the removed members list if 
     * the user is in the members list;
     * if the user is in the added member list it is removed from this */
    public void removeMember(UserRO user) {
        if (members.contains(user)) { 
            removedMembers.add(user);
        }
        else if (addedMembers.contains(user)) {
            addedMembers.remove(user);
        }
    }

    /**
     * Getter for addedMembers.
     *
     * @return the addedMembers
     */
    public Set<UserRO> getAddedMembers() {
        return addedMembers;
    }

    /**
     * Getter for removedMembers.
     *
     * @return the removedMembers
     */
    public Set<UserRO> getRemovedMembers() {
        return removedMembers;
    }
}

