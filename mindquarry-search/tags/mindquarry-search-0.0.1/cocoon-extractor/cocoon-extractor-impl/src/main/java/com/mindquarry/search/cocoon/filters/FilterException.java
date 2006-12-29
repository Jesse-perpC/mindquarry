/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package com.mindquarry.search.cocoon.filters;

/**
 * A generic exception for the filter interface.
 * 
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 *
 */
public class FilterException extends Exception {

    private static final long serialVersionUID = -6231064073864540003L;
    
    public FilterException() {
        super();
    }

    public FilterException(String message) {
        super(message);
    }

    public FilterException(String message, Throwable t) {
        super(message, t);
    }

    public FilterException(Throwable t) {
        super(t);
    }
}
