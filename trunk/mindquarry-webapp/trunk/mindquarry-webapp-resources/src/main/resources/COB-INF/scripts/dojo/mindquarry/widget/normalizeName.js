
dojo.provide("mindquarry.widget.normalizeName");

// filters the provided name to exclude illegal characters
function normalizeName(name) {
	var legal = "0123456789_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"; // characters to keep
	var result = [];
	
	name = name.split(" ").join("_"); // convert spaces to underscore
	
	for (var i = 0; i < name.length; i++) {
		var ch = name[i];
		if (legal.indexOf(ch) != -1) result.push(ch);
	}
	return result.join("");
}