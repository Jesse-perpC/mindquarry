/*
Created By: Chris Campbell
Website: http://particletree.com
Date: 2/1/2006

Adapted By: Simon de Haan
Website: http://blog.eight.nl
Date: 21/2/2006

Inspired by the lightbox implementation found at http://www.huddletogether.com/projects/lightbox/
And the lightbox gone wild by ParticleTree at http://particletree.com/features/lightbox-gone-wild/

*/
dojo.require("dojo.event");
dojo.require("cocoon.ajax.common");
dojo.require("cocoon.ajax.insertion");
dojo.require("dojo.lfx.html");
/*-------------------------------GLOBAL VARIABLES------------------------------------*/

var detect = navigator.userAgent.toLowerCase();
var OS,browser,version,total,thestring;
var lightboxnodes = new Array();

/*-----------------------------------------------------------------------------------------------*/

//Browser detect script origionally created by Peter Paul Koch at http://www.quirksmode.org/

function getBrowserInfo() {
	if (checkIt('konqueror')) {
		browser = "Konqueror";
		OS = "Linux";
	}
	else if (checkIt('safari')) browser 	= "Safari"
	else if (checkIt('omniweb')) browser 	= "OmniWeb"
	else if (checkIt('opera')) browser 		= "Opera"
	else if (checkIt('webtv')) browser 		= "WebTV";
	else if (checkIt('icab')) browser 		= "iCab"
	else if (checkIt('msie')) browser 		= "Internet Explorer"
	else if (!checkIt('compatible')) {
		browser = "Netscape Navigator"
		version = detect.charAt(8);
	}
	else browser = "An unknown browser";

	if (!version) version = detect.charAt(place + thestring.length);

	if (!OS) {
		if (checkIt('linux')) OS 		= "Linux";
		else if (checkIt('x11')) OS 	= "Unix";
		else if (checkIt('mac')) OS 	= "Mac"
		else if (checkIt('win')) OS 	= "Windows"
		else OS 								= "an unknown operating system";
	}
}

function checkIt(string) {
	place = detect.indexOf(string) + 1;
	thestring = string;
	return place;
}

/*-----------------------------------------------------------------------------------------------*/


var MyClass = {
	create: function() {
		return function() {
			this.initialize.apply(this, arguments);
		}
	}
}

var lightbox = MyClass.create();

function handleClick(event) {
	alert("click");
}

lightbox.prototype = {

	yPos : 0,
	xPos : 0,

	initialize: function(ctrl) {
		this.content = ctrl.href;
		ctrl.onclick = function(){return false;};
		dojo.event.connect(ctrl, "onclick", this, "activate");
	},
	
	// Turn everything on - mainly the IE fixes
	activate: function() {
		if (browser == 'Internet Explorer'){
			this.getScroll();
			this.prepareIE('100%', 'hidden');
			this.setScroll(0,0);
			this.hideSelects('hidden');
		}
		this.displayLightbox("block");
	},
	
	// Ie requires height to 100% and overflow hidden or else you can scroll down past the lightbox
	prepareIE: function(height, overflow){
		bod = document.getElementsByTagName('body')[0];
		bod.style.height = height;
		bod.style.overflow = overflow;
  
		htm = document.getElementsByTagName('html')[0];
		htm.style.height = height;
		htm.style.overflow = overflow; 
	},
	
	// In IE, select elements hover on top of the lightbox
	hideSelects: function(visibility){
		selects = document.getElementsByTagName('select');
		for(i = 0; i < selects.length; i++) {
			selects[i].style.visibility = visibility;
		}
	},
	
	// Taken from lightbox implementation found at http://www.huddletogether.com/projects/lightbox/
	getScroll: function(){
		if (self.pageYOffset) {
			this.yPos = self.pageYOffset;
		} else if (document.documentElement && document.documentElement.scrollTop){
			this.yPos = document.documentElement.scrollTop; 
		} else if (document.body) {
			this.yPos = document.body.scrollTop;
		}
	},
	
	setScroll: function(x, y){
		window.scrollTo(x, y); 
	},
	
	displayLightbox: function(display) {
		var overlay = document.getElementById('overlay');
		overlay.style.display = display;
		dojo.event.connect(overlay, "onclick", this, "deactivate");
		var hashIndex = this.content.indexOf("#");
		if(hashIndex!=-1) {
			var contentId = this.content.substr(hashIndex+1);
			var contentHolder = document.getElementById(contentId);
			placeholder = document.getElementById('lightboxplaceholder');
			placeholder.style.display = display;
			dojo.dom.removeChildren(placeholder);
			dojo.dom.copyChildren(contentHolder, placeholder, false);
		}
		else {
			placeholder = document.getElementById('lightboxplaceholder');
			cocoon.ajax.update(this.content + "?lightbox-request=true", placeholder, "insert");
			//placeholder.style.visibility = "visible";
			placeholder.style.display = display;
			//dojo.lfx.html.fadeShow(placeholder, 1000).play();
			cocoon.ajax.insertionHelper.parseDojoWidgets(placeholder);
		}
		if(display != 'none') this.actions();		
		
		return false;
	},
	
	// TODO: Search through new links within the lightbox, and attach click event
	actions: function(){

	},
	
	// Example of creating your own functionality once lightbox is initiated
	deactivate: function(){
		if (browser == "Internet Explorer"){
			this.setScroll(0,this.yPos);
			this.prepareIE("auto", "auto");
			this.hideSelects("visible");
		}
		this.displayLightbox("none");
	}
}

/*-----------------------------------------------------------------------------------------------*/

// Onload, make all links that need to trigger a lightbox active
function initialize(){
	addLightboxMarkup();
	lboxcandidates = document.getElementsByTagName('a');
	for(i = 0; i < lboxcandidates.length; i++) {
		if (lboxcandidates[i].getAttribute('rel')=='lightbox') {
			//do not create two lightboxes for one element
			if (!dojo.lang.inArray(lightboxnodes, lboxcandidates[i])) {
				valid = new lightbox(lboxcandidates[i]);
				lightboxnodes[lightboxnodes.length] = lboxcandidates[i];
			}
		}
	}
}

// Add in markup necessary to make this work. Basically two divs:
// Overlay holds the shadow
// Lightbox is the centered square that the content is put into.
function addLightboxMarkup() {
	if (document.getElementById('overlay')==null) {
		bod 				= document.getElementsByTagName('body')[0];
		overlay 			= document.createElement('div');
		overlay.id			= 'overlay';
		lightboxplaceholder 			= document.createElement('div');
		lightboxplaceholder.id			= 'lightboxplaceholder';
		bod.appendChild(overlay);
		bod.appendChild(lightboxplaceholder);
	}
}
/*
overwriting dojo function
*/
cocoon.ajax.insertionHelper.parseDojoWidgets = function(element) {
		initialize();
	    // Find a parent widget (if any) so that Dojo can maintain its widget tree
	    var parentWidget = this.findParentWidget(element);
		var parser = new dojo.xml.Parse();

		// FIXME: parser.parseElement() parses the _children_ of an element, whereas we want here
		// the element itself to be parsed. Parsing its parent is not an option as we don't want
		// to parse the siblings. So place it in a temporary div that we'll trash afterwards.
		var div = document.createElement("DIV");
		element.parentNode.replaceChild(div, element);
		div.appendChild(element);
		var frag = parser.parseElement(div, null, true);
		dojo.widget.getParser().createComponents(frag, parentWidget);
		// Get again the first child of the div, which may no more be the original one
		// if it's a widget
		element = div.firstChild;
		div.parentNode.replaceChild(element, div);
		parentWidget && parentWidget.onResized();

		return element;
	}

dojo.event.connect(dojo.hostenv, "loaded", "initialize");
dojo.event.connect(dojo.hostenv, "loaded", "getBrowserInfo");