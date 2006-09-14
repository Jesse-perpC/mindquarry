cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");

var model_;

function manageProjects(form) {

	model_ = form.getModel();
	
	model_.projects[0].name = "Foo";
	model_.projects[1].name = "Bar";
	model_.projects[2].name = "Hello World";
	
	form.showForm("admin.instance");
	
	cocoon.redirectTo("", false);
}

function createProject(event) {
	print(event.source.lookupWidget("../newprojectname").value);
	
	model_.projects[model_.projects.length - 1].name = event.source.lookupWidget("../newprojectname").value;
	event.source.lookupWidget("../newprojectname").value = "";
}