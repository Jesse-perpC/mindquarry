/*
 * Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
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
