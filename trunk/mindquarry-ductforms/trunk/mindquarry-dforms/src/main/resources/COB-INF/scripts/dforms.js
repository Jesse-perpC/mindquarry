dojo.require("cocoon.forms.*");

//var djConfig = { isDebug: true };

dojo.event.connect (window, "onload" , function() {
            sayHello();
        });

function sayHello() {
	alert("helloing from dforms");
}
/*
var result = new PopupWindow();
result.forms_onload = function() {
    //alert("hello");
}

forms_onloadHandlers.push(result);
    
var sayHelloFunction = function sayHello() {
	alert("say hello");
}
*/
