cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");

var model_;
var projectadmin_ = cocoon.getComponent("projectadmin");

function manageProjects(form) {
	model_ = form.getModel();
	populateModel();
	form.showForm("admin.instance");
	
	var projectname = model_.newprojectname;
	projectadmin_.create(projectname);
	
	cocoon.redirectTo("", false);
}

function populateModel() {
	var list = projectadmin_.list();
	for (var i=0;i<list.size();i++) {
		model_.projects[i].name = list.get(i).name;
	}
}

function isNoDuplicate(widget) {
	var name = widget.value;
	print(name);
	var list = projectadmin_.list();
	for (var i=0;i<model_.projects.length;i++) {
		if (model_.projects[i].name == name) {
			widget.setValidationError(
           new Packages.org.apache.cocoon.forms.
             validation.ValidationError("Duplicate project", false));
    		return false;
		}
	}
	return true;
}

function deleteProjects(event) {
	var list = projectadmin_.list();
	for (var i=0;i<list.size();i++) {
		var button = event.source.form.lookupWidget("/projects/"+i+"/delete");
		var checkbox = event.source.form.lookupWidget("/projects/"+i+"/selected");
		//if either the button was clicked or the checkbox selected
		if (event.source==button||(checkbox!=null&&checkbox.value==true)) {
			print("delete " + list.get(i).name);
			projectadmin_.remove(list.get(i).name);
		}
	}
}