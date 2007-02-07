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
dojo.addOnLoad(function()
{
    var browserCodeName = navigator.appCodeName;
    var browserName = navigator.appName;
    var browserVersion = navigator.appVersion;
    var cookieEnabled = navigator.cookieEnabled;
    var browserLanguage = navigator.language;
    var browserPlatform = navigator.platform;
    
    // build body content
    var body = "Thank you for supporting Mindquarry by reporting a bug! ";
    body += "Please add below a description of what you have done and which problem occurs!";
    
    var reportLink = document.getElementById("bugreport");
    reportLink.href += ", code=" + browserCodeName 
        + ", browser=" + browserName 
        + " " + browserVersion 
        + ", cookies=" + cookieEnabled 
        + ", language=" + browserLanguage 
        + ", platform=" + browserPlatform 
        + "&body=" + body;
});
