
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
cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");
var form_;
var endpoint_;

function displaySearchForm() {
    endpoint_ = cocoon.parameters["endpoint"];
    
	form_ = new Form(cocoon.parameters["definitionURI"]);
	form_.showForm(cocoon.parameters["templatePipeline"]);
}

function executeSearch() {
    var searchField = form_.lookupWidget("/searchField");
    var messageField = form_.lookupWidget("messageField");
	
    // create HTTP client
    var httpClient = new Packages.org.apache.commons.httpclient.HttpClient();
    httpClient.getParams().setAuthenticationPreemptive(true);
    
    // execute search
    var get = new Packages.org.apache.commons.httpclient.methods.GetMethod(
        endpoint_ + "/solr-select/?wt=json&indent=true&fl=score&q=" + searchField.getValue());
    get.addRequestHeader("Authorization", cocoon.request.getHeader("Authorization"));
    get.setDoAuthentication(true);
    httpClient.executeMethod(get);
    
    // FIXME use cocoon internal pipelines instead of HTTP client!!!

    // check results
    if (get.getStatusCode() == 200) {
        var response = eval('(' + get.getResponseBodyAsString() + ')');
        messageField.setValue("search finished with " + response.response.numFound + " results");
        processResponse(response);
        
        // used for DEBUGGING
        print(get.getResponseBodyAsString());
    } else if (get.getStatusCode() == 401) {
        messageField.setValue("You are not authorized to execute a search.");
    } else {
        messageField.setValue("Unknown error during search (" + get.getStatusCode() + ")");
    }
    get.releaseConnection();
}

function processResponse(response) {
    var resultRepeater = form_.lookupWidget("/results");
    resultRepeater.clear();
    
    for(var i=0; i<response.response.docs.length; i++) {
        var row = resultRepeater.addRow();
        row.lookupWidget("source").setValue(response.response.docs[i].location);
    }
}
