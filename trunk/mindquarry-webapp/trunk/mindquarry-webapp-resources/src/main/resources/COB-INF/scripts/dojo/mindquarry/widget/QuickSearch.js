/*
 * QuickSearch - extends HtmlWidget.
 * Provides a search field with popup results.
 */
dojo.provide("mindquarry.widget.QuickSearch");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.event");

dojo.widget.defineWidget(
	"mindquarry.widget.QuickSearch",
	dojo.widget.HtmlWidget,
	{
		widgetType: "QuickSearch",
		isContainer: false,
		templateString: '<div id="quicksearch" class="mindquarry-quicksearch" style="position:relative;">' +
						    '<div class="search" style="position:relative;">' +
							    '<form dojoAttachPoint="formNode">' +
									'<input name="q" size="30" dojoAttachPoint="inputNode" />' +
									'<input name="wt" value="mq" type="hidden"/>' +
									'<input name="fl" value="score" type="hidden"/>' +
									'<input type="button" dojoOnClick="buttonClick;" value="Search" />' +
								'</form>' +
							'</div>' +
							'<div style="position:relative;">' +
							    '<div class="results" dojoAttachPoint="resultNode" style="filter:alpha(opacity=60);opacity:.6;background:#fbf4d4;padding:2px;font:11px sans-serif;border:1px solid grey;position:absolute;top:0;left:0"></div>' + 
							'</div>' +
						'</div>',
		
		// template nodes
		inputNode: null,   // the search input field
		formNode: null,    // the search form
		resultNode: null,  // the results container
		
		// config attributes
		url: "", // the url to access to perform searches (set via this attr on the tag)
		height: "",
		width: "",
		
		// instance properties
		_busy: false,

		// widget interface
		postCreate: function(){
			dojo.html.hide(this.resultNode);
			this.resultNode.style.width = this.width + "px";
			this.resultNode.style.height = this.height + "px";
		},
		
		// search button click handler
		buttonClick: function(evt) {
			dojo.debug("QuickSearch - starting search");
			
			if (this._busy) return; // only one request at a time
			this._busy = true;
			if (this.resultNode) {
				dojo.html.hide(this.resultNode);
				dojo.dom.removeChildren(this.resultNode); // clear any content from the resultsNode
			}
			var self = this;
			
			dojo.io.bind({
				url: this.url,
				mimetype: "text/json",
				formNode: this.formNode,
				method: "get",
				handle: function(type, data, evt) {
					if (type == "load") {
						if (!data) return;
							
						self.update(data);
						self._busy = false;
					} else if (type == "error") {
						dojo.debug("QuickSearch - status request failed");
					}           
				}
			});
		},
		
		// update the results display
		update: function(data) {
			dojo.debug("QuickSearch - got result: " + data);
			dojo.html.show(this.resultNode);
			
			// create close button
			var closeButton = document.createElement("a");
			closeButton.appendChild(document.createTextNode(""))
			closeButton.setAttribute("href", "#");
			closeButton.style.background = "url('../buttons/close.png')";
			closeButton.style.display = "block";
			closeButton.style.height = "10px";
			closeButton.style.width = "10px";
			closeButton.style.cssFloat = "right";
			dojo.event.connect(closeButton, "onclick", this, "closeResults");
			
			this.resultNode.appendChild(closeButton);
			
			// create status info
			var status = document.createElement("div");
			if (data.response.numFound > 0) {
			    status.appendChild(document.createTextNode("Search finished with " + data.response.numFound + " results."));
			} else {
			    status.appendChild(document.createTextNode("Search finished without any results."));
			}
			status.style.marginBottom = "8px";
			this.resultNode.appendChild(status);
			
			if (data.response.numFound > 0) {
				var results = document.createElement("table");
				var resultsbody = document.createElement("tbody");
				results.appendChild(resultsbody);
								
				for (var type in data.response.docs) {
				    var typerow = document.createElement("tr");
					typerow.setAttribute("valign", "top");
										
					var typecell = document.createElement("td");
					typecell.appendChild(document.createTextNode(type + ":"));
					
					var rescell = document.createElement("td");
					for (var hit in data.response.docs[type]) {
						var a = document.createElement("a");
						a.setAttribute("href", data.response.docs[type][hit].location);
						a.appendChild(document.createTextNode(data.response.docs[type][hit].title));
						rescell.appendChild(a);
						
						var score = document.createElement("span");
						score.appendChild(document.createTextNode(" (Score: " + data.response.docs[type][hit].score + ")"));
						rescell.appendChild(score);
						
						var br = document.createElement("br");
						rescell.appendChild(br);
					}
					typerow.appendChild(typecell);
					typerow.appendChild(rescell);
					resultsbody.appendChild(typerow);
				}
				this.resultNode.appendChild(results);
			}
		},
		
		closeResults: function(evt) {
		    dojo.html.hide(this.resultNode);
        }
	}
);
