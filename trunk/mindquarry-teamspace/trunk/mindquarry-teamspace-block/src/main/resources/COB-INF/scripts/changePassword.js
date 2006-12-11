
function changePasswordInBrowser() {
	
	var newPwdInputId = "changePassword.new_password:input"
	var newPwd = document.getElementById(newPwdInputId).value;
	
	var username = document.getElementById("edit-user-link").firstChild.nodeValue;
		
	// remember current location (page to edit user settings) in temp variable
//	var editUserSite = window.location;
	
    var pathToWebappRoot = document.getElementById("path-to-webapp-root").value;
	
	var forceLogoutRequest = getHTTPObject();
	forceLogoutRequest.open("get", pathToWebappRoot, false, "null", "null");
	forceLogoutRequest.send(null);
	
	var reloginRequest = getHTTPObject();
	forceLogoutRequest.open("get", pathToWebappRoot, false, username, newPwd);
	forceLogoutRequest.send("");
	
	
//	window.location = editUserSite;
}