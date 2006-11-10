/*
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");
var form_;

function displayQueryForm() {
	form_ = new Form(cocoon.parameters["definitionURI"]);
	form_.showForm(cocoon.parameters["templatePipeline"]);
}

function executeQuery(event) {
	// validate query widget
	form_.getWidget().validate();
	if(!form_.getWidget().isValid()) return;
	
	// retrieve model
	var model = form_.getModel();
    var partRepeater = form_.lookupWidget("/parts");
    
	// build query
    var query = "jcr:///teamspaces/*/tasks?" + "/*";

    var rowCount = partRepeater.getSize();
    if (rowCount > 0) {
    	query = query + "[";
    	
	    for(var i = 0; i < rowCount; i++) {
	    	// check if this is the first query item, if not prepend an 'and'
	    	if(i > 0) {
	    		query = query + " and "
	    	}
	    	// evaluate query item
	    	var fieldWidget = partRepeater.getWidget(i, "field");
	    	var selectorWidget = partRepeater.getWidget(i, "selector");
	    	var valueWidget = partRepeater.getWidget(i, "value");
	    		    	
			if (selectorWidget.getValue() == "equals") {
				query = query + ".//" + fieldWidget.getValue().toLowerCase()
					 + "='" + valueWidget.getValue() + "'";
			} else if (selectorWidget.getValue() == "contains") {
				query = query + "contains(.//" + 
					fieldWidget.getValue().toLowerCase() + ",'" + 
					valueWidget.getValue() + "')";
			}
	    }
	    query = query + "]";
	}
	// clear previous results (if necessary)
	var resultRepeater = form_.lookupWidget("/results");
	resultRepeater.clear();
			
	// execute query
	var srcResolver;
	try {
		srcResolver = cocoon.getComponent(Packages.org.apache.cocoon.environment.SourceResolver.ROLE);
		source = srcResolver.resolveURI(query);
		
		// process result
		var results = source.getChildren();
		for(var i = 0; i < results.size(); i++) {
			var source = results.get(i);
		    
		    var os = Packages.java.io.ByteArrayOutputStream();
		    Packages.org.apache.commons.io.IOUtils.copy(source.getInputStream(), os);
		    
		    // display results
			var resultItem = resultRepeater.addRow().getChild("resultItem");
			resultItem.setValue(new Packages.java.lang.String(os.toByteArray()));
		}
		
		var xmlAdapter = new Packages.org.apache.cocoon.forms.util.XMLAdapter(resultRepeater);
		var tf = Packages.javax.xml.transform.TransformerFactory.newInstance();
		
		var transformerHandler = tf.newTransformerHandler();
        var transformer = transformerHandler.getTransformer();
        transformer.setOutputProperty(Packages.javax.xml.transform.OutputKeys.INDENT, "true");
        transformer.setOutputProperty(Packages.javax.xml.transform.OutputKeys.METHOD, "xml");
        var os2 = Packages.java.io.ByteArrayOutputStream();
        transformerHandler.setResult(new Packages.javax.xml.transform.stream.StreamResult(os2));
        xmlAdapter.toSAX(transformerHandler);
        print(os2);
	} finally {
		if (source != null) {
			srcResolver.release(source);
		}
		cocoon.releaseComponent(srcResolver);
	}
}
