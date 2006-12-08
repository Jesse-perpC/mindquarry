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
		
		// config attributes, the text could be supplied via i18n on the tag
		url: "", // the url to access to perform searches (required, set via this attr on the tag)
		maxheight: 300, // the maximum height of the results popup
		size: "20", // the size of the search input field
		searchButton: "Search", // text of the search button
		searchingStatus: "Searching ...",	// shown while searching
		noResultsStatus: "Search finished without any results.", // shown if there are no results
		gotResultsStatus: "Search finished with %{count} results :", // shown if there are results

		templateString: '<div style="position:relative;" class="mindquarry-quicksearch">' + 
			'<div class="search-field">' +
				'<form dojoAttachPoint="formNode">' +
					'<span dojoAttachPoint="widthNode">' + 
						'<input name="q" size="${this.size}"/>' +
						'<input name="wt" value="mq" type="hidden"/>' +
						'<input name="fl" value="score" type="hidden"/>' +
						'<input type="button" dojoOnClick="searchClick;" value="${this.searchButton}"/>' + 
					'</span>' +
				'</form>' +
			'</div>' +
			'<div style="position:relative;">' +
					'<div class="result-popup" dojoAttachPoint="popupNode" style="position:absolute;top:0;left:0;overflow:auto;padding:5px;">' +
						'<a dojoOnClick="closeClick;" href="#" style="background:url(\'../buttons/close.png\');display:block;height:10px;width:10px;float:right;"></a>' 	+
						'<span class="result-status" dojoAttachPoint="resultStatus" style="margin-bottom:8px;"></span>' +
						'<table width="100%" class="result-table"><tbody dojoAttachPoint="resultNode"></tbody></table>' 	+
					'</div>' + 
			'</div>' +
		'</div>',
		
		typeTemplate: '<tr valign="top">' +
										'<td width="25%" align="right" class="task-column">%{type}</td>' + 
										'<td class="file-column"><table width="100%"><tbody>%{hits}</tbody></table></td>' + 
									'</tr>',
									
		hitTemplate: '<tr valign="top"><td width="32"><div style="width:30px;height:12px;border:1px solid #666;position:relative;">' + 
									 '<div style="background:#ddf;height:10px;width:%{score}%;margin:1px 0;padding:0;"> </div>' + 
									 '<div style="position:absolute;top:0;right:0;margin:0;padding:0;color:#444;font-size:10px;">%{score}</div>' + 
								 '</div></td>' +
								'<td><a href="%{uri}">%{title}</a></td></tr>',
		
		// template nodes
		formNode: null,      // the search form
		widthNode: null,     // the node to use to measure the width of the form
		popupNode: null,     // the results popup container
		resultNode: null,    // the results container
		resultStatus: null,  // the results status node
		
		// instance properties
		_busy: false,

		// widget interface
		postCreate: function(){
			dojo.html.hide(this.popupNode);
			this.popupNode.style.width = dojo.style.getOuterWidth(this.widthNode) + "px";
		},
		
		// search button click handler
		searchClick: function(evt) {
			dojo.debug("QuickSearch - starting search");
			
			if (this._busy) return; // only one request at a time
			this._busy = true;
			this._setStatus(this.searchingStatus);
			dojo.dom.removeChildren(this.resultNode); // clear any rows from the resultsNode
			dojo.html.show(this.popupNode);
			
			dojo.io.bind({
				url: this.url,
				mimetype: "text/json",
				formNode: this.formNode,
				method: "get",
				handle: dojo.lang.hitch(this, function(type, data, evt) {
					if (type == "load") {
						if (!data) return;
						this.update(data);
						this._busy = false;
					} else if (type == "error") {
						dojo.debug("QuickSearch - status request failed");
					}           
				})
			});
		},

		// handle clicks on the close button
		closeClick: function(evt) {
		  dojo.html.hide(this.popupNode);
        },
		
		// set the status area
		_setStatus: function(status) {
			this.resultStatus.innerHTML = status;
		},
		
		// use a pair of mini templates to build the results
		_buildResults: function(docs) {
		    var types = [];
			for (var type in docs) {
				var hits = [];
				for (var hit in docs[type]) {
					hits.push(
					    dojo.string.substituteParams(this.hitTemplate, 
					    {
					        uri: docs[type][hit].location, 
					        title: docs[type][hit].title || "untitled", 
					        score: docs[type][hit].score
					    }
					));
				}
				types.push(dojo.string.substituteParams(this.typeTemplate, {type: type, hits: hits.join("")})); 
			}
			return types.join("");
		},
		
		// update the results display
		update: function(data) {
			dojo.debug("QuickSearch - got results: " + data.response.numFound);
						
			if (data.response.numFound > 0) {
			    this._setStatus(dojo.string.substituteParams(this.gotResultsStatus, {count:data.response.numFound}));
			    this.resultNode.innerHTML = this._buildResults(data.response.docs);
				
				if (dojo.style.getOuterHeight(this.resultNode) > this.maxheight) {
					this.popupNode.style.height = this.maxheight + "px"; 					
				}
			} else {
				this._setStatus(this.noResultsStatus);
			}
		}
	}
);
