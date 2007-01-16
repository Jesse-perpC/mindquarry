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

/*
 * Widget SortableHTMLTable
 * 
 * Extension of the SortableTable (dojo 0.3, will be deprecated in 0.5!) that
 * allows the provision of a value field for a table cell that is used for
 * sorting instead of the actual innerHTML, which allows to have unsortable
 * full HTML data type in the cell.
 * 
 * Note: this implementation breaks json-stuff (get/set data programmatically)
 * and selecting rows.
 */ 
dojo.provide("mindquarry.widget.SortableHTMLTable");

dojo.require("dojo.widget.html.SortableTable");
dojo.require("dojo.html");
dojo.require("dojo.event");

dojo.widget.tags.addParseTreeHandler("dojo:SortableHTMLTable");
dojo.widget.manager.registerWidgetPackage("mindquarry.widget");

mindquarry.widget.SortableHTMLTable = function() {
	dojo.widget.html.SortableTable.call(this);
	this.widgetType = "SortableHTMLTable";
	this.sortValueAttribute = "sortValue";
	this.enableAlternateRows = true;
	this.headerSortUpClass="selected-sortup";
	this.headerSortDownClass="selected-sortdown";
}

dojo.inherits(mindquarry.widget.SortableHTMLTable, dojo.widget.html.SortableTable);

dojo.lang.extend(mindquarry.widget.SortableHTMLTable, {
	widgetType: "SortableHTMLTable",
	isContainer: false,
	
	parseColumns:function(/* HTMLTableHeadElement */ node){
		//	summary
		//	parses the passed element to create column objects
		this.reset();
		var row=node.getElementsByTagName("tr")[0];
		var cells=row.getElementsByTagName("td");
		if (cells.length==0) cells=row.getElementsByTagName("th");
		for(var i=0; i<cells.length; i++){
			var o={
				field:null,
				format:null,
				noSort:false,
				sortType:"String",
				dataType:String,   // the data type for the sort value
				contentType:"String", // the type of the actual content
				sortFunction:null,
				label:null,
				align:"left",
				valign:"middle",
				getField:function(){ return this.field||this.label; },
				getType:function(){ return this.dataType; }
			};
			//	presentation attributes
			if(dojo.html.hasAttribute(cells[i], "align")){
				o.align=dojo.html.getAttribute(cells[i],"align");
			}
			if(dojo.html.hasAttribute(cells[i], "valign")){
				o.valign=dojo.html.getAttribute(cells[i],"valign");
			}

			//	sorting features.
			if(dojo.html.hasAttribute(cells[i], "nosort")){
				o.noSort=dojo.html.getAttribute(cells[i],"nosort")=="true";
			}
			if(dojo.html.hasAttribute(cells[i], "sortusing")){
				var trans=dojo.html.getAttribute(cells[i],"sortusing");
				var f=this.getTypeFromString(trans);
				if (f!=null && f!=window && typeof(f)=="function") 
					o.sortFunction=f;
			}

			if(dojo.html.hasAttribute(cells[i], "field")){
				o.field=dojo.html.getAttribute(cells[i],"field");
			}
			if(dojo.html.hasAttribute(cells[i], "format")){
				o.format=dojo.html.getAttribute(cells[i],"format");
			}
			if(dojo.html.hasAttribute(cells[i], "dataType")){
				var sortType=dojo.html.getAttribute(cells[i],"dataType");
				if(sortType.toLowerCase()=="html"||sortType.toLowerCase()=="markup"){
					o.sortType="__markup__";	//	always convert to "__markup__"
					o.noSort=true;
				}else{
					var type=this.getTypeFromString(sortType);
					if(type){
						o.sortType=sortType;
						o.dataType=type;
					}
				}
			}
			if(dojo.html.hasAttribute(cells[i], "contentType")){
				var contentType=dojo.html.getAttribute(cells[i],"contentType");
				if(contentType.toLowerCase()=="html"||contentType.toLowerCase()=="markup"){
					o.contentType="__markup__";	//	always convert to "__markup__"
				}else{
					o.contentType = contentType;
				}
			}
			o.label=dojo.html.renderedTextContent(cells[i]);
			this.columns.push(o);

			//	check to see if there's a default sort, and set the properties necessary
			if(dojo.html.hasAttribute(cells[i], "sort")){
				this.sortIndex=i;
				var dir=dojo.html.getAttribute(cells[i], "sort");
				if(!isNaN(parseInt(dir))){
					dir=parseInt(dir);
					this.sortDirection=(dir!=0)?1:0;
				}else{
					this.sortDirection=(dir.toLowerCase()=="desc")?1:0;
				}
			}
		}
	},

	parseDataFromTable:function(/* HTMLTableBodyElement */ tbody) {
		//	summary
		//	parses the data in the tbody of a table to create a set of objects.
		//	Will add objects to this.selected if an attribute 'selected="true"' is present on the row.
		this.data=[];
		this.selected=[];
		var rows=tbody.getElementsByTagName("tr");
		for(var i=0; i<rows.length; i++){
			if(dojo.html.getAttribute(rows[i],"ignoreIfParsed")=="true"){
				continue;
			}
			var o={};	//	new data object.
			var cells=rows[i].getElementsByTagName("td");
			for(var j=0; j<this.columns.length; j++){
				var field=this.columns[j].getField();

				// get the content of the cell (depends on the dataType)
				var cellContent;
				if(this.columns[j].contentType=="__markup__"){
					cellContent=cells[j].innerHTML;
				} else {
					var type=this.columns[j].getType();
					var val=dojo.html.renderedTextContent(cells[j]); //	should be the same index as the column.
					if (val) cellContent=new type(val);
					else cellContent=new type();	//	let it use the default.
				}
				
				var sortValue=cellContent;
				// look for the sortValue attribute which will be used for
				// sorting if present (instead of the cell content)
				if (dojo.html.hasAttribute(cells[j], this.sortValueAttribute)) {
					sortValue = dojo.html.getAttribute(cells[j], this.sortValueAttribute);
					
					var type=this.columns[j].getType();
					var val=dojo.html.renderedTextContent(cells[j]); //	should be the same index as the column.
					if (val) sortValue=new type(val);
					else sortValue=new type();	//	let it use the default.
				}
				
				o[field] = [sortValue, cellContent];
			}
			if(dojo.html.hasAttribute(rows[i],"value")&&!o[this.valueField]){
				o[this.valueField]=dojo.html.getAttribute(rows[i],"value");
			}
			//	FIXME: add code to preserve row attributes in __metadata__ field?
			this.data.push(o);
			
			//	add it to the selections if selected="true" is present.
			if(dojo.html.getAttribute(rows[i],"selected")=="true"){
				this.selected.push(o);
			}
		}
	},
	
	render:function(bDontPreserve) {
		//	summary
		//	renders the table to the browser
		var data=[];
		var body=this.domNode.getElementsByTagName("tbody")[0];

		if(!bDontPreserve){
			//	rebuild data and selection
			this.parseDataFromTable(body);
		}

		//	clone this.data for sorting purposes.
		for(var i=0; i<this.data.length; i++){
			data.push(this.data[i]);
		}
		
		var col=this.columns[this.sortIndex];
		if(!col.noSort){
			var field=col.getField();
			if(col.sortFunction){
				var sort=col.sortFunction;
			}else{
				var sort=function(a,b){
					// each hash entry under 'field' contains a 2-field array
					// with the first element [0] being the value to sort
					if (a[field][0]>b[field][0]) return 1;
					if (a[field][0]<b[field][0]) return -1;
					return 0;
				}
			}
			data.sort(sort);
			if(this.sortDirection!=0) data.reverse();
		}

		//	build the table and pop it in.
		while(body.childNodes.length>0) body.removeChild(body.childNodes[0]);
		for(var i=0; i<data.length;i++){
			var row=document.createElement("tr");
			dojo.html.disableSelection(row);
			if (data[i][this.valueField]){
				row.setAttribute("value",data[i][this.valueField]);
			}
			if(this.isSelected(data[i])){
				row.className=this.rowSelectedClass;
				row.setAttribute("selected","true");
			} else {
				if(this.enableAlternateRows&&i%2==1){
					row.className=this.rowAlternateClass;
				}
			}
			for(var j=0;j<this.columns.length;j++){
				var cell=document.createElement("td");
				cell.setAttribute("align", this.columns[j].align);
				cell.setAttribute("valign", this.columns[j].valign);
				dojo.html.disableSelection(cell);
				if(this.sortIndex==j){
					cell.className=this.columnSelected;
				}
				if(this.columns[j].contentType=="__markup__"){
					// the second array element in the field data contains the stuff to be rendered
					cell.innerHTML=data[i][this.columns[j].getField()][1];
					for(var k=0; k<cell.childNodes.length; k++){
						var node=cell.childNodes[k];
						if(node&&node.nodeType==dojo.html.ELEMENT_NODE){
							dojo.html.disableSelection(node);
						}
					}
				}else{
					if(this.columns[j].getType()==Date){
						var format=this.defaultDateFormat;
						if(this.columns[j].format) format=this.columns[j].format;
						// the second array element in the field data contains the stuff to be rendered
						cell.appendChild(document.createTextNode(dojo.date.format(data[i][this.columns[j].getField()][1], format)));
					}else{
						// the second array element in the field data contains the stuff to be rendered
						cell.appendChild(document.createTextNode(data[i][this.columns[j].getField()][1]));
					}
				}
				row.appendChild(cell);
			}
			body.appendChild(row);
			//dojo.event.connect(row, "onclick", this, "onUISelect");
		}
		
		//	if minRows exist.
		var minRows=parseInt(this.minRows);
		if (!isNaN(minRows) && minRows>0 && data.length<minRows){
			var mod=0;
			if(data.length%2==0) mod=1;
			var nRows=minRows-data.length;
			for(var i=0; i<nRows; i++){
				var row=document.createElement("tr");
				row.setAttribute("ignoreIfParsed","true");
				if(this.enableAlternateRows&&i%2==mod){
					row.className=this.rowAlternateClass;
				}
				for(var j=0;j<this.columns.length;j++){
					var cell=document.createElement("td");
					cell.appendChild(document.createTextNode("\u00A0"));
					row.appendChild(cell);
				}
				body.appendChild(row);
			}
		}
	}
});

