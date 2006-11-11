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
		    
		    // transform results to repeater format
		    var os = Packages.java.io.ByteArrayOutputStream();
		    var xmlSource = new Packages.javax.xml.transform.stream.StreamSource(
		    				source.getInputStream());
		    var xsltSource = new Packages.javax.xml.transform.stream.StreamSource(
		    				srcResolver.resolveURI("xslt/forms/queryresult2form.xsl").getInputStream());
		    
		    var tf = Packages.javax.xml.transform.TransformerFactory.newInstance();
	        var transformer = tf.newTransformer(xsltSource);
	        transformer.setParameter("id", source.getName());
	        transformer.transform(xmlSource, 
	        	new Packages.javax.xml.transform.stream.StreamResult(os));
	        
	        // add query result to results repeater
			var row = resultRepeater.addRow();
			var xmlAdapter = new Packages.org.apache.cocoon.forms.util.XMLAdapter(row);
			
			var parser = Packages.org.xml.sax.helpers.XMLReaderFactory.createXMLReader();
			parser.setContentHandler(xmlAdapter);
			parser.parse(new Packages.org.xml.sax.InputSource(
				new Packages.java.io.ByteArrayInputStream(os.toByteArray())));
		}
	} finally {
		if (source != null) {
			srcResolver.release(source);
		}
		cocoon.releaseComponent(srcResolver);
	}
}
