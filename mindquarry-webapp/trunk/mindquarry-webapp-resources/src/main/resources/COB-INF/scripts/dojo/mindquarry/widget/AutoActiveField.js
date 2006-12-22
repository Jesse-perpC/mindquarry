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

        // the class attribute is used to indicate special cases where a
        // sub-element will be the communicator for the autoactive information
        // that should be applied to the parent widget	    
	    if (this.domNode.className.indexOf("use-parent-for-autoactive") >= 0) {
	        dojo.event.connect(this.domNode.parentNode, "onclick", this, "onClick");
	    } else {
	        dojo.event.connect(this.domNode, "onclick", this, "onClick");
	    }
	    
	    // sometimes the id is contained directly within the element that is
	    // marked as autoactive, and sometimes we apply it to the parent and
	    // this one will contain the id that is sent in the activation call
	    if (this.domNode.className.indexOf("use-parent-id") >= 0) {
	        this.activateID = this.domNode.parentNode.id;
	    } else {
	        this.activateID = this.domNode.id;
	    }
    },
    
    onClick: function(event) {
        dojo.debug("onClick: domNode=" + this.domNode + ", activateID=" + this.activateID);
        // in case of a parent onClick handling, the parent domNode will have
        // a longer lifecycle than the domNode, which might have been replaced
        // by ajax calls in the meantime; in such a case the domNode gets null
        if (this.domNode==null) {
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
        	this.cform.submit("ductform.ductforms_activate", {activate : this.activateID});
        }
        return false;
    }
	
});