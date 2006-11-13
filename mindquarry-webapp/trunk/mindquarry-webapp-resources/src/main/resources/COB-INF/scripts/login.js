/*function logout(target) {
	try{
	  var agt=navigator.userAgent.toLowerCase();
	  if (agt.indexOf("msie") != -1) {
	    // IE clear HTTP Authentication
	    document.execCommand("ClearAuthenticationCache");
	  } else {
	    var xmlhttp = createXMLObject();
	    xmlhttp.open("GET",target.href,false,"nouser","nopassword");
	    xmlhttp.send("");
	    xmlhttp.abort();
	  }
	//  window.location = "/rest/";
	} catch(e) {
		// There was an error
		alert("there was an error");
	}
	return false;
}*/

function createXMLObject() {
	try {
		if (window.XMLHttpRequest) {
			xmlhttp = new XMLHttpRequest();
		}
		// code for IE
		else if (window.ActiveXObject) {
			xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}
	} catch (e) {
		xmlhttp=false
	}
	return xmlhttp;
}

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
    	createLogoutForm(anchors[index]);
    }
});

function createLoginForm(httpauth)
{
    var form = document.createElement("form");
    form.action = httpauth.href;
    form.method = "get";
    form.onsubmit = function() {login(form); return false;};
    form.id = httpauth.id;

    var username = document.createElement("label");
    var usernameInput = document.createElement("input");
    usernameInput.name = "username";
    usernameInput.type = "text";
    usernameInput.id = httpauth.id + "-username";
    username.appendChild(document.createTextNode("Username"));
    username.appendChild(usernameInput);
    
    var password = document.createElement("label");
    var passwordInput = document.createElement("input");
    passwordInput.name = "password";
    passwordInput.type = "password";
    passwordInput.id = httpauth.id + "-password";
    password.appendChild(document.createTextNode("Password"));
    password.appendChild(passwordInput);
    
    var submit = document.createElement("input");
    submit.type = "submit";
    submit.value = "Log in";
    submit.id = httpauth.id + "-submit";
    form.appendChild(username);
    form.appendChild(password);
    form.appendChild(submit);

    httpauth.parentNode.replaceChild(form, httpauth);
}

function createLogoutForm(httplogout)
{
	var logoutDiv = document.createElement("div");
    logoutDiv.onclick = logout;
    logoutDiv.id = "http-logout-link";
	logoutDiv.innerHTML = "Log out";
	
    httplogout.appendChild(logoutDiv);
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
    http.open("get", "/?request=login", false, username, password);    
    http.send(null);
    
	if (http.status == 200) {
		document.location = form.action.split("targetUri=")[1];
	} else {
        alert("Incorrect username and/or password!");
    }
}

function logout()
{
    var http = getHTTPObject();
    http.open("get", "/", false, "null", "null");
    http.send(null);
    document.location = "/logoutpage";
    return false;
}
