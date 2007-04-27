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
function userById(userId) {
	var userAdmin = cocoon.getComponent(UserAdmin.ROLE);	
	var result = userAdmin.userById(userId);	
	cocoon.releaseComponent(userAdmin);
	return result;
}
 
function existsUserWithId(userIdWidget) {
	var userModel = model_.userModel;
	var userExists = (null != userById(userModel.userId));
	if (userExists) {
		userIdWidget.setValidationError(
			new Packages.org.apache.cocoon.forms.validation.ValidationError("A user with this id already exists."));
	}
	return false;
}
 
function deleteUser() {
	var userId = cocoon.parameters["userId"];
	var userAdmin = cocoon.getComponent(UserAdmin.ROLE);	
	userAdmin.deleteUser(userAdmin.userById(userId));	
	cocoon.releaseComponent(userAdmin);
	
	cocoon.response.sendStatus(200);
}