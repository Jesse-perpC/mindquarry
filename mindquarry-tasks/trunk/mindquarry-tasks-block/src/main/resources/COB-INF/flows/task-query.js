/*
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");
var form_;
var teamspaceID_;

function displayQueryForm() {
	teamspaceID_ = cocoon.parameters["teamspaceID"];
	
	form_ = new Form(cocoon.parameters["definitionURI"]);
	form_.showForm(cocoon.parameters["templatePipeline"]);
}

function displaySavedFilter() {
    var filterID = cocoon.parameters["filterID"];
    teamspaceID_ = cocoon.parameters["teamspaceID"];
    
    // retrieve saved filter
	var pfSource;
	var srcResolver;
	try {
		// resolve filter directory
		srcResolver = cocoon.getComponent(
				Packages.org.apache.cocoon.environment.SourceResolver.ROLE);
		pfSource = srcResolver.resolveURI("jcr:///teamspaces/" + teamspaceID_ + 
										"/tasks/filters/" + filterID);
		
		form_ = new Form(cocoon.parameters["definitionURI"]);
		var xmlAdapter = new Packages.org.apache.cocoon.forms.util.XMLAdapter(
								form_.lookupWidget("/queryBuilder"));
	
    	// load saved filter data
    	var parser = Packages.org.xml.sax.helpers.XMLReaderFactory.createXMLReader();
    	parser.setContentHandler(xmlAdapter);
    	parser.parse(new Packages.org.xml.sax.InputSource(pfSource.getInputStream()));
    	
    	executeQuery();
    	form_.showForm(cocoon.parameters["templatePipeline"]);
	} finally {
		if (pfSource != null) {
			srcResolver.release(pfSource);
		}
		cocoon.releaseComponent(srcResolver);
	}
}

function buildQuery() {
	var partRepeater = form_.lookupWidget("/queryBuilder/parts");
    var aggregator = form_.lookupWidget("/queryBuilder/aggregator").getValue();
    
	// build query
    var query = "jcr:///teamspaces/" + teamspaceID_ + "/tasks?" + "/*";

    var rowCount = partRepeater.getSize();
    if (rowCount > 0) {
    	query = query + "[";
    	
	    for(var i = 0; i < rowCount; i++) {
	    	// check if this is the first query item, if not prepend an 'and'
	    	if(i > 0) {
	    		query = query + " " + aggregator + " ";
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
	return(query);
}

function executeQuery() {
	// validate query widget
	form_.lookupWidget("/queryBuilder/parts").validate();
	if(!form_.lookupWidget("/queryBuilder/parts").isValid()) return;
	
	// create query
	var query = buildQuery();
        
    // clear previous results (if necessary)
	var resultRepeater = form_.lookupWidget("/results");
	resultRepeater.clear();
    
   	// execute query
   	var querySource;
	var srcResolver;
	try {
		srcResolver = cocoon.getComponent(
				Packages.org.apache.cocoon.environment.SourceResolver.ROLE);
		querySource = srcResolver.resolveURI(query);
		
		// process result
		var results = querySource.getChildren();
		for(var i = 0; i < results.size(); i++) {
			var source = results.get(i);
		    if(source.isCollection()) continue;
		    
		    // transform results to repeater format
		    var os = Packages.java.io.ByteArrayOutputStream();
		    var xmlSource = new Packages.javax.xml.transform.stream.StreamSource(
		    				source.getInputStream());
		    var xsltSource = new Packages.javax.xml.transform.stream.StreamSource(
		    				srcResolver.resolveURI("xslt/forms/queryresult2form.xsl").getInputStream());
		    
		    var tf = Packages.javax.xml.transform.TransformerFactory.newInstance();
	        var transformer = tf.newTransformer(xsltSource);
	        transformer.setParameter("taskID", 
	        	source.getName().substring(0, source.getName().indexOf(".")));
	        transformer.setParameter("teamspaceID", teamspaceID_);
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
		if (querySource != null) {
			srcResolver.release(querySource);
		}
		cocoon.releaseComponent(srcResolver);
	}
}

function saveQuery() {
	// validate query name widget
	var nameWidget = form_.lookupWidget("/queryBuilder/queryName");
	nameWidget.validate();
	
	if(!nameWidget.isValid()) return;
	
	// save query
	var fdSource;
	var srcResolver;
	try {
		// resolve filter directory
		srcResolver = cocoon.getComponent(
				Packages.org.apache.cocoon.environment.SourceResolver.ROLE);
		fdSource = srcResolver.resolveURI("jcr:///teamspaces/" + teamspaceID_ + 
										"/tasks/filters");
		
		// if filter dir not yet exist, create it
		var fNumber = 0;
		if(!fdSource.exists()) fdSource.makeCollection();
		
		// loop persistent filters and find the highest filter number
		var pFilters = fdSource.getChildren();
		for(var i = 0; i < pFilters.size(); i++) {
			var pFilter = pFilters.get(i);
			if(pFilter.getName() > fNumber) {
				fNumber = pFilter.getName(); 
			}
		}
		fNumber++;
		
		// save filter definition
		var pfTitleSource = srcResolver.resolveURI("jcr:///teamspaces/" + 
							teamspaceID_ + "/tasks/filters/" + fNumber);
		var os = pfTitleSource.getOutputStream();
		
		var xmlAdapter = new Packages.org.apache.cocoon.forms.util.XMLAdapter(
								form_.lookupWidget("/queryBuilder"));
		var tf = Packages.javax.xml.transform.TransformerFactory.newInstance();
		var transformerHandler = tf.newTransformerHandler();
        var transformer = transformerHandler.getTransformer();
        transformerHandler.setResult(new Packages.javax.xml.transform.stream.StreamResult(os));
        xmlAdapter.toSAX(transformerHandler);
        os.flush();
        os.close();
	} finally {
		if (fdSource != null) {
			srcResolver.release(fdSource);
		}
		cocoon.releaseComponent(srcResolver);
	}
}
