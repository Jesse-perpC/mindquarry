/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.common.persistence;

import java.util.List;

/**
 * This interface defines the contract with an object oriented 
 * persistence implementation. The persistence interfaces aim to 
 * minimize technological dependencies. You have to use an implementation of
 * the <code>SessionFactory</code> interface to retrieve the instance
 * that is proper for your current context (e.g. a particular transaction).  
 * 
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public interface Session {
    
    void persist(Object entity);
    
    void update(Object entity);
    
    boolean delete(Object entity);
    
    List<Object> query(String queryName, Object[] params);
    
    void commit();
}
