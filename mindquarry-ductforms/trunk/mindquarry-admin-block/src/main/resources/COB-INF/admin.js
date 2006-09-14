cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");

function manageProjects(form) {

	var model = form.getModel();
	
	model.projects[0].name = "Foo";
	model.projects[1].name = "Bar";
	model.projects[2].name = "Hello World";
	print(model.projects);
	
	form.showForm("admin.instance");
}