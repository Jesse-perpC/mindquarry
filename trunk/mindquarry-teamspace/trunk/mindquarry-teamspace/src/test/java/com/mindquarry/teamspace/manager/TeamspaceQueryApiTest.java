package com.mindquarry.teamspace.manager;

import org.apache.avalon.framework.service.ServiceException;

import com.mindquarry.teamspace.TeamspaceAdmin;

public class TeamspaceQueryApiTest extends TeamspaceTestBase {

    public void testTeamspacesForUser() throws ServiceException {
                
        TeamspaceAdmin admin = lookupTeamspaceAdmin();
        // test with user that does not exists
        assertEquals(0, admin.teamspacesForUser("foo").size());
        
        // test with ivnalid userIds
        String[] invalidUserIds = new String[] {"", null};
        for (String invalidUserId : invalidUserIds) {
            try {
                admin.teamspacesForUser(invalidUserId);
            } catch (AssertionError e) {
                return;
            } catch (Exception e) {
                fail("expected AssertionError for user with id null, " +
                        "instead got: " + e.getMessage());
            }            
        }
        fail("expected AssertionError for user with id null.");
    }

}
