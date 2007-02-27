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

var issliderinitialized = false;

/* doing some init stuff */
initSliderEvents = function() {
    if (issliderinitialized) {
        return;
    }
    
	slidingLists = dojo.html.getElementsByClassName("members");

	// add prev & next buttons to all member lists
	for (i=0; i<slidingLists.length; i++) {
		var next = document.createElement('a');
		next.href = "#";
		next.title="Click here to see next project members";
		next.className = "slidernext";
		next.innerHTML = "<b>next</b>";
		
		var prev = document.createElement('a');
		prev.href = "#";
		prev.title="Click here to see previous project members"
		prev.className = "sliderprev";
		prev.innerHTML = "<b>prev</b>";
		prev.style.visibility = "hidden";
		
		slidingLists[i].appendChild(next);
		slidingLists[i].insertBefore(prev, slidingLists[i].firstChild);
		
		dojo.event.connect(next, "onclick", "doNext");
		dojo.event.connect(prev, "onclick", "doPrevious");
		
		// set visibility for member entries
		memberEntries = slidingLists[i].getElementsByTagName("li");
		for(j=0; j<memberEntries.length; j++) {
			if(j<5){
				memberEntries[j].style.display = "block";
			} else {
				memberEntries[j].style.display = "none";
			}
		}
		if (memberEntries.length <= 5) {
			next.style.visibility = "hidden";
		}
	}
	issliderinitialized = true;
}

/* slide to next image */
doNext = function(event) {
    event.preventDefault();
    
	var first = null;
	var last = null;
	
	var ul = event.target.parentNode;
	var lis = ul.getElementsByTagName("li");
	
	for (i=0; i<lis.length; i++) {
		var li = lis[i];
		if ((li.style.display != 'none') && (first==null)) {
			first = li;
		}
		if ((li.style.display == 'none') && (last == null) && (first != null)) {
			last = li;
			
			if(i == (lis.length - 1)) {
				event.target.style.visibility = "hidden";
			}
		}
	}
	if ((first != null) && (last != null)) {
		first.style.display = 'none';
		last.style.display = 'block';
		
		ul.firstChild.style.visibility = "visible";
	}
}

/* slide to previous image */
doPrevious = function(event)	{
    event.preventDefault();

	var first = null;
	var last = null;
	
	var ul = event.target.parentNode;
	var lis = ul.getElementsByTagName("li");
	
	for (i=lis.length-1; i>=0; i--) {
		var li = lis[i];
		if ((li.style.display != 'none') &&(first == null)) {
			first = li;
		}
		if ((li.style.display == 'none') && (last == null) && (first != null)) {
			last = li;
			
			if(i == 0) {
				event.target.style.visibility = "hidden";
			}
		}
	}
	if ((first != null) && (last != null)) {
		first.style.display = 'none';
		last.style.display = 'block';
		
		ul.lastChild.style.visibility = "visible";
	}
}

dojo.event.connect(dojo.hostenv, "loaded", "initSliderEvents");
