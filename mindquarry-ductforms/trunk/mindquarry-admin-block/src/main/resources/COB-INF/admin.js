cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");

var model_;
var projectadmin_ = cocoon.getComponent("projectadmin");

function manageProjects(form) {
	model_ = form.getModel();
	populateModel();
	form.showForm("admin.instance");
	cocoon.redirectTo("", false);
}

function populateModel() {
	var list = projectadmin_.list();
	for (var i=0;i<list.size();i++) {
		model_.projects[i].name = list.get(i).name;
	}
}

function isDuplicate(name) {
	var list = projectadmin_.list();
	for (var i=0;i<model_.projects.length;i++) {
		if (model_.projects[i].name == name) {
			return true;
		}
	}
	return false;
}

function createProject(event) {
	var projectname = event.source.lookupWidget("../newprojectname").value;
	if (! isDuplicate(projectname)) {
		print("Creating project");
		projectadmin_.create(projectname);
		populateModel();
		event.source.lookupWidget("../newprojectname").value = "";
	} else {
		print("duplicate");
		return false;
	}
}