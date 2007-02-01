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
	
	var username = document.getElementById("userid").firstChild.nodeValue;
	
	
    var pathToWebappRoot = document.getElementById("path-to-webapp-root").href;
	
	var forceLogoutRequest = getHTTPObject();
	forceLogoutRequest.open("get", pathToWebappRoot, false, "null", "null");
	forceLogoutRequest.send(null);
	
	var reloginRequest = getHTTPObject();
	reloginRequest.open("get", pathToWebappRoot, false, username, newPwd);
	reloginRequest.send(null);
}