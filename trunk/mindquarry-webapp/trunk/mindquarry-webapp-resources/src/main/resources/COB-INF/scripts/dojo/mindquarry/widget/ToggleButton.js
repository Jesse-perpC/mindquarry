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
}

dojo.inherits(mindquarry.widget.ToggleButton, dojo.widget.DomWidget);

dojo.lang.extend(mindquarry.widget.ToggleButton, {
	widgetType: "ToggleButton",
	isContainer: true,
	
	    buildRendering: function(args, parserFragment, parentWidget) {
        // Magical statement to get the dom node, stolen in DomWidget
	    this.domNode = parserFragment["dojo:"+this.widgetType.toLowerCase()].nodeRef;
	    this.checkbox = dojo.dom.getFirstChildElement(this.domNode, "input");
	    
	    dojo.event.connect(this.domNode, "onclick", this, "onClick");
	    //dojo.event.connect(this.checkbox, "onvaluechange", this, "adjustClasses");
    },
    
    onClick: function(event) {
        event.preventDefault();
        this.checkbox.checked = !this.checkbox.checked;
        this.adjustClasses(event);
    },
    
    adjustClasses: function(event) {
        event.preventDefault();
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