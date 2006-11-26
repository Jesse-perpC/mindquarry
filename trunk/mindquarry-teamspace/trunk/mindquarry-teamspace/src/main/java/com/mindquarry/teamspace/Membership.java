/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mindquarry.user.UserRO;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class Membership {

    public final TeamspaceRO teamspace;
    
    private final Map<String, UserRO> membersMap;
    
    private final Map<String, UserRO> nonMembersMap;
    
    private final Map<String, UserRO> addedMembersMap;
    
    private final Map<String, UserRO> removedMembersMap;
    
    public Membership(TeamspaceRO teamspace, 
            Set<UserRO> members, Set<UserRO> nonMembers) {
        
        this.teamspace = teamspace;
        
        membersMap = createIdUserMap(members);
        nonMembersMap = createIdUserMap(nonMembers);
        
        addedMembersMap = new HashMap<String, UserRO>(1);
        removedMembersMap = new HashMap<String, UserRO>(1);
    }
    
    private Map<String, UserRO> createIdUserMap(final Set<UserRO> users) {
        Map<String, UserRO> result = 
            new HashMap<String, UserRO>(users.size() + 1);
        
        for (UserRO user : users)
            result.put(user.getId(), user);
        
        return result;
    }
    
    /** returns a calculated list of current members */
    public List<UserRO> getMembers() {
        List<UserRO> result = new LinkedList<UserRO>(membersMap.values());
        for (UserRO addedMember : addedMembersMap.values())
            result.add(addedMember);
        return result;
    }
    
    /** returns a calcualted list of current nonMembers */
    public List<UserRO> getNonMembers() {
        List<UserRO> result = new LinkedList<UserRO>(nonMembersMap.values());
        for (UserRO removedMember : removedMembersMap.values())
            result.add(removedMember);
        return result;
    }
    
    /** adds an user to the added members list if  
     * the user is in the nonMembers list;
     * if the user is in the removed member list it is removed from this */
    public void addMember(UserRO user) {
        if (nonMembersMap.containsKey(user.getId())) {
            addedMembersMap.put(user.getId(), user);            
        }
        else if (removedMembersMap.containsKey(user.getId())) {
            removedMembersMap.put(user.getId(), user);
        }
    }
    
    /** adds an user to the added members list if  
     * the user is in the nonMembers list;
     * if the user is in the removed member list it is removed from this */
    public void addMember(String userId) {
        if (nonMembersMap.containsKey(userId)) {
            addedMembersMap.put(userId, nonMembersMap.get(userId));            
        }
        else if (removedMembersMap.containsKey(userId)) {
            removedMembersMap.remove(userId);
        }
    }
    
    /** adds an user to the removed members list if 
     * the user is in the members list;
     * if the user is in the added member list it is removed from this */
    public void removeMember(UserRO user) {
        if (membersMap.containsKey(user.getId())) { 
            removedMembersMap.put(user.getId(), user);
        }
        else if (addedMembersMap.containsKey(user.getId())) {
            addedMembersMap.remove(user.getId());
        }
    }
    
    /** adds an user to the removed members list if 
     * the user is in the members list;
     * if the user is in the added member list it is removed from this */
    public void removeMember(String userId) {
        if (membersMap.containsKey(userId)) { 
            removedMembersMap.put(userId, membersMap.get(userId));
        }
        else if (addedMembersMap.containsKey(userId)) {
            addedMembersMap.remove(userId);
        }
    }

    /**
     * Getter for addedMembers.
     *
     * @return the addedMembers
     */
    public Collection<UserRO> getAddedMembers() {
        return addedMembersMap.values();
    }

    /**
     * Getter for removedMembers.
     *
     * @return the removedMembers
     */
    public Collection<UserRO> getRemovedMembers() {
        return removedMembersMap.values();
    }
}

