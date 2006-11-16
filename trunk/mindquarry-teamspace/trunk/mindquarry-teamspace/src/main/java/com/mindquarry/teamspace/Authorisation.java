/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace;

/**
 * Interface for REST-style authorisation.
 *
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 */
public interface Authorisation {
    
    /**
     * tries to authorise the user with the given URI and method
     *  
     * @param userId user id
     * @param uri REST-style URI for the protected resource
     * @param method REST-style method, eg. HTTP GET, PUT, DELETE etc.
     * @returns true if the user is allowed to do that, otherwise false
     */
    boolean authorise(String userId, String uri, String method);
}
