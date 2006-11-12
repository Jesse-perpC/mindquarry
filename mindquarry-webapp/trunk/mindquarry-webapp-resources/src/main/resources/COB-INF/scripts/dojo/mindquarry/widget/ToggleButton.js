dojo.provide("mindquarry.widget.ToggleButton");

dojo.require("dojo.widget.*");
dojo.require("dojo.html");
dojo.require("dojo.event");

dojo.widget.tags.addParseTreeHandler("dojo:ToggleButton");
dojo.widget.manager.registerWidgetPackage("mindquarry.widget");
mindquarry.widget.ToggleButton = function() {
	dojo.widget.DomWidget.call(this);
}

dojo.inherits(mindquarry.widget.ToggleButton, dojo.widget.DomWidget);

dojo.lang.extend(mindquarry.widget.ToggleButton, {
	widgetType: "ToggleButton",
	isContainer: true,
	
	    buildRendering: function(args, parserFragment, parentWidget) {
        // Magical statement to get the dom node, stolen in DomWidget
	    this.domNode = parserFragment["dojo:"+this.widgetType.toLowerCase()].nodeRef;
	    //dojo.event.connect(this.domNode, "onclick", this, "onClick");
    },
    
    onClick: function(event) {
        event.preventDefault();
        //alert("clicked");
        return false;
    }
	
});