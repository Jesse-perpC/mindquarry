function createUniqueName(baseURI) {
    var tasksManager;
	try {
		tasksManager = cocoon.getComponent("com.mindquarry.tasks.TasksManager");
		return tasksManager.getUniqueTaskId(baseURI);
	} finally {
		cocoon.releaseComponent(tasksManager);
	}
    return "task_error";
}

createUniqueName(baseURI_);