/*
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
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