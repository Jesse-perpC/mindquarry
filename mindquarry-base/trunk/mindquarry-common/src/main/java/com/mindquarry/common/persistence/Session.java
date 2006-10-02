/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.common.persistence;

import java.util.List;

/**
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public interface Session {

    Object newEntity(Class clazz);
    
    /**
     * @deprecated use commit at the end of your session
     */    
    void persist(Object object);
    
    List query(String queryName, Object[] params);
    
    void commit();
}
