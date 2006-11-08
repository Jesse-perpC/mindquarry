/*
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");

var form_;

function displayQueryForm() {
	var form = new Form(cocoon.parameters["definitionURI"]);
	form_ = form;
    //form.loadXML(cocoon.parameters["formData"]);
	form.showForm(cocoon.parameters["templatePipeline"]);
}

function executeQuery(event) {
	var model = form_.getModel();
    print("test best rest");
    var repeater = form_.lookupWidget("/parts");
    var output = form_.lookupWidget("/xpath");
    var xpath = "//task";
    print(repeater);
    var rowCount = repeater.getSize();
    for(var i = 0; i < rowCount; i++) {
    	print("i:" + i + " (" + rowCount + ")");
    	var widget = repeater.getWidget(i, "field");
    	xpath = xpath + "[" + widget.getValue() + "]";
    }
    output.setValue(xpath);
      //model.teamspaceId, model.name, model.description, user);
}
