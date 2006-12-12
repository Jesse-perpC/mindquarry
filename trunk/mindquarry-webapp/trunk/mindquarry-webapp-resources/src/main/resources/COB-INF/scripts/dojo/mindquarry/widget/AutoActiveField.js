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
	    if (this.domNode.nodeName == "INPUT") {
	        //alert("autoactive parent=" + this.domNode.parentNode.nodeName);
	        dojo.event.connect(this.domNode.parentNode, "onclick", this, "onClick");
	    } else {
	        //alert("autoactive nodeName=" + this.domNode.nodeName);
	        dojo.event.connect(this.domNode, "onclick", this, "onClick");
	    }
    },
    
    onClick: function(event) {
        // in case of a parent onClick handling, the parent domNode will have
        // a longer lifecycle than the domNode, which might have been replaced
        // by ajax calls in the meantime; in such a case the domNode gets null
        if (this.domNode==null) {
            //alert("isNull");
            return true;
        }
        
        event.preventDefault();

        if (this.cform==null) {
        	var form = cocoon.forms.getForm(this.domNode);
        	var dojoId = form.getAttribute("dojoWidgetId");
        	if (dojoId) {
        		this.cform = dojo.widget.byId(dojoId);
        	}
        }
        if (this.cform!=null) {
        	this.cform.submit("ductform.ductforms_activate" ,{activate : this.domNode.id});
        }
        return false;
    }
	
});