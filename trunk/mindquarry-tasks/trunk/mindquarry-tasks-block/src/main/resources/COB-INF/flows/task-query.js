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
    var repeater = form_.lookupWidget("/parts");
    
	// build query
    var query = "jcr:///teamspaces/*/tasks?" + "/*";

    var rowCount = repeater.getSize();
    if (rowCount > 0) {
    	query = query + "[";
    	
	    for(var i = 0; i < rowCount; i++) {
	    	// check if this is the first query item, if not prepend an 'and'
	    	if(i > 0) {
	    		query = query + " and "
	    	}
	    	
	    	// evaluate query item
	    	var fieldWidget = repeater.getWidget(i, "field");
	    	var selectorWidget = repeater.getWidget(i, "selector");
	    	var valueWidget = repeater.getWidget(i, "value");
	    		    	
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
	// execute query
	var srcResolver;
	try {
		srcResolver = cocoon.getComponent(Packages.org.apache.cocoon.environment.SourceResolver.ROLE);
		source = srcResolver.resolveURI(query);
		
		// process result
		var results = source.getChildren();
		for(var i = 0; i < results.size(); i++) {
			var source = results.get(i);
			
			var bf = Packages.javax.xml.parsers.DocumentBuilderFactory.newInstance();
		    var builder = bf.newDocumentBuilder();
		    var doc = builder.parse(source.getInputStream());
		    
		    var format = new Packages.org.apache.xml.serialize.OutputFormat();
		    format.setLineWidth(65);
		    format.setIndenting(true);
		    format.setIndent(2);
		    
		    var serializer = new Packages.org.apache.xml.serialize.XMLSerializer(Packages.java.lang.System.out, format);
		    serializer.serialize(doc);
		}
	} finally {
		if (source != null) {
			srcResolver.release(source);
		}
		cocoon.releaseComponent(srcResolver);
	}
}
