function createUniqueName(baseURI) {
    var uniqueIDGenerator;
	try {
		uniqueIDGenerator = cocoon.getComponent("com.mindquarry.jcr.id.JCRUniqueIDGenerator");
		return "task" + uniqueIDGenerator.getNextID(baseURI);
	} finally {
		cocoon.releaseComponent(uniqueIDGenerator);
	}
    return "task_error";
}

createUniqueName(baseURI_);