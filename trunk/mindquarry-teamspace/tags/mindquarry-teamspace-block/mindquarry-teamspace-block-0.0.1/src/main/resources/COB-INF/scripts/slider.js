dojo.require("dojo.io.BrowserIO");
dojo.require("dojo.event.*");
dojo.require("dojo.html");

/* doing some init stuff */
initSliderEvents = function() {
	slidingLists = dojo.html.getElementsByClassName("members");

	// add prev & next buttons to all member lists
	for (i=0; i<slidingLists.length; i++) {
		var next = document.createElement('a');
		next.href = "#";
		next.title="Click here to see next project members";
		next.className = "slidernext";
		next.innerHTML = "next";
		
		var prev = document.createElement('a');
		prev.href = "#";
		prev.title="Click here to see previous project members"
		prev.className = "sliderprev";
		prev.innerHTML = "prev";
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
}

/* slide to next image */
doNext = function(event) {
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
