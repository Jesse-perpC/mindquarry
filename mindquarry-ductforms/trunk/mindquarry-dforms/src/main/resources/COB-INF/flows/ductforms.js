cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");

var CLEAN_MODEL_XSL = "xsl/model/saveclean.xsl";
var form_;
var fullURI_;

/////////////////////////////////////////////////////////
// specific actions
/////////////////////////////////////////////////////////

// called by auto-reload or AJAX for mutivalue fields
function fieldsChanged(event) {
	print("*** fieldsChanged");
	if (form_) {
		setWidgetStates(form_, true);
	}
}

function switchEditView(event) {
	print("*** switchEditView");
	if (form_) {
		if (cocoon.request.activate) {
			//print("activating: " + cocoon.request.activate.substring(9))
			var selectedWidget = form_.lookupWidget("/" + cocoon.request.activate.substring(9));
			if (selectedWidget) {
				selectedWidget.setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
				form_.lookupWidget("/ductforms_save").setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
				form_.lookupWidget("/ductforms_switch").setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE);
			}
		} else {
			print("  switchEditView");
			var formWidget = form_.lookupWidget("/title");
			print(formWidget.getState());
			if (formWidget.getState() == Packages.org.apache.cocoon.forms.formmodel.WidgetState.OUTPUT) {
				//formWidget.setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
				setWidgetStates(form_, true);
			} else {
				//formWidget.setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.OUTPUT);
				setWidgetStates(form_, false);
			}
		}
	}
}

function save(event) {
	if (form_ && fullURI_) {
		// the form includes all possible fields, but only some of them are
		// actually used. the best solution would be to strip out forms
		form_.saveXML(fullURI_, CLEAN_MODEL_XSL);
		switchEditView(event);
	}
}

/////////////////////////////////////////////////////////
// form handling
/////////////////////////////////////////////////////////

function setWidgetStates(form, isEdit) {
	// hide all widgets first
	var allWidgets = form.lookupWidget("/").getChildren();
	for (; allWidgets.hasNext(); ) {
		var widget = allWidgets.next();
		print(widget + " invisible");
		widget.setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE);
		//allWidgets.next().setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE);
	}
	
	var ductformsWidget = form.lookupWidget("/ductforms");
	
	// show only selected fields
	var ductfields = ductformsWidget.getValue();
	for (var i=0; i<ductfields.length; i++) {
		var widget = form.lookupWidget("/" + ductfields[i]);
		var attribute = widget.getAttribute("related");
		if (attribute!=null) {
			var related = attribute.split(",");
		} else {
			var related = new Array();
		}
		if (isEdit) {
			widget.setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
			for (var j=0;j<related.length;j++) {
				var relwidget = form.lookupWidget("/" + related[j]);
				relwidget.state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE;
				print("also activate related " + relwidget);
			}
		} else {
			widget.setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.OUTPUT);
			for (var j=0;j<related.length;j++) {
				var relwidget = form.lookupWidget("/" + related[j]);
				if (relwidget instanceof Packages.org.apache.cocoon.forms.formmodel.Action) {
					relwidget.state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE;
				} else {
					relwidget.state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.OUTPUT;
				}
			}
		}
	}

	if (isEdit) {
		// show the field selector in edit mode
		ductformsWidget.setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);

		// save button only in edit mode
		form.lookupWidget("/ductforms_save").setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
	} else {
	
		// the switch button should not be always active
		form.lookupWidget("/ductforms_switch").setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
	}
	
}

function setFormState(form, isEdit) {
	// hide all widgets first
	var allWidgets = form.lookupWidget("/").getChildren();
	for (; allWidgets.hasNext(); ) {
		allWidgets.next().setState(Packages.org.apache.cocoon.forms.formmodel.WidgetState.OUTPUT);
	}
}

// called from sitemap via handleForm()
function showDForm(form) {
	var filename = cocoon.parameters["documentID"] + ".xml";
	
	// save form and uri for actions
	form_ = form;
	fullURI_ = cocoon.parameters["baseURI"] + filename;

	// load file from internal pipeline
	form.loadXML("cocoon:/" + filename + ".plain");
	
	// set initial state to output
	setWidgetStates(form, false);

	print("before showForm");
	form.showForm(filename + ".instance");
	print("after showForm");
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