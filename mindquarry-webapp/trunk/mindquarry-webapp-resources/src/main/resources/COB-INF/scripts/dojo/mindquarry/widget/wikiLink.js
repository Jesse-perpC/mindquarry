/*
 * Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 */
 

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
	// normalize only if the entered link does not start with "http(s)://"
	if (href && (href.indexOf("http://") != 0) && (href.indexOf("https://") != 0)) {
    	href = this.normalizeName(href);
    }
	if (href) {
		editorToolbar.exec("createlink", href);
	}
}
