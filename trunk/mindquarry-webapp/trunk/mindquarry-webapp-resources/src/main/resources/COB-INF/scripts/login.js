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
	var lastfoo = false;
    var anchors = document.getElementsByTagName("a");
    for (var foo = 0; foo < anchors.length; foo++) {
        if (anchors[foo].className == "httpauth") {
        	lastfoo = foo;
        }
    }
    if (lastfoo) {
    	createForm(anchors[lastfoo]);
    }
});

function createForm(httpauth)
{
    var form = document.createElement("form");
    form.action = httpauth.href;
    form.method = "get";
    form.onsubmit = login;
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
    var logoutLink = document.createElement("input");
    logoutLink.type = "reset"
    logoutLink.href = "#";
    logoutLink.onclick = logout;
    logoutLink.value ="Log out";
    logoutLink.classname = "logout";
    logoutLink.id = httpauth.id + "-reset";
    form.appendChild(logoutLink);
    httpauth.parentNode.replaceChild(form, httpauth);
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

function login()
{
    var username = document.getElementById(this.id + "-username").value;
    var password = document.getElementById(this.id + "-password").value;
    var http = getHTTPObject();
    http.open("get", this.action, false, username, password);
    http.send("");
	if (http.status == 200) {
		document.location = this.action;
	} else {
        alert("Incorrect username and/or password!");
    }
    return false;
}

function logout()
{
    var http = getHTTPObject();
    http.open("get", this.parentNode.action, false, "null", "null");
    http.send("");
    alert("You have been logged out.");
    return false;
}
