cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");

var myform;
var mypage;
var pipeline = false;

function edit(form, page) {
	form.loadXML("cocoon:/" + page + ".xml.plain");
	form.showForm(page+".xml.instance");
	form.saveXML("xmldb:xindice-embed:///db/test/" + page + ".xml");
	myform = null;
	mypage = null;
	cocoon.redirectTo(page+".xml", false);
}

function editPage(form) {
	print("editing page from cocoon");
	mypage = cocoon.parameters["page"];
	myform = form;
	edit(myform, mypage);
}

function showPage(form) {
	var page = cocoon.parameters["page"];
	//set status to output only.
	form.lookupWidget("/").setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.OUTPUT);
	form.loadXML("cocoon:/" + page + ".xml.plain");
	form.showForm(page+".xml.instance");
}

function upd(event) {
	print(event.getNewValue());
}