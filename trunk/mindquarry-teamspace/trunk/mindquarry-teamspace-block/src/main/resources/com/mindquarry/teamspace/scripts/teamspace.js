dojo.require("dojo.io.BrowserIO");
dojo.require("dojo.event.*");
dojo.require("dojo.html");

/* doing some init stuff */
initTeamspaceEvents = function() {
	detailsButtons = dojo.html.getElementsByClassName("details-collapsed");

	// add prev & next buttons to all member lists
	for (i=0; i<detailsButtons.length; i++) {
		dojo.event.connect(detailsButtons[i], "onclick", "doExpandOrCollapse");
	}
}

/* expand teamspace */
doExpandOrCollapse = function(event)	{
	var detailsButton = event.target;
	if(detailsButton.className == "details-expanded") {
		detailsButton.className = "details-collapsed";
	} else {
		detailsButton.className = "details-expanded";
	}
	
	var childs = detailsButton.nextSibling.childNodes;
	for(i=0; i<childs.length; i++) {
		var child = childs[i];
		
		if((child.nodeName == "DIV") && (child.className == "details")) {
			if(detailsButton.className == "details-expanded") {
				child.style.display = "block";
			} else {
				child.style.display = "none";
			}
		}
		
		if((child.nodeName == "UL") && (child.className == "members")) {
			if(detailsButton.className == "details-expanded") {
				child.style.display = "none";
			} else {
				child.style.display = "block";
			}
		}
		
		if((child.nodeName == "A") && (child.className == "edit_members_button")) {
			if(detailsButton.className == "details-expanded") {
				child.style.display = "block";
			} else {
				child.style.display = "none";
			}
		}
		
		if((child.nodeName == "A") && (child.className == "edit_subprojects_button")) {
			if(detailsButton.className == "details-expanded") {
				child.style.display = "block";
			} else {
				child.style.display = "none";
			}
		}
	}
}

dojo.event.connect(dojo.hostenv, "loaded", "initTeamspaceEvents");
