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
package com.mindquarry.tasks.modules;

import java.util.Map;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.cocoon.components.modules.input.AbstractInputModule;

import com.mindquarry.tasks.TasksManager;

/**
 * Creates a unique id for the jcr path given as attribute name. This id will
 * look like "task" + id, where id is a unique number below the given path. The
 * implementation calls <code>TasksManager.getUniqueTaskId()</code>.
 * 
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 *
 */
public class UniqueTaskIdInputModule extends AbstractInputModule implements Serviceable {

    private TasksManager tasksManager;
    
    public void service(ServiceManager manager) throws ServiceException {
        this.tasksManager = (TasksManager) manager.lookup(TasksManager.class.getName());
    }

    @Override
    public Object getAttribute(String name, Configuration modeConf, Map objectModel) throws ConfigurationException {
        try {
            return tasksManager.getUniqueTaskId(name);
        } catch (Exception e) {
            throw new ConfigurationException("Unique ID generation failed: " + e.getMessage(), e);
        }
    }

}
