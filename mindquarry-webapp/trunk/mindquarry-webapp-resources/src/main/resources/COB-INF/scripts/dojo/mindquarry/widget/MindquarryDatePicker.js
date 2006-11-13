dojo.provide("mindquarry.widget.MindquarryDatePicker");

dojo.require("dojo.widget.*");
dojo.require("dojo.widget.html.*");
dojo.require("dojo.html");
dojo.require("dojo.event");

dojo.widget.tags.addParseTreeHandler("dojo:MindquarryDatePicker");
dojo.widget.manager.registerWidgetPackage("mindquarry.widget");

mindquarry.widget.MindquarryDatePicker = function() {
	//dojo.widget.html.DatePicker.call(this);
	
	var cform = null;
	alert("date picker");
}

dojo.inherits(mindquarry.widget.MindquarryDatePicker, dojo.widget.html.DatePicker);

dojo.lang.extend(mindquarry.widget.MindquarryDatePicker, {
	widgetType: "MindquarryDatePicker",
	isContainer: true,
	    buildRendering: function(args, parserFragment, parentWidget) {
        // Magical statement to get the dom node, stolen in DomWidget
	    this.domNode = parserFragment["dojo:"+this.widgetType.toLowerCase()].nodeRef;
	    this.cform = parentWidget;
	    
	    dojo.event.connect(this.domNode, "onclick", this, "onClick");
    },
});