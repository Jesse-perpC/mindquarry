function sendJNLP() {
	var parameterMap = { 
		"username" : cocoon.parameters["username"], 
		"endpoint" : cocoon.parameters["endpoint"],
		"ctxPath" : cocoon.parameters["ctxPath"]
	}
	
	var target = cocoon.parameters["target"];
    cocoon.sendPage(target, parameterMap);
}
