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
package com.mindquarry.persistence.jcr;

import javax.jcr.RepositoryException;
import javax.jcr.query.QueryManager;

/**
 * acts also as wrapper for javax.jcr.Session
 * and provides the following "jcr api" convenience methods
 * without the useless checked exceptions 
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public interface JcrSession {
    
    public JcrNode getRootNode();
    
    public QueryManager getQueryManager() throws RepositoryException;
    
    public Pool getPool();
}
