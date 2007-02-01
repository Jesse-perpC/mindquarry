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
 
dojo.provide("mindquarry.widget.AutoActiveButton");

dojo.require("dojo.widget.*");
dojo.require("dojo.html");
dojo.require("dojo.event");

dojo.widget.tags.addParseTreeHandler("dojo:AutoActiveButton");
dojo.widget.manager.registerWidgetPackage("mindquarry.widget");
mindquarry.widget.AutoActiveButton = function() {
	dojo.widget.DomWidget.call(this);
	
	var cform = null;
	var confirm = null;
}


dojo.inherits(mindquarry.widget.AutoActiveButton, dojo.widget.DomWidget);

// the AutoActiveButton adds compatibility of cocoon buttons with our
// AutoActiveField - there are problems with the identification of the
// submit widget when you add the AutoActiveField to a CForm -> to avoid that
// make all buttons of the form an dojo AutoActiveButton
dojo.lang.extend(mindquarry.widget.AutoActiveButton, {
	widgetType: "AutoActiveButton",
	isContainer: true,
	
	buildRendering: function(args, parserFragment, parentWidget) {
        // Magical statement to get the dom node, stolen in DomWidget
	    this.domNode = parserFragment["dojo:"+this.widgetType.toLowerCase()].nodeRef;
	    this.cform = parentWidget;

		this.confirm = dojo.html.getAttribute(this.domNode, "confirm");
        
	    dojo.event.connect(this.domNode, "onclick", this, "onClick");
    },
    
    onClick: function(event) {
        // in case of a parent onClick handling, the parent domNode will have
        // a longer lifecycle than the domNode, which might have been replaced
        // by ajax calls in the meantime; in such a case the domNode gets null
        if (this.domNode==null) {
            return true;
        }
        
        event.preventDefault();

        if (this.confirm) {
        	if (!confirm(this.confirm)) {
        		return;
        	}
        }
        
        if (this.cform==null) {
        	var form = cocoon.forms.getForm(this.domNode);
        	var dojoId = form.getAttribute("dojoWidgetId");
        	if (dojoId) {
        		this.cform = dojo.widget.byId(dojoId);
        	}
        }
        if (this.cform!=null) {
            // update the dojo Editor2 content
            dojo.lang.forEach(dojo.widget.byType("Editor2"), function(ed){
              	dojo.byId(ed.widgetId).value = ed.getEditorContent();
              }
            );
	
        	//never forget: use name, not id for cocoon forms
        	this.cform.submit(this.domNode.name);
        }
        return false;
    }
	
});