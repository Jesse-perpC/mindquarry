/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package com.mindquarry.common.resources;

/**
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 *
 */
public class ResourceDoesNotExistException extends RuntimeException {

    private static final long serialVersionUID = 1649853452613679527L;
    
    private String uri;

    public ResourceDoesNotExistException(String uri, String message) {
        super(message + " (URI: '" + uri + "')");
        this.uri = uri;
    }
    
    public ResourceDoesNotExistException(String uri, String message, Throwable cause) {
        super(message + " (URI: '" + uri + "')", cause);
        this.uri = uri;
    }
    
    public String getURI() {
        return this.uri;
    }
}
