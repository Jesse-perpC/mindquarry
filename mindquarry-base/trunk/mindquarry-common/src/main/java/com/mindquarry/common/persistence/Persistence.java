/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.common.persistence;

import java.util.List;

/**
 *
 * @author <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public interface Persistence {

    Object newInstance(Class clazz);
    
    void persist(Object object);
    
    List query(String queryName, Object[] params);
}
