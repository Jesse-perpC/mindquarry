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
dojo.require("dojo.io.BrowserIO");
dojo.require("dojo.event.*");
dojo.require("dojo.html");
dojo.require("dojo.dom");
dojo.require("dojo.lfx.html");

/* doing some init stuff */
initTeamspaceEvents = function() {
	detailsButtons = dojo.html.getElementsByClassName("details-collapsed");

	// add prev & next buttons to all member lists
	for (i=0; i<detailsButtons.length; i++) {
		dojo.event.connect(detailsButtons[i], "onclick", "doExpandOrCollapse");
	}
}

/* expand teamspace */
doExpandOrCollapse = function(event) {
    event.preventDefault();
	var detailsButton = event.target;
	if(detailsButton.className == "details-expanded") {
		detailsButton.className = "details-collapsed";
	} else {
		detailsButton.className = "details-expanded";
	}
	
	var childs = dojo.dom.getNextSiblingElement(detailsButton).childNodes;
	for(i=0; i<childs.length; i++) {
		var child = childs[i];
		var anims = new Array();
		if((child.nodeName == "DIV") && (child.className == "details")) {
			if(detailsButton.className == "details-expanded") {
				dojo.lfx.html.wipeIn(child, 350).play();
				child.style.display = "block";
			} else {
				//dojo.lfx.html.fadeOut(child, 340, 1, function() {child.style.display = "none"; });
				dojo.lfx.html.wipeOut(child, 350).play();
				//child.style.display = "none";
			}
		}
		
		if((child.nodeName == "UL") && (child.className == "members")) {
			if(detailsButton.className == "details-expanded") {
				child.style.display = "none";
				//anims[anims.length] = dojo.lfx.html.fadeOut(child, 350, null, function() {child.style.display = "none";});
			} else {
				child.style.display = "block";
				//anims[anims.length] = dojo.lfx.html.fadeIn(child, 350);
			}
		}
		
		if((child.nodeName == "DIV") && (child.className == "edit-buttons")) {
			if(detailsButton.className == "details-expanded") {
				//alert(child);
				//anims[anims.length] = dojo.lfx.html.fadeIn(child, 350);
				child.style.display = "block";
				//child.firstChild.style.display = "block";
				//child.lastChild.style.display = "block";
			} else {
				//anims[anims.length] = dojo.lfx.html.fadeOut(child, 350);
				child.style.display = "none";
				//child.firstChild.style.display = "none";
				//child.lastChild.style.display = "none";
			}
		}
		
		//dojo.lfx.chain(anims).play();
	}
}

dojo.event.connect(dojo.hostenv, "loaded", "initTeamspaceEvents");
