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
package com.mindquarry.persistence.jcr.model;

import com.mindquarry.common.persistence.PersistenceException;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class ModelException extends PersistenceException {

    /**
     * 
     */
    private static final long serialVersionUID = 7603672548879882929L;

    /**
     * @param arg0
     */
    public ModelException(String arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }
    
    /**
     * @param arg0
     */
    public ModelException(Throwable arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     * @param arg1
     */
    public ModelException(String arg0, Throwable arg1) {
        super(arg0, arg1);
        // TODO Auto-generated constructor stub
    }

}
