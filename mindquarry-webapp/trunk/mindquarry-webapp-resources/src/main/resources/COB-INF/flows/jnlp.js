function sendJNLP() {
	var parameterMap = { "username" : cocoon.parameters["username"] }
	
	var target = cocoon.parameters["target"];
    cocoon.sendPage(target, parameterMap);
}
