
dojo.require("dojo.io.BrowserIO");
dojo.require("dojo.event.*");
dojo.require("dojo.html");
dojo.require("dojo.fx.html");

/* doing some init stuff */
initEvents = function() {
	slidingLists = dojo.html.getElementsByClassName("members");

	for (i=0;i<slidingLists.length;i++) {
		next = document.createElement('a');
		next.href = "#";
		next.innerHTML = "next";
		prev = document.createElement('a');
		prev.href = "#";
		prev.innerHTML = "prev";
		
		slidingLists[i].appendChild(next);
		slidingLists[i].insertBefore(prev, slidingLists[i].firstChild);
		
		dojo.event.connect(next, "onclick", "doNext");
		dojo.event.connect(prev, "onclick", "doPrevious");
	}
}

/* slide to next image */
doNext = function(event)	{
	var a = event.target;
	var ul = a.parentNode;
	var first = null;
	var last = null;
	var lis = ul.getElementsByTagName("li");
	for (i=0;i<lis.length;i++) {
		var li = lis[i];
		if (li.style.display!='none'&&first==null) {
			first = li;
		}
		if (li.style.display=='none'&&last==null&&first!=null) {
			last = li;
		}
	}
	if (first!=null&&last!=null) {
//		dojo.fx.html.fadeOut(first, 300, function(){});}
		first.style.display = 'none';last.style.display = 'block';
	}
}

/* slide to previous image */
doPrevious = function(event)	{
	var a = event.target;
	var ul = a.parentNode;
	var first = null;
	var last = null;
	var lis = ul.getElementsByTagName("li");
	for (i=lis.length-1;i>=0;i--) {
		var li = lis[i];
		if (li.style.display!='none'&&first==null) {
			first = li;
		}
		if (li.style.display=='none'&&last==null&&first!=null) {
			last = li;
		}
	}
	if (first!=null&&last!=null) {
		//dojo.fx.html.fadeOut(first, 300, function(){});}
		first.style.display = 'none';last.style.display = 'block';
	}
}

dojo.event.connect(dojo.hostenv, "loaded", "initEvents");