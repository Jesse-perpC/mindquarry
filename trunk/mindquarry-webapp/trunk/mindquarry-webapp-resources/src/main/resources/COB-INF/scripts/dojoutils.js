/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
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


// Onload, make all links that need to trigger a lightbox active
function initializeDojoUtils(){
	dojo.lang.forEach(dojo.widget.byType("CFormsForm"), function(form) {
	        dojo.event.disconnect("around", form.domNode, "onsubmit", form, "_browserSubmit");
	        dojo.event.connect("around", form.domNode, "onsubmit", form, "_fixDojoEditor2");
           //dojo.byId(ed.widgetId).value = ed.getEditorContent();
          }
    );
}

dojo.event.connect(dojo.hostenv, "loaded", "initializeDojoUtils");

/*****************************************************************************/
/* javascript object dump function */

var MAX_DUMP_DEPTH = 10;

function dumpObj(obj, name, indent, depth) {
       if (depth > MAX_DUMP_DEPTH) {
              return indent + name + ": <Maximum Depth Reached>\n";
       }
       
       if (obj == null) {
              return indent + name + ": <object is null>\n";
       }

       if (typeof obj == "object") {
              var child = null;
              var output = indent + name + "\n";
              indent += "\t";
              for (var item in obj)
              {
                    try {
                           child = obj[item];
                    } catch (e) {
                           child = "<Unable to Evaluate>";
                    }

                    if (typeof child == "object") {
                           output += dumpObj(child, item, indent, depth + 1);
                    } else {
                           output += indent + item + ": " + child + "\n";
                    }
              }
              return output;

       } else {
              return obj;
       }
}
/*****************************************************************************/

