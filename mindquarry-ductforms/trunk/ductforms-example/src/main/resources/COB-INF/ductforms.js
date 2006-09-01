cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");

function editPage(form) {
	var page = cocoon.parameters["page"];
	form.loadXML("cocoon:/" + page + ".xml.plain");

//	var xmlAdapter = form.getXML();
//	var pipeUtil = cocoon.createObject(Packages.org.apache.cocoon.components.flow.util.PipelineUtil);

	form.showForm(page+".xml.instance");
	form.saveXML("xmldb:xindice-embed:///db/test/" + page + ".xml");
	cocoon.redirectTo(page+".xml", false);
}