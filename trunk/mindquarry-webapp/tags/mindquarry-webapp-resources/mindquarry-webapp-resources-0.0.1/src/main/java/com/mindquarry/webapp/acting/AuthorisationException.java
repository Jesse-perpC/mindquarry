/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package com.mindquarry.webapp.acting;

/**
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 *
 */
public class AuthorisationException extends Exception {

    private static final long serialVersionUID = -6887647765941173457L;

    public AuthorisationException(String message) {
        super(message);
    }

    public AuthorisationException(String message, Throwable t) {
        super(message, t);
    }

    public AuthorisationException(Throwable t) {
        super(t);
    }

}
