/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.manager;

import java.util.List;

import com.mindquarry.teamspace.TeamspaceRO;
import com.mindquarry.user.UserRO;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class MembersLoading implements ListLoading<UserRO> {

    private TeamspaceRO teamspace_;
    
    private TeamspaceManager teamspaceManager_;
    
    /**
     * @param teamspace
     * @param teamspaceManager
     */
    public MembersLoading(TeamspaceRO teamspace, 
            TeamspaceManager teamspaceManager) {
        
        teamspace_ = teamspace;
        teamspaceManager_ = teamspaceManager;
    }

    /**
     * @see com.mindquarry.teamspace.manager.ListLoading#load()
     */
    public List<UserRO> load() {
        return teamspaceManager_.queryMembersForTeamspace(teamspace_);
    }

}
