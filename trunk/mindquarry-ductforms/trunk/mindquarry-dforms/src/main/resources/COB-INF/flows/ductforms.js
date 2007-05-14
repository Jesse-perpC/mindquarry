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
cocoon.load("servlet:resources:/flows/util.js"); // only reloaded on restart!

/////////////////////////////////////////////////////////
// global variables (in a object to avoid collisions)
/////////////////////////////////////////////////////////

var DFORM = {
    CLEAN_MODEL_XSL	: "xsl/model/saveclean.xsl",
    form			: null,
    baseURI			: null,
    documentID		: null,
    suffix			: null,
    rootElement		: null,
    revision		: null,
    getFilename		: function() { return this.documentID + this.suffix; },
    getFullPath		: function() { return this.baseURI + this.getFilename(); }
}

/////////////////////////////////////////////////////////
// entry point method
/////////////////////////////////////////////////////////

// called from sitemap via handleForm()
function showDForm(form) {
    // catch all parameters and store them globally
    DFORM.baseURI = cocoon.parameters["baseURI"];
    DFORM.documentID = cocoon.parameters["documentID"];
    DFORM.suffix = ".xml";
    DFORM.rootElement = cocoon.parameters["rootElement"];
    //revision of the source
    DFORM.revision = cocoon.parameters["revision"];
    if (DFORM.revision!="") {
      DFORM.revision = "?revision=" + DFORM.revision;
    }
    var isEditStart = (DFORM.documentID == 'new' || !resourceExists(DFORM.getFullPath()));
    
	// save form and uri for actions
	DFORM.form = form;

	// load file from internal pipeline
	form.loadXML("cocoon:/" + DFORM.getFilename() + ".plain" + DFORM.revision);

    // feature for wiki: when creating a new page via the URL == documentID
    // give the title that documentID as default value
    if (isEditStart && DFORM.documentID != "new") { 
    	var titleWidget = DFORM.form.lookupWidget("/title");
    	if (titleWidget && titleWidget.getValue() == null) {
    	    titleWidget.setValue(DFORM.documentID);
    	}
    }
	
	// set initial state to output
	setWidgetStates(form, isEditStart);

    // the continuations will be inside this method again and again
	form.showForm(DFORM.getFilename() + ".instance");
}

/////////////////////////////////////////////////////////
// action widgets handler
/////////////////////////////////////////////////////////

// called by auto-reload or AJAX or on loadXML binding for mutivalue fields
function fieldsChanged(event) {
	if (DFORM.form) {
		setWidgetStates(DFORM.form, true);
	}
}

// enter partial edit mode or "expand" partial edit mode
function activate(event) {
	// do not allow editing on a revision
	if (DFORM.form && cocoon.request.activate && !DFORM.revision) {
		var selectedWidget = DFORM.form.lookupWidget("/" + cocoon.request.activate.substring(9));
		if (selectedWidget) {
		    // activate all descendant widgets
    		var descendants = getSubWidgets(selectedWidget);
    		
			// descendants contains the widget itself
			for (var j=0;j<descendants.length;j++) {
				descendants[j].setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
			}

            // activate all related widgets
    		var related = getRelatedWidgets(DFORM.form, selectedWidget);
		
			for (var j=0;j<related.length;j++) {
				var relwidget = DFORM.form.lookupWidget("/" + related[j]);
				relwidget.setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
			}
			
			// only show the delete button in full output mode, but not in partial edit nor full edit mode
			DFORM.form.lookupWidget("/ductforms_delete").setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE);
			
			// as soon something can be edited, you can save or cancel it
			DFORM.form.lookupWidget("/ductforms_save").setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
			DFORM.form.lookupWidget("/ductforms_cancel").setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
		}
	}
}

// enter mode where all fields are active and the ductforms field chooser is visible
function editAll(event) {
	// do not allow editing on a revision
	if (DFORM.form && !DFORM.revision) {
        // make everything editable
		setWidgetStates(DFORM.form, true);
	}
}

function save(event) {
	// do not allow save on a revision
	if (DFORM.form && !DFORM.revision) {
	    var newDocument = false;
	    if (DFORM.documentID == "new") {
	        // this does the trick: it will load the uniqueName.js from the
	        // block that calls us via servlet:super: and provides its own version
	        // of uniqueName.js. the implementation of uniqueName.js must return
	        // a new document id.
	        DFORM.documentID = evalJavaScriptSource("servlet:/uniqueName.js");
	        newDocument = true;
	    }
	    
		// the form includes all possible fields, but only some of them are
		// actually used. the best solution would be to strip out forms
		DFORM.form.saveXML(DFORM.getFullPath(), DFORM.CLEAN_MODEL_XSL);
		
		if (newDocument) {

            // stop form processing before the redirect, otherwise the redirect
            // would not work properly as he also runs the form output pipeline
		    DFORM.form.getWidget().endProcessing(false);
		    // go to the real url of the newly created document (the pipeline
		    // will create some special redirect xml that is either interpreted
		    // by the browser-update handler client-side javascript to do the
		    // redirect from within the browser or (if Javascript is disabled
		    // == this is no ajax request) the RedirectTransformer will do a
		    // HTTP redirect based on the xml content) 
		    
		    
		    //redirect with 
		    cocoon.redirectTo("cocoon:/redirectTo/" + DFORM.documentID);
		    
		} else {
		
		    // existing documents only switch to view mode after a save
		    setWidgetStates(DFORM.form, false);
		}
	}
}

function cancel(event) {
	if (DFORM.form) {
	    if (DFORM.documentID == "new") {
	        // user does not want to create a new document
	        
    	    // stop any further form processing to be able to do the redirect
    		DFORM.form.getWidget().endProcessing(false);
    		
    	    // go back to the task list
    	    cocoon.redirectTo("cocoon:/redirectTo/.");
    	} else {
    	    // user cancels changes of an existing document
    	    
    	    // stop any further form processing to be able to do the redirect
    		DFORM.form.getWidget().endProcessing(false);
    		
    		
    		if (cocoon.request.formbaselink) {
				// add current formbaselink before redirect (true for "absoluteLink")
				cocoon.redirectTo("cocoon:/redirectTo/" + cocoon.request.formbaselink);
    		} else {
				// reload the form in view mode (new continuation)
				cocoon.redirectTo("cocoon:/redirectTo/" + DFORM.documentID);   			
    		}
    	}
	}
}

/* delete is a protected keyword in javascript... */
function deleteIt(event) {
	if (DFORM.form) {
		var source = 0;
		try {
			source = resolveSource(DFORM.getFullPath());
			
			// to avoid the 'delete' keyword, we use the alternative array-style
			// notation for object members to call the delete() method
			source["delete"]();
		} finally {
			releaseSource(source);
		}

	    // stop any further form processing to be able to do the redirect
		DFORM.form.getWidget().endProcessing(false);
		
	    // reload the form in view mode (new continuation)
	    cocoon.redirectTo("cocoon:/redirectTo/.");
	}
}

function keepAlive(event) {
	// periodically called to keep the continuation alive
	// so do nothing here...
}

/////////////////////////////////////////////////////////
// form handling helper
/////////////////////////////////////////////////////////

function getRelatedWidgets(form, widget) {
	var attribute = widget.getAttribute("related");
	if (attribute!=null) {
		return attribute.split(",");
	} else {
		return new Array();
	}		
}

// returns all widgets below 'widget', including widget itself. can handle
// standard containerwidget children as well as repeater rows
function getSubWidgets(widget) {
    var result = new Array();
	if (widget instanceof Packages.org.apache.cocoon.forms.formmodel.Repeater) {
        for (var i=0; i<widget.getSize(); i++) {
            var subwidgets = getSubWidgets(widget.getRow(i));
            for (var j=0; j<subwidgets.length; j++) {
                result.push(subwidgets[j]);
            }
        }
	} else if (widget instanceof Packages.org.apache.cocoon.forms.formmodel.ContainerWidget) {
	    var children = widget.getChildren();
	    while (children.hasNext()) {
	        var subwidgets = getSubWidgets(children.next());
            for (var j=0; j<subwidgets.length; j++) {
                result.push(subwidgets[j]);
            }
	    }
	}
	
	result.push(widget);
	return result;
}

function setWidgetStates(form, isEdit) {
	// hide all widgets first
	var allWidgets = form.lookupWidget("/").getChildren();
	//create a hashtable before actually setting the state
	var widgetMap = new java.util.Hashtable();
	for (; allWidgets.hasNext(); ) {
		var widget = allWidgets.next();
		widgetMap.put(widget, Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE);
	}
	
	var ductformsWidget = form.lookupWidget("/ductforms");
	
	// show only selected fields
	var ductfields = ductformsWidget.getValue();
	for (var i=0; i<ductfields.length; i++) {
		var widget = form.lookupWidget("/" + ductfields[i]);		
		var descendants = getSubWidgets(widget);
		var related = getRelatedWidgets(form, widget);
		
		if (isEdit) {
		    // activate all descendant widgets
			// (note: descendants contains the widget itself)
			for (var j=0;j<descendants.length;j++) {
				widgetMap.put(descendants[j], Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
			}

            // activate all related widgets
			for (var j=0;j<related.length;j++) {
				var relwidget = form.lookupWidget("/" + related[j]);
				widgetMap.put(relwidget, Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
			}
		} else {
		    // de-activate all descendant widgets (actions -> invisible, others -> output)
			// (note: descendants contains the widget itself)
			for (var j=0;j<descendants.length;j++) {
				if (descendants[j] instanceof Packages.org.apache.cocoon.forms.formmodel.Action) {
    				widgetMap.put(descendants[j], Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE);
				} else {
    				widgetMap.put(descendants[j], Packages.org.apache.cocoon.forms.formmodel.WidgetState.OUTPUT);
				}
			}

            // de-activate all related widgets (actions -> invisible, others -> output)
			for (var j=0;j<related.length;j++) {
				var relwidget = form.lookupWidget("/" + related[j]);
				if (relwidget instanceof Packages.org.apache.cocoon.forms.formmodel.Action) {
					widgetMap.put(relwidget, Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE);
				} else {
					widgetMap.put(relwidget, Packages.org.apache.cocoon.forms.formmodel.WidgetState.OUTPUT);
				}
			}
		}
	}
	
	// Note: all widgets are set to INVISIBLE state in the hashmap already

	if (isEdit) {
		// show the field selector in edit mode
		widgetMap.put(ductformsWidget, Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
		
		widgetMap.put(form.lookupWidget("/ductforms_save"), Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
		widgetMap.put(form.lookupWidget("/ductforms_cancel"), Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
	} else {
		// show the edit all button in view mode
		widgetMap.put(form.lookupWidget("/ductforms_editall"), Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
		
		// delete button only in view mode
		widgetMap.put(form.lookupWidget("/ductforms_delete"), Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
	}

    // the activate and keepAlive actions are always active (but not visible)
	widgetMap.put(form.lookupWidget("/ductforms_activate"), Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
	widgetMap.put(form.lookupWidget("/ductforms_keepalive"), Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
	
	if (DFORM.revision) {
		// hide edit/save etc. buttons when looking at an older revision
		widgetMap.put(form.lookupWidget("/ductforms_save"), Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE);
		widgetMap.put(form.lookupWidget("/ductforms_cancel"), Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE);
		widgetMap.put(form.lookupWidget("/ductforms_editall"), Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE);
		//widgetMap.put(form.lookupWidget("/ductforms_activate"), Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE);
		widgetMap.put(form.lookupWidget("/ductforms_keepalive"), Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE);
	}
	
	//cycle through the widget map and set the states accordingly
	var widgetList = widgetMap.keySet().toArray();
	for each (var widget in widgetList) {
		var state = widgetMap.get(widget);
		if (widget.getState()!=state) {
			widget.setState(state);
		}
	}
}

/////////////////////////////////////////////////////////
// overwriting standard Form.js functions
/////////////////////////////////////////////////////////

// ok, I admit Javascript seriously rocks. I am overloading this function
// with another parameter to support transformations of the xml document
// before actually saving
Form.prototype.saveXML = function(uri, xsluri) {
    var source = null;
    var resolver = null;
    var outputStream = null;
    var xslsource = null;
    try {
        resolver = cocoon.getComponent(Packages.org.apache.cocoon.environment.SourceResolver.ROLE);
        source = resolver.resolveURI(uri);
        xslsource = new Packages.javax.xml.transform.stream.StreamSource(resolver.resolveURI(xsluri).getInputStream());

        var tf = Packages.javax.xml.transform.TransformerFactory.newInstance();

        if (source instanceof Packages.org.apache.excalibur.source.ModifiableSource
            && tf.getFeature(Packages.javax.xml.transform.sax.SAXTransformerFactory.FEATURE)) {

            outputStream = source.getOutputStream();
            var transformerHandler = tf.newTransformerHandler(xslsource);
            var transformer = transformerHandler.getTransformer();
            transformer.setOutputProperty(Packages.javax.xml.transform.OutputKeys.INDENT, "true");
            transformer.setOutputProperty(Packages.javax.xml.transform.OutputKeys.METHOD, "xml");
	        transformer.setParameter("rootElement", DFORM.rootElement);
            transformerHandler.setResult(new Packages.javax.xml.transform.stream.StreamResult(outputStream));
            this.getXML().toSAX(transformerHandler);
        } else {
            throw new Packages.org.apache.cocoon.ProcessingException("Cannot write to source " + uri);
        }

    } finally {
        if (outputStream != null) {
            outputStream.flush();
            outputStream.close();
        }

        if (source != null) {
            resolver.release(source);
        }

        cocoon.releaseComponent(resolver);
    }
}