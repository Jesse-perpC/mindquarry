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
dojo.require("cocoon.ajax.common");
dojo.require("cocoon.ajax.insertion");
dojo.require("dojo.lfx.html");

 
//copy the html content from the dojo editor2 to the
//wrapped textarea before submitting a form
dojo.lang.extend(cocoon.forms.CFormsForm, {
    _fixDojoEditor2: function(invocation) {
        if (invocation.proceed() == false) {
            // onsubmit handlers stopped submission
            return false;
        }

        var event = invocation.args[0] || window.event;
        // Interestingly, FF provides the explicitOriginalTarget property that can avoid
        // grabClickTarget above, but avoid browser specifics for now.
        var target = /*event.explicitOriginalTarget ||*/ this.lastClickTarget;

        dojo.lang.forEach(dojo.widget.byType("Editor2"), function(ed){
           dojo.byId(ed.widgetId).value = ed.getEditorContent();
          }
        );
	
        this.submit(target && target.name);
        
        // If real submit has to occur, it's taken care of in submit()
        return false;
    }    
});


// Onload, make sure _fixDojoEditor2() is registered as around-advice when form.onsubmit() is called
function initializeDojoUtils(){
	dojo.lang.forEach(dojo.widget.byType("CFormsForm"), function(form) {
	        dojo.event.disconnect("around", form.domNode, "onsubmit", form, "_browserSubmit");
	        dojo.event.connect("around", form.domNode, "onsubmit", form, "_fixDojoEditor2");
        }
    );
}

dojo.event.connect(dojo.hostenv, "loaded", "initializeDojoUtils");
