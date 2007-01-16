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
dojo.provide("mindquarry.widget.TeamSwitcher");

dojo.require("dojo.widget.*");
dojo.require("dojo.html");
dojo.require("dojo.event");

dojo.widget.tags.addParseTreeHandler("dojo:TeamSwitcher");
dojo.widget.manager.registerWidgetPackage("mindquarry.widget");

mindquarry.widget.TeamSwitcher = function() {
	dojo.widget.DomWidget.call(this);
	
	var cform = null;
}
dojo.inherits(mindquarry.widget.TeamSwitcher, dojo.widget.DomWidget);

dojo.lang.extend(mindquarry.widget.TeamSwitcher, {
	widgetType: "TeamSwitcher",
	isContainer: true,
	
	slArea_: null,
	
	buildRendering: function(args, parserFragment, parentWidget) {
        // Magical statement to get the dom node, stolen in DomWidget
	    this.domNode = parserFragment["dojo:"+this.widgetType.toLowerCase()].nodeRef;
	    var list = this.domNode.getElementsByTagName("ul")[0]
	    
	    var links = list.getElementsByTagName("a");
	    var current = this.domNode.title;
	    this.domNode.title = "";
	    
	    // create selection list area
	    slArea_ = document.createElement("div");
	    slArea_.id = "slarea";
	    slArea_.appendChild(list.cloneNode(true));
	    slArea_.style.display = "block";
	    
	    var active = false;
	    for (var i=0;i<links.length;i++) {
	        if (links[i].title != current) {
	            links[i].parentNode.style.display = "none";
	        } else {
	            active = true;
	        }
	    }
	    
	    if (!active) {
	        var overview = document.createElement("li");
	        overview.innerHTML = '<a href="#">Overview</a>';
	        list.appendChild(overview);
	    }
	    
	    dojo.event.connect(list, "onclick", this, "onClick");
	    
	    this.domNode.appendChild(slArea_);
    },	
    
    onClick : function(event) {
        event.preventDefault();
        if (slArea_.style.display!="block") {
            slArea_.style.display = "block";
        } else {
            slArea_.style.display = "none";
        }
    }
});