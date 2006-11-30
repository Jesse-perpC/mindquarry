
dojo.provide("mindquarry.widget.wikiLink");
dojo.require("mindquarry.widget.normalizeName");

// makes a link out of the selection in the Wiki Editor, from a list of available wiki pages in this teamspace
function wikiLinkListClick() {			
	var _editor = dojo.widget.byType("Editor2")[0];	
	var range;
	if(_editor.document.selection){
		range = _editor.document.selection.createRange().text;
	}else if(dojo.render.html.mozilla){
		range = _editor.window.getSelection().toString();
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
function wikiLinkWordClick() {			
	var _editor = dojo.widget.byType("Editor2")[0];	
	var range;
	if(_editor.document.selection){
		range = _editor.document.selection.createRange().text;
	}else if(dojo.render.html.mozilla){
		range = _editor.window.getSelection().toString();
	}
	if(range.length){
		wikiLinkSet(range);
	}else{
		alert("Please select text to link");
	}
}

function wikiLinkSet(href) {
	var _editorToolbar = dojo.widget.byType("Editor2Toolbar")[0];
	href = normalizeName(href);
	if (href) {
		_editorToolbar.exec("createlink", href);
	}
}
