/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.jcr.mapping;

import java.util.List;

import com.mindquarry.persistence.jcr.mapping.commands.CommandManager;
import com.mindquarry.persistence.jcr.mapping.model.Model;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class MappingManager {

    private CommandManager commandManager_;
    
    private MappingManager() {}
    
    public static MappingManager buildFromClazzes(List<Class<?>> clazzes) {
        
        Model model = Model.buildFromClazzes(clazzes);
        
        MappingManager result = new MappingManager();
        result.commandManager_ = new CommandManager(model);
        
        return result;
    }
    
    public Command createCommand(Object entity, Operations operation) {
        return commandManager_.createCommand(entity, operation);
    }
}
