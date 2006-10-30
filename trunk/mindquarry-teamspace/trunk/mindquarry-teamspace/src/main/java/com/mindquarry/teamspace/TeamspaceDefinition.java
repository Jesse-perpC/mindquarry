/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public interface TeamspaceDefinition extends TeamspaceRO {

    void setName(String value);
    
    void setDescription(String value);
    
    String getProperty(String key);
    
    void setProperty(String key, String value);
}
