cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");

function addJob(form) {
	//get the job's name from the sitemap
	var jobname = cocoon.parameters["jobname"];
	var model = form.getModel();
	form.showForm("forms/jobs");
	form.saveXML("xmldb:xindice-embed:///db/test/" + jobname + ".xml");
	cocoon.sendPage("")
}
