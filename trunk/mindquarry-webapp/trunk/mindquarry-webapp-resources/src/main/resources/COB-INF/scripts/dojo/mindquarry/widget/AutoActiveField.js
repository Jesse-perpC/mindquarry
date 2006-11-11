dojo.provide("mindquarry.widget.AutoActiveField");

dojo.require("dojo.widget.*");
dojo.require("dojo.html");
dojo.require("dojo.event");

dojo.widget.tags.addParseTreeHandler("dojo:AutoActiveField");
dojo.widget.manager.registerWidgetPackage("mindquarry.widget");
mindquarry.widget.AutoActiveField = function() {
	dojo.widget.DomWidget.call(this);
	
	var cform = null;
}

dojo.inherits(mindquarry.widget.AutoActiveField, dojo.widget.DomWidget);

dojo.lang.extend(mindquarry.widget.AutoActiveField, {
	widgetType: "AutoActiveField",
	isContainer: true,
	
	    buildRendering: function(args, parserFragment, parentWidget) {
        // Magical statement to get the dom node, stolen in DomWidget
	    this.domNode = parserFragment["dojo:"+this.widgetType.toLowerCase()].nodeRef;
	    this.cform = parentWidget;
	    
	    dojo.event.connect(this.domNode, "onclick", this, "onClick");
    },
    
    onClick: function(event) {
        event.preventDefault();
        this.cform.submit("ductform.ductforms_switch" ,{activate : this.domNode.id});
        return false;
    }
	
});