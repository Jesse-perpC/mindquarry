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
package com.mindquarry.persistence.jcr.cmds;

import com.mindquarry.persistence.jcr.Command;
import com.mindquarry.persistence.jcr.Operations;
import com.mindquarry.persistence.jcr.api.JcrNode;
import com.mindquarry.persistence.jcr.trafo.TransformationManager;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class CommandManager {

    private TransformationManager transformationManager_;
    
    public void initialize(TransformationManager transformationManager) {
        transformationManager_ = transformationManager;
    }
    
    public Command createCommand(Operations operation, Object object) {
        switch (operation) {
            case PERSIST : return createWriteCommand(object);
            case UPDATE : return createWriteCommand(object);
            case READ : return createReadCommand(object);
            case DELETE : return new DeleteCommand(object, transformationManager_);
            default : return null;
        }
    }
    
    private Command createWriteCommand(Object entity) {
        return new WriteCommand(entity, transformationManager_);
    }
    
    private Command createReadCommand(Object entityNode) {
        return new ReadCommand((JcrNode) entityNode, transformationManager_);
    }
}
