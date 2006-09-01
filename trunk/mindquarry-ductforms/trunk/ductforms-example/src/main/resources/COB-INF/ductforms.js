cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");

function editPage(form) {
	var page = cocoon.parameters["page"];
	form.showForm(page+".xml.instance");
	form.saveXML("xmldb:xindice-embed:///db/test/" + page + ".xml");
	cocoon.redirectTo(page+".xml", false);
}