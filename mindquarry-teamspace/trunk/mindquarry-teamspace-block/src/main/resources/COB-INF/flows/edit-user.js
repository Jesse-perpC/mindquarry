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
cocoon.load("resource://com/mindquarry/cocoon/forms/Form.js");
cocoon.load("flows/upload-photo.js");

var model_;
var form_;

/**
 * Same as showForm
 */
Form.prototype.sendFormAndWait = function(uri, viewdata, ttl) {
	
    var finished = false;

    var comingBack = false;
    var bookmark = cocoon.createWebContinuation(ttl);

    // Attach the form to the continuation so that we can access by just knowing the continuation id
    bookmark.setAttribute("form", this.form);

    if (comingBack) {
        // We come back to the bookmark: process the form

        if (finished && cocoon.request.getParameter("cocoon-ajax-continue") != null) {
            // A request with this parameter is sent by the client upon receiving the indication
            // that Ajax interaction on the form is finished (see below).
            // We also check "finished" to ensure we won't exit showForm() because of some
            // faulty or hacked request. It's set to false, this will simply redisplay the form.
            return bookmark;
        }

        if (this.restoreHook) {
            this.restoreHook(this);
        }

        var formContext = new Packages.org.apache.cocoon.forms.FormContext(cocoon.request, this.locale);

        // Prematurely add the viewdata as in the object model so that event listeners can use it
        // (the same is done by cocoon.sendPage())
        // FIXME : hack needed because FOM doesn't provide access to the object model
        var objectModel = org.apache.cocoon.components.ContextHelper.getObjectModel(this.avalonContext);
        org.apache.cocoon.components.flow.FlowHelper.setContextObject(objectModel, viewdata);

        finished = this.form.process(formContext);

        if (finished) {
            this.isValid = this.form.isValid();
            var widget = this.form.getSubmitWidget();
            // Can be null on "normal" submit
            this.submitId = widget == null ? null : widget.getId();

            if (cocoon.request.getParameter("cocoon-ajax") != null) {
                // Ask the client to issue a new request reloading the whole page.
                // As we have nothing special to send back, so a header should be just what we need...
                // e.g. cocoon.response.setHeader("X-Cocoon-Ajax", "continue");
                //      cocoon.sendStatus(200);
                // ...but Safari doesn't consider empty responses (with content-length = 0) as
                // valid ones. So send a continue response by using directly the HttpResponse's
                // output stream. Avoiding this hack would require to put an additional pipeline
                // in the sitemap for just sending constant response, which isn't nice.
                cocoon.sendStatus(200);
                var httpResponse = objectModel.get(org.apache.cocoon.environment.http.HttpEnvironment.HTTP_RESPONSE_OBJECT);

                if (httpResponse) {
                    var text ="";
                    if (cocoon.request.getParameter("dojo.transport")=="iframe") {
                    	httpResponse.setContentType("text/html");
                    	text = "<html><head><title>Browser Update Data-Island</title></head><body>"
                    		+ "<form id='browser-update'>"
                    		+ "<textarea name='continue'></textarea>"
                    		+ "</form>"
                    		+ "</body></html>";
                    } else {
                    	httpResponse.setContentType("text/xml");
	                    text = "<?xml version='1.0'?><bu:document xmlns:bu='" +
	                        org.apache.cocoon.ajax.BrowserUpdateTransformer.BU_NSURI +
	                        "'><bu:continue/></bu:document>";
	                    
                    }
                    httpResponse.setContentLength(text.length);
                    httpResponse.writer.print(text);
                } else {
                    // Empty response
                    cocoon.response.setHeader("Content-Length", "0");
                }

                FOM_Cocoon.suicide();
            }

            return bookmark;
        }
    }

    comingBack = true;
    viewdata = this.buildViewData(viewdata)
    cocoon.sendPage(uri, viewdata, bookmark);

    // Clean up after sending the page
    if (this.cleanupHook) {
        this.cleanupHook(this);
    }

    FOM_Cocoon.suicide();
}

function processEditUser(form) {
	model_ = form.getModel();
	form_ = form;
	
	var targetUri = cocoon.request.getParameter("targetUri");
	
	var user = userById(cocoon.request.getAttribute("username"));	
	model_.fullName = user.name + " " + user.surname;
	
	model_.profile.name = user.getName();
	model_.profile.surname = user.getSurname();
	model_.profile.email = user.getEmail();
	model_.profile.skills = user.getSkills();
	
	form_.lookupWidget("/changePassword/successfullyChanged").value = "Password Changed";
	form_.lookupWidget("/changePassword/passwordNotChanged").value = "Password could not be changed";

	form.showForm("edit-user.instance");
			
	cocoon.redirectTo("cocoon:/redirectTo/" + targetUri);
}

function updateProfile() {
	var firstname = model_.profile.name;
	var surname = model_.profile.surname;
	var email = model_.profile.email;
	var skills = model_.profile.skills;
		
	var userAdmin = cocoon.getComponent(UserAdmin.ROLE);
	var currentUserId = cocoon.request.getAttribute("username");
	var currentUser = userAdmin.userById(currentUserId);
	
		
	currentUser.setName(firstname);
	currentUser.setSurname(surname);	
	currentUser.setEmail(email);
	currentUser.setSkills(skills);
	
	userAdmin.updateUser(currentUser);	
}

function changePassword() {
	var oldPwd = model_.changePassword.currentPassword;
	var newPwd = model_.changePassword.newPassword;
		
	var userAdmin = cocoon.getComponent(UserAdmin.ROLE);
	
	var currentUserId = cocoon.request.getAttribute("username");
	var currentUser = userAdmin.userById(currentUserId);
		
	var changed = currentUser.changePassword(oldPwd, newPwd);
	
	if (changed) {
	    userAdmin.updateUser(currentUser);
	    
		form_.lookupWidget("/changePassword/successfullyChanged").state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE;
		form_.lookupWidget("/changePassword/passwordNotChanged").state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE;
	}
	else {
		form_.lookupWidget("/changePassword/passwordNotChanged").state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE;
		form_.lookupWidget("/changePassword/successfullyChanged").state = Packages.org.apache.cocoon.forms.formmodel.WidgetState.INVISIBLE;
	}
}

function uploadPhoto() {
	var currentUserId = cocoon.request.getAttribute("username");
	var photoWidget = form_.lookupWidget("/uploadPhoto/photo");
	
	if (containsPhotoUpload(photoWidget))
	    persistUserPhoto(currentUserId, photoWidget);
}
