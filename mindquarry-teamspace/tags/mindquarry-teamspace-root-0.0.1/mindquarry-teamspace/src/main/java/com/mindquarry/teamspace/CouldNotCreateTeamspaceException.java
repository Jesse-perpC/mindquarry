/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package com.mindquarry.teamspace;

/**
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 *
 */
public class CouldNotCreateTeamspaceException extends Exception {

    private static final long serialVersionUID = -4774743570834926591L;

    public CouldNotCreateTeamspaceException(String message) {
        super(message);
    }

    public CouldNotCreateTeamspaceException(String message, Throwable t) {
        super(message, t);
    }

    public CouldNotCreateTeamspaceException(Throwable t) {
        super(t);
    }
}
