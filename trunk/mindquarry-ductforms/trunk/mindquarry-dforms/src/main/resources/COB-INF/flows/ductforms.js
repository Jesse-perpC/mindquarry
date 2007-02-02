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
cocoon.load("block:resources:/flows/util.js"); // only reloaded on restart!

/////////////////////////////////////////////////////////
// global variables (TODO: make an object)
/////////////////////////////////////////////////////////

var CLEAN_MODEL_XSL = "xsl/model/saveclean.xsl";
var form_;
var baseURI_;
var documentID_;
var suffix_;
var rootElement_;

function getFilename() {
    return documentID_ + suffix_;
}

function getFullPath() {
    return baseURI_ + getFilename();
}

/////////////////////////////////////////////////////////
// entry point method
/////////////////////////////////////////////////////////

// called from sitemap via handleForm()
function showDForm(form) {
    // catch all parameters and store them globally
    baseURI_ = cocoon.parameters["baseURI"];
    documentID_ = cocoon.parameters["documentID"];
    suffix_ = ".xml";
    rootElement_ = cocoon.parameters["rootElement"];

    var isEditStart = (documentID_ == 'new' || !resourceExists(getFullPath()));
    
	// save form and uri for actions
	form_ = form;

	// load file from internal pipeline
	form.loadXML("cocoon:/" + getFilename() + ".plain");

    // feature for wiki: when creating a new page via the URL == documentID
    // give the title that documentID as default value
    if (isEditStart && documentID_ != "new") { 
    	var titleWidget = form_.lookupWidget("/title");
    	if (titleWidget && titleWidget.getValue() == null) {
    	    titleWidget.setValue(documentID_);
    	}
    }
	
	// set initial state to output
	setWidgetStates(form, isEditStart);

    // the continuations will be inside this method again and again
	form.showForm(getFilename() + ".instance");
}

/////////////////////////////////////////////////////////
// action widgets handler
/////////////////////////////////////////////////////////

// called by auto-reload or AJAX or on loadXML binding for mutivalue fields
function fieldsChanged(event) {
	if (form_) {
		setWidgetStates(form_, true);
	}
}

// enter partial edit mode or "expand" partial edit mode
function activate(event) {
	if (form_ && cocoon.request.activate) {
		var selectedWidget = form_.lookupWidget("/" + cocoon.request.activate.substring(9));
		if (selectedWidget) {
		    // activate all descendant widgets
    		var descendants = getSubWidgets(selectedWidget);
    		
			// descendants contains the widget itself
			for (var j=0;j<descendants.length;j++) {
				descendants[j].setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
			}

            // activate all related widgets
    		var related = getRelatedWidgets(form_, selectedWidget);
		
			for (var j=0;j<related.length;j++) {
				var relwidget = form_.lookupWidget("/" + related[j]);
				relwidget.setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
			}
			
			form_.lookupWidget("/ductforms_save").setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
			form_.lookupWidget("/ductforms_cancel").setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
		}
	}
}

// enter mode where all fields are active and the ductforms field chooser is visible
function editAll(event) {
	if (form_) {
        // make everything editable
		setWidgetStates(form_, true);
	}
}

function save(event) {
	if (form_) {
	    var newDocument = false;
	    if (documentID_ == "new") {
	        // this does the trick: it will load the uniqueName.js from the
	        // block that calls us via block:super: and provides its own version
	        // of uniqueName.js. the implementation of uniqueName.js must return
	        // a new document id.
	        documentID_ = evalJavaScriptSource("block:/uniqueName.js");
	        newDocument = true;
	    }
	    
		// the form includes all possible fields, but only some of them are
		// actually used. the best solution would be to strip out forms
		form_.saveXML(getFullPath(), CLEAN_MODEL_XSL);
		
		if (newDocument) {

            // stop form processing before the redirect, otherwise the redirect
            // would not work properly as he also runs the form output pipeline
		    form_.getWidget().endProcessing(false);
		    // go to the real url of the newly created document (the pipeline
		    // will create some special redirect xml that is either interpreted
		    // by the browser-update handler client-side javascript to do the
		    // redirect from within the browser or (if Javascript is disabled
		    // == this is no ajax request) the RedirectTransformer will do a
		    // HTTP redirect based on the xml content) 
		    cocoon.redirectTo("cocoon:/redirectTo/" + documentID_);
		    
		} else {
		
		    // existing documents only switch to view mode after a save
		    setWidgetStates(form_, false);
		}
	}
}

function cancel(event) {
	if (form_) {
	    if (documentID_ == "new") {
	        // user does not want to create a new document
	        
    	    // stop any further form processing to be able to do the redirect
    		form_.getWidget().endProcessing(false);
    		
    	    // go back to the task list
    	    cocoon.redirectTo("cocoon:/redirectTo/.");
    	} else {
    	    // user cancels changes of an existing document
    	    
    	    // stop any further form processing to be able to do the redirect
    		form_.getWidget().endProcessing(false);
    		
    	    // reload the form in view mode (new continuation)
    	    cocoon.redirectTo("cocoon:/redirectTo/" + documentID_);
    	}
	}
}

/* delete is a protected keyword in javascript... */
function deleteIt(event) {
	if (form_) {
		var source = 0;
		try {
			source = resolveSource(getFullPath());
			source["delete"]();
		} finally {
			releaseSource(source);
		}

	    // stop any further form processing to be able to do the redirect
		form_.getWidget().endProcessing(false);
		
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
	}

    // the activate and keepAlive actions are always active (but not visible)
	widgetMap.put(form.lookupWidget("/ductforms_activate"), Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
	widgetMap.put(form.lookupWidget("/ductforms_keepalive"), Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
	// the delete action is always active except when creating a document
    if (documentID_ != "new") {
		widgetMap.put(form.lookupWidget("/ductforms_delete"), Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
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
	        transformer.setParameter("rootElement", rootElement_);
            transformerHandler.setResult(new Packages.javax.xml.transform.stream.StreamResult(outputStream));
            this.getXML().toSAX(transformerHandler);
        } else {
            throw new Packages.org.apache.cocoon.ProcessingException("Cannot write to source " + uri);
        }

    } finally {
        if (source != null)
            resolver.release(source);

        cocoon.releaseComponent(resolver);

        if (outputStream != null) {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (error) {
                cocoon.log.error("Could not flush/close outputstream: " + error);
            }
        }
    }
}