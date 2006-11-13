dojo.provide("mindquarry.widget.ToggleButton");

dojo.require("dojo.widget.*");
dojo.require("dojo.html");
dojo.require("dojo.dom");
dojo.require("dojo.event");

dojo.widget.tags.addParseTreeHandler("dojo:ToggleButton");
dojo.widget.manager.registerWidgetPackage("mindquarry.widget");
mindquarry.widget.ToggleButton = function() {
	dojo.widget.DomWidget.call(this);
	var checkbox = null;
	var parentname;
	var radio = false;
}

dojo.inherits(mindquarry.widget.ToggleButton, dojo.widget.DomWidget);

dojo.lang.extend(mindquarry.widget.ToggleButton, {
	widgetType: "ToggleButton",
	isContainer: true,
	
	    buildRendering: function(args, parserFragment, parentWidget) {
        // Magical statement to get the dom node, stolen in DomWidget
	    this.domNode = parserFragment["dojo:"+this.widgetType.toLowerCase()].nodeRef;
	    this.checkbox = dojo.dom.getFirstChildElement(this.domNode, "input");
	    this.parentname = this.domNode.parentNode.id;
	    if (this.checkbox.type=="radio") {
	    	this.radio = true;
	    }
	    dojo.event.connect(this.domNode, "onclick", this, "onClick");
	    //dojo.event.connect(this.checkbox, "onCValueChanged", this, "adjustClasses");
    },
    
    onClick: function(event) {
        event.preventDefault();
        this.checkbox.checked = !this.checkbox.checked;
        this.adjustClasses(event);
        cocoon.forms.submitForm(this.domNode, this.parentname);
    },
    
    adjustClasses: function(event) {
    	event.preventDefault();
    	if (this.radio) {
	    	var children = this.domNode.parentNode.childNodes;
	    	for (var i=0;i<children.length;i++) {
	    		if (children[i].nodeType==1&&children[i].nodeName=="DIV") {
	    			var checkbox = checkbox = dojo.dom.getFirstChildElement(children[i], "input");
	    			if (checkbox.checked) {
			        	//alert("checked");
			        	dojo.html.replaceClass(children[i], "togglebuttonpushed", "togglebutton");
			        	//this.domNode.classname="togglebuttonpushed";
			        } else {
			        	//alert("unchecked");
			        	dojo.html.replaceClass(children[i], "togglebutton", "togglebuttonpushed");
			        	//this.domNode.classname="togglebutton";
			        }
	    		}
	    	}
    	}
        if (this.checkbox.checked) {
        	//alert("checked");
        	dojo.html.replaceClass(this.domNode, "togglebuttonpushed", "togglebutton");
        	//this.domNode.classname="togglebuttonpushed";
        } else {
        	//alert("unchecked");
        	dojo.html.replaceClass(this.domNode, "togglebutton", "togglebuttonpushed");
        	//this.domNode.classname="togglebutton";
        }
    }
});