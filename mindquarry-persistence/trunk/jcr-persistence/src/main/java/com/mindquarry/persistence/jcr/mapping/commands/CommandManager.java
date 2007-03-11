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
package com.mindquarry.persistence.jcr.mapping.commands;

import com.mindquarry.persistence.jcr.mapping.Command;
import com.mindquarry.persistence.jcr.mapping.Operations;
import com.mindquarry.persistence.jcr.mapping.model.Model;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class CommandManager {

    private Model model_;
    
    public CommandManager(Model model) {
        model_ = model;
    }
    
    public Command createCommand(Object entity, Operations operation) {
        switch (operation) {
            case PERSIST : return createPersistCommand(entity);
            default : return null;
        }
    }
    
    private Command createPersistCommand(Object entity) {        
        return new PersistCommand(entity, model_.entityClass(entity));
    }
}
