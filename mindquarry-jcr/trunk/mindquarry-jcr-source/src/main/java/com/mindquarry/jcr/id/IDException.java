/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package com.mindquarry.jcr.id;

/**
 * Exception when something during unique id generation failed.
 * 
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 *
 */
public class IDException extends Exception {

    public IDException(String message) {
        super(message);
    }

    public IDException(String message, Throwable t) {
        super(message, t);
    }

    public IDException(Throwable t) {
        super(t);
    }
}
