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
 

dojo.provide("mindquarry.widget.normalizeName");

// setup the namespace
var mindquarry = mindquarry || {};
mindquarry.widget = mindquarry.widget || {};


// filters the provided name to exclude illegal characters
mindquarry.widget.normalizeName = function(name) {
	var legal = "0123456789_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"; // characters to keep
	var result = [];
	
	name = name.split(" ").join("_"); // convert spaces to underscore
	
	for (var i = 0; i < name.length; i++) {
		var ch = name[i];
		if (legal.indexOf(ch) != -1) result.push(ch);
	}
	
	return result.join("");
}