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
 
dojo.provide("mindquarry.widget.MindquarryDatePicker");

dojo.require("dojo.widget.*");
dojo.require("dojo.widget.html.DatePicker");
dojo.require("dojo.html");
dojo.require("dojo.event");

dojo.widget.tags.addParseTreeHandler("dojo:MindquarryDatePicker");
dojo.widget.manager.registerWidgetPackage("mindquarry.widget");

mindquarry.widget.MindquarryDatePicker = function() {
	dojo.widget.html.DatePicker.call(this);
	this.widgetType = "MindquarryDatePicker";
	//alert("date picker");
}

dojo.inherits(mindquarry.widget.MindquarryDatePicker, dojo.widget.html.DatePicker);

//dojo.inherits(mindquarry.widget.MindquarryDatePicker, dojo.widget.html.DatePicker);


dojo.lang.extend(mindquarry.widget.MindquarryDatePicker, {
	widgetType: "MindquarryDatePicker",
	isContainer: true,
	    buildRendering: function(args, parserFragment, parentWidget) {
        // Magical statement to get the dom node, stolen in DomWidget
	    this.domNode = parserFragment["dojo:"+this.widgetType.toLowerCase()].nodeRef;
	    this.cform = parentWidget;
	    
	    dojo.event.connect(this.domNode, "onclick", this, "onClick");
    },
    
    onClicj : function(event) {}
});