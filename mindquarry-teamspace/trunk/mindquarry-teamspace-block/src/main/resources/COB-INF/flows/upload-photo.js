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

function containsPhotoUpload(uploadWidget) {
	return (uploadWidget.getValue() != null);
}

function persistUserPhoto(userId, uploadWidget) {
	
	var photoJcrUri = "jcr:///users/" + userId + ".png"
	var photoSource = resolveSource(photoJcrUri);

	var uploadValue = uploadWidget.getValue();	
	uploadValue.copyToSource(photoSource);
}

function resolveSource(uri) {
	var resolver = cocoon.getComponent(Packages.org.apache.cocoon.environment.SourceResolver.ROLE);
    var result = resolver.resolveURI(uri);
    cocoon.releaseComponent(resolver);
    return result;
}