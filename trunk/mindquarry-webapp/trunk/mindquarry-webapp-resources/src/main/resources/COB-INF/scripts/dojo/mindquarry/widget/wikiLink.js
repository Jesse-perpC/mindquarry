
dojo.provide("mindquarry.widget.wikiLink");
dojo.require("mindquarry.widget.normalizeName");



// setup the namespace
var mindquarry = mindquarry || {};
mindquarry.widget = mindquarry.widget || {};


// makes a link out of the selection in the Wiki Editor, from a list of available wiki pages in this teamspace
mindquarry.widget.wikiLinkListClick = function() {			
	var editor = dojo.widget.byType("Editor2")[0];	
	var range;
	if(editor.document.selection){
		range = editor.document.selection.createRange().text;
	}else if(dojo.render.html.mozilla){
		range = editor.window.getSelection().toString();
	}
	if(range.length){
		var wikiLinkDialog = dojo.widget.byId("wikiLinkDialog");
		var wikiLinkDialogOKBtn = document.getElementById("wikiLinkDialogOK");
		var wikiLinkDialogCancelBtn = document.getElementById("wikiLinkDialogCancel");
		wikiLinkDialog.setCloseControl(wikiLinkDialogOKBtn);
		wikiLinkDialog.setCloseControl(wikiLinkDialogCancelBtn);
		wikiLinkDialog.show();
	} else {
		alert("Please select text to link");
	}
}

// makes a link out of the selection in the Wiki Editor, using the selection text as the href
mindquarry.widget.wikiLinkWordClick = function() {			
	var editor = dojo.widget.byType("Editor2")[0];	
	var range;
	if(editor.document.selection){
		range = editor.document.selection.createRange().text;
	}else if(dojo.render.html.mozilla){
		range = editor.window.getSelection().toString();
	}
	if(range.length){
		this.wikiLinkSet(range);
	}else{
		alert("Please select text to link");
	}
}

mindquarry.widget.wikiLinkSet = function(href) {
	var editorToolbar = dojo.widget.byType("Editor2Toolbar")[0];
	href = this.normalizeName(href);
	if (href) {
		editorToolbar.exec("createlink", href);
	}
}
