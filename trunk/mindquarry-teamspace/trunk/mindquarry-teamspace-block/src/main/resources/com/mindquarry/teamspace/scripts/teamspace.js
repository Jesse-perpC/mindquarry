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
	var parent = event.target.parentNode;
	var details = dojo.html.getElementsByClass("details", parent);
	var members = dojo.html.getElementsByClass("members", parent);
	
	var detailsButton = event.target;
	if(detailsButton.className == "details-expanded") {
		detailsButton.className = "details-collapsed";
		
		for(i=0; i<details.length; i++) {
			details[i].style.display = "none";
		}
		for(i=0; i<members.length; i++) {
			members[i].style.display = "block";
		}
	} else {
		detailsButton.className = "details-expanded";
		
		for(i=0; i<details.length; i++) {
			details[i].style.display = "block";
		}
		for(i=0; i<members.length; i++) {
			members[i].style.display = "none";
		}
	}
}

dojo.event.connect(dojo.hostenv, "loaded", "initTeamspaceEvents");
