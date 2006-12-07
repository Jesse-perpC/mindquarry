
function changePasswordInBrowser() {
	
	var newPwdInputId = "changePassword.new_password:input"
	var newPwd = document.getElementById(newPwdInputId).value;
	
	var username = document.getElementById("edit-user-link").firstChild.nodeValue;
		
	
	var editUserSite = "http://localhost:8888/";
	
	var forceLogoutRequest = getHTTPObject();
	forceLogoutRequest.open("get", editUserSite, false, "null", "null");
	forceLogoutRequest.send(null); 
		
	/*
	try {
	    // "ClearAuthenticationCache" is only available in some browsers
	    // including the IE; for eg. Firefox, who cannot handle this command,
	    // we have the try-catch statement
	    
	    // works in IE
	    document.execCommand("ClearAuthenticationCache");
	    
	} catch (e) {
		// other browsers, like Firefox, Safari, etc.
		
	    var forceLogoutRequest = getHTTPObject();
		forceLogoutRequest.open("get", editUserSite, false, "null", "null");
		forceLogoutRequest.send(null);  
	}	*/	
	
	var reloginRequest = getHTTPObject();
	forceLogoutRequest.open("get", editUserSite, false, username, newPwd);
	forceLogoutRequest.send("");
	/*
	window.location = editUserSite;
	*/
}