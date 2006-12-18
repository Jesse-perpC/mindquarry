dojo.provide("mindquarry.widget.ChangePassword");

dojo.require("dojo.widget.*");
dojo.require("dojo.html");
dojo.require("dojo.event");

dojo.widget.tags.addParseTreeHandler("dojo:ChangePassword");
dojo.widget.manager.registerWidgetPackage("mindquarry.widget");

mindquarry.widget.ChangePassword = function() {
	dojo.widget.DomWidget.call(this);	
	var cform = null;
}

dojo.inherits(mindquarry.widget.ChangePassword, dojo.widget.DomWidget);

dojo.lang.extend(mindquarry.widget.ChangePassword, {
	widgetType: "ChangePassword",
	isContainer: true,
	    
    postCreate: function(event) {
    	changePasswordInBrowser();
    }
	
});


function changePasswordInBrowser() {
	
	var newPwdInputId = "changePassword.newPassword:input";
	var newPwd = document.getElementById(newPwdInputId).value;

	
	var username = document.getElementById("edit-user-link").firstChild.nodeValue;
	
	// remember current location (page to edit user settings) in temp variable
	//var editUserSite = window.location;
	
    var pathToWebappRoot = "http://localhost:8888/"; //document.getElementById("path-to-webapp-root").href;
	
	var forceLogoutRequest = getHTTPObject();
	forceLogoutRequest.open("get", pathToWebappRoot, false, "null", "null");
	forceLogoutRequest.send(null);
	
	var reloginRequest = getHTTPObject();
	forceLogoutRequest.open("get", pathToWebappRoot, false, username, newPwd);
	forceLogoutRequest.send(null);
	
	
	//window.location = editUserSite;
}

/*
// Onload, make all links that need to trigger a lightbox active
function initializeChangePasswordWidget(){
	dojo.widget.defineWidget(
		// widget name and class
		"ChangePassword",
	
		// superclass
		dojo.widget.HtmlWidget,

		{
			postCreate: function() {
				changePasswordInBrowser();
			}
		}
	);
} */

//dojo.event.connect(dojo.hostenv, "loaded", "initializeChangePasswordWidget");