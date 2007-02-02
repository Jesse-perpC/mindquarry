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
 
dojo.require("dojo.event");
dojo.require("dojo.widget.Editor2");

dojo.require("cocoon.forms.CFormsForm");

// we want to replace the existing submit() method of CFormsForm with
// a wrapper that handles special issues and then calls the original
// submit(). therfore we store the original one and use it under a different
// name (_renamedOldSubmit()) in the extension of the CFormsForm below
var originalFormSubmitMethod = cocoon.forms.CFormsForm.prototype.submit;

//copy the html content from the dojo editor2 to the
//wrapped textarea before submitting a form
dojo.lang.extend(cocoon.forms.CFormsForm, {
	
    // use the original submit method/function prototype and register it under
    // a new name in the same CFormsForm class
    _renamedOldSubmit: originalFormSubmitMethod,
    
    // overwrite the existing submit() method so that we can do additional stuff
    // before calling the original one via _renamedOldSubmit()
    submit: function(name, params) {
		this.prepareEditor2WidgetsForSubmit();
		
    	this._renamedOldSubmit(name, params);
    },
    
    prepareEditor2WidgetsForSubmit: function() {
    	// grab all editor2 widgets...
    	var editor2Widgets = dojo.widget.byType("Editor2");
    	if (editor2Widgets != null) {
	        dojo.lang.forEach(editor2Widgets,
	        	function(ed2) {
	        		// ...and update the value of the forms widget with the
	        		// current content of the editor2 "textarea" so that the
	        		// text is included in the form parameters that are sent
	        		var editor2DomNode = dojo.byId(ed2.widgetId);
	        		if (editor2DomNode != null) { 
						editor2DomNode.value = ed2.getEditorContent();
	        		}
				}
	        );
    	}
    }
});
