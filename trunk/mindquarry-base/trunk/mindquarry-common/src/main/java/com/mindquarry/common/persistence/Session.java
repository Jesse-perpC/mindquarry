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
    
    void persist(Object entity);
    
    boolean delete(Object entity);
    
    List query(String queryName, Object[] params);
    
    void commit();
}
