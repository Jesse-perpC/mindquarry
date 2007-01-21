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
 
dojo.provide("mindquarry.widget.TableExpandLink");

dojo.require("dojo.widget.*");
dojo.require("dojo.html");
dojo.require("dojo.event");

dojo.widget.tags.addParseTreeHandler("dojo:TableExpandLink");
dojo.widget.manager.registerWidgetPackage("mindquarry.widget");
mindquarry.widget.TableExpandLink = function() {
	dojo.widget.DomWidget.call(this);
}


dojo.inherits(mindquarry.widget.TableExpandLink, dojo.widget.DomWidget);

dojo.lang.extend(mindquarry.widget.TableExpandLink, {
	widgetType: "TableExpandLink",
	isContainer: true,
  tbody: null,
  contextrow: null,
	
	buildRendering: function(args, parserFragment, parentWidget) {
        // Magical statement to get the dom node, stolen in DomWidget
	    this.domNode = parserFragment["dojo:"+this.widgetType.toLowerCase()].nodeRef;
      
      var context = this.domNode;

      while (context.parentNode) {
        context = context.parentNode;
        if (context.nodeName=="TR" && this.contextrow==null) {
          this.contextrow = context;
        }
        if (context.nodeName=="TBODY" && this.tbody==null) {
          this.tbody = context;
          break;
        }
      }

	    dojo.event.connect(this.domNode, "onclick", this, "onClick");
    },
    
    onClick: function(event) {
        event.preventDefault();
        //alert(this.tbody);
        return false;
    }
	
});