cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");

function editPage(form) {
	var page = cocoon.parameters["page"];
	form.loadXML("cocoon:/" + page + ".xml.plain");
	form.showForm(page+".xml.instance");
	form.saveXML("xmldb:xindice-embed:///db/test/" + page + ".xml");
	cocoon.redirectTo(page+".xml", false);
}

function showPage(form) {
	var page = cocoon.parameters["page"];
	form.lookupWidget("/").setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.OUTPUT);
	form.loadXML("cocoon:/" + page + ".xml.plain");
	form.showForm(page+".xml.instance");
}