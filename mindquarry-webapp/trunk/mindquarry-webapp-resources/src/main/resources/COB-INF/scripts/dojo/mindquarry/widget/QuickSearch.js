 /*
    QuickSearch - extends HtmlWidget.
    Provides a search field with popup results.
 */
 
dojo.provide("mindquarry.widget.QuickSearch");
dojo.require("dojo.widget.HtmlWidget");

dojo.widget.defineWidget(
	"mindquarry.widget.QuickSearch",
	dojo.widget.HtmlWidget,
	{

		widgetType: "QuickSearch",
		isContainer: false,
		templateString: '<div class="mindquarry-quicksearch" style="position:relative;">' +
											'<div class="search" style="position:relative;">' +
												'<form dojoAttachPoint="formNode">' +
													'<input name="q" size="20" dojoAttachPoint="inputNode" />' +
													'<input type="button" dojoOnClick="buttonClick;" value="GO" />' +
												'</form>' +
											'</div>' +
											'<div style="position:relative;">' +
												'<div class="results" dojoAttachPoint="resultNode" style="background:white;padding:2px;font:10px sans-serif;border:1px solid black;position:absolute;top:0;left:0"></div>' + 
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
					method: "post",
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
			dojo.debug("QuickSearch- got result: " + data);
			dojo.html.show(this.resultNode);
			var status = document.createElement("div");
			status.appendChild(document.createTextNode("Results: " + data.response.numFound))
			this.resultNode.appendChild(status);
			if (data.response.numFound > 0) {
				var results = document.createElement("table");
				var resultsbody = document.createElement("tbody");
				results.appendChild(resultsbody);
				for (var type in data.docs) {
					var typerow = document.createElement("tr");
					typerow.setAttribute("valign", "top")
					var typecell = document.createElement("td");
					typecell.appendChild(document.createTextNode(type));
					var rescell = document.createElement("td");
					for (var hit in data.docs[type]) {
						var a = document.createElement("a");
						var br = document.createElement("br");
						a.setAttribute("href", data.docs[type][hit].url);
						a.appendChild(document.createTextNode(data.docs[type][hit].title));
						rescell.appendChild(a);
						rescell.appendChild(br);
					}
					typerow.appendChild(typecell);
					typerow.appendChild(rescell);
					resultsbody.appendChild(typerow);
				}
				this.resultNode.appendChild(results);
			}
		}
	}
);
