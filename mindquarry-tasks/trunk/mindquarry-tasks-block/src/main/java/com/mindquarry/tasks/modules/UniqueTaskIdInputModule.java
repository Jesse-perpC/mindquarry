/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
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
