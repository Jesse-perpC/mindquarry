/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
 
 dojo.addOnLoad(function()
{	
	var index = -1;
    var anchors = document.getElementsByTagName("a");
    for (var i = 0; i < anchors.length; i++) {
        if (anchors[i].className == "httpauth") {
        	index = i;
        }
    }
    if (index >= 0) {
    	createLoginForm(anchors[index]);
    }

	index = -1;
    anchors = document.getElementsByTagName("div");
    for (var i = 0; i < anchors.length; i++) {
        if (anchors[i].className == "http-logout") {
        	index = i;
        }
    }
    if (index >= 0) {
    	createLogoutLink(anchors[index]);
    }
});

function createLoginForm(httpauth)
{
    var form = document.createElement("form");
    form.action = httpauth.href;
    form.method = "get";
    form.onsubmit = function() {login(form); return false;};
    form.id = httpauth.id;

    var table = document.createElement("table");
    table.id = httpauth.id + "-table";
    
    // IE needs a tBody element to recognize the table created here!
    var tBody = document.createElement("tBody");
    table.appendChild(tBody);

    var row1 = document.createElement("tr");
    tBody.appendChild(row1);
    var cell11 = document.createElement("td");
    row1.appendChild(cell11);
    var cell12 = document.createElement("td");
    row1.appendChild(cell12);

    var row2 = document.createElement("tr");
    tBody.appendChild(row2);
    var cell21 = document.createElement("td");
    row2.appendChild(cell21);
    var cell22 = document.createElement("td");
    row2.appendChild(cell22);

    var row3 = document.createElement("tr");
    tBody.appendChild(row3);
    var cell31 = document.createElement("td");
    row3.appendChild(cell31);
    var cell32 = document.createElement("td");
    row3.appendChild(cell32);

    var usernameLabel = document.createElement("label");
    usernameLabel.htmlFor = httpauth.id + "-username";
    usernameLabel.appendChild(document.createTextNode("Username"));

    var usernameInput = document.createElement("input");
    usernameInput.name = "username";
    usernameInput.type = "text";
    usernameInput.id = httpauth.id + "-username";
    
    var passwordLabel = document.createElement("label");
    passwordLabel.htmlFor = httpauth.id + "-password";
    passwordLabel.appendChild(document.createTextNode("Password"));

    var passwordInput = document.createElement("input");
    passwordInput.name = "password";
    passwordInput.type = "password";
    passwordInput.id = httpauth.id + "-password";
    
    var submit = document.createElement("input");
    submit.type = "submit";
    submit.value = "Log in";
    submit.id = httpauth.id + "-submit";

    cell11.appendChild(usernameLabel);
    cell12.appendChild(usernameInput);

    cell21.appendChild(passwordLabel);
    cell22.appendChild(passwordInput);
    
    cell32.appendChild(submit);

    form.appendChild(table);

    httpauth.parentNode.replaceChild(form, httpauth);
}

function createLogoutLink(httplogout)
{
/*
	var logoutDiv = document.createElement("div");
    logoutDiv.onclick = function() {logout();};
    logoutDiv.id = "http-logout-link";
	logoutDiv.innerHTML = "Log out";
	
    httplogout.appendChild(logoutDiv);
*/
    httplogout.onclick = function() {logout();};
    httplogout.id = "http-logout-link";
	httplogout.innerHTML = "Log out";
}

function getHTTPObject() {
	var xmlhttp = false;
	if (typeof XMLHttpRequest != 'undefined') {
		try {
			xmlhttp = new XMLHttpRequest();
		} catch (e) {
			xmlhttp = false;
		}
	} else {
        /*@cc_on
        @if (@_jscript_version >= 5)
            try {
                xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
            } catch (e) {
                try {
                    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
                } catch (E) {
                    xmlhttp = false;
                }
            }
        @end @*/
    }
	return xmlhttp;
}

function login(form)
{
    var username = document.getElementById(form.id + "-username").value;
    var password = document.getElementById(form.id + "-password").value;

    var http = getHTTPObject();
    
    // call the special login request page provided by the server that
    // does the authentication and will store the correct credentials in the
    // cache of the browser
    http.open("get", "/?request=login", false, username, password);    
    http.send(null);
    
    // we are not interested in the response, only the status
    
	if (http.status == 200) {
		// go to the page that was requested before the login page came
		document.location = form.action.split("targetUri=")[1];
	} else {
        alert("Incorrect username and/or password!");
    }
}

function logout()
{
    var http = getHTTPObject();
    
    // do a authentication with wrong username / password so that after the
    // not-authorized response from the server the browser will clear those
    // credentials from the cache => we are "logged out"
    http.open("get", "/", false, "null", "null");
    http.send(null);
    
    document.location = "/logoutpage?targetUri=" + window.location.pathname;
}
