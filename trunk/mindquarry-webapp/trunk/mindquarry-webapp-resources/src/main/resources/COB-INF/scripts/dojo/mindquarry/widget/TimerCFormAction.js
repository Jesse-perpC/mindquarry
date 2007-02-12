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
 
dojo.provide("mindquarry.widget.TimerCFormAction");

dojo.require("dojo.widget.*");
dojo.require("dojo.html");
dojo.require("dojo.event");
//dojo.require("dojo.lang.timing.Timer"); // 0.4 ???
dojo.require("dojo.animation.Timer"); // 0.3.1 - seems deprecated later

dojo.widget.tags.addParseTreeHandler("dojo:TimerCFormAction");
dojo.widget.manager.registerWidgetPackage("mindquarry.widget");
mindquarry.widget.TimerCFormAction = function() {
	dojo.widget.DomWidget.call(this);
	
	var cform = null;
}


dojo.inherits(mindquarry.widget.TimerCFormAction, dojo.widget.DomWidget);

dojo.lang.extend(mindquarry.widget.TimerCFormAction, {
	widgetType: "TimerCFormAction",
	isContainer: true,
	
	buildRendering: function(args, parserFragment, parentWidget) {
        // Magical statement to get the dom node, stolen in DomWidget
	    this.domNode = parserFragment["dojo:"+this.widgetType.toLowerCase()].nodeRef;
	    this.cform = parentWidget;
	    
		var delay = dojo.html.getAttribute(this.domNode, "delay");
		
		// in case no delay value was given or if it is smaller than 1 second,
		// we reset it to 1 minute to avoid continous requests blocking the site 
		if (delay == null || delay == "" || delay < 1000) {
			delay = 60000; // 1 minute
		}
		
	    // call a keep-alive on each delay
	    var timer = new dojo.animation.Timer(delay);
	    
	    // store in local variables for onTick function
	    var domNode = this.domNode;
	    var cform = this.cform;
	    
	    timer.onTick = function() {
	        if (cform==null) {
	        	var form = cocoon.forms.getForm(domNode);
	        	var dojoId = form.getAttribute("dojoWidgetId");
	        	if (dojoId) {
	        		cform = dojo.widget.byId(dojoId);
	        	}
	        }
        	cform.submit(domNode.name);	    	
	    }
	    
	    timer.start();
    }	
});