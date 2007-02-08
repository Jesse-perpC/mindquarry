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
    
    // trick to get a new line in the body
    var eol = "%0A";
    var sep = "+--------------------------------------------------------------+";
    var tab = "| ";
    
    // build body content
    var body = "Bug Report:" + eol;
    body += "===========" + eol;
    body += eol + "Thank you for supporting Mindquarry by reporting a bug!";
    body += eol + "Please fill in the bug information below:";
    body += eol;
    body += eol + "What steps have you done?";
    body += eol;
    body += eol + "...";
    body += eol;
    body += eol + "What problem has occured?";
    body += eol;
    body += eol + "...";
    body += eol;
    body += eol;
    body += eol;
    
    body += eol + "Please keep the error information below.";
    body += eol + "It should not contain any personal information.";
    body += eol;
    body += eol + sep;
    body += eol + tab + "Mindquarry Information";
    body += eol + sep;
    body += eol + tab + "Path    : " + encodeURIComponent(g_mindquarryPath);
    body += eol + tab + "Block   : " + g_mindquarryBlock;
    body += eol + tab + "Version : " + g_mindquarryVersion;
    body += eol + tab + "Time    : " + g_mindquarryTimeStamp;
    body += eol + sep;
    body += eol + tab + "Browser Information";
    body += eol + sep;
    body += eol + tab + "Code    : " + browserCodeName;
    body += eol + tab + "Browser : " + browserName + " " + browserVersion;
    body += eol + tab + "Cookies : " + cookieEnabled;
    body += eol + tab + "Language: " + browserLanguage;
    body += eol + tab + "Platform: " + browserPlatform;
    body += eol + sep;

    var reportLink = document.getElementById("bugreport");
    if (reportLink) {
	    reportLink.href = "mailto:support@mindquarry.com";
	    reportLink.href += "?subject=[Bug%20Report] " + g_mindquarryBlock + ":" + g_mindquarryPath + "(" + g_mindquarryVersion + ")";
	    reportLink.href += " [yn]"; // javascript enabled, no error message
	    reportLink.href += "&body=" + body;
    }

	// on the error page there is an additional report link with more details
    reportLink = document.getElementById("bugreport-onerror");
    if (reportLink) {
    	var stacktraceNewlineRE = / at /gi
    	var stacktraceNewlineReplace = eol + "    at ";
	    body += eol + tab + "Error information";
	    body += eol + sep;
        body += eol + tab + "Message:";
        body += eol + tab + g_mindquarryErrorMessage;
        body += eol + sep;
        body += eol + tab + "Stacktrace:";
        body += eol + g_mindquarryStacktrace.replace(stacktraceNewlineRE, stacktraceNewlineReplace);
        body += eol + sep;
        body += eol + tab + "Full Stacktrace:";
        body += eol + g_mindquarryFullStacktrace.replace(stacktraceNewlineRE, stacktraceNewlineReplace);
        
	    reportLink.href = "mailto:support@mindquarry.com";
	    reportLink.href += "?subject=[Bug%20Report] " + g_mindquarryBlock + ":" + g_mindquarryPath + "(" + g_mindquarryVersion + ")";
	    reportLink.href += " [yy]"; // javascript enabled, with error message
	    reportLink.href += "&body=" + body;
    }
});
