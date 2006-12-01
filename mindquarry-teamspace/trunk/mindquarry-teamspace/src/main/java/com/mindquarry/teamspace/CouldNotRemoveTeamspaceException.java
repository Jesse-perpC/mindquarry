/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package com.mindquarry.teamspace;

/**
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 *
 */
public class CouldNotRemoveTeamspaceException extends Exception {

    private static final long serialVersionUID = -5521634518836088322L;

    public CouldNotRemoveTeamspaceException(String message) {
        super(message);
    }

    public CouldNotRemoveTeamspaceException(String message, Throwable t) {
        super(message, t);
    }

    public CouldNotRemoveTeamspaceException(Throwable t) {
        super(t);
    }
}
