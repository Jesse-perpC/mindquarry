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
package com.mindquarry.persistence.jcr.api;

import static com.mindquarry.common.lang.ReflectionUtil.invoke;

import javax.jcr.Node;
import javax.jcr.Session;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class JcrSession {

    private Session session_;
    
    public JcrSession(Session session) {
        session_ = session;
    }
    
    public JcrNode getRootNode() {
        return new JcrNode((Node) invoke("getRootNode", session_));
    }
    
    public void save() {
        invoke("save", session_);
    }
}
