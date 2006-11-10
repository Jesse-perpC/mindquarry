/*
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
var source;
var srcResolver;
var is;

try {
	srcResolver = cocoon.getComponent(Packages.org.apache.cocoon.environment.SourceResolver.ROLE);
	source = srcResolver.resolveURI("/teamspaces/mindquarry/metadata.xml");
	is = source.getInputStream();
} finally {
	if (source != null) {
		srcResolver.release(source);
	}
	cocoon.releaseComponent(srcResolver);
	
	if (is != null) {
		try {
			is.close();
		} catch (error) {
			cocoon.log.error("Could not flush/close outputstream: " + error);
		}
	}
}

cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");
var form_;

function displayQueryForm() {
	form_ = new Form(cocoon.parameters["definitionURI"]);
	form_.showForm(cocoon.parameters["templatePipeline"]);
}

function executeQuery(event) {
	var model = form_.getModel();
    
    var repeater = form_.lookupWidget("/parts");
    var output = form_.lookupWidget("/xpath");
    var xpath = "//task[";
    
    var rowCount = repeater.getSize();
    for(var i = 0; i < rowCount; i++) {
    	// check if this is the first query item, if not prepend an 'and'
    	if(i > 0) {
    		xpath = xpath + " and "
    	}
    	
    	// evaluate query item
    	var fieldWidget = repeater.getWidget(i, "field");
    	var valueWidget = repeater.getWidget(i, "value");
    	if(fieldWidget.getValue() != null) {
    		xpath = xpath + "//" + fieldWidget.getValue().toLowerCase() + "='" +
    			valueWidget.getValue() + "'";
    	}
    }
    xpath = xpath + "]";
    output.setValue(xpath);
}
