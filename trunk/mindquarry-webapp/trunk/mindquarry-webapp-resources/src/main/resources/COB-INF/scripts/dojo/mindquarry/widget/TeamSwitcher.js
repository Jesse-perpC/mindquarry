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
	    var list = this.domNode.getElementsByTagName("ul")[0];
	    
	    var links = list.getElementsByTagName("a");
	    var current = this.domNode.title;
	    var baselink = dojo.byId("teamspace-base-link");
	    this.domNode.title = "";
	    
	    // create selection list area
	    slArea_ = document.createElement("div");
	    slArea_.id = "slarea";
	    var secondlist = list.cloneNode(true);
	    var allteams = document.createElement("li");
	    allteams.innerHTML = "<a href='"+baselink.href+"' class='allyourteams'>All your teams</a>"
	    secondlist.appendChild(allteams);
	    slArea_.appendChild(secondlist);
	    slArea_.style.display = "none";
	    
	    var newlist = slArea_.getElementsByTagName("a");
	    
	    for (var i=0;i<newlist.length;i++) {
	        if (newlist[i].title!="") {
	            newlist[i].href = baselink.href + newlist[i].title + "/";
	            newlist[i].style.backgroundImage="url(/teamspace/"+newlist[i].title+".22.png)";
	        } else {
	            newlist[i].href = baselink.href;
	        }
	    }
	    	    
	    var active = false;
	    var empty = true;
	    for (var i=0;i<links.length;i++) {
	    	empty = false;
	        if (links[i].title == current) {
	            links[i].parentNode.style.display = "block";
	            //alert("Make it visible");
	            active = true;
	        }
	    }
	    
	    if (!active) {
	        var overview = document.createElement("li");
	        if (empty) {
	        	overview.innerHTML = '<a href="#">No teams</a>';
	        } else {
	        	overview.innerHTML = '<a href="'+baselink.href+'" class="allyourteams">All your teams</a>';
	        }
	        overview.style.display = "block";
	        list.appendChild(overview);
	        
	    }
	    
	    if (!empty) {
	    	dojo.event.connect(list, "onclick", this, "onClick");
	    }
	    dojo.event.connect(document.body, "onclick", this, "hideSwitcher");
	    this.domNode.appendChild(slArea_);
    },	
    
    onClick : function(event) {
        event.preventDefault();
        if (slArea_.style.display!="block") {
            slArea_.style.display = "block";
        } else {
            slArea_.style.display = "none";
        }
    },
    
    hideSwitcher : function(event) {
        if (slArea_.style.display=="block") {
            var context = event.target;
            while (context.parentNode) {
                if (context==this.domNode) {
                    return;
                }
                context = context.parentNode;        
            }
            slArea_.style.display = "none";
        }
    }
});