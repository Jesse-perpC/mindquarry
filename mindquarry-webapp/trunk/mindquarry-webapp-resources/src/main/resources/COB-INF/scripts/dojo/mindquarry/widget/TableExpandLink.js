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
dojo.require("cocoon.ajax");
dojo.require("cocoon.ajax.insertion");

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
  table: null,
  rownumber: 0,
  contextrow: null,
  addedrows: new Array(),
  expanded: false,
	
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
        }
        if (context.nodeName=="TABLE" && this.table==null) {
          this.table = context;
        }
      }

	    dojo.event.connect(this.domNode, "onclick", this, "onClick");
    },
    
    onClick: function(event) {
        event.preventDefault();
        var href = event.target.href;
        if (!this.expanded) {
          if(href.indexOf("?") == -1) {
              cocoon.ajax.update(href + "?lightbox-request=true", this.contextrow, this.insertRow);
          } else {
              cocoon.ajax.update(href + "&lightbox-request=true", this.contextrow, this.insertRow);
          }
          this.expanded = true;
          event.target.className = "collapse";
        } else {
          this.expanded = false;
          event.target.className = "expand";
          
          var minindent = this.getIndent(this.contextrow);
          //alert(minindent);
          
          var row = this.contextrow.rowIndex;
          var deletedsome = false;
          for (var i=row+1;i<this.table.rows.length;i++) {
            var rowindent = this.getIndent(this.table.rows[i]);
            if (rowindent > minindent) {
              deletedsome = true;
              this.table.deleteRow(i);
              i--;
            } else if (deletedsome==true) {
              //indicates we could be viewing child nodes of a different folder
              break;
            }
          }
        }
        return false;
    },
    
    getIndent: function(element) {
      var indent = 0;
      var classes = dojo.html.getClasses(element);
      for (var i=0;i<classes.length;i++) {
        if (classes[i].indexOf("indent")==0) {
          var myindent = classes[i].substr(6);
          if (myindent>indent) {
            indent = myindent;
          }
        }
      }
      return indent;
    },
    
    insertRow: function (refElt, content) {
        return cocoon.ajax.insertionHelper.insert(refElt, content, function(refElt, newElt) {
            var table = refElt.parentNode.parentNode;
            var baseindex = refElt.rowIndex + 1;
            var baselink = refElt.getElementsByTagName("a")[0].href;
            var revision = baselink.substr(baselink.lastIndexOf("?revision="));
            var indent = 0;
            var classes = dojo.html.getClasses(refElt);
            for (var i=0;i<classes.length;i++) {
              if (classes[i].indexOf("indent")==0) {
                var myindent = classes[i].substr(6);
                if (myindent>indent) {
                  indent = myindent;
                }
              }
            }
            indent++;
            
            var urlprefix = "url(";
            for (var i=0;i<indent;i++) {
              urlprefix = urlprefix + "../";
            }
            //alert(urlprefix);
            baselink = baselink.substr(0, baselink.indexOf("?revision="));
            
            //var rownumber = 0;
            
            
            for (var i=0;i<newElt.childNodes.length;i++) {
              if (newElt.childNodes[i].nodeName=="DIV") {
                var row = newElt.childNodes[i];
                //alert("inserting " + row.className);
                //var newrowindex = baseindex + i;
                newrow = table.insertRow(baseindex + i);
                newrow.className = row.className + " indent" + indent;
                for (var j=0;j<row.childNodes.length;j++) {
                  var newcell = newrow.insertCell(j);
                  newcell.innerHTML = row.childNodes[j].innerHTML;
                  newcell.className = row.childNodes[j].className;
                  
                  
                  var links = newcell.getElementsByTagName("a");
                  for (var k=0;k<links.length;k++) {
                    links[k].style.backgroundImage = links[k].style.backgroundImage.replace(urlprefix,"url(");
                    var mylink = links[k].href;
                    var folder = false;
                    if (mylink.indexOf("/?")!=-1) {
                       mylink = mylink.substr(0, mylink.lastIndexOf("/?"));
                       folder = true;
                    } else {
                      mylink = mylink.substr(0, mylink.lastIndexOf("?"));
                    }
                    mylink = mylink.substr(mylink.lastIndexOf("/") + 1);
                    if (folder) {
                      mylink = mylink + "/" + revision;
                    }
                    //alert(mylink);
                    links[k].href = baselink + mylink;
                  }
                  var divs = newcell.getElementsByTagName("div");
                  for (var k=0;k<divs.length;k++) {
                    divs[k].style.backgroundImage = divs[k].style.backgroundImage.replace("url(../","url(");
                  }
                  
                  //TODO: parse inserted xml for dojo widgets
                }
                cocoon.ajax.insertionHelper.parseDojoWidgets(newrow);
              }
            }
            
        });
    }
	
});