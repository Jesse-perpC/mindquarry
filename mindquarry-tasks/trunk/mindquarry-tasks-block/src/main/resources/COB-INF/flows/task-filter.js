/*
 * Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 */
cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");
var form_;
var filterID_;
var teamspaceID_;

function displayNewFilterForm() {
	filterID_ = null;
	teamspaceID_ = cocoon.parameters["teamspaceID"];
	form_ = new Form(cocoon.parameters["definitionURI"]);
	
	// deactivate unecessary widgets
    var WidgetState = Packages.org.apache.cocoon.forms.formmodel.WidgetState;
    var deleteFilterWidgets = form_.lookupWidget("/deleteFilterWidgets");
    deleteFilterWidgets.setState(WidgetState.INVISIBLE);
    
	var partRepeater = form_.lookupWidget("/filterBuilderWidgets/rules");
	partRepeater.addRow();
	
	form_.showForm(cocoon.parameters["templatePipeline"]);
	finishForm();
}

function displaySavedFilterForm() {
    filterID_ = cocoon.parameters["filterID"];
    teamspaceID_ = cocoon.parameters["teamspaceID"];
    
    // retrieve saved filter
	var pfSource;
	var srcResolver;
	try {
		// resolve filter directory
		srcResolver = cocoon.getComponent(
				Packages.org.apache.cocoon.environment.SourceResolver.ROLE);
		pfSource = srcResolver.resolveURI("jcr:///teamspaces/" + teamspaceID_ + 
										"/tasks/filters/" + filterID_);
		
		form_ = new Form(cocoon.parameters["definitionURI"]);
		var xmlAdapter = new Packages.org.apache.cocoon.forms.util.XMLAdapter(
								form_.lookupWidget("/filterBuilderWidgets"));
	
    	// load saved filter data
    	var parser = Packages.org.xml.sax.helpers.XMLReaderFactory.createXMLReader();
    	parser.setContentHandler(xmlAdapter);
    	parser.parse(new Packages.org.xml.sax.InputSource(pfSource.getInputStream()));
    	
    	executeFilter();
    	
    	// set filter title
    	var filterTitleField = form_.lookupWidget("/filterBuilderWidgets/title");
    	var filterNameField = form_.lookupWidget("/filterName");
    	filterNameField.setValue(filterTitleField.getValue());
    	
    	form_.showForm(cocoon.parameters["templatePipeline"]);
	} finally {
		if (pfSource != null) {
			srcResolver.release(pfSource);
		}
		cocoon.releaseComponent(srcResolver);
	}
	finishForm(filterID_);
}

function finishForm(fID) {
    var saveFilterAction = form_.lookupWidget("/saveFilterWidgets/saveFilterAction");
	var deleteFilterAction = form_.lookupWidget("/deleteFilterWidgets/deleteFilterAction");
	
	// eval submit action
	var submitWidget = form_.getWidget().getSubmitWidget();
	if (submitWidget.getId().equals(saveFilterAction.getId())) {
	    saveFilter();
	} else if (submitWidget.getId().equals(deleteFilterAction.getId())) {
	    deleteFilter(fID);
	}
}

function buildFilter() {
	var partRepeater = form_.lookupWidget("/filterBuilderWidgets/rules");
    var aggregator = form_.lookupWidget("/filterBuilderWidgets/aggregator").getValue();
    
	// build filter (only files below tasks/ that contain task
    var filter = "jcr:///teamspaces/" + teamspaceID_ + "/tasks?" + "/*[contains(local-name(.),'task')]";

    var rowCount = partRepeater.getSize();
    if (rowCount > 0) {
    	filter = filter + "[";
    	
	    for(var i = 0; i < rowCount; i++) {
	    	// check if this is the first filter item, if not prepend an 'and'
	    	if(i > 0) {
	    		filter = filter + " " + aggregator + " ";
	    	}
	    	// evaluate filter item
	    	var fieldWidget = partRepeater.getWidget(i, "field");
	    	var selectorWidget = partRepeater.getWidget(i, "selector");
	    	var valueWidget = partRepeater.getWidget(i, "value");
	    		    	
			if (selectorWidget.getValue() == "equals") {
				filter = filter + ".//" + fieldWidget.getValue().toLowerCase()
					 + "='" + valueWidget.getValue() + "'";
			} else if (selectorWidget.getValue() == "contains") {
				filter = filter + "contains(.//" + 
					fieldWidget.getValue().toLowerCase() + ",'" + 
					valueWidget.getValue() + "')";
			}
	    }
	    filter = filter + "]";
	}
	return(filter);
}

function executeFilter() {
    // validate filter widget
	form_.lookupWidget("/filterBuilderWidgets/rules").validate();
	if(!form_.lookupWidget("/filterBuilderWidgets/rules").isValid()) return;
	
	// create filter
	var filter = buildFilter();
        
    // clear previous results (if necessary)
	var resultRepeater = form_.lookupWidget("/results");
	resultRepeater.clear();
    
   	// execute filter
   	var filterSource;
	var srcResolver;
	try {
		srcResolver = cocoon.getComponent(
				Packages.org.apache.cocoon.environment.SourceResolver.ROLE);
		filterSource = srcResolver.resolveURI(filter);
		
        //print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        	
    	// process result
		var results = filterSource.getChildren();
		for(var i = 0; i < results.size(); i++) {
			var source = results.get(i);

		    if(source.isCollection()) continue;
		    
		    // transform results to repeater format
		    var os = Packages.java.io.ByteArrayOutputStream();
		    var xmlSource = new Packages.javax.xml.transform.stream.StreamSource(
		    				source.getInputStream());
		    var xsltSource = new Packages.javax.xml.transform.stream.StreamSource(
		    				srcResolver.resolveURI("xslt/forms/filterresult2form.xsl").getInputStream());
		    
		    var tf = Packages.javax.xml.transform.TransformerFactory.newInstance();
	        var transformer = tf.newTransformer(xsltSource);
	        transformer.setParameter("taskID", 
	        	source.getName().substring(0, source.getName().indexOf(".")));
	        transformer.setParameter("teamspaceID", teamspaceID_);
	        transformer.transform(xmlSource,
	        	new Packages.javax.xml.transform.stream.StreamResult( os ));
	        
	        //print(new Packages.java.lang.String(os.toByteArray()));
	        
	        // add filter result to results repeater
			var row = resultRepeater.addRow();
			var xmlAdapter = new Packages.org.apache.cocoon.forms.util.XMLAdapter(row);
			
			var parser = Packages.org.xml.sax.helpers.XMLReaderFactory.createXMLReader();
			parser.setContentHandler(xmlAdapter);
			parser.parse(new Packages.org.xml.sax.InputSource(
				new Packages.java.io.ByteArrayInputStream(os.toByteArray())));
		}
	} finally {
		if (filterSource != null) {
			srcResolver.release(filterSource);
		}
		cocoon.releaseComponent(srcResolver);
	}
}

function saveFilter() {
	// validate filter name widget
	var nameWidget = form_.lookupWidget("/filterBuilderWidgets/title");
	nameWidget.validate();
	
	if(!nameWidget.isValid()) return;
	
	// save filter
	var fdSource;
	var srcResolver;
	try {
	    var baseURI = "jcr:///teamspaces/" + teamspaceID_ +	"/tasks/filters";
		// resolve filter directory
		srcResolver = cocoon.getComponent(
				Packages.org.apache.cocoon.environment.SourceResolver.ROLE);
		fdSource = srcResolver.resolveURI(baseURI);
										
		// if filter dir not yet exist, create it
		if(!fdSource.exists()) fdSource.makeCollection();
		
		if (filterID_ == null) {
	        var tasksManager;
	    	try {
	    		tasksManager = cocoon.getComponent("com.mindquarry.tasks.TasksManager");
	    		filterID_ = tasksManager.getUniqueId(baseURI);
	    	} finally {
	    		cocoon.releaseComponent(tasksManager);
	    	}
		}
		
		// save filter definition
		var pfTitleSource = srcResolver.resolveURI(baseURI + "/" + filterID_);
		var os = pfTitleSource.getOutputStream();
		
		var xmlAdapter = new Packages.org.apache.cocoon.forms.util.XMLAdapter(
								form_.lookupWidget("/filterBuilderWidgets"));
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
	cocoon.redirectTo("cocoon:/redirectTo/" + filterID_);
}

function deleteFilter(fID) {
    var fSource;
    var srcResolver;
    try {
		// resolve filter directory
		srcResolver = cocoon.getComponent(
				Packages.org.apache.cocoon.environment.SourceResolver.ROLE);
		
		// delete filter definition
		var fSource = srcResolver.resolveURI("jcr:///teamspaces/" + 
							teamspaceID_ + "/tasks/filters/" + fID);
		fSource['delete']();
	} finally {
		if (fSource != null) {
			srcResolver.release(fSource);
		}
		cocoon.releaseComponent(srcResolver);
	}
    cocoon.redirectTo("cocoon:/redirectTo/" + "..");
}
