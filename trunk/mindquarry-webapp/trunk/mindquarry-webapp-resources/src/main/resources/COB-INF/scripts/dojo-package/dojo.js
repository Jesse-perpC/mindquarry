/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

/*
	This is a compiled version of Dojo, built for deployment and not for
	development. To get an editable version, please visit:

		http://dojotoolkit.org

	for documentation and information on getting the source.
*/

if(typeof dojo=="undefined"){
var dj_global=this;
function dj_undef(_1,_2){
if(_2==null){
_2=dj_global;
}
return (typeof _2[_1]=="undefined");
}
if(dj_undef("djConfig")){
var djConfig={};
}
if(dj_undef("dojo")){
var dojo={};
}
dojo.version={major:0,minor:0,patch:0,flag:"dev",revision:Number("$Rev: 4342 $".match(/[0-9]+/)[0]),toString:function(){
with(dojo.version){
return major+"."+minor+"."+patch+flag+" ("+revision+")";
}
}};
dojo.evalProp=function(_3,_4,_5){
return (_4&&!dj_undef(_3,_4)?_4[_3]:(_5?(_4[_3]={}):undefined));
};
dojo.parseObjPath=function(_6,_7,_8){
var _9=(_7!=null?_7:dj_global);
var _a=_6.split(".");
var _b=_a.pop();
for(var i=0,l=_a.length;i<l&&_9;i++){
_9=dojo.evalProp(_a[i],_9,_8);
}
return {obj:_9,prop:_b};
};
dojo.evalObjPath=function(_e,_f){
if(typeof _e!="string"){
return dj_global;
}
if(_e.indexOf(".")==-1){
return dojo.evalProp(_e,dj_global,_f);
}
var ref=dojo.parseObjPath(_e,dj_global,_f);
if(ref){
return dojo.evalProp(ref.prop,ref.obj,_f);
}
return null;
};
dojo.errorToString=function(_11){
if(!dj_undef("message",_11)){
return _11.message;
}else{
if(!dj_undef("description",_11)){
return _11.description;
}else{
return _11;
}
}
};
dojo.raise=function(_12,_13){
if(_13){
_12=_12+": "+dojo.errorToString(_13);
}
try{
dojo.hostenv.println("FATAL: "+_12);
}
catch(e){
}
throw Error(_12);
};
dojo.debug=function(){
};
dojo.debugShallow=function(obj){
};
dojo.profile={start:function(){
},end:function(){
},stop:function(){
},dump:function(){
}};
function dj_eval(_15){
return dj_global.eval?dj_global.eval(_15):eval(_15);
}
dojo.unimplemented=function(_16,_17){
var _18="'"+_16+"' not implemented";
if(_17!=null){
_18+=" "+_17;
}
dojo.raise(_18);
};
dojo.deprecated=function(_19,_1a,_1b){
var _1c="DEPRECATED: "+_19;
if(_1a){
_1c+=" "+_1a;
}
if(_1b){
_1c+=" -- will be removed in version: "+_1b;
}
dojo.debug(_1c);
};
dojo.inherits=function(_1d,_1e){
if(typeof _1e!="function"){
dojo.raise("dojo.inherits: superclass argument ["+_1e+"] must be a function (subclass: ["+_1d+"']");
}
_1d.prototype=new _1e();
_1d.prototype.constructor=_1d;
_1d.superclass=_1e.prototype;
_1d["super"]=_1e.prototype;
};
dojo.render=(function(){
function vscaffold(_1f,_20){
var tmp={capable:false,support:{builtin:false,plugin:false},prefixes:_1f};
for(var _22 in _20){
tmp[_22]=false;
}
return tmp;
}
return {name:"",ver:dojo.version,os:{win:false,linux:false,osx:false},html:vscaffold(["html"],["ie","opera","khtml","safari","moz"]),svg:vscaffold(["svg"],["corel","adobe","batik"]),vml:vscaffold(["vml"],["ie"]),swf:vscaffold(["Swf","Flash","Mm"],["mm"]),swt:vscaffold(["Swt"],["ibm"])};
})();
dojo.hostenv=(function(){
var _23={isDebug:false,allowQueryConfig:false,baseScriptUri:"",baseRelativePath:"",libraryScriptUri:"",iePreventClobber:false,ieClobberMinimal:true,preventBackButtonFix:true,searchIds:[],parseWidgets:true};
if(typeof djConfig=="undefined"){
djConfig=_23;
}else{
for(var _24 in _23){
if(typeof djConfig[_24]=="undefined"){
djConfig[_24]=_23[_24];
}
}
}
return {name_:"(unset)",version_:"(unset)",getName:function(){
return this.name_;
},getVersion:function(){
return this.version_;
},getText:function(uri){
dojo.unimplemented("getText","uri="+uri);
}};
})();
dojo.hostenv.getBaseScriptUri=function(){
if(djConfig.baseScriptUri.length){
return djConfig.baseScriptUri;
}
var uri=new String(djConfig.libraryScriptUri||djConfig.baseRelativePath);
if(!uri){
dojo.raise("Nothing returned by getLibraryScriptUri(): "+uri);
}
var _27=uri.lastIndexOf("/");
djConfig.baseScriptUri=djConfig.baseRelativePath;
return djConfig.baseScriptUri;
};
(function(){
var _28={pkgFileName:"__package__",loading_modules_:{},loaded_modules_:{},addedToLoadingCount:[],removedFromLoadingCount:[],inFlightCount:0,modulePrefixes_:{dojo:{name:"dojo",value:"src"}},setModulePrefix:function(_29,_2a){
this.modulePrefixes_[_29]={name:_29,value:_2a};
},getModulePrefix:function(_2b){
var mp=this.modulePrefixes_;
if((mp[_2b])&&(mp[_2b]["name"])){
return mp[_2b].value;
}
return _2b;
},getTextStack:[],loadUriStack:[],loadedUris:[],post_load_:false,modulesLoadedListeners:[],unloadListeners:[],loadNotifying:false};
for(var _2d in _28){
dojo.hostenv[_2d]=_28[_2d];
}
})();
dojo.hostenv.loadPath=function(_2e,_2f,cb){
var uri;
if((_2e.charAt(0)=="/")||(_2e.match(/^\w+:/))){
uri=_2e;
}else{
uri=this.getBaseScriptUri()+_2e;
}
if(djConfig.cacheBust&&dojo.render.html.capable){
uri+="?"+String(djConfig.cacheBust).replace(/\W+/g,"");
}
try{
return ((!_2f)?this.loadUri(uri,cb):this.loadUriAndCheck(uri,_2f,cb));
}
catch(e){
dojo.debug(e);
return false;
}
};
dojo.hostenv.loadUri=function(uri,cb){
if(this.loadedUris[uri]){
return 1;
}
var _34=this.getText(uri,null,true);
if(_34==null){
return 0;
}
this.loadedUris[uri]=true;
if(cb){
_34="("+_34+")";
}
var _35=dj_eval(_34);
if(cb){
cb(_35);
}
return 1;
};
dojo.hostenv.loadUriAndCheck=function(uri,_37,cb){
var ok=true;
try{
ok=this.loadUri(uri,cb);
}
catch(e){
dojo.debug("failed loading ",uri," with error: ",e);
}
return ((ok)&&(this.findModule(_37,false)))?true:false;
};
dojo.loaded=function(){
};
dojo.unloaded=function(){
};
dojo.hostenv.loaded=function(){
this.loadNotifying=true;
this.post_load_=true;
var mll=this.modulesLoadedListeners;
for(var x=0;x<mll.length;x++){
mll[x]();
}
this.modulesLoadedListeners=[];
this.loadNotifying=false;
dojo.loaded();
};
dojo.hostenv.unloaded=function(){
var mll=this.unloadListeners;
while(mll.length){
(mll.pop())();
}
dojo.unloaded();
};
dojo.addOnLoad=function(obj,_3e){
var dh=dojo.hostenv;
if(arguments.length==1){
dh.modulesLoadedListeners.push(obj);
}else{
if(arguments.length>1){
dh.modulesLoadedListeners.push(function(){
obj[_3e]();
});
}
}
if(dh.post_load_&&dh.inFlightCount==0&&!dh.loadNotifying){
dh.callLoaded();
}
};
dojo.addOnUnload=function(obj,_41){
var dh=dojo.hostenv;
if(arguments.length==1){
dh.unloadListeners.push(obj);
}else{
if(arguments.length>1){
dh.unloadListeners.push(function(){
obj[_41]();
});
}
}
};
dojo.hostenv.modulesLoaded=function(){
if(this.post_load_){
return;
}
if((this.loadUriStack.length==0)&&(this.getTextStack.length==0)){
if(this.inFlightCount>0){
dojo.debug("files still in flight!");
return;
}
dojo.hostenv.callLoaded();
}
};
dojo.hostenv.callLoaded=function(){
if(typeof setTimeout=="object"){
setTimeout("dojo.hostenv.loaded();",0);
}else{
dojo.hostenv.loaded();
}
};
dojo.hostenv.getModuleSymbols=function(_43){
var _44=_43.split(".");
for(var i=_44.length-1;i>0;i--){
var _46=_44.slice(0,i).join(".");
var _47=this.getModulePrefix(_46);
if(_47!=_46){
_44.splice(0,i,_47);
break;
}
}
return _44;
};
dojo.hostenv._global_omit_module_check=false;
dojo.hostenv.loadModule=function(_48,_49,_4a){
if(!_48){
return;
}
_4a=this._global_omit_module_check||_4a;
var _4b=this.findModule(_48,false);
if(_4b){
return _4b;
}
if(dj_undef(_48,this.loading_modules_)){
this.addedToLoadingCount.push(_48);
}
this.loading_modules_[_48]=1;
var _4c=_48.replace(/\./g,"/")+".js";
var _4d=this.getModuleSymbols(_48);
var _4e=((_4d[0].charAt(0)!="/")&&(!_4d[0].match(/^\w+:/)));
var _4f=_4d[_4d.length-1];
var _50=_48.split(".");
if(_4f=="*"){
_48=(_50.slice(0,-1)).join(".");
while(_4d.length){
_4d.pop();
_4d.push(this.pkgFileName);
_4c=_4d.join("/")+".js";
if(_4e&&(_4c.charAt(0)=="/")){
_4c=_4c.slice(1);
}
ok=this.loadPath(_4c,((!_4a)?_48:null));
if(ok){
break;
}
_4d.pop();
}
}else{
_4c=_4d.join("/")+".js";
_48=_50.join(".");
var ok=this.loadPath(_4c,((!_4a)?_48:null));
if((!ok)&&(!_49)){
_4d.pop();
while(_4d.length){
_4c=_4d.join("/")+".js";
ok=this.loadPath(_4c,((!_4a)?_48:null));
if(ok){
break;
}
_4d.pop();
_4c=_4d.join("/")+"/"+this.pkgFileName+".js";
if(_4e&&(_4c.charAt(0)=="/")){
_4c=_4c.slice(1);
}
ok=this.loadPath(_4c,((!_4a)?_48:null));
if(ok){
break;
}
}
}
if((!ok)&&(!_4a)){
dojo.raise("Could not load '"+_48+"'; last tried '"+_4c+"'");
}
}
if(!_4a&&!this["isXDomain"]){
_4b=this.findModule(_48,false);
if(!_4b){
dojo.raise("symbol '"+_48+"' is not defined after loading '"+_4c+"'");
}
}
return _4b;
};
dojo.hostenv.startPackage=function(_52){
var _53=dojo.evalObjPath((_52.split(".").slice(0,-1)).join("."));
this.loaded_modules_[(new String(_52)).toLowerCase()]=_53;
var _54=_52.split(/\./);
if(_54[_54.length-1]=="*"){
_54.pop();
}
return dojo.evalObjPath(_54.join("."),true);
};
dojo.hostenv.findModule=function(_55,_56){
var lmn=(new String(_55)).toLowerCase();
if(this.loaded_modules_[lmn]){
return this.loaded_modules_[lmn];
}
var _58=dojo.evalObjPath(_55);
if((_55)&&(typeof _58!="undefined")&&(_58)){
this.loaded_modules_[lmn]=_58;
return _58;
}
if(_56){
dojo.raise("no loaded module named '"+_55+"'");
}
return null;
};
dojo.kwCompoundRequire=function(_59){
var _5a=_59["common"]||[];
var _5b=(_59[dojo.hostenv.name_])?_5a.concat(_59[dojo.hostenv.name_]||[]):_5a.concat(_59["default"]||[]);
for(var x=0;x<_5b.length;x++){
var _5d=_5b[x];
if(_5d.constructor==Array){
dojo.hostenv.loadModule.apply(dojo.hostenv,_5d);
}else{
dojo.hostenv.loadModule(_5d);
}
}
};
dojo.require=function(){
dojo.hostenv.loadModule.apply(dojo.hostenv,arguments);
};
dojo.requireIf=function(){
if((arguments[0]===true)||(arguments[0]=="common")||(arguments[0]&&dojo.render[arguments[0]].capable)){
var _5e=[];
for(var i=1;i<arguments.length;i++){
_5e.push(arguments[i]);
}
dojo.require.apply(dojo,_5e);
}
};
dojo.requireAfterIf=dojo.requireIf;
dojo.provide=function(){
return dojo.hostenv.startPackage.apply(dojo.hostenv,arguments);
};
dojo.setModulePrefix=function(_60,_61){
return dojo.hostenv.setModulePrefix(_60,_61);
};
dojo.exists=function(obj,_63){
var p=_63.split(".");
for(var i=0;i<p.length;i++){
if(!(obj[p[i]])){
return false;
}
obj=obj[p[i]];
}
return true;
};
}
if(typeof window=="undefined"){
dojo.raise("no window object");
}
(function(){
if(djConfig.allowQueryConfig){
var _66=document.location.toString();
var _67=_66.split("?",2);
if(_67.length>1){
var _68=_67[1];
var _69=_68.split("&");
for(var x in _69){
var sp=_69[x].split("=");
if((sp[0].length>9)&&(sp[0].substr(0,9)=="djConfig.")){
var opt=sp[0].substr(9);
try{
djConfig[opt]=eval(sp[1]);
}
catch(e){
djConfig[opt]=sp[1];
}
}
}
}
}
if(((djConfig["baseScriptUri"]=="")||(djConfig["baseRelativePath"]==""))&&(document&&document.getElementsByTagName)){
var _6d=document.getElementsByTagName("script");
var _6e=/(__package__|dojo|bootstrap1)\.js([\?\.]|$)/i;
for(var i=0;i<_6d.length;i++){
var src=_6d[i].getAttribute("src");
if(!src){
continue;
}
var m=src.match(_6e);
if(m){
var _72=src.substring(0,m.index);
if(src.indexOf("bootstrap1")>-1){
_72+="../";
}
if(!this["djConfig"]){
djConfig={};
}
if(djConfig["baseScriptUri"]==""){
djConfig["baseScriptUri"]=_72;
}
if(djConfig["baseRelativePath"]==""){
djConfig["baseRelativePath"]=_72;
}
break;
}
}
}
var dr=dojo.render;
var drh=dojo.render.html;
var drs=dojo.render.svg;
var dua=drh.UA=navigator.userAgent;
var dav=drh.AV=navigator.appVersion;
var t=true;
var f=false;
drh.capable=t;
drh.support.builtin=t;
dr.ver=parseFloat(drh.AV);
dr.os.mac=dav.indexOf("Macintosh")>=0;
dr.os.win=dav.indexOf("Windows")>=0;
dr.os.linux=dav.indexOf("X11")>=0;
drh.opera=dua.indexOf("Opera")>=0;
drh.khtml=(dav.indexOf("Konqueror")>=0)||(dav.indexOf("Safari")>=0);
drh.safari=dav.indexOf("Safari")>=0;
var _7a=dua.indexOf("Gecko");
drh.mozilla=drh.moz=(_7a>=0)&&(!drh.khtml);
if(drh.mozilla){
drh.geckoVersion=dua.substring(_7a+6,_7a+14);
}
drh.ie=(document.all)&&(!drh.opera);
drh.ie50=drh.ie&&dav.indexOf("MSIE 5.0")>=0;
drh.ie55=drh.ie&&dav.indexOf("MSIE 5.5")>=0;
drh.ie60=drh.ie&&dav.indexOf("MSIE 6.0")>=0;
drh.ie70=drh.ie&&dav.indexOf("MSIE 7.0")>=0;
dojo.locale=(drh.ie?navigator.userLanguage:navigator.language).toLowerCase();
dr.vml.capable=drh.ie;
drs.capable=f;
drs.support.plugin=f;
drs.support.builtin=f;
if(document.implementation&&document.implementation.hasFeature&&document.implementation.hasFeature("org.w3c.dom.svg","1.0")){
drs.capable=t;
drs.support.builtin=t;
drs.support.plugin=f;
}
})();
dojo.hostenv.startPackage("dojo.hostenv");
dojo.render.name=dojo.hostenv.name_="browser";
dojo.hostenv.searchIds=[];
dojo.hostenv._XMLHTTP_PROGIDS=["Msxml2.XMLHTTP","Microsoft.XMLHTTP","Msxml2.XMLHTTP.4.0"];
dojo.hostenv.getXmlhttpObject=function(){
var _7b=null;
var _7c=null;
try{
_7b=new XMLHttpRequest();
}
catch(e){
}
if(!_7b){
for(var i=0;i<3;++i){
var _7e=dojo.hostenv._XMLHTTP_PROGIDS[i];
try{
_7b=new ActiveXObject(_7e);
}
catch(e){
_7c=e;
}
if(_7b){
dojo.hostenv._XMLHTTP_PROGIDS=[_7e];
break;
}
}
}
if(!_7b){
return dojo.raise("XMLHTTP not available",_7c);
}
return _7b;
};
dojo.hostenv.getText=function(uri,_80,_81){
var _82=this.getXmlhttpObject();
if(_80){
_82.onreadystatechange=function(){
if(4==_82.readyState){
if((!_82["status"])||((200<=_82.status)&&(300>_82.status))){
_80(_82.responseText);
}
}
};
}
_82.open("GET",uri,_80?true:false);
try{
_82.send(null);
if(_80){
return null;
}
if((_82["status"])&&((200>_82.status)||(300<=_82.status))){
throw Error("Unable to load "+uri+" status:"+_82.status);
}
}
catch(e){
if((_81)&&(!_80)){
return null;
}else{
throw e;
}
}
return _82.responseText;
};
dojo.hostenv.defaultDebugContainerId="dojoDebug";
dojo.hostenv._println_buffer=[];
dojo.hostenv._println_safe=false;
dojo.hostenv.println=function(_83){
if(!dojo.hostenv._println_safe){
dojo.hostenv._println_buffer.push(_83);
}else{
try{
var _84=document.getElementById(djConfig.debugContainerId?djConfig.debugContainerId:dojo.hostenv.defaultDebugContainerId);
if(!_84){
_84=document.getElementsByTagName("body")[0]||document.body;
}
var div=document.createElement("div");
div.appendChild(document.createTextNode(_83));
_84.appendChild(div);
}
catch(e){
try{
document.write("<div>"+_83+"</div>");
}
catch(e2){
window.status=_83;
}
}
}
};
dojo.addOnLoad(function(){
dojo.hostenv._println_safe=true;
while(dojo.hostenv._println_buffer.length>0){
dojo.hostenv.println(dojo.hostenv._println_buffer.shift());
}
});
function dj_addNodeEvtHdlr(_86,_87,fp,_89){
var _8a=_86["on"+_87]||function(){
};
_86["on"+_87]=function(){
fp.apply(_86,arguments);
_8a.apply(_86,arguments);
};
return true;
}
dj_addNodeEvtHdlr(window,"load",function(){
if(arguments.callee.initialized){
return;
}
arguments.callee.initialized=true;
var _8b=function(){
if(dojo.render.html.ie){
dojo.hostenv.makeWidgets();
}
};
if(dojo.hostenv.inFlightCount==0){
_8b();
dojo.hostenv.modulesLoaded();
}else{
dojo.addOnLoad(_8b);
}
});
dj_addNodeEvtHdlr(window,"unload",function(){
dojo.hostenv.unloaded();
});
dojo.hostenv.makeWidgets=function(){
var _8c=[];
if(djConfig.searchIds&&djConfig.searchIds.length>0){
_8c=_8c.concat(djConfig.searchIds);
}
if(dojo.hostenv.searchIds&&dojo.hostenv.searchIds.length>0){
_8c=_8c.concat(dojo.hostenv.searchIds);
}
if((djConfig.parseWidgets)||(_8c.length>0)){
if(dojo.evalObjPath("dojo.widget.Parse")){
var _8d=new dojo.xml.Parse();
if(_8c.length>0){
for(var x=0;x<_8c.length;x++){
var _8f=document.getElementById(_8c[x]);
if(!_8f){
continue;
}
var _90=_8d.parseElement(_8f,null,true);
dojo.widget.getParser().createComponents(_90);
}
}else{
if(djConfig.parseWidgets){
var _90=_8d.parseElement(document.getElementsByTagName("body")[0]||document.body,null,true);
dojo.widget.getParser().createComponents(_90);
}
}
}
}
};
dojo.addOnLoad(function(){
if(!dojo.render.html.ie){
dojo.hostenv.makeWidgets();
}
});
try{
if(dojo.render.html.ie){
document.write("<style>v:*{ behavior:url(#default#VML); }</style>");
document.write("<xml:namespace ns=\"urn:schemas-microsoft-com:vml\" prefix=\"v\"/>");
}
}
catch(e){
}
dojo.hostenv.writeIncludes=function(){
};
dojo.byId=function(id,doc){
if(id&&(typeof id=="string"||id instanceof String)){
if(!doc){
doc=document;
}
return doc.getElementById(id);
}
return id;
};
(function(){
if(typeof dj_usingBootstrap!="undefined"){
return;
}
var _93=false;
var _94=false;
var _95=false;
if((typeof this["load"]=="function")&&((typeof this["Packages"]=="function")||(typeof this["Packages"]=="object"))){
_93=true;
}else{
if(typeof this["load"]=="function"){
_94=true;
}else{
if(window.widget){
_95=true;
}
}
}
var _96=[];
if((this["djConfig"])&&((djConfig["isDebug"])||(djConfig["debugAtAllCosts"]))){
_96.push("debug.js");
}
if((this["djConfig"])&&(djConfig["debugAtAllCosts"])&&(!_93)&&(!_95)){
_96.push("browser_debug.js");
}
if((this["djConfig"])&&(djConfig["compat"])){
_96.push("compat/"+djConfig["compat"]+".js");
}
var _97=djConfig["baseScriptUri"];
if((this["djConfig"])&&(djConfig["baseLoaderUri"])){
_97=djConfig["baseLoaderUri"];
}
for(var x=0;x<_96.length;x++){
var _99=_97+"src/"+_96[x];
if(_93||_94){
load(_99);
}else{
try{
document.write("<scr"+"ipt type='text/javascript' src='"+_99+"'></scr"+"ipt>");
}
catch(e){
var _9a=document.createElement("script");
_9a.src=_99;
document.getElementsByTagName("head")[0].appendChild(_9a);
}
}
}
})();
dojo.fallback_locale="en";
dojo.normalizeLocale=function(_9b){
return _9b?_9b.toLowerCase():dojo.locale;
};
dojo.requireLocalization=function(_9c,_9d,_9e){
dojo.debug("EXPERIMENTAL: dojo.requireLocalization");
var _9f=dojo.hostenv.getModuleSymbols(_9c);
var _a0=_9f.concat("nls").join("/");
_9e=dojo.normalizeLocale(_9e);
var _a1=_9e.split("-");
var _a2=[];
for(var i=_a1.length;i>0;i--){
_a2.push(_a1.slice(0,i).join("-"));
}
if(_a2[_a2.length-1]!=dojo.fallback_locale){
_a2.push(dojo.fallback_locale);
}
var _a4=[_9c,"_nls",_9d].join(".");
var _a5=dojo.hostenv.startPackage(_a4);
dojo.hostenv.loaded_modules_[_a4]=_a5;
var _a6=false;
for(var i=_a2.length-1;i>=0;i--){
var loc=_a2[i];
var pkg=[_a4,loc].join(".");
var _a9=false;
if(!dojo.hostenv.findModule(pkg)){
dojo.hostenv.loaded_modules_[pkg]=null;
var _aa=[_a0,loc,_9d].join("/")+".js";
_a9=dojo.hostenv.loadPath(_aa,null,function(_ab){
_a5[loc]=_ab;
if(_a6){
for(var x in _a6){
if(!_a5[loc][x]){
_a5[loc][x]=_a6[x];
}
}
}
});
}else{
_a9=true;
}
if(_a9&&_a5[loc]){
_a6=_a5[loc];
}
}
};
dojo.provide("dojo.lang.common");
dojo.lang._mixin=function(obj,_ae){
var _af={};
for(var x in _ae){
if(typeof _af[x]=="undefined"||_af[x]!=_ae[x]){
obj[x]=_ae[x];
}
}
if(dojo.render.html.ie&&dojo.lang.isFunction(_ae["toString"])&&_ae["toString"]!=obj["toString"]){
obj.toString=_ae.toString;
}
return obj;
};
dojo.lang.mixin=function(obj,_b2){
for(var i=1,l=arguments.length;i<l;i++){
dojo.lang._mixin(obj,arguments[i]);
}
return obj;
};
dojo.lang.extend=function(_b5,_b6){
for(var i=1,l=arguments.length;i<l;i++){
dojo.lang._mixin(_b5.prototype,arguments[i]);
}
return _b5;
};
dojo.lang.find=function(arr,val,_bb,_bc){
if(!dojo.lang.isArrayLike(arr)&&dojo.lang.isArrayLike(val)){
var a=arr;
arr=val;
val=a;
}
var _be=dojo.lang.isString(arr);
if(_be){
arr=arr.split("");
}
if(_bc){
var _bf=-1;
var i=arr.length-1;
var end=-1;
}else{
var _bf=1;
var i=0;
var end=arr.length;
}
if(_bb){
while(i!=end){
if(arr[i]===val){
return i;
}
i+=_bf;
}
}else{
while(i!=end){
if(arr[i]==val){
return i;
}
i+=_bf;
}
}
return -1;
};
dojo.lang.indexOf=dojo.lang.find;
dojo.lang.findLast=function(arr,val,_c4){
return dojo.lang.find(arr,val,_c4,true);
};
dojo.lang.lastIndexOf=dojo.lang.findLast;
dojo.lang.inArray=function(arr,val){
return dojo.lang.find(arr,val)>-1;
};
dojo.lang.isObject=function(wh){
if(typeof wh=="undefined"){
return false;
}
return (typeof wh=="object"||wh===null||dojo.lang.isArray(wh)||dojo.lang.isFunction(wh));
};
dojo.lang.isArray=function(wh){
return (wh instanceof Array||typeof wh=="array");
};
dojo.lang.isArrayLike=function(wh){
if(dojo.lang.isString(wh)){
return false;
}
if(dojo.lang.isFunction(wh)){
return false;
}
if(dojo.lang.isArray(wh)){
return true;
}
if(typeof wh!="undefined"&&wh&&dojo.lang.isNumber(wh.length)&&isFinite(wh.length)){
return true;
}
return false;
};
dojo.lang.isFunction=function(wh){
if(!wh){
return false;
}
return (wh instanceof Function||typeof wh=="function");
};
dojo.lang.isString=function(wh){
return (wh instanceof String||typeof wh=="string");
};
dojo.lang.isAlien=function(wh){
if(!wh){
return false;
}
return !dojo.lang.isFunction()&&/\{\s*\[native code\]\s*\}/.test(String(wh));
};
dojo.lang.isBoolean=function(wh){
return (wh instanceof Boolean||typeof wh=="boolean");
};
dojo.lang.isNumber=function(wh){
return (wh instanceof Number||typeof wh=="number");
};
dojo.lang.isUndefined=function(wh){
return ((wh==undefined)&&(typeof wh=="undefined"));
};
dojo.provide("dojo.lang.array");
dojo.lang.has=function(obj,_d1){
try{
return (typeof obj[_d1]!="undefined");
}
catch(e){
return false;
}
};
dojo.lang.isEmpty=function(obj){
if(dojo.lang.isObject(obj)){
var tmp={};
var _d4=0;
for(var x in obj){
if(obj[x]&&(!tmp[x])){
_d4++;
break;
}
}
return (_d4==0);
}else{
if(dojo.lang.isArrayLike(obj)||dojo.lang.isString(obj)){
return obj.length==0;
}
}
};
dojo.lang.map=function(arr,obj,_d8){
var _d9=dojo.lang.isString(arr);
if(_d9){
arr=arr.split("");
}
if(dojo.lang.isFunction(obj)&&(!_d8)){
_d8=obj;
obj=dj_global;
}else{
if(dojo.lang.isFunction(obj)&&_d8){
var _da=obj;
obj=_d8;
_d8=_da;
}
}
if(Array.map){
var _db=Array.map(arr,_d8,obj);
}else{
var _db=[];
for(var i=0;i<arr.length;++i){
_db.push(_d8.call(obj,arr[i]));
}
}
if(_d9){
return _db.join("");
}else{
return _db;
}
};
dojo.lang.forEach=function(_dd,_de,_df){
if(dojo.lang.isString(_dd)){
_dd=_dd.split("");
}
if(Array.forEach){
Array.forEach(_dd,_de,_df);
}else{
if(!_df){
_df=dj_global;
}
for(var i=0,l=_dd.length;i<l;i++){
_de.call(_df,_dd[i],i,_dd);
}
}
};
dojo.lang._everyOrSome=function(_e2,arr,_e4,_e5){
if(dojo.lang.isString(arr)){
arr=arr.split("");
}
if(Array.every){
return Array[(_e2)?"every":"some"](arr,_e4,_e5);
}else{
if(!_e5){
_e5=dj_global;
}
for(var i=0,l=arr.length;i<l;i++){
var _e8=_e4.call(_e5,arr[i],i,arr);
if((_e2)&&(!_e8)){
return false;
}else{
if((!_e2)&&(_e8)){
return true;
}
}
}
return (_e2)?true:false;
}
};
dojo.lang.every=function(arr,_ea,_eb){
return this._everyOrSome(true,arr,_ea,_eb);
};
dojo.lang.some=function(arr,_ed,_ee){
return this._everyOrSome(false,arr,_ed,_ee);
};
dojo.lang.filter=function(arr,_f0,_f1){
var _f2=dojo.lang.isString(arr);
if(_f2){
arr=arr.split("");
}
if(Array.filter){
var _f3=Array.filter(arr,_f0,_f1);
}else{
if(!_f1){
if(arguments.length>=3){
dojo.raise("thisObject doesn't exist!");
}
_f1=dj_global;
}
var _f3=[];
for(var i=0;i<arr.length;i++){
if(_f0.call(_f1,arr[i],i,arr)){
_f3.push(arr[i]);
}
}
}
if(_f2){
return _f3.join("");
}else{
return _f3;
}
};
dojo.lang.unnest=function(){
var out=[];
for(var i=0;i<arguments.length;i++){
if(dojo.lang.isArrayLike(arguments[i])){
var add=dojo.lang.unnest.apply(this,arguments[i]);
out=out.concat(add);
}else{
out.push(arguments[i]);
}
}
return out;
};
dojo.lang.toArray=function(_f8,_f9){
var _fa=[];
for(var i=_f9||0;i<_f8.length;i++){
_fa.push(_f8[i]);
}
return _fa;
};
dojo.provide("dojo.dom");
dojo.dom.ELEMENT_NODE=1;
dojo.dom.ATTRIBUTE_NODE=2;
dojo.dom.TEXT_NODE=3;
dojo.dom.CDATA_SECTION_NODE=4;
dojo.dom.ENTITY_REFERENCE_NODE=5;
dojo.dom.ENTITY_NODE=6;
dojo.dom.PROCESSING_INSTRUCTION_NODE=7;
dojo.dom.COMMENT_NODE=8;
dojo.dom.DOCUMENT_NODE=9;
dojo.dom.DOCUMENT_TYPE_NODE=10;
dojo.dom.DOCUMENT_FRAGMENT_NODE=11;
dojo.dom.NOTATION_NODE=12;
dojo.dom.dojoml="http://www.dojotoolkit.org/2004/dojoml";
dojo.dom.xmlns={svg:"http://www.w3.org/2000/svg",smil:"http://www.w3.org/2001/SMIL20/",mml:"http://www.w3.org/1998/Math/MathML",cml:"http://www.xml-cml.org",xlink:"http://www.w3.org/1999/xlink",xhtml:"http://www.w3.org/1999/xhtml",xul:"http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul",xbl:"http://www.mozilla.org/xbl",fo:"http://www.w3.org/1999/XSL/Format",xsl:"http://www.w3.org/1999/XSL/Transform",xslt:"http://www.w3.org/1999/XSL/Transform",xi:"http://www.w3.org/2001/XInclude",xforms:"http://www.w3.org/2002/01/xforms",saxon:"http://icl.com/saxon",xalan:"http://xml.apache.org/xslt",xsd:"http://www.w3.org/2001/XMLSchema",dt:"http://www.w3.org/2001/XMLSchema-datatypes",xsi:"http://www.w3.org/2001/XMLSchema-instance",rdf:"http://www.w3.org/1999/02/22-rdf-syntax-ns#",rdfs:"http://www.w3.org/2000/01/rdf-schema#",dc:"http://purl.org/dc/elements/1.1/",dcq:"http://purl.org/dc/qualifiers/1.0","soap-env":"http://schemas.xmlsoap.org/soap/envelope/",wsdl:"http://schemas.xmlsoap.org/wsdl/",AdobeExtensions:"http://ns.adobe.com/AdobeSVGViewerExtensions/3.0/"};
dojo.dom.isNode=function(wh){
if(typeof Element=="object"){
try{
return wh instanceof Element;
}
catch(E){
}
}else{
return wh&&!isNaN(wh.nodeType);
}
};
dojo.dom.getTagName=function(_fd){
dojo.deprecated("dojo.dom.getTagName","use node.tagName instead","0.4");
var _fe=_fd.tagName;
if(_fe.substr(0,5).toLowerCase()!="dojo:"){
if(_fe.substr(0,4).toLowerCase()=="dojo"){
return "dojo:"+_fe.substring(4).toLowerCase();
}
var djt=_fd.getAttribute("dojoType")||_fd.getAttribute("dojotype");
if(djt){
return "dojo:"+djt.toLowerCase();
}
if((_fd.getAttributeNS)&&(_fd.getAttributeNS(this.dojoml,"type"))){
return "dojo:"+_fd.getAttributeNS(this.dojoml,"type").toLowerCase();
}
try{
djt=_fd.getAttribute("dojo:type");
}
catch(e){
}
if(djt){
return "dojo:"+djt.toLowerCase();
}
if((!dj_global["djConfig"])||(!djConfig["ignoreClassNames"])){
var _100=_fd.className||_fd.getAttribute("class");
if((_100)&&(_100.indexOf)&&(_100.indexOf("dojo-")!=-1)){
var _101=_100.split(" ");
for(var x=0;x<_101.length;x++){
if((_101[x].length>5)&&(_101[x].indexOf("dojo-")>=0)){
return "dojo:"+_101[x].substr(5).toLowerCase();
}
}
}
}
}
return _fe.toLowerCase();
};
dojo.dom.getUniqueId=function(){
do{
var id="dj_unique_"+(++arguments.callee._idIncrement);
}while(document.getElementById(id));
return id;
};
dojo.dom.getUniqueId._idIncrement=0;
dojo.dom.firstElement=dojo.dom.getFirstChildElement=function(_104,_105){
var node=_104.firstChild;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.nextSibling;
}
if(_105&&node&&node.tagName&&node.tagName.toLowerCase()!=_105.toLowerCase()){
node=dojo.dom.nextElement(node,_105);
}
return node;
};
dojo.dom.lastElement=dojo.dom.getLastChildElement=function(_107,_108){
var node=_107.lastChild;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.previousSibling;
}
if(_108&&node&&node.tagName&&node.tagName.toLowerCase()!=_108.toLowerCase()){
node=dojo.dom.prevElement(node,_108);
}
return node;
};
dojo.dom.nextElement=dojo.dom.getNextSiblingElement=function(node,_10b){
if(!node){
return null;
}
do{
node=node.nextSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_10b&&_10b.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.nextElement(node,_10b);
}
return node;
};
dojo.dom.prevElement=dojo.dom.getPreviousSiblingElement=function(node,_10d){
if(!node){
return null;
}
if(_10d){
_10d=_10d.toLowerCase();
}
do{
node=node.previousSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_10d&&_10d.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.prevElement(node,_10d);
}
return node;
};
dojo.dom.moveChildren=function(_10e,_10f,trim){
var _111=0;
if(trim){
while(_10e.hasChildNodes()&&_10e.firstChild.nodeType==dojo.dom.TEXT_NODE){
_10e.removeChild(_10e.firstChild);
}
while(_10e.hasChildNodes()&&_10e.lastChild.nodeType==dojo.dom.TEXT_NODE){
_10e.removeChild(_10e.lastChild);
}
}
while(_10e.hasChildNodes()){
_10f.appendChild(_10e.firstChild);
_111++;
}
return _111;
};
dojo.dom.copyChildren=function(_112,_113,trim){
var _115=_112.cloneNode(true);
return this.moveChildren(_115,_113,trim);
};
dojo.dom.removeChildren=function(node){
var _117=node.childNodes.length;
while(node.hasChildNodes()){
node.removeChild(node.firstChild);
}
return _117;
};
dojo.dom.replaceChildren=function(node,_119){
dojo.dom.removeChildren(node);
node.appendChild(_119);
};
dojo.dom.removeNode=function(node){
if(node&&node.parentNode){
return node.parentNode.removeChild(node);
}
};
dojo.dom.getAncestors=function(node,_11c,_11d){
var _11e=[];
var _11f=dojo.lang.isFunction(_11c);
while(node){
if(!_11f||_11c(node)){
_11e.push(node);
}
if(_11d&&_11e.length>0){
return _11e[0];
}
node=node.parentNode;
}
if(_11d){
return null;
}
return _11e;
};
dojo.dom.getAncestorsByTag=function(node,tag,_122){
tag=tag.toLowerCase();
return dojo.dom.getAncestors(node,function(el){
return ((el.tagName)&&(el.tagName.toLowerCase()==tag));
},_122);
};
dojo.dom.getFirstAncestorByTag=function(node,tag){
return dojo.dom.getAncestorsByTag(node,tag,true);
};
dojo.dom.isDescendantOf=function(node,_127,_128){
if(_128&&node){
node=node.parentNode;
}
while(node){
if(node==_127){
return true;
}
node=node.parentNode;
}
return false;
};
dojo.dom.innerXML=function(node){
if(node.innerXML){
return node.innerXML;
}else{
if(node.xml){
return node.xml;
}else{
if(typeof XMLSerializer!="undefined"){
return (new XMLSerializer()).serializeToString(node);
}
}
}
};
dojo.dom.createDocument=function(){
var doc=null;
if(!dj_undef("ActiveXObject")){
var _12b=["MSXML2","Microsoft","MSXML","MSXML3"];
for(var i=0;i<_12b.length;i++){
try{
doc=new ActiveXObject(_12b[i]+".XMLDOM");
}
catch(e){
}
if(doc){
break;
}
}
}else{
if((document.implementation)&&(document.implementation.createDocument)){
doc=document.implementation.createDocument("","",null);
}
}
return doc;
};
dojo.dom.createDocumentFromText=function(str,_12e){
if(!_12e){
_12e="text/xml";
}
if(!dj_undef("DOMParser")){
var _12f=new DOMParser();
return _12f.parseFromString(str,_12e);
}else{
if(!dj_undef("ActiveXObject")){
var _130=dojo.dom.createDocument();
if(_130){
_130.async=false;
_130.loadXML(str);
return _130;
}else{
dojo.debug("toXml didn't work?");
}
}else{
if(document.createElement){
var tmp=document.createElement("xml");
tmp.innerHTML=str;
if(document.implementation&&document.implementation.createDocument){
var _132=document.implementation.createDocument("foo","",null);
for(var i=0;i<tmp.childNodes.length;i++){
_132.importNode(tmp.childNodes.item(i),true);
}
return _132;
}
return ((tmp.document)&&(tmp.document.firstChild?tmp.document.firstChild:tmp));
}
}
}
return null;
};
dojo.dom.prependChild=function(node,_135){
if(_135.firstChild){
_135.insertBefore(node,_135.firstChild);
}else{
_135.appendChild(node);
}
return true;
};
dojo.dom.insertBefore=function(node,ref,_138){
if(_138!=true&&(node===ref||node.nextSibling===ref)){
return false;
}
var _139=ref.parentNode;
_139.insertBefore(node,ref);
return true;
};
dojo.dom.insertAfter=function(node,ref,_13c){
var pn=ref.parentNode;
if(ref==pn.lastChild){
if((_13c!=true)&&(node===ref)){
return false;
}
pn.appendChild(node);
}else{
return this.insertBefore(node,ref.nextSibling,_13c);
}
return true;
};
dojo.dom.insertAtPosition=function(node,ref,_140){
if((!node)||(!ref)||(!_140)){
return false;
}
switch(_140.toLowerCase()){
case "before":
return dojo.dom.insertBefore(node,ref);
case "after":
return dojo.dom.insertAfter(node,ref);
case "first":
if(ref.firstChild){
return dojo.dom.insertBefore(node,ref.firstChild);
}else{
ref.appendChild(node);
return true;
}
break;
default:
ref.appendChild(node);
return true;
}
};
dojo.dom.insertAtIndex=function(node,_142,_143){
var _144=_142.childNodes;
if(!_144.length){
_142.appendChild(node);
return true;
}
var _145=null;
for(var i=0;i<_144.length;i++){
var _147=_144.item(i)["getAttribute"]?parseInt(_144.item(i).getAttribute("dojoinsertionindex")):-1;
if(_147<_143){
_145=_144.item(i);
}
}
if(_145){
return dojo.dom.insertAfter(node,_145);
}else{
return dojo.dom.insertBefore(node,_144.item(0));
}
};
dojo.dom.textContent=function(node,text){
if(text){
dojo.dom.replaceChildren(node,document.createTextNode(text));
return text;
}else{
var _14a="";
if(node==null){
return _14a;
}
for(var i=0;i<node.childNodes.length;i++){
switch(node.childNodes[i].nodeType){
case 1:
case 5:
_14a+=dojo.dom.textContent(node.childNodes[i]);
break;
case 3:
case 2:
case 4:
_14a+=node.childNodes[i].nodeValue;
break;
default:
break;
}
}
return _14a;
}
};
dojo.dom.collectionToArray=function(_14c){
dojo.deprecated("dojo.dom.collectionToArray","use dojo.lang.toArray instead","0.4");
return dojo.lang.toArray(_14c);
};
dojo.dom.hasParent=function(node){
return node&&node.parentNode&&dojo.dom.isNode(node.parentNode);
};
dojo.dom.isTag=function(node){
if(node&&node.tagName){
var arr=dojo.lang.toArray(arguments,1);
return arr[dojo.lang.find(node.tagName,arr)]||"";
}
return "";
};
dojo.provide("dojo.xml.Parse");
dojo.xml.Parse=function(){
function getDojoTagName(node){
var _151=node.tagName;
if(_151.substr(0,5).toLowerCase()!="dojo:"){
if(_151.substr(0,4).toLowerCase()=="dojo"){
return "dojo:"+_151.substring(4).toLowerCase();
}
var djt=node.getAttribute("dojoType")||node.getAttribute("dojotype");
if(djt){
return "dojo:"+djt.toLowerCase();
}
if(node.getAttributeNS&&node.getAttributeNS(dojo.dom.dojoml,"type")){
return "dojo:"+node.getAttributeNS(dojo.dom.dojoml,"type").toLowerCase();
}
try{
djt=node.getAttribute("dojo:type");
}
catch(e){
}
if(djt){
return "dojo:"+djt.toLowerCase();
}
if(!dj_global["djConfig"]||!djConfig["ignoreClassNames"]){
var _153=node.className||node.getAttribute("class");
if(_153&&_153.indexOf&&_153.indexOf("dojo-")!=-1){
var _154=_153.split(" ");
for(var x=0;x<_154.length;x++){
if(_154[x].length>5&&_154[x].indexOf("dojo-")>=0){
return "dojo:"+_154[x].substr(5).toLowerCase();
}
}
}
}
}
return _151.toLowerCase();
}
this.parseElement=function(node,_157,_158,_159){
if(node.getAttribute("parseWidgets")=="false"){
return {};
}
var _15a={};
var _15b=getDojoTagName(node);
_15a[_15b]=[];
if((!_158)||(_15b.substr(0,4).toLowerCase()=="dojo")){
var _15c=parseAttributes(node);
for(var attr in _15c){
if((!_15a[_15b][attr])||(typeof _15a[_15b][attr]!="array")){
_15a[_15b][attr]=[];
}
_15a[_15b][attr].push(_15c[attr]);
}
_15a[_15b].nodeRef=node;
_15a.tagName=_15b;
_15a.index=_159||0;
}
var _15e=0;
var tcn,i=0,_161=node.childNodes;
while(tcn=_161[i++]){
switch(tcn.nodeType){
case dojo.dom.ELEMENT_NODE:
_15e++;
var ctn=getDojoTagName(tcn);
if(!_15a[ctn]){
_15a[ctn]=[];
}
_15a[ctn].push(this.parseElement(tcn,true,_158,_15e));
if((tcn.childNodes.length==1)&&(tcn.childNodes.item(0).nodeType==dojo.dom.TEXT_NODE)){
_15a[ctn][_15a[ctn].length-1].value=tcn.childNodes.item(0).nodeValue;
}
break;
case dojo.dom.TEXT_NODE:
if(node.childNodes.length==1){
_15a[_15b].push({value:node.childNodes.item(0).nodeValue});
}
break;
default:
break;
}
}
return _15a;
};
function parseAttributes(node){
var _164={};
var atts=node.attributes;
var _166,i=0;
while(_166=atts[i++]){
if((dojo.render.html.capable)&&(dojo.render.html.ie)){
if(!_166){
continue;
}
if((typeof _166=="object")&&(typeof _166.nodeValue=="undefined")||(_166.nodeValue==null)||(_166.nodeValue=="")){
continue;
}
}
var nn=(_166.nodeName.indexOf("dojo:")==-1)?_166.nodeName:_166.nodeName.split("dojo:")[1];
_164[nn]={value:_166.nodeValue};
}
return _164;
}
};
dojo.provide("dojo.lang.func");
dojo.lang.hitch=function(_169,_16a){
if(dojo.lang.isString(_16a)){
var fcn=_169[_16a];
}else{
var fcn=_16a;
}
return function(){
return fcn.apply(_169,arguments);
};
};
dojo.lang.anonCtr=0;
dojo.lang.anon={};
dojo.lang.nameAnonFunc=function(_16c,_16d,_16e){
var nso=(_16d||dojo.lang.anon);
if((_16e)||((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"]==true))){
for(var x in nso){
if(nso[x]===_16c){
return x;
}
}
}
var ret="__"+dojo.lang.anonCtr++;
while(typeof nso[ret]!="undefined"){
ret="__"+dojo.lang.anonCtr++;
}
nso[ret]=_16c;
return ret;
};
dojo.lang.forward=function(_172){
return function(){
return this[_172].apply(this,arguments);
};
};
dojo.lang.curry=function(ns,func){
var _175=[];
ns=ns||dj_global;
if(dojo.lang.isString(func)){
func=ns[func];
}
for(var x=2;x<arguments.length;x++){
_175.push(arguments[x]);
}
var _177=(func["__preJoinArity"]||func.length)-_175.length;
function gather(_178,_179,_17a){
var _17b=_17a;
var _17c=_179.slice(0);
for(var x=0;x<_178.length;x++){
_17c.push(_178[x]);
}
_17a=_17a-_178.length;
if(_17a<=0){
var res=func.apply(ns,_17c);
_17a=_17b;
return res;
}else{
return function(){
return gather(arguments,_17c,_17a);
};
}
}
return gather([],_175,_177);
};
dojo.lang.curryArguments=function(ns,func,args,_182){
var _183=[];
var x=_182||0;
for(x=_182;x<args.length;x++){
_183.push(args[x]);
}
return dojo.lang.curry.apply(dojo.lang,[ns,func].concat(_183));
};
dojo.lang.tryThese=function(){
for(var x=0;x<arguments.length;x++){
try{
if(typeof arguments[x]=="function"){
var ret=(arguments[x]());
if(ret){
return ret;
}
}
}
catch(e){
dojo.debug(e);
}
}
};
dojo.lang.delayThese=function(farr,cb,_189,_18a){
if(!farr.length){
if(typeof _18a=="function"){
_18a();
}
return;
}
if((typeof _189=="undefined")&&(typeof cb=="number")){
_189=cb;
cb=function(){
};
}else{
if(!cb){
cb=function(){
};
if(!_189){
_189=0;
}
}
}
setTimeout(function(){
(farr.shift())();
cb();
dojo.lang.delayThese(farr,cb,_189,_18a);
},_189);
};
dojo.provide("dojo.lang.extras");
dojo.lang.setTimeout=function(func,_18c){
var _18d=window,_18e=2;
if(!dojo.lang.isFunction(func)){
_18d=func;
func=_18c;
_18c=arguments[2];
_18e++;
}
if(dojo.lang.isString(func)){
func=_18d[func];
}
var args=[];
for(var i=_18e;i<arguments.length;i++){
args.push(arguments[i]);
}
return setTimeout(function(){
func.apply(_18d,args);
},_18c);
};
dojo.lang.getNameInObj=function(ns,item){
if(!ns){
ns=dj_global;
}
for(var x in ns){
if(ns[x]===item){
return new String(x);
}
}
return null;
};
dojo.lang.shallowCopy=function(obj){
var ret={},key;
for(key in obj){
if(dojo.lang.isUndefined(ret[key])){
ret[key]=obj[key];
}
}
return ret;
};
dojo.lang.firstValued=function(){
for(var i=0;i<arguments.length;i++){
if(typeof arguments[i]!="undefined"){
return arguments[i];
}
}
return undefined;
};
dojo.lang.getObjPathValue=function(_198,_199,_19a){
with(dojo.parseObjPath(_198,_199,_19a)){
return dojo.evalProp(prop,obj,_19a);
}
};
dojo.lang.setObjPathValue=function(_19b,_19c,_19d,_19e){
if(arguments.length<4){
_19e=true;
}
with(dojo.parseObjPath(_19b,_19d,_19e)){
if(obj&&(_19e||(prop in obj))){
obj[prop]=_19c;
}
}
};
dojo.provide("dojo.lang.declare");
dojo.lang.declare=function(_19f,_1a0,init,_1a2){
if((dojo.lang.isFunction(_1a2))||((!_1a2)&&(!dojo.lang.isFunction(init)))){
var temp=_1a2;
_1a2=init;
init=temp;
}
var _1a4=[];
if(dojo.lang.isArray(_1a0)){
_1a4=_1a0;
_1a0=_1a4.shift();
}
if(!init){
init=dojo.evalObjPath(_19f,false);
if((init)&&(!dojo.lang.isFunction(init))){
init=null;
}
}
var ctor=dojo.lang.declare._makeConstructor();
var scp=(_1a0?_1a0.prototype:null);
if(scp){
scp.prototyping=true;
ctor.prototype=new _1a0();
scp.prototyping=false;
}
ctor.superclass=scp;
ctor.mixins=_1a4;
for(var i=0,l=_1a4.length;i<l;i++){
dojo.lang.extend(ctor,_1a4[i].prototype);
}
ctor.prototype.initializer=null;
ctor.prototype.declaredClass=_19f;
if(dojo.lang.isArray(_1a2)){
dojo.lang.extend.apply(dojo.lang,[ctor].concat(_1a2));
}else{
dojo.lang.extend(ctor,(_1a2)||{});
}
dojo.lang.extend(ctor,dojo.lang.declare.base);
ctor.prototype.constructor=ctor;
ctor.prototype.initializer=(ctor.prototype.initializer)||(init)||(function(){
});
dojo.lang.setObjPathValue(_19f,ctor,null,true);
};
dojo.lang.declare._makeConstructor=function(){
return function(){
var self=this._getPropContext();
var s=self.constructor.superclass;
if((s)&&(s.constructor)){
if(s.constructor==arguments.callee){
this.inherited("constructor",arguments);
}else{
this._inherited(s,"constructor",arguments);
}
}
var m=(self.constructor.mixins)||([]);
for(var i=0,l=m.length;i<l;i++){
(((m[i].prototype)&&(m[i].prototype.initializer))||(m[i])).apply(this,arguments);
}
if((!this.prototyping)&&(self.initializer)){
self.initializer.apply(this,arguments);
}
};
};
dojo.lang.declare.base={_getPropContext:function(){
return (this.___proto||this);
},_inherited:function(_1ae,_1af,args){
var _1b1=this.___proto;
this.___proto=_1ae;
var _1b2=_1ae[_1af].apply(this,(args||[]));
this.___proto=_1b1;
return _1b2;
},inheritedFrom:function(ctor,prop,args){
var p=((ctor)&&(ctor.prototype)&&(ctor.prototype[prop]));
return (dojo.lang.isFunction(p)?p.apply(this,(args||[])):p);
},inherited:function(prop,args){
var p=this._getPropContext();
do{
if((!p.constructor)||(!p.constructor.superclass)){
return;
}
p=p.constructor.superclass;
}while(!(prop in p));
return (dojo.lang.isFunction(p[prop])?this._inherited(p,prop,args):p[prop]);
}};
dojo.declare=dojo.lang.declare;
dojo.provide("dojo.event");
dojo.event=new function(){
this.canTimeout=dojo.lang.isFunction(dj_global["setTimeout"])||dojo.lang.isAlien(dj_global["setTimeout"]);
function interpolateArgs(args,_1bb){
var dl=dojo.lang;
var ao={srcObj:dj_global,srcFunc:null,adviceObj:dj_global,adviceFunc:null,aroundObj:null,aroundFunc:null,adviceType:(args.length>2)?args[0]:"after",precedence:"last",once:false,delay:null,rate:0,adviceMsg:false};
switch(args.length){
case 0:
return;
case 1:
return;
case 2:
ao.srcFunc=args[0];
ao.adviceFunc=args[1];
break;
case 3:
if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isString(args[2]))){
ao.adviceType="after";
ao.srcObj=args[0];
ao.srcFunc=args[1];
ao.adviceFunc=args[2];
}else{
if((dl.isString(args[1]))&&(dl.isString(args[2]))){
ao.srcFunc=args[1];
ao.adviceFunc=args[2];
}else{
if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isFunction(args[2]))){
ao.adviceType="after";
ao.srcObj=args[0];
ao.srcFunc=args[1];
var _1be=dl.nameAnonFunc(args[2],ao.adviceObj,_1bb);
ao.adviceFunc=_1be;
}else{
if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){
ao.adviceType="after";
ao.srcObj=dj_global;
var _1be=dl.nameAnonFunc(args[0],ao.srcObj,_1bb);
ao.srcFunc=_1be;
ao.adviceObj=args[1];
ao.adviceFunc=args[2];
}
}
}
}
break;
case 4:
if((dl.isObject(args[0]))&&(dl.isObject(args[2]))){
ao.adviceType="after";
ao.srcObj=args[0];
ao.srcFunc=args[1];
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if((dl.isString(args[0]))&&(dl.isString(args[1]))&&(dl.isObject(args[2]))){
ao.adviceType=args[0];
ao.srcObj=dj_global;
ao.srcFunc=args[1];
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if((dl.isString(args[0]))&&(dl.isFunction(args[1]))&&(dl.isObject(args[2]))){
ao.adviceType=args[0];
ao.srcObj=dj_global;
var _1be=dl.nameAnonFunc(args[1],dj_global,_1bb);
ao.srcFunc=_1be;
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if((dl.isString(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))&&(dl.isFunction(args[3]))){
ao.srcObj=args[1];
ao.srcFunc=args[2];
var _1be=dl.nameAnonFunc(args[3],dj_global,_1bb);
ao.adviceObj=dj_global;
ao.adviceFunc=_1be;
}else{
if(dl.isObject(args[1])){
ao.srcObj=args[1];
ao.srcFunc=args[2];
ao.adviceObj=dj_global;
ao.adviceFunc=args[3];
}else{
if(dl.isObject(args[2])){
ao.srcObj=dj_global;
ao.srcFunc=args[1];
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
ao.srcObj=ao.adviceObj=ao.aroundObj=dj_global;
ao.srcFunc=args[1];
ao.adviceFunc=args[2];
ao.aroundFunc=args[3];
}
}
}
}
}
}
break;
case 6:
ao.srcObj=args[1];
ao.srcFunc=args[2];
ao.adviceObj=args[3];
ao.adviceFunc=args[4];
ao.aroundFunc=args[5];
ao.aroundObj=dj_global;
break;
default:
ao.srcObj=args[1];
ao.srcFunc=args[2];
ao.adviceObj=args[3];
ao.adviceFunc=args[4];
ao.aroundObj=args[5];
ao.aroundFunc=args[6];
ao.once=args[7];
ao.delay=args[8];
ao.rate=args[9];
ao.adviceMsg=args[10];
break;
}
if(dl.isFunction(ao.aroundFunc)){
var _1be=dl.nameAnonFunc(ao.aroundFunc,ao.aroundObj,_1bb);
ao.aroundFunc=_1be;
}
if(dl.isFunction(ao.srcFunc)){
ao.srcFunc=dl.getNameInObj(ao.srcObj,ao.srcFunc);
}
if(dl.isFunction(ao.adviceFunc)){
ao.adviceFunc=dl.getNameInObj(ao.adviceObj,ao.adviceFunc);
}
if((ao.aroundObj)&&(dl.isFunction(ao.aroundFunc))){
ao.aroundFunc=dl.getNameInObj(ao.aroundObj,ao.aroundFunc);
}
if(!ao.srcObj){
dojo.raise("bad srcObj for srcFunc: "+ao.srcFunc);
}
if(!ao.adviceObj){
dojo.raise("bad adviceObj for adviceFunc: "+ao.adviceFunc);
}
return ao;
}
this.connect=function(){
if(arguments.length==1){
var ao=arguments[0];
}else{
var ao=interpolateArgs(arguments,true);
}
if(dojo.lang.isArray(ao.srcObj)&&ao.srcObj!=""){
var _1c0={};
for(var x in ao){
_1c0[x]=ao[x];
}
var mjps=[];
dojo.lang.forEach(ao.srcObj,function(src){
if((dojo.render.html.capable)&&(dojo.lang.isString(src))){
src=dojo.byId(src);
}
_1c0.srcObj=src;
mjps.push(dojo.event.connect.call(dojo.event,_1c0));
});
return mjps;
}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);
if(ao.adviceFunc){
var mjp2=dojo.event.MethodJoinPoint.getForMethod(ao.adviceObj,ao.adviceFunc);
}
mjp.kwAddAdvice(ao);
return mjp;
};
this.log=function(a1,a2){
var _1c8;
if((arguments.length==1)&&(typeof a1=="object")){
_1c8=a1;
}else{
_1c8={srcObj:a1,srcFunc:a2};
}
_1c8.adviceFunc=function(){
var _1c9=[];
for(var x=0;x<arguments.length;x++){
_1c9.push(arguments[x]);
}
dojo.debug("("+_1c8.srcObj+")."+_1c8.srcFunc,":",_1c9.join(", "));
};
this.kwConnect(_1c8);
};
this.connectBefore=function(){
var args=["before"];
for(var i=0;i<arguments.length;i++){
args.push(arguments[i]);
}
return this.connect.apply(this,args);
};
this.connectAround=function(){
var args=["around"];
for(var i=0;i<arguments.length;i++){
args.push(arguments[i]);
}
return this.connect.apply(this,args);
};
this.connectOnce=function(){
var ao=interpolateArgs(arguments,true);
ao.once=true;
return this.connect(ao);
};
this._kwConnectImpl=function(_1d0,_1d1){
var fn=(_1d1)?"disconnect":"connect";
if(typeof _1d0["srcFunc"]=="function"){
_1d0.srcObj=_1d0["srcObj"]||dj_global;
var _1d3=dojo.lang.nameAnonFunc(_1d0.srcFunc,_1d0.srcObj,true);
_1d0.srcFunc=_1d3;
}
if(typeof _1d0["adviceFunc"]=="function"){
_1d0.adviceObj=_1d0["adviceObj"]||dj_global;
var _1d3=dojo.lang.nameAnonFunc(_1d0.adviceFunc,_1d0.adviceObj,true);
_1d0.adviceFunc=_1d3;
}
return dojo.event[fn]((_1d0["type"]||_1d0["adviceType"]||"after"),_1d0["srcObj"]||dj_global,_1d0["srcFunc"],_1d0["adviceObj"]||_1d0["targetObj"]||dj_global,_1d0["adviceFunc"]||_1d0["targetFunc"],_1d0["aroundObj"],_1d0["aroundFunc"],_1d0["once"],_1d0["delay"],_1d0["rate"],_1d0["adviceMsg"]||false);
};
this.kwConnect=function(_1d4){
return this._kwConnectImpl(_1d4,false);
};
this.disconnect=function(){
var ao=interpolateArgs(arguments,true);
if(!ao.adviceFunc){
return;
}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);
return mjp.removeAdvice(ao.adviceObj,ao.adviceFunc,ao.adviceType,ao.once);
};
this.kwDisconnect=function(_1d7){
return this._kwConnectImpl(_1d7,true);
};
};
dojo.event.MethodInvocation=function(_1d8,obj,args){
this.jp_=_1d8;
this.object=obj;
this.args=[];
for(var x=0;x<args.length;x++){
this.args[x]=args[x];
}
this.around_index=-1;
};
dojo.event.MethodInvocation.prototype.proceed=function(){
this.around_index++;
if(this.around_index>=this.jp_.around.length){
return this.jp_.object[this.jp_.methodname].apply(this.jp_.object,this.args);
}else{
var ti=this.jp_.around[this.around_index];
var mobj=ti[0]||dj_global;
var meth=ti[1];
return mobj[meth].call(mobj,this);
}
};
dojo.event.MethodJoinPoint=function(obj,_1e0){
this.object=obj||dj_global;
this.methodname=_1e0;
this.methodfunc=this.object[_1e0];
this.before=[];
this.after=[];
this.around=[];
};
dojo.event.MethodJoinPoint.getForMethod=function(obj,_1e2){
if(!obj){
obj=dj_global;
}
if(!obj[_1e2]){
obj[_1e2]=function(){
};
if(!obj[_1e2]){
dojo.raise("Cannot set do-nothing method on that object "+_1e2);
}
}else{
if((!dojo.lang.isFunction(obj[_1e2]))&&(!dojo.lang.isAlien(obj[_1e2]))){
return null;
}
}
var _1e3=_1e2+"$joinpoint";
var _1e4=_1e2+"$joinpoint$method";
var _1e5=obj[_1e3];
if(!_1e5){
var _1e6=false;
if(dojo.event["browser"]){
if((obj["attachEvent"])||(obj["nodeType"])||(obj["addEventListener"])){
_1e6=true;
dojo.event.browser.addClobberNodeAttrs(obj,[_1e3,_1e4,_1e2]);
}
}
var _1e7=obj[_1e2].length;
obj[_1e4]=obj[_1e2];
_1e5=obj[_1e3]=new dojo.event.MethodJoinPoint(obj,_1e4);
obj[_1e2]=function(){
var args=[];
if((_1e6)&&(!arguments.length)){
var evt=null;
try{
if(obj.ownerDocument){
evt=obj.ownerDocument.parentWindow.event;
}else{
if(obj.documentElement){
evt=obj.documentElement.ownerDocument.parentWindow.event;
}else{
evt=window.event;
}
}
}
catch(e){
evt=window.event;
}
if(evt){
args.push(dojo.event.browser.fixEvent(evt,this));
}
}else{
for(var x=0;x<arguments.length;x++){
if((x==0)&&(_1e6)&&(dojo.event.browser.isEvent(arguments[x]))){
args.push(dojo.event.browser.fixEvent(arguments[x],this));
}else{
args.push(arguments[x]);
}
}
}
return _1e5.run.apply(_1e5,args);
};
obj[_1e2].__preJoinArity=_1e7;
}
return _1e5;
};
dojo.lang.extend(dojo.event.MethodJoinPoint,{unintercept:function(){
this.object[this.methodname]=this.methodfunc;
this.before=[];
this.after=[];
this.around=[];
},disconnect:dojo.lang.forward("unintercept"),run:function(){
var obj=this.object||dj_global;
var args=arguments;
var _1ed=[];
for(var x=0;x<args.length;x++){
_1ed[x]=args[x];
}
var _1ef=function(marr){
if(!marr){
dojo.debug("Null argument to unrollAdvice()");
return;
}
var _1f1=marr[0]||dj_global;
var _1f2=marr[1];
if(!_1f1[_1f2]){
dojo.raise("function \""+_1f2+"\" does not exist on \""+_1f1+"\"");
}
var _1f3=marr[2]||dj_global;
var _1f4=marr[3];
var msg=marr[6];
var _1f6;
var to={args:[],jp_:this,object:obj,proceed:function(){
return _1f1[_1f2].apply(_1f1,to.args);
}};
to.args=_1ed;
var _1f8=parseInt(marr[4]);
var _1f9=((!isNaN(_1f8))&&(marr[4]!==null)&&(typeof marr[4]!="undefined"));
if(marr[5]){
var rate=parseInt(marr[5]);
var cur=new Date();
var _1fc=false;
if((marr["last"])&&((cur-marr.last)<=rate)){
if(dojo.event.canTimeout){
if(marr["delayTimer"]){
clearTimeout(marr.delayTimer);
}
var tod=parseInt(rate*2);
var mcpy=dojo.lang.shallowCopy(marr);
marr.delayTimer=setTimeout(function(){
mcpy[5]=0;
_1ef(mcpy);
},tod);
}
return;
}else{
marr.last=cur;
}
}
if(_1f4){
_1f3[_1f4].call(_1f3,to);
}else{
if((_1f9)&&((dojo.render.html)||(dojo.render.svg))){
dj_global["setTimeout"](function(){
if(msg){
_1f1[_1f2].call(_1f1,to);
}else{
_1f1[_1f2].apply(_1f1,args);
}
},_1f8);
}else{
if(msg){
_1f1[_1f2].call(_1f1,to);
}else{
_1f1[_1f2].apply(_1f1,args);
}
}
}
};
if(this.before.length>0){
dojo.lang.forEach(this.before,_1ef);
}
var _1ff;
if(this.around.length>0){
var mi=new dojo.event.MethodInvocation(this,obj,args);
_1ff=mi.proceed();
}else{
if(this.methodfunc){
_1ff=this.object[this.methodname].apply(this.object,args);
}
}
if(this.after.length>0){
dojo.lang.forEach(this.after,_1ef);
}
return (this.methodfunc)?_1ff:null;
},getArr:function(kind){
var arr=this.after;
if((typeof kind=="string")&&(kind.indexOf("before")!=-1)){
arr=this.before;
}else{
if(kind=="around"){
arr=this.around;
}
}
return arr;
},kwAddAdvice:function(args){
this.addAdvice(args["adviceObj"],args["adviceFunc"],args["aroundObj"],args["aroundFunc"],args["adviceType"],args["precedence"],args["once"],args["delay"],args["rate"],args["adviceMsg"]);
},addAdvice:function(_204,_205,_206,_207,_208,_209,once,_20b,rate,_20d){
var arr=this.getArr(_208);
if(!arr){
dojo.raise("bad this: "+this);
}
var ao=[_204,_205,_206,_207,_20b,rate,_20d];
if(once){
if(this.hasAdvice(_204,_205,_208,arr)>=0){
return;
}
}
if(_209=="first"){
arr.unshift(ao);
}else{
arr.push(ao);
}
},hasAdvice:function(_210,_211,_212,arr){
if(!arr){
arr=this.getArr(_212);
}
var ind=-1;
for(var x=0;x<arr.length;x++){
var aao=(typeof _211=="object")?(new String(_211)).toString():_211;
var a1o=(typeof arr[x][1]=="object")?(new String(arr[x][1])).toString():arr[x][1];
if((arr[x][0]==_210)&&(a1o==aao)){
ind=x;
}
}
return ind;
},removeAdvice:function(_218,_219,_21a,once){
var arr=this.getArr(_21a);
var ind=this.hasAdvice(_218,_219,_21a,arr);
if(ind==-1){
return false;
}
while(ind!=-1){
arr.splice(ind,1);
if(once){
break;
}
ind=this.hasAdvice(_218,_219,_21a,arr);
}
return true;
}});
dojo.provide("dojo.event.topic");
dojo.event.topic=new function(){
this.topics={};
this.getTopic=function(_21e){
if(!this.topics[_21e]){
this.topics[_21e]=new this.TopicImpl(_21e);
}
return this.topics[_21e];
};
this.registerPublisher=function(_21f,obj,_221){
var _21f=this.getTopic(_21f);
_21f.registerPublisher(obj,_221);
};
this.subscribe=function(_222,obj,_224){
var _222=this.getTopic(_222);
_222.subscribe(obj,_224);
};
this.unsubscribe=function(_225,obj,_227){
var _225=this.getTopic(_225);
_225.unsubscribe(obj,_227);
};
this.destroy=function(_228){
this.getTopic(_228).destroy();
delete this.topics[_228];
};
this.publishApply=function(_229,args){
var _229=this.getTopic(_229);
_229.sendMessage.apply(_229,args);
};
this.publish=function(_22b,_22c){
var _22b=this.getTopic(_22b);
var args=[];
for(var x=1;x<arguments.length;x++){
args.push(arguments[x]);
}
_22b.sendMessage.apply(_22b,args);
};
};
dojo.event.topic.TopicImpl=function(_22f){
this.topicName=_22f;
this.subscribe=function(_230,_231){
var tf=_231||_230;
var to=(!_231)?dj_global:_230;
dojo.event.kwConnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
this.unsubscribe=function(_234,_235){
var tf=(!_235)?_234:_235;
var to=(!_235)?null:_234;
dojo.event.kwDisconnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
this.destroy=function(){
dojo.event.MethodJoinPoint.getForMethod(this,"sendMessage").disconnect();
};
this.registerPublisher=function(_238,_239){
dojo.event.connect(_238,_239,this,"sendMessage");
};
this.sendMessage=function(_23a){
};
};
dojo.provide("dojo.event.browser");
dojo._ie_clobber=new function(){
this.clobberNodes=[];
function nukeProp(node,prop){
try{
node[prop]=null;
}
catch(e){
}
try{
delete node[prop];
}
catch(e){
}
try{
node.removeAttribute(prop);
}
catch(e){
}
}
this.clobber=function(_23d){
var na;
var tna;
if(_23d){
tna=_23d.all||_23d.getElementsByTagName("*");
na=[_23d];
for(var x=0;x<tna.length;x++){
if(tna[x]["__doClobber__"]){
na.push(tna[x]);
}
}
}else{
try{
window.onload=null;
}
catch(e){
}
na=(this.clobberNodes.length)?this.clobberNodes:document.all;
}
tna=null;
var _241={};
for(var i=na.length-1;i>=0;i=i-1){
var el=na[i];
if(el["__clobberAttrs__"]){
for(var j=0;j<el.__clobberAttrs__.length;j++){
nukeProp(el,el.__clobberAttrs__[j]);
}
nukeProp(el,"__clobberAttrs__");
nukeProp(el,"__doClobber__");
}
}
na=null;
};
};
if(dojo.render.html.ie){
dojo.addOnUnload(function(){
dojo._ie_clobber.clobber();
try{
if((dojo["widget"])&&(dojo.widget["manager"])){
dojo.widget.manager.destroyAll();
}
}
catch(e){
}
try{
window.onload=null;
}
catch(e){
}
try{
window.onunload=null;
}
catch(e){
}
dojo._ie_clobber.clobberNodes=[];
});
}
dojo.event.browser=new function(){
var _245=0;
this.clean=function(node){
if(dojo.render.html.ie){
dojo._ie_clobber.clobber(node);
}
};
this.addClobberNode=function(node){
if(!dojo.render.html.ie){
return;
}
if(!node["__doClobber__"]){
node.__doClobber__=true;
dojo._ie_clobber.clobberNodes.push(node);
node.__clobberAttrs__=[];
}
};
this.addClobberNodeAttrs=function(node,_249){
if(!dojo.render.html.ie){
return;
}
this.addClobberNode(node);
for(var x=0;x<_249.length;x++){
node.__clobberAttrs__.push(_249[x]);
}
};
this.removeListener=function(node,_24c,fp,_24e){
if(!_24e){
var _24e=false;
}
_24c=_24c.toLowerCase();
if(_24c.substr(0,2)=="on"){
_24c=_24c.substr(2);
}
if(node.removeEventListener){
node.removeEventListener(_24c,fp,_24e);
}
};
this.addListener=function(node,_250,fp,_252,_253){
if(!node){
return;
}
if(!_252){
var _252=false;
}
_250=_250.toLowerCase();
if(_250.substr(0,2)!="on"){
_250="on"+_250;
}
if(!_253){
var _254=function(evt){
if(!evt){
evt=window.event;
}
var ret=fp(dojo.event.browser.fixEvent(evt,this));
if(_252){
dojo.event.browser.stopEvent(evt);
}
return ret;
};
}else{
_254=fp;
}
if(node.addEventListener){
node.addEventListener(_250.substr(2),_254,_252);
return _254;
}else{
if(typeof node[_250]=="function"){
var _257=node[_250];
node[_250]=function(e){
_257(e);
return _254(e);
};
}else{
node[_250]=_254;
}
if(dojo.render.html.ie){
this.addClobberNodeAttrs(node,[_250]);
}
return _254;
}
};
this.isEvent=function(obj){
return (typeof obj!="undefined")&&(typeof Event!="undefined")&&(obj.eventPhase);
};
this.currentEvent=null;
this.callListener=function(_25a,_25b){
if(typeof _25a!="function"){
dojo.raise("listener not a function: "+_25a);
}
dojo.event.browser.currentEvent.currentTarget=_25b;
return _25a.call(_25b,dojo.event.browser.currentEvent);
};
this.stopPropagation=function(){
dojo.event.browser.currentEvent.cancelBubble=true;
};
this.preventDefault=function(){
dojo.event.browser.currentEvent.returnValue=false;
};
this.keys={KEY_BACKSPACE:8,KEY_TAB:9,KEY_ENTER:13,KEY_SHIFT:16,KEY_CTRL:17,KEY_ALT:18,KEY_PAUSE:19,KEY_CAPS_LOCK:20,KEY_ESCAPE:27,KEY_SPACE:32,KEY_PAGE_UP:33,KEY_PAGE_DOWN:34,KEY_END:35,KEY_HOME:36,KEY_LEFT_ARROW:37,KEY_UP_ARROW:38,KEY_RIGHT_ARROW:39,KEY_DOWN_ARROW:40,KEY_INSERT:45,KEY_DELETE:46,KEY_LEFT_WINDOW:91,KEY_RIGHT_WINDOW:92,KEY_SELECT:93,KEY_F1:112,KEY_F2:113,KEY_F3:114,KEY_F4:115,KEY_F5:116,KEY_F6:117,KEY_F7:118,KEY_F8:119,KEY_F9:120,KEY_F10:121,KEY_F11:122,KEY_F12:123,KEY_NUM_LOCK:144,KEY_SCROLL_LOCK:145};
this.revKeys=[];
for(var key in this.keys){
this.revKeys[this.keys[key]]=key;
}
this.fixEvent=function(evt,_25e){
if((!evt)&&(window["event"])){
var evt=window.event;
}
if((evt["type"])&&(evt["type"].indexOf("key")==0)){
evt.keys=this.revKeys;
for(var key in this.keys){
evt[key]=this.keys[key];
}
if((dojo.render.html.ie)&&(evt["type"]=="keypress")){
evt.charCode=evt.keyCode;
}
}
if(dojo.render.html.ie){
if(!evt.target){
evt.target=evt.srcElement;
}
if(!evt.currentTarget){
evt.currentTarget=(_25e?_25e:evt.srcElement);
}
if(!evt.layerX){
evt.layerX=evt.offsetX;
}
if(!evt.layerY){
evt.layerY=evt.offsetY;
}
var _260=((dojo.render.html.ie55)||(document["compatMode"]=="BackCompat"))?document.body:document.documentElement;
if(!evt.pageX){
evt.pageX=evt.clientX+(_260.scrollLeft||0);
}
if(!evt.pageY){
evt.pageY=evt.clientY+(_260.scrollTop||0);
}
if(evt.type=="mouseover"){
evt.relatedTarget=evt.fromElement;
}
if(evt.type=="mouseout"){
evt.relatedTarget=evt.toElement;
}
this.currentEvent=evt;
evt.callListener=this.callListener;
evt.stopPropagation=this.stopPropagation;
evt.preventDefault=this.preventDefault;
}
return evt;
};
this.stopEvent=function(ev){
if(window.event){
ev.returnValue=false;
ev.cancelBubble=true;
}else{
ev.preventDefault();
ev.stopPropagation();
}
};
};
dojo.provide("dojo.event.*");
dojo.provide("dojo.widget.Manager");
dojo.widget.manager=new function(){
this.widgets=[];
this.widgetIds=[];
this.topWidgets={};
var _262={};
var _263=[];
this.getUniqueId=function(_264){
return _264+"_"+(_262[_264]!=undefined?++_262[_264]:_262[_264]=0);
};
this.add=function(_265){
dojo.profile.start("dojo.widget.manager.add");
this.widgets.push(_265);
if(!_265.extraArgs["id"]){
_265.extraArgs["id"]=_265.extraArgs["ID"];
}
if(_265.widgetId==""){
if(_265["id"]){
_265.widgetId=_265["id"];
}else{
if(_265.extraArgs["id"]){
_265.widgetId=_265.extraArgs["id"];
}else{
_265.widgetId=this.getUniqueId(_265.widgetType);
}
}
}
if(this.widgetIds[_265.widgetId]){
dojo.debug("widget ID collision on ID: "+_265.widgetId);
}
this.widgetIds[_265.widgetId]=_265;
dojo.profile.end("dojo.widget.manager.add");
};
this.destroyAll=function(){
for(var x=this.widgets.length-1;x>=0;x--){
try{
this.widgets[x].destroy(true);
delete this.widgets[x];
}
catch(e){
}
}
};
this.remove=function(_267){
var tw=this.widgets[_267].widgetId;
delete this.widgetIds[tw];
this.widgets.splice(_267,1);
};
this.removeById=function(id){
for(var i=0;i<this.widgets.length;i++){
if(this.widgets[i].widgetId==id){
this.remove(i);
break;
}
}
};
this.getWidgetById=function(id){
return this.widgetIds[id];
};
this.getWidgetsByType=function(type){
var lt=type.toLowerCase();
var ret=[];
dojo.lang.forEach(this.widgets,function(x){
if(x.widgetType.toLowerCase()==lt){
ret.push(x);
}
});
return ret;
};
this.getWidgetsOfType=function(id){
dojo.deprecated("getWidgetsOfType","use getWidgetsByType","0.4");
return dojo.widget.manager.getWidgetsByType(id);
};
this.getWidgetsByFilter=function(_271,_272){
var ret=[];
dojo.lang.every(this.widgets,function(x){
if(_271(x)){
ret.push(x);
if(_272){
return false;
}
}
return true;
});
return (_272?ret[0]:ret);
};
this.getAllWidgets=function(){
return this.widgets.concat();
};
this.getWidgetByNode=function(node){
var w=this.getAllWidgets();
for(var i=0;i<w.length;i++){
if(w[i].domNode==node){
return w[i];
}
}
return null;
};
this.byId=this.getWidgetById;
this.byType=this.getWidgetsByType;
this.byFilter=this.getWidgetsByFilter;
this.byNode=this.getWidgetByNode;
var _278={};
var _279=["dojo.widget"];
for(var i=0;i<_279.length;i++){
_279[_279[i]]=true;
}
this.registerWidgetPackage=function(_27b){
if(!_279[_27b]){
_279[_27b]=true;
_279.push(_27b);
}
};
this.getWidgetPackageList=function(){
return dojo.lang.map(_279,function(elt){
return (elt!==true?elt:undefined);
});
};
this.getImplementation=function(_27d,_27e,_27f){
var impl=this.getImplementationName(_27d);
if(impl){
var ret=new impl(_27e);
return ret;
}
};
this.getImplementationName=function(_282){
var _283=_282.toLowerCase();
var impl=_278[_283];
if(impl){
return impl;
}
if(!_263.length){
for(var _285 in dojo.render){
if(dojo.render[_285]["capable"]===true){
var _286=dojo.render[_285].prefixes;
for(var i=0;i<_286.length;i++){
_263.push(_286[i].toLowerCase());
}
}
}
_263.push("");
}
for(var i=0;i<_279.length;i++){
var _288=dojo.evalObjPath(_279[i]);
if(!_288){
continue;
}
for(var j=0;j<_263.length;j++){
if(!_288[_263[j]]){
continue;
}
for(var _28a in _288[_263[j]]){
if(_28a.toLowerCase()!=_283){
continue;
}
_278[_283]=_288[_263[j]][_28a];
return _278[_283];
}
}
for(var j=0;j<_263.length;j++){
for(var _28a in _288){
if(_28a.toLowerCase()!=(_263[j]+_283)){
continue;
}
_278[_283]=_288[_28a];
return _278[_283];
}
}
}
throw new Error("Could not locate \""+_282+"\" class");
};
this.resizing=false;
this.onWindowResized=function(){
if(this.resizing){
return;
}
try{
this.resizing=true;
for(var id in this.topWidgets){
var _28c=this.topWidgets[id];
if(_28c.checkSize){
_28c.checkSize();
}
}
}
catch(e){
}
finally{
this.resizing=false;
}
};
if(typeof window!="undefined"){
dojo.addOnLoad(this,"onWindowResized");
dojo.event.connect(window,"onresize",this,"onWindowResized");
}
};
(function(){
var dw=dojo.widget;
var dwm=dw.manager;
var h=dojo.lang.curry(dojo.lang,"hitch",dwm);
var g=function(_291,_292){
dw[(_292||_291)]=h(_291);
};
g("add","addWidget");
g("destroyAll","destroyAllWidgets");
g("remove","removeWidget");
g("removeById","removeWidgetById");
g("getWidgetById");
g("getWidgetById","byId");
g("getWidgetsByType");
g("getWidgetsByFilter");
g("getWidgetsByType","byType");
g("getWidgetsByFilter","byFilter");
g("getWidgetByNode","byNode");
dw.all=function(n){
var _294=dwm.getAllWidgets.apply(dwm,arguments);
if(arguments.length>0){
return _294[n];
}
return _294;
};
g("registerWidgetPackage");
g("getImplementation","getWidgetImplementation");
g("getImplementationName","getWidgetImplementationName");
dw.widgets=dwm.widgets;
dw.widgetIds=dwm.widgetIds;
dw.root=dwm.root;
})();
dojo.provide("dojo.widget.Widget");
dojo.provide("dojo.widget.tags");
dojo.declare("dojo.widget.Widget",null,{initializer:function(){
this.children=[];
this.extraArgs={};
},parent:null,isTopLevel:false,isModal:false,isEnabled:true,isHidden:false,isContainer:false,widgetId:"",widgetType:"Widget",toString:function(){
return "[Widget "+this.widgetType+", "+(this.widgetId||"NO ID")+"]";
},repr:function(){
return this.toString();
},enable:function(){
this.isEnabled=true;
},disable:function(){
this.isEnabled=false;
},hide:function(){
this.isHidden=true;
},show:function(){
this.isHidden=false;
},onResized:function(){
this.notifyChildrenOfResize();
},notifyChildrenOfResize:function(){
for(var i=0;i<this.children.length;i++){
var _296=this.children[i];
if(_296.onResized){
_296.onResized();
}
}
},create:function(args,_298,_299){
this.satisfyPropertySets(args,_298,_299);
this.mixInProperties(args,_298,_299);
this.postMixInProperties(args,_298,_299);
dojo.widget.manager.add(this);
this.buildRendering(args,_298,_299);
this.initialize(args,_298,_299);
this.postInitialize(args,_298,_299);
this.postCreate(args,_298,_299);
return this;
},destroy:function(_29a){
this.destroyChildren();
this.uninitialize();
this.destroyRendering(_29a);
dojo.widget.manager.removeById(this.widgetId);
},destroyChildren:function(){
while(this.children.length>0){
var tc=this.children[0];
this.removeChild(tc);
tc.destroy();
}
},getChildrenOfType:function(type,_29d){
var ret=[];
var _29f=dojo.lang.isFunction(type);
if(!_29f){
type=type.toLowerCase();
}
for(var x=0;x<this.children.length;x++){
if(_29f){
if(this.children[x] instanceof type){
ret.push(this.children[x]);
}
}else{
if(this.children[x].widgetType.toLowerCase()==type){
ret.push(this.children[x]);
}
}
if(_29d){
ret=ret.concat(this.children[x].getChildrenOfType(type,_29d));
}
}
return ret;
},getDescendants:function(){
var _2a1=[];
var _2a2=[this];
var elem;
while(elem=_2a2.pop()){
_2a1.push(elem);
dojo.lang.forEach(elem.children,function(elem){
_2a2.push(elem);
});
}
return _2a1;
},satisfyPropertySets:function(args){
return args;
},mixInProperties:function(args,frag){
if((args["fastMixIn"])||(frag["fastMixIn"])){
for(var x in args){
this[x]=args[x];
}
return;
}
var _2a9;
var _2aa=dojo.widget.lcArgsCache[this.widgetType];
if(_2aa==null){
_2aa={};
for(var y in this){
_2aa[((new String(y)).toLowerCase())]=y;
}
dojo.widget.lcArgsCache[this.widgetType]=_2aa;
}
var _2ac={};
for(var x in args){
if(!this[x]){
var y=_2aa[(new String(x)).toLowerCase()];
if(y){
args[y]=args[x];
x=y;
}
}
if(_2ac[x]){
continue;
}
_2ac[x]=true;
if((typeof this[x])!=(typeof _2a9)){
if(typeof args[x]!="string"){
this[x]=args[x];
}else{
if(dojo.lang.isString(this[x])){
this[x]=args[x];
}else{
if(dojo.lang.isNumber(this[x])){
this[x]=new Number(args[x]);
}else{
if(dojo.lang.isBoolean(this[x])){
this[x]=(args[x].toLowerCase()=="false")?false:true;
}else{
if(dojo.lang.isFunction(this[x])){
if(args[x].search(/[^\w\.]+/i)==-1){
this[x]=dojo.evalObjPath(args[x],false);
}else{
var tn=dojo.lang.nameAnonFunc(new Function(args[x]),this);
dojo.event.connect(this,x,this,tn);
}
}else{
if(dojo.lang.isArray(this[x])){
this[x]=args[x].split(";");
}else{
if(this[x] instanceof Date){
this[x]=new Date(Number(args[x]));
}else{
if(typeof this[x]=="object"){
if(this[x] instanceof dojo.uri.Uri){
this[x]=args[x];
}else{
var _2ae=args[x].split(";");
for(var y=0;y<_2ae.length;y++){
var si=_2ae[y].indexOf(":");
if((si!=-1)&&(_2ae[y].length>si)){
this[x][_2ae[y].substr(0,si).replace(/^\s+|\s+$/g,"")]=_2ae[y].substr(si+1);
}
}
}
}else{
this[x]=args[x];
}
}
}
}
}
}
}
}
}else{
this.extraArgs[x.toLowerCase()]=args[x];
}
}
},postMixInProperties:function(){
},initialize:function(args,frag){
return false;
},postInitialize:function(args,frag){
return false;
},postCreate:function(args,frag){
return false;
},uninitialize:function(){
return false;
},buildRendering:function(){
dojo.unimplemented("dojo.widget.Widget.buildRendering, on "+this.toString()+", ");
return false;
},destroyRendering:function(){
dojo.unimplemented("dojo.widget.Widget.destroyRendering");
return false;
},cleanUp:function(){
dojo.unimplemented("dojo.widget.Widget.cleanUp");
return false;
},addedTo:function(_2b6){
},addChild:function(_2b7){
dojo.unimplemented("dojo.widget.Widget.addChild");
return false;
},removeChild:function(_2b8){
for(var x=0;x<this.children.length;x++){
if(this.children[x]===_2b8){
this.children.splice(x,1);
break;
}
}
return _2b8;
},resize:function(_2ba,_2bb){
this.setWidth(_2ba);
this.setHeight(_2bb);
},setWidth:function(_2bc){
if((typeof _2bc=="string")&&(_2bc.substr(-1)=="%")){
this.setPercentageWidth(_2bc);
}else{
this.setNativeWidth(_2bc);
}
},setHeight:function(_2bd){
if((typeof _2bd=="string")&&(_2bd.substr(-1)=="%")){
this.setPercentageHeight(_2bd);
}else{
this.setNativeHeight(_2bd);
}
},setPercentageHeight:function(_2be){
return false;
},setNativeHeight:function(_2bf){
return false;
},setPercentageWidth:function(_2c0){
return false;
},setNativeWidth:function(_2c1){
return false;
},getPreviousSibling:function(){
var idx=this.getParentIndex();
if(idx<=0){
return null;
}
return this.getSiblings()[idx-1];
},getSiblings:function(){
return this.parent.children;
},getParentIndex:function(){
return dojo.lang.indexOf(this.getSiblings(),this,true);
},getNextSibling:function(){
var idx=this.getParentIndex();
if(idx==this.getSiblings().length-1){
return null;
}
if(idx<0){
return null;
}
return this.getSiblings()[idx+1];
}});
dojo.widget.lcArgsCache={};
dojo.widget.tags={};
dojo.widget.tags.addParseTreeHandler=function(type){
var _2c5=type.toLowerCase();
this[_2c5]=function(_2c6,_2c7,_2c8,_2c9,_2ca){
return dojo.widget.buildWidgetFromParseTree(_2c5,_2c6,_2c7,_2c8,_2c9,_2ca);
};
};
dojo.widget.tags.addParseTreeHandler("dojo:widget");
dojo.widget.tags["dojo:propertyset"]=function(_2cb,_2cc,_2cd){
var _2ce=_2cc.parseProperties(_2cb["dojo:propertyset"]);
};
dojo.widget.tags["dojo:connect"]=function(_2cf,_2d0,_2d1){
var _2d2=_2d0.parseProperties(_2cf["dojo:connect"]);
};
dojo.widget.buildWidgetFromParseTree=function(type,frag,_2d5,_2d6,_2d7,_2d8){
var _2d9=type.split(":");
_2d9=(_2d9.length==2)?_2d9[1]:type;
var _2da=_2d8||_2d5.parseProperties(frag["dojo:"+_2d9]);
var _2db=dojo.widget.manager.getImplementation(_2d9);
if(!_2db){
throw new Error("cannot find \""+_2d9+"\" widget");
}else{
if(!_2db.create){
throw new Error("\""+_2d9+"\" widget object does not appear to implement *Widget");
}
}
_2da["dojoinsertionindex"]=_2d7;
var ret=_2db.create(_2da,frag,_2d6);
return ret;
};
dojo.widget.defineWidget=function(_2dd,_2de,_2df,init,_2e1){
if(dojo.lang.isString(arguments[3])){
dojo.widget._defineWidget(arguments[0],arguments[3],arguments[1],arguments[4],arguments[2]);
}else{
var args=[arguments[0]],p=3;
if(dojo.lang.isString(arguments[1])){
args.push(arguments[1],arguments[2]);
}else{
args.push("",arguments[1]);
p=2;
}
if(dojo.lang.isFunction(arguments[p])){
args.push(arguments[p],arguments[p+1]);
}else{
args.push(null,arguments[p]);
}
dojo.widget._defineWidget.apply(this,args);
}
};
dojo.widget.defineWidget.renderers="html|svg|vml";
dojo.widget._defineWidget=function(_2e4,_2e5,_2e6,init,_2e8){
var _2e9=_2e4.split(".");
var type=_2e9.pop();
var regx="\\.("+(_2e5?_2e5+"|":"")+dojo.widget.defineWidget.renderers+")\\.";
var r=_2e4.search(new RegExp(regx));
_2e9=(r<0?_2e9.join("."):_2e4.substr(0,r));
dojo.widget.manager.registerWidgetPackage(_2e9);
dojo.widget.tags.addParseTreeHandler("dojo:"+type.toLowerCase());
_2e8=(_2e8)||{};
_2e8.widgetType=type;
if((!init)&&(_2e8["classConstructor"])){
init=_2e8.classConstructor;
delete _2e8.classConstructor;
}
dojo.declare(_2e4,_2e6,init,_2e8);
};
dojo.provide("dojo.widget.Parse");
dojo.widget.Parse=function(_2ed){
this.propertySetsList=[];
this.fragment=_2ed;
this.createComponents=function(frag,_2ef){
var _2f0=[];
var _2f1=false;
try{
if((frag)&&(frag["tagName"])&&(frag!=frag["nodeRef"])){
var _2f2=dojo.widget.tags;
var tna=String(frag["tagName"]).split(";");
for(var x=0;x<tna.length;x++){
var ltn=(tna[x].replace(/^\s+|\s+$/g,"")).toLowerCase();
if(_2f2[ltn]){
_2f1=true;
frag.tagName=ltn;
var ret=_2f2[ltn](frag,this,_2ef,frag["index"]);
_2f0.push(ret);
}else{
if((dojo.lang.isString(ltn))&&(ltn.substr(0,5)=="dojo:")){
dojo.debug("no tag handler registed for type: ",ltn);
}
}
}
}
}
catch(e){
dojo.debug("dojo.widget.Parse: error:",e);
}
if(!_2f1){
_2f0=_2f0.concat(this.createSubComponents(frag,_2ef));
}
return _2f0;
};
this.createSubComponents=function(_2f7,_2f8){
var frag,_2fa=[];
for(var item in _2f7){
frag=_2f7[item];
if((frag)&&(typeof frag=="object")&&(frag!=_2f7.nodeRef)&&(frag!=_2f7["tagName"])){
_2fa=_2fa.concat(this.createComponents(frag,_2f8));
}
}
return _2fa;
};
this.parsePropertySets=function(_2fc){
return [];
var _2fd=[];
for(var item in _2fc){
if((_2fc[item]["tagName"]=="dojo:propertyset")){
_2fd.push(_2fc[item]);
}
}
this.propertySetsList.push(_2fd);
return _2fd;
};
this.parseProperties=function(_2ff){
var _300={};
for(var item in _2ff){
if((_2ff[item]==_2ff["tagName"])||(_2ff[item]==_2ff.nodeRef)){
}else{
if((_2ff[item]["tagName"])&&(dojo.widget.tags[_2ff[item].tagName.toLowerCase()])){
}else{
if((_2ff[item][0])&&(_2ff[item][0].value!="")&&(_2ff[item][0].value!=null)){
try{
if(item.toLowerCase()=="dataprovider"){
var _302=this;
this.getDataProvider(_302,_2ff[item][0].value);
_300.dataProvider=this.dataProvider;
}
_300[item]=_2ff[item][0].value;
var _303=this.parseProperties(_2ff[item]);
for(var _304 in _303){
_300[_304]=_303[_304];
}
}
catch(e){
dojo.debug(e);
}
}
}
}
}
return _300;
};
this.getDataProvider=function(_305,_306){
dojo.io.bind({url:_306,load:function(type,_308){
if(type=="load"){
_305.dataProvider=_308;
}
},mimetype:"text/javascript",sync:true});
};
this.getPropertySetById=function(_309){
for(var x=0;x<this.propertySetsList.length;x++){
if(_309==this.propertySetsList[x]["id"][0].value){
return this.propertySetsList[x];
}
}
return "";
};
this.getPropertySetsByType=function(_30b){
var _30c=[];
for(var x=0;x<this.propertySetsList.length;x++){
var cpl=this.propertySetsList[x];
var cpcc=cpl["componentClass"]||cpl["componentType"]||null;
if((cpcc)&&(propertySetId==cpcc[0].value)){
_30c.push(cpl);
}
}
return _30c;
};
this.getPropertySets=function(_310){
var ppl="dojo:propertyproviderlist";
var _312=[];
var _313=_310["tagName"];
if(_310[ppl]){
var _314=_310[ppl].value.split(" ");
for(var _315 in _314){
if((_315.indexOf("..")==-1)&&(_315.indexOf("://")==-1)){
var _316=this.getPropertySetById(_315);
if(_316!=""){
_312.push(_316);
}
}else{
}
}
}
return (this.getPropertySetsByType(_313)).concat(_312);
};
this.createComponentFromScript=function(_317,_318,_319){
var ltn="dojo:"+_318.toLowerCase();
if(dojo.widget.tags[ltn]){
_319.fastMixIn=true;
return [dojo.widget.tags[ltn](_319,this,null,null,_319)];
}else{
if(ltn.substr(0,5)=="dojo:"){
dojo.debug("no tag handler registed for type: ",ltn);
}
}
};
};
dojo.widget._parser_collection={"dojo":new dojo.widget.Parse()};
dojo.widget.getParser=function(name){
if(!name){
name="dojo";
}
if(!this._parser_collection[name]){
this._parser_collection[name]=new dojo.widget.Parse();
}
return this._parser_collection[name];
};
dojo.widget.createWidget=function(name,_31d,_31e,_31f){
var _320=name.toLowerCase();
var _321="dojo:"+_320;
var _322=(dojo.byId(name)&&(!dojo.widget.tags[_321]));
if((arguments.length==1)&&((typeof name!="string")||(_322))){
var xp=new dojo.xml.Parse();
var tn=(_322)?dojo.byId(name):name;
return dojo.widget.getParser().createComponents(xp.parseElement(tn,null,true))[0];
}
function fromScript(_325,name,_327){
_327[_321]={dojotype:[{value:_320}],nodeRef:_325,fastMixIn:true};
return dojo.widget.getParser().createComponentFromScript(_325,name,_327,true);
}
if(typeof name!="string"&&typeof _31d=="string"){
dojo.deprecated("dojo.widget.createWidget","argument order is now of the form "+"dojo.widget.createWidget(NAME, [PROPERTIES, [REFERENCENODE, [POSITION]]])","0.4");
return fromScript(name,_31d,_31e);
}
_31d=_31d||{};
var _328=false;
var tn=null;
var h=dojo.render.html.capable;
if(h){
tn=document.createElement("span");
}
if(!_31e){
_328=true;
_31e=tn;
if(h){
document.body.appendChild(_31e);
}
}else{
if(_31f){
dojo.dom.insertAtPosition(tn,_31e,_31f);
}else{
tn=_31e;
}
}
var _32a=fromScript(tn,name,_31d);
if(!_32a||!_32a[0]||typeof _32a[0].widgetType=="undefined"){
throw new Error("createWidget: Creation of \""+name+"\" widget failed.");
}
if(_328){
if(_32a[0].domNode.parentNode){
_32a[0].domNode.parentNode.removeChild(_32a[0].domNode);
}
}
return _32a[0];
};
dojo.widget.fromScript=function(name,_32c,_32d,_32e){
dojo.deprecated("dojo.widget.fromScript"," use "+"dojo.widget.createWidget instead","0.4");
return dojo.widget.createWidget(name,_32c,_32d,_32e);
};
dojo.provide("dojo.uri.Uri");
dojo.uri=new function(){
this.joinPath=function(){
var arr=[];
for(var i=0;i<arguments.length;i++){
arr.push(arguments[i]);
}
return arr.join("/").replace(/\/{2,}/g,"/").replace(/((https*|ftps*):)/i,"$1/");
};
this.dojoUri=function(uri){
return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri(),uri);
};
this.Uri=function(){
var uri=arguments[0];
for(var i=1;i<arguments.length;i++){
if(!arguments[i]){
continue;
}
var _334=new dojo.uri.Uri(arguments[i].toString());
var _335=new dojo.uri.Uri(uri.toString());
if(_334.path==""&&_334.scheme==null&&_334.authority==null&&_334.query==null){
if(_334.fragment!=null){
_335.fragment=_334.fragment;
}
_334=_335;
}else{
if(_334.scheme==null){
_334.scheme=_335.scheme;
if(_334.authority==null){
_334.authority=_335.authority;
if(_334.path.charAt(0)!="/"){
var path=_335.path.substring(0,_335.path.lastIndexOf("/")+1)+_334.path;
var segs=path.split("/");
for(var j=0;j<segs.length;j++){
if(segs[j]=="."){
if(j==segs.length-1){
segs[j]="";
}else{
segs.splice(j,1);
j--;
}
}else{
if(j>0&&!(j==1&&segs[0]=="")&&segs[j]==".."&&segs[j-1]!=".."){
if(j==segs.length-1){
segs.splice(j,1);
segs[j-1]="";
}else{
segs.splice(j-1,2);
j-=2;
}
}
}
}
_334.path=segs.join("/");
}
}
}
}
uri="";
if(_334.scheme!=null){
uri+=_334.scheme+":";
}
if(_334.authority!=null){
uri+="//"+_334.authority;
}
uri+=_334.path;
if(_334.query!=null){
uri+="?"+_334.query;
}
if(_334.fragment!=null){
uri+="#"+_334.fragment;
}
}
this.uri=uri.toString();
var _339="^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$";
var r=this.uri.match(new RegExp(_339));
this.scheme=r[2]||(r[1]?"":null);
this.authority=r[4]||(r[3]?"":null);
this.path=r[5];
this.query=r[7]||(r[6]?"":null);
this.fragment=r[9]||(r[8]?"":null);
if(this.authority!=null){
_339="^((([^:]+:)?([^@]+))@)?([^:]*)(:([0-9]+))?$";
r=this.authority.match(new RegExp(_339));
this.user=r[3]||null;
this.password=r[4]||null;
this.host=r[5];
this.port=r[7]||null;
}
this.toString=function(){
return this.uri;
};
};
};
dojo.provide("dojo.uri.*");
dojo.provide("dojo.widget.DomWidget");
dojo.widget._cssFiles={};
dojo.widget._cssStrings={};
dojo.widget._templateCache={};
dojo.widget.defaultStrings={dojoRoot:dojo.hostenv.getBaseScriptUri(),baseScriptUri:dojo.hostenv.getBaseScriptUri()};
dojo.widget.buildFromTemplate=function(){
dojo.lang.forward("fillFromTemplateCache");
};
dojo.widget.fillFromTemplateCache=function(obj,_33c,_33d,_33e,_33f){
var _340=_33c||obj.templatePath;
var _341=_33d||obj.templateCssPath;
if(_340&&!(_340 instanceof dojo.uri.Uri)){
_340=dojo.uri.dojoUri(_340);
dojo.deprecated("templatePath should be of type dojo.uri.Uri",null,"0.4");
}
if(_341&&!(_341 instanceof dojo.uri.Uri)){
_341=dojo.uri.dojoUri(_341);
dojo.deprecated("templateCssPath should be of type dojo.uri.Uri",null,"0.4");
}
var _342=dojo.widget._templateCache;
if(!obj["widgetType"]){
do{
var _343="__dummyTemplate__"+dojo.widget._templateCache.dummyCount++;
}while(_342[_343]);
obj.widgetType=_343;
}
var wt=obj.widgetType;
if(_341&&!dojo.widget._cssFiles[_341.toString()]){
if((!obj.templateCssString)&&(_341)){
obj.templateCssString=dojo.hostenv.getText(_341);
obj.templateCssPath=null;
}
if((obj["templateCssString"])&&(!obj.templateCssString["loaded"])){
dojo.style.insertCssText(obj.templateCssString,null,_341);
if(!obj.templateCssString){
obj.templateCssString="";
}
obj.templateCssString.loaded=true;
}
dojo.widget._cssFiles[_341.toString()]=true;
}
var ts=_342[wt];
if(!ts){
_342[wt]={"string":null,"node":null};
if(_33f){
ts={};
}else{
ts=_342[wt];
}
}
if((!obj.templateString)&&(!_33f)){
obj.templateString=_33e||ts["string"];
}
if((!obj.templateNode)&&(!_33f)){
obj.templateNode=ts["node"];
}
if((!obj.templateNode)&&(!obj.templateString)&&(_340)){
var _346=dojo.hostenv.getText(_340);
if(_346){
_346=_346.replace(/^\s*<\?xml(\s)+version=[\'\"](\d)*.(\d)*[\'\"](\s)*\?>/im,"");
var _347=_346.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
if(_347){
_346=_347[1];
}
}else{
_346="";
}
obj.templateString=_346;
if(!_33f){
_342[wt]["string"]=_346;
}
}
if((!ts["string"])&&(!_33f)){
ts.string=obj.templateString;
}
};
dojo.widget._templateCache.dummyCount=0;
dojo.widget.attachProperties=["dojoAttachPoint","id"];
dojo.widget.eventAttachProperty="dojoAttachEvent";
dojo.widget.onBuildProperty="dojoOnBuild";
dojo.widget.waiNames=["waiRole","waiState"];
dojo.widget.wai={waiRole:{name:"waiRole",namespace:"http://www.w3.org/TR/xhtml2",alias:"x2",prefix:"wairole:",nsName:"role"},waiState:{name:"waiState",namespace:"http://www.w3.org/2005/07/aaa",alias:"aaa",prefix:"",nsName:"state"},setAttr:function(node,attr,_34a){
if(dojo.render.html.ie){
node.setAttribute(this[attr].alias+":"+this[attr].nsName,this[attr].prefix+_34a);
}else{
node.setAttributeNS(this[attr].namespace,this[attr].nsName,this[attr].prefix+_34a);
}
}};
dojo.widget.attachTemplateNodes=function(_34b,_34c,_34d){
var _34e=dojo.dom.ELEMENT_NODE;
function trim(str){
return str.replace(/^\s+|\s+$/g,"");
}
if(!_34b){
_34b=_34c.domNode;
}
if(_34b.nodeType!=_34e){
return;
}
var _350=_34b.all||_34b.getElementsByTagName("*");
var _351=_34c;
for(var x=-1;x<_350.length;x++){
var _353=(x==-1)?_34b:_350[x];
var _354=[];
for(var y=0;y<this.attachProperties.length;y++){
var _356=_353.getAttribute(this.attachProperties[y]);
if(_356){
_354=_356.split(";");
for(var z=0;z<_354.length;z++){
if(dojo.lang.isArray(_34c[_354[z]])){
_34c[_354[z]].push(_353);
}else{
_34c[_354[z]]=_353;
}
}
break;
}
}
var _358=_353.getAttribute(this.templateProperty);
if(_358){
_34c[_358]=_353;
}
dojo.lang.forEach(dojo.widget.waiNames,function(name){
var wai=dojo.widget.wai[name];
var val=_353.getAttribute(wai.name);
if(val){
dojo.widget.wai.setAttr(_353,wai.name,val);
}
},this);
var _35c=_353.getAttribute(this.eventAttachProperty);
if(_35c){
var evts=_35c.split(";");
for(var y=0;y<evts.length;y++){
if((!evts[y])||(!evts[y].length)){
continue;
}
var _35e=null;
var tevt=trim(evts[y]);
if(evts[y].indexOf(":")>=0){
var _360=tevt.split(":");
tevt=trim(_360[0]);
_35e=trim(_360[1]);
}
if(!_35e){
_35e=tevt;
}
var tf=function(){
var ntf=new String(_35e);
return function(evt){
if(_351[ntf]){
_351[ntf](dojo.event.browser.fixEvent(evt,this));
}
};
}();
dojo.event.browser.addListener(_353,tevt,tf,false,true);
}
}
for(var y=0;y<_34d.length;y++){
var _364=_353.getAttribute(_34d[y]);
if((_364)&&(_364.length)){
var _35e=null;
var _365=_34d[y].substr(4);
_35e=trim(_364);
var _366=[_35e];
if(_35e.indexOf(";")>=0){
_366=dojo.lang.map(_35e.split(";"),trim);
}
for(var z=0;z<_366.length;z++){
if(!_366[z].length){
continue;
}
var tf=function(){
var ntf=new String(_366[z]);
return function(evt){
if(_351[ntf]){
_351[ntf](dojo.event.browser.fixEvent(evt,this));
}
};
}();
dojo.event.browser.addListener(_353,_365,tf,false,true);
}
}
}
var _369=_353.getAttribute(this.onBuildProperty);
if(_369){
eval("var node = baseNode; var widget = targetObj; "+_369);
}
}
};
dojo.widget.getDojoEventsFromStr=function(str){
var re=/(dojoOn([a-z]+)(\s?))=/gi;
var evts=str?str.match(re)||[]:[];
var ret=[];
var lem={};
for(var x=0;x<evts.length;x++){
if(evts[x].legth<1){
continue;
}
var cm=evts[x].replace(/\s/,"");
cm=(cm.slice(0,cm.length-1));
if(!lem[cm]){
lem[cm]=true;
ret.push(cm);
}
}
return ret;
};
dojo.declare("dojo.widget.DomWidget",dojo.widget.Widget,{initializer:function(){
if((arguments.length>0)&&(typeof arguments[0]=="object")){
this.create(arguments[0]);
}
},templateNode:null,templateString:null,templateCssString:null,preventClobber:false,domNode:null,containerNode:null,addChild:function(_371,_372,pos,ref,_375){
if(!this.isContainer){
dojo.debug("dojo.widget.DomWidget.addChild() attempted on non-container widget");
return null;
}else{
this.addWidgetAsDirectChild(_371,_372,pos,ref,_375);
this.registerChild(_371,_375);
}
return _371;
},addWidgetAsDirectChild:function(_376,_377,pos,ref,_37a){
if((!this.containerNode)&&(!_377)){
this.containerNode=this.domNode;
}
var cn=(_377)?_377:this.containerNode;
if(!pos){
pos="after";
}
if(!ref){
if(!cn){
cn=document.body;
}
ref=cn.lastChild;
}
if(!_37a){
_37a=0;
}
_376.domNode.setAttribute("dojoinsertionindex",_37a);
if(!ref){
cn.appendChild(_376.domNode);
}else{
if(pos=="insertAtIndex"){
dojo.dom.insertAtIndex(_376.domNode,ref.parentNode,_37a);
}else{
if((pos=="after")&&(ref===cn.lastChild)){
cn.appendChild(_376.domNode);
}else{
dojo.dom.insertAtPosition(_376.domNode,cn,pos);
}
}
}
},registerChild:function(_37c,_37d){
_37c.dojoInsertionIndex=_37d;
var idx=-1;
for(var i=0;i<this.children.length;i++){
if(this.children[i].dojoInsertionIndex<_37d){
idx=i;
}
}
this.children.splice(idx+1,0,_37c);
_37c.parent=this;
_37c.addedTo(this);
delete dojo.widget.manager.topWidgets[_37c.widgetId];
},removeChild:function(_380){
dojo.dom.removeNode(_380.domNode);
return dojo.widget.DomWidget.superclass.removeChild.call(this,_380);
},getFragNodeRef:function(frag){
if(!frag||!frag["dojo:"+this.widgetType.toLowerCase()]){
dojo.raise("Error: no frag for widget type "+this.widgetType+", id "+this.widgetId+" (maybe a widget has set it's type incorrectly)");
}
return (frag?frag["dojo:"+this.widgetType.toLowerCase()]["nodeRef"]:null);
},postInitialize:function(args,frag,_384){
var _385=this.getFragNodeRef(frag);
if(_384&&(_384.snarfChildDomOutput||!_385)){
_384.addWidgetAsDirectChild(this,"","insertAtIndex","",args["dojoinsertionindex"],_385);
}else{
if(_385){
if(this.domNode&&(this.domNode!==_385)){
var _386=_385.parentNode.replaceChild(this.domNode,_385);
}
}
}
if(_384){
_384.registerChild(this,args.dojoinsertionindex);
}else{
dojo.widget.manager.topWidgets[this.widgetId]=this;
}
if(this.isContainer){
var _387=dojo.widget.getParser();
_387.createSubComponents(frag,this);
}
},buildRendering:function(args,frag){
var ts=dojo.widget._templateCache[this.widgetType];
if((!this.preventClobber)&&((this.templatePath)||(this.templateNode)||((this["templateString"])&&(this.templateString.length))||((typeof ts!="undefined")&&((ts["string"])||(ts["node"]))))){
this.buildFromTemplate(args,frag);
}else{
this.domNode=this.getFragNodeRef(frag);
}
this.fillInTemplate(args,frag);
},buildFromTemplate:function(args,frag){
var _38d=false;
if(args["templatecsspath"]){
args["templateCssPath"]=args["templatecsspath"];
}
if(args["templatepath"]){
_38d=true;
args["templatePath"]=args["templatepath"];
}
dojo.widget.fillFromTemplateCache(this,args["templatePath"],args["templateCssPath"],null,_38d);
var ts=dojo.widget._templateCache[this.widgetType];
if((ts)&&(!_38d)){
if(!this.templateString.length){
this.templateString=ts["string"];
}
if(!this.templateNode){
this.templateNode=ts["node"];
}
}
var _38f=false;
var node=null;
var tstr=this.templateString;
if((!this.templateNode)&&(this.templateString)){
_38f=this.templateString.match(/\$\{([^\}]+)\}/g);
if(_38f){
var hash=this.strings||{};
for(var key in dojo.widget.defaultStrings){
if(dojo.lang.isUndefined(hash[key])){
hash[key]=dojo.widget.defaultStrings[key];
}
}
for(var i=0;i<_38f.length;i++){
var key=_38f[i];
key=key.substring(2,key.length-1);
var kval=(key.substring(0,5)=="this.")?dojo.lang.getObjPathValue(key.substring(5),this):hash[key];
var _396;
if((kval)||(dojo.lang.isString(kval))){
_396=(dojo.lang.isFunction(kval))?kval.call(this,key,this.templateString):kval;
tstr=tstr.replace(_38f[i],_396);
}
}
}else{
this.templateNode=this.createNodesFromText(this.templateString,true)[0];
if(!_38d){
ts.node=this.templateNode;
}
}
}
if((!this.templateNode)&&(!_38f)){
dojo.debug("weren't able to create template!");
return false;
}else{
if(!_38f){
node=this.templateNode.cloneNode(true);
if(!node){
return false;
}
}else{
node=this.createNodesFromText(tstr,true)[0];
}
}
this.domNode=node;
this.attachTemplateNodes(this.domNode,this);
if(this.isContainer&&this.containerNode){
var src=this.getFragNodeRef(frag);
if(src){
dojo.dom.moveChildren(src,this.containerNode);
}
}
},attachTemplateNodes:function(_398,_399){
if(!_399){
_399=this;
}
return dojo.widget.attachTemplateNodes(_398,_399,dojo.widget.getDojoEventsFromStr(this.templateString));
},fillInTemplate:function(){
},destroyRendering:function(){
try{
delete this.domNode;
}
catch(e){
}
},cleanUp:function(){
},getContainerHeight:function(){
dojo.unimplemented("dojo.widget.DomWidget.getContainerHeight");
},getContainerWidth:function(){
dojo.unimplemented("dojo.widget.DomWidget.getContainerWidth");
},createNodesFromText:function(){
dojo.unimplemented("dojo.widget.DomWidget.createNodesFromText");
}});
dojo.provide("dojo.graphics.color");
dojo.graphics.color.Color=function(r,g,b,a){
if(dojo.lang.isArray(r)){
this.r=r[0];
this.g=r[1];
this.b=r[2];
this.a=r[3]||1;
}else{
if(dojo.lang.isString(r)){
var rgb=dojo.graphics.color.extractRGB(r);
this.r=rgb[0];
this.g=rgb[1];
this.b=rgb[2];
this.a=g||1;
}else{
if(r instanceof dojo.graphics.color.Color){
this.r=r.r;
this.b=r.b;
this.g=r.g;
this.a=r.a;
}else{
this.r=r;
this.g=g;
this.b=b;
this.a=a;
}
}
}
};
dojo.graphics.color.Color.fromArray=function(arr){
return new dojo.graphics.color.Color(arr[0],arr[1],arr[2],arr[3]);
};
dojo.lang.extend(dojo.graphics.color.Color,{toRgb:function(_3a0){
if(_3a0){
return this.toRgba();
}else{
return [this.r,this.g,this.b];
}
},toRgba:function(){
return [this.r,this.g,this.b,this.a];
},toHex:function(){
return dojo.graphics.color.rgb2hex(this.toRgb());
},toCss:function(){
return "rgb("+this.toRgb().join()+")";
},toString:function(){
return this.toHex();
},blend:function(_3a1,_3a2){
return dojo.graphics.color.blend(this.toRgb(),new dojo.graphics.color.Color(_3a1).toRgb(),_3a2);
}});
dojo.graphics.color.named={white:[255,255,255],black:[0,0,0],red:[255,0,0],green:[0,255,0],blue:[0,0,255],navy:[0,0,128],gray:[128,128,128],silver:[192,192,192]};
dojo.graphics.color.blend=function(a,b,_3a5){
if(typeof a=="string"){
return dojo.graphics.color.blendHex(a,b,_3a5);
}
if(!_3a5){
_3a5=0;
}else{
if(_3a5>1){
_3a5=1;
}else{
if(_3a5<-1){
_3a5=-1;
}
}
}
var c=new Array(3);
for(var i=0;i<3;i++){
var half=Math.abs(a[i]-b[i])/2;
c[i]=Math.floor(Math.min(a[i],b[i])+half+(half*_3a5));
}
return c;
};
dojo.graphics.color.blendHex=function(a,b,_3ab){
return dojo.graphics.color.rgb2hex(dojo.graphics.color.blend(dojo.graphics.color.hex2rgb(a),dojo.graphics.color.hex2rgb(b),_3ab));
};
dojo.graphics.color.extractRGB=function(_3ac){
var hex="0123456789abcdef";
_3ac=_3ac.toLowerCase();
if(_3ac.indexOf("rgb")==0){
var _3ae=_3ac.match(/rgba*\((\d+), *(\d+), *(\d+)/i);
var ret=_3ae.splice(1,3);
return ret;
}else{
var _3b0=dojo.graphics.color.hex2rgb(_3ac);
if(_3b0){
return _3b0;
}else{
return dojo.graphics.color.named[_3ac]||[255,255,255];
}
}
};
dojo.graphics.color.hex2rgb=function(hex){
var _3b2="0123456789ABCDEF";
var rgb=new Array(3);
if(hex.indexOf("#")==0){
hex=hex.substring(1);
}
hex=hex.toUpperCase();
if(hex.replace(new RegExp("["+_3b2+"]","g"),"")!=""){
return null;
}
if(hex.length==3){
rgb[0]=hex.charAt(0)+hex.charAt(0);
rgb[1]=hex.charAt(1)+hex.charAt(1);
rgb[2]=hex.charAt(2)+hex.charAt(2);
}else{
rgb[0]=hex.substring(0,2);
rgb[1]=hex.substring(2,4);
rgb[2]=hex.substring(4);
}
for(var i=0;i<rgb.length;i++){
rgb[i]=_3b2.indexOf(rgb[i].charAt(0))*16+_3b2.indexOf(rgb[i].charAt(1));
}
return rgb;
};
dojo.graphics.color.rgb2hex=function(r,g,b){
if(dojo.lang.isArray(r)){
g=r[1]||0;
b=r[2]||0;
r=r[0]||0;
}
var ret=dojo.lang.map([r,g,b],function(x){
x=new Number(x);
var s=x.toString(16);
while(s.length<2){
s="0"+s;
}
return s;
});
ret.unshift("#");
return ret.join("");
};
dojo.provide("dojo.style");
(function(){
var h=dojo.render.html;
var ds=dojo.style;
var db=document["body"]||document["documentElement"];
ds.boxSizing={MARGIN_BOX:"margin-box",BORDER_BOX:"border-box",PADDING_BOX:"padding-box",CONTENT_BOX:"content-box"};
var bs=ds.boxSizing;
ds.getBoxSizing=function(node){
if((h.ie)||(h.opera)){
var cm=document["compatMode"];
if((cm=="BackCompat")||(cm=="QuirksMode")){
return bs.BORDER_BOX;
}else{
return bs.CONTENT_BOX;
}
}else{
if(arguments.length==0){
node=document.documentElement;
}
var _3c1=ds.getStyle(node,"-moz-box-sizing");
if(!_3c1){
_3c1=ds.getStyle(node,"box-sizing");
}
return (_3c1?_3c1:bs.CONTENT_BOX);
}
};
ds.isBorderBox=function(node){
return (ds.getBoxSizing(node)==bs.BORDER_BOX);
};
ds.getUnitValue=function(node,_3c4,_3c5){
var s=ds.getComputedStyle(node,_3c4);
if((!s)||((s=="auto")&&(_3c5))){
return {value:0,units:"px"};
}
if(dojo.lang.isUndefined(s)){
return ds.getUnitValue.bad;
}
var _3c7=s.match(/(\-?[\d.]+)([a-z%]*)/i);
if(!_3c7){
return ds.getUnitValue.bad;
}
return {value:Number(_3c7[1]),units:_3c7[2].toLowerCase()};
};
ds.getUnitValue.bad={value:NaN,units:""};
ds.getPixelValue=function(node,_3c9,_3ca){
var _3cb=ds.getUnitValue(node,_3c9,_3ca);
if(isNaN(_3cb.value)){
return 0;
}
if((_3cb.value)&&(_3cb.units!="px")){
return NaN;
}
return _3cb.value;
};
ds.getNumericStyle=function(){
dojo.deprecated("dojo.(style|html).getNumericStyle","in favor of dojo.(style|html).getPixelValue","0.4");
return ds.getPixelValue.apply(this,arguments);
};
ds.setPositivePixelValue=function(node,_3cd,_3ce){
if(isNaN(_3ce)){
return false;
}
node.style[_3cd]=Math.max(0,_3ce)+"px";
return true;
};
ds._sumPixelValues=function(node,_3d0,_3d1){
var _3d2=0;
for(var x=0;x<_3d0.length;x++){
_3d2+=ds.getPixelValue(node,_3d0[x],_3d1);
}
return _3d2;
};
ds.isPositionAbsolute=function(node){
return (ds.getComputedStyle(node,"position")=="absolute");
};
ds.getBorderExtent=function(node,side){
return (ds.getStyle(node,"border-"+side+"-style")=="none"?0:ds.getPixelValue(node,"border-"+side+"-width"));
};
ds.getMarginWidth=function(node){
return ds._sumPixelValues(node,["margin-left","margin-right"],ds.isPositionAbsolute(node));
};
ds.getBorderWidth=function(node){
return ds.getBorderExtent(node,"left")+ds.getBorderExtent(node,"right");
};
ds.getPaddingWidth=function(node){
return ds._sumPixelValues(node,["padding-left","padding-right"],true);
};
ds.getPadBorderWidth=function(node){
return ds.getPaddingWidth(node)+ds.getBorderWidth(node);
};
ds.getContentBoxWidth=function(node){
node=dojo.byId(node);
return node.offsetWidth-ds.getPadBorderWidth(node);
};
ds.getBorderBoxWidth=function(node){
node=dojo.byId(node);
return node.offsetWidth;
};
ds.getMarginBoxWidth=function(node){
return ds.getInnerWidth(node)+ds.getMarginWidth(node);
};
ds.setContentBoxWidth=function(node,_3df){
node=dojo.byId(node);
if(ds.isBorderBox(node)){
_3df+=ds.getPadBorderWidth(node);
}
return ds.setPositivePixelValue(node,"width",_3df);
};
ds.setMarginBoxWidth=function(node,_3e1){
node=dojo.byId(node);
if(!ds.isBorderBox(node)){
_3e1-=ds.getPadBorderWidth(node);
}
_3e1-=ds.getMarginWidth(node);
return ds.setPositivePixelValue(node,"width",_3e1);
};
ds.getContentWidth=ds.getContentBoxWidth;
ds.getInnerWidth=ds.getBorderBoxWidth;
ds.getOuterWidth=ds.getMarginBoxWidth;
ds.setContentWidth=ds.setContentBoxWidth;
ds.setOuterWidth=ds.setMarginBoxWidth;
ds.getMarginHeight=function(node){
return ds._sumPixelValues(node,["margin-top","margin-bottom"],ds.isPositionAbsolute(node));
};
ds.getBorderHeight=function(node){
return ds.getBorderExtent(node,"top")+ds.getBorderExtent(node,"bottom");
};
ds.getPaddingHeight=function(node){
return ds._sumPixelValues(node,["padding-top","padding-bottom"],true);
};
ds.getPadBorderHeight=function(node){
return ds.getPaddingHeight(node)+ds.getBorderHeight(node);
};
ds.getContentBoxHeight=function(node){
node=dojo.byId(node);
return node.offsetHeight-ds.getPadBorderHeight(node);
};
ds.getBorderBoxHeight=function(node){
node=dojo.byId(node);
return node.offsetHeight;
};
ds.getMarginBoxHeight=function(node){
return ds.getInnerHeight(node)+ds.getMarginHeight(node);
};
ds.setContentBoxHeight=function(node,_3ea){
node=dojo.byId(node);
if(ds.isBorderBox(node)){
_3ea+=ds.getPadBorderHeight(node);
}
return ds.setPositivePixelValue(node,"height",_3ea);
};
ds.setMarginBoxHeight=function(node,_3ec){
node=dojo.byId(node);
if(!ds.isBorderBox(node)){
_3ec-=ds.getPadBorderHeight(node);
}
_3ec-=ds.getMarginHeight(node);
return ds.setPositivePixelValue(node,"height",_3ec);
};
ds.getContentHeight=ds.getContentBoxHeight;
ds.getInnerHeight=ds.getBorderBoxHeight;
ds.getOuterHeight=ds.getMarginBoxHeight;
ds.setContentHeight=ds.setContentBoxHeight;
ds.setOuterHeight=ds.setMarginBoxHeight;
ds.getAbsolutePosition=ds.abs=function(node,_3ee){
node=dojo.byId(node);
var ret=[];
ret.x=ret.y=0;
var st=dojo.html.getScrollTop();
var sl=dojo.html.getScrollLeft();
if(h.ie){
with(node.getBoundingClientRect()){
ret.x=left-2;
ret.y=top-2;
}
}else{
if(document.getBoxObjectFor){
var bo=document.getBoxObjectFor(node);
ret.x=bo.x-ds.sumAncestorProperties(node,"scrollLeft");
ret.y=bo.y-ds.sumAncestorProperties(node,"scrollTop");
}else{
if(node["offsetParent"]){
var _3f3;
if((h.safari)&&(node.style.getPropertyValue("position")=="absolute")&&(node.parentNode==db)){
_3f3=db;
}else{
_3f3=db.parentNode;
}
if(node.parentNode!=db){
var nd=node;
if(window.opera){
nd=db;
}
ret.x-=ds.sumAncestorProperties(nd,"scrollLeft");
ret.y-=ds.sumAncestorProperties(nd,"scrollTop");
}
do{
var n=node["offsetLeft"];
ret.x+=isNaN(n)?0:n;
var m=node["offsetTop"];
ret.y+=isNaN(m)?0:m;
node=node.offsetParent;
}while((node!=_3f3)&&(node!=null));
}else{
if(node["x"]&&node["y"]){
ret.x+=isNaN(node.x)?0:node.x;
ret.y+=isNaN(node.y)?0:node.y;
}
}
}
}
if(_3ee){
ret.y+=st;
ret.x+=sl;
}
ret[0]=ret.x;
ret[1]=ret.y;
return ret;
};
ds.sumAncestorProperties=function(node,prop){
node=dojo.byId(node);
if(!node){
return 0;
}
var _3f9=0;
while(node){
var val=node[prop];
if(val){
_3f9+=val-0;
if(node==document.body){
break;
}
}
node=node.parentNode;
}
return _3f9;
};
ds.getTotalOffset=function(node,type,_3fd){
return ds.abs(node,_3fd)[(type=="top")?"y":"x"];
};
ds.getAbsoluteX=ds.totalOffsetLeft=function(node,_3ff){
return ds.getTotalOffset(node,"left",_3ff);
};
ds.getAbsoluteY=ds.totalOffsetTop=function(node,_401){
return ds.getTotalOffset(node,"top",_401);
};
ds.styleSheet=null;
ds.insertCssRule=function(_402,_403,_404){
if(!ds.styleSheet){
if(document.createStyleSheet){
ds.styleSheet=document.createStyleSheet();
}else{
if(document.styleSheets[0]){
ds.styleSheet=document.styleSheets[0];
}else{
return null;
}
}
}
if(arguments.length<3){
if(ds.styleSheet.cssRules){
_404=ds.styleSheet.cssRules.length;
}else{
if(ds.styleSheet.rules){
_404=ds.styleSheet.rules.length;
}else{
return null;
}
}
}
if(ds.styleSheet.insertRule){
var rule=_402+" { "+_403+" }";
return ds.styleSheet.insertRule(rule,_404);
}else{
if(ds.styleSheet.addRule){
return ds.styleSheet.addRule(_402,_403,_404);
}else{
return null;
}
}
};
ds.removeCssRule=function(_406){
if(!ds.styleSheet){
dojo.debug("no stylesheet defined for removing rules");
return false;
}
if(h.ie){
if(!_406){
_406=ds.styleSheet.rules.length;
ds.styleSheet.removeRule(_406);
}
}else{
if(document.styleSheets[0]){
if(!_406){
_406=ds.styleSheet.cssRules.length;
}
ds.styleSheet.deleteRule(_406);
}
}
return true;
};
ds.insertCssFile=function(URI,doc,_409){
if(!URI){
return;
}
if(!doc){
doc=document;
}
var _40a=dojo.hostenv.getText(URI);
_40a=ds.fixPathsInCssText(_40a,URI);
if(_409){
var _40b=doc.getElementsByTagName("style");
var _40c="";
for(var i=0;i<_40b.length;i++){
_40c=(_40b[i].styleSheet&&_40b[i].styleSheet.cssText)?_40b[i].styleSheet.cssText:_40b[i].innerHTML;
if(_40a==_40c){
return;
}
}
}
var _40e=ds.insertCssText(_40a);
if(_40e&&djConfig.isDebug){
_40e.setAttribute("dbgHref",URI);
}
return _40e;
};
ds.insertCssText=function(_40f,doc,URI){
if(!_40f){
return;
}
if(!doc){
doc=document;
}
if(URI){
_40f=ds.fixPathsInCssText(_40f,URI);
}
var _412=doc.createElement("style");
_412.setAttribute("type","text/css");
var head=doc.getElementsByTagName("head")[0];
if(!head){
dojo.debug("No head tag in document, aborting styles");
return;
}else{
head.appendChild(_412);
}
if(_412.styleSheet){
_412.styleSheet.cssText=_40f;
}else{
var _414=doc.createTextNode(_40f);
_412.appendChild(_414);
}
return _412;
};
ds.fixPathsInCssText=function(_415,URI){
if(!_415||!URI){
return;
}
var pos=0;
var str="";
var url="";
while(pos!=-1){
pos=0;
url="";
pos=_415.indexOf("url(",pos);
if(pos<0){
break;
}
str+=_415.slice(0,pos+4);
_415=_415.substring(pos+4,_415.length);
url+=_415.match(/^[\t\s\w()\/.\\'"-:#=&?]*\)/)[0];
_415=_415.substring(url.length-1,_415.length);
url=url.replace(/^[\s\t]*(['"]?)([\w()\/.\\'"-:#=&?]*)\1[\s\t]*?\)/,"$2");
if(url.search(/(file|https?|ftps?):\/\//)==-1){
url=(new dojo.uri.Uri(URI,url).toString());
}
str+=url;
}
return str+_415;
};
ds.getBackgroundColor=function(node){
node=dojo.byId(node);
var _41b;
do{
_41b=ds.getStyle(node,"background-color");
if(_41b.toLowerCase()=="rgba(0, 0, 0, 0)"){
_41b="transparent";
}
if(node==document.getElementsByTagName("body")[0]){
node=null;
break;
}
node=node.parentNode;
}while(node&&dojo.lang.inArray(_41b,["transparent",""]));
if(_41b=="transparent"){
_41b=[255,255,255,0];
}else{
_41b=dojo.graphics.color.extractRGB(_41b);
}
return _41b;
};
ds.getComputedStyle=function(node,_41d,_41e){
node=dojo.byId(node);
var _41d=ds.toSelectorCase(_41d);
var _41f=ds.toCamelCase(_41d);
if(!node||!node.style){
return _41e;
}else{
if(document.defaultView){
try{
var cs=document.defaultView.getComputedStyle(node,"");
if(cs){
return cs.getPropertyValue(_41d);
}
}
catch(e){
if(node.style.getPropertyValue){
return node.style.getPropertyValue(_41d);
}else{
return _41e;
}
}
}else{
if(node.currentStyle){
return node.currentStyle[_41f];
}
}
}
if(node.style.getPropertyValue){
return node.style.getPropertyValue(_41d);
}else{
return _41e;
}
};
ds.getStyleProperty=function(node,_422){
node=dojo.byId(node);
return (node&&node.style?node.style[ds.toCamelCase(_422)]:undefined);
};
ds.getStyle=function(node,_424){
var _425=ds.getStyleProperty(node,_424);
return (_425?_425:ds.getComputedStyle(node,_424));
};
ds.setStyle=function(node,_427,_428){
node=dojo.byId(node);
if(node&&node.style){
var _429=ds.toCamelCase(_427);
node.style[_429]=_428;
}
};
ds.toCamelCase=function(_42a){
var arr=_42a.split("-"),cc=arr[0];
for(var i=1;i<arr.length;i++){
cc+=arr[i].charAt(0).toUpperCase()+arr[i].substring(1);
}
return cc;
};
ds.toSelectorCase=function(_42e){
return _42e.replace(/([A-Z])/g,"-$1").toLowerCase();
};
ds.setOpacity=function setOpacity(node,_430,_431){
node=dojo.byId(node);
if(!_431){
if(_430>=1){
if(h.ie){
ds.clearOpacity(node);
return;
}else{
_430=0.999999;
}
}else{
if(_430<0){
_430=0;
}
}
}
if(h.ie){
if(node.nodeName.toLowerCase()=="tr"){
var tds=node.getElementsByTagName("td");
for(var x=0;x<tds.length;x++){
tds[x].style.filter="Alpha(Opacity="+_430*100+")";
}
}
node.style.filter="Alpha(Opacity="+_430*100+")";
}else{
if(h.moz){
node.style.opacity=_430;
node.style.MozOpacity=_430;
}else{
if(h.safari){
node.style.opacity=_430;
node.style.KhtmlOpacity=_430;
}else{
node.style.opacity=_430;
}
}
}
};
ds.getOpacity=function getOpacity(node){
node=dojo.byId(node);
if(h.ie){
var opac=(node.filters&&node.filters.alpha&&typeof node.filters.alpha.opacity=="number"?node.filters.alpha.opacity:100)/100;
}else{
var opac=node.style.opacity||node.style.MozOpacity||node.style.KhtmlOpacity||1;
}
return opac>=0.999999?1:Number(opac);
};
ds.clearOpacity=function clearOpacity(node){
node=dojo.byId(node);
var ns=node.style;
if(h.ie){
try{
if(node.filters&&node.filters.alpha){
ns.filter="";
}
}
catch(e){
}
}else{
if(h.moz){
ns.opacity=1;
ns.MozOpacity=1;
}else{
if(h.safari){
ns.opacity=1;
ns.KhtmlOpacity=1;
}else{
ns.opacity=1;
}
}
}
};
ds.setStyleAttributes=function(node,_439){
var _43a={"opacity":dojo.style.setOpacity,"content-height":dojo.style.setContentHeight,"content-width":dojo.style.setContentWidth,"outer-height":dojo.style.setOuterHeight,"outer-width":dojo.style.setOuterWidth};
var _43b=_439.replace(/(;)?\s*$/,"").split(";");
for(var i=0;i<_43b.length;i++){
var _43d=_43b[i].split(":");
var name=_43d[0].replace(/\s*$/,"").replace(/^\s*/,"").toLowerCase();
var _43f=_43d[1].replace(/\s*$/,"").replace(/^\s*/,"");
if(dojo.lang.has(_43a,name)){
_43a[name](node,_43f);
}else{
node.style[dojo.style.toCamelCase(name)]=_43f;
}
}
};
ds._toggle=function(node,_441,_442){
node=dojo.byId(node);
_442(node,!_441(node));
return _441(node);
};
ds.show=function(node){
node=dojo.byId(node);
if(ds.getStyleProperty(node,"display")=="none"){
ds.setStyle(node,"display",(node.dojoDisplayCache||""));
node.dojoDisplayCache=undefined;
}
};
ds.hide=function(node){
node=dojo.byId(node);
if(typeof node["dojoDisplayCache"]=="undefined"){
var d=ds.getStyleProperty(node,"display");
if(d!="none"){
node.dojoDisplayCache=d;
}
}
ds.setStyle(node,"display","none");
};
ds.setShowing=function(node,_447){
ds[(_447?"show":"hide")](node);
};
ds.isShowing=function(node){
return (ds.getStyleProperty(node,"display")!="none");
};
ds.toggleShowing=function(node){
return ds._toggle(node,ds.isShowing,ds.setShowing);
};
ds.displayMap={tr:"",td:"",th:"",img:"inline",span:"inline",input:"inline",button:"inline"};
ds.suggestDisplayByTagName=function(node){
node=dojo.byId(node);
if(node&&node.tagName){
var tag=node.tagName.toLowerCase();
return (tag in ds.displayMap?ds.displayMap[tag]:"block");
}
};
ds.setDisplay=function(node,_44d){
ds.setStyle(node,"display",(dojo.lang.isString(_44d)?_44d:(_44d?ds.suggestDisplayByTagName(node):"none")));
};
ds.isDisplayed=function(node){
return (ds.getComputedStyle(node,"display")!="none");
};
ds.toggleDisplay=function(node){
return ds._toggle(node,ds.isDisplayed,ds.setDisplay);
};
ds.setVisibility=function(node,_451){
ds.setStyle(node,"visibility",(dojo.lang.isString(_451)?_451:(_451?"visible":"hidden")));
};
ds.isVisible=function(node){
return (ds.getComputedStyle(node,"visibility")!="hidden");
};
ds.toggleVisibility=function(node){
return ds._toggle(node,ds.isVisible,ds.setVisibility);
};
ds.toCoordinateArray=function(_454,_455){
if(dojo.lang.isArray(_454)){
while(_454.length<4){
_454.push(0);
}
while(_454.length>4){
_454.pop();
}
var ret=_454;
}else{
var node=dojo.byId(_454);
var pos=ds.getAbsolutePosition(node,_455);
var ret=[pos.x,pos.y,ds.getBorderBoxWidth(node),ds.getBorderBoxHeight(node)];
}
ret.x=ret[0];
ret.y=ret[1];
ret.w=ret[2];
ret.h=ret[3];
return ret;
};
})();
dojo.provide("dojo.string.common");
dojo.string.trim=function(str,wh){
if(!str.replace){
return str;
}
if(!str.length){
return str;
}
var re=(wh>0)?(/^\s+/):(wh<0)?(/\s+$/):(/^\s+|\s+$/g);
return str.replace(re,"");
};
dojo.string.trimStart=function(str){
return dojo.string.trim(str,1);
};
dojo.string.trimEnd=function(str){
return dojo.string.trim(str,-1);
};
dojo.string.repeat=function(str,_45f,_460){
var out="";
for(var i=0;i<_45f;i++){
out+=str;
if(_460&&i<_45f-1){
out+=_460;
}
}
return out;
};
dojo.string.pad=function(str,len,c,dir){
var out=String(str);
if(!c){
c="0";
}
if(!dir){
dir=1;
}
while(out.length<len){
if(dir>0){
out=c+out;
}else{
out+=c;
}
}
return out;
};
dojo.string.padLeft=function(str,len,c){
return dojo.string.pad(str,len,c,1);
};
dojo.string.padRight=function(str,len,c){
return dojo.string.pad(str,len,c,-1);
};
dojo.provide("dojo.string");
dojo.provide("dojo.html");
dojo.lang.mixin(dojo.html,dojo.dom);
dojo.lang.mixin(dojo.html,dojo.style);
dojo.html.clearSelection=function(){
try{
if(window["getSelection"]){
if(dojo.render.html.safari){
window.getSelection().collapse();
}else{
window.getSelection().removeAllRanges();
}
}else{
if(document.selection){
if(document.selection.empty){
document.selection.empty();
}else{
if(document.selection.clear){
document.selection.clear();
}
}
}
}
return true;
}
catch(e){
dojo.debug(e);
return false;
}
};
dojo.html.disableSelection=function(_46e){
_46e=dojo.byId(_46e)||document.body;
var h=dojo.render.html;
if(h.mozilla){
_46e.style.MozUserSelect="none";
}else{
if(h.safari){
_46e.style.KhtmlUserSelect="none";
}else{
if(h.ie){
_46e.unselectable="on";
}else{
return false;
}
}
}
return true;
};
dojo.html.enableSelection=function(_470){
_470=dojo.byId(_470)||document.body;
var h=dojo.render.html;
if(h.mozilla){
_470.style.MozUserSelect="";
}else{
if(h.safari){
_470.style.KhtmlUserSelect="";
}else{
if(h.ie){
_470.unselectable="off";
}else{
return false;
}
}
}
return true;
};
dojo.html.selectElement=function(_472){
_472=dojo.byId(_472);
if(document.selection&&document.body.createTextRange){
var _473=document.body.createTextRange();
_473.moveToElementText(_472);
_473.select();
}else{
if(window["getSelection"]){
var _474=window.getSelection();
if(_474["selectAllChildren"]){
_474.selectAllChildren(_472);
}
}
}
};
dojo.html.selectInputText=function(_475){
_475=dojo.byId(_475);
if(document.selection&&document.body.createTextRange){
var _476=_475.createTextRange();
_476.moveStart("character",0);
_476.moveEnd("character",_475.value.length);
_476.select();
}else{
if(window["getSelection"]){
var _477=window.getSelection();
_475.setSelectionRange(0,_475.value.length);
}
}
_475.focus();
};
dojo.html.isSelectionCollapsed=function(){
if(document["selection"]){
return document.selection.createRange().text=="";
}else{
if(window["getSelection"]){
var _478=window.getSelection();
if(dojo.lang.isString(_478)){
return _478=="";
}else{
return _478.isCollapsed;
}
}
}
};
dojo.html.getEventTarget=function(evt){
if(!evt){
evt=window.event||{};
}
var t=(evt.srcElement?evt.srcElement:(evt.target?evt.target:null));
while((t)&&(t.nodeType!=1)){
t=t.parentNode;
}
return t;
};
dojo.html.getDocumentWidth=function(){
dojo.deprecated("dojo.html.getDocument*","replaced by dojo.html.getViewport*","0.4");
return dojo.html.getViewportWidth();
};
dojo.html.getDocumentHeight=function(){
dojo.deprecated("dojo.html.getDocument*","replaced by dojo.html.getViewport*","0.4");
return dojo.html.getViewportHeight();
};
dojo.html.getDocumentSize=function(){
dojo.deprecated("dojo.html.getDocument*","replaced of dojo.html.getViewport*","0.4");
return dojo.html.getViewportSize();
};
dojo.html.getViewportWidth=function(){
var w=0;
if(window.innerWidth){
w=window.innerWidth;
}
if(dojo.exists(document,"documentElement.clientWidth")){
var w2=document.documentElement.clientWidth;
if(!w||w2&&w2<w){
w=w2;
}
return w;
}
if(document.body){
return document.body.clientWidth;
}
return 0;
};
dojo.html.getViewportHeight=function(){
if(window.innerHeight){
return window.innerHeight;
}
if(dojo.exists(document,"documentElement.clientHeight")){
return document.documentElement.clientHeight;
}
if(document.body){
return document.body.clientHeight;
}
return 0;
};
dojo.html.getViewportSize=function(){
var ret=[dojo.html.getViewportWidth(),dojo.html.getViewportHeight()];
ret.w=ret[0];
ret.h=ret[1];
return ret;
};
dojo.html.getScrollTop=function(){
return window.pageYOffset||document.documentElement.scrollTop||document.body.scrollTop||0;
};
dojo.html.getScrollLeft=function(){
return window.pageXOffset||document.documentElement.scrollLeft||document.body.scrollLeft||0;
};
dojo.html.getScrollOffset=function(){
var off=[dojo.html.getScrollLeft(),dojo.html.getScrollTop()];
off.x=off[0];
off.y=off[1];
return off;
};
dojo.html.getParentOfType=function(node,type){
dojo.deprecated("dojo.html.getParentOfType","replaced by dojo.html.getParentByType*","0.4");
return dojo.html.getParentByType(node,type);
};
dojo.html.getParentByType=function(node,type){
var _483=dojo.byId(node);
type=type.toLowerCase();
while((_483)&&(_483.nodeName.toLowerCase()!=type)){
if(_483==(document["body"]||document["documentElement"])){
return null;
}
_483=_483.parentNode;
}
return _483;
};
dojo.html.getAttribute=function(node,attr){
node=dojo.byId(node);
if((!node)||(!node.getAttribute)){
return null;
}
var ta=typeof attr=="string"?attr:new String(attr);
var v=node.getAttribute(ta.toUpperCase());
if((v)&&(typeof v=="string")&&(v!="")){
return v;
}
if(v&&v.value){
return v.value;
}
if((node.getAttributeNode)&&(node.getAttributeNode(ta))){
return (node.getAttributeNode(ta)).value;
}else{
if(node.getAttribute(ta)){
return node.getAttribute(ta);
}else{
if(node.getAttribute(ta.toLowerCase())){
return node.getAttribute(ta.toLowerCase());
}
}
}
return null;
};
dojo.html.hasAttribute=function(node,attr){
node=dojo.byId(node);
return dojo.html.getAttribute(node,attr)?true:false;
};
dojo.html.getClass=function(node){
node=dojo.byId(node);
if(!node){
return "";
}
var cs="";
if(node.className){
cs=node.className;
}else{
if(dojo.html.hasAttribute(node,"class")){
cs=dojo.html.getAttribute(node,"class");
}
}
return dojo.string.trim(cs);
};
dojo.html.getClasses=function(node){
var c=dojo.html.getClass(node);
return (c=="")?[]:c.split(/\s+/g);
};
dojo.html.hasClass=function(node,_48f){
return dojo.lang.inArray(dojo.html.getClasses(node),_48f);
};
dojo.html.prependClass=function(node,_491){
_491+=" "+dojo.html.getClass(node);
return dojo.html.setClass(node,_491);
};
dojo.html.addClass=function(node,_493){
if(dojo.html.hasClass(node,_493)){
return false;
}
_493=dojo.string.trim(dojo.html.getClass(node)+" "+_493);
return dojo.html.setClass(node,_493);
};
dojo.html.setClass=function(node,_495){
node=dojo.byId(node);
var cs=new String(_495);
try{
if(typeof node.className=="string"){
node.className=cs;
}else{
if(node.setAttribute){
node.setAttribute("class",_495);
node.className=cs;
}else{
return false;
}
}
}
catch(e){
dojo.debug("dojo.html.setClass() failed",e);
}
return true;
};
dojo.html.removeClass=function(node,_498,_499){
var _498=dojo.string.trim(new String(_498));
try{
var cs=dojo.html.getClasses(node);
var nca=[];
if(_499){
for(var i=0;i<cs.length;i++){
if(cs[i].indexOf(_498)==-1){
nca.push(cs[i]);
}
}
}else{
for(var i=0;i<cs.length;i++){
if(cs[i]!=_498){
nca.push(cs[i]);
}
}
}
dojo.html.setClass(node,nca.join(" "));
}
catch(e){
dojo.debug("dojo.html.removeClass() failed",e);
}
return true;
};
dojo.html.replaceClass=function(node,_49e,_49f){
dojo.html.removeClass(node,_49f);
dojo.html.addClass(node,_49e);
};
dojo.html.classMatchType={ContainsAll:0,ContainsAny:1,IsOnly:2};
dojo.html.getElementsByClass=function(_4a0,_4a1,_4a2,_4a3,_4a4){
_4a1=dojo.byId(_4a1)||document;
var _4a5=_4a0.split(/\s+/g);
var _4a6=[];
if(_4a3!=1&&_4a3!=2){
_4a3=0;
}
var _4a7=new RegExp("(\\s|^)(("+_4a5.join(")|(")+"))(\\s|$)");
var _4a8=[];
if(!_4a4&&document.evaluate){
var _4a9="//"+(_4a2||"*")+"[contains(";
if(_4a3!=dojo.html.classMatchType.ContainsAny){
_4a9+="concat(' ',@class,' '), ' "+_4a5.join(" ') and contains(concat(' ',@class,' '), ' ")+" ')]";
}else{
_4a9+="concat(' ',@class,' '), ' "+_4a5.join(" ')) or contains(concat(' ',@class,' '), ' ")+" ')]";
}
var _4aa=document.evaluate(_4a9,_4a1,null,XPathResult.ANY_TYPE,null);
var _4ab=_4aa.iterateNext();
while(_4ab){
try{
_4a8.push(_4ab);
_4ab=_4aa.iterateNext();
}
catch(e){
break;
}
}
return _4a8;
}else{
if(!_4a2){
_4a2="*";
}
_4a8=_4a1.getElementsByTagName(_4a2);
var node,i=0;
outer:
while(node=_4a8[i++]){
var _4ae=dojo.html.getClasses(node);
if(_4ae.length==0){
continue outer;
}
var _4af=0;
for(var j=0;j<_4ae.length;j++){
if(_4a7.test(_4ae[j])){
if(_4a3==dojo.html.classMatchType.ContainsAny){
_4a6.push(node);
continue outer;
}else{
_4af++;
}
}else{
if(_4a3==dojo.html.classMatchType.IsOnly){
continue outer;
}
}
}
if(_4af==_4a5.length){
if((_4a3==dojo.html.classMatchType.IsOnly)&&(_4af==_4ae.length)){
_4a6.push(node);
}else{
if(_4a3==dojo.html.classMatchType.ContainsAll){
_4a6.push(node);
}
}
}
}
return _4a6;
}
};
dojo.html.getElementsByClassName=dojo.html.getElementsByClass;
dojo.html.getCursorPosition=function(e){
e=e||window.event;
var _4b2={x:0,y:0};
if(e.pageX||e.pageY){
_4b2.x=e.pageX;
_4b2.y=e.pageY;
}else{
var de=document.documentElement;
var db=document.body;
_4b2.x=e.clientX+((de||db)["scrollLeft"])-((de||db)["clientLeft"]);
_4b2.y=e.clientY+((de||db)["scrollTop"])-((de||db)["clientTop"]);
}
return _4b2;
};
dojo.html.overElement=function(_4b5,e){
_4b5=dojo.byId(_4b5);
var _4b7=dojo.html.getCursorPosition(e);
with(dojo.html){
var top=getAbsoluteY(_4b5,true);
var _4b9=top+getInnerHeight(_4b5);
var left=getAbsoluteX(_4b5,true);
var _4bb=left+getInnerWidth(_4b5);
}
return (_4b7.x>=left&&_4b7.x<=_4bb&&_4b7.y>=top&&_4b7.y<=_4b9);
};
dojo.html.setActiveStyleSheet=function(_4bc){
var i=0,a,els=document.getElementsByTagName("link");
while(a=els[i++]){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")){
a.disabled=true;
if(a.getAttribute("title")==_4bc){
a.disabled=false;
}
}
}
};
dojo.html.getActiveStyleSheet=function(){
var i=0,a,els=document.getElementsByTagName("link");
while(a=els[i++]){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")&&!a.disabled){
return a.getAttribute("title");
}
}
return null;
};
dojo.html.getPreferredStyleSheet=function(){
var i=0,a,els=document.getElementsByTagName("link");
while(a=els[i++]){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("rel").indexOf("alt")==-1&&a.getAttribute("title")){
return a.getAttribute("title");
}
}
return null;
};
dojo.html.body=function(){
return document.body||document.getElementsByTagName("body")[0];
};
dojo.html.isTag=function(node){
node=dojo.byId(node);
if(node&&node.tagName){
var arr=dojo.lang.map(dojo.lang.toArray(arguments,1),function(a){
return String(a).toLowerCase();
});
return arr[dojo.lang.find(node.tagName.toLowerCase(),arr)]||"";
}
return "";
};
dojo.html.copyStyle=function(_4c9,_4ca){
if(dojo.lang.isUndefined(_4ca.style.cssText)){
_4c9.setAttribute("style",_4ca.getAttribute("style"));
}else{
_4c9.style.cssText=_4ca.style.cssText;
}
dojo.html.addClass(_4c9,dojo.html.getClass(_4ca));
};
dojo.html._callExtrasDeprecated=function(_4cb,args){
var _4cd="dojo.html.extras";
dojo.deprecated("dojo.html."+_4cb,"moved to "+_4cd,"0.4");
dojo["require"](_4cd);
return dojo.html[_4cb].apply(dojo.html,args);
};
dojo.html.createNodesFromText=function(){
return dojo.html._callExtrasDeprecated("createNodesFromText",arguments);
};
dojo.html.gravity=function(){
return dojo.html._callExtrasDeprecated("gravity",arguments);
};
dojo.html.placeOnScreen=function(){
return dojo.html._callExtrasDeprecated("placeOnScreen",arguments);
};
dojo.html.placeOnScreenPoint=function(){
return dojo.html._callExtrasDeprecated("placeOnScreenPoint",arguments);
};
dojo.html.renderedTextContent=function(){
return dojo.html._callExtrasDeprecated("renderedTextContent",arguments);
};
dojo.html.BackgroundIframe=function(){
return dojo.html._callExtrasDeprecated("BackgroundIframe",arguments);
};
dojo.provide("dojo.string.extras");
dojo.string.substituteParams=function(_4ce,hash){
var map=(typeof hash=="object")?hash:dojo.lang.toArray(arguments,1);
return _4ce.replace(/\%\{(\w+)\}/g,function(_4d1,key){
return map[key]||dojo.raise("Substitution not found: "+key);
});
};
dojo.string.paramString=function(str,_4d4,_4d5){
dojo.deprecated("dojo.string.paramString","use dojo.string.substituteParams instead","0.4");
for(var name in _4d4){
var re=new RegExp("\\%\\{"+name+"\\}","g");
str=str.replace(re,_4d4[name]);
}
if(_4d5){
str=str.replace(/%\{([^\}\s]+)\}/g,"");
}
return str;
};
dojo.string.capitalize=function(str){
if(!dojo.lang.isString(str)){
return "";
}
if(arguments.length==0){
str=this;
}
var _4d9=str.split(" ");
for(var i=0;i<_4d9.length;i++){
_4d9[i]=_4d9[i].charAt(0).toUpperCase()+_4d9[i].substring(1);
}
return _4d9.join(" ");
};
dojo.string.isBlank=function(str){
if(!dojo.lang.isString(str)){
return true;
}
return (dojo.string.trim(str).length==0);
};
dojo.string.encodeAscii=function(str){
if(!dojo.lang.isString(str)){
return str;
}
var ret="";
var _4de=escape(str);
var _4df,re=/%u([0-9A-F]{4})/i;
while((_4df=_4de.match(re))){
var num=Number("0x"+_4df[1]);
var _4e2=escape("&#"+num+";");
ret+=_4de.substring(0,_4df.index)+_4e2;
_4de=_4de.substring(_4df.index+_4df[0].length);
}
ret+=_4de.replace(/\+/g,"%2B");
return ret;
};
dojo.string.escape=function(type,str){
var args=dojo.lang.toArray(arguments,1);
switch(type.toLowerCase()){
case "xml":
case "html":
case "xhtml":
return dojo.string.escapeXml.apply(this,args);
case "sql":
return dojo.string.escapeSql.apply(this,args);
case "regexp":
case "regex":
return dojo.string.escapeRegExp.apply(this,args);
case "javascript":
case "jscript":
case "js":
return dojo.string.escapeJavaScript.apply(this,args);
case "ascii":
return dojo.string.encodeAscii.apply(this,args);
default:
return str;
}
};
dojo.string.escapeXml=function(str,_4e7){
str=str.replace(/&/gm,"&amp;").replace(/</gm,"&lt;").replace(/>/gm,"&gt;").replace(/"/gm,"&quot;");
if(!_4e7){
str=str.replace(/'/gm,"&#39;");
}
return str;
};
dojo.string.escapeSql=function(str){
return str.replace(/'/gm,"''");
};
dojo.string.escapeRegExp=function(str){
return str.replace(/\\/gm,"\\\\").replace(/([\f\b\n\t\r[\^$|?*+(){}])/gm,"\\$1");
};
dojo.string.escapeJavaScript=function(str){
return str.replace(/(["'\f\b\n\t\r])/gm,"\\$1");
};
dojo.string.escapeString=function(str){
return ("\""+str.replace(/(["\\])/g,"\\$1")+"\"").replace(/[\f]/g,"\\f").replace(/[\b]/g,"\\b").replace(/[\n]/g,"\\n").replace(/[\t]/g,"\\t").replace(/[\r]/g,"\\r");
};
dojo.string.summary=function(str,len){
if(!len||str.length<=len){
return str;
}else{
return str.substring(0,len).replace(/\.+$/,"")+"...";
}
};
dojo.string.endsWith=function(str,end,_4f0){
if(_4f0){
str=str.toLowerCase();
end=end.toLowerCase();
}
if((str.length-end.length)<0){
return false;
}
return str.lastIndexOf(end)==str.length-end.length;
};
dojo.string.endsWithAny=function(str){
for(var i=1;i<arguments.length;i++){
if(dojo.string.endsWith(str,arguments[i])){
return true;
}
}
return false;
};
dojo.string.startsWith=function(str,_4f4,_4f5){
if(_4f5){
str=str.toLowerCase();
_4f4=_4f4.toLowerCase();
}
return str.indexOf(_4f4)==0;
};
dojo.string.startsWithAny=function(str){
for(var i=1;i<arguments.length;i++){
if(dojo.string.startsWith(str,arguments[i])){
return true;
}
}
return false;
};
dojo.string.has=function(str){
for(var i=1;i<arguments.length;i++){
if(str.indexOf(arguments[i])>-1){
return true;
}
}
return false;
};
dojo.string.normalizeNewlines=function(text,_4fb){
if(_4fb=="\n"){
text=text.replace(/\r\n/g,"\n");
text=text.replace(/\r/g,"\n");
}else{
if(_4fb=="\r"){
text=text.replace(/\r\n/g,"\r");
text=text.replace(/\n/g,"\r");
}else{
text=text.replace(/([^\r])\n/g,"$1\r\n");
text=text.replace(/\r([^\n])/g,"\r\n$1");
}
}
return text;
};
dojo.string.splitEscaped=function(str,_4fd){
var _4fe=[];
for(var i=0,_500=0;i<str.length;i++){
if(str.charAt(i)=="\\"){
i++;
continue;
}
if(str.charAt(i)==_4fd){
_4fe.push(str.substring(_500,i));
_500=i+1;
}
}
_4fe.push(str.substr(_500));
return _4fe;
};
dojo.provide("dojo.html.extras");
dojo.html.gravity=function(node,e){
node=dojo.byId(node);
var _503=dojo.html.getCursorPosition(e);
with(dojo.html){
var _504=getAbsoluteX(node,true)+(getInnerWidth(node)/2);
var _505=getAbsoluteY(node,true)+(getInnerHeight(node)/2);
}
with(dojo.html.gravity){
return ((_503.x<_504?WEST:EAST)|(_503.y<_505?NORTH:SOUTH));
}
};
dojo.html.gravity.NORTH=1;
dojo.html.gravity.SOUTH=1<<1;
dojo.html.gravity.EAST=1<<2;
dojo.html.gravity.WEST=1<<3;
dojo.html.renderedTextContent=function(node){
node=dojo.byId(node);
var _507="";
if(node==null){
return _507;
}
for(var i=0;i<node.childNodes.length;i++){
switch(node.childNodes[i].nodeType){
case 1:
case 5:
var _509="unknown";
try{
_509=dojo.style.getStyle(node.childNodes[i],"display");
}
catch(E){
}
switch(_509){
case "block":
case "list-item":
case "run-in":
case "table":
case "table-row-group":
case "table-header-group":
case "table-footer-group":
case "table-row":
case "table-column-group":
case "table-column":
case "table-cell":
case "table-caption":
_507+="\n";
_507+=dojo.html.renderedTextContent(node.childNodes[i]);
_507+="\n";
break;
case "none":
break;
default:
if(node.childNodes[i].tagName&&node.childNodes[i].tagName.toLowerCase()=="br"){
_507+="\n";
}else{
_507+=dojo.html.renderedTextContent(node.childNodes[i]);
}
break;
}
break;
case 3:
case 2:
case 4:
var text=node.childNodes[i].nodeValue;
var _50b="unknown";
try{
_50b=dojo.style.getStyle(node,"text-transform");
}
catch(E){
}
switch(_50b){
case "capitalize":
text=dojo.string.capitalize(text);
break;
case "uppercase":
text=text.toUpperCase();
break;
case "lowercase":
text=text.toLowerCase();
break;
default:
break;
}
switch(_50b){
case "nowrap":
break;
case "pre-wrap":
break;
case "pre-line":
break;
case "pre":
break;
default:
text=text.replace(/\s+/," ");
if(/\s$/.test(_507)){
text.replace(/^\s/,"");
}
break;
}
_507+=text;
break;
default:
break;
}
}
return _507;
};
dojo.html.createNodesFromText=function(txt,trim){
if(trim){
txt=dojo.string.trim(txt);
}
var tn=document.createElement("div");
tn.style.visibility="hidden";
document.body.appendChild(tn);
var _50f="none";
if((/^<t[dh][\s\r\n>]/i).test(dojo.string.trimStart(txt))){
txt="<table><tbody><tr>"+txt+"</tr></tbody></table>";
_50f="cell";
}else{
if((/^<tr[\s\r\n>]/i).test(dojo.string.trimStart(txt))){
txt="<table><tbody>"+txt+"</tbody></table>";
_50f="row";
}else{
if((/^<(thead|tbody|tfoot)[\s\r\n>]/i).test(dojo.string.trimStart(txt))){
txt="<table>"+txt+"</table>";
_50f="section";
}
}
}
tn.innerHTML=txt;
if(tn["normalize"]){
tn.normalize();
}
var _510=null;
switch(_50f){
case "cell":
_510=tn.getElementsByTagName("tr")[0];
break;
case "row":
_510=tn.getElementsByTagName("tbody")[0];
break;
case "section":
_510=tn.getElementsByTagName("table")[0];
break;
default:
_510=tn;
break;
}
var _511=[];
for(var x=0;x<_510.childNodes.length;x++){
_511.push(_510.childNodes[x].cloneNode(true));
}
tn.style.display="none";
document.body.removeChild(tn);
return _511;
};
dojo.html.placeOnScreen=function(node,_514,_515,_516,_517){
if(dojo.lang.isArray(_514)){
_517=_516;
_516=_515;
_515=_514[1];
_514=_514[0];
}
if(!isNaN(_516)){
_516=[Number(_516),Number(_516)];
}else{
if(!dojo.lang.isArray(_516)){
_516=[0,0];
}
}
var _518=dojo.html.getScrollOffset();
var view=dojo.html.getViewportSize();
node=dojo.byId(node);
var w=node.offsetWidth+_516[0];
var h=node.offsetHeight+_516[1];
if(_517){
_514-=_518.x;
_515-=_518.y;
}
var x=_514+w;
if(x>view.w){
x=view.w-w;
}else{
x=_514;
}
x=Math.max(_516[0],x)+_518.x;
var y=_515+h;
if(y>view.h){
y=view.h-h;
}else{
y=_515;
}
y=Math.max(_516[1],y)+_518.y;
node.style.left=x+"px";
node.style.top=y+"px";
var ret=[x,y];
ret.x=x;
ret.y=y;
return ret;
};
dojo.html.placeOnScreenPoint=function(node,_520,_521,_522,_523){
if(dojo.lang.isArray(_520)){
_523=_522;
_522=_521;
_521=_520[1];
_520=_520[0];
}
if(!isNaN(_522)){
_522=[Number(_522),Number(_522)];
}else{
if(!dojo.lang.isArray(_522)){
_522=[0,0];
}
}
var _524=dojo.html.getScrollOffset();
var view=dojo.html.getViewportSize();
node=dojo.byId(node);
var _526=node.style.display;
node.style.display="";
var w=dojo.style.getInnerWidth(node);
var h=dojo.style.getInnerHeight(node);
node.style.display=_526;
if(_523){
_520-=_524.x;
_521-=_524.y;
}
var x=-1,y=-1;
if((_520+_522[0])+w<=view.w&&(_521+_522[1])+h<=view.h){
x=(_520+_522[0]);
y=(_521+_522[1]);
}
if((x<0||y<0)&&(_520-_522[0])<=view.w&&(_521+_522[1])+h<=view.h){
x=(_520-_522[0])-w;
y=(_521+_522[1]);
}
if((x<0||y<0)&&(_520+_522[0])+w<=view.w&&(_521-_522[1])<=view.h){
x=(_520+_522[0]);
y=(_521-_522[1])-h;
}
if((x<0||y<0)&&(_520-_522[0])<=view.w&&(_521-_522[1])<=view.h){
x=(_520-_522[0])-w;
y=(_521-_522[1])-h;
}
if(x<0||y<0||(x+w>view.w)||(y+h>view.h)){
return dojo.html.placeOnScreen(node,_520,_521,_522,_523);
}
x+=_524.x;
y+=_524.y;
node.style.left=x+"px";
node.style.top=y+"px";
var ret=[x,y];
ret.x=x;
ret.y=y;
return ret;
};
dojo.html.BackgroundIframe=function(node){
if(dojo.render.html.ie55||dojo.render.html.ie60){
var html="<iframe "+"style='position: absolute; left: 0px; top: 0px; width: 100%; height: 100%;"+"z-index: -1; filter:Alpha(Opacity=\"0\");' "+">";
this.iframe=document.createElement(html);
if(node){
node.appendChild(this.iframe);
this.domNode=node;
}else{
document.body.appendChild(this.iframe);
this.iframe.style.display="none";
}
}
};
dojo.lang.extend(dojo.html.BackgroundIframe,{iframe:null,onResized:function(){
if(this.iframe&&this.domNode&&this.domNode.parentElement){
var w=dojo.style.getOuterWidth(this.domNode);
var h=dojo.style.getOuterHeight(this.domNode);
if(w==0||h==0){
dojo.lang.setTimeout(this,this.onResized,50);
return;
}
var s=this.iframe.style;
s.width=w+"px";
s.height=h+"px";
}
},size:function(node){
if(!this.iframe){
return;
}
var _532=dojo.style.toCoordinateArray(node,true);
var s=this.iframe.style;
s.width=_532.w+"px";
s.height=_532.h+"px";
s.left=_532.x+"px";
s.top=_532.y+"px";
},setZIndex:function(node){
if(!this.iframe){
return;
}
if(dojo.dom.isNode(node)){
this.iframe.style.zIndex=dojo.html.getStyle(node,"z-index")-1;
}else{
if(!isNaN(node)){
this.iframe.style.zIndex=node;
}
}
},show:function(){
if(!this.iframe){
return;
}
this.iframe.style.display="block";
},hide:function(){
if(!this.ie){
return;
}
var s=this.iframe.style;
s.display="none";
},remove:function(){
dojo.dom.removeNode(this.iframe);
}});
dojo.provide("dojo.lfx.Animation");
dojo.provide("dojo.lfx.Line");
dojo.lfx.Line=function(_536,end){
this.start=_536;
this.end=end;
if(dojo.lang.isArray(_536)){
var diff=[];
dojo.lang.forEach(this.start,function(s,i){
diff[i]=this.end[i]-s;
},this);
this.getValue=function(n){
var res=[];
dojo.lang.forEach(this.start,function(s,i){
res[i]=(diff[i]*n)+s;
},this);
return res;
};
}else{
var diff=end-_536;
this.getValue=function(n){
return (diff*n)+this.start;
};
}
};
dojo.lfx.easeIn=function(n){
return Math.pow(n,3);
};
dojo.lfx.easeOut=function(n){
return (1-Math.pow(1-n,3));
};
dojo.lfx.easeInOut=function(n){
return ((3*Math.pow(n,2))-(2*Math.pow(n,3)));
};
dojo.lfx.IAnimation=function(){
};
dojo.lang.extend(dojo.lfx.IAnimation,{curve:null,duration:1000,easing:null,repeatCount:0,rate:25,handler:null,beforeBegin:null,onBegin:null,onAnimate:null,onEnd:null,onPlay:null,onPause:null,onStop:null,play:null,pause:null,stop:null,fire:function(evt,args){
if(this[evt]){
this[evt].apply(this,(args||[]));
}
},_active:false,_paused:false});
dojo.lfx.Animation=function(_545,_546,_547,_548,_549,rate){
dojo.lfx.IAnimation.call(this);
if(dojo.lang.isNumber(_545)||(!_545&&_546.getValue)){
rate=_549;
_549=_548;
_548=_547;
_547=_546;
_546=_545;
_545=null;
}else{
if(_545.getValue||dojo.lang.isArray(_545)){
rate=_548;
_549=_547;
_548=_546;
_547=_545;
_546=null;
_545=null;
}
}
if(dojo.lang.isArray(_547)){
this.curve=new dojo.lfx.Line(_547[0],_547[1]);
}else{
this.curve=_547;
}
if(_546!=null&&_546>0){
this.duration=_546;
}
if(_549){
this.repeatCount=_549;
}
if(rate){
this.rate=rate;
}
if(_545){
this.handler=_545.handler;
this.beforeBegin=_545.beforeBegin;
this.onBegin=_545.onBegin;
this.onEnd=_545.onEnd;
this.onPlay=_545.onPlay;
this.onPause=_545.onPause;
this.onStop=_545.onStop;
this.onAnimate=_545.onAnimate;
}
if(_548&&dojo.lang.isFunction(_548)){
this.easing=_548;
}
};
dojo.inherits(dojo.lfx.Animation,dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Animation,{_startTime:null,_endTime:null,_timer:null,_percent:0,_startRepeatCount:0,play:function(_54b,_54c){
if(_54c){
clearTimeout(this._timer);
this._active=false;
this._paused=false;
this._percent=0;
}else{
if(this._active&&!this._paused){
return this;
}
}
this.fire("handler",["beforeBegin"]);
this.fire("beforeBegin");
if(_54b>0){
setTimeout(dojo.lang.hitch(this,function(){
this.play(null,_54c);
}),_54b);
return this;
}
this._startTime=new Date().valueOf();
if(this._paused){
this._startTime-=(this.duration*this._percent/100);
}
this._endTime=this._startTime+this.duration;
this._active=true;
this._paused=false;
var step=this._percent/100;
var _54e=this.curve.getValue(step);
if(this._percent==0){
if(!this._startRepeatCount){
this._startRepeatCount=this.repeatCount;
}
this.fire("handler",["begin",_54e]);
this.fire("onBegin",[_54e]);
}
this.fire("handler",["play",_54e]);
this.fire("onPlay",[_54e]);
this._cycle();
return this;
},pause:function(){
clearTimeout(this._timer);
if(!this._active){
return this;
}
this._paused=true;
var _54f=this.curve.getValue(this._percent/100);
this.fire("handler",["pause",_54f]);
this.fire("onPause",[_54f]);
return this;
},gotoPercent:function(pct,_551){
clearTimeout(this._timer);
this._active=true;
this._paused=true;
this._percent=pct;
if(_551){
this.play();
}
},stop:function(_552){
clearTimeout(this._timer);
var step=this._percent/100;
if(_552){
step=1;
}
var _554=this.curve.getValue(step);
this.fire("handler",["stop",_554]);
this.fire("onStop",[_554]);
this._active=false;
this._paused=false;
return this;
},status:function(){
if(this._active){
return this._paused?"paused":"playing";
}else{
return "stopped";
}
},_cycle:function(){
clearTimeout(this._timer);
if(this._active){
var curr=new Date().valueOf();
var step=(curr-this._startTime)/(this._endTime-this._startTime);
if(step>=1){
step=1;
this._percent=100;
}else{
this._percent=step*100;
}
if((this.easing)&&(dojo.lang.isFunction(this.easing))){
step=this.easing(step);
}
var _557=this.curve.getValue(step);
this.fire("handler",["animate",_557]);
this.fire("onAnimate",[_557]);
if(step<1){
this._timer=setTimeout(dojo.lang.hitch(this,"_cycle"),this.rate);
}else{
this._active=false;
this.fire("handler",["end"]);
this.fire("onEnd");
if(this.repeatCount>0){
this.repeatCount--;
this.play(null,true);
}else{
if(this.repeatCount==-1){
this.play(null,true);
}else{
if(this._startRepeatCount){
this.repeatCount=this._startRepeatCount;
this._startRepeatCount=0;
}
}
}
}
}
return this;
}});
dojo.lfx.Combine=function(){
dojo.lfx.IAnimation.call(this);
this._anims=[];
this._animsEnded=0;
var _558=arguments;
if(_558.length==1&&(dojo.lang.isArray(_558[0])||dojo.lang.isArrayLike(_558[0]))){
_558=_558[0];
}
var _559=this;
dojo.lang.forEach(_558,function(anim){
_559._anims.push(anim);
var _55b=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
anim.onEnd=function(){
_55b();
_559._onAnimsEnded();
};
});
};
dojo.inherits(dojo.lfx.Combine,dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Combine,{_animsEnded:0,play:function(_55c,_55d){
if(!this._anims.length){
return this;
}
this.fire("beforeBegin");
if(_55c>0){
setTimeout(dojo.lang.hitch(this,function(){
this.play(null,_55d);
}),_55c);
return this;
}
if(_55d||this._anims[0].percent==0){
this.fire("onBegin");
}
this.fire("onPlay");
this._animsCall("play",null,_55d);
return this;
},pause:function(){
this.fire("onPause");
this._animsCall("pause");
return this;
},stop:function(_55e){
this.fire("onStop");
this._animsCall("stop",_55e);
return this;
},_onAnimsEnded:function(){
this._animsEnded++;
if(this._animsEnded>=this._anims.length){
this.fire("onEnd");
}
return this;
},_animsCall:function(_55f){
var args=[];
if(arguments.length>1){
for(var i=1;i<arguments.length;i++){
args.push(arguments[i]);
}
}
var _562=this;
dojo.lang.forEach(this._anims,function(anim){
anim[_55f](args);
},_562);
return this;
}});
dojo.lfx.Chain=function(){
dojo.lfx.IAnimation.call(this);
this._anims=[];
this._currAnim=-1;
var _564=arguments;
if(_564.length==1&&(dojo.lang.isArray(_564[0])||dojo.lang.isArrayLike(_564[0]))){
_564=_564[0];
}
var _565=this;
dojo.lang.forEach(_564,function(anim,i,_568){
_565._anims.push(anim);
var _569=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
if(i<_568.length-1){
anim.onEnd=function(){
_569();
_565._playNext();
};
}else{
anim.onEnd=function(){
_569();
_565.fire("onEnd");
};
}
},_565);
};
dojo.inherits(dojo.lfx.Chain,dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Chain,{_currAnim:-1,play:function(_56a,_56b){
if(!this._anims.length){
return this;
}
if(_56b||!this._anims[this._currAnim]){
this._currAnim=0;
}
var _56c=this._anims[this._currAnim];
this.fire("beforeBegin");
if(_56a>0){
setTimeout(dojo.lang.hitch(this,function(){
this.play(null,_56b);
}),_56a);
return this;
}
if(_56c){
if(this._currAnim==0){
this.fire("handler",["begin",this._currAnim]);
this.fire("onBegin",[this._currAnim]);
}
this.fire("onPlay",[this._currAnim]);
_56c.play(null,_56b);
}
return this;
},pause:function(){
if(this._anims[this._currAnim]){
this._anims[this._currAnim].pause();
this.fire("onPause",[this._currAnim]);
}
return this;
},playPause:function(){
if(this._anims.length==0){
return this;
}
if(this._currAnim==-1){
this._currAnim=0;
}
var _56d=this._anims[this._currAnim];
if(_56d){
if(!_56d._active||_56d._paused){
this.play();
}else{
this.pause();
}
}
return this;
},stop:function(){
var _56e=this._anims[this._currAnim];
if(_56e){
_56e.stop();
this.fire("onStop",[this._currAnim]);
}
return _56e;
},_playNext:function(){
if(this._currAnim==-1||this._anims.length==0){
return this;
}
this._currAnim++;
if(this._anims[this._currAnim]){
this._anims[this._currAnim].play(null,true);
}
return this;
}});
dojo.lfx.combine=function(){
var _56f=arguments;
if(dojo.lang.isArray(arguments[0])){
_56f=arguments[0];
}
return new dojo.lfx.Combine(_56f);
};
dojo.lfx.chain=function(){
var _570=arguments;
if(dojo.lang.isArray(arguments[0])){
_570=arguments[0];
}
return new dojo.lfx.Chain(_570);
};
dojo.provide("dojo.lfx.html");
dojo.lfx.html._byId=function(_571){
if(!_571){
return [];
}
if(dojo.lang.isArray(_571)){
if(!_571.alreadyChecked){
var n=[];
dojo.lang.forEach(_571,function(node){
n.push(dojo.byId(node));
});
n.alreadyChecked=true;
return n;
}else{
return _571;
}
}else{
var n=[];
n.push(dojo.byId(_571));
n.alreadyChecked=true;
return n;
}
};
dojo.lfx.html.propertyAnimation=function(_574,_575,_576,_577){
_574=dojo.lfx.html._byId(_574);
if(_574.length==1){
dojo.lang.forEach(_575,function(prop){
if(typeof prop["start"]=="undefined"){
if(prop.property!="opacity"){
prop.start=parseInt(dojo.style.getComputedStyle(_574[0],prop.property));
}else{
prop.start=dojo.style.getOpacity(_574[0]);
}
}
});
}
var _579=function(_57a){
var _57b=new Array(_57a.length);
for(var i=0;i<_57a.length;i++){
_57b[i]=Math.round(_57a[i]);
}
return _57b;
};
var _57d=function(n,_57f){
n=dojo.byId(n);
if(!n||!n.style){
return;
}
for(var s in _57f){
if(s=="opacity"){
dojo.style.setOpacity(n,_57f[s]);
}else{
n.style[s]=_57f[s];
}
}
};
var _581=function(_582){
this._properties=_582;
this.diffs=new Array(_582.length);
dojo.lang.forEach(_582,function(prop,i){
if(dojo.lang.isArray(prop.start)){
this.diffs[i]=null;
}else{
if(prop.start instanceof dojo.graphics.color.Color){
prop.startRgb=prop.start.toRgb();
prop.endRgb=prop.end.toRgb();
}else{
this.diffs[i]=prop.end-prop.start;
}
}
},this);
this.getValue=function(n){
var ret={};
dojo.lang.forEach(this._properties,function(prop,i){
var _589=null;
if(dojo.lang.isArray(prop.start)){
}else{
if(prop.start instanceof dojo.graphics.color.Color){
_589=(prop.units||"rgb")+"(";
for(var j=0;j<prop.startRgb.length;j++){
_589+=Math.round(((prop.endRgb[j]-prop.startRgb[j])*n)+prop.startRgb[j])+(j<prop.startRgb.length-1?",":"");
}
_589+=")";
}else{
_589=((this.diffs[i])*n)+prop.start+(prop.property!="opacity"?prop.units||"px":"");
}
}
ret[dojo.style.toCamelCase(prop.property)]=_589;
},this);
return ret;
};
};
var anim=new dojo.lfx.Animation({onAnimate:function(_58c){
dojo.lang.forEach(_574,function(node){
_57d(node,_58c);
});
}},_576,new _581(_575),_577);
return anim;
};
dojo.lfx.html._makeFadeable=function(_58e){
var _58f=function(node){
if(dojo.render.html.ie){
if((node.style.zoom.length==0)&&(dojo.style.getStyle(node,"zoom")=="normal")){
node.style.zoom="1";
}
if((node.style.width.length==0)&&(dojo.style.getStyle(node,"width")=="auto")){
node.style.width="auto";
}
}
};
if(dojo.lang.isArrayLike(_58e)){
dojo.lang.forEach(_58e,_58f);
}else{
_58f(_58e);
}
};
dojo.lfx.html.fadeIn=function(_591,_592,_593,_594){
_591=dojo.lfx.html._byId(_591);
dojo.lfx.html._makeFadeable(_591);
var anim=dojo.lfx.propertyAnimation(_591,[{property:"opacity",start:dojo.style.getOpacity(_591[0]),end:1}],_592,_593);
if(_594){
var _596=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
anim.onEnd=function(){
_596();
_594(_591,anim);
};
}
return anim;
};
dojo.lfx.html.fadeOut=function(_597,_598,_599,_59a){
_597=dojo.lfx.html._byId(_597);
dojo.lfx.html._makeFadeable(_597);
var anim=dojo.lfx.propertyAnimation(_597,[{property:"opacity",start:dojo.style.getOpacity(_597[0]),end:0}],_598,_599);
if(_59a){
var _59c=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
anim.onEnd=function(){
_59c();
_59a(_597,anim);
};
}
return anim;
};
dojo.lfx.html.fadeShow=function(_59d,_59e,_59f,_5a0){
var anim=dojo.lfx.html.fadeIn(_59d,_59e,_59f,_5a0);
var _5a2=(anim["beforeBegin"])?dojo.lang.hitch(anim,"beforeBegin"):function(){
};
anim.beforeBegin=function(){
_5a2();
if(dojo.lang.isArrayLike(_59d)){
dojo.lang.forEach(_59d,dojo.style.show);
}else{
dojo.style.show(_59d);
}
};
return anim;
};
dojo.lfx.html.fadeHide=function(_5a3,_5a4,_5a5,_5a6){
var anim=dojo.lfx.html.fadeOut(_5a3,_5a4,_5a5,function(){
if(dojo.lang.isArrayLike(_5a3)){
dojo.lang.forEach(_5a3,dojo.style.hide);
}else{
dojo.style.hide(_5a3);
}
if(_5a6){
_5a6(_5a3,anim);
}
});
return anim;
};
dojo.lfx.html.wipeIn=function(_5a8,_5a9,_5aa,_5ab){
_5a8=dojo.lfx.html._byId(_5a8);
var _5ac=[];
dojo.lang.forEach(_5a8,function(node){
var _5ae=dojo.style.getStyle(node,"overflow");
if(_5ae=="visible"){
node.style.overflow="hidden";
}
node.style.height="0px";
dojo.style.show(node);
var anim=dojo.lfx.propertyAnimation(node,[{property:"height",start:0,end:node.scrollHeight}],_5a9,_5aa);
var _5b0=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
anim.onEnd=function(){
_5b0();
node.style.overflow=_5ae;
node.style.height="auto";
if(_5ab){
_5ab(node,anim);
}
};
_5ac.push(anim);
});
if(_5a8.length>1){
return dojo.lfx.combine(_5ac);
}else{
return _5ac[0];
}
};
dojo.lfx.html.wipeOut=function(_5b1,_5b2,_5b3,_5b4){
_5b1=dojo.lfx.html._byId(_5b1);
var _5b5=[];
dojo.lang.forEach(_5b1,function(node){
var _5b7=dojo.style.getStyle(node,"overflow");
if(_5b7=="visible"){
node.style.overflow="hidden";
}
dojo.style.show(node);
var anim=dojo.lfx.propertyAnimation(node,[{property:"height",start:dojo.style.getContentBoxHeight(node),end:0}],_5b2,_5b3);
var _5b9=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
anim.onEnd=function(){
_5b9();
dojo.style.hide(node);
node.style.overflow=_5b7;
if(_5b4){
_5b4(node,anim);
}
};
_5b5.push(anim);
});
if(_5b1.length>1){
return dojo.lfx.combine(_5b5);
}else{
return _5b5[0];
}
};
dojo.lfx.html.slideTo=function(_5ba,_5bb,_5bc,_5bd,_5be){
_5ba=dojo.lfx.html._byId(_5ba);
var _5bf=[];
dojo.lang.forEach(_5ba,function(node){
var top=null;
var left=null;
var init=(function(){
var _5c4=node;
return function(){
top=_5c4.offsetTop;
left=_5c4.offsetLeft;
if(!dojo.style.isPositionAbsolute(_5c4)){
var ret=dojo.style.abs(_5c4,true);
dojo.style.setStyleAttributes(_5c4,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");
top=ret.y;
left=ret.x;
}
};
})();
init();
var anim=dojo.lfx.propertyAnimation(node,[{property:"top",start:top,end:_5bb[0]},{property:"left",start:left,end:_5bb[1]}],_5bc,_5bd);
var _5c7=(anim["beforeBegin"])?dojo.lang.hitch(anim,"beforeBegin"):function(){
};
anim.beforeBegin=function(){
_5c7();
init();
};
if(_5be){
var _5c8=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
anim.onEnd=function(){
_5c8();
_5be(_5ba,anim);
};
}
_5bf.push(anim);
});
if(_5ba.length>1){
return dojo.lfx.combine(_5bf);
}else{
return _5bf[0];
}
};
dojo.lfx.html.slideBy=function(_5c9,_5ca,_5cb,_5cc,_5cd){
_5c9=dojo.lfx.html._byId(_5c9);
var _5ce=[];
dojo.lang.forEach(_5c9,function(node){
var top=null;
var left=null;
var init=(function(){
var _5d3=node;
return function(){
top=node.offsetTop;
left=node.offsetLeft;
if(!dojo.style.isPositionAbsolute(_5d3)){
var ret=dojo.style.abs(_5d3);
dojo.style.setStyleAttributes(_5d3,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");
top=ret.y;
left=ret.x;
}
};
})();
init();
var anim=dojo.lfx.propertyAnimation(node,[{property:"top",start:top,end:top+_5ca[0]},{property:"left",start:left,end:left+_5ca[1]}],_5cb,_5cc);
var _5d6=(anim["beforeBegin"])?dojo.lang.hitch(anim,"beforeBegin"):function(){
};
anim.beforeBegin=function(){
_5d6();
init();
};
if(_5cd){
var _5d7=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
anim.onEnd=function(){
_5d7();
_5cd(_5c9,anim);
};
}
_5ce.push(anim);
});
if(_5c9.length>1){
return dojo.lfx.combine(_5ce);
}else{
return _5ce[0];
}
};
dojo.lfx.html.explode=function(_5d8,_5d9,_5da,_5db,_5dc){
_5d8=dojo.byId(_5d8);
_5d9=dojo.byId(_5d9);
var _5dd=dojo.style.toCoordinateArray(_5d8,true);
var _5de=document.createElement("div");
dojo.html.copyStyle(_5de,_5d9);
with(_5de.style){
position="absolute";
display="none";
}
document.body.appendChild(_5de);
with(_5d9.style){
visibility="hidden";
display="block";
}
var _5df=dojo.style.toCoordinateArray(_5d9,true);
with(_5d9.style){
display="none";
visibility="visible";
}
var anim=new dojo.lfx.propertyAnimation(_5de,[{property:"height",start:_5dd[3],end:_5df[3]},{property:"width",start:_5dd[2],end:_5df[2]},{property:"top",start:_5dd[1],end:_5df[1]},{property:"left",start:_5dd[0],end:_5df[0]},{property:"opacity",start:0.3,end:1}],_5da,_5db);
anim.beforeBegin=function(){
dojo.style.setDisplay(_5de,"block");
};
anim.onEnd=function(){
dojo.style.setDisplay(_5d9,"block");
_5de.parentNode.removeChild(_5de);
};
if(_5dc){
var _5e1=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
anim.onEnd=function(){
_5e1();
_5dc(_5d9,anim);
};
}
return anim;
};
dojo.lfx.html.implode=function(_5e2,end,_5e4,_5e5,_5e6){
_5e2=dojo.byId(_5e2);
end=dojo.byId(end);
var _5e7=dojo.style.toCoordinateArray(_5e2,true);
var _5e8=dojo.style.toCoordinateArray(end,true);
var _5e9=document.createElement("div");
dojo.html.copyStyle(_5e9,_5e2);
dojo.style.setOpacity(_5e9,0.3);
with(_5e9.style){
position="absolute";
display="none";
}
document.body.appendChild(_5e9);
var anim=new dojo.lfx.propertyAnimation(_5e9,[{property:"height",start:_5e7[3],end:_5e8[3]},{property:"width",start:_5e7[2],end:_5e8[2]},{property:"top",start:_5e7[1],end:_5e8[1]},{property:"left",start:_5e7[0],end:_5e8[0]},{property:"opacity",start:1,end:0.3}],_5e4,_5e5);
anim.beforeBegin=function(){
dojo.style.hide(_5e2);
dojo.style.show(_5e9);
};
anim.onEnd=function(){
_5e9.parentNode.removeChild(_5e9);
};
if(_5e6){
var _5eb=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
anim.onEnd=function(){
_5eb();
_5e6(_5e2,anim);
};
}
return anim;
};
dojo.lfx.html.highlight=function(_5ec,_5ed,_5ee,_5ef,_5f0){
_5ec=dojo.lfx.html._byId(_5ec);
var _5f1=[];
dojo.lang.forEach(_5ec,function(node){
var _5f3=dojo.style.getBackgroundColor(node);
var bg=dojo.style.getStyle(node,"background-color").toLowerCase();
var _5f5=dojo.style.getStyle(node,"background-image");
var _5f6=(bg=="transparent"||bg=="rgba(0, 0, 0, 0)");
while(_5f3.length>3){
_5f3.pop();
}
var rgb=new dojo.graphics.color.Color(_5ed);
var _5f8=new dojo.graphics.color.Color(_5f3);
var anim=dojo.lfx.propertyAnimation(node,[{property:"background-color",start:rgb,end:_5f8}],_5ee,_5ef);
var _5fa=(anim["beforeBegin"])?dojo.lang.hitch(anim,"beforeBegin"):function(){
};
anim.beforeBegin=function(){
_5fa();
if(_5f5){
node.style.backgroundImage="none";
}
node.style.backgroundColor="rgb("+rgb.toRgb().join(",")+")";
};
var _5fb=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
anim.onEnd=function(){
_5fb();
if(_5f5){
node.style.backgroundImage=_5f5;
}
if(_5f6){
node.style.backgroundColor="transparent";
}
if(_5f0){
_5f0(node,anim);
}
};
_5f1.push(anim);
});
if(_5ec.length>1){
return dojo.lfx.combine(_5f1);
}else{
return _5f1[0];
}
};
dojo.lfx.html.unhighlight=function(_5fc,_5fd,_5fe,_5ff,_600){
_5fc=dojo.lfx.html._byId(_5fc);
var _601=[];
dojo.lang.forEach(_5fc,function(node){
var _603=new dojo.graphics.color.Color(dojo.style.getBackgroundColor(node));
var rgb=new dojo.graphics.color.Color(_5fd);
var _605=dojo.style.getStyle(node,"background-image");
var anim=dojo.lfx.propertyAnimation(node,[{property:"background-color",start:_603,end:rgb}],_5fe,_5ff);
var _607=(anim["beforeBegin"])?dojo.lang.hitch(anim,"beforeBegin"):function(){
};
anim.beforeBegin=function(){
_607();
if(_605){
node.style.backgroundImage="none";
}
node.style.backgroundColor="rgb("+_603.toRgb().join(",")+")";
};
var _608=(anim["onEnd"])?dojo.lang.hitch(anim,"onEnd"):function(){
};
anim.onEnd=function(){
_608();
if(_600){
_600(node,anim);
}
};
_601.push(anim);
});
if(_5fc.length>1){
return dojo.lfx.combine(_601);
}else{
return _601[0];
}
};
dojo.lang.mixin(dojo.lfx,dojo.lfx.html);
dojo.provide("dojo.lfx.*");
dojo.provide("dojo.lfx.toggle");
dojo.lfx.toggle.plain={show:function(node,_60a,_60b,_60c){
dojo.style.show(node);
if(dojo.lang.isFunction(_60c)){
_60c();
}
},hide:function(node,_60e,_60f,_610){
dojo.style.hide(node);
if(dojo.lang.isFunction(_610)){
_610();
}
}};
dojo.lfx.toggle.fade={show:function(node,_612,_613,_614){
dojo.lfx.fadeShow(node,_612,_613,_614).play();
},hide:function(node,_616,_617,_618){
dojo.lfx.fadeHide(node,_616,_617,_618).play();
}};
dojo.lfx.toggle.wipe={show:function(node,_61a,_61b,_61c){
dojo.lfx.wipeIn(node,_61a,_61b,_61c).play();
},hide:function(node,_61e,_61f,_620){
dojo.lfx.wipeOut(node,_61e,_61f,_620).play();
}};
dojo.lfx.toggle.explode={show:function(node,_622,_623,_624,_625){
dojo.lfx.explode(_625||[0,0,0,0],node,_622,_623,_624).play();
},hide:function(node,_627,_628,_629,_62a){
dojo.lfx.implode(node,_62a||[0,0,0,0],_627,_628,_629).play();
}};
dojo.provide("dojo.widget.HtmlWidget");
dojo.declare("dojo.widget.HtmlWidget",dojo.widget.DomWidget,{widgetType:"HtmlWidget",templateCssPath:null,templatePath:null,toggle:"plain",toggleDuration:150,animationInProgress:false,initialize:function(args,frag){
},postMixInProperties:function(args,frag){
this.toggleObj=dojo.lfx.toggle[this.toggle.toLowerCase()]||dojo.lfx.toggle.plain;
},getContainerHeight:function(){
dojo.unimplemented("dojo.widget.HtmlWidget.getContainerHeight");
},getContainerWidth:function(){
return this.parent.domNode.offsetWidth;
},setNativeHeight:function(_62f){
var ch=this.getContainerHeight();
},createNodesFromText:function(txt,wrap){
return dojo.html.createNodesFromText(txt,wrap);
},destroyRendering:function(_633){
try{
if(!_633){
dojo.event.browser.clean(this.domNode);
}
this.domNode.parentNode.removeChild(this.domNode);
delete this.domNode;
}
catch(e){
}
},isShowing:function(){
return dojo.style.isShowing(this.domNode);
},toggleShowing:function(){
if(this.isHidden){
this.show();
}else{
this.hide();
}
},show:function(){
this.animationInProgress=true;
this.isHidden=false;
this.toggleObj.show(this.domNode,this.toggleDuration,null,dojo.lang.hitch(this,this.onShow),this.explodeSrc);
},onShow:function(){
this.animationInProgress=false;
this.checkSize();
},hide:function(){
this.animationInProgress=true;
this.isHidden=true;
this.toggleObj.hide(this.domNode,this.toggleDuration,null,dojo.lang.hitch(this,this.onHide),this.explodeSrc);
},onHide:function(){
this.animationInProgress=false;
},_isResized:function(w,h){
if(!this.isShowing()){
return false;
}
w=w||dojo.style.getOuterWidth(this.domNode);
h=h||dojo.style.getOuterHeight(this.domNode);
if(this.width==w&&this.height==h){
return false;
}
this.width=w;
this.height=h;
return true;
},checkSize:function(){
if(!this._isResized()){
return;
}
this.onResized();
},resizeTo:function(w,h){
if(!this._isResized(w,h)){
return;
}
dojo.style.setOuterWidth(this.domNode,w);
dojo.style.setOuterHeight(this.domNode,h);
this.onResized();
},resizeSoon:function(){
if(this.isShowing()){
dojo.lang.setTimeout(this,this.onResized,0);
}
},onResized:function(){
dojo.lang.forEach(this.children,function(_638){
_638.checkSize();
});
}});
dojo.provide("dojo.widget.*");
dojo.provide("dojo.io.IO");
dojo.io.transports=[];
dojo.io.hdlrFuncNames=["load","error","timeout"];
dojo.io.Request=function(url,_63a,_63b,_63c){
if((arguments.length==1)&&(arguments[0].constructor==Object)){
this.fromKwArgs(arguments[0]);
}else{
this.url=url;
if(_63a){
this.mimetype=_63a;
}
if(_63b){
this.transport=_63b;
}
if(arguments.length>=4){
this.changeUrl=_63c;
}
}
};
dojo.lang.extend(dojo.io.Request,{url:"",mimetype:"text/plain",method:"GET",content:undefined,transport:undefined,changeUrl:undefined,formNode:undefined,sync:false,bindSuccess:false,useCache:false,preventCache:false,load:function(type,data,evt){
},error:function(type,_641){
},timeout:function(type){
},handle:function(){
},timeoutSeconds:0,abort:function(){
},fromKwArgs:function(_643){
if(_643["url"]){
_643.url=_643.url.toString();
}
if(_643["formNode"]){
_643.formNode=dojo.byId(_643.formNode);
}
if(!_643["method"]&&_643["formNode"]&&_643["formNode"].method){
_643.method=_643["formNode"].method;
}
if(!_643["handle"]&&_643["handler"]){
_643.handle=_643.handler;
}
if(!_643["load"]&&_643["loaded"]){
_643.load=_643.loaded;
}
if(!_643["changeUrl"]&&_643["changeURL"]){
_643.changeUrl=_643.changeURL;
}
_643.encoding=dojo.lang.firstValued(_643["encoding"],djConfig["bindEncoding"],"");
_643.sendTransport=dojo.lang.firstValued(_643["sendTransport"],djConfig["ioSendTransport"],false);
var _644=dojo.lang.isFunction;
for(var x=0;x<dojo.io.hdlrFuncNames.length;x++){
var fn=dojo.io.hdlrFuncNames[x];
if(_644(_643[fn])){
continue;
}
if(_644(_643["handle"])){
_643[fn]=_643.handle;
}
}
dojo.lang.mixin(this,_643);
}});
dojo.io.Error=function(msg,type,num){
this.message=msg;
this.type=type||"unknown";
this.number=num||0;
};
dojo.io.transports.addTransport=function(name){
this.push(name);
this[name]=dojo.io[name];
};
dojo.io.bind=function(_64b){
if(!(_64b instanceof dojo.io.Request)){
try{
_64b=new dojo.io.Request(_64b);
}
catch(e){
dojo.debug(e);
}
}
var _64c="";
if(_64b["transport"]){
_64c=_64b["transport"];
if(!this[_64c]){
return _64b;
}
}else{
for(var x=0;x<dojo.io.transports.length;x++){
var tmp=dojo.io.transports[x];
if((this[tmp])&&(this[tmp].canHandle(_64b))){
_64c=tmp;
}
}
if(_64c==""){
return _64b;
}
}
this[_64c].bind(_64b);
_64b.bindSuccess=true;
return _64b;
};
dojo.io.queueBind=function(_64f){
if(!(_64f instanceof dojo.io.Request)){
try{
_64f=new dojo.io.Request(_64f);
}
catch(e){
dojo.debug(e);
}
}
var _650=_64f.load;
_64f.load=function(){
dojo.io._queueBindInFlight=false;
var ret=_650.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
var _652=_64f.error;
_64f.error=function(){
dojo.io._queueBindInFlight=false;
var ret=_652.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
dojo.io._bindQueue.push(_64f);
dojo.io._dispatchNextQueueBind();
return _64f;
};
dojo.io._dispatchNextQueueBind=function(){
if(!dojo.io._queueBindInFlight){
dojo.io._queueBindInFlight=true;
if(dojo.io._bindQueue.length>0){
dojo.io.bind(dojo.io._bindQueue.shift());
}else{
dojo.io._queueBindInFlight=false;
}
}
};
dojo.io._bindQueue=[];
dojo.io._queueBindInFlight=false;
dojo.io.argsFromMap=function(map,_655,last){
var enc=/utf/i.test(_655||"")?encodeURIComponent:dojo.string.encodeAscii;
var _658=[];
var _659=new Object();
for(var name in map){
var _65b=function(elt){
var val=enc(name)+"="+enc(elt);
_658[(last==name)?"push":"unshift"](val);
};
if(!_659[name]){
var _65e=map[name];
if(dojo.lang.isArray(_65e)){
dojo.lang.forEach(_65e,_65b);
}else{
_65b(_65e);
}
}
}
return _658.join("&");
};
dojo.io.setIFrameSrc=function(_65f,src,_661){
try{
var r=dojo.render.html;
if(!_661){
if(r.safari){
_65f.location=src;
}else{
frames[_65f.name].location=src;
}
}else{
var idoc;
if(r.ie){
idoc=_65f.contentWindow.document;
}else{
if(r.safari){
idoc=_65f.document;
}else{
idoc=_65f.contentWindow;
}
}
if(!idoc){
_65f.location=src;
return;
}else{
idoc.location.replace(src);
}
}
}
catch(e){
dojo.debug(e);
dojo.debug("setIFrameSrc: "+e);
}
};
dojo.provide("dojo.undo.browser");
try{
if((!djConfig["preventBackButtonFix"])&&(!dojo.hostenv.post_load_)){
document.write("<iframe style='border: 0px; width: 1px; height: 1px; position: absolute; bottom: 0px; right: 0px; visibility: visible;' name='djhistory' id='djhistory' src='"+(dojo.hostenv.getBaseScriptUri()+"iframe_history.html")+"'></iframe>");
}
}
catch(e){
}
if(dojo.render.html.opera){
dojo.debug("Opera is not supported with dojo.undo.browser, so back/forward detection will not work.");
}
dojo.undo.browser={initialHref:window.location.href,initialHash:window.location.hash,moveForward:false,historyStack:[],forwardStack:[],historyIframe:null,bookmarkAnchor:null,locationTimer:null,setInitialState:function(args){
this.initialState={"url":this.initialHref,"kwArgs":args,"urlHash":this.initialHash};
},addToHistory:function(args){
var hash=null;
if(!this.historyIframe){
this.historyIframe=window.frames["djhistory"];
}
if(!this.bookmarkAnchor){
this.bookmarkAnchor=document.createElement("a");
(document.body||document.getElementsByTagName("body")[0]).appendChild(this.bookmarkAnchor);
this.bookmarkAnchor.style.display="none";
}
if((!args["changeUrl"])||(dojo.render.html.ie)){
var url=dojo.hostenv.getBaseScriptUri()+"iframe_history.html?"+(new Date()).getTime();
this.moveForward=true;
dojo.io.setIFrameSrc(this.historyIframe,url,false);
}
if(args["changeUrl"]){
this.changingUrl=true;
hash="#"+((args["changeUrl"]!==true)?args["changeUrl"]:(new Date()).getTime());
setTimeout("window.location.href = '"+hash+"'; dojo.undo.browser.changingUrl = false;",1);
this.bookmarkAnchor.href=hash;
if(dojo.render.html.ie){
var _668=args["back"]||args["backButton"]||args["handle"];
var tcb=function(_66a){
if(window.location.hash!=""){
setTimeout("window.location.href = '"+hash+"';",1);
}
_668.apply(this,[_66a]);
};
if(args["back"]){
args.back=tcb;
}else{
if(args["backButton"]){
args.backButton=tcb;
}else{
if(args["handle"]){
args.handle=tcb;
}
}
}
this.forwardStack=[];
var _66b=args["forward"]||args["forwardButton"]||args["handle"];
var tfw=function(_66d){
if(window.location.hash!=""){
window.location.href=hash;
}
if(_66b){
_66b.apply(this,[_66d]);
}
};
if(args["forward"]){
args.forward=tfw;
}else{
if(args["forwardButton"]){
args.forwardButton=tfw;
}else{
if(args["handle"]){
args.handle=tfw;
}
}
}
}else{
if(dojo.render.html.moz){
if(!this.locationTimer){
this.locationTimer=setInterval("dojo.undo.browser.checkLocation();",200);
}
}
}
}
this.historyStack.push({"url":url,"kwArgs":args,"urlHash":hash});
},checkLocation:function(){
if(!this.changingUrl){
var hsl=this.historyStack.length;
if((window.location.hash==this.initialHash||window.location.href==this.initialHref)&&(hsl==1)){
this.handleBackButton();
return;
}
if(this.forwardStack.length>0){
if(this.forwardStack[this.forwardStack.length-1].urlHash==window.location.hash){
this.handleForwardButton();
return;
}
}
if((hsl>=2)&&(this.historyStack[hsl-2])){
if(this.historyStack[hsl-2].urlHash==window.location.hash){
this.handleBackButton();
return;
}
}
}
},iframeLoaded:function(evt,_670){
if(!dojo.render.html.opera){
var _671=this._getUrlQuery(_670.href);
if(_671==null){
if(this.historyStack.length==1){
this.handleBackButton();
}
return;
}
if(this.moveForward){
this.moveForward=false;
return;
}
if(this.historyStack.length>=2&&_671==this._getUrlQuery(this.historyStack[this.historyStack.length-2].url)){
this.handleBackButton();
}else{
if(this.forwardStack.length>0&&_671==this._getUrlQuery(this.forwardStack[this.forwardStack.length-1].url)){
this.handleForwardButton();
}
}
}
},handleBackButton:function(){
var _672=this.historyStack.pop();
if(!_672){
return;
}
var last=this.historyStack[this.historyStack.length-1];
if(!last&&this.historyStack.length==0){
last=this.initialState;
}
if(last){
if(last.kwArgs["back"]){
last.kwArgs["back"]();
}else{
if(last.kwArgs["backButton"]){
last.kwArgs["backButton"]();
}else{
if(last.kwArgs["handle"]){
last.kwArgs.handle("back");
}
}
}
}
this.forwardStack.push(_672);
},handleForwardButton:function(){
var last=this.forwardStack.pop();
if(!last){
return;
}
if(last.kwArgs["forward"]){
last.kwArgs.forward();
}else{
if(last.kwArgs["forwardButton"]){
last.kwArgs.forwardButton();
}else{
if(last.kwArgs["handle"]){
last.kwArgs.handle("forward");
}
}
}
this.historyStack.push(last);
},_getUrlQuery:function(url){
var _676=url.split("?");
if(_676.length<2){
return null;
}else{
return _676[1];
}
}};
dojo.provide("dojo.io.BrowserIO");
dojo.io.checkChildrenForFile=function(node){
var _678=false;
var _679=node.getElementsByTagName("input");
dojo.lang.forEach(_679,function(_67a){
if(_678){
return;
}
if(_67a.getAttribute("type")=="file"){
_678=true;
}
});
return _678;
};
dojo.io.formHasFile=function(_67b){
return dojo.io.checkChildrenForFile(_67b);
};
dojo.io.updateNode=function(node,_67d){
node=dojo.byId(node);
var args=_67d;
if(dojo.lang.isString(_67d)){
args={url:_67d};
}
args.mimetype="text/html";
args.load=function(t,d,e){
while(node.firstChild){
if(dojo["event"]){
try{
dojo.event.browser.clean(node.firstChild);
}
catch(e){
}
}
node.removeChild(node.firstChild);
}
node.innerHTML=d;
};
dojo.io.bind(args);
};
dojo.io.formFilter=function(node){
var type=(node.type||"").toLowerCase();
return !node.disabled&&node.name&&!dojo.lang.inArray(type,["file","submit","image","reset","button"]);
};
dojo.io.encodeForm=function(_684,_685,_686){
if((!_684)||(!_684.tagName)||(!_684.tagName.toLowerCase()=="form")){
dojo.raise("Attempted to encode a non-form element.");
}
if(!_686){
_686=dojo.io.formFilter;
}
var enc=/utf/i.test(_685||"")?encodeURIComponent:dojo.string.encodeAscii;
var _688=[];
for(var i=0;i<_684.elements.length;i++){
var elm=_684.elements[i];
if(!elm||elm.tagName.toLowerCase()=="fieldset"||!_686(elm)){
continue;
}
var name=enc(elm.name);
var type=elm.type.toLowerCase();
if(type=="select-multiple"){
for(var j=0;j<elm.options.length;j++){
if(elm.options[j].selected){
_688.push(name+"="+enc(elm.options[j].value));
}
}
}else{
if(dojo.lang.inArray(type,["radio","checkbox"])){
if(elm.checked){
_688.push(name+"="+enc(elm.value));
}
}else{
_688.push(name+"="+enc(elm.value));
}
}
}
var _68e=_684.getElementsByTagName("input");
for(var i=0;i<_68e.length;i++){
var _68f=_68e[i];
if(_68f.type.toLowerCase()=="image"&&_68f.form==_684&&_686(_68f)){
var name=enc(_68f.name);
_688.push(name+"="+enc(_68f.value));
_688.push(name+".x=0");
_688.push(name+".y=0");
}
}
return _688.join("&")+"&";
};
dojo.io.FormBind=function(args){
this.bindArgs={};
if(args&&args.formNode){
this.init(args);
}else{
if(args){
this.init({formNode:args});
}
}
};
dojo.lang.extend(dojo.io.FormBind,{form:null,bindArgs:null,clickedButton:null,init:function(args){
var form=dojo.byId(args.formNode);
if(!form||!form.tagName||form.tagName.toLowerCase()!="form"){
throw new Error("FormBind: Couldn't apply, invalid form");
}else{
if(this.form==form){
return;
}else{
if(this.form){
throw new Error("FormBind: Already applied to a form");
}
}
}
dojo.lang.mixin(this.bindArgs,args);
this.form=form;
this.connect(form,"onsubmit","submit");
for(var i=0;i<form.elements.length;i++){
var node=form.elements[i];
if(node&&node.type&&dojo.lang.inArray(node.type.toLowerCase(),["submit","button"])){
this.connect(node,"onclick","click");
}
}
var _695=form.getElementsByTagName("input");
for(var i=0;i<_695.length;i++){
var _696=_695[i];
if(_696.type.toLowerCase()=="image"&&_696.form==form){
this.connect(_696,"onclick","click");
}
}
},onSubmit:function(form){
return true;
},submit:function(e){
e.preventDefault();
if(this.onSubmit(this.form)){
dojo.io.bind(dojo.lang.mixin(this.bindArgs,{formFilter:dojo.lang.hitch(this,"formFilter")}));
}
},click:function(e){
var node=e.currentTarget;
if(node.disabled){
return;
}
this.clickedButton=node;
},formFilter:function(node){
var type=(node.type||"").toLowerCase();
var _69d=false;
if(node.disabled||!node.name){
_69d=false;
}else{
if(dojo.lang.inArray(type,["submit","button","image"])){
if(!this.clickedButton){
this.clickedButton=node;
}
_69d=node==this.clickedButton;
}else{
_69d=!dojo.lang.inArray(type,["file","submit","reset","button"]);
}
}
return _69d;
},connect:function(_69e,_69f,_6a0){
if(dojo.evalObjPath("dojo.event.connect")){
dojo.event.connect(_69e,_69f,this,_6a0);
}else{
var fcn=dojo.lang.hitch(this,_6a0);
_69e[_69f]=function(e){
if(!e){
e=window.event;
}
if(!e.currentTarget){
e.currentTarget=e.srcElement;
}
if(!e.preventDefault){
e.preventDefault=function(){
window.event.returnValue=false;
};
}
fcn(e);
};
}
}});
dojo.io.XMLHTTPTransport=new function(){
var _6a3=this;
var _6a4={};
this.useCache=false;
this.preventCache=false;
function getCacheKey(url,_6a6,_6a7){
return url+"|"+_6a6+"|"+_6a7.toLowerCase();
}
function addToCache(url,_6a9,_6aa,http){
_6a4[getCacheKey(url,_6a9,_6aa)]=http;
}
function getFromCache(url,_6ad,_6ae){
return _6a4[getCacheKey(url,_6ad,_6ae)];
}
this.clearCache=function(){
_6a4={};
};
function doLoad(_6af,http,url,_6b2,_6b3){
if(((http.status>=200)&&(http.status<300))||(http.status==304)||(location.protocol=="file:"&&(http.status==0||http.status==undefined))||(location.protocol=="chrome:"&&(http.status==0||http.status==undefined))){
var ret;
if(_6af.method.toLowerCase()=="head"){
var _6b5=http.getAllResponseHeaders();
ret={};
ret.toString=function(){
return _6b5;
};
var _6b6=_6b5.split(/[\r\n]+/g);
for(var i=0;i<_6b6.length;i++){
var pair=_6b6[i].match(/^([^:]+)\s*:\s*(.+)$/i);
if(pair){
ret[pair[1]]=pair[2];
}
}
}else{
if(_6af.mimetype=="text/javascript"){
try{
ret=dj_eval(http.responseText);
}
catch(e){
dojo.debug(e);
dojo.debug(http.responseText);
ret=null;
}
}else{
if(_6af.mimetype=="text/json"){
try{
ret=dj_eval("("+http.responseText+")");
}
catch(e){
dojo.debug(e);
dojo.debug(http.responseText);
ret=false;
}
}else{
if((_6af.mimetype=="application/xml")||(_6af.mimetype=="text/xml")){
ret=http.responseXML;
if(!ret||typeof ret=="string"||!http.getResponseHeader("Content-Type")){
ret=dojo.dom.createDocumentFromText(http.responseText);
}
}else{
ret=http.responseText;
}
}
}
}
if(_6b3){
addToCache(url,_6b2,_6af.method,http);
}
_6af[(typeof _6af.load=="function")?"load":"handle"]("load",ret,http,_6af);
}else{
var _6b9=new dojo.io.Error("XMLHttpTransport Error: "+http.status+" "+http.statusText);
_6af[(typeof _6af.error=="function")?"error":"handle"]("error",_6b9,http,_6af);
}
}
function setHeaders(http,_6bb){
if(_6bb["headers"]){
for(var _6bc in _6bb["headers"]){
if(_6bc.toLowerCase()=="content-type"&&!_6bb["contentType"]){
_6bb["contentType"]=_6bb["headers"][_6bc];
}else{
http.setRequestHeader(_6bc,_6bb["headers"][_6bc]);
}
}
}
}
this.inFlight=[];
this.inFlightTimer=null;
this.startWatchingInFlight=function(){
if(!this.inFlightTimer){
this.inFlightTimer=setInterval("dojo.io.XMLHTTPTransport.watchInFlight();",10);
}
};
this.watchInFlight=function(){
var now=null;
for(var x=this.inFlight.length-1;x>=0;x--){
var tif=this.inFlight[x];
if(!tif){
this.inFlight.splice(x,1);
continue;
}
if(4==tif.http.readyState){
this.inFlight.splice(x,1);
doLoad(tif.req,tif.http,tif.url,tif.query,tif.useCache);
}else{
if(tif.startTime){
if(!now){
now=(new Date()).getTime();
}
if(tif.startTime+(tif.req.timeoutSeconds*1000)<now){
if(typeof tif.http.abort=="function"){
tif.http.abort();
}
this.inFlight.splice(x,1);
tif.req[(typeof tif.req.timeout=="function")?"timeout":"handle"]("timeout",null,tif.http,tif.req);
}
}
}
}
if(this.inFlight.length==0){
clearInterval(this.inFlightTimer);
this.inFlightTimer=null;
}
};
var _6c0=dojo.hostenv.getXmlhttpObject()?true:false;
this.canHandle=function(_6c1){
return _6c0&&dojo.lang.inArray((_6c1["mimetype"].toLowerCase()||""),["text/plain","text/html","application/xml","text/xml","text/javascript","text/json"])&&!(_6c1["formNode"]&&dojo.io.formHasFile(_6c1["formNode"]));
};
this.multipartBoundary="45309FFF-BD65-4d50-99C9-36986896A96F";
this.bind=function(_6c2){
if(!_6c2["url"]){
if(!_6c2["formNode"]&&(_6c2["backButton"]||_6c2["back"]||_6c2["changeUrl"]||_6c2["watchForURL"])&&(!djConfig.preventBackButtonFix)){
dojo.deprecated("Using dojo.io.XMLHTTPTransport.bind() to add to browser history without doing an IO request","Use dojo.undo.browser.addToHistory() instead.","0.4");
dojo.undo.browser.addToHistory(_6c2);
return true;
}
}
var url=_6c2.url;
var _6c4="";
if(_6c2["formNode"]){
var ta=_6c2.formNode.getAttribute("action");
if((ta)&&(!_6c2["url"])){
url=ta;
}
var tp=_6c2.formNode.getAttribute("method");
if((tp)&&(!_6c2["method"])){
_6c2.method=tp;
}
_6c4+=dojo.io.encodeForm(_6c2.formNode,_6c2.encoding,_6c2["formFilter"]);
}
if(url.indexOf("#")>-1){
dojo.debug("Warning: dojo.io.bind: stripping hash values from url:",url);
url=url.split("#")[0];
}
if(_6c2["file"]){
_6c2.method="post";
}
if(!_6c2["method"]){
_6c2.method="get";
}
if(_6c2.method.toLowerCase()=="get"){
_6c2.multipart=false;
}else{
if(_6c2["file"]){
_6c2.multipart=true;
}else{
if(!_6c2["multipart"]){
_6c2.multipart=false;
}
}
}
if(_6c2["backButton"]||_6c2["back"]||_6c2["changeUrl"]){
dojo.undo.browser.addToHistory(_6c2);
}
var _6c7=_6c2["content"]||{};
if(_6c2.sendTransport){
_6c7["dojo.transport"]="xmlhttp";
}
do{
if(_6c2.postContent){
_6c4=_6c2.postContent;
break;
}
if(_6c7){
_6c4+=dojo.io.argsFromMap(_6c7,_6c2.encoding);
}
if(_6c2.method.toLowerCase()=="get"||!_6c2.multipart){
break;
}
var t=[];
if(_6c4.length){
var q=_6c4.split("&");
for(var i=0;i<q.length;++i){
if(q[i].length){
var p=q[i].split("=");
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+p[0]+"\"","",p[1]);
}
}
}
if(_6c2.file){
if(dojo.lang.isArray(_6c2.file)){
for(var i=0;i<_6c2.file.length;++i){
var o=_6c2.file[i];
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}else{
var o=_6c2.file;
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}
if(t.length){
t.push("--"+this.multipartBoundary+"--","");
_6c4=t.join("\r\n");
}
}while(false);
var _6cd=_6c2["sync"]?false:true;
var _6ce=_6c2["preventCache"]||(this.preventCache==true&&_6c2["preventCache"]!=false);
var _6cf=_6c2["useCache"]==true||(this.useCache==true&&_6c2["useCache"]!=false);
if(!_6ce&&_6cf){
var _6d0=getFromCache(url,_6c4,_6c2.method);
if(_6d0){
doLoad(_6c2,_6d0,url,_6c4,false);
return;
}
}
var http=dojo.hostenv.getXmlhttpObject(_6c2);
var _6d2=false;
if(_6cd){
var _6d3=this.inFlight.push({"req":_6c2,"http":http,"url":url,"query":_6c4,"useCache":_6cf,"startTime":_6c2.timeoutSeconds?(new Date()).getTime():0});
this.startWatchingInFlight();
}
if(_6c2.method.toLowerCase()=="post"){
http.open("POST",url,_6cd);
setHeaders(http,_6c2);
http.setRequestHeader("Content-Type",_6c2.multipart?("multipart/form-data; boundary="+this.multipartBoundary):(_6c2.contentType||"application/x-www-form-urlencoded"));
try{
http.send(_6c4);
}
catch(e){
if(typeof http.abort=="function"){
http.abort();
}
doLoad(_6c2,{status:404},url,_6c4,_6cf);
}
}else{
var _6d4=url;
if(_6c4!=""){
_6d4+=(_6d4.indexOf("?")>-1?"&":"?")+_6c4;
}
if(_6ce){
_6d4+=(dojo.string.endsWithAny(_6d4,"?","&")?"":(_6d4.indexOf("?")>-1?"&":"?"))+"dojo.preventCache="+new Date().valueOf();
}
http.open(_6c2.method.toUpperCase(),_6d4,_6cd);
setHeaders(http,_6c2);
try{
http.send(null);
}
catch(e){
if(typeof http.abort=="function"){
http.abort();
}
doLoad(_6c2,{status:404},url,_6c4,_6cf);
}
}
if(!_6cd){
doLoad(_6c2,http,url,_6c4,_6cf);
}
_6c2.abort=function(){
return http.abort();
};
return;
};
dojo.io.transports.addTransport("XMLHTTPTransport");
};
dojo.provide("dojo.io.cookie");
dojo.io.cookie.setCookie=function(name,_6d6,days,path,_6d9,_6da){
var _6db=-1;
if(typeof days=="number"&&days>=0){
var d=new Date();
d.setTime(d.getTime()+(days*24*60*60*1000));
_6db=d.toGMTString();
}
_6d6=escape(_6d6);
document.cookie=name+"="+_6d6+";"+(_6db!=-1?" expires="+_6db+";":"")+(path?"path="+path:"")+(_6d9?"; domain="+_6d9:"")+(_6da?"; secure":"");
};
dojo.io.cookie.set=dojo.io.cookie.setCookie;
dojo.io.cookie.getCookie=function(name){
var idx=document.cookie.lastIndexOf(name+"=");
if(idx==-1){
return null;
}
var _6df=document.cookie.substring(idx+name.length+1);
var end=_6df.indexOf(";");
if(end==-1){
end=_6df.length;
}
_6df=_6df.substring(0,end);
_6df=unescape(_6df);
return _6df;
};
dojo.io.cookie.get=dojo.io.cookie.getCookie;
dojo.io.cookie.deleteCookie=function(name){
dojo.io.cookie.setCookie(name,"-",0);
};
dojo.io.cookie.setObjectCookie=function(name,obj,days,path,_6e6,_6e7,_6e8){
if(arguments.length==5){
_6e8=_6e6;
_6e6=null;
_6e7=null;
}
var _6e9=[],_6ea,_6eb="";
if(!_6e8){
_6ea=dojo.io.cookie.getObjectCookie(name);
}
if(days>=0){
if(!_6ea){
_6ea={};
}
for(var prop in obj){
if(prop==null){
delete _6ea[prop];
}else{
if(typeof obj[prop]=="string"||typeof obj[prop]=="number"){
_6ea[prop]=obj[prop];
}
}
}
prop=null;
for(var prop in _6ea){
_6e9.push(escape(prop)+"="+escape(_6ea[prop]));
}
_6eb=_6e9.join("&");
}
dojo.io.cookie.setCookie(name,_6eb,days,path,_6e6,_6e7);
};
dojo.io.cookie.getObjectCookie=function(name){
var _6ee=null,_6ef=dojo.io.cookie.getCookie(name);
if(_6ef){
_6ee={};
var _6f0=_6ef.split("&");
for(var i=0;i<_6f0.length;i++){
var pair=_6f0[i].split("=");
var _6f3=pair[1];
if(isNaN(_6f3)){
_6f3=unescape(pair[1]);
}
_6ee[unescape(pair[0])]=_6f3;
}
}
return _6ee;
};
dojo.io.cookie.isSupported=function(){
if(typeof navigator.cookieEnabled!="boolean"){
dojo.io.cookie.setCookie("__TestingYourBrowserForCookieSupport__","CookiesAllowed",90,null);
var _6f4=dojo.io.cookie.getCookie("__TestingYourBrowserForCookieSupport__");
navigator.cookieEnabled=(_6f4=="CookiesAllowed");
if(navigator.cookieEnabled){
this.deleteCookie("__TestingYourBrowserForCookieSupport__");
}
}
return navigator.cookieEnabled;
};
if(!dojo.io.cookies){
dojo.io.cookies=dojo.io.cookie;
}
dojo.provide("dojo.io.*");
dojo.provide("dojo.widget.RichText");
dojo.provide("dojo.widget.html.RichText");
try{
document.write("<textarea id=\"dojo.widget.RichText.savedContent\" "+"style=\"display:none;position:absolute;top:-100px;left:-100px;height:3px;width:3px;overflow:hidden;\"></textarea>");
}
catch(e){
}
dojo.widget.defineWidget("dojo.widget.html.RichText",dojo.widget.HtmlWidget,{inheritWidth:false,focusOnLoad:true,saveName:"",_content:"",height:null,minHeight:"1em",isClosed:true,isLoaded:false,useActiveX:false,relativeImageUrls:false,_SEPARATOR:"@@**%%__RICHTEXTBOUNDRY__%%**@@",fillInTemplate:function(){
this.open();
var _6f5=["queryCommandEnabled","queryCommandState","queryCommandValue","execCommand"];
for(var i=0;i<_6f5.length;i++){
dojo.event.connect("around",this,_6f5[i],this,"_normalizeCommand");
}
dojo.event.connect(this,"onKeyPressed",this,"afterKeyPress");
dojo.event.connect(this,"onKeyPress",this,"keyPress");
dojo.event.connect(this,"onKeyDown",this,"keyDown");
dojo.event.connect(this,"onKeyUp",this,"keyUp");
var ctrl=this.KEY_CTRL;
var exec=function(cmd,arg){
return arguments.length==1?function(){
this.execCommand(cmd);
}:function(){
this.execCommand(cmd,arg);
};
};
this.addKeyHandler("b",ctrl,exec("bold"));
this.addKeyHandler("i",ctrl,exec("italic"));
this.addKeyHandler("u",ctrl,exec("underline"));
this.addKeyHandler("a",ctrl,exec("selectall"));
this.addKeyHandler("s",ctrl,function(){
this.save(true);
});
this.addKeyHandler("1",ctrl,exec("formatblock","h1"));
this.addKeyHandler("2",ctrl,exec("formatblock","h2"));
this.addKeyHandler("3",ctrl,exec("formatblock","h3"));
this.addKeyHandler("4",ctrl,exec("formatblock","h4"));
this.addKeyHandler("\\",ctrl,exec("insertunorderedlist"));
if(!dojo.render.html.ie){
this.addKeyHandler("Z",ctrl,exec("redo"));
}
},events:["onBlur","onFocus","onKeyPress","onKeyDown","onKeyUp","onClick"],open:function(_6fb){
dojo.event.topic.publish("dojo.widget.RichText::open",this);
if(!this.isClosed){
this.close();
}
this._content="";
if((arguments.length==1)&&(_6fb["nodeName"])){
this.domNode=_6fb;
}
if((this.domNode["nodeName"])&&(this.domNode.nodeName.toLowerCase()=="textarea")){
this.textarea=this.domNode;
var html=dojo.string.trim(this.textarea.value);
if(html==""){
html="&nbsp;";
}
this.domNode=document.createElement("div");
with(this.textarea.style){
display="block";
position="absolute";
width="1px";
height="1px";
border=margin=padding="0px";
visiblity="hidden";
if(dojo.render.html.ie){
overflow="hidden";
}
}
dojo.dom.insertBefore(this.domNode,this.textarea);
this.domNode.innerHTML=html;
if(this.textarea.form){
dojo.event.connect(this.textarea.form,"onsubmit",dojo.lang.hitch(this,function(){
this.textarea.value=this.getEditorContent();
}));
}
var _6fd=this;
dojo.event.connect(this,"postCreate",function(){
dojo.dom.insertAfter(_6fd.textarea,_6fd.domNode);
});
}else{
var html=dojo.string.trim(this.domNode.innerHTML);
if(html==""){
html="&nbsp;";
}
}
this._oldHeight=dojo.style.getContentHeight(this.domNode);
this._oldWidth=dojo.style.getContentWidth(this.domNode);
this._firstChildContributingMargin=this._getContributingMargin(this.domNode,"top");
this._lastChildContributingMargin=this._getContributingMargin(this.domNode,"bottom");
this.savedContent=document.createElement("div");
while(this.domNode.hasChildNodes()){
this.savedContent.appendChild(this.domNode.firstChild);
}
if((this.domNode["nodeName"])&&(this.domNode.nodeName=="LI")){
this.domNode.innerHTML=" <br>";
}
if(this.saveName!=""){
var _6fe=document.getElementById("dojo.widget.RichText.savedContent");
if(_6fe.value!=""){
var _6ff=_6fe.value.split(this._SEPARATOR);
for(var i=0;i<_6ff.length;i++){
var data=_6ff[i].split(":");
if(data[0]==this.saveName){
html=data[1];
_6ff.splice(i,1);
break;
}
}
}
dojo.event.connect("before",window,"onunload",this,"_saveContent");
}
if(this.useActiveX&&dojo.render.html.ie){
this._drawObject(html);
}else{
if(dojo.render.html.ie){
this.editNode=document.createElement("div");
with(this.editNode){
innerHTML=html;
contentEditable=true;
style.height=this.height?this.height:this.minHeight;
}
if(this.height){
this.editNode.style.overflowY="scroll";
}
this.domNode.appendChild(this.editNode);
dojo.lang.forEach(this.events,function(e){
dojo.event.connect(this.editNode,e.toLowerCase(),this,e);
},this);
this.window=window;
this.document=document;
this.onLoad();
}else{
this._drawIframe(html);
}
}
if(this.domNode.nodeName=="LI"){
this.domNode.lastChild.style.marginTop="-1.2em";
}
dojo.html.addClass(this.domNode,"RichTextEditable");
this.isClosed=false;
},_hasCollapseableMargin:function(_703,side){
if(dojo.style.getPixelValue(_703,"border-"+side+"-width",false)){
return false;
}else{
if(dojo.style.getPixelValue(_703,"padding-"+side,false)){
return false;
}else{
return true;
}
}
},_getContributingMargin:function(_705,_706){
if(_706=="top"){
var _707="previousSibling";
var _708="nextSibling";
var _709="firstChild";
var _70a="margin-top";
var _70b="margin-bottom";
}else{
var _707="nextSibling";
var _708="previousSibling";
var _709="lastChild";
var _70a="margin-bottom";
var _70b="margin-top";
}
var _70c=dojo.style.getPixelValue(_705,_70a,false);
function isSignificantNode(_70d){
return !(_70d.nodeType==3&&dojo.string.isBlank(_70d.data))&&dojo.style.getStyle(_70d,"display")!="none"&&!dojo.style.isPositionAbsolute(_70d);
}
var _70e=0;
var _70f=_705[_709];
while(_70f){
while((!isSignificantNode(_70f))&&_70f[_708]){
_70f=_70f[_708];
}
_70e=Math.max(_70e,dojo.style.getPixelValue(_70f,_70a,false));
if(!this._hasCollapseableMargin(_70f,_706)){
break;
}
_70f=_70f[_709];
}
if(!this._hasCollapseableMargin(_705,_706)){
return parseInt(_70e);
}
var _710=0;
var _711=_705[_707];
while(_711){
if(isSignificantNode(_711)){
_710=dojo.style.getPixelValue(_711,_70b,false);
break;
}
_711=_711[_707];
}
if(!_711){
_710=dojo.style.getPixelValue(_705.parentNode,_70a,false);
}
if(_70e>_70c){
return parseInt(Math.max((_70e-_70c)-_710,0));
}else{
return 0;
}
},_drawIframe:function(html){
var _713=Boolean(dojo.render.html.moz&&(typeof window.XML=="undefined"));
if(!this.iframe){
var _714=(new dojo.uri.Uri(document.location)).host;
this.iframe=document.createElement("iframe");
with(this.iframe){
scrolling=this.height?"auto":"no";
style.border="none";
style.lineHeight="0";
style.verticalAlign="bottom";
}
}
this.iframe.src=dojo.uri.dojoUri("src/widget/templates/richtextframe.html")+"#"+((document.domain!=_714)?document.domain:"");
this.iframe.width=this.inheritWidth?this._oldWidth:"100%";
if(this.height){
this.iframe.style.height=this.height;
}else{
var _715=this._oldHeight;
if(this._hasCollapseableMargin(this.domNode,"top")){
_715+=this._firstChildContributingMargin;
}
if(this._hasCollapseableMargin(this.domNode,"bottom")){
_715+=this._lastChildContributingMargin;
}
this.iframe.height=_715;
}
var _716=document.createElement("div");
_716.innerHTML=html;
if(this.relativeImageUrls){
var imgs=_716.getElementsByTagName("img");
for(var i=0;i<imgs.length;i++){
imgs[i].src=(new dojo.uri.Uri(window.location,imgs[i].src)).toString();
}
html=_716.innerHTML;
}
var _719=dojo.dom.firstElement(_716);
var _71a=dojo.dom.lastElement(_716);
if(_719){
_719.style.marginTop=this._firstChildContributingMargin+"px";
}
if(_71a){
_71a.style.marginBottom=this._lastChildContributingMargin+"px";
}
_716.style.position="absolute";
this.domNode.appendChild(_716);
this.domNode.appendChild(this.iframe);
var _71b=false;
var _71c=dojo.lang.hitch(this,function(){
if(!_71b){
_71b=true;
}else{
return;
}
if(!this.editNode){
if(this.iframe.contentWindow){
this.window=this.iframe.contentWindow;
}else{
this.window=this.iframe.contentDocument.window;
}
if(dojo.render.html.moz){
this.document=this.iframe.contentWindow.document;
}else{
this.document=this.iframe.contentDocument;
}
var _71d=(function(_71e){
return function(_71f){
return dojo.style.getStyle(_71e,_71f);
};
})(this.domNode);
var font=_71d("font-weight")+" "+_71d("font-size")+" "+_71d("font-family");
var _721="1.0";
var _722=dojo.style.getUnitValue(this.domNode,"line-height");
if(_722.value&&_722.units==""){
_721=_722.value;
}
dojo.style.insertCssText("    body,html { background: transparent; padding: 0; margin: 0; }\n"+"    body { top: 0; left: 0; right: 0;"+(this.height?"":" position: fixed; ")+"        font: "+font+";\n"+"        min-height: "+this.minHeight+"; \n"+"        line-height: "+_721+"} \n"+"    p { margin: 1em 0 !important; }\n"+"    body > *:first-child { padding-top: 0 !important; margin-top: "+this._firstChildContributingMargin+"px !important; }\n"+"    body > *:last-child { padding-bottom: 0 !important; margin-bottom: "+this._lastChildContributingMargin+"px !important; }\n"+"    li > ul:-moz-first-node, li > ol:-moz-first-node { padding-top: 1.2em; }\n"+"    li { min-height: 1.2em; }\n"+"",this.document);
_716.parentNode.removeChild(_716);
this.document.body.innerHTML=html;
if(_713){
this.document.designMode="on";
}
this.onLoad();
}else{
_716.parentNode.removeChild(_716);
this.editNode.innerHTML=html;
this.onDisplayChanged();
}
});
if(this.editNode){
_71c();
}else{
if(dojo.render.html.moz){
this.iframe.onload=function(){
setTimeout(_71c,250);
};
}else{
this.iframe.onload=_71c;
}
}
},_drawObject:function(html){
this.object=document.createElement("object");
with(this.object){
classid="clsid:2D360201-FFF5-11D1-8D03-00A0C959BC0A";
width=this.inheritWidth?this._oldWidth:"100%";
style.height=this.height?this.height:(this._oldHeight+"px");
Scrollbars=this.height?true:false;
Appearance=this._activeX.appearance.flat;
}
this.domNode.appendChild(this.object);
this.object.attachEvent("DocumentComplete",dojo.lang.hitch(this,"onLoad"));
this.object.attachEvent("DisplayChanged",dojo.lang.hitch(this,"_updateHeight"));
this.object.attachEvent("DisplayChanged",dojo.lang.hitch(this,"onDisplayChanged"));
dojo.lang.forEach(this.events,function(e){
this.object.attachEvent(e.toLowerCase(),dojo.lang.hitch(this,e));
},this);
this.object.DocumentHTML="<!doctype HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">"+"<title></title>"+"<style type=\"text/css\">"+"    body,html { padding: 0; margin: 0; }"+(this.height?"":"    body { overflow: hidden; }")+"</style>"+"<body><div id=\"bodywrapper\">"+html+"</div></body>";
},_isResized:function(){
return false;
},onLoad:function(e){
this.isLoaded=true;
if(this.object){
this.document=this.object.DOM;
this.window=this.document.parentWindow;
this.editNode=this.document.body.firstChild;
this.domNode.style.height=this.height?this.height:this.minHeight;
this.connect(this,"onDisplayChanged","_updateHeight");
}else{
if(this.iframe){
this.editNode=this.document.body;
this.connect(this,"onDisplayChanged","_updateHeight");
try{
this.document.execCommand("useCSS",false,true);
this.document.execCommand("styleWithCSS",false,false);
}
catch(e2){
}
if(dojo.render.html.safari){
this.connect(this.editNode,"onblur","onBlur");
this.connect(this.editNode,"onfocus","onFocus");
this.interval=setInterval(dojo.lang.hitch(this,"onDisplayChanged"),750);
}else{
if(dojo.render.html.mozilla||dojo.render.html.opera){
var doc=this.document;
var _727=dojo.event.browser.addListener(this.document,"blur",dojo.lang.hitch(this,"onBlur"));
var _728={unBlur:function(e){
dojo.event.browser.removeListener(doc,"blur",_727);
}};
dojo.event.connect("before",this,"close",_728,"unBlur");
dojo.event.browser.addListener(this.document,"focus",dojo.lang.hitch(this,"onFocus"));
var _72a=dojo.event.browser.addListener;
_72a(this.document,"keypress",dojo.lang.hitch(this,"onKeyPress"));
_72a(this.document,"keydown",dojo.lang.hitch(this,"onKeyDown"));
_72a(this.document,"keyup",dojo.lang.hitch(this,"onKeyUp"));
_72a(this.document,"click",dojo.lang.hitch(this,"onClick"));
}
}
}else{
if(dojo.render.html.ie){
this.editNode.style.zoom=1;
}
}
}
if(this.focusOnLoad){
this.focus();
}
this.onDisplayChanged(e);
},onKeyDown:function(e){
if((!e)&&(this.object)){
e=dojo.event.browser.fixEvent(this.window.event);
}
dojo.debug("onkeydown:",e.keyCode);
if((dojo.render.html.ie)&&(e.keyCode==e.KEY_TAB)){
e.preventDefault();
e.stopPropagation();
this.execCommand((e.shiftKey?"outdent":"indent"));
}else{
if(dojo.render.html.ie){
if((65<=e.keyCode)&&(e.keyCode<=90)){
e.charCode=e.keyCode;
this.onKeyPress(e);
}
}
}
},onKeyUp:function(e){
return;
},KEY_CTRL:1,onKeyPress:function(e){
if((!e)&&(this.object)){
e=dojo.event.browser.fixEvent(this.window.event);
}
var _72e=e.charCode>0?String.fromCharCode(e.charCode):null;
var code=e.keyCode;
var _730=e.ctrlKey?this.KEY_CTRL:0;
if(this._keyHandlers[_72e]){
dojo.debug("char:",_72e);
var _731=this._keyHandlers[_72e],i=0,_733;
while(_733=_731[i++]){
if(_730==_733.modifiers){
_733.handler.call(this);
e.preventDefault();
break;
}
}
}
dojo.lang.setTimeout(this,this.onKeyPressed,1,e);
},addKeyHandler:function(key,_735,_736){
if(!(this._keyHandlers[key] instanceof Array)){
this._keyHandlers[key]=[];
}
this._keyHandlers[key].push({modifiers:_735||0,handler:_736});
},onKeyPressed:function(e){
this.onDisplayChanged();
},onClick:function(e){
this.onDisplayChanged(e);
},onBlur:function(e){
},_initialFocus:true,onFocus:function(e){
if((dojo.render.html.mozilla)&&(this._initialFocus)){
this._initialFocus=false;
if(dojo.string.trim(this.editNode.innerHTML)=="&nbsp;"){
this.execCommand("selectall");
this.window.getSelection().collapseToStart();
}
}
},blur:function(){
if(this.iframe){
this.window.blur();
}else{
if(this.editNode){
this.editNode.blur();
}
}
},focus:function(){
if(this.iframe){
this.window.focus();
}else{
if(this.editNode){
this.editNode.focus();
}
}
},onDisplayChanged:function(e){
},_activeX:{command:{bold:5000,italic:5023,underline:5048,justifycenter:5024,justifyleft:5025,justifyright:5026,cut:5003,copy:5002,paste:5032,"delete":5004,undo:5049,redo:5033,removeformat:5034,selectall:5035,unlink:5050,indent:5018,outdent:5031,insertorderedlist:5030,insertunorderedlist:5051,inserttable:5022,insertcell:5019,insertcol:5020,insertrow:5021,deletecells:5005,deletecols:5006,deleterows:5007,mergecells:5029,splitcell:5047,setblockformat:5043,getblockformat:5011,getblockformatnames:5012,setfontname:5044,getfontname:5013,setfontsize:5045,getfontsize:5014,setbackcolor:5042,getbackcolor:5010,setforecolor:5046,getforecolor:5015,findtext:5008,font:5009,hyperlink:5016,image:5017,lockelement:5027,makeabsolute:5028,sendbackward:5036,bringforward:5037,sendbelowtext:5038,bringabovetext:5039,sendtoback:5040,bringtofront:5041,properties:5052},ui:{"default":0,prompt:1,noprompt:2},status:{notsupported:0,disabled:1,enabled:3,latched:7,ninched:11},appearance:{flat:0,inset:1},state:{unchecked:0,checked:1,gray:2}},_normalizeCommand:function(_73c){
var drh=dojo.render.html;
var _73e=_73c.args[0].toLowerCase();
if(_73e=="formatblock"){
if(drh.safari){
_73e="heading";
}
if(drh.ie){
_73c.args[1]="<"+_73c.args[1]+">";
}
}
if(_73e=="hilitecolor"&&!drh.mozilla){
_73e="backcolor";
}
_73c.args[0]=_73e;
if(_73c.args.length>1){
var _73f=_73c.args[1];
if(_73e=="heading"){
throw new Error("unimplemented");
}
_73c.args[1]=_73f;
}
return _73c.proceed();
},queryCommandAvailable:function(_740){
var ie=1;
var _742=1<<1;
var _743=1<<2;
var _744=1<<3;
function isSupportedBy(_745){
return {ie:Boolean(_745&ie),mozilla:Boolean(_745&_742),safari:Boolean(_745&_743),opera:Boolean(_745&_744)};
}
var _746=null;
switch(_740.toLowerCase()){
case "bold":
case "italic":
case "underline":
case "subscript":
case "superscript":
case "fontname":
case "fontsize":
case "forecolor":
case "hilitecolor":
case "justifycenter":
case "justifyfull":
case "justifyleft":
case "justifyright":
case "delete":
case "undo":
case "redo":
_746=isSupportedBy(_742|ie|_743|_744);
break;
case "createlink":
case "unlink":
case "removeformat":
case "inserthorizontalrule":
case "insertimage":
case "insertorderedlist":
case "insertunorderedlist":
case "indent":
case "outdent":
case "formatblock":
case "inserthtml":
_746=isSupportedBy(_742|ie|_744);
break;
case "strikethrough":
_746=isSupportedBy(_742|_744|(this.object?0:ie));
break;
case "blockdirltr":
case "blockdirrtl":
case "dirltr":
case "dirrtl":
case "inlinedirltr":
case "inlinedirrtl":
case "cut":
case "copy":
case "paste":
_746=isSupportedBy(ie);
break;
case "inserttable":
_746=isSupportedBy(_742|(this.object?ie:0));
break;
case "insertcell":
case "insertcol":
case "insertrow":
case "deletecells":
case "deletecols":
case "deleterows":
case "mergecells":
case "splitcell":
_746=isSupportedBy(this.object?ie:0);
break;
default:
return false;
}
return (dojo.render.html.ie&&_746.ie)||(dojo.render.html.mozilla&&_746.mozilla)||(dojo.render.html.safari&&_746.safari)||(dojo.render.html.opera&&_746.opera);
},execCommand:function(_747,_748){
var _749;
if(this.object){
if(_747=="forecolor"){
_747="setforecolor";
}else{
if(_747=="backcolor"){
_747="setbackcolor";
}
}
if(_747=="inserttable"){
var _74a=this.constructor._tableInfo;
if(!_74a){
_74a=document.createElement("object");
_74a.classid="clsid:47B0DFC7-B7A3-11D1-ADC5-006008A5848C";
document.body.appendChild(_74a);
this.constructor._table=_74a;
}
_74a.NumRows=_748["rows"];
_74a.NumCols=_748["cols"];
_74a.TableAttrs=_748["TableAttrs"];
_74a.CellAttrs=_748["CellAttrs"];
_74a.Caption=_748["Caption"];
}
if(_747=="inserthtml"){
var _74b=this.document.selection.createRange();
_74b.select();
_74b.pasteHTML(_748);
_74b.collapse(true);
return true;
}else{
if(arguments.length==1){
return this.object.ExecCommand(this._activeX.command[_747],this._activeX.ui.noprompt);
}else{
return this.object.ExecCommand(this._activeX.command[_747],this._activeX.ui.noprompt,_748);
}
}
}else{
if(_747=="inserthtml"){
if(dojo.render.html.ie){
dojo.debug("inserthtml breaks the undo stack when not using the ActiveX version of the control!");
var _74b=this.document.selection.createRange();
_74b.select();
_74b.pasteHTML(_748);
_74b.collapse(true);
return true;
}else{
return this.document.execCommand(_747,false,_748);
}
}else{
if((_747=="unlink")&&(this.queryCommandEnabled("unlink"))&&(dojo.render.html.mozilla)){
var _74c=this.window.getSelection();
var _74d=_74c.getRangeAt(0);
var _74e=_74d.startContainer;
var _74f=_74d.startOffset;
var _750=_74d.endContainer;
var _751=_74d.endOffset;
var _752=document.createRange();
var a=this.getSelectedNode();
while(a.nodeName!="A"){
a=a.parentNode;
}
_752.selectNode(a);
_74c.removeAllRanges();
_74c.addRange(_752);
_749=this.document.execCommand("unlink",false,null);
var _74d=document.createRange();
_74d.setStart(_74e,_74f);
_74d.setEnd(_750,_751);
_74c.removeAllRanges();
_74c.addRange(_74d);
return _749;
}else{
if((_747=="inserttable")&&(dojo.render.html.mozilla)){
var cols="<tr>";
for(var i=0;i<_748.cols;i++){
cols+="<td></td>";
}
cols+="</tr>";
var _756="<table><tbody>";
for(var i=0;i<_748.rows;i++){
_756+=cols;
}
_756+="</tbody></table>";
_749=this.document.execCommand("inserthtml",false,_756);
}else{
if((_747=="hilitecolor")&&(dojo.render.html.mozilla)){
this.document.execCommand("useCSS",false,false);
_749=this.document.execCommand(_747,false,_748);
this.document.execCommand("useCSS",false,true);
}else{
if((dojo.render.html.ie)&&((_747=="backcolor")||(_747=="forecolor"))){
var tr=this.document.selection.createRange();
_748=arguments.length>1?_748:null;
_749=this.document.execCommand(_747,false,_748);
setTimeout(function(){
tr.select();
},1);
}else{
_748=arguments.length>1?_748:null;
if(dojo.render.html.moz){
this.document=this.iframe.contentWindow.document;
}
_749=this.document.execCommand(_747,false,_748);
}
}
}
}
}
}
this.onDisplayChanged();
return _749;
},queryCommandEnabled:function(_758,_759){
if(this.object){
if(_758=="forecolor"){
_758="setforecolor";
}else{
if(_758=="backcolor"){
_758="setbackcolor";
}
}
if(typeof this._activeX.command[_758]=="undefined"){
return false;
}
var _75a=this.object.QueryStatus(this._activeX.command[_758]);
return ((_75a!=this.activeX.status.notsupported)&&(_75a!=this.activeX.status.diabled));
}else{
if(_758=="unlink"&&dojo.render.html.mozilla){
var node=this.getSelectedNode();
while(node.parentNode&&node.nodeName!="A"){
node=node.parentNode;
}
return node.nodeName=="A";
}else{
if(_758=="inserttable"&&dojo.render.html.mozilla){
return true;
}
}
var elem=(dojo.render.html.ie)?this.document.selection.createRange():this.document;
return elem.queryCommandEnabled(_758);
}
},queryCommandState:function(_75d,_75e){
if(this.object){
if(_75d=="forecolor"){
_75d="setforecolor";
}else{
if(_75d=="backcolor"){
_75d="setbackcolor";
}
}
if(typeof this._activeX.command[_75d]=="undefined"){
return null;
}
var _75f=this.object.QueryStatus(this._activeX.command[_75d]);
return ((_75f==this._activeX.status.enabled)||(_75f==this._activeX.status.ninched));
}else{
return this.document.queryCommandState(_75d);
}
},queryCommandValue:function(_760,_761){
if(this.object){
switch(_760){
case "forecolor":
case "backcolor":
case "fontsize":
case "fontname":
case "blockformat":
_760="get"+_760;
return this.object.execCommand(this._activeX.command[_760],this._activeX.ui.noprompt);
}
}else{
return this.document.queryCommandValue(_760);
}
},getSelectedNode:function(){
if(!this.isLoaded){
return;
}
if(this.document.selection){
return this.document.selection.createRange().parentElement();
}else{
if(dojo.render.html.mozilla){
return this.window.getSelection().getRangeAt(0).commonAncestorContainer;
}
}
return this.editNode;
},placeCursorAtStart:function(){
if(!this.isLoaded){
dojo.event.connect(this,"onLoad",this,"placeCursorAtEnd");
return;
}
dojo.event.disconnect(this,"onLoad",this,"placeCursorAtEnd");
if(this.window.getSelection){
var _762=this.window.getSelection;
if(_762.removeAllRanges){
var _763=this.document.createRange();
_763.selectNode(this.editNode.firstChild);
_763.collapse(true);
var _762=this.window.getSelection();
_762.removeAllRanges();
_762.addRange(_763);
}else{
}
}else{
if(this.document.selection){
var _763=this.document.body.createTextRange();
_763.moveToElementText(this.editNode);
_763.collapse(true);
_763.select();
}
}
},replaceEditorContent:function(html){
if(this.window.getSelection){
var _765=this.window.getSelection;
if(dojo.render.html.moz){
var _766=this.document.createRange();
_766.selectNodeContents(this.editNode);
var _765=this.window.getSelection();
_765.removeAllRanges();
_765.addRange(_766);
this.execCommand("inserthtml",html);
}else{
this.editNode.innerHTML=html;
}
}else{
if(this.document.selection){
var _766=this.document.body.createTextRange();
_766.moveToElementText(this.editNode);
_766.select();
this.execCommand("inserthtml",html);
}
}
},placeCursorAtEnd:function(){
if(!this.isLoaded){
dojo.event.connect(this,"onLoad",this,"placeCursorAtEnd");
return;
}
dojo.event.disconnect(this,"onLoad",this,"placeCursorAtEnd");
if(this.window.getSelection){
var _767=this.window.getSelection;
if(_767.removeAllRanges){
var _768=this.document.createRange();
_768.selectNode(this.editNode.lastChild);
_768.collapse(false);
var _767=this.window.getSelection();
_767.removeAllRanges();
_767.addRange(_768);
}else{
}
}else{
if(this.document.selection){
var _768=this.document.body.createTextRange();
_768.moveToElementText(this.editNode);
_768.collapse(true);
_768.select();
}
}
},_lastHeight:0,_updateHeight:function(){
if(!this.isLoaded){
return;
}
if(this.height){
return;
}
if(this.iframe){
var _769=["margin-top","margin-bottom","padding-bottom","padding-top","border-width-bottom","border-width-top"];
for(var i=0,_76b=0;i<_769.length;i++){
var _76c=dojo.style.getStyle(this.iframe,_769[i]);
if(_76c){
_76b+=Number(_76c.replace(/[^0-9]/g,""));
}
}
if(this.document.body["offsetHeight"]){
this._lastHeight=Math.max(this.document.body.scrollHeight,this.document.body.offsetHeight)+_76b;
this.iframe.height=this._lastHeight+"px";
this.window.scrollTo(0,0);
}
}else{
if(this.object){
this.object.style.height=dojo.style.getInnerHeight(this.editNode)+"px";
}
}
},_saveContent:function(e){
var _76e=document.getElementById("dojo.widget.RichText.savedContent");
_76e.value+=this._SEPARATOR+this.saveName+":"+this.getEditorContent();
},getEditorContent:function(){
var ec="";
try{
ec=(this._content.length>0)?this._content:this.editNode.innerHTML;
if(dojo.string.trim(ec)=="&nbsp;"){
ec="";
}
}
catch(e){
}
dojo.lang.forEach(this.contentFilters,function(ef){
ec=ef(ec);
});
if(this.relativeImageUrls){
var _771=window.location.protocol+"//"+window.location.host;
var _772=window.location.pathname;
if(_772.match(/\/$/)){
}else{
var _773=_772.split("/");
if(_773.length){
_773.pop();
}
_772=_773.join("/")+"/";
}
var _774=new RegExp("(<img[^>]* src=[\"'])("+_771+"("+_772+")?)","ig");
ec=ec.replace(_774,"$1");
}
return ec;
},close:function(save,_776){
if(this.isClosed){
return false;
}
if(arguments.length==0){
save=true;
}
this._content=this.editNode.innerHTML;
var _777=(this.savedContent.innerHTML!=this._content);
if(this.interval){
clearInterval(this.interval);
}
if(dojo.render.html.ie&&!this.object){
dojo.event.browser.clean(this.editNode);
}
if(this.iframe){
delete this.iframe;
}
this.domNode.innerHTML="";
if(save){
dojo.event.browser.clean(this.savedContent);
if(dojo.render.html.moz){
var nc=document.createElement("span");
this.domNode.appendChild(nc);
nc.innerHTML=this.editNode.innerHTML;
}else{
this.domNode.innerHTML=this._content;
}
}else{
while(this.savedContent.hasChildNodes()){
this.domNode.appendChild(this.savedContent.firstChild);
}
}
delete this.savedContent;
dojo.html.removeClass(this.domNode,"RichTextEditable");
this.isClosed=true;
this.isLoaded=false;
delete this.editNode;
return _777;
},destroyRendering:function(){
},destroy:function(){
this.destroyRendering();
if(!this.isClosed){
this.close(false);
}
while(this._connected.length){
this.disconnect(this._connected[0],this._connected[1],this._connected[2]);
}
},_connected:[],connect:function(_779,_77a,_77b){
dojo.event.connect(_779,_77a,this,_77b);
},disconnect:function(_77c,_77d,_77e){
for(var i=0;i<this._connected.length;i++){
if(this._connected[0]==_77c&&this._connected[1]==_77d&&this._connected[2]==_77e){
dojo.event.disconnect(_77c,_77d,this,_77e);
this._connected.splice(i,1);
break;
}
}
},disconnectAllWithRoot:function(_780){
for(var i=0;i<this._connected.length;i++){
if(this._connected[0]==_780){
dojo.event.disconnect(_780,this._connected[1],this,this._connected[2]);
this._connected.splice(i,1);
}
}
}},"html",function(){
this.contentFilters=[];
this._keyHandlers={};
});
dojo.provide("dojo.lang.type");
dojo.lang.whatAmI=function(wh){
try{
if(dojo.lang.isArray(wh)){
return "array";
}
if(dojo.lang.isFunction(wh)){
return "function";
}
if(dojo.lang.isString(wh)){
return "string";
}
if(dojo.lang.isNumber(wh)){
return "number";
}
if(dojo.lang.isBoolean(wh)){
return "boolean";
}
if(dojo.lang.isAlien(wh)){
return "alien";
}
if(dojo.lang.isUndefined(wh)){
return "undefined";
}
for(var name in dojo.lang.whatAmI.custom){
if(dojo.lang.whatAmI.custom[name](wh)){
return name;
}
}
if(dojo.lang.isObject(wh)){
return "object";
}
}
catch(E){
}
return "unknown";
};
dojo.lang.whatAmI.custom={};
dojo.lang.isNumeric=function(wh){
return (!isNaN(wh)&&isFinite(wh)&&(wh!=null)&&!dojo.lang.isBoolean(wh)&&!dojo.lang.isArray(wh));
};
dojo.lang.isBuiltIn=function(wh){
return (dojo.lang.isArray(wh)||dojo.lang.isFunction(wh)||dojo.lang.isString(wh)||dojo.lang.isNumber(wh)||dojo.lang.isBoolean(wh)||(wh==null)||(wh instanceof Error)||(typeof wh=="error"));
};
dojo.lang.isPureObject=function(wh){
return ((wh!=null)&&dojo.lang.isObject(wh)&&wh.constructor==Object);
};
dojo.lang.isOfType=function(_787,type){
if(dojo.lang.isArray(type)){
var _789=type;
for(var i in _789){
var _78b=_789[i];
if(dojo.lang.isOfType(_787,_78b)){
return true;
}
}
return false;
}else{
if(dojo.lang.isString(type)){
type=type.toLowerCase();
}
switch(type){
case Array:
case "array":
return dojo.lang.isArray(_787);
break;
case Function:
case "function":
return dojo.lang.isFunction(_787);
break;
case String:
case "string":
return dojo.lang.isString(_787);
break;
case Number:
case "number":
return dojo.lang.isNumber(_787);
break;
case "numeric":
return dojo.lang.isNumeric(_787);
break;
case Boolean:
case "boolean":
return dojo.lang.isBoolean(_787);
break;
case Object:
case "object":
return dojo.lang.isObject(_787);
break;
case "pureobject":
return dojo.lang.isPureObject(_787);
break;
case "builtin":
return dojo.lang.isBuiltIn(_787);
break;
case "alien":
return dojo.lang.isAlien(_787);
break;
case "undefined":
return dojo.lang.isUndefined(_787);
break;
case null:
case "null":
return (_787===null);
break;
case "optional":
return ((_787===null)||dojo.lang.isUndefined(_787));
break;
default:
if(dojo.lang.isFunction(type)){
return (_787 instanceof type);
}else{
dojo.raise("dojo.lang.isOfType() was passed an invalid type");
}
break;
}
}
dojo.raise("If we get here, it means a bug was introduced above.");
};
dojo.lang.getObject=function(str){
var _78d=str.split("."),i=0,obj=dj_global;
do{
obj=obj[_78d[i++]];
}while(i<_78d.length&&obj);
return (obj!=dj_global)?obj:null;
};
dojo.lang.doesObjectExist=function(str){
var _791=str.split("."),i=0,obj=dj_global;
do{
obj=obj[_791[i++]];
}while(i<_791.length&&obj);
return (obj&&obj!=dj_global);
};
dojo.provide("dojo.lang.assert");
dojo.lang.assert=function(_794,_795){
if(!_794){
var _796="An assert statement failed.\n"+"The method dojo.lang.assert() was called with a 'false' value.\n";
if(_795){
_796+="Here's the assert message:\n"+_795+"\n";
}
throw new Error(_796);
}
};
dojo.lang.assertType=function(_797,type,_799){
if(!dojo.lang.isOfType(_797,type)){
if(!_799){
if(!dojo.lang.assertType._errorMessage){
dojo.lang.assertType._errorMessage="Type mismatch: dojo.lang.assertType() failed.";
}
_799=dojo.lang.assertType._errorMessage;
}
dojo.lang.assert(false,_799);
}
};
dojo.lang.assertValidKeywords=function(_79a,_79b,_79c){
var key;
if(!_79c){
if(!dojo.lang.assertValidKeywords._errorMessage){
dojo.lang.assertValidKeywords._errorMessage="In dojo.lang.assertValidKeywords(), found invalid keyword:";
}
_79c=dojo.lang.assertValidKeywords._errorMessage;
}
if(dojo.lang.isArray(_79b)){
for(key in _79a){
if(!dojo.lang.inArray(_79b,key)){
dojo.lang.assert(false,_79c+" "+key);
}
}
}else{
for(key in _79a){
if(!(key in _79b)){
dojo.lang.assert(false,_79c+" "+key);
}
}
}
};
dojo.provide("dojo.AdapterRegistry");
dojo.AdapterRegistry=function(){
this.pairs=[];
};
dojo.lang.extend(dojo.AdapterRegistry,{register:function(name,_79f,wrap,_7a1){
if(_7a1){
this.pairs.unshift([name,_79f,wrap]);
}else{
this.pairs.push([name,_79f,wrap]);
}
},match:function(){
for(var i=0;i<this.pairs.length;i++){
var pair=this.pairs[i];
if(pair[1].apply(this,arguments)){
return pair[2].apply(this,arguments);
}
}
throw new Error("No match found");
},unregister:function(name){
for(var i=0;i<this.pairs.length;i++){
var pair=this.pairs[i];
if(pair[0]==name){
this.pairs.splice(i,1);
return true;
}
}
return false;
}});
dojo.provide("dojo.lang.repr");
dojo.lang.reprRegistry=new dojo.AdapterRegistry();
dojo.lang.registerRepr=function(name,_7a8,wrap,_7aa){
dojo.lang.reprRegistry.register(name,_7a8,wrap,_7aa);
};
dojo.lang.repr=function(obj){
if(typeof (obj)=="undefined"){
return "undefined";
}else{
if(obj===null){
return "null";
}
}
try{
if(typeof (obj["__repr__"])=="function"){
return obj["__repr__"]();
}else{
if((typeof (obj["repr"])=="function")&&(obj.repr!=arguments.callee)){
return obj["repr"]();
}
}
return dojo.lang.reprRegistry.match(obj);
}
catch(e){
if(typeof (obj.NAME)=="string"&&(obj.toString==Function.prototype.toString||obj.toString==Object.prototype.toString)){
return o.NAME;
}
}
if(typeof (obj)=="function"){
obj=(obj+"").replace(/^\s+/,"");
var idx=obj.indexOf("{");
if(idx!=-1){
obj=obj.substr(0,idx)+"{...}";
}
}
return obj+"";
};
dojo.lang.reprArrayLike=function(arr){
try{
var na=dojo.lang.map(arr,dojo.lang.repr);
return "["+na.join(", ")+"]";
}
catch(e){
}
};
dojo.lang.reprString=function(str){
dojo.deprecated("dojo.lang.reprNumber","use `String(num)` instead","0.4");
return dojo.string.escapeString(str);
};
dojo.lang.reprNumber=function(num){
dojo.deprecated("dojo.lang.reprNumber","use `String(num)` instead","0.4");
return num+"";
};
(function(){
var m=dojo.lang;
m.registerRepr("arrayLike",m.isArrayLike,m.reprArrayLike);
m.registerRepr("string",m.isString,m.reprString);
m.registerRepr("numbers",m.isNumber,m.reprNumber);
m.registerRepr("boolean",m.isBoolean,m.reprNumber);
})();
dojo.provide("dojo.lang.*");
dojo.provide("dojo.widget.ToolbarContainer");
dojo.provide("dojo.widget.html.ToolbarContainer");
dojo.provide("dojo.widget.Toolbar");
dojo.provide("dojo.widget.html.Toolbar");
dojo.provide("dojo.widget.ToolbarItem");
dojo.provide("dojo.widget.html.ToolbarButtonGroup");
dojo.provide("dojo.widget.html.ToolbarButton");
dojo.provide("dojo.widget.html.ToolbarDialog");
dojo.provide("dojo.widget.html.ToolbarMenu");
dojo.provide("dojo.widget.html.ToolbarSeparator");
dojo.provide("dojo.widget.html.ToolbarSpace");
dojo.provide("dojo.widget.Icon");
dojo.widget.tags.addParseTreeHandler("dojo:toolbarContainer");
dojo.widget.html.ToolbarContainer=function(){
dojo.widget.HtmlWidget.call(this);
};
dojo.inherits(dojo.widget.html.ToolbarContainer,dojo.widget.HtmlWidget);
dojo.lang.extend(dojo.widget.html.ToolbarContainer,{widgetType:"ToolbarContainer",isContainer:true,templateString:"<div class=\"toolbarContainer\" dojoAttachPoint=\"containerNode\"></div>",templateCssPath:dojo.uri.dojoUri("src/widget/templates/HtmlToolbar.css"),getItem:function(name){
if(name instanceof dojo.widget.ToolbarItem){
return name;
}
for(var i=0;i<this.children.length;i++){
var _7b4=this.children[i];
if(_7b4 instanceof dojo.widget.html.Toolbar){
var item=_7b4.getItem(name);
if(item){
return item;
}
}
}
return null;
},getItems:function(){
var _7b6=[];
for(var i=0;i<this.children.length;i++){
var _7b8=this.children[i];
if(_7b8 instanceof dojo.widget.html.Toolbar){
_7b6=_7b6.concat(_7b8.getItems());
}
}
return _7b6;
},enable:function(){
for(var i=0;i<this.children.length;i++){
var _7ba=this.children[i];
if(_7ba instanceof dojo.widget.html.Toolbar){
_7ba.enable.apply(_7ba,arguments);
}
}
},disable:function(){
for(var i=0;i<this.children.length;i++){
var _7bc=this.children[i];
if(_7bc instanceof dojo.widget.html.Toolbar){
_7bc.disable.apply(_7bc,arguments);
}
}
},select:function(name){
for(var i=0;i<this.children.length;i++){
var _7bf=this.children[i];
if(_7bf instanceof dojo.widget.html.Toolbar){
_7bf.select(arguments);
}
}
},deselect:function(name){
for(var i=0;i<this.children.length;i++){
var _7c2=this.children[i];
if(_7c2 instanceof dojo.widget.html.Toolbar){
_7c2.deselect(arguments);
}
}
},getItemsState:function(){
var _7c3={};
for(var i=0;i<this.children.length;i++){
var _7c5=this.children[i];
if(_7c5 instanceof dojo.widget.html.Toolbar){
dojo.lang.mixin(_7c3,_7c5.getItemsState());
}
}
return _7c3;
},getItemsActiveState:function(){
var _7c6={};
for(var i=0;i<this.children.length;i++){
var _7c8=this.children[i];
if(_7c8 instanceof dojo.widget.html.Toolbar){
dojo.lang.mixin(_7c6,_7c8.getItemsActiveState());
}
}
return _7c6;
},getItemsSelectedState:function(){
var _7c9={};
for(var i=0;i<this.children.length;i++){
var _7cb=this.children[i];
if(_7cb instanceof dojo.widget.html.Toolbar){
dojo.lang.mixin(_7c9,_7cb.getItemsSelectedState());
}
}
return _7c9;
}});
dojo.widget.tags.addParseTreeHandler("dojo:toolbar");
dojo.widget.html.Toolbar=function(){
dojo.widget.HtmlWidget.call(this);
};
dojo.inherits(dojo.widget.html.Toolbar,dojo.widget.HtmlWidget);
dojo.lang.extend(dojo.widget.html.Toolbar,{widgetType:"Toolbar",isContainer:true,templateString:"<div class=\"toolbar\" dojoAttachPoint=\"containerNode\" unselectable=\"on\" dojoOnMouseover=\"_onmouseover\" dojoOnMouseout=\"_onmouseout\" dojoOnClick=\"_onclick\" dojoOnMousedown=\"_onmousedown\" dojoOnMouseup=\"_onmouseup\"></div>",_getItem:function(node){
var _7cd=new Date();
var _7ce=null;
while(node&&node!=this.domNode){
if(dojo.html.hasClass(node,"toolbarItem")){
var _7cf=dojo.widget.manager.getWidgetsByFilter(function(w){
return w.domNode==node;
});
if(_7cf.length==1){
_7ce=_7cf[0];
break;
}else{
if(_7cf.length>1){
dojo.raise("Toolbar._getItem: More than one widget matches the node");
}
}
}
node=node.parentNode;
}
return _7ce;
},_onmouseover:function(e){
var _7d2=this._getItem(e.target);
if(_7d2&&_7d2._onmouseover){
_7d2._onmouseover(e);
}
},_onmouseout:function(e){
var _7d4=this._getItem(e.target);
if(_7d4&&_7d4._onmouseout){
_7d4._onmouseout(e);
}
},_onclick:function(e){
var _7d6=this._getItem(e.target);
if(_7d6&&_7d6._onclick){
_7d6._onclick(e);
}
},_onmousedown:function(e){
var _7d8=this._getItem(e.target);
if(_7d8&&_7d8._onmousedown){
_7d8._onmousedown(e);
}
},_onmouseup:function(e){
var _7da=this._getItem(e.target);
if(_7da&&_7da._onmouseup){
_7da._onmouseup(e);
}
},addChild:function(item,pos,_7dd){
var _7de=dojo.widget.ToolbarItem.make(item,null,_7dd);
var ret=dojo.widget.html.Toolbar.superclass.addChild.call(this,_7de,null,pos,null);
return ret;
},push:function(){
for(var i=0;i<arguments.length;i++){
this.addChild(arguments[i]);
}
},getItem:function(name){
if(name instanceof dojo.widget.ToolbarItem){
return name;
}
for(var i=0;i<this.children.length;i++){
var _7e3=this.children[i];
if(_7e3 instanceof dojo.widget.ToolbarItem&&_7e3._name==name){
return _7e3;
}
}
return null;
},getItems:function(){
var _7e4=[];
for(var i=0;i<this.children.length;i++){
var _7e6=this.children[i];
if(_7e6 instanceof dojo.widget.ToolbarItem){
_7e4.push(_7e6);
}
}
return _7e4;
},getItemsState:function(){
var _7e7={};
for(var i=0;i<this.children.length;i++){
var _7e9=this.children[i];
if(_7e9 instanceof dojo.widget.ToolbarItem){
_7e7[_7e9._name]={selected:_7e9._selected,enabled:_7e9._enabled};
}
}
return _7e7;
},getItemsActiveState:function(){
var _7ea=this.getItemsState();
for(var item in _7ea){
_7ea[item]=_7ea[item].enabled;
}
return _7ea;
},getItemsSelectedState:function(){
var _7ec=this.getItemsState();
for(var item in _7ec){
_7ec[item]=_7ec[item].selected;
}
return _7ec;
},enable:function(){
var _7ee=arguments.length?arguments:this.children;
for(var i=0;i<_7ee.length;i++){
var _7f0=this.getItem(_7ee[i]);
if(_7f0 instanceof dojo.widget.ToolbarItem){
_7f0.enable(false,true);
}
}
},disable:function(){
var _7f1=arguments.length?arguments:this.children;
for(var i=0;i<_7f1.length;i++){
var _7f3=this.getItem(_7f1[i]);
if(_7f3 instanceof dojo.widget.ToolbarItem){
_7f3.disable();
}
}
},select:function(){
for(var i=0;i<arguments.length;i++){
var name=arguments[i];
var item=this.getItem(name);
if(item){
item.select();
}
}
},deselect:function(){
for(var i=0;i<arguments.length;i++){
var name=arguments[i];
var item=this.getItem(name);
if(item){
item.disable();
}
}
},setValue:function(){
for(var i=0;i<arguments.length;i+=2){
var name=arguments[i],_7fc=arguments[i+1];
var item=this.getItem(name);
if(item){
if(item instanceof dojo.widget.ToolbarItem){
item.setValue(_7fc);
}
}
}
}});
dojo.widget.ToolbarItem=function(){
dojo.widget.HtmlWidget.call(this);
};
dojo.inherits(dojo.widget.ToolbarItem,dojo.widget.HtmlWidget);
dojo.lang.extend(dojo.widget.ToolbarItem,{templateString:"<span unselectable=\"on\" class=\"toolbarItem\"></span>",_name:null,getName:function(){
return this._name;
},setName:function(_7fe){
return this._name=_7fe;
},getValue:function(){
return this.getName();
},setValue:function(_7ff){
return this.setName(_7ff);
},_selected:false,isSelected:function(){
return this._selected;
},setSelected:function(is,_801,_802){
if(!this._toggleItem&&!_801){
return;
}
is=Boolean(is);
if(_801||this._enabled&&this._selected!=is){
this._selected=is;
this.update();
if(!_802){
this._fireEvent(is?"onSelect":"onDeselect");
this._fireEvent("onChangeSelect");
}
}
},select:function(_803,_804){
return this.setSelected(true,_803,_804);
},deselect:function(_805,_806){
return this.setSelected(false,_805,_806);
},_toggleItem:false,isToggleItem:function(){
return this._toggleItem;
},setToggleItem:function(_807){
this._toggleItem=Boolean(_807);
},toggleSelected:function(_808){
return this.setSelected(!this._selected,_808);
},_enabled:true,isEnabled:function(){
return this._enabled;
},setEnabled:function(is,_80a,_80b){
is=Boolean(is);
if(_80a||this._enabled!=is){
this._enabled=is;
this.update();
if(!_80b){
this._fireEvent(this._enabled?"onEnable":"onDisable");
this._fireEvent("onChangeEnabled");
}
}
return this._enabled;
},enable:function(_80c,_80d){
return this.setEnabled(true,_80c,_80d);
},disable:function(_80e,_80f){
return this.setEnabled(false,_80e,_80f);
},toggleEnabled:function(_810,_811){
return this.setEnabled(!this._enabled,_810,_811);
},_icon:null,getIcon:function(){
return this._icon;
},setIcon:function(_812){
var icon=dojo.widget.Icon.make(_812);
if(this._icon){
this._icon.setIcon(icon);
}else{
this._icon=icon;
}
var _814=this._icon.getNode();
if(_814.parentNode!=this.domNode){
if(this.domNode.hasChildNodes()){
this.domNode.insertBefore(_814,this.domNode.firstChild);
}else{
this.domNode.appendChild(_814);
}
}
return this._icon;
},_label:"",getLabel:function(){
return this._label;
},setLabel:function(_815){
var ret=this._label=_815;
if(!this.labelNode){
this.labelNode=document.createElement("span");
this.domNode.appendChild(this.labelNode);
}
this.labelNode.innerHTML="";
this.labelNode.appendChild(document.createTextNode(this._label));
this.update();
return ret;
},update:function(){
if(this._enabled){
dojo.html.removeClass(this.domNode,"disabled");
if(this._selected){
dojo.html.addClass(this.domNode,"selected");
}else{
dojo.html.removeClass(this.domNode,"selected");
}
}else{
this._selected=false;
dojo.html.addClass(this.domNode,"disabled");
dojo.html.removeClass(this.domNode,"down");
dojo.html.removeClass(this.domNode,"hover");
}
this._updateIcon();
},_updateIcon:function(){
if(this._icon){
if(this._enabled){
if(this._cssHover){
this._icon.hover();
}else{
if(this._selected){
this._icon.select();
}else{
this._icon.enable();
}
}
}else{
this._icon.disable();
}
}
},_fireEvent:function(evt){
if(typeof this[evt]=="function"){
var args=[this];
for(var i=1;i<arguments.length;i++){
args.push(arguments[i]);
}
this[evt].apply(this,args);
}
},_onmouseover:function(e){
if(!this._enabled){
return;
}
dojo.html.addClass(this.domNode,"hover");
},_onmouseout:function(e){
dojo.html.removeClass(this.domNode,"hover");
dojo.html.removeClass(this.domNode,"down");
if(!this._selected){
dojo.html.removeClass(this.domNode,"selected");
}
},_onclick:function(e){
if(this._enabled&&!this._toggleItem){
this._fireEvent("onClick");
}
},_onmousedown:function(e){
if(e.preventDefault){
e.preventDefault();
}
if(!this._enabled){
return;
}
dojo.html.addClass(this.domNode,"down");
if(this._toggleItem){
if(this.parent.preventDeselect&&this._selected){
return;
}
this.toggleSelected();
}
},_onmouseup:function(e){
dojo.html.removeClass(this.domNode,"down");
},fillInTemplate:function(args,frag){
if(args.name){
this._name=args.name;
}
if(args.selected){
this.select();
}
if(args.disabled){
this.disable();
}
if(args.label){
this.setLabel(args.label);
}
if(args.icon){
this.setIcon(args.icon);
}
if(args.toggleitem||args.toggleItem){
this.setToggleItem(true);
}
}});
dojo.widget.ToolbarItem.make=function(wh,_822,_823){
var item=null;
if(wh instanceof Array){
item=dojo.widget.createWidget("ToolbarButtonGroup",_823);
item.setName(wh[0]);
for(var i=1;i<wh.length;i++){
item.addChild(wh[i]);
}
}else{
if(wh instanceof dojo.widget.ToolbarItem){
item=wh;
}else{
if(wh instanceof dojo.uri.Uri){
item=dojo.widget.createWidget("ToolbarButton",dojo.lang.mixin(_823||{},{icon:new dojo.widget.Icon(wh.toString())}));
}else{
if(_822){
item=dojo.widget.createWidget(wh,_823);
}else{
if(typeof wh=="string"||wh instanceof String){
switch(wh.charAt(0)){
case "|":
case "-":
case "/":
item=dojo.widget.createWidget("ToolbarSeparator",_823);
break;
case " ":
if(wh.length==1){
item=dojo.widget.createWidget("ToolbarSpace",_823);
}else{
item=dojo.widget.createWidget("ToolbarFlexibleSpace",_823);
}
break;
default:
if(/\.(gif|jpg|jpeg|png)$/i.test(wh)){
item=dojo.widget.createWidget("ToolbarButton",dojo.lang.mixin(_823||{},{icon:new dojo.widget.Icon(wh.toString())}));
}else{
item=dojo.widget.createWidget("ToolbarButton",dojo.lang.mixin(_823||{},{label:wh.toString()}));
}
}
}else{
if(wh&&wh.tagName&&/^img$/i.test(wh.tagName)){
item=dojo.widget.createWidget("ToolbarButton",dojo.lang.mixin(_823||{},{icon:wh}));
}else{
item=dojo.widget.createWidget("ToolbarButton",dojo.lang.mixin(_823||{},{label:wh.toString()}));
}
}
}
}
}
}
return item;
};
dojo.widget.tags.addParseTreeHandler("dojo:toolbarButtonGroup");
dojo.widget.html.ToolbarButtonGroup=function(){
dojo.widget.ToolbarItem.call(this);
};
dojo.inherits(dojo.widget.html.ToolbarButtonGroup,dojo.widget.ToolbarItem);
dojo.lang.extend(dojo.widget.html.ToolbarButtonGroup,{widgetType:"ToolbarButtonGroup",isContainer:true,templateString:"<span unselectable=\"on\" class=\"toolbarButtonGroup\" dojoAttachPoint=\"containerNode\"></span>",defaultButton:"",postCreate:function(){
for(var i=0;i<this.children.length;i++){
this._injectChild(this.children[i]);
}
},addChild:function(item,pos,_829){
var _82a=dojo.widget.ToolbarItem.make(item,null,dojo.lang.mixin(_829||{},{toggleItem:true}));
var ret=dojo.widget.html.ToolbarButtonGroup.superclass.addChild.call(this,_82a,null,pos,null);
this._injectChild(_82a);
return ret;
},_injectChild:function(_82c){
dojo.event.connect(_82c,"onSelect",this,"onChildSelected");
dojo.event.connect(_82c,"onDeselect",this,"onChildDeSelected");
if(_82c._name==this.defaultButton||(typeof this.defaultButton=="number"&&this.children.length-1==this.defaultButton)){
_82c.select(false,true);
}
},getItem:function(name){
if(name instanceof dojo.widget.ToolbarItem){
return name;
}
for(var i=0;i<this.children.length;i++){
var _82f=this.children[i];
if(_82f instanceof dojo.widget.ToolbarItem&&_82f._name==name){
return _82f;
}
}
return null;
},getItems:function(){
var _830=[];
for(var i=0;i<this.children.length;i++){
var _832=this.children[i];
if(_832 instanceof dojo.widget.ToolbarItem){
_830.push(_832);
}
}
return _830;
},onChildSelected:function(e){
this.select(e._name);
},onChildDeSelected:function(e){
this._fireEvent("onChangeSelect",this._value);
},enable:function(_835,_836){
for(var i=0;i<this.children.length;i++){
var _838=this.children[i];
if(_838 instanceof dojo.widget.ToolbarItem){
_838.enable(_835,_836);
if(_838._name==this._value){
_838.select(_835,_836);
}
}
}
},disable:function(_839,_83a){
for(var i=0;i<this.children.length;i++){
var _83c=this.children[i];
if(_83c instanceof dojo.widget.ToolbarItem){
_83c.disable(_839,_83a);
}
}
},_value:"",getValue:function(){
return this._value;
},select:function(name,_83e,_83f){
for(var i=0;i<this.children.length;i++){
var _841=this.children[i];
if(_841 instanceof dojo.widget.ToolbarItem){
if(_841._name==name){
_841.select(_83e,_83f);
this._value=name;
}else{
_841.deselect(true,true);
}
}
}
if(!_83f){
this._fireEvent("onSelect",this._value);
this._fireEvent("onChangeSelect",this._value);
}
},setValue:this.select,preventDeselect:false});
dojo.widget.tags.addParseTreeHandler("dojo:toolbarButton");
dojo.widget.html.ToolbarButton=function(){
dojo.widget.ToolbarItem.call(this);
};
dojo.inherits(dojo.widget.html.ToolbarButton,dojo.widget.ToolbarItem);
dojo.lang.extend(dojo.widget.html.ToolbarButton,{widgetType:"ToolbarButton",fillInTemplate:function(args,frag){
dojo.widget.html.ToolbarButton.superclass.fillInTemplate.call(this,args,frag);
dojo.html.addClass(this.domNode,"toolbarButton");
if(this._icon){
this.setIcon(this._icon);
}
if(this._label){
this.setLabel(this._label);
}
if(!this._name){
if(this._label){
this.setName(this._label);
}else{
if(this._icon){
var src=this._icon.getSrc("enabled").match(/[\/^]([^\.\/]+)\.(gif|jpg|jpeg|png)$/i);
if(src){
this.setName(src[1]);
}
}else{
this._name=this._widgetId;
}
}
}
}});
dojo.widget.tags.addParseTreeHandler("dojo:toolbarDialog");
dojo.widget.html.ToolbarDialog=function(){
dojo.widget.html.ToolbarButton.call(this);
};
dojo.inherits(dojo.widget.html.ToolbarDialog,dojo.widget.html.ToolbarButton);
dojo.lang.extend(dojo.widget.html.ToolbarDialog,{widgetType:"ToolbarDialog",fillInTemplate:function(args,frag){
dojo.widget.html.ToolbarDialog.superclass.fillInTemplate.call(this,args,frag);
dojo.event.connect(this,"onSelect",this,"showDialog");
dojo.event.connect(this,"onDeselect",this,"hideDialog");
},showDialog:function(e){
dojo.lang.setTimeout(dojo.event.connect,1,document,"onmousedown",this,"deselect");
},hideDialog:function(e){
dojo.event.disconnect(document,"onmousedown",this,"deselect");
}});
dojo.widget.tags.addParseTreeHandler("dojo:toolbarMenu");
dojo.widget.html.ToolbarMenu=function(){
dojo.widget.html.ToolbarDialog.call(this);
this.widgetType="ToolbarMenu";
};
dojo.inherits(dojo.widget.html.ToolbarMenu,dojo.widget.html.ToolbarDialog);
dojo.widget.ToolbarMenuItem=function(){
};
dojo.widget.tags.addParseTreeHandler("dojo:toolbarSeparator");
dojo.widget.html.ToolbarSeparator=function(){
dojo.widget.ToolbarItem.call(this);
};
dojo.inherits(dojo.widget.html.ToolbarSeparator,dojo.widget.ToolbarItem);
dojo.lang.extend(dojo.widget.html.ToolbarSeparator,{widgetType:"ToolbarSeparator",templateString:"<span unselectable=\"on\" class=\"toolbarItem toolbarSeparator\"></span>",defaultIconPath:new dojo.uri.dojoUri("src/widget/templates/buttons/-.gif"),fillInTemplate:function(args,frag,skip){
dojo.widget.html.ToolbarSeparator.superclass.fillInTemplate.call(this,args,frag);
this._name=this.widgetId;
if(!skip){
if(!this._icon){
this.setIcon(this.defaultIconPath);
}
this.domNode.appendChild(this._icon.getNode());
}
},_onmouseover:null,_onmouseout:null,_onclick:null,_onmousedown:null,_onmouseup:null});
dojo.widget.tags.addParseTreeHandler("dojo:toolbarSpace");
dojo.widget.html.ToolbarSpace=function(){
dojo.widget.html.ToolbarSeparator.call(this);
};
dojo.inherits(dojo.widget.html.ToolbarSpace,dojo.widget.html.ToolbarSeparator);
dojo.lang.extend(dojo.widget.html.ToolbarSpace,{widgetType:"ToolbarSpace",fillInTemplate:function(args,frag,skip){
dojo.widget.html.ToolbarSpace.superclass.fillInTemplate.call(this,args,frag,true);
if(!skip){
dojo.html.addClass(this.domNode,"toolbarSpace");
}
}});
dojo.widget.tags.addParseTreeHandler("dojo:toolbarSelect");
dojo.widget.html.ToolbarSelect=function(){
dojo.widget.ToolbarItem.call(this);
};
dojo.inherits(dojo.widget.html.ToolbarSelect,dojo.widget.ToolbarItem);
dojo.lang.extend(dojo.widget.html.ToolbarSelect,{widgetType:"ToolbarSelect",templateString:"<span class=\"toolbarItem toolbarSelect\" unselectable=\"on\"><select dojoAttachPoint=\"selectBox\" dojoOnChange=\"changed\"></select></span>",fillInTemplate:function(args,frag){
dojo.widget.html.ToolbarSelect.superclass.fillInTemplate.call(this,args,frag,true);
var keys=args.values;
var i=0;
for(var val in keys){
var opt=document.createElement("option");
opt.setAttribute("value",keys[val]);
opt.innerHTML=val;
this.selectBox.appendChild(opt);
}
},changed:function(e){
this._fireEvent("onSetValue",this.selectBox.value);
},setEnabled:function(is,_857,_858){
var ret=dojo.widget.html.ToolbarSelect.superclass.setEnabled.call(this,is,_857,_858);
this.selectBox.disabled=!this._enabled;
return ret;
},_onmouseover:null,_onmouseout:null,_onclick:null,_onmousedown:null,_onmouseup:null});
dojo.widget.Icon=function(_85a,_85b,_85c,_85d){
if(!arguments.length){
throw new Error("Icon must have at least an enabled state");
}
var _85e=["enabled","disabled","hover","selected"];
var _85f="enabled";
var _860=document.createElement("img");
this.getState=function(){
return _85f;
};
this.setState=function(_861){
if(dojo.lang.inArray(_861,_85e)){
if(this[_861]){
_85f=_861;
_860.setAttribute("src",this[_85f].src);
}
}else{
throw new Error("Invalid state set on Icon (state: "+_861+")");
}
};
this.setSrc=function(_862,_863){
if(/^img$/i.test(_863.tagName)){
this[_862]=_863;
}else{
if(typeof _863=="string"||_863 instanceof String||_863 instanceof dojo.uri.Uri){
this[_862]=new Image();
this[_862].src=_863.toString();
}
}
return this[_862];
};
this.setIcon=function(icon){
for(var i=0;i<_85e.length;i++){
if(icon[_85e[i]]){
this.setSrc(_85e[i],icon[_85e[i]]);
}
}
this.update();
};
this.enable=function(){
this.setState("enabled");
};
this.disable=function(){
this.setState("disabled");
};
this.hover=function(){
this.setState("hover");
};
this.select=function(){
this.setState("selected");
};
this.getSize=function(){
return {width:_860.width||_860.offsetWidth,height:_860.height||_860.offsetHeight};
};
this.setSize=function(w,h){
_860.width=w;
_860.height=h;
return {width:w,height:h};
};
this.getNode=function(){
return _860;
};
this.getSrc=function(_868){
if(_868){
return this[_868].src;
}
return _860.src||"";
};
this.update=function(){
this.setState(_85f);
};
for(var i=0;i<_85e.length;i++){
var arg=arguments[i];
var _86b=_85e[i];
this[_86b]=null;
if(!arg){
continue;
}
this.setSrc(_86b,arg);
}
this.enable();
};
dojo.widget.Icon.make=function(a,b,c,d){
for(var i=0;i<arguments.length;i++){
if(arguments[i] instanceof dojo.widget.Icon){
return arguments[i];
}
}
return new dojo.widget.Icon(a,b,c,d);
};
dojo.provide("dojo.widget.ColorPalette");
dojo.provide("dojo.widget.html.ColorPalette");
dojo.widget.tags.addParseTreeHandler("dojo:ToolbarColorDialog");
dojo.widget.html.ToolbarColorDialog=function(){
dojo.widget.html.ToolbarDialog.call(this);
};
dojo.inherits(dojo.widget.html.ToolbarColorDialog,dojo.widget.html.ToolbarDialog);
dojo.lang.extend(dojo.widget.html.ToolbarColorDialog,{widgetType:"ToolbarColorDialog",palette:"7x10",fillInTemplate:function(args,frag){
dojo.widget.html.ToolbarColorDialog.superclass.fillInTemplate.call(this,args,frag);
this.dialog=dojo.widget.createWidget("ColorPalette",{palette:this.palette});
this.dialog.domNode.style.position="absolute";
dojo.event.connect(this.dialog,"onColorSelect",this,"_setValue");
},_setValue:function(_873){
this._value=_873;
this._fireEvent("onSetValue",_873);
},showDialog:function(e){
dojo.widget.html.ToolbarColorDialog.superclass.showDialog.call(this,e);
var x=dojo.html.getAbsoluteX(this.domNode);
var y=dojo.html.getAbsoluteY(this.domNode)+dojo.html.getInnerHeight(this.domNode);
this.dialog.showAt(x,y);
},hideDialog:function(e){
dojo.widget.html.ToolbarColorDialog.superclass.hideDialog.call(this,e);
this.dialog.hide();
}});
dojo.widget.tags.addParseTreeHandler("dojo:colorpalette");
dojo.widget.html.ColorPalette=function(){
dojo.widget.HtmlWidget.call(this);
};
dojo.inherits(dojo.widget.html.ColorPalette,dojo.widget.HtmlWidget);
dojo.lang.extend(dojo.widget.html.ColorPalette,{widgetType:"colorpalette",palette:"7x10",bgIframe:null,palettes:{"7x10":[["fff","fcc","fc9","ff9","ffc","9f9","9ff","cff","ccf","fcf"],["ccc","f66","f96","ff6","ff3","6f9","3ff","6ff","99f","f9f"],["c0c0c0","f00","f90","fc6","ff0","3f3","6cc","3cf","66c","c6c"],["999","c00","f60","fc3","fc0","3c0","0cc","36f","63f","c3c"],["666","900","c60","c93","990","090","399","33f","60c","939"],["333","600","930","963","660","060","366","009","339","636"],["000","300","630","633","330","030","033","006","309","303"]],"3x4":[["ffffff","00ff00","008000","0000ff"],["c0c0c0","ffff00","ff00ff","000080"],["808080","ff0000","800080","000000"]]},buildRendering:function(){
this.domNode=document.createElement("table");
dojo.html.disableSelection(this.domNode);
dojo.event.connect(this.domNode,"onmousedown",function(e){
e.preventDefault();
});
with(this.domNode){
cellPadding="0";
cellSpacing="1";
border="1";
style.backgroundColor="white";
}
var _879=document.createElement("tbody");
this.domNode.appendChild(_879);
var _87a=this.palettes[this.palette];
for(var i=0;i<_87a.length;i++){
var tr=document.createElement("tr");
for(var j=0;j<_87a[i].length;j++){
if(_87a[i][j].length==3){
_87a[i][j]=_87a[i][j].replace(/(.)(.)(.)/,"$1$1$2$2$3$3");
}
var td=document.createElement("td");
with(td.style){
backgroundColor="#"+_87a[i][j];
border="1px solid gray";
width=height="15px";
fontSize="1px";
}
td.color="#"+_87a[i][j];
td.onmouseover=function(e){
this.style.borderColor="white";
};
td.onmouseout=function(e){
this.style.borderColor="gray";
};
dojo.event.connect(td,"onmousedown",this,"click");
td.innerHTML="&nbsp;";
tr.appendChild(td);
}
_879.appendChild(tr);
}
if(dojo.render.html.ie){
this.bgIframe=document.createElement("<iframe frameborder='0' src='javascript:void(0);'>");
with(this.bgIframe.style){
position="absolute";
left=top="0px";
display="none";
}
document.body.appendChild(this.bgIframe);
dojo.style.setOpacity(this.bgIframe,0);
}
},click:function(e){
this.onColorSelect(e.currentTarget.color);
e.currentTarget.style.borderColor="gray";
},onColorSelect:function(_882){
},hide:function(){
this.domNode.parentNode.removeChild(this.domNode);
if(this.bgIframe){
this.bgIframe.style.display="none";
}
},showAt:function(x,y){
with(this.domNode.style){
top=y+"px";
left=x+"px";
zIndex=999;
}
document.body.appendChild(this.domNode);
if(this.bgIframe){
with(this.bgIframe.style){
display="block";
top=y+"px";
left=x+"px";
zIndex=998;
width=dojo.html.getOuterWidth(this.domNode)+"px";
height=dojo.html.getOuterHeight(this.domNode)+"px";
}
}
}});
dojo.provide("dojo.widget.Editor2Toolbar");
dojo.provide("dojo.widget.html.Editor2Toolbar");
dojo.widget.defineWidget("dojo.widget.html.Editor2Toolbar",dojo.widget.HtmlWidget,{commandList:["bold","italic","underline","subscript","superscript","fontname","fontsize","forecolor","hilitecolor","justifycenter","justifyfull","justifyleft","justifyright","cut","copy","paste","delete","undo","redo","createlink","unlink","removeformat","inserthorizontalrule","insertimage","insertorderedlist","insertunorderedlist","indent","outdent","formatblock","strikethrough","inserthtml","blockdirltr","blockdirrtl","dirltr","dirrtl","inlinedirltr","inlinedirrtl","inserttable","insertcell","insertcol","insertrow","deletecells","deletecols","deleterows","mergecells","splitcell"],templatePath:dojo.uri.dojoUri("src/widget/templates/HtmlEditorToolbar.html"),templateCssPath:dojo.uri.dojoUri("src/widget/templates/HtmlEditorToolbar.css"),forecolorPalette:null,hilitecolorPalette:null,wikiwordButton:null,htmltoggleButton:null,insertimageButton:null,styleDropdownButton:null,styleDropdownContainer:null,copyButton:null,boldButton:null,italicButton:null,underlineButton:null,justifycenterButton:null,justifyleftButton:null,justifyfullButton:null,justifyrightButton:null,pasteButton:null,undoButton:null,redoButton:null,linkButton:null,insertunorderedlistButton:null,insertorderedlistButton:null,forecolorButton:null,forecolorDropDown:null,hilitecolorButton:null,hilitecolorDropDown:null,formatSelectBox:null,inserthorizontalruleButton:null,strikethroughButton:null,clickInterceptDiv:null,oneLineTr:null,buttonClick:function(e){
e.preventDefault();
},buttonMouseOver:function(e){
},buttonMouseOut:function(e){
},preventSelect:function(e){
if(dojo.render.html.safari){
e.preventDefault();
}
},wikiwordClick:function(){
},insertimageClick:function(){
},htmltoggleClick:function(){
},styleDropdownClick:function(){
dojo.debug("styleDropdownClick:",this.styleDropdownContainer);
dojo.style.toggleShowing(this.styleDropdownContainer);
},copyClick:function(){
this.exec("copy");
},boldClick:function(){
this.exec("bold");
},italicClick:function(){
this.exec("italic");
},underlineClick:function(){
this.exec("underline");
},justifyleftClick:function(){
this.exec("justifyleft");
},justifycenterClick:function(){
this.exec("justifycenter");
},justifyfullClick:function(){
this.exec("justifyfull");
},justifyrightClick:function(){
this.exec("justifyright");
},pasteClick:function(){
this.exec("paste");
},undoClick:function(){
this.exec("undo");
},redoClick:function(){
this.exec("redo");
},linkClick:function(){
},insertunorderedlistClick:function(){
this.exec("insertunorderedlist");
},insertorderedlistClick:function(){
this.exec("insertorderedlist");
},inserthorizontalruleClick:function(){
this.exec("inserthorizontalrule");
},strikethroughClick:function(){
this.exec("strikethrough");
},formatSelectClick:function(){
var sv=this.formatSelectBox.value.toLowerCase();
this.exec("formatblock",sv);
},normalTextClick:function(){
this.exec("formatblock","p");
},h1TextClick:function(){
this.exec("formatblock","h1");
},h2TextClick:function(){
this.exec("formatblock","h2");
},h3TextClick:function(){
this.exec("formatblock","h3");
},h4TextClick:function(){
this.exec("formatblock","h4");
},indentClick:function(){
this.exec("indent");
},outdentClick:function(){
this.exec("outdent");
},hideAllDropDowns:function(){
this.domNode.style.height="";
dojo.lang.forEach(dojo.widget.byType("Editor2Toolbar"),function(tb){
try{
dojo.style.hide(tb.forecolorDropDown);
dojo.style.hide(tb.hilitecolorDropDown);
dojo.style.hide(tb.styleDropdownContainer);
if(tb.clickInterceptDiv){
dojo.style.hide(tb.clickInterceptDiv);
}
}
catch(e){
}
if(dojo.render.html.ie){
try{
dojo.style.hide(tb.forecolorPalette.bgIframe);
}
catch(e){
}
try{
dojo.style.hide(tb.hilitecolorPalette.bgIframe);
}
catch(e){
}
}
});
},selectFormat:function(_88b){
dojo.lang.forEach(this.formatSelectBox.options,function(item){
if(item.value.toLowerCase()==_88b.toLowerCase()){
item.selected=true;
}
});
},forecolorClick:function(e){
this.colorClick(e,"forecolor");
},hilitecolorClick:function(e){
this.colorClick(e,"hilitecolor");
},colorClick:function(e,type){
var h=dojo.render.html;
this.hideAllDropDowns();
e.stopPropagation();
var dd=this[type+"DropDown"];
var pal=this[type+"Palette"];
dojo.style.toggleShowing(dd);
if(!pal){
pal=this[type+"Palette"]=dojo.widget.createWidget("ColorPalette",{},dd,"first");
var fcp=pal.domNode;
with(dd.style){
width=dojo.html.getOuterWidth(fcp)+"px";
height=dojo.html.getOuterHeight(fcp)+"px";
zIndex=1002;
position="absolute";
}
dojo.event.connect("after",pal,"onColorSelect",this,"exec",function(mi){
mi.args.unshift(type);
return mi.proceed();
});
dojo.event.connect("after",pal,"onColorSelect",dojo.style,"toggleShowing",this,function(mi){
mi.args.unshift(dd);
return mi.proceed();
});
var cid=this.clickInterceptDiv;
if(!cid){
cid=this.clickInterceptDiv=document.createElement("div");
document.body.appendChild(cid);
with(cid.style){
backgroundColor="transparent";
top=left="0px";
height=width="100%";
position="absolute";
border="none";
display="none";
zIndex=1001;
}
dojo.event.connect(cid,"onclick",function(){
cid.style.display="none";
});
}
dojo.event.connect(pal,"onColorSelect",function(){
cid.style.display="none";
});
dojo.event.kwConnect({srcObj:document.body,srcFunc:"onclick",targetObj:this,targetFunc:"hideAllDropDowns",once:true});
document.body.appendChild(dd);
}
dojo.style.toggleShowing(this.clickInterceptDiv);
var pos=dojo.style.abs(this[type+"Button"]);
dojo.html.placeOnScreenPoint(dd,pos.x,pos.y,0,false);
if(pal.bgIframe){
with(pal.bgIframe.style){
display="block";
left=dd.style.left;
top=dd.style.top;
width=dojo.style.getOuterWidth(dd)+"px";
height=dojo.style.getOuterHeight(dd)+"px";
}
}
},uninitialize:function(){
if(!dojo.render.html.ie){
dojo.event.kwDisconnect({srcObj:document.body,srcFunc:"onclick",targetObj:this,targetFunc:"hideAllDropDowns",once:true});
}
},exec:function(what,arg){
},hideUnusableButtons:function(obj){
var op=obj||dojo.widget.html.RichText.prototype;
dojo.lang.forEach(this.commandList,function(cmd){
if(this[cmd+"Button"]){
var cb=this[cmd+"Button"];
if(!op.queryCommandAvailable(cmd)){
cb.style.display="none";
cb.parentNode.style.display="none";
}
}
},this);
if(this.oneLineTr){
var _89f=false;
var _8a0=false;
var tds=this.oneLineTr.getElementsByTagName("td");
dojo.lang.forEach(tds,function(td){
if(td.getAttribute("isSpacer")){
if(td.style.display!="none"){
if(_89f){
td.style.display="none";
}
_89f=true;
}else{
_8a0=td;
_89f=true;
}
}else{
if(td.style.display!="none"){
_8a0=td;
_89f=false;
}
}
});
}
},highlightButton:function(name){
var bn=name+"Button";
if(this[bn]){
with(this[bn].style){
backgroundColor="White";
border="1px solid #aeaeab";
}
}
},unhighlightButton:function(name){
var bn=name+"Button";
if(this[bn]){
with(this[bn].style){
backgroundColor="";
border="";
}
}
}},"html",function(){
dojo.event.connect(this,"fillInTemplate",dojo.lang.hitch(this,function(){
if(dojo.render.html.ie){
this.domNode.style.zoom=1;
}
}));
});
dojo.provide("dojo.widget.Editor2");
dojo.provide("dojo.widget.html.Editor2");
dojo.widget.defineWidget("dojo.widget.html.Editor2",dojo.widget.html.RichText,{saveUrl:"",saveMethod:"post",saveArgName:"editorContent",closeOnSave:false,shareToolbar:false,toolbarAlwaysVisible:false,htmlEditing:false,_inHtmlMode:false,_htmlEditNode:null,commandList:dojo.widget.html.Editor2Toolbar.prototype.commandList,toolbarWidget:null,scrollInterval:null,editorOnLoad:function(){
var _8a7=dojo.widget.byType("Editor2Toolbar");
if((!_8a7.length)||(!this.shareToolbar)){
var _8a8={};
_8a8.templatePath=dojo.uri.dojoUri("src/widget/templates/HtmlEditorToolbarOneline.html");
this.toolbarWidget=dojo.widget.createWidget("Editor2Toolbar",_8a8,this.domNode,"before");
dojo.event.connect(this,"destroy",this.toolbarWidget,"destroy");
this.toolbarWidget.hideUnusableButtons(this);
if(this.object){
this.tbBgIframe=new dojo.html.BackgroundIframe(this.toolbarWidget.domNode);
this.tbBgIframe.iframe.style.height="30px";
}
if(this.toolbarAlwaysVisible){
var src=document["documentElement"]||window;
this.scrollInterval=setInterval(dojo.lang.hitch(this,"globalOnScrollHandler"),100);
dojo.event.connect("before",this,"destroyRendering",this,"unhookScroller");
}
}else{
this.toolbarWidget=_8a7[0];
}
dojo.event.topic.registerPublisher("Editor2.clobberFocus",this.editNode,"onfocus");
dojo.event.topic.subscribe("Editor2.clobberFocus",this,"setBlur");
dojo.event.connect(this.editNode,"onfocus",this,"setFocus");
dojo.event.connect(this.toolbarWidget.linkButton,"onclick",dojo.lang.hitch(this,function(){
var _8aa;
if(this.document.selection){
_8aa=this.document.selection.createRange().text;
}else{
if(dojo.render.html.mozilla){
_8aa=this.window.getSelection().toString();
}
}
if(_8aa.length){
this.toolbarWidget.exec("createlink",prompt("Please enter the URL of the link:","http://"));
}else{
alert("Please select text to link");
}
}));
var _8ab=dojo.lang.hitch(this,function(){
if(dojo.render.html.ie){
this.editNode.focus();
}else{
this.window.focus();
}
});
dojo.event.connect(this.toolbarWidget,"formatSelectClick",_8ab);
dojo.event.connect(this,"execCommand",_8ab);
if(this.htmlEditing){
var tb=this.toolbarWidget.htmltoggleButton;
if(tb){
tb.style.display="";
dojo.event.connect(this.toolbarWidget,"htmltoggleClick",this,"toggleHtmlEditing");
}
}
},toggleHtmlEditing:function(){
if(!this._inHtmlMode){
this._inHtmlMode=true;
this.toolbarWidget.highlightButton("htmltoggle");
if(!this._htmlEditNode){
this._htmlEditNode=document.createElement("textarea");
dojo.html.insertBefore(this._htmlEditNode,this.domNode);
}
this._htmlEditNode.style.display="";
this._htmlEditNode.style.width="100%";
this._htmlEditNode.style.height=dojo.style.getInnerHeight(this.editNode)+"px";
this._htmlEditNode.value=this.editNode.innerHTML;
this.domNode.style.display="none";
}else{
this._inHtmlMode=false;
this.domNode.style.display="";
this.toolbarWidget.unhighlightButton("htmltoggle");
dojo.lang.setTimeout(this,"replaceEditorContent",1,this._htmlEditNode.value);
this._htmlEditNode.style.display="none";
this.editNode.focus();
}
},setFocus:function(){
dojo.event.connect(this.toolbarWidget,"exec",this,"execCommand");
},setBlur:function(){
dojo.event.disconnect(this.toolbarWidget,"exec",this,"execCommand");
},_scrollSetUp:false,_fixEnabled:false,_scrollThreshold:false,_handleScroll:true,globalOnScrollHandler:function(){
var isIE=dojo.render.html.ie;
if(!this._handleScroll){
return;
}
var ds=dojo.style;
var tdn=this.toolbarWidget.domNode;
var db=document["body"];
var _8b1=ds.getOuterHeight(tdn);
if(!this._scrollSetUp){
this._scrollSetUp=true;
var _8b2=ds.getOuterWidth(this.domNode);
this._scrollThreshold=ds.abs(tdn,false).y;
if((isIE)&&(db)&&(ds.getStyle(db,"background-image")=="none")){
with(db.style){
backgroundImage="url("+dojo.uri.dojoUri("src/widget/templates/images/blank.gif")+")";
backgroundAttachment="fixed";
}
}
}
var _8b3=(window["pageYOffset"])?window["pageYOffset"]:(document["documentElement"]||document["body"]).scrollTop;
if(_8b3>this._scrollThreshold){
if(!this._fixEnabled){
this.domNode.style.marginTop=_8b1+"px";
if(isIE){
var cl=dojo.style.abs(tdn).x;
document.body.appendChild(tdn);
tdn.style.left=cl+dojo.style.getPixelValue(document.body,"margin-left")+"px";
dojo.html.addClass(tdn,"IEFixedToolbar");
if(this.object){
dojo.html.addClass(this.tbBgIframe,"IEFixedToolbar");
}
}else{
with(tdn.style){
position="fixed";
top="0px";
}
}
tdn.style.zIndex=1000;
this._fixEnabled=true;
}
if(!dojo.render.html.safari){
var _8b5=(this.height)?parseInt(this.height):((this.object)?dojo.style.getInnerHeight(this.editNode):this._lastHeight);
if(_8b3>(this._scrollThreshold+_8b5)){
tdn.style.display="none";
}else{
tdn.style.display="";
}
}
}else{
if(this._fixEnabled){
this.domNode.style.marginTop=null;
with(tdn.style){
position="";
top="";
zIndex="";
if(isIE){
marginTop="";
}
}
if(isIE){
dojo.html.removeClass(tdn,"IEFixedToolbar");
dojo.html.insertBefore(tdn,this._htmlEditNode||this.domNode);
}
this._fixEnabled=false;
}
}
},unhookScroller:function(){
this._handleScroll=false;
clearInterval(this.scrollInterval);
if(dojo.render.html.ie){
dojo.html.removeClass(this.toolbarWidget.domNode,"IEFixedToolbar");
}
},_updateToolbarLastRan:null,_updateToolbarTimer:null,_updateToolbarFrequency:500,updateToolbar:function(_8b6){
if((!this.isLoaded)||(!this.toolbarWidget)){
return;
}
var diff=new Date()-this._updateToolbarLastRan;
if((!_8b6)&&(this._updateToolbarLastRan)&&((diff<this._updateToolbarFrequency))){
clearTimeout(this._updateToolbarTimer);
var _8b8=this;
this._updateToolbarTimer=setTimeout(function(){
_8b8.updateToolbar();
},this._updateToolbarFrequency/2);
return;
}else{
this._updateToolbarLastRan=new Date();
}
dojo.lang.forEach(this.commandList,function(cmd){
if(cmd=="inserthtml"){
return;
}
try{
if(this.queryCommandEnabled(cmd)){
if(this.queryCommandState(cmd)){
this.toolbarWidget.highlightButton(cmd);
}else{
this.toolbarWidget.unhighlightButton(cmd);
}
}
}
catch(e){
}
},this);
var h=dojo.render.html;
if(h.safari){
return;
}
var _8bb=(h.ie)?this.document.selection.createRange().parentElement():this.window.getSelection().anchorNode;
while((_8bb)&&(_8bb.nodeType!=1)){
_8bb=_8bb.parentNode;
}
if(!_8bb){
return;
}
var _8bc=["p","pre","h1","h2","h3","h4"];
var type=_8bc[dojo.lang.find(_8bc,_8bb.nodeName.toLowerCase())];
while((_8bb)&&(_8bb!=this.editNode)&&(!type)){
_8bb=_8bb.parentNode;
type=_8bc[dojo.lang.find(_8bc,_8bb.nodeName.toLowerCase())];
}
if(!type){
type="";
}else{
if(type.charAt(0)=="h"){
this.toolbarWidget.unhighlightButton("bold");
}
}
this.toolbarWidget.selectFormat(type);
},updateItem:function(item){
try{
var cmd=item._name;
var _8c0=this._richText.queryCommandEnabled(cmd);
item.setEnabled(_8c0,false,true);
var _8c1=this._richText.queryCommandState(cmd);
if(_8c1&&cmd=="underline"){
_8c1=!this._richText.queryCommandEnabled("unlink");
}
item.setSelected(_8c1,false,true);
return true;
}
catch(err){
return false;
}
},_save:function(e){
if(!this.isClosed){
if(this.saveUrl.length){
var _8c3={};
_8c3[this.saveArgName]=this.getHtml();
dojo.io.bind({method:this.saveMethod,url:this.saveUrl,content:_8c3});
}else{
dojo.debug("please set a saveUrl for the editor");
}
if(this.closeOnSave){
this.close(e.getName().toLowerCase()=="save");
}
}
},wireUpOnLoad:function(){
if(!dojo.render.html.ie){
}
}},"html",function(){
var cp=dojo.widget.html.Editor2.prototype;
if(!cp._wrappersSet){
cp._wrappersSet=true;
cp.fillInTemplate=(function(fit){
return function(){
fit.call(this);
this.editorOnLoad();
};
})(cp.fillInTemplate);
cp.onDisplayChanged=(function(odc){
return function(){
try{
odc.call(this);
this.updateToolbar();
}
catch(e){
}
};
})(cp.onDisplayChanged);
cp.onLoad=(function(ol){
return function(){
ol.call(this);
this.wireUpOnLoad();
};
})(cp.onLoad);
}
});
dojo.provide("dojo.widget.Tooltip");
dojo.provide("dojo.widget.ContentPane");
dojo.provide("dojo.widget.html.ContentPane");
dojo.widget.html.ContentPane=function(){
this._onLoadStack=[];
this._onUnLoadStack=[];
dojo.widget.HtmlWidget.call(this);
};
dojo.inherits(dojo.widget.html.ContentPane,dojo.widget.HtmlWidget);
dojo.lang.extend(dojo.widget.html.ContentPane,{widgetType:"ContentPane",isContainer:true,adjustPaths:true,href:"",extractContent:true,parseContent:true,cacheContent:true,preload:false,refreshOnShow:false,handler:"",executeScripts:false,scriptScope:null,_remoteStyles:null,_callOnUnLoad:false,postCreate:function(args,frag,_8ca){
if(this.handler!=""){
this.setHandler(this.handler);
}
if(this.isShowing()||this.preload){
this.loadContents();
}
},show:function(){
if(this.refreshOnShow){
this.refresh();
}else{
this.loadContents();
}
dojo.widget.html.ContentPane.superclass.show.call(this);
},refresh:function(){
this.isLoaded=false;
this.loadContents();
},loadContents:function(){
if(this.isLoaded){
return;
}
this.isLoaded=true;
if(dojo.lang.isFunction(this.handler)){
this._runHandler();
}else{
if(this.href!=""){
this._downloadExternalContent(this.href,this.cacheContent);
}
}
},setUrl:function(url){
this.href=url;
this.isLoaded=false;
if(this.preload||this.isShowing()){
this.loadContents();
}
},_downloadExternalContent:function(url,_8cd){
this._handleDefaults("Loading...","onDownloadStart");
var self=this;
dojo.io.bind({url:url,useCache:_8cd,preventCache:!_8cd,mimetype:"text/html",handler:function(type,data,e){
if(type=="load"){
self.onDownloadEnd.call(self,url,data);
}else{
self._handleDefaults.call(self,"Error loading '"+url+"' ("+e.status+" "+e.statusText+")","onDownloadError");
self.onLoad();
}
}});
},onLoad:function(e){
this._runStack("_onLoadStack");
},onUnLoad:function(e){
this._runStack("_onUnLoadStack");
this.scriptScope=null;
},_runStack:function(_8d4){
var st=this[_8d4];
var err="";
for(var i=0;i<st.length;i++){
try{
st[i].call(this.scriptScope);
}
catch(e){
err+="\n"+st[i]+" failed: "+e.description;
}
}
this[_8d4]=[];
if(err.length){
var name=(_8d4=="_onLoadStack")?"addOnLoad":"addOnUnLoad";
this._handleDefaults(name+" failure\n "+err,"onExecError",true);
}
},addOnLoad:function(obj,func){
this._pushOnStack(this._onLoadStack,obj,func);
},addOnUnLoad:function(obj,func){
this._pushOnStack(this._onUnLoadStack,obj,func);
},_pushOnStack:function(_8dd,obj,func){
if(typeof func=="undefined"){
_8dd.push(obj);
}else{
_8dd.push(function(){
obj[func]();
});
}
},destroy:function(){
this.onUnLoad();
dojo.widget.html.ContentPane.superclass.destroy.call(this);
},onExecError:function(e){
},onContentError:function(e){
},onDownloadError:function(e){
},onDownloadStart:function(e){
},onDownloadEnd:function(url,data){
data=this.splitAndFixPaths(data,url);
this.setContent(data);
},_handleDefaults:function(e,_8e7,_8e8){
if(!_8e7){
_8e7="onContentError";
}
if(dojo.lang.isString(e)){
e={"text":e,"toString":function(){
return this.text;
}};
}
if(typeof e.returnValue!="boolean"){
e.returnValue=true;
}
if(typeof e.preventDefault!="function"){
e.preventDefault=function(){
this.returnValue=false;
};
}
this[_8e7](e);
if(e.returnValue){
if(_8e8){
alert(e.toString());
}else{
if(this._callOnUnLoad){
this.onUnLoad();
}
this._callOnUnLoad=false;
this._setContent(e.toString());
}
}
},splitAndFixPaths:function(s,url){
if(!url){
url="./";
}
if(!s){
return "";
}
var _8eb=[];
var _8ec=[];
var _8ed=[];
var _8ee=[];
var _8ef=[];
var _8f0=[];
var _8f1=[];
while(_8f1){
_8f1=s.match(/<title[^>]*>([\s\S]*?)<\/title>/i);
if(!_8f1){
break;
}
_8eb.push(_8f1[1]);
s=s.replace(/<title[^>]*>[\s\S]*?<\/title>/i,"");
}
var _8f1=[];
while(_8f1){
_8f1=s.match(/<style[^>]*>([\s\S]*?)<\/style>/i);
if(!_8f1){
break;
}
_8ee.push(dojo.style.fixPathsInCssText(_8f1[1],url));
s=s.replace(/<style[^>]*?>[\s\S]*?<\/style>/i,"");
}
var pos=0;
var pos2=0;
var stop=0;
var str="";
var _8f6="";
var attr=[];
var fix="";
var _8f9="";
var tag="";
var _8fb="";
while(pos>-1){
pos=s.search(/<[a-z][a-z0-9]*[^>]*\s(?:(?:src|href|style)=[^>])+[^>]*>/i);
if(pos==-1){
break;
}
str+=s.substring(0,pos);
s=s.substring(pos,s.length);
tag=s.match(/^<[a-z][a-z0-9]*[^>]*>/i)[0];
s=s.substring(tag.length,s.length);
pos2=0;
_8f9="";
fix="";
_8fb="";
var _8fc=0;
while(pos2!=-1){
_8f9+=tag.substring(0,pos2)+fix;
tag=tag.substring(pos2+_8fc,tag.length);
attr=tag.match(/ (src|href|style)=(['"]?)([\w()\[\]\/.,\\'"-:;#=&?\s@]+?)\2/i);
if(!attr){
break;
}
switch(attr[1].toLowerCase()){
case "src":
case "href":
if(attr[3].search(/^(?:[#]|(?:(?:https?|ftps?|file|javascript|mailto|news):))/)==-1){
_8f6=(new dojo.uri.Uri(url,attr[3]).toString());
}else{
pos2=pos2+attr[3].length;
continue;
}
break;
case "style":
_8f6=dojo.style.fixPathsInCssText(attr[3],url);
break;
default:
pos2=pos2+attr[3].length;
continue;
}
_8fb=" "+attr[1]+"="+attr[2]+attr[3]+attr[2];
_8fc=_8fb.length;
fix=" "+attr[1]+"="+attr[2]+_8f6+attr[2];
pos2=tag.search(new RegExp(dojo.string.escapeRegExp(_8fb)));
}
str+=_8f9+tag;
pos=0;
}
s=str+s;
_8f1=[];
var tmp=[];
while(_8f1){
_8f1=s.match(/<script([^>]*)>([\s\S]*?)<\/script>/i);
if(!_8f1){
break;
}
if(_8f1[1]){
attr=_8f1[1].match(/src=(['"]?)([^"']*)\1/i);
if(attr){
var tmp=attr[2].search(/.*(\bdojo\b(?:\.uncompressed)?\.js)$/);
if(tmp>-1){
dojo.debug("Security note! inhibit:"+attr[2]+" from  beeing loaded again.");
}else{
_8ef.push(attr[2]);
}
}
}
if(_8f1[2]){
var sc=_8f1[2].replace(/(?:var )?\bdjConfig\b(?:[\s]*=[\s]*\{[^}]+\}|\.[\w]*[\s]*=[\s]*[^;\n]*)?;?|dojo\.hostenv\.writeIncludes\(\s*\);?/g,"");
if(!sc){
continue;
}
tmp=[];
while(tmp&&_8f0.length<100){
tmp=sc.match(/dojo\.(?:(?:require(?:After)?(?:If)?)|(?:widget\.(?:manager\.)?registerWidgetPackage)|(?:(?:hostenv\.)?setModulePrefix))\((['"]).*?\1\)\s*;?/);
if(!tmp){
break;
}
_8f0.push(tmp[0]);
sc=sc.replace(tmp[0],"");
}
_8ec.push(sc);
}
s=s.replace(/<script[^>]*>[\s\S]*?<\/script>/i,"");
}
if(this.executeScripts){
var _8fb=/(<[a-zA-Z][a-zA-Z0-9]*\s[^>]*\S=(['"])[^>]*[^\.\]])scriptScope([^>]*>)/;
var pos=0;
var str="";
_8f1=[];
var cit="";
while(pos>-1){
pos=s.search(_8fb);
if(pos>-1){
cit=((RegExp.$2=="'")?"\"":"'");
str+=s.substring(0,pos);
s=s.substr(pos).replace(_8fb,"$1dojo.widget.byId("+cit+this.widgetId+cit+").scriptScope$3");
}
}
s=str+s;
}
_8f1=[];
while(_8f1){
_8f1=s.match(/<link ([^>]*rel=['"]?stylesheet['"]?[^>]*)>/i);
if(!_8f1){
break;
}
attr=_8f1[1].match(/href=(['"]?)([^'">]*)\1/i);
if(attr){
_8ed.push(attr[2]);
}
s=s.replace(new RegExp(_8f1[0]),"");
}
return {"xml":s,"styles":_8ee,"linkStyles":_8ed,"titles":_8eb,"requires":_8f0,"scripts":_8ec,"remoteScripts":_8ef,"url":url};
},_setContent:function(xml){
this.destroyChildren();
if(this._remoteStyles){
for(var i=0;i<this._remoteStyles.length;i++){
if(this._remoteStyles[i]&&this._remoteStyles.parentNode){
this._remoteStyles[i].parentNode.removeChild(this._remoteStyles[i]);
}
}
this._remoteStyles=null;
}
var node=this.containerNode||this.domNode;
try{
if(typeof xml!="string"){
node.innerHTML="";
node.appendChild(xml);
}else{
node.innerHTML=xml;
}
}
catch(e){
e="Could'nt load content:"+e;
this._handleDefaults(e,"onContentError");
}
},setContent:function(data){
if(this._callOnUnLoad){
this.onUnLoad();
}
this._callOnUnLoad=true;
if(!data||dojo.dom.isNode(data)){
this._setContent(data);
this.onResized();
this.onLoad();
}else{
if((!data.xml)&&(this.adjustPaths)){
data=this.splitAndFixPaths(data);
}
if(this.extractContent){
var _904=data.xml.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
if(_904){
data.xml=_904[1];
}
}
for(var i=0;i<data.styles.length;i++){
if(i==0){
this._remoteStyles=[];
}
this._remoteStyles.push(dojo.style.insertCssText(data.styles[i]));
}
for(var i=0;i<data.linkStyles.length;i++){
if(i==0){
this._remoteStyles=[];
}
this._remoteStyles.push(dojo.style.insertCssFile(data.linkStyles[i]));
}
this._setContent(data.xml);
if(this.parseContent){
for(var i=0;i<data.requires.length;i++){
try{
eval(data.requires[i]);
}
catch(e){
this._handleDefaults(e,"onContentError",true);
}
}
}
var _906=this;
function asyncParse(){
if(_906.executeScripts){
_906._executeScripts(data);
}
if(_906.parseContent){
var node=_906.containerNode||_906.domNode;
var _908=new dojo.xml.Parse();
var frag=_908.parseElement(node,null,true);
dojo.widget.getParser().createSubComponents(frag,_906);
}
_906.onResized();
_906.onLoad();
}
if(dojo.hostenv.isXDomain&&data.requires.length){
dojo.addOnLoad(asyncParse);
}else{
asyncParse();
}
}
},setHandler:function(_90a){
var fcn=dojo.lang.isFunction(_90a)?_90a:window[_90a];
if(!dojo.lang.isFunction(fcn)){
this._handleDefaults("Unable to set handler, '"+_90a+"' not a function.","onExecError",true);
return;
}
this.handler=function(){
return fcn.apply(this,arguments);
};
},_runHandler:function(){
if(dojo.lang.isFunction(this.handler)){
this.handler(this,this.domNode);
return false;
}
return true;
},_executeScripts:function(data){
var self=this;
for(var i=0;i<data.remoteScripts.length;i++){
dojo.io.bind({"url":data.remoteScripts[i],"useCash":this.cacheContent,"load":function(type,_910){
dojo.lang.hitch(self,data.scripts.push(_910));
},"error":function(type,_912){
self._handleDefaults.call(self,type+" downloading remote script","onExecError",true);
},"mimetype":"text/plain","sync":true});
}
var _913="";
for(var i=0;i<data.scripts.length;i++){
_913+=data.scripts[i];
}
try{
this.scriptScope=null;
this.scriptScope=new (new Function("_container_",_913+"; return this;"))(self);
}
catch(e){
this._handleDefaults("Error running scripts from content:\n"+e,"onExecError",true);
}
}});
dojo.widget.tags.addParseTreeHandler("dojo:ContentPane");
dojo.provide("dojo.widget.html.Tooltip");
dojo.widget.defineWidget("dojo.widget.html.Tooltip",dojo.widget.html.ContentPane,{widgetType:"Tooltip",isContainer:true,caption:"",showDelay:500,hideDelay:100,connectId:"",templatePath:dojo.uri.dojoUri("src/widget/templates/HtmlTooltipTemplate.html"),templateCssPath:dojo.uri.dojoUri("src/widget/templates/HtmlTooltipTemplate.css"),connectNode:null,state:"erased",fillInTemplate:function(args,frag){
if(this.caption!=""){
this.domNode.appendChild(document.createTextNode(this.caption));
}
this.connectNode=dojo.byId(this.connectId);
dojo.widget.html.Tooltip.superclass.fillInTemplate.call(this,args,frag);
},postCreate:function(args,frag){
document.body.appendChild(this.domNode);
dojo.event.connect(this.connectNode,"onmouseover",this,"onMouseOver");
dojo.widget.html.Tooltip.superclass.postCreate.call(this,args,frag);
},onMouseOver:function(e){
this.mouse={x:e.pageX,y:e.pageY};
if(!this.showTimer){
this.showTimer=setTimeout(dojo.lang.hitch(this,"show"),this.showDelay);
dojo.event.connect(document.documentElement,"onmousemove",this,"onMouseMove");
}
},onMouseMove:function(e){
this.mouse={x:e.pageX,y:e.pageY};
if(dojo.html.overElement(this.connectNode,e)||dojo.html.overElement(this.domNode,e)){
if(this.hideTimer){
clearTimeout(this.hideTimer);
delete this.hideTimer;
}
}else{
if(this.showTimer){
clearTimeout(this.showTimer);
delete this.showTimer;
}
if((this.state=="displaying"||this.state=="displayed")&&!this.hideTimer){
this.hideTimer=setTimeout(dojo.lang.hitch(this,"hide"),this.hideDelay);
}
}
},show:function(){
if(this.state=="erasing"){
this.displayScheduled=true;
return;
}
if(this.state=="displaying"||this.state=="displayed"){
return;
}
if(!this.bgIframe){
this.bgIframe=new dojo.html.BackgroundIframe(this.domNode);
}
this.position();
this.explodeSrc=[this.mouse.x,this.mouse.y];
this.state="displaying";
dojo.widget.html.Tooltip.superclass.show.call(this);
},onShow:function(){
dojo.widget.html.Tooltip.superclass.onShow.call(this);
this.state="displayed";
if(this.eraseScheduled){
this.hide();
this.eraseScheduled=false;
}
},hide:function(){
if(this.state=="displaying"){
this.eraseScheduled=true;
return;
}
if(this.state=="displayed"){
this.state="erasing";
if(this.showTimer){
clearTimeout(this.showTimer);
delete this.showTimer;
}
if(this.hideTimer){
clearTimeout(this.hideTimer);
delete this.hideTimer;
}
dojo.event.disconnect(document.documentElement,"onmousemove",this,"onMouseMove");
dojo.widget.html.Tooltip.superclass.hide.call(this);
}
},onHide:function(){
this.state="erased";
if(this.displayScheduled){
this.display();
this.displayScheduled=false;
}
},position:function(){
dojo.html.placeOnScreenPoint(this.domNode,this.mouse.x,this.mouse.y,[10,15],true);
this.bgIframe.onResized();
},onLoad:function(){
if(this.isShowing()){
dojo.lang.setTimeout(this,this.position,50);
dojo.widget.html.Tooltip.superclass.onLoad.apply(this,arguments);
}
},checkSize:function(){
}});
dojo.provide("dojo.widget.Dialog");
dojo.provide("dojo.widget.html.Dialog");
dojo.widget.defineWidget("dojo.widget.html.Dialog",dojo.widget.html.ContentPane,{templatePath:dojo.uri.dojoUri("src/widget/templates/HtmlDialog.html"),isContainer:true,_scrollConnected:false,focusElement:"",bg:null,bgColor:"black",bgOpacity:0.4,followScroll:true,_fromTrap:false,anim:null,blockDuration:0,lifetime:0,trapTabs:function(e){
if(e.target==this.tabStart){
if(this._fromTrap){
this._fromTrap=false;
}else{
this._fromTrap=true;
this.tabEnd.focus();
}
}else{
if(e.target==this.tabEnd){
if(this._fromTrap){
this._fromTrap=false;
}else{
this._fromTrap=true;
this.tabStart.focus();
}
}
}
},clearTrap:function(e){
var _91c=this;
setTimeout(function(){
_91c._fromTrap=false;
},100);
},postCreate:function(args,frag,_91f){
with(this.domNode.style){
position="absolute";
zIndex=999;
display="none";
overflow="visible";
}
var b=document.body;
b.appendChild(this.domNode);
this.bg=document.createElement("div");
this.bg.className="dialogUnderlay";
with(this.bg.style){
position="absolute";
left=top="0px";
zIndex=998;
display="none";
}
this.setBackgroundColor(this.bgColor);
b.appendChild(this.bg);
this.bgIframe=new dojo.html.BackgroundIframe(this.bg);
},setBackgroundColor:function(_921){
if(arguments.length>=3){
_921=new dojo.graphics.color.Color(arguments[0],arguments[1],arguments[2]);
}else{
_921=new dojo.graphics.color.Color(_921);
}
this.bg.style.backgroundColor=_921.toString();
return this.bgColor=_921;
},setBackgroundOpacity:function(op){
if(arguments.length==0){
op=this.bgOpacity;
}
dojo.style.setOpacity(this.bg,op);
try{
this.bgOpacity=dojo.style.getOpacity(this.bg);
}
catch(e){
this.bgOpacity=op;
}
return this.bgOpacity;
},sizeBackground:function(){
if(this.bgOpacity>0){
var h=Math.max(document.documentElement.scrollHeight||document.body.scrollHeight,dojo.html.getViewportHeight());
var w=dojo.html.getViewportWidth();
this.bg.style.width=w+"px";
this.bg.style.height=h+"px";
}
this.bgIframe.onResized();
},showBackground:function(){
this.sizeBackground();
if(this.bgOpacity>0){
this.bg.style.display="block";
}
},placeDialog:function(){
var _925=dojo.html.getScrollOffset();
var _926=dojo.html.getViewportSize();
var w=dojo.style.getOuterWidth(this.containerNode);
var h=dojo.style.getOuterHeight(this.containerNode);
var x=_925[0]+(_926[0]-w)/2;
var y=_925[1]+(_926[1]-h)/2;
with(this.domNode.style){
left=x+"px";
top=y+"px";
}
},show:function(){
this.setBackgroundOpacity();
this.showBackground();
dojo.widget.html.Dialog.superclass.show.call(this);
if(this.followScroll&&!this._scrollConnected){
this._scrollConnected=true;
dojo.event.connect(window,"onscroll",this,"onScroll");
}
if(this.lifetime){
this.timeRemaining=this.lifetime;
if(!this.blockDuration){
dojo.event.connect(this.bg,"onclick",this,"hide");
}else{
dojo.event.disconnect(this.bg,"onclick",this,"hide");
}
if(this.timerNode){
this.timerNode.innerHTML=Math.ceil(this.timeRemaining/1000);
}
if(this.blockDuration&&this.closeNode){
if(this.lifetime>this.blockDuration){
this.closeNode.style.visibility="hidden";
}else{
this.closeNode.style.display="none";
}
}
this.timer=setInterval(dojo.lang.hitch(this,"onTick"),100);
}
this.checkSize();
},onLoad:function(){
this.placeDialog();
},fillInTemplate:function(){
},hide:function(){
if(this.focusElement){
dojo.byId(this.focusElement).focus();
dojo.byId(this.focusElement).blur();
}
if(this.timer){
clearInterval(this.timer);
}
this.bg.style.display="none";
this.bg.style.width=this.bg.style.height="1px";
dojo.widget.html.Dialog.superclass.hide.call(this);
if(this._scrollConnected){
this._scrollConnected=false;
dojo.event.disconnect(window,"onscroll",this,"onScroll");
}
},setTimerNode:function(node){
this.timerNode=node;
},setCloseControl:function(node){
this.closeNode=node;
dojo.event.connect(node,"onclick",this,"hide");
},setShowControl:function(node){
dojo.event.connect(node,"onclick",this,"show");
},onTick:function(){
if(this.timer){
this.timeRemaining-=100;
if(this.lifetime-this.timeRemaining>=this.blockDuration){
dojo.event.connect(this.bg,"onclick",this,"hide");
if(this.closeNode){
this.closeNode.style.visibility="visible";
}
}
if(!this.timeRemaining){
clearInterval(this.timer);
this.hide();
}else{
if(this.timerNode){
this.timerNode.innerHTML=Math.ceil(this.timeRemaining/1000);
}
}
}
},onScroll:function(){
this.placeDialog();
this.domNode.style.display="block";
},checkSize:function(){
if(this.isShowing()){
this.sizeBackground();
this.placeDialog();
this.domNode.style.display="block";
this.onResized();
}
},killEvent:function(evt){
evt.preventDefault();
evt.stopPropagation();
}});
dojo.provide("dojo.widget.DropdownContainer");
dojo.widget.defineWidget("dojo.widget.DropdownContainer",dojo.widget.HtmlWidget,{initializer:function(){
},inputWidth:"7em",inputId:"",inputName:"",iconURL:dojo.uri.dojoUri("src/widget/templates/images/combo_box_arrow.png"),iconAlt:"",inputNode:null,buttonNode:null,containerNode:null,subWidgetNode:null,containerToggle:"plain",containerToggleDuration:150,containerAnimInProgress:false,templateString:"<div><span style=\"white-space:nowrap\"><input type=\"text\" value=\"\" style=\"vertical-align:middle;\" dojoAttachPoint=\"inputNode\" autocomplete=\"off\" /> <img src=\"${this.iconURL}\" alt=\"${this.iconAlt}\" dojoAttachPoint=\"buttonNode\" dojoAttachEvent=\"onclick: onIconClick;\" style=\"vertical-align:middle; cursor:pointer; cursor:hand;\" /></span><br /><div dojoAttachPoint=\"containerNode\" style=\"display:none;position:absolute;width:12em;background-color:#fff;\"></div></div>",templateCssPath:"",fillInTemplate:function(args,frag){
var _931=this.getFragNodeRef(frag);
this.containerNode.style.left="";
this.containerNode.style.top="";
if(this.inputId){
this.inputNode.id=this.inputId;
}
if(this.inputName){
this.inputNode.name=this.inputName;
}
this.inputNode.style.width=this.inputWidth;
dojo.event.connect(this.inputNode,"onchange",this,"onInputChange");
this.containerIframe=new dojo.html.BackgroundIframe(this.containerNode);
this.containerIframe.size([0,0,0,0]);
},postMixInProperties:function(args,frag,_934){
this.containerToggleObj=dojo.lfx.toggle[this.containerToggle.toLowerCase()]||dojo.lfx.toggle.plain;
dojo.widget.DropdownContainer.superclass.postMixInProperties.call(this,args,frag,_934);
},onIconClick:function(evt){
this.toggleContainerShow();
},toggleContainerShow:function(){
if(dojo.html.isShowing(this.containerNode)){
this.hideContainer();
}else{
this.showContainer();
}
},showContainer:function(){
this.containerAnimInProgress=true;
this.containerToggleObj.show(this.containerNode,this.containerToggleDuration,null,dojo.lang.hitch(this,this.onContainerShow),this.explodeSrc);
dojo.lang.setTimeout(this,this.sizeBackgroundIframe,this.containerToggleDuration);
},onContainerShow:function(){
this.containerAnimInProgress=false;
},hideContainer:function(){
this.containerAnimInProgress=true;
this.containerToggleObj.hide(this.containerNode,this.containerToggleDuration,null,dojo.lang.hitch(this,this.onContainerHide),this.explodeSrc);
dojo.lang.setTimeout(this,this.sizeBackgroundIframe,this.containerToggleDuration);
},onContainerHide:function(){
this.containerAnimInProgress=false;
},sizeBackgroundIframe:function(){
var w=dojo.style.getOuterWidth(this.containerNode);
var h=dojo.style.getOuterHeight(this.containerNode);
if(w==0||h==0){
dojo.lang.setTimeout(this,"sizeBackgroundIframe",100);
return;
}
if(dojo.html.isShowing(this.containerNode)){
this.containerIframe.size([0,0,w,h]);
}
},onInputChange:function(){
}},"html");
dojo.widget.tags.addParseTreeHandler("dojo:dropdowncontainer");
dojo.provide("dojo.date");
dojo.date.setDayOfYear=function(_938,_939){
_938.setMonth(0);
_938.setDate(_939);
return _938;
};
dojo.date.getDayOfYear=function(_93a){
var _93b=new Date(_93a.getFullYear(),0,1);
return Math.floor((_93a.getTime()-_93b.getTime())/86400000);
};
dojo.date.setWeekOfYear=function(_93c,week,_93e){
if(arguments.length==1){
_93e=0;
}
dojo.unimplemented("dojo.date.setWeekOfYear");
};
dojo.date.getWeekOfYear=function(_93f,_940){
if(arguments.length==1){
_940=0;
}
var _941=new Date(_93f.getFullYear(),0,1);
var day=_941.getDay();
_941.setDate(_941.getDate()-day+_940-(day>_940?7:0));
return Math.floor((_93f.getTime()-_941.getTime())/604800000);
};
dojo.date.setIsoWeekOfYear=function(_943,week,_945){
if(arguments.length==1){
_945=1;
}
dojo.unimplemented("dojo.date.setIsoWeekOfYear");
};
dojo.date.getIsoWeekOfYear=function(_946,_947){
if(arguments.length==1){
_947=1;
}
dojo.unimplemented("dojo.date.getIsoWeekOfYear");
};
dojo.date.setIso8601=function(_948,_949){
var _94a=(_949.indexOf("T")==-1)?_949.split(" "):_949.split("T");
dojo.date.setIso8601Date(_948,_94a[0]);
if(_94a.length==2){
dojo.date.setIso8601Time(_948,_94a[1]);
}
return _948;
};
dojo.date.fromIso8601=function(_94b){
return dojo.date.setIso8601(new Date(0,0),_94b);
};
dojo.date.setIso8601Date=function(_94c,_94d){
var _94e="^([0-9]{4})((-?([0-9]{2})(-?([0-9]{2}))?)|"+"(-?([0-9]{3}))|(-?W([0-9]{2})(-?([1-7]))?))?$";
var d=_94d.match(new RegExp(_94e));
if(!d){
dojo.debug("invalid date string: "+_94d);
return false;
}
var year=d[1];
var _951=d[4];
var date=d[6];
var _953=d[8];
var week=d[10];
var _955=(d[12])?d[12]:1;
_94c.setYear(year);
if(_953){
dojo.date.setDayOfYear(_94c,Number(_953));
}else{
if(week){
_94c.setMonth(0);
_94c.setDate(1);
var gd=_94c.getDay();
var day=(gd)?gd:7;
var _958=Number(_955)+(7*Number(week));
if(day<=4){
_94c.setDate(_958+1-day);
}else{
_94c.setDate(_958+8-day);
}
}else{
if(_951){
_94c.setDate(1);
_94c.setMonth(_951-1);
}
if(date){
_94c.setDate(date);
}
}
}
return _94c;
};
dojo.date.fromIso8601Date=function(_959){
return dojo.date.setIso8601Date(new Date(0,0),_959);
};
dojo.date.setIso8601Time=function(_95a,_95b){
var _95c="Z|(([-+])([0-9]{2})(:?([0-9]{2}))?)$";
var d=_95b.match(new RegExp(_95c));
var _95e=0;
if(d){
if(d[0]!="Z"){
_95e=(Number(d[3])*60)+Number(d[5]);
_95e*=((d[2]=="-")?1:-1);
}
_95e-=_95a.getTimezoneOffset();
_95b=_95b.substr(0,_95b.length-d[0].length);
}
var _95f="^([0-9]{2})(:?([0-9]{2})(:?([0-9]{2})(.([0-9]+))?)?)?$";
var d=_95b.match(new RegExp(_95f));
if(!d){
dojo.debug("invalid time string: "+_95b);
return false;
}
var _960=d[1];
var mins=Number((d[3])?d[3]:0);
var secs=(d[5])?d[5]:0;
var ms=d[7]?(Number("0."+d[7])*1000):0;
_95a.setHours(_960);
_95a.setMinutes(mins);
_95a.setSeconds(secs);
_95a.setMilliseconds(ms);
return _95a;
};
dojo.date.fromIso8601Time=function(_964){
return dojo.date.setIso8601Time(new Date(0,0),_964);
};
dojo.date.shortTimezones=["IDLW","BET","HST","MART","AKST","PST","MST","CST","EST","AST","NFT","BST","FST","AT","GMT","CET","EET","MSK","IRT","GST","AFT","AGTT","IST","NPT","ALMT","MMT","JT","AWST","JST","ACST","AEST","LHST","VUT","NFT","NZT","CHAST","PHOT","LINT"];
dojo.date.timezoneOffsets=[-720,-660,-600,-570,-540,-480,-420,-360,-300,-240,-210,-180,-120,-60,0,60,120,180,210,240,270,300,330,345,360,390,420,480,540,570,600,630,660,690,720,765,780,840];
dojo.date.months=["January","February","March","April","May","June","July","August","September","October","November","December"];
dojo.date.shortMonths=["Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"];
dojo.date.days=["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];
dojo.date.shortDays=["Sun","Mon","Tues","Wed","Thur","Fri","Sat"];
dojo.date.getDaysInMonth=function(_965){
var _966=_965.getMonth();
var days=[31,28,31,30,31,30,31,31,30,31,30,31];
if(_966==1&&dojo.date.isLeapYear(_965)){
return 29;
}else{
return days[_966];
}
};
dojo.date.isLeapYear=function(_968){
var year=_968.getFullYear();
return (year%400==0)?true:(year%100==0)?false:(year%4==0)?true:false;
};
dojo.date.getDayName=function(_96a){
return dojo.date.days[_96a.getDay()];
};
dojo.date.getDayShortName=function(_96b){
return dojo.date.shortDays[_96b.getDay()];
};
dojo.date.getMonthName=function(_96c){
return dojo.date.months[_96c.getMonth()];
};
dojo.date.getMonthShortName=function(_96d){
return dojo.date.shortMonths[_96d.getMonth()];
};
dojo.date.getTimezoneName=function(_96e){
var _96f=-(_96e.getTimezoneOffset());
for(var i=0;i<dojo.date.timezoneOffsets.length;i++){
if(dojo.date.timezoneOffsets[i]==_96f){
return dojo.date.shortTimezones[i];
}
}
function $(s){
s=String(s);
while(s.length<2){
s="0"+s;
}
return s;
}
return (_96f<0?"-":"+")+$(Math.floor(Math.abs(_96f)/60))+":"+$(Math.abs(_96f)%60);
};
dojo.date.getOrdinal=function(_972){
var date=_972.getDate();
if(date%100!=11&&date%10==1){
return "st";
}else{
if(date%100!=12&&date%10==2){
return "nd";
}else{
if(date%100!=13&&date%10==3){
return "rd";
}else{
return "th";
}
}
}
};
dojo.date.format=dojo.date.strftime=function(_974,_975){
var _976=null;
function _(s,n){
s=String(s);
n=(n||2)-s.length;
while(n-->0){
s=(_976==null?"0":_976)+s;
}
return s;
}
function $(_979){
switch(_979){
case "a":
return dojo.date.getDayShortName(_974);
break;
case "A":
return dojo.date.getDayName(_974);
break;
case "b":
case "h":
return dojo.date.getMonthShortName(_974);
break;
case "B":
return dojo.date.getMonthName(_974);
break;
case "c":
return _974.toLocaleString();
break;
case "C":
return _(Math.floor(_974.getFullYear()/100));
break;
case "d":
return _(_974.getDate());
break;
case "D":
return $("m")+"/"+$("d")+"/"+$("y");
break;
case "e":
if(_976==null){
_976=" ";
}
return _(_974.getDate(),2);
break;
case "g":
break;
case "G":
break;
case "F":
return $("Y")+"-"+$("m")+"-"+$("d");
break;
case "H":
return _(_974.getHours());
break;
case "I":
return _(_974.getHours()%12||12);
break;
case "j":
return _(dojo.date.getDayOfYear(_974),3);
break;
case "m":
return _(_974.getMonth()+1);
break;
case "M":
return _(_974.getMinutes());
break;
case "n":
return "\n";
break;
case "p":
return _974.getHours()<12?"am":"pm";
break;
case "r":
return $("I")+":"+$("M")+":"+$("S")+" "+$("p");
break;
case "R":
return $("H")+":"+$("M");
break;
case "S":
return _(_974.getSeconds());
break;
case "t":
return "\t";
break;
case "T":
return $("H")+":"+$("M")+":"+$("S");
break;
case "u":
return String(_974.getDay()||7);
break;
case "U":
return _(dojo.date.getWeekOfYear(_974));
break;
case "V":
return _(dojo.date.getIsoWeekOfYear(_974));
break;
case "W":
return _(dojo.date.getWeekOfYear(_974,1));
break;
case "w":
return String(_974.getDay());
break;
case "x":
break;
case "X":
break;
case "y":
return _(_974.getFullYear()%100);
break;
case "Y":
return String(_974.getFullYear());
break;
case "z":
var _97a=_974.getTimezoneOffset();
return (_97a<0?"-":"+")+_(Math.floor(Math.abs(_97a)/60))+":"+_(Math.abs(_97a)%60);
break;
case "Z":
return dojo.date.getTimezoneName(_974);
break;
case "%":
return "%";
break;
}
}
var _97b="";
var i=0,_97d=0,_97e;
while((_97d=_975.indexOf("%",i))!=-1){
_97b+=_975.substring(i,_97d++);
switch(_975.charAt(_97d++)){
case "_":
_976=" ";
break;
case "-":
_976="";
break;
case "0":
_976="0";
break;
case "^":
_97e="upper";
break;
case "#":
_97e="swap";
break;
default:
_976=null;
_97d--;
break;
}
var _97f=$(_975.charAt(_97d++));
if(_97e=="upper"||(_97e=="swap"&&/[a-z]/.test(_97f))){
_97f=_97f.toUpperCase();
}else{
if(_97e=="swap"&&!/[a-z]/.test(_97f)){
_97f=_97f.toLowerCase();
}
}
var _980=null;
_97b+=_97f;
i=_97d;
}
_97b+=_975.substring(i);
return _97b;
};
dojo.date.compareTypes={DATE:1,TIME:2};
dojo.date.compare=function(_981,_982,_983){
var dA=_981;
var dB=_982||new Date();
var now=new Date();
var opt=_983||(dojo.date.compareTypes.DATE|dojo.date.compareTypes.TIME);
var d1=new Date(((opt&dojo.date.compareTypes.DATE)?(dA.getFullYear()):now.getFullYear()),((opt&dojo.date.compareTypes.DATE)?(dA.getMonth()):now.getMonth()),((opt&dojo.date.compareTypes.DATE)?(dA.getDate()):now.getDate()),((opt&dojo.date.compareTypes.TIME)?(dA.getHours()):0),((opt&dojo.date.compareTypes.TIME)?(dA.getMinutes()):0),((opt&dojo.date.compareTypes.TIME)?(dA.getSeconds()):0));
var d2=new Date(((opt&dojo.date.compareTypes.DATE)?(dB.getFullYear()):now.getFullYear()),((opt&dojo.date.compareTypes.DATE)?(dB.getMonth()):now.getMonth()),((opt&dojo.date.compareTypes.DATE)?(dB.getDate()):now.getDate()),((opt&dojo.date.compareTypes.TIME)?(dB.getHours()):0),((opt&dojo.date.compareTypes.TIME)?(dB.getMinutes()):0),((opt&dojo.date.compareTypes.TIME)?(dB.getSeconds()):0));
if(d1.valueOf()>d2.valueOf()){
return 1;
}
if(d1.valueOf()<d2.valueOf()){
return -1;
}
return 0;
};
dojo.date.dateParts={YEAR:0,MONTH:1,DAY:2,HOUR:3,MINUTE:4,SECOND:5,MILLISECOND:6};
dojo.date.add=function(d,unit,_98c){
var n=(_98c)?_98c:1;
var v;
switch(unit){
case dojo.date.dateParts.YEAR:
v=new Date(d.getFullYear()+n,d.getMonth(),d.getDate(),d.getHours(),d.getMinutes(),d.getSeconds(),d.getMilliseconds());
break;
case dojo.date.dateParts.MONTH:
v=new Date(d.getFullYear(),d.getMonth()+n,d.getDate(),d.getHours(),d.getMinutes(),d.getSeconds(),d.getMilliseconds());
break;
case dojo.date.dateParts.HOUR:
v=new Date(d.getFullYear(),d.getMonth(),d.getDate(),d.getHours()+n,d.getMinutes(),d.getSeconds(),d.getMilliseconds());
break;
case dojo.date.dateParts.MINUTE:
v=new Date(d.getFullYear(),d.getMonth(),d.getDate(),d.getHours(),d.getMinutes()+n,d.getSeconds(),d.getMilliseconds());
break;
case dojo.date.dateParts.SECOND:
v=new Date(d.getFullYear(),d.getMonth(),d.getDate(),d.getHours(),d.getMinutes(),d.getSeconds()+n,d.getMilliseconds());
break;
case dojo.date.dateParts.MILLISECOND:
v=new Date(d.getFullYear(),d.getMonth(),d.getDate(),d.getHours(),d.getMinutes(),d.getSeconds(),d.getMilliseconds()+n);
break;
default:
v=new Date(d.getFullYear(),d.getMonth(),d.getDate()+n,d.getHours(),d.getMinutes(),d.getSeconds(),d.getMilliseconds());
}
return v;
};
dojo.date.toString=function(date,_990){
dojo.deprecated("dojo.date.toString","use dojo.date.format instead","0.4");
if(_990.indexOf("#d")>-1){
_990=_990.replace(/#dddd/g,dojo.date.getDayOfWeekName(date));
_990=_990.replace(/#ddd/g,dojo.date.getShortDayOfWeekName(date));
_990=_990.replace(/#dd/g,(date.getDate().toString().length==1?"0":"")+date.getDate());
_990=_990.replace(/#d/g,date.getDate());
}
if(_990.indexOf("#M")>-1){
_990=_990.replace(/#MMMM/g,dojo.date.getMonthName(date));
_990=_990.replace(/#MMM/g,dojo.date.getShortMonthName(date));
_990=_990.replace(/#MM/g,((date.getMonth()+1).toString().length==1?"0":"")+(date.getMonth()+1));
_990=_990.replace(/#M/g,date.getMonth()+1);
}
if(_990.indexOf("#y")>-1){
var _991=date.getFullYear().toString();
_990=_990.replace(/#yyyy/g,_991);
_990=_990.replace(/#yy/g,_991.substring(2));
_990=_990.replace(/#y/g,_991.substring(3));
}
if(_990.indexOf("#")==-1){
return _990;
}
if(_990.indexOf("#h")>-1){
var _992=date.getHours();
_992=(_992>12?_992-12:(_992==0)?12:_992);
_990=_990.replace(/#hh/g,(_992.toString().length==1?"0":"")+_992);
_990=_990.replace(/#h/g,_992);
}
if(_990.indexOf("#H")>-1){
_990=_990.replace(/#HH/g,(date.getHours().toString().length==1?"0":"")+date.getHours());
_990=_990.replace(/#H/g,date.getHours());
}
if(_990.indexOf("#m")>-1){
_990=_990.replace(/#mm/g,(date.getMinutes().toString().length==1?"0":"")+date.getMinutes());
_990=_990.replace(/#m/g,date.getMinutes());
}
if(_990.indexOf("#s")>-1){
_990=_990.replace(/#ss/g,(date.getSeconds().toString().length==1?"0":"")+date.getSeconds());
_990=_990.replace(/#s/g,date.getSeconds());
}
if(_990.indexOf("#T")>-1){
_990=_990.replace(/#TT/g,date.getHours()>=12?"PM":"AM");
_990=_990.replace(/#T/g,date.getHours()>=12?"P":"A");
}
if(_990.indexOf("#t")>-1){
_990=_990.replace(/#tt/g,date.getHours()>=12?"pm":"am");
_990=_990.replace(/#t/g,date.getHours()>=12?"p":"a");
}
return _990;
};
dojo.date.daysInMonth=function(_993,year){
dojo.deprecated("daysInMonth(month, year)","replaced by getDaysInMonth(dateObject)","0.4");
return dojo.date.getDaysInMonth(new Date(year,_993,1));
};
dojo.date.toLongDateString=function(date){
dojo.deprecated("dojo.date.toLongDateString","use dojo.date.format(date, \"%B %e, %Y\") instead","0.4");
return dojo.date.format(date,"%B %e, %Y");
};
dojo.date.toShortDateString=function(date){
dojo.deprecated("dojo.date.toShortDateString","use dojo.date.format(date, \"%b %e, %Y\") instead","0.4");
return dojo.date.format(date,"%b %e, %Y");
};
dojo.date.toMilitaryTimeString=function(date){
dojo.deprecated("dojo.date.toMilitaryTimeString","use dojo.date.format(date, \"%T\")","0.4");
return dojo.date.format(date,"%T");
};
dojo.date.toRelativeString=function(date){
var now=new Date();
var diff=(now-date)/1000;
var end=" ago";
var _99c=false;
if(diff<0){
_99c=true;
end=" from now";
diff=-diff;
}
if(diff<60){
diff=Math.round(diff);
return diff+" second"+(diff==1?"":"s")+end;
}else{
if(diff<3600){
diff=Math.round(diff/60);
return diff+" minute"+(diff==1?"":"s")+end;
}else{
if(diff<3600*24&&date.getDay()==now.getDay()){
diff=Math.round(diff/3600);
return diff+" hour"+(diff==1?"":"s")+end;
}else{
if(diff<3600*24*7){
diff=Math.round(diff/(3600*24));
if(diff==1){
return _99c?"Tomorrow":"Yesterday";
}else{
return diff+" days"+end;
}
}else{
return dojo.date.toShortDateString(date);
}
}
}
}
};
dojo.date.getDayOfWeekName=function(date){
dojo.deprecated("dojo.date.getDayOfWeekName","use dojo.date.getDayName instead","0.4");
return dojo.date.days[date.getDay()];
};
dojo.date.getShortDayOfWeekName=function(date){
dojo.deprecated("dojo.date.getShortDayOfWeekName","use dojo.date.getDayShortName instead","0.4");
return dojo.date.shortDays[date.getDay()];
};
dojo.date.getShortMonthName=function(date){
dojo.deprecated("dojo.date.getShortMonthName","use dojo.date.getMonthShortName instead","0.4");
return dojo.date.shortMonths[date.getMonth()];
};
dojo.date.toSql=function(date,_9a1){
return dojo.date.format(date,"%F"+!_9a1?" %T":"");
};
dojo.date.fromSql=function(_9a2){
var _9a3=_9a2.split(/[\- :]/g);
while(_9a3.length<6){
_9a3.push(0);
}
return new Date(_9a3[0],(parseInt(_9a3[1],10)-1),_9a3[2],_9a3[3],_9a3[4],_9a3[5]);
};
dojo.provide("dojo.widget.DatePicker");
dojo.provide("dojo.widget.DatePicker.util");
dojo.widget.DatePicker=function(){
this.months=dojo.date.months,this.weekdays=dojo.date.days,this.toRfcDate=dojo.widget.DatePicker.util.toRfcDate,this.fromRfcDate=dojo.widget.DatePicker.util.fromRfcDate,this.initFirstSaturday=dojo.widget.DatePicker.util.initFirstSaturday;
};
dojo.widget.DatePicker.util=new function(){
this.months=dojo.date.months;
this.weekdays=dojo.date.days;
this.toRfcDate=function(_9a4){
if(!_9a4){
var _9a4=new Date();
}
return dojo.date.format(_9a4,"%Y-%m-%d");
};
this.fromRfcDate=function(_9a5){
if(_9a5.indexOf("Tany")!=-1){
_9a5=_9a5.replace("Tany","");
}
var _9a6=new Date();
dojo.date.setIso8601(_9a6,_9a5);
return _9a6;
};
this.initFirstSaturday=function(_9a7,year){
if(!_9a7){
_9a7=this.date.getMonth();
}
if(!year){
year=this.date.getFullYear();
}
var _9a9=new Date(year,_9a7,1);
return {year:year,month:_9a7,date:7-_9a9.getDay()};
};
};
dojo.provide("dojo.widget.html.DatePicker");
dojo.widget.defineWidget("dojo.widget.html.DatePicker",dojo.widget.HtmlWidget,{classConstructor:function(){
dojo.widget.DatePicker.call(this);
this.today="";
this.date="";
this.storedDate="";
this.currentDate={};
this.firstSaturday={};
},classNames:{previous:"previousMonth",current:"currentMonth",next:"nextMonth",currentDate:"currentDate",selectedDate:"selectedItem"},templatePath:dojo.uri.dojoUri("src/widget/templates/HtmlDatePicker.html"),templateCssPath:dojo.uri.dojoUri("src/widget/templates/HtmlDatePicker.css"),fillInTemplate:function(){
dojo.widget.DatePicker.call(this);
this.initData();
this.initUI();
},initData:function(){
this.today=new Date();
if(this.storedDate&&(this.storedDate.split("-").length>2)){
this.date=dojo.widget.DatePicker.util.fromRfcDate(this.storedDate);
}else{
this.date=this.today;
}
this.today.setHours(0);
this.date.setHours(0);
var _9aa=this.date.getMonth();
var _9ab=dojo.widget.DatePicker.util.initFirstSaturday(this.date.getMonth().toString(),this.date.getFullYear());
this.firstSaturday.year=_9ab.year;
this.firstSaturday.month=_9ab.month;
this.firstSaturday.date=_9ab.date;
},setDate:function(_9ac){
this.storedDate=_9ac;
},initUI:function(){
this.selectedIsUsed=false;
this.currentIsUsed=false;
var _9ad="";
var _9ae=new Date();
var _9af=this.calendarDatesContainerNode.getElementsByTagName("td");
var _9b0;
_9ae.setHours(8);
var _9b1=new Date(this.firstSaturday.year,this.firstSaturday.month,this.firstSaturday.date,8);
if(this.firstSaturday.date<7){
var _9b2=6;
for(var i=this.firstSaturday.date;i>0;i--){
_9b0=_9af.item(_9b2);
_9b0.innerHTML=_9b1.getDate();
dojo.html.setClass(_9b0,this.getDateClassName(_9b1,"current"));
_9b2--;
_9ae=_9b1;
_9b1=this.incrementDate(_9b1,false);
}
for(var i=_9b2;i>-1;i--){
_9b0=_9af.item(i);
_9b0.innerHTML=_9b1.getDate();
dojo.html.setClass(_9b0,this.getDateClassName(_9b1,"previous"));
_9ae=_9b1;
_9b1=this.incrementDate(_9b1,false);
}
}else{
_9b1.setDate(this.firstSaturday.date-6);
for(var i=0;i<7;i++){
_9b0=_9af.item(i);
_9b0.innerHTML=_9b1.getDate();
dojo.html.setClass(_9b0,this.getDateClassName(_9b1,"current"));
_9ae=_9b1;
_9b1=this.incrementDate(_9b1,true);
}
}
_9ae.setDate(this.firstSaturday.date);
_9ae.setMonth(this.firstSaturday.month);
_9ae.setFullYear(this.firstSaturday.year);
_9b1=this.incrementDate(_9ae,true);
var _9b4=7;
_9b0=_9af.item(_9b4);
while((_9b1.getMonth()==_9ae.getMonth())&&(_9b4<42)){
_9b0.innerHTML=_9b1.getDate();
dojo.html.setClass(_9b0,this.getDateClassName(_9b1,"current"));
_9b0=_9af.item(++_9b4);
_9ae=_9b1;
_9b1=this.incrementDate(_9b1,true);
}
while(_9b4<42){
_9b0.innerHTML=_9b1.getDate();
dojo.html.setClass(_9b0,this.getDateClassName(_9b1,"next"));
_9b0=_9af.item(++_9b4);
_9ae=_9b1;
_9b1=this.incrementDate(_9b1,true);
}
this.setMonthLabel(this.firstSaturday.month);
this.setYearLabels(this.firstSaturday.year);
},incrementDate:function(date,bool){
var time=date.getTime();
var _9b8=1000*60*60*24;
time=(bool)?(time+_9b8):(time-_9b8);
var _9b9=new Date();
_9b9.setTime(time);
return _9b9;
},incrementWeek:function(evt){
var date=this.firstSaturday.date;
var _9bc=this.firstSaturday.month;
var year=this.firstSaturday.year;
switch(evt.target){
case this.increaseWeekNode.getElementsByTagName("img").item(0):
case this.increaseWeekNode:
date=date+7;
if(date>this._daysIn(_9bc,year)){
date=date-this._daysIn(_9bc,year);
if(_9bc<11){
_9bc++;
}else{
_9bc=0;
year++;
}
}
break;
case this.decreaseWeekNode.getElementsByTagName("img").item(0):
case this.decreaseWeekNode:
if(date>7){
date=date-7;
}else{
var diff=7-date;
if(_9bc>0){
_9bc--;
date=this._daysIn(_9bc,year)-diff;
}else{
year--;
_9bc=11;
date=31-diff;
}
}
break;
}
this.firstSaturday.date=date;
this.firstSaturday.month=_9bc;
this.firstSaturday.year=year;
this.initUI();
},incrementMonth:function(evt){
var _9c0=this.firstSaturday.month;
var year=this.firstSaturday.year;
switch(evt.currentTarget){
case this.increaseMonthNode:
if(_9c0<11){
_9c0++;
}else{
_9c0=0;
year++;
this.setYearLabels(year);
}
break;
case this.decreaseMonthNode:
if(_9c0>0){
_9c0--;
}else{
_9c0=11;
year--;
this.setYearLabels(year);
}
break;
case this.increaseMonthNode.getElementsByTagName("img").item(0):
if(_9c0<11){
_9c0++;
}else{
_9c0=0;
year++;
this.setYearLabels(year);
}
break;
case this.decreaseMonthNode.getElementsByTagName("img").item(0):
if(_9c0>0){
_9c0--;
}else{
_9c0=11;
year--;
this.setYearLabels(year);
}
break;
}
var _9c2=dojo.widget.DatePicker.util.initFirstSaturday(_9c0.toString(),year);
this.firstSaturday.year=_9c2.year;
this.firstSaturday.month=_9c2.month;
this.firstSaturday.date=_9c2.date;
this.initUI();
},incrementYear:function(evt){
var year=this.firstSaturday.year;
switch(evt.target){
case this.nextYearLabelNode:
year++;
break;
case this.previousYearLabelNode:
year--;
break;
}
var _9c5=dojo.widget.DatePicker.util.initFirstSaturday(this.firstSaturday.month.toString(),year);
this.firstSaturday.year=_9c5.year;
this.firstSaturday.month=_9c5.month;
this.firstSaturday.date=_9c5.date;
this.initUI();
},_daysIn:function(_9c6,year){
var _9c8=[31,28,31,30,31,30,31,31,30,31,30,31];
if(_9c6==1){
return (year%400==0)?29:(year%100==0)?28:(year%4==0)?29:28;
}else{
return _9c8[_9c6];
}
},onIncrementDate:function(evt){
dojo.unimplemented("dojo.widget.html.DatePicker.onIncrementDate");
},onIncrementWeek:function(evt){
evt.stopPropagation();
this.incrementWeek(evt);
},onIncrementMonth:function(evt){
evt.stopPropagation();
this.incrementMonth(evt);
},onIncrementYear:function(evt){
evt.stopPropagation();
this.incrementYear(evt);
},setMonthLabel:function(_9cd){
this.monthLabelNode.innerHTML=dojo.date.months[_9cd];
},setYearLabels:function(year){
this.previousYearLabelNode.innerHTML=year-1;
this.currentYearLabelNode.innerHTML=year;
this.nextYearLabelNode.innerHTML=year+1;
},getDateClassName:function(date,_9d0){
var _9d1=this.classNames[_9d0];
if((!this.selectedIsUsed)&&(date.getDate()==this.date.getDate())&&(date.getMonth()==this.date.getMonth())&&(date.getFullYear()==this.date.getFullYear())){
_9d1=this.classNames.selectedDate+" "+_9d1;
this.selectedIsUsed=1;
}
if((!this.currentIsUsed)&&(date.getDate()==this.today.getDate())&&(date.getMonth()==this.today.getMonth())&&(date.getFullYear()==this.today.getFullYear())){
_9d1=_9d1+" "+this.classNames.currentDate;
this.currentIsUsed=1;
}
return _9d1;
},onClick:function(evt){
dojo.event.browser.stopEvent(evt);
},onSetDate:function(evt){
dojo.event.browser.stopEvent(evt);
this.selectedIsUsed=0;
this.todayIsUsed=0;
var _9d4=this.firstSaturday.month;
var year=this.firstSaturday.year;
if(dojo.html.hasClass(evt.target,this.classNames["next"])){
_9d4=++_9d4%12;
year=(_9d4==0)?++year:year;
}else{
if(dojo.html.hasClass(evt.target,this.classNames["previous"])){
_9d4=--_9d4%12;
year=(_9d4==11)?--year:year;
}
}
this.date=new Date(year,_9d4,evt.target.innerHTML);
this.setDate(dojo.widget.DatePicker.util.toRfcDate(this.date));
this.initUI();
}});
dojo.provide("dojo.widget.DropdownDatePicker");
dojo.widget.defineWidget("dojo.widget.DropdownDatePicker",dojo.widget.DropdownContainer,{iconURL:dojo.uri.dojoUri("src/widget/templates/images/dateIcon.gif"),iconAlt:"Select a Date",zIndex:"10",datePicker:null,dateFormat:"%m/%d/%Y",date:null,fillInTemplate:function(args,frag){
dojo.widget.DropdownDatePicker.superclass.fillInTemplate.call(this,args,frag);
var _9d8=this.getFragNodeRef(frag);
if(args.date){
this.date=new Date(args.date);
}
var _9d9=document.createElement("div");
this.containerNode.appendChild(_9d9);
var _9da={widgetContainerId:this.widgetId};
if(this.date){
_9da["date"]=this.date;
_9da["storedDate"]=dojo.widget.DatePicker.util.toRfcDate(this.date);
this.inputNode.value=dojo.date.format(this.date,this.dateFormat);
}
this.datePicker=dojo.widget.createWidget("DatePicker",_9da,_9d9);
dojo.event.connect(this.datePicker,"onSetDate",this,"onSetDate");
this.containerNode.style.zIndex=this.zIndex;
this.containerNode.style.backgroundColor="transparent";
},onSetDate:function(){
this.inputNode.value=dojo.date.format(this.datePicker.date,this.dateFormat);
this.hideContainer();
},onInputChange:function(){
var tmp=new Date(this.inputNode.value);
this.datePicker.date=tmp;
this.datePicker.setDate(dojo.widget.DatePicker.util.toRfcDate(tmp));
this.datePicker.initData();
this.datePicker.initUI();
}},"html");
dojo.widget.tags.addParseTreeHandler("dojo:dropdowndatepicker");
dojo.provide("cocoon.ajax.insertion");
cocoon.ajax.insertion={};
dojo.lang.mixin(cocoon.ajax.insertion,{before:function(_9dc,_9dd){
return cocoon.ajax.insertionHelper.insert(_9dc,_9dd,function(_9de,_9df){
_9de.parentNode.insertBefore(_9df,_9de);
});
},after:function(_9e0,_9e1){
return cocoon.ajax.insertionHelper.insert(_9e0,_9e1,function(_9e2,_9e3){
if(_9e2.nextSibling){
_9e2.parentNode.insertBefore(_9e3,_9e2.nextSibling);
}else{
_9e2.parentNode.appendChild(_9e3);
}
});
},top:function(_9e4,_9e5){
return cocoon.ajax.insertionHelper.insert(_9e4,_9e5,function(_9e6,_9e7){
if(_9e6.firstChild){
_9e6.insertBefore(_9e7,_9e6.firstChild);
}else{
_9e6.appendChild(_9e7);
}
});
},bottom:function(_9e8,_9e9){
return cocoon.ajax.insertionHelper.insert(_9e8,_9e9,function(_9ea,_9eb){
_9ea.appendChild(_9eb);
});
},inside:function(_9ec,_9ed){
return cocoon.ajax.insertionHelper.insert(_9ec,_9ed,function(_9ee,_9ef){
while(_9ee.hasChildNodes()){
var _9f0=_9ee.firstChild;
if(_9f0.nodeType==dojo.dom.ELEMENT_NODE){
cocoon.ajax.insertionHelper.destroy(_9f0);
}
_9ee.removeChild(_9f0);
}
_9ee.appendChild(_9ef);
});
},replace:function(_9f1,_9f2){
return cocoon.ajax.insertionHelper.insert(_9f1,_9f2,function(_9f3,_9f4){
_9f3.parentNode.replaceChild(_9f4,_9f3);
cocoon.ajax.insertionHelper.destroy(_9f3);
});
}});
cocoon.ajax.insertionHelper={};
dojo.lang.mixin(cocoon.ajax.insertionHelper,{importNode:function(node,_9f6){
if(node.xml||dojo.lang.isString(node)){
var text=dojo.lang.isString(node)?node:node.xml;
var div=_9f6.createElement("DIV");
var _9f9="(?:<script.*?>)((\n|\r|.)*?)(?:</script>)";
var _9fa=text.replace(new RegExp(_9f9,"img"),"");
var _9fb="(<textarea[^<]*)/>";
var _9fc=_9fa.match(_9fb);
var _9fd=_9fa.replace(new RegExp(_9fb,"img"),"$1></textarea>");
div.innerHTML=_9fd;
var _9fe=new RegExp(_9f9,"img");
var _9ff=new RegExp(_9f9,"im");
var _a00=text.match(_9fe);
var _a01=new Array();
if(_a00){
for(var i=0;i<_a00.length;i++){
_a01.push(_a00[i].match(_9ff)[1]);
}
}
return {element:dojo.dom.getFirstChildElement(div),scripts:_a01};
}else{
var _a01=new Array();
var _a03=this._importDomNode(node,_9f6,_a01);
return {element:_a03,scripts:_a01};
}
},_importDomNode:function(node,_a05,_a06){
switch(node.nodeType){
case dojo.dom.ELEMENT_NODE:
if(node.nodeName.toLowerCase()=="script"){
_a06.push(node.firstChild&&node.firstChild.nodeValue);
return;
}
var _a07=_a05.createElement(node.nodeName);
var _a08=node.attributes;
for(var i=0;i<_a08.length;i++){
var attr=_a08[i];
_a07.setAttribute(attr.nodeName,attr.nodeValue);
}
var _a0b=node.childNodes;
for(var j=0;j<_a0b.length;j++){
var _a0d=this._importDomNode(_a0b[j],_a05,_a06);
if(_a0d){
_a07.appendChild(_a0d);
}
}
return _a07;
break;
case dojo.dom.TEXT_NODE:
return _a05.createTextNode(node.nodeValue);
break;
case dojo.dom.CDATA_SECTION_NODE:
return _a05.createTextNode(node.nodeValue);
break;
}
},_runScripts:function(_a0e){
for(var i=0;i<_a0e.scripts.length;i++){
eval(_a0e.scripts[i]);
}
},insert:function(_a10,_a11,_a12){
_a10=dojo.byId(_a10,_a11);
var _a13=this.importNode(_a11,_a10.ownerDocument);
_a12(_a10,_a13.element);
this._runScripts(_a13);
return this.parseDojoWidgets(_a13.element);
},destroy:function(_a14){
var _a15=dojo.widget.byNode(_a14);
if(_a15){
_a15.destroy(true,true);
}else{
var _a16=_a14.childNodes;
for(var i=0;i<_a16.length;i++){
var _a18=_a16[i];
if(_a18.nodeType==dojo.dom.ELEMENT_NODE){
this.destroy(_a18);
}
}
}
},parseDojoWidgets:function(_a19){
var _a1a=this.findParentWidget(_a19);
var _a1b=new dojo.xml.Parse();
var div=document.createElement("DIV");
_a19.parentNode.replaceChild(div,_a19);
div.appendChild(_a19);
var frag=_a1b.parseElement(div,null,true);
dojo.widget.getParser().createComponents(frag,_a1a);
_a19=div.firstChild;
div.parentNode.replaceChild(_a19,div);
_a1a&&_a1a.onResized();
return _a19;
},findParentWidget:function(_a1e){
var _a1f=_a1e.parentNode;
var _a20;
while(_a1f&&!_a20){
var _a20=dojo.widget.byNode(_a1f);
if(_a20){
return _a20;
}
_a1f=_a1f.parentNode;
}
}});
dojo.provide("dojo.animation.Timer");
dojo.animation.Timer=function(_a21){
var _a22=null;
this.isRunning=false;
this.interval=_a21;
this.onTick=function(){
};
this.onStart=null;
this.onStop=null;
this.setInterval=function(ms){
if(this.isRunning){
window.clearInterval(_a22);
}
this.interval=ms;
if(this.isRunning){
_a22=window.setInterval(dojo.lang.hitch(this,"onTick"),this.interval);
}
};
this.start=function(){
if(typeof this.onStart=="function"){
this.onStart();
}
this.isRunning=true;
_a22=window.setInterval(this.onTick,this.interval);
};
this.stop=function(){
if(typeof this.onStop=="function"){
this.onStop();
}
this.isRunning=false;
window.clearInterval(_a22);
};
};
dojo.provide("cocoon.ajax");
dojo.provide("cocoon.ajax.common");
dojo.lang.mixin(cocoon.ajax,{update:function(href,_a25,_a26){
if(dojo.lang.isString(_a25)){
var _a27=_a25.split("#");
if(_a27.length==2){
_a26=_a27[0];
_a25=dojo.byId(_a27[1]);
}else{
_a25=dojo.byId(_a25);
}
}
if(dojo.lang.isString(_a26)){
_a26=cocoon.ajax.insertion[_a26];
}
_a26=_a26||cocoon.ajax.insertion.inside;
dojo.io.bind({url:href,load:function(type,data,evt){
_a26(_a25,data);
},mimetype:"text/plain"});
},periodicalUpdate:function(_a2b,href,_a2d,_a2e){
dojo.require("dojo.animation.Timer");
var _a2f=new dojo.animation.Timer(_a2b);
_a2f.onTick=function(){
cocoon.ajax.update(href,_a2d,_a2e);
};
_a2f.onStart=_a2f.onTick;
_a2f.start();
return _a2f;
}});
dojo.provide("dojo.math.curves");
dojo.math.curves={Line:function(_a30,end){
this.start=_a30;
this.end=end;
this.dimensions=_a30.length;
for(var i=0;i<_a30.length;i++){
_a30[i]=Number(_a30[i]);
}
for(var i=0;i<end.length;i++){
end[i]=Number(end[i]);
}
this.getValue=function(n){
var _a34=new Array(this.dimensions);
for(var i=0;i<this.dimensions;i++){
_a34[i]=((this.end[i]-this.start[i])*n)+this.start[i];
}
return _a34;
};
return this;
},Bezier:function(pnts){
this.getValue=function(step){
if(step>=1){
return this.p[this.p.length-1];
}
if(step<=0){
return this.p[0];
}
var _a38=new Array(this.p[0].length);
for(var k=0;j<this.p[0].length;k++){
_a38[k]=0;
}
for(var j=0;j<this.p[0].length;j++){
var C=0;
var D=0;
for(var i=0;i<this.p.length;i++){
C+=this.p[i][j]*this.p[this.p.length-1][0]*dojo.math.bernstein(step,this.p.length,i);
}
for(var l=0;l<this.p.length;l++){
D+=this.p[this.p.length-1][0]*dojo.math.bernstein(step,this.p.length,l);
}
_a38[j]=C/D;
}
return _a38;
};
this.p=pnts;
return this;
},CatmullRom:function(pnts,c){
this.getValue=function(step){
var _a42=step*(this.p.length-1);
var node=Math.floor(_a42);
var _a44=_a42-node;
var i0=node-1;
if(i0<0){
i0=0;
}
var i=node;
var i1=node+1;
if(i1>=this.p.length){
i1=this.p.length-1;
}
var i2=node+2;
if(i2>=this.p.length){
i2=this.p.length-1;
}
var u=_a44;
var u2=_a44*_a44;
var u3=_a44*_a44*_a44;
var _a4c=new Array(this.p[0].length);
for(var k=0;k<this.p[0].length;k++){
var x1=(-this.c*this.p[i0][k])+((2-this.c)*this.p[i][k])+((this.c-2)*this.p[i1][k])+(this.c*this.p[i2][k]);
var x2=(2*this.c*this.p[i0][k])+((this.c-3)*this.p[i][k])+((3-2*this.c)*this.p[i1][k])+(-this.c*this.p[i2][k]);
var x3=(-this.c*this.p[i0][k])+(this.c*this.p[i1][k]);
var x4=this.p[i][k];
_a4c[k]=x1*u3+x2*u2+x3*u+x4;
}
return _a4c;
};
if(!c){
this.c=0.7;
}else{
this.c=c;
}
this.p=pnts;
return this;
},Arc:function(_a52,end,ccw){
var _a55=dojo.math.points.midpoint(_a52,end);
var _a56=dojo.math.points.translate(dojo.math.points.invert(_a55),_a52);
var rad=Math.sqrt(Math.pow(_a56[0],2)+Math.pow(_a56[1],2));
var _a58=dojo.math.radToDeg(Math.atan(_a56[1]/_a56[0]));
if(_a56[0]<0){
_a58-=90;
}else{
_a58+=90;
}
dojo.math.curves.CenteredArc.call(this,_a55,rad,_a58,_a58+(ccw?-180:180));
},CenteredArc:function(_a59,_a5a,_a5b,end){
this.center=_a59;
this.radius=_a5a;
this.start=_a5b||0;
this.end=end;
this.getValue=function(n){
var _a5e=new Array(2);
var _a5f=dojo.math.degToRad(this.start+((this.end-this.start)*n));
_a5e[0]=this.center[0]+this.radius*Math.sin(_a5f);
_a5e[1]=this.center[1]-this.radius*Math.cos(_a5f);
return _a5e;
};
return this;
},Circle:function(_a60,_a61){
dojo.math.curves.CenteredArc.call(this,_a60,_a61,0,360);
return this;
},Path:function(){
var _a62=[];
var _a63=[];
var _a64=[];
var _a65=0;
this.add=function(_a66,_a67){
if(_a67<0){
dojo.raise("dojo.math.curves.Path.add: weight cannot be less than 0");
}
_a62.push(_a66);
_a63.push(_a67);
_a65+=_a67;
computeRanges();
};
this.remove=function(_a68){
for(var i=0;i<_a62.length;i++){
if(_a62[i]==_a68){
_a62.splice(i,1);
_a65-=_a63.splice(i,1)[0];
break;
}
}
computeRanges();
};
this.removeAll=function(){
_a62=[];
_a63=[];
_a65=0;
};
this.getValue=function(n){
var _a6b=false,_a6c=0;
for(var i=0;i<_a64.length;i++){
var r=_a64[i];
if(n>=r[0]&&n<r[1]){
var subN=(n-r[0])/r[2];
_a6c=_a62[i].getValue(subN);
_a6b=true;
break;
}
}
if(!_a6b){
_a6c=_a62[_a62.length-1].getValue(1);
}
for(var j=0;j<i;j++){
_a6c=dojo.math.points.translate(_a6c,_a62[j].getValue(1));
}
return _a6c;
};
function computeRanges(){
var _a71=0;
for(var i=0;i<_a63.length;i++){
var end=_a71+_a63[i]/_a65;
var len=end-_a71;
_a64[i]=[_a71,end,len];
_a71=end;
}
}
return this;
}};
dojo.provide("dojo.fx.html");
dojo.deprecated("dojo.fx.html","use dojo.lfx.html instead","0.4");
dojo.fx.duration=300;
dojo.fx.html._makeFadeable=function(node){
if(dojo.render.html.ie){
if((node.style.zoom.length==0)&&(dojo.style.getStyle(node,"zoom")=="normal")){
node.style.zoom="1";
}
if((node.style.width.length==0)&&(dojo.style.getStyle(node,"width")=="auto")){
node.style.width="auto";
}
}
};
dojo.fx.html.fadeOut=function(node,_a77,_a78,_a79){
return dojo.fx.html.fade(node,_a77,dojo.style.getOpacity(node),0,_a78,_a79);
};
dojo.fx.html.fadeIn=function(node,_a7b,_a7c,_a7d){
return dojo.fx.html.fade(node,_a7b,dojo.style.getOpacity(node),1,_a7c,_a7d);
};
dojo.fx.html.fadeHide=function(node,_a7f,_a80,_a81){
node=dojo.byId(node);
if(!_a7f){
_a7f=150;
}
return dojo.fx.html.fadeOut(node,_a7f,function(node){
node.style.display="none";
if(typeof _a80=="function"){
_a80(node);
}
});
};
dojo.fx.html.fadeShow=function(node,_a84,_a85,_a86){
node=dojo.byId(node);
if(!_a84){
_a84=150;
}
node.style.display="block";
return dojo.fx.html.fade(node,_a84,0,1,_a85,_a86);
};
dojo.fx.html.fade=function(node,_a88,_a89,_a8a,_a8b,_a8c){
node=dojo.byId(node);
dojo.fx.html._makeFadeable(node);
var anim=new dojo.animation.Animation(new dojo.math.curves.Line([_a89],[_a8a]),_a88||dojo.fx.duration,0);
dojo.event.connect(anim,"onAnimate",function(e){
dojo.style.setOpacity(node,e.x);
});
if(_a8b){
dojo.event.connect(anim,"onEnd",function(e){
_a8b(node,anim);
});
}
if(!_a8c){
anim.play(true);
}
return anim;
};
dojo.fx.html.slideTo=function(node,_a91,_a92,_a93,_a94){
if(!dojo.lang.isNumber(_a91)){
var tmp=_a91;
_a91=_a92;
_a92=tmp;
}
node=dojo.byId(node);
var top=node.offsetTop;
var left=node.offsetLeft;
var pos=dojo.style.getComputedStyle(node,"position");
if(pos=="relative"||pos=="static"){
top=parseInt(dojo.style.getComputedStyle(node,"top"))||0;
left=parseInt(dojo.style.getComputedStyle(node,"left"))||0;
}
return dojo.fx.html.slide(node,_a91,[left,top],_a92,_a93,_a94);
};
dojo.fx.html.slideBy=function(node,_a9a,_a9b,_a9c,_a9d){
if(!dojo.lang.isNumber(_a9a)){
var tmp=_a9a;
_a9a=_a9b;
_a9b=tmp;
}
node=dojo.byId(node);
var top=node.offsetTop;
var left=node.offsetLeft;
var pos=dojo.style.getComputedStyle(node,"position");
if(pos=="relative"||pos=="static"){
top=parseInt(dojo.style.getComputedStyle(node,"top"))||0;
left=parseInt(dojo.style.getComputedStyle(node,"left"))||0;
}
return dojo.fx.html.slideTo(node,_a9a,[left+_a9b[0],top+_a9b[1]],_a9c,_a9d);
};
dojo.fx.html.slide=function(node,_aa3,_aa4,_aa5,_aa6,_aa7){
if(!dojo.lang.isNumber(_aa3)){
var tmp=_aa3;
_aa3=_aa5;
_aa5=_aa4;
_aa4=tmp;
}
node=dojo.byId(node);
if(dojo.style.getComputedStyle(node,"position")=="static"){
node.style.position="relative";
}
var anim=new dojo.animation.Animation(new dojo.math.curves.Line(_aa4,_aa5),_aa3||dojo.fx.duration,0);
dojo.event.connect(anim,"onAnimate",function(e){
with(node.style){
left=e.x+"px";
top=e.y+"px";
}
});
if(_aa6){
dojo.event.connect(anim,"onEnd",function(e){
_aa6(node,anim);
});
}
if(!_aa7){
anim.play(true);
}
return anim;
};
dojo.fx.html.colorFadeIn=function(node,_aad,_aae,_aaf,_ab0,_ab1){
if(!dojo.lang.isNumber(_aad)){
var tmp=_aad;
_aad=_aae;
_aae=tmp;
}
node=dojo.byId(node);
var _ab3=dojo.style.getBackgroundColor(node);
var bg=dojo.style.getStyle(node,"background-color").toLowerCase();
var _ab5=bg=="transparent"||bg=="rgba(0, 0, 0, 0)";
while(_ab3.length>3){
_ab3.pop();
}
var rgb=new dojo.graphics.color.Color(_aae).toRgb();
var anim=dojo.fx.html.colorFade(node,_aad||dojo.fx.duration,_aae,_ab3,_ab0,true);
dojo.event.connect(anim,"onEnd",function(e){
if(_ab5){
node.style.backgroundColor="transparent";
}
});
if(_aaf>0){
node.style.backgroundColor="rgb("+rgb.join(",")+")";
if(!_ab1){
setTimeout(function(){
anim.play(true);
},_aaf);
}
}else{
if(!_ab1){
anim.play(true);
}
}
return anim;
};
dojo.fx.html.highlight=dojo.fx.html.colorFadeIn;
dojo.fx.html.colorFadeFrom=dojo.fx.html.colorFadeIn;
dojo.fx.html.colorFadeOut=function(node,_aba,_abb,_abc,_abd,_abe){
if(!dojo.lang.isNumber(_aba)){
var tmp=_aba;
_aba=_abb;
_abb=tmp;
}
node=dojo.byId(node);
var _ac0=new dojo.graphics.color.Color(dojo.style.getBackgroundColor(node)).toRgb();
var rgb=new dojo.graphics.color.Color(_abb).toRgb();
var anim=dojo.fx.html.colorFade(node,_aba||dojo.fx.duration,_ac0,rgb,_abd,_abc>0||_abe);
if(_abc>0){
node.style.backgroundColor="rgb("+_ac0.join(",")+")";
if(!_abe){
setTimeout(function(){
anim.play(true);
},_abc);
}
}
return anim;
};
dojo.fx.html.unhighlight=dojo.fx.html.colorFadeOut;
dojo.fx.html.colorFadeTo=dojo.fx.html.colorFadeOut;
dojo.fx.html.colorFade=function(node,_ac4,_ac5,_ac6,_ac7,_ac8){
if(!dojo.lang.isNumber(_ac4)){
var tmp=_ac4;
_ac4=_ac6;
_ac6=_ac5;
_ac5=tmp;
}
node=dojo.byId(node);
var _aca=new dojo.graphics.color.Color(_ac5).toRgb();
var _acb=new dojo.graphics.color.Color(_ac6).toRgb();
var anim=new dojo.animation.Animation(new dojo.math.curves.Line(_aca,_acb),_ac4||dojo.fx.duration,0);
dojo.event.connect(anim,"onAnimate",function(e){
node.style.backgroundColor="rgb("+e.coordsAsInts().join(",")+")";
});
if(_ac7){
dojo.event.connect(anim,"onEnd",function(e){
_ac7(node,anim);
});
}
if(!_ac8){
anim.play(true);
}
return anim;
};
dojo.fx.html.wipeIn=function(node,_ad0,_ad1,_ad2){
node=dojo.byId(node);
var _ad3=dojo.style.getStyle(node,"overflow");
if(_ad3=="visible"){
node.style.overflow="hidden";
}
node.style.height=0;
dojo.style.show(node);
var anim=dojo.fx.html.wipe(node,_ad0,0,node.scrollHeight,null,true);
dojo.event.connect(anim,"onEnd",function(){
node.style.overflow=_ad3;
node.style.visibility="";
node.style.height="auto";
if(_ad1){
_ad1(node,anim);
}
});
if(!_ad2){
anim.play();
}
return anim;
};
dojo.fx.html.wipeOut=function(node,_ad6,_ad7,_ad8){
node=dojo.byId(node);
var _ad9=dojo.style.getStyle(node,"overflow");
if(_ad9=="visible"){
node.style.overflow="hidden";
}
var anim=dojo.fx.html.wipe(node,_ad6,node.offsetHeight,0,null,true);
dojo.event.connect(anim,"onEnd",function(){
dojo.style.hide(node);
node.style.visibility="hidden";
node.style.overflow=_ad9;
if(_ad7){
_ad7(node,anim);
}
});
if(!_ad8){
anim.play();
}
return anim;
};
dojo.fx.html.wipe=function(node,_adc,_add,_ade,_adf,_ae0){
node=dojo.byId(node);
var anim=new dojo.animation.Animation([[_add],[_ade]],_adc||dojo.fx.duration,0);
dojo.event.connect(anim,"onAnimate",function(e){
node.style.height=e.x+"px";
});
dojo.event.connect(anim,"onEnd",function(){
if(_adf){
_adf(node,anim);
}
});
if(!_ae0){
anim.play();
}
return anim;
};
dojo.fx.html.wiper=function(node,_ae4){
this.node=dojo.byId(node);
if(_ae4){
dojo.event.connect(dojo.byId(_ae4),"onclick",this,"toggle");
}
};
dojo.lang.extend(dojo.fx.html.wiper,{duration:dojo.fx.duration,_anim:null,toggle:function(){
if(!this._anim){
var type="wipe"+(dojo.style.isVisible(this.node)?"Out":"In");
this._anim=dojo.fx[type](this.node,this.duration,dojo.lang.hitch(this,"_callback"));
}
},_callback:function(){
this._anim=null;
}});
dojo.fx.html.explode=function(_ae6,_ae7,_ae8,_ae9,_aea){
var _aeb=dojo.style.toCoordinateArray(_ae6);
var _aec=document.createElement("div");
with(_aec.style){
position="absolute";
border="1px solid black";
display="none";
}
document.body.appendChild(_aec);
_ae7=dojo.byId(_ae7);
with(_ae7.style){
visibility="hidden";
display="block";
}
var _aed=dojo.style.toCoordinateArray(_ae7);
with(_ae7.style){
display="none";
visibility="visible";
}
var anim=new dojo.animation.Animation(new dojo.math.curves.Line(_aeb,_aed),_ae8||dojo.fx.duration,0);
dojo.event.connect(anim,"onBegin",function(e){
_aec.style.display="block";
});
dojo.event.connect(anim,"onAnimate",function(e){
with(_aec.style){
left=e.x+"px";
top=e.y+"px";
width=e.coords[2]+"px";
height=e.coords[3]+"px";
}
});
dojo.event.connect(anim,"onEnd",function(){
_ae7.style.display="block";
_aec.parentNode.removeChild(_aec);
if(_ae9){
_ae9(_ae7,anim);
}
});
if(!_aea){
anim.play();
}
return anim;
};
dojo.fx.html.implode=function(_af1,end,_af3,_af4,_af5){
var _af6=dojo.style.toCoordinateArray(_af1);
var _af7=dojo.style.toCoordinateArray(end);
_af1=dojo.byId(_af1);
var _af8=document.createElement("div");
with(_af8.style){
position="absolute";
border="1px solid black";
display="none";
}
document.body.appendChild(_af8);
var anim=new dojo.animation.Animation(new dojo.math.curves.Line(_af6,_af7),_af3||dojo.fx.duration,0);
dojo.event.connect(anim,"onBegin",function(e){
_af1.style.display="none";
_af8.style.display="block";
});
dojo.event.connect(anim,"onAnimate",function(e){
with(_af8.style){
left=e.x+"px";
top=e.y+"px";
width=e.coords[2]+"px";
height=e.coords[3]+"px";
}
});
dojo.event.connect(anim,"onEnd",function(){
_af8.parentNode.removeChild(_af8);
if(_af4){
_af4(_af1,anim);
}
});
if(!_af5){
anim.play();
}
return anim;
};
dojo.fx.html.Exploder=function(_afc,_afd){
_afc=dojo.byId(_afc);
_afd=dojo.byId(_afd);
var _afe=this;
this.waitToHide=500;
this.timeToShow=100;
this.waitToShow=200;
this.timeToHide=70;
this.autoShow=false;
this.autoHide=false;
var _aff=null;
var _b00=null;
var _b01=null;
var _b02=null;
var _b03=null;
var _b04=null;
this.showing=false;
this.onBeforeExplode=null;
this.onAfterExplode=null;
this.onBeforeImplode=null;
this.onAfterImplode=null;
this.onExploding=null;
this.onImploding=null;
this.timeShow=function(){
clearTimeout(_b01);
_b01=setTimeout(_afe.show,_afe.waitToShow);
};
this.show=function(){
clearTimeout(_b01);
clearTimeout(_b02);
if((_b00&&_b00.status()=="playing")||(_aff&&_aff.status()=="playing")||_afe.showing){
return;
}
if(typeof _afe.onBeforeExplode=="function"){
_afe.onBeforeExplode(_afc,_afd);
}
_aff=dojo.fx.html.explode(_afc,_afd,_afe.timeToShow,function(e){
_afe.showing=true;
if(typeof _afe.onAfterExplode=="function"){
_afe.onAfterExplode(_afc,_afd);
}
});
if(typeof _afe.onExploding=="function"){
dojo.event.connect(_aff,"onAnimate",this,"onExploding");
}
};
this.timeHide=function(){
clearTimeout(_b01);
clearTimeout(_b02);
if(_afe.showing){
_b02=setTimeout(_afe.hide,_afe.waitToHide);
}
};
this.hide=function(){
clearTimeout(_b01);
clearTimeout(_b02);
if(_aff&&_aff.status()=="playing"){
return;
}
_afe.showing=false;
if(typeof _afe.onBeforeImplode=="function"){
_afe.onBeforeImplode(_afc,_afd);
}
_b00=dojo.fx.html.implode(_afd,_afc,_afe.timeToHide,function(e){
if(typeof _afe.onAfterImplode=="function"){
_afe.onAfterImplode(_afc,_afd);
}
});
if(typeof _afe.onImploding=="function"){
dojo.event.connect(_b00,"onAnimate",this,"onImploding");
}
};
dojo.event.connect(_afc,"onclick",function(e){
if(_afe.showing){
_afe.hide();
}else{
_afe.show();
}
});
dojo.event.connect(_afc,"onmouseover",function(e){
if(_afe.autoShow){
_afe.timeShow();
}
});
dojo.event.connect(_afc,"onmouseout",function(e){
if(_afe.autoHide){
_afe.timeHide();
}
});
dojo.event.connect(_afd,"onmouseover",function(e){
clearTimeout(_b02);
});
dojo.event.connect(_afd,"onmouseout",function(e){
if(_afe.autoHide){
_afe.timeHide();
}
});
dojo.event.connect(document.documentElement||document.body,"onclick",function(e){
function isDesc(node,_b0e){
while(node){
if(node==_b0e){
return true;
}
node=node.parentNode;
}
return false;
}
if(_afe.autoHide&&_afe.showing&&!isDesc(e.target,_afd)&&!isDesc(e.target,_afc)){
_afe.hide();
}
});
return this;
};
dojo.fx.html.toggle={};
dojo.fx.html.toggle.plain={show:function(node,_b10,_b11,_b12){
dojo.style.show(node);
if(dojo.lang.isFunction(_b12)){
_b12();
}
},hide:function(node,_b14,_b15,_b16){
dojo.style.hide(node);
if(dojo.lang.isFunction(_b16)){
_b16();
}
}};
dojo.fx.html.toggle.fade={show:function(node,_b18,_b19,_b1a){
dojo.fx.html.fadeShow(node,_b18,_b1a);
},hide:function(node,_b1c,_b1d,_b1e){
dojo.fx.html.fadeHide(node,_b1c,_b1e);
}};
dojo.fx.html.toggle.wipe={show:function(node,_b20,_b21,_b22){
dojo.fx.html.wipeIn(node,_b20,_b22);
},hide:function(node,_b24,_b25,_b26){
dojo.fx.html.wipeOut(node,_b24,_b26);
}};
dojo.fx.html.toggle.explode={show:function(node,_b28,_b29,_b2a){
dojo.fx.html.explode(_b29||[0,0,0,0],node,_b28,_b2a);
},hide:function(node,_b2c,_b2d,_b2e){
dojo.fx.html.implode(node,_b2d||[0,0,0,0],_b2c,_b2e);
}};
dojo.lang.mixin(dojo.fx,dojo.fx.html);
dojo.provide("dojo.fx.*");
dojo.provide("cocoon.ajax.BUHandler");
cocoon.ajax.BUHandler=function(){
};
cocoon.ajax.BUHandler.fade=function(node){
dojo.require("dojo.fx.*");
dojo.fx.highlight(element,dojo.graphics.color.hex2rgb("#ffc"),700,300);
};
dojo.lang.extend(cocoon.ajax.BUHandler,{highlight:null,processResponse:function(doc,_b31){
var base=doc.documentElement;
var _b33=[];
if(base.nodeName.toLowerCase()=="bu:document"){
_b33=base.childNodes;
dojo.debug("got response using: XMLHTTPTransport");
}else{
base=dojo.byId("browser-update",doc);
if(base){
_b33=base.childNodes;
dojo.debug("got response using: IframeTransport");
}else{
this.handleError("No response data found",doc);
}
}
for(var i=0;i<_b33.length;i++){
var node=_b33[i];
if(node.nodeType==dojo.dom.ELEMENT_NODE){
var _b36=node.nodeName.replace(/.*:/,"").toLowerCase();
if(_b36=="textarea"){
_b36=node.getAttribute("name");
}
var _b37=this.handlers[_b36];
if(_b37){
_b37(node);
}else{
this.handleError("No handler found for element "+_b36,doc);
}
}
}
},handleError:function(_b38,_b39){
if(confirm(_b38+"\nShow server response?")){
var w=window.open(undefined,"Cocoon Error","location=no,resizable=yes,scrollbars=yes");
if(w==undefined){
alert("You must allow popups from this server to display the response.");
}else{
var doc=w.document;
if(_b39.responseText){
doc.open();
doc.write(_b39.responseText);
doc.close();
}else{
if(_b39.childNodes){
dojo.dom.copyChildren(doc,_b39);
}
}
}
}
},handlers:{replace:function(_b3c){
var id=_b3c.getAttribute("id");
if(!id){
alert("no id found on update element");
return;
}
var _b3e=dojo.dom.getFirstChildElement(_b3c);
if(!_b3e&&_b3c.nodeName.toLowerCase()=="textarea"){
_b3e=dojo.dom.createDocumentFromText(_b3c.value).documentElement;
}
var _b3f=document.getElementById(id);
if(!_b3f){
alert("no element '"+id+"' in source document");
return;
}
var _b40=cocoon.ajax.insertion.replace(_b3f,_b3e);
if(this.highlight){
this.highlight(_b40);
}
},redirect:function(_b41){
var uri=_b41.getAttribute("uri");
if(!uri){
alert("no uri found on redirect element");
return;
}
window.location=uri;
}}});
dojo.provide("cocoon.ajax.*");
if(dojo){
dojo.provide("cocoon.forms");
dojo.provide("cocoon.forms.common");
}else{
cocoon=cocoon||{};
cocoon.forms=cocoon.forms||{};
}
cocoon.forms.getForm=function(_b43){
while(_b43!=null&&_b43.tagName!=null&&_b43.tagName.toLowerCase()!="form"){
_b43=_b43.parentNode;
}
return _b43;
};
cocoon.forms.submitForm=function(_b44,name){
var form=this.getForm(_b44);
if(form==null){
alert("Cannot find form for "+_b44);
return;
}
if(!name){
name=_b44.name;
}
var _b47=form.getAttribute("dojoWidgetId");
if(_b47){
dojo.widget.byId(_b47).submit(name);
}else{
form["forms_submit_id"].value=name;
if(!form.onsubmit||form.onsubmit()!=false){
form.submit();
}
}
};
forms_submitForm=function(){
cocoon.forms.submitForm.apply(cocoon.forms,arguments);
};
dojo.provide("dojo.io.IframeIO");
dojo.io.createIFrame=function(_b48,_b49){
if(window[_b48]){
return window[_b48];
}
if(window.frames[_b48]){
return window.frames[_b48];
}
var r=dojo.render.html;
var _b4b=null;
var turi=dojo.uri.dojoUri("iframe_history.html?noInit=true");
var _b4d=((r.ie)&&(dojo.render.os.win))?"<iframe name='"+_b48+"' src='"+turi+"' onload='"+_b49+"'>":"iframe";
_b4b=document.createElement(_b4d);
with(_b4b){
name=_b48;
setAttribute("name",_b48);
id=_b48;
}
(document.body||document.getElementsByTagName("body")[0]).appendChild(_b4b);
window[_b48]=_b4b;
with(_b4b.style){
position="absolute";
left=top="0px";
height=width="1px";
visibility="hidden";
}
if(!r.ie){
dojo.io.setIFrameSrc(_b4b,turi,true);
_b4b.onload=new Function(_b49);
}
return _b4b;
};
dojo.io.iframeContentWindow=function(_b4e){
var win=_b4e.contentWindow||dojo.io.iframeContentDocument(_b4e).defaultView||dojo.io.iframeContentDocument(_b4e).__parent__||(_b4e.name&&document.frames[_b4e.name])||null;
return win;
};
dojo.io.iframeContentDocument=function(_b50){
var doc=_b50.contentDocument||((_b50.contentWindow)&&(_b50.contentWindow.document))||((_b50.name)&&(document.frames[_b50.name])&&(document.frames[_b50.name].document))||null;
return doc;
};
dojo.io.IframeTransport=new function(){
var _b52=this;
this.currentRequest=null;
this.requestQueue=[];
this.iframeName="dojoIoIframe";
this.fireNextRequest=function(){
if((this.currentRequest)||(this.requestQueue.length==0)){
return;
}
var cr=this.currentRequest=this.requestQueue.shift();
cr._contentToClean=[];
var fn=cr["formNode"];
var _b55=cr["content"]||{};
if(cr.sendTransport){
_b55["dojo.transport"]="iframe";
}
if(fn){
if(_b55){
for(var x in _b55){
if(!fn[x]){
var tn;
if(dojo.render.html.ie){
tn=document.createElement("<input type='hidden' name='"+x+"' value='"+_b55[x]+"'>");
fn.appendChild(tn);
}else{
tn=document.createElement("input");
fn.appendChild(tn);
tn.type="hidden";
tn.name=x;
tn.value=_b55[x];
}
cr._contentToClean.push(x);
}else{
fn[x].value=_b55[x];
}
}
}
if(cr["url"]){
cr._originalAction=fn.getAttribute("action");
fn.setAttribute("action",cr.url);
}
if(!fn.getAttribute("method")){
fn.setAttribute("method",(cr["method"])?cr["method"]:"post");
}
cr._originalTarget=fn.getAttribute("target");
fn.setAttribute("target",this.iframeName);
fn.target=this.iframeName;
fn.submit();
}else{
var _b58=dojo.io.argsFromMap(this.currentRequest.content);
var _b59=(cr.url.indexOf("?")>-1?"&":"?")+_b58;
dojo.io.setIFrameSrc(this.iframe,_b59,true);
}
};
this.canHandle=function(_b5a){
return ((dojo.lang.inArray(_b5a["mimetype"],["text/plain","text/html","text/javascript","text/json"]))&&((_b5a["formNode"])&&(dojo.io.checkChildrenForFile(_b5a["formNode"])))&&(dojo.lang.inArray(_b5a["method"].toLowerCase(),["post","get"]))&&(!((_b5a["sync"])&&(_b5a["sync"]==true))));
};
this.bind=function(_b5b){
if(!this["iframe"]){
this.setUpIframe();
}
this.requestQueue.push(_b5b);
this.fireNextRequest();
return;
};
this.setUpIframe=function(){
this.iframe=dojo.io.createIFrame(this.iframeName,"dojo.io.IframeTransport.iframeOnload();");
};
this.iframeOnload=function(){
if(!_b52.currentRequest){
_b52.fireNextRequest();
return;
}
var req=_b52.currentRequest;
var _b5d=req._contentToClean;
for(var i=0;i<_b5d.length;i++){
var key=_b5d[i];
if(dojo.render.html.safari){
var _b60=req.formNode;
for(var j=0;j<_b60.childNodes.length;j++){
var _b62=_b60.childNodes[j];
if(_b62.name==key){
var _b63=_b62.parentNode;
_b63.removeChild(_b62);
break;
}
}
}else{
var _b64=req.formNode[key];
req.formNode.removeChild(_b64);
req.formNode[key]=null;
}
}
if(req["_originalAction"]){
req.formNode.setAttribute("action",req._originalAction);
}
req.formNode.setAttribute("target",req._originalTarget);
req.formNode.target=req._originalTarget;
var ifd=dojo.io.iframeContentDocument(_b52.iframe);
var _b66;
var _b67=false;
try{
var cmt=req.mimetype;
if((cmt=="text/javascript")||(cmt=="text/json")){
var js=ifd.getElementsByTagName("textarea")[0].value;
if(cmt=="text/json"){
js="("+js+")";
}
_b66=dj_eval(js);
}else{
if(cmt=="text/html"){
_b66=ifd;
}else{
_b66=ifd.getElementsByTagName("textarea")[0].value;
}
}
_b67=true;
}
catch(e){
var _b6a=new dojo.io.Error("IframeTransport Error");
if(dojo.lang.isFunction(req["error"])){
req.error("error",_b6a,req);
}
}
try{
if(_b67&&dojo.lang.isFunction(req["load"])){
req.load("load",_b66,req);
}
}
catch(e){
throw e;
}
finally{
_b52.currentRequest=null;
_b52.fireNextRequest();
}
};
dojo.io.transports.addTransport("IframeTransport");
};
dojo.provide("cocoon.forms.CFormsForm");
dojo.widget.defineWidget("cocoon.forms.CFormsForm",dojo.widget.DomWidget,{widgetType:"CFormsForm",isContainer:true,buildRendering:function(args,_b6c,_b6d){
this.domNode=_b6c["dojo:"+this.widgetType.toLowerCase()].nodeRef;
this.id=this.domNode.getAttribute("id");
this.domNode.setAttribute("dojoWidgetId",this.widgetId);
dojo.event.connect("around",this.domNode,"onsubmit",this,"_browserSubmit");
dojo.event.connect(this.domNode,"onclick",this,"_grabClickTarget");
},_grabClickTarget:function(_b6e){
this.lastClickTarget=dojo.html.getEventTarget(_b6e);
},_browserSubmit:function(_b6f){
if(_b6f.proceed()==false){
return false;
}
var _b70=_b6f.args[0]||window.event;
var _b71=this.lastClickTarget;
this.submit(_b71&&_b71.name);
return false;
},submit:function(name,_b73){
var form=this.domNode;
var _b75=this;
var _b76="text/xml";
if(!_b73){
_b73={};
}
document.body.style.cursor="wait";
var uri=form.getAttribute("ajax-action");
if(!uri){
uri=form.action;
}
if(uri==""){
uri=document.location;
}
form["forms_submit_id"].value=name;
_b73["cocoon-ajax"]=true;
if(dojo.io.formHasFile(form)){
if(dojo.render.html.safari){
form.submit();
return;
}
dojo.require("dojo.io.IframeIO");
_b76="text/html";
}
dojo.io.bind({url:uri,handle:function(type,data,evt){
_b75._handleBrowserUpdate(_b75,name,type,data,evt);
},method:"post",mimetype:_b76,content:_b73,formNode:form,sendTransport:true});
if(form[name]){
form[name].disabled=true;
}
},_handleBrowserUpdate:function(_b7b,name,type,data,evt){
document.body.style.cursor="auto";
if(this.domNode[name]){
this.domNode[name].disabled=false;
}
var _b80=new cocoon.ajax.BUHandler();
if(type=="load"){
if(!data){
cocoon.ajax.BUHandler.handleError("No xml answer",data);
return;
}
_b80.handlers["continue"]=function(){
_b7b._continue();
};
_b80.processResponse(data,evt);
}else{
if(type=="error"){
_b80.handleError("Request failed",data);
}else{
}
}
},_continue:function(){
var form=this.domNode;
if(form.method.toLowerCase()=="post"){
var div=document.createElement("div");
var _b83="<form action='"+form.action+"' method='POST'>"+"<input type='hidden' name='cocoon-ajax-continue' value='true'/>";
if(form.elements["continuation-id"]){
_b83+="<input type='hidden' name='continuation-id' value='"+form.elements["continuation-id"].value+"'/>";
}
_b83+="</form>";
div.innerHTML=_b83;
document.body.appendChild(div);
div.firstChild.submit();
}else{
var _b84="?cocoon-ajax-continue=true";
if(form.elements["continuation-id"]){
_b84+="&continuation-id="+form.elements["continuation-id"].value;
}
window.location.href=form.action+_b84;
}
}});
dojo.io.checkChildrenForFile=function(node){
var _b86=false;
var _b87=node.getElementsByTagName("input");
dojo.lang.forEach(_b87,function(_b88){
if(_b86){
return;
}
if(_b88.getAttribute("type")=="file"&&!_b88.disabled){
_b86=true;
}
});
return _b86;
};
dojo.provide("dojo.dnd.DragSource");
dojo.provide("dojo.dnd.DropTarget");
dojo.provide("dojo.dnd.DragObject");
dojo.provide("dojo.dnd.DragAndDrop");
dojo.dnd.DragSource=function(){
var dm=dojo.dnd.dragManager;
if(dm["registerDragSource"]){
dm.registerDragSource(this);
}
};
dojo.lang.extend(dojo.dnd.DragSource,{type:"",onDragEnd:function(){
},onDragStart:function(){
},onSelected:function(){
},unregister:function(){
dojo.dnd.dragManager.unregisterDragSource(this);
},reregister:function(){
dojo.dnd.dragManager.registerDragSource(this);
}});
dojo.dnd.DragObject=function(){
var dm=dojo.dnd.dragManager;
if(dm["registerDragObject"]){
dm.registerDragObject(this);
}
};
dojo.lang.extend(dojo.dnd.DragObject,{type:"",onDragStart:function(){
},onDragMove:function(){
},onDragOver:function(){
},onDragOut:function(){
},onDragEnd:function(){
},onDragLeave:this.onDragOut,onDragEnter:this.onDragOver,ondragout:this.onDragOut,ondragover:this.onDragOver});
dojo.dnd.DropTarget=function(){
if(this.constructor==dojo.dnd.DropTarget){
return;
}
this.acceptedTypes=[];
dojo.dnd.dragManager.registerDropTarget(this);
};
dojo.lang.extend(dojo.dnd.DropTarget,{acceptsType:function(type){
if(!dojo.lang.inArray(this.acceptedTypes,"*")){
if(!dojo.lang.inArray(this.acceptedTypes,type)){
return false;
}
}
return true;
},accepts:function(_b8c){
if(!dojo.lang.inArray(this.acceptedTypes,"*")){
for(var i=0;i<_b8c.length;i++){
if(!dojo.lang.inArray(this.acceptedTypes,_b8c[i].type)){
return false;
}
}
}
return true;
},onDragOver:function(){
},onDragOut:function(){
},onDragMove:function(){
},onDropStart:function(){
},onDrop:function(){
},onDropEnd:function(){
}});
dojo.dnd.DragEvent=function(){
this.dragSource=null;
this.dragObject=null;
this.target=null;
this.eventStatus="success";
};
dojo.dnd.DragManager=function(){
};
dojo.lang.extend(dojo.dnd.DragManager,{selectedSources:[],dragObjects:[],dragSources:[],registerDragSource:function(){
},dropTargets:[],registerDropTarget:function(){
},lastDragTarget:null,currentDragTarget:null,onKeyDown:function(){
},onMouseOut:function(){
},onMouseMove:function(){
},onMouseUp:function(){
}});
dojo.provide("dojo.dnd.HtmlDragManager");
dojo.dnd.HtmlDragManager=function(){
};
dojo.inherits(dojo.dnd.HtmlDragManager,dojo.dnd.DragManager);
dojo.lang.extend(dojo.dnd.HtmlDragManager,{disabled:false,nestedTargets:false,mouseDownTimer:null,dsCounter:0,dsPrefix:"dojoDragSource",dropTargetDimensions:[],currentDropTarget:null,previousDropTarget:null,_dragTriggered:false,selectedSources:[],dragObjects:[],currentX:null,currentY:null,lastX:null,lastY:null,mouseDownX:null,mouseDownY:null,threshold:7,dropAcceptable:false,cancelEvent:function(e){
e.stopPropagation();
e.preventDefault();
},registerDragSource:function(ds){
if(ds["domNode"]){
var dp=this.dsPrefix;
var _b91=dp+"Idx_"+(this.dsCounter++);
ds.dragSourceId=_b91;
this.dragSources[_b91]=ds;
ds.domNode.setAttribute(dp,_b91);
if(dojo.render.html.ie){
dojo.event.connect(ds.domNode,"ondragstart",this.cancelEvent);
}
}
},unregisterDragSource:function(ds){
if(ds["domNode"]){
var dp=this.dsPrefix;
var _b94=ds.dragSourceId;
delete ds.dragSourceId;
delete this.dragSources[_b94];
ds.domNode.setAttribute(dp,null);
}
if(dojo.render.html.ie){
dojo.event.disconnect(ds.domNode,"ondragstart",this.cancelEvent);
}
},registerDropTarget:function(dt){
this.dropTargets.push(dt);
},unregisterDropTarget:function(dt){
var _b97=dojo.lang.find(this.dropTargets,dt,true);
if(_b97>=0){
this.dropTargets.splice(_b97,1);
}
},getDragSource:function(e){
var tn=e.target;
if(tn===document.body){
return;
}
var ta=dojo.html.getAttribute(tn,this.dsPrefix);
while((!ta)&&(tn)){
tn=tn.parentNode;
if((!tn)||(tn===document.body)){
return;
}
ta=dojo.html.getAttribute(tn,this.dsPrefix);
}
return this.dragSources[ta];
},onKeyDown:function(e){
},onMouseDown:function(e){
if(this.disabled){
return;
}
if(dojo.render.html.ie){
if(e.button!=1){
return;
}
}else{
if(e.which!=1){
return;
}
}
var _b9d=e.target.nodeType==dojo.dom.TEXT_NODE?e.target.parentNode:e.target;
if(dojo.html.isTag(_b9d,"button","textarea","input","select","option")){
return;
}
var ds=this.getDragSource(e);
if(!ds){
return;
}
if(!dojo.lang.inArray(this.selectedSources,ds)){
this.selectedSources.push(ds);
ds.onSelected();
}
this.mouseDownX=e.pageX;
this.mouseDownY=e.pageY;
e.preventDefault();
dojo.event.connect(document,"onmousemove",this,"onMouseMove");
},onMouseUp:function(e,_ba0){
if(this.selectedSources.length==0){
return;
}
this.mouseDownX=null;
this.mouseDownY=null;
this._dragTriggered=false;
e.dragSource=this.dragSource;
if((!e.shiftKey)&&(!e.ctrlKey)){
if(this.currentDropTarget){
this.currentDropTarget.onDropStart();
}
dojo.lang.forEach(this.dragObjects,function(_ba1){
var ret=null;
if(!_ba1){
return;
}
if(this.currentDropTarget){
e.dragObject=_ba1;
var ce=this.currentDropTarget.domNode.childNodes;
if(ce.length>0){
e.dropTarget=ce[0];
while(e.dropTarget==_ba1.domNode){
e.dropTarget=e.dropTarget.nextSibling;
}
}else{
e.dropTarget=this.currentDropTarget.domNode;
}
if(this.dropAcceptable){
ret=this.currentDropTarget.onDrop(e);
}else{
this.currentDropTarget.onDragOut(e);
}
}
e.dragStatus=this.dropAcceptable&&ret?"dropSuccess":"dropFailure";
dojo.lang.delayThese([function(){
try{
_ba1.dragSource.onDragEnd(e);
}
catch(err){
var _ba4={};
for(var i in e){
if(i=="type"){
_ba4.type="mouseup";
continue;
}
_ba4[i]=e[i];
}
_ba1.dragSource.onDragEnd(_ba4);
}
},function(){
_ba1.onDragEnd(e);
}]);
},this);
this.selectedSources=[];
this.dragObjects=[];
this.dragSource=null;
if(this.currentDropTarget){
this.currentDropTarget.onDropEnd();
}
}
dojo.event.disconnect(document,"onmousemove",this,"onMouseMove");
this.currentDropTarget=null;
},onScroll:function(){
for(var i=0;i<this.dragObjects.length;i++){
if(this.dragObjects[i].updateDragOffset){
this.dragObjects[i].updateDragOffset();
}
}
this.cacheTargetLocations();
},_dragStartDistance:function(x,y){
if((!this.mouseDownX)||(!this.mouseDownX)){
return;
}
var dx=Math.abs(x-this.mouseDownX);
var dx2=dx*dx;
var dy=Math.abs(y-this.mouseDownY);
var dy2=dy*dy;
return parseInt(Math.sqrt(dx2+dy2),10);
},cacheTargetLocations:function(){
this.dropTargetDimensions=[];
dojo.lang.forEach(this.dropTargets,function(_bad){
var tn=_bad.domNode;
if(!tn){
return;
}
var ttx=dojo.style.getAbsoluteX(tn,true);
var tty=dojo.style.getAbsoluteY(tn,true);
this.dropTargetDimensions.push([[ttx,tty],[ttx+dojo.style.getInnerWidth(tn),tty+dojo.style.getInnerHeight(tn)],_bad]);
},this);
},onMouseMove:function(e){
if((dojo.render.html.ie)&&(e.button!=1)){
this.currentDropTarget=null;
this.onMouseUp(e,true);
return;
}
if((this.selectedSources.length)&&(!this.dragObjects.length)){
var dx;
var dy;
if(!this._dragTriggered){
this._dragTriggered=(this._dragStartDistance(e.pageX,e.pageY)>this.threshold);
if(!this._dragTriggered){
return;
}
dx=e.pageX-this.mouseDownX;
dy=e.pageY-this.mouseDownY;
}
this.dragSource=this.selectedSources[0];
dojo.lang.forEach(this.selectedSources,function(_bb4){
if(!_bb4){
return;
}
var tdo=_bb4.onDragStart(e);
if(tdo){
tdo.onDragStart(e);
tdo.dragOffset.top+=dy;
tdo.dragOffset.left+=dx;
tdo.dragSource=_bb4;
this.dragObjects.push(tdo);
}
},this);
this.previousDropTarget=null;
this.cacheTargetLocations();
}
dojo.lang.forEach(this.dragObjects,function(_bb6){
if(_bb6){
_bb6.onDragMove(e);
}
});
if(this.currentDropTarget){
var c=dojo.style.toCoordinateArray(this.currentDropTarget.domNode,true);
var dtp=[[c[0],c[1]],[c[0]+c[2],c[1]+c[3]]];
}
if((!this.nestedTargets)&&(dtp)&&(this.isInsideBox(e,dtp))){
if(this.dropAcceptable){
this.currentDropTarget.onDragMove(e,this.dragObjects);
}
}else{
var _bb9=this.findBestTarget(e);
if(_bb9.target===null){
if(this.currentDropTarget){
this.currentDropTarget.onDragOut(e);
this.previousDropTarget=this.currentDropTarget;
this.currentDropTarget=null;
}
this.dropAcceptable=false;
return;
}
if(this.currentDropTarget!==_bb9.target){
if(this.currentDropTarget){
this.previousDropTarget=this.currentDropTarget;
this.currentDropTarget.onDragOut(e);
}
this.currentDropTarget=_bb9.target;
e.dragObjects=this.dragObjects;
this.dropAcceptable=this.currentDropTarget.onDragOver(e);
}else{
if(this.dropAcceptable){
this.currentDropTarget.onDragMove(e,this.dragObjects);
}
}
}
},findBestTarget:function(e){
var _bbb=this;
var _bbc=new Object();
_bbc.target=null;
_bbc.points=null;
dojo.lang.every(this.dropTargetDimensions,function(_bbd){
if(!_bbb.isInsideBox(e,_bbd)){
return true;
}
_bbc.target=_bbd[2];
_bbc.points=_bbd;
return Boolean(_bbb.nestedTargets);
});
return _bbc;
},isInsideBox:function(e,_bbf){
if((e.pageX>_bbf[0][0])&&(e.pageX<_bbf[1][0])&&(e.pageY>_bbf[0][1])&&(e.pageY<_bbf[1][1])){
return true;
}
return false;
},onMouseOver:function(e){
},onMouseOut:function(e){
}});
dojo.dnd.dragManager=new dojo.dnd.HtmlDragManager();
(function(){
var d=document;
var dm=dojo.dnd.dragManager;
dojo.event.connect(d,"onkeydown",dm,"onKeyDown");
dojo.event.connect(d,"onmouseover",dm,"onMouseOver");
dojo.event.connect(d,"onmouseout",dm,"onMouseOut");
dojo.event.connect(d,"onmousedown",dm,"onMouseDown");
dojo.event.connect(d,"onmouseup",dm,"onMouseUp");
dojo.event.connect(window,"onscroll",dm,"onScroll");
})();
dojo.provide("dojo.dnd.HtmlDragAndDrop");
dojo.provide("dojo.dnd.HtmlDragSource");
dojo.provide("dojo.dnd.HtmlDropTarget");
dojo.provide("dojo.dnd.HtmlDragObject");
dojo.dnd.HtmlDragSource=function(node,type){
node=dojo.byId(node);
this.dragObjects=[];
this.constrainToContainer=false;
if(node){
this.domNode=node;
this.dragObject=node;
dojo.dnd.DragSource.call(this);
this.type=(type)||(this.domNode.nodeName.toLowerCase());
}
};
dojo.inherits(dojo.dnd.HtmlDragSource,dojo.dnd.DragSource);
dojo.lang.extend(dojo.dnd.HtmlDragSource,{dragClass:"",onDragStart:function(){
var _bc6=new dojo.dnd.HtmlDragObject(this.dragObject,this.type);
if(this.dragClass){
_bc6.dragClass=this.dragClass;
}
if(this.constrainToContainer){
_bc6.constrainTo(this.constrainingContainer||this.domNode.parentNode);
}
return _bc6;
},setDragHandle:function(node){
node=dojo.byId(node);
dojo.dnd.dragManager.unregisterDragSource(this);
this.domNode=node;
dojo.dnd.dragManager.registerDragSource(this);
},setDragTarget:function(node){
this.dragObject=node;
},constrainTo:function(_bc9){
this.constrainToContainer=true;
if(_bc9){
this.constrainingContainer=_bc9;
}
},onSelected:function(){
for(var i=0;i<this.dragObjects.length;i++){
dojo.dnd.dragManager.selectedSources.push(new dojo.dnd.HtmlDragSource(this.dragObjects[i]));
}
},addDragObjects:function(el){
for(var i=0;i<arguments.length;i++){
this.dragObjects.push(arguments[i]);
}
}});
dojo.dnd.HtmlDragObject=function(node,type){
this.domNode=dojo.byId(node);
this.type=type;
this.constrainToContainer=false;
this.dragSource=null;
};
dojo.inherits(dojo.dnd.HtmlDragObject,dojo.dnd.DragObject);
dojo.lang.extend(dojo.dnd.HtmlDragObject,{dragClass:"",opacity:0.5,createIframe:true,disableX:false,disableY:false,createDragNode:function(){
var node=this.domNode.cloneNode(true);
if(this.dragClass){
dojo.html.addClass(node,this.dragClass);
}
if(this.opacity<1){
dojo.style.setOpacity(node,this.opacity);
}
if(node.tagName.toLowerCase()=="tr"){
var doc=this.domNode.ownerDocument;
var _bd1=doc.createElement("table");
var _bd2=doc.createElement("tbody");
_bd2.appendChild(node);
_bd1.appendChild(_bd2);
var _bd3=this.domNode.childNodes;
var _bd4=node.childNodes;
for(var i=0;i<_bd3.length;i++){
if((_bd4[i])&&(_bd4[i].style)){
_bd4[i].style.width=dojo.style.getContentWidth(_bd3[i])+"px";
}
}
node=_bd1;
}
if((dojo.render.html.ie55||dojo.render.html.ie60)&&this.createIframe){
with(node.style){
top="0px";
left="0px";
}
var _bd6=document.createElement("div");
_bd6.appendChild(node);
this.bgIframe=new dojo.html.BackgroundIframe(_bd6);
_bd6.appendChild(this.bgIframe.iframe);
node=_bd6;
}
node.style.zIndex=999;
return node;
},onDragStart:function(e){
dojo.html.clearSelection();
this.scrollOffset=dojo.html.getScrollOffset();
this.dragStartPosition=dojo.style.getAbsolutePosition(this.domNode,true);
this.dragOffset={y:this.dragStartPosition.y-e.pageY,x:this.dragStartPosition.x-e.pageX};
this.dragClone=this.createDragNode();
this.containingBlockPosition=this.domNode.offsetParent?dojo.style.getAbsolutePosition(this.domNode.offsetParent):{x:0,y:0};
if(this.constrainToContainer){
this.constraints=this.getConstraints();
}
with(this.dragClone.style){
position="absolute";
top=this.dragOffset.y+e.pageY+"px";
left=this.dragOffset.x+e.pageX+"px";
}
document.body.appendChild(this.dragClone);
dojo.event.topic.publish("dragStart",{source:this});
},getConstraints:function(){
if(this.constrainingContainer.nodeName.toLowerCase()=="body"){
var _bd8=dojo.html.getViewportWidth();
var _bd9=dojo.html.getViewportHeight();
var x=0;
var y=0;
}else{
_bd8=dojo.style.getContentWidth(this.constrainingContainer);
_bd9=dojo.style.getContentHeight(this.constrainingContainer);
x=this.containingBlockPosition.x+dojo.style.getPixelValue(this.constrainingContainer,"padding-left",true)+dojo.style.getBorderExtent(this.constrainingContainer,"left");
y=this.containingBlockPosition.y+dojo.style.getPixelValue(this.constrainingContainer,"padding-top",true)+dojo.style.getBorderExtent(this.constrainingContainer,"top");
}
return {minX:x,minY:y,maxX:x+_bd8-dojo.style.getOuterWidth(this.domNode),maxY:y+_bd9-dojo.style.getOuterHeight(this.domNode)};
},updateDragOffset:function(){
var _bdc=dojo.html.getScrollOffset();
if(_bdc.y!=this.scrollOffset.y){
var diff=_bdc.y-this.scrollOffset.y;
this.dragOffset.y+=diff;
this.scrollOffset.y=_bdc.y;
}
if(_bdc.x!=this.scrollOffset.x){
var diff=_bdc.x-this.scrollOffset.x;
this.dragOffset.x+=diff;
this.scrollOffset.x=_bdc.x;
}
},onDragMove:function(e){
this.updateDragOffset();
var x=this.dragOffset.x+e.pageX;
var y=this.dragOffset.y+e.pageY;
if(this.constrainToContainer){
if(x<this.constraints.minX){
x=this.constraints.minX;
}
if(y<this.constraints.minY){
y=this.constraints.minY;
}
if(x>this.constraints.maxX){
x=this.constraints.maxX;
}
if(y>this.constraints.maxY){
y=this.constraints.maxY;
}
}
this.setAbsolutePosition(x,y);
dojo.event.topic.publish("dragMove",{source:this});
},setAbsolutePosition:function(x,y){
if(!this.disableY){
this.dragClone.style.top=y+"px";
}
if(!this.disableX){
this.dragClone.style.left=x+"px";
}
},onDragEnd:function(e){
switch(e.dragStatus){
case "dropSuccess":
dojo.dom.removeNode(this.dragClone);
this.dragClone=null;
break;
case "dropFailure":
var _be4=dojo.style.getAbsolutePosition(this.dragClone,true);
var _be5=[this.dragStartPosition.x+1,this.dragStartPosition.y+1];
var line=new dojo.lfx.Line(_be4,_be5);
var anim=new dojo.lfx.Animation(500,line,dojo.lfx.easeOut);
var _be8=this;
dojo.event.connect(anim,"onAnimate",function(e){
_be8.dragClone.style.left=e[0]+"px";
_be8.dragClone.style.top=e[1]+"px";
});
dojo.event.connect(anim,"onEnd",function(e){
dojo.lang.setTimeout(function(){
dojo.dom.removeNode(_be8.dragClone);
_be8.dragClone=null;
},200);
});
anim.play();
break;
}
dojo.event.connect(this.domNode,"onclick",this,"squelchOnClick");
dojo.event.topic.publish("dragEnd",{source:this});
},squelchOnClick:function(e){
e.preventDefault();
dojo.event.disconnect(this.domNode,"onclick",this,"squelchOnClick");
},constrainTo:function(_bec){
this.constrainToContainer=true;
if(_bec){
this.constrainingContainer=_bec;
}else{
this.constrainingContainer=this.domNode.parentNode;
}
}});
dojo.dnd.HtmlDropTarget=function(node,_bee){
if(arguments.length==0){
return;
}
this.domNode=dojo.byId(node);
dojo.dnd.DropTarget.call(this);
if(_bee&&dojo.lang.isString(_bee)){
_bee=[_bee];
}
this.acceptedTypes=_bee||[];
};
dojo.inherits(dojo.dnd.HtmlDropTarget,dojo.dnd.DropTarget);
dojo.lang.extend(dojo.dnd.HtmlDropTarget,{onDragOver:function(e){
if(!this.accepts(e.dragObjects)){
return false;
}
this.childBoxes=[];
for(var i=0,_bf1;i<this.domNode.childNodes.length;i++){
_bf1=this.domNode.childNodes[i];
if(_bf1.nodeType!=dojo.dom.ELEMENT_NODE){
continue;
}
var pos=dojo.style.getAbsolutePosition(_bf1,true);
var _bf3=dojo.style.getInnerHeight(_bf1);
var _bf4=dojo.style.getInnerWidth(_bf1);
this.childBoxes.push({top:pos.y,bottom:pos.y+_bf3,left:pos.x,right:pos.x+_bf4,node:_bf1});
}
return true;
},_getNodeUnderMouse:function(e){
for(var i=0,_bf7;i<this.childBoxes.length;i++){
with(this.childBoxes[i]){
if(e.pageX>=left&&e.pageX<=right&&e.pageY>=top&&e.pageY<=bottom){
return i;
}
}
}
return -1;
},createDropIndicator:function(){
this.dropIndicator=document.createElement("div");
with(this.dropIndicator.style){
position="absolute";
zIndex=999;
borderTopWidth="1px";
borderTopColor="black";
borderTopStyle="solid";
width=dojo.style.getInnerWidth(this.domNode)+"px";
left=dojo.style.getAbsoluteX(this.domNode,true)+"px";
}
},onDragMove:function(e,_bf9){
var i=this._getNodeUnderMouse(e);
if(!this.dropIndicator){
this.createDropIndicator();
}
if(i<0){
if(this.childBoxes.length){
var _bfb=(dojo.html.gravity(this.childBoxes[0].node,e)&dojo.html.gravity.NORTH);
}else{
var _bfb=true;
}
}else{
var _bfc=this.childBoxes[i];
var _bfb=(dojo.html.gravity(_bfc.node,e)&dojo.html.gravity.NORTH);
}
this.placeIndicator(e,_bf9,i,_bfb);
if(!dojo.html.hasParent(this.dropIndicator)){
document.body.appendChild(this.dropIndicator);
}
},placeIndicator:function(e,_bfe,_bff,_c00){
with(this.dropIndicator.style){
if(_bff<0){
if(this.childBoxes.length){
top=(_c00?this.childBoxes[0].top:this.childBoxes[this.childBoxes.length-1].bottom)+"px";
}else{
top=dojo.style.getAbsoluteY(this.domNode,true)+"px";
}
}else{
var _c01=this.childBoxes[_bff];
top=(_c00?_c01.top:_c01.bottom)+"px";
}
}
},onDragOut:function(e){
if(this.dropIndicator){
dojo.dom.removeNode(this.dropIndicator);
delete this.dropIndicator;
}
},onDrop:function(e){
this.onDragOut(e);
var i=this._getNodeUnderMouse(e);
if(i<0){
if(this.childBoxes.length){
if(dojo.html.gravity(this.childBoxes[0].node,e)&dojo.html.gravity.NORTH){
return this.insert(e,this.childBoxes[0].node,"before");
}else{
return this.insert(e,this.childBoxes[this.childBoxes.length-1].node,"after");
}
}
return this.insert(e,this.domNode,"append");
}
var _c05=this.childBoxes[i];
if(dojo.html.gravity(_c05.node,e)&dojo.html.gravity.NORTH){
return this.insert(e,_c05.node,"before");
}else{
return this.insert(e,_c05.node,"after");
}
},insert:function(e,_c07,_c08){
var node=e.dragObject.domNode;
if(_c08=="before"){
return dojo.html.insertBefore(node,_c07);
}else{
if(_c08=="after"){
return dojo.html.insertAfter(node,_c07);
}else{
if(_c08=="append"){
_c07.appendChild(node);
return true;
}
}
}
return false;
}});
dojo.provide("cocoon.forms.CFormsRepeater");
cocoon.forms.CFormsRepeater=function(){
dojo.widget.DomWidget.call(this);
};
dojo.inherits(cocoon.forms.CFormsRepeater,dojo.widget.DomWidget);
dojo.lang.extend(cocoon.forms.CFormsRepeater,{orderable:false,select:"$no$",widgetType:"CFormsRepeater",isContainer:true,getType:function(){
return "cforms-"+this.id;
},buildRendering:function(args,_c0b,_c0c){
this.domNode=_c0b["dojo:"+this.widgetType.toLowerCase()].nodeRef;
this.id=this.domNode.getAttribute("id");
if(!this.orderable&&this.select=="none"){
dojo.debug(this.widgetType+" '"+this.id+"' is not orderable nor selectable");
}
if(this.orderable){
var _c0d=dojo.byId(this.id+".0");
if(!_c0d){
return;
}
if(_c0d.tagName.toLowerCase()=="tr"&&_c0d.parentNode.tagName.toLowerCase()!="tbody"){
throw this.widgetType+" requires TR's to be in a TBODY (check '"+this.id+"')";
}
var type=this.getType();
var _c0f=new dojo.dnd.HtmlDropTarget(_c0d.parentNode,[type]);
_c0f.createDropIndicator=function(){
this.dropIndicator=document.createElement("div");
this.dropIndicator.className="forms-dropIndicator";
with(this.dropIndicator.style){
position="absolute";
zIndex=1;
width=dojo.style.getInnerWidth(this.domNode)+"px";
left=dojo.style.getAbsoluteX(this.domNode)+"px";
}
};
dojo.event.connect(_c0f,"insert",this,"afterInsert");
var row;
for(var idx=0;row=dojo.byId(this.id+"."+idx);idx++){
var _c12=new dojo.dnd.HtmlDragSource(row,type);
row.style.cursor="move";
}
}
if(this.select!="$no$"){
var row;
var _c13=this;
for(var idx=0;row=dojo.byId(this.id+"."+idx);idx++){
var _c14=row.getAttribute("id")+"."+this.select+":input";
var _c15=dojo.byId(_c14);
if(!_c15){
throw "No select input found for row '"+row.getAttribute("id")+"'";
}
if(_c15.checked){
dojo.html.prependClass(row,"forms-row-selected");
}
(function(){
var _c16=idx;
var _c17=row;
dojo.event.connect(row,"onclick",function(e){
_c13.selectRow(e,_c17,_c16);
});
dojo.event.connect(row,"onmouseover",function(e){
dojo.html.prependClass(_c17,"forms-row-hover");
});
dojo.event.connect(row,"onmouseout",function(e){
dojo.html.removeClass(_c17,"forms-row-hover");
});
})();
}
}
},afterInsert:function(e,_c1c,_c1d){
var _c1e=e.dragObject.domNode.getAttribute("id").split(".");
var _c1f=parseInt(_c1e[_c1e.length-1]);
_c1e=_c1c.getAttribute("id").split(".");
var _c20=parseInt(_c1e[_c1e.length-1]);
if(_c1d=="after"){
_c20++;
}
if(_c20==_c1f||_c20==_c1f+1){
return;
}
var form=cocoon.forms.getForm(this.domNode);
var _c22={};
_c22[this.id+".action"]="move";
_c22[this.id+".from"]=_c1f;
_c22[this.id+".before"]=_c20;
dojo.widget.byId(form.getAttribute("dojoWidgetId")).submit(this.id,_c22);
},isValidEvent:function(e){
var elt=dojo.html.getEventTarget(e);
if(!elt){
return true;
}
if(elt.onclick){
return false;
}
var name=elt.tagName.toLowerCase();
return (name!="input"&&name!="a");
},selectRow:function(e,row,idx){
if(this.isValidEvent(e)){
var _c29=dojo.byId(row.getAttribute("id")+"."+this.select+":input");
_c29.checked=_c29.checked?false:true;
(_c29.checked?dojo.html.prependClass:dojo.html.removeClass)(row,"forms-row-selected");
}
}});
dojo.widget.tags.addParseTreeHandler("dojo:CFormsRepeater");
dojo.widget.manager.registerWidgetPackage("cocoon.forms");
dojo.provide("cocoon.forms.CFormsDragAndDropRepeater");
cocoon.forms.CFormsDragAndDropRepeater=function(){
cocoon.forms.CFormsRepeater.call(this);
};
dojo.inherits(cocoon.forms.CFormsDragAndDropRepeater,cocoon.forms.CFormsRepeater);
dojo.lang.extend(cocoon.forms.CFormsDragAndDropRepeater,{widgetType:"CFormsDragAndDropRepeater",getDndAction:function(){
var _c2a=this.domNode.getAttribute("dnd-action");
dojo.debug("getDndAction: action="+_c2a);
return _c2a;
},getType:function(){
var type=this.domNode.getAttribute("dnd-id");
if(type==null){
type=this.id;
}
return "cforms-"+type;
},buildRendering:function(args,_c2d,_c2e){
this.domNode=_c2d["dojo:"+this.widgetType.toLowerCase()].nodeRef;
this.id=this.domNode.getAttribute("id");
if(!this.orderable&&this.select=="none"){
dojo.debug(this.widgetType+" '"+this.id+"' is not orderable nor selectable");
}
if(this.orderable){
var _c2f=dojo.byId(this.id+".0");
if(!_c2f){
return;
}
if(_c2f.tagName.toLowerCase()=="tr"&&_c2f.parentNode.tagName.toLowerCase()!="tbody"){
throw this.widgetType+" requires TR's to be in a TBODY (check '"+this.id+"')";
}
var type=this.getType();
var _c31=new dojo.dnd.HtmlDropTarget(_c2f.parentNode,[type]);
_c31.createDropIndicator=function(){
this.dropIndicator=document.createElement("div");
this.dropIndicator.className="forms-dropIndicator";
with(this.dropIndicator.style){
position="absolute";
zIndex=1;
width=dojo.style.getInnerWidth(this.domNode)+"px";
left=dojo.style.getAbsoluteX(this.domNode)+"px";
}
};
dojo.event.connect("before",_c31,"insert",this,"beforeInsert");
dojo.event.connect(_c31,"insert",this,"afterInsert");
var row;
for(var idx=0;row=dojo.byId(this.id+"."+idx);idx++){
row.setAttribute("dndType","repeaterRow");
row.setAttribute("dndRepeaterId",this.id);
row.setAttribute("dndRowIndex",idx);
var _c34=new dojo.dnd.HtmlDragSource(row,type);
row.style.cursor="move";
}
}
if(this.select!="$no$"){
var row;
var _c35=this;
for(var idx=0;row=dojo.byId(this.id+"."+idx);idx++){
var _c36=row.getAttribute("id")+"."+this.select+":input";
var _c37=dojo.byId(_c36);
if(!_c37){
throw "No select input found for row '"+row.getAttribute("id")+"'";
}
if(_c37.checked){
dojo.html.prependClass(row,"forms-row-selected");
}
(function(){
var _c38=idx;
var _c39=row;
dojo.event.connect(row,"onclick",function(e){
_c35.selectRow(e,_c39,_c38);
});
dojo.event.connect(row,"onmouseover",function(e){
dojo.html.prependClass(_c39,"forms-row-hover");
});
dojo.event.connect(row,"onmouseout",function(e){
dojo.html.removeClass(_c39,"forms-row-hover");
});
})();
}
}
},beforeInsert:function(e,_c3e,_c3f){
if(this.keepSourceInPlace(e,_c3e,_c3f)){
e.dragObject.domNode=e.dragObject.domNode.cloneNode(true);
}
},keepSourceInPlace:function(e,_c41,_c42){
var _c43=e.dragObject.domNode.getAttribute("id").split(".");
var _c44=_c43[0];
return _c44!=this.id;
},makeDragSource:function(e){
var _c46=this;
var _c47={_init:function(e){
this.e=e;
this.sourceRowIndex=e.dragObject.domNode.getAttribute("dndRowIndex");
this.sourceRepeaterId=e.dragObject.domNode.getAttribute("dndRepeaterId");
},getRowIdx:function(){
return this.sourceRowIndex;
},getRepeaterId:function(){
return this.sourceRepeaterId;
},isRepeater:function(){
return e.dragObject.domNode.getAttribute("dndType")=="repeaterRow";
},makeParameters:function(){
var res={};
res[_c46.id+".sourceRowIndex"]=this.sourceRowIndex;
res[_c46.id+".sourceRepeaterId"]=this.sourceRepeaterId;
return res;
}};
_c47._init(e);
return _c47;
},afterInsert:function(e,_c4b,_c4c){
var _c4d=_c4b.getAttribute("dndRowIndex");
if(_c4c=="after"){
_c4d++;
}
var _c4e=this.makeDragSource(e);
if(_c4e.isRepeater(e)){
var _c4f=_c4e.getRowIdx();
var _c50=_c4e.getRepeaterId();
dojo.debug("afterInsert: sourceRepeaterId="+_c50);
dojo.debug("afterInsert: sourceRowIndex="+_c4f);
}
dojo.debug("afterInsert: targetRepeaterId="+this.id);
dojo.debug("afterInsert: targetRowIndex="+_c4d);
var _c51=_c4e.makeParameters();
_c51[this.id+".before"]=_c4d;
_c51["dndTarget.id"]=this.id;
if(_c50==this.id){
if(_c4d==_c4f||_c4d==_c4f+1){
return;
}
var form=cocoon.forms.getForm(this.domNode);
_c51[this.id+".action"]="move";
_c51[this.id+".from"]=_c4f;
dojo.widget.byId(form.getAttribute("dojoWidgetId")).submit(this.id,_c51);
}else{
if(this.getDndAction()){
var form=cocoon.forms.getForm(this.domNode);
var _c53=dojo.widget.byId(form.getAttribute("dojoWidgetId"));
this.dndAction(_c53,_c4e,_c4d,_c51);
}
}
},dndAction:function(_c54,_c55,_c56,_c57){
_c54.submit(this.getDndAction(),_c57);
}});
dojo.widget.tags.addParseTreeHandler("dojo:CFormsDragAndDropRepeater");
dojo.widget.manager.registerWidgetPackage("cocoon.forms");
dojo.provide("dojo.widget.ComboBox");
dojo.widget.incrementalComboBoxDataProvider=function(url,_c59,_c5a){
this.searchUrl=url;
this.inFlight=false;
this.activeRequest=null;
this.allowCache=false;
this.cache={};
this.init=function(cbox){
this.searchUrl=cbox.dataUrl;
};
this.addToCache=function(_c5c,data){
if(this.allowCache){
this.cache[_c5c]=data;
}
};
this.startSearch=function(_c5e,type,_c60){
if(this.inFlight){
}
var tss=encodeURIComponent(_c5e);
var _c62=dojo.string.paramString(this.searchUrl,{"searchString":tss});
var _c63=this;
var _c64=dojo.io.bind({url:_c62,method:"get",mimetype:"text/json",load:function(type,data,evt){
_c63.inFlight=false;
if(!dojo.lang.isArray(data)){
var _c68=[];
for(var key in data){
_c68.push([data[key],key]);
}
data=_c68;
}
_c63.addToCache(_c5e,data);
_c63.provideSearchResults(data);
}});
this.inFlight=true;
};
};
dojo.widget.ComboBoxDataProvider=function(_c6a,_c6b,_c6c){
this.data=[];
this.searchTimeout=500;
this.searchLimit=30;
this.searchType="STARTSTRING";
this.caseSensitive=false;
this._lastSearch="";
this._lastSearchResults=null;
this.init=function(cbox,node){
if(!dojo.string.isBlank(cbox.dataUrl)){
this.getData(cbox.dataUrl);
}else{
if((node)&&(node.nodeName.toLowerCase()=="select")){
var opts=node.getElementsByTagName("option");
var ol=opts.length;
var data=[];
for(var x=0;x<ol;x++){
var _c73=[new String(opts[x].innerHTML),new String(opts[x].value)];
data.push(_c73);
if(opts[x].selected){
cbox.setAllValues(_c73[0],_c73[1]);
}
}
this.setData(data);
}
}
};
this.getData=function(url){
dojo.io.bind({url:url,load:dojo.lang.hitch(this,function(type,data,evt){
if(!dojo.lang.isArray(data)){
var _c78=[];
for(var key in data){
_c78.push([data[key],key]);
}
data=_c78;
}
this.setData(data);
}),mimetype:"text/json"});
};
this.startSearch=function(_c7a,type,_c7c){
this._preformSearch(_c7a,type,_c7c);
};
this._preformSearch=function(_c7d,type,_c7f){
var st=type||this.searchType;
var ret=[];
if(!this.caseSensitive){
_c7d=_c7d.toLowerCase();
}
for(var x=0;x<this.data.length;x++){
if((!_c7f)&&(ret.length>=this.searchLimit)){
break;
}
var _c83=new String((!this.caseSensitive)?this.data[x][0].toLowerCase():this.data[x][0]);
if(_c83.length<_c7d.length){
continue;
}
if(st=="STARTSTRING"){
if(_c7d==_c83.substr(0,_c7d.length)){
ret.push(this.data[x]);
}
}else{
if(st=="SUBSTRING"){
if(_c83.indexOf(_c7d)>=0){
ret.push(this.data[x]);
}
}else{
if(st=="STARTWORD"){
var idx=_c83.indexOf(_c7d);
if(idx==0){
ret.push(this.data[x]);
}
if(idx<=0){
continue;
}
var _c85=false;
while(idx!=-1){
if(" ,/(".indexOf(_c83.charAt(idx-1))!=-1){
_c85=true;
break;
}
idx=_c83.indexOf(_c7d,idx+1);
}
if(!_c85){
continue;
}else{
ret.push(this.data[x]);
}
}
}
}
}
this.provideSearchResults(ret);
};
this.provideSearchResults=function(_c86){
};
this.addData=function(_c87){
this.data=this.data.concat(_c87);
};
this.setData=function(_c88){
this.data=_c88;
};
if(_c6a){
this.setData(_c6a);
}
};
dojo.declare("dojo.widget.ComboBox",null,{widgetType:"ComboBox",isContainer:false,forceValidOption:false,searchType:"stringstart",dataProvider:null,startSearch:function(_c89){
},openResultList:function(_c8a){
},clearResultList:function(){
},hideResultList:function(){
},selectNextResult:function(){
},selectPrevResult:function(){
},setSelectedResult:function(){
}});
dojo.provide("dojo.widget.html.stabile");
dojo.widget.html.stabile={_sqQuotables:new RegExp("([\\\\'])","g"),_depth:0,_recur:false,depthLimit:2};
dojo.widget.html.stabile.getState=function(id){
dojo.widget.html.stabile.setup();
return dojo.widget.html.stabile.widgetState[id];
};
dojo.widget.html.stabile.setState=function(id,_c8d,_c8e){
dojo.widget.html.stabile.setup();
dojo.widget.html.stabile.widgetState[id]=_c8d;
if(_c8e){
dojo.widget.html.stabile.commit(dojo.widget.html.stabile.widgetState);
}
};
dojo.widget.html.stabile.setup=function(){
if(!dojo.widget.html.stabile.widgetState){
var text=dojo.widget.html.stabile.getStorage().value;
dojo.widget.html.stabile.widgetState=text?dj_eval("("+text+")"):{};
}
};
dojo.widget.html.stabile.commit=function(_c90){
dojo.widget.html.stabile.getStorage().value=dojo.widget.html.stabile.description(_c90);
};
dojo.widget.html.stabile.description=function(v,_c92){
var _c93=dojo.widget.html.stabile._depth;
var _c94=function(){
return this.description(this,true);
};
try{
if(v===void (0)){
return "undefined";
}
if(v===null){
return "null";
}
if(typeof (v)=="boolean"||typeof (v)=="number"||v instanceof Boolean||v instanceof Number){
return v.toString();
}
if(typeof (v)=="string"||v instanceof String){
var v1=v.replace(dojo.widget.html.stabile._sqQuotables,"\\$1");
v1=v1.replace(/\n/g,"\\n");
v1=v1.replace(/\r/g,"\\r");
return "'"+v1+"'";
}
if(v instanceof Date){
return "new Date("+d.getFullYear+","+d.getMonth()+","+d.getDate()+")";
}
var d;
if(v instanceof Array||v.push){
if(_c93>=dojo.widget.html.stabile.depthLimit){
return "[ ... ]";
}
d="[";
var _c97=true;
dojo.widget.html.stabile._depth++;
for(var i=0;i<v.length;i++){
if(_c97){
_c97=false;
}else{
d+=",";
}
d+=arguments.callee(v[i],_c92);
}
return d+"]";
}
if(v.constructor==Object||v.toString==_c94){
if(_c93>=dojo.widget.html.stabile.depthLimit){
return "{ ... }";
}
if(typeof (v.hasOwnProperty)!="function"&&v.prototype){
throw new Error("description: "+v+" not supported by script engine");
}
var _c97=true;
d="{";
dojo.widget.html.stabile._depth++;
for(var key in v){
if(v[key]==void (0)||typeof (v[key])=="function"){
continue;
}
if(_c97){
_c97=false;
}else{
d+=", ";
}
var kd=key;
if(!kd.match(/^[a-zA-Z_][a-zA-Z0-9_]*$/)){
kd=arguments.callee(key,_c92);
}
d+=kd+": "+arguments.callee(v[key],_c92);
}
return d+"}";
}
if(_c92){
if(dojo.widget.html.stabile._recur){
var _c9b=Object.prototype.toString;
return _c9b.apply(v,[]);
}else{
dojo.widget.html.stabile._recur=true;
return v.toString();
}
}else{
throw new Error("Unknown type: "+v);
return "'unknown'";
}
}
finally{
dojo.widget.html.stabile._depth=_c93;
}
};
dojo.widget.html.stabile.getStorage=function(){
if(dojo.widget.html.stabile.dataField){
return dojo.widget.html.stabile.dataField;
}
var form=document.forms._dojo_form;
return dojo.widget.html.stabile.dataField=form?form.stabile:{value:""};
};
dojo.provide("dojo.widget.html.ComboBox");
dojo.widget.defineWidget("dojo.widget.html.ComboBox",[dojo.widget.HtmlWidget,dojo.widget.ComboBox],{autoComplete:true,formInputName:"",name:"",textInputNode:null,comboBoxValue:null,comboBoxSelectionValue:null,optionsListWrapper:null,optionsListNode:null,downArrowNode:null,cbTableNode:null,searchTimer:null,searchDelay:100,dataUrl:"",fadeTime:200,maxListLength:8,mode:"local",selectedResult:null,_highlighted_option:null,_prev_key_backspace:false,_prev_key_esc:false,_result_list_open:false,_gotFocus:false,_mouseover_list:false,dataProviderClass:"dojo.widget.ComboBoxDataProvider",templatePath:dojo.uri.dojoUri("src/widget/templates/HtmlComboBox.html"),templateCssPath:dojo.uri.dojoUri("src/widget/templates/HtmlComboBox.css"),setValue:function(_c9d){
this.comboBoxValue.value=_c9d;
if(this.textInputNode.value!=_c9d){
this.textInputNode.value=_c9d;
}
dojo.widget.html.stabile.setState(this.widgetId,this.getState(),true);
},getValue:function(){
return this.comboBoxValue.value;
},getState:function(){
return {value:this.getValue()};
},setState:function(_c9e){
this.setValue(_c9e.value);
},getCaretPos:function(_c9f){
if(dojo.lang.isNumber(_c9f.selectionStart)){
return _c9f.selectionStart;
}else{
if(dojo.render.html.ie){
var tr=document.selection.createRange().duplicate();
var ntr=_c9f.createTextRange();
tr.move("character",0);
ntr.move("character",0);
try{
ntr.setEndPoint("EndToEnd",tr);
return String(ntr.text).replace(/\r/g,"").length;
}
catch(e){
return 0;
}
}
}
},setCaretPos:function(_ca2,_ca3){
_ca3=parseInt(_ca3);
this.setSelectedRange(_ca2,_ca3,_ca3);
},setSelectedRange:function(_ca4,_ca5,end){
if(!end){
end=_ca4.value.length;
}
if(_ca4.setSelectionRange){
_ca4.focus();
_ca4.setSelectionRange(_ca5,end);
}else{
if(_ca4.createTextRange){
var _ca7=_ca4.createTextRange();
with(_ca7){
collapse(true);
moveEnd("character",end);
moveStart("character",_ca5);
select();
}
}else{
_ca4.value=_ca4.value;
_ca4.blur();
_ca4.focus();
var dist=parseInt(_ca4.value.length)-end;
var _ca9=String.fromCharCode(37);
var tcc=_ca9.charCodeAt(0);
for(var x=0;x<dist;x++){
var te=document.createEvent("KeyEvents");
te.initKeyEvent("keypress",true,true,null,false,false,false,false,tcc,tcc);
_ca4.dispatchEvent(te);
}
}
}
},_handleKeyEvents:function(evt){
if(evt.ctrlKey||evt.altKey){
return;
}
this._prev_key_backspace=false;
this._prev_key_esc=false;
var k=dojo.event.browser.keys;
var _caf=true;
var _cb0=evt.keyCode;
if(_cb0==0&&evt.charCode==k.KEY_SPACE){
_cb0=k.KEY_SPACE;
}
switch(_cb0){
case k.KEY_DOWN_ARROW:
if(!this._result_list_open){
this.startSearchFromInput();
}
this.highlightNextOption();
dojo.event.browser.stopEvent(evt);
return;
case k.KEY_UP_ARROW:
this.highlightPrevOption();
dojo.event.browser.stopEvent(evt);
return;
case k.KEY_ENTER:
if(this._result_list_open){
dojo.event.browser.stopEvent(evt);
}
case k.KEY_TAB:
if(!this.autoComplete&&this._result_list_open&&this._highlighted_option){
dojo.event.browser.stopEvent(evt);
this.selectOption({"target":this._highlighted_option,"noHide":true});
this.setSelectedRange(this.textInputNode,this.textInputNode.value.length,null);
}else{
this.selectOption();
return;
}
break;
case k.KEY_SPACE:
if(this._result_list_open&&this._highlighted_option){
dojo.event.browser.stopEvent(evt);
this.selectOption();
this.hideResultList();
return;
}
break;
case k.KEY_ESCAPE:
this.hideResultList();
this._prev_key_esc=true;
return;
case k.KEY_BACKSPACE:
this._prev_key_backspace=true;
if(!this.textInputNode.value.length){
this.setAllValues("","");
this.hideResultList();
_caf=false;
}
break;
case k.KEY_RIGHT_ARROW:
case k.KEY_LEFT_ARROW:
case k.KEY_SHIFT:
_caf=false;
break;
default:
if(evt.charCode==0){
_caf=false;
}
}
if(this.searchTimer){
clearTimeout(this.searchTimer);
}
if(_caf){
this.blurOptionNode();
this.searchTimer=setTimeout(dojo.lang.hitch(this,this.startSearchFromInput),this.searchDelay);
}
},onKeyDown:function(evt){
if(!document.createEvent){
this._handleKeyEvents(evt);
}
},onKeyPress:function(evt){
if(document.createEvent){
this._handleKeyEvents(evt);
}
},onKeyUp:function(evt){
this.setValue(this.textInputNode.value);
},setSelectedValue:function(_cb4){
this.comboBoxSelectionValue.value=_cb4;
},setAllValues:function(_cb5,_cb6){
this.setValue(_cb5);
this.setSelectedValue(_cb6);
},scrollIntoView:function(){
var node=this._highlighted_option;
var _cb8=this.optionsListNode;
if(dojo.render.html.ie||dojo.render.html.mozilla){
node.scrollIntoView(false);
}else{
var _cb9=_cb8.scrollTop+dojo.style.getInnerHeight(_cb8);
var _cba=node.offsetTop+dojo.style.getOuterHeight(node);
if(_cb9<_cba){
_cb8.scrollTop+=(_cba-_cb9);
}else{
if(_cb8.scrollTop>node.offsetTop){
_cb8.scrollTop-=(_cb8.scrollTop-node.offsetTop);
}
}
}
},focusOptionNode:function(node){
if(this._highlighted_option!=node){
this.blurOptionNode();
this._highlighted_option=node;
dojo.html.addClass(this._highlighted_option,"dojoComboBoxItemHighlight");
}
},blurOptionNode:function(){
if(this._highlighted_option){
dojo.html.removeClass(this._highlighted_option,"dojoComboBoxItemHighlight");
this._highlighted_option=null;
}
},highlightNextOption:function(){
if((!this._highlighted_option)||!this._highlighted_option.parentNode){
this.focusOptionNode(this.optionsListNode.firstChild);
}else{
if(this._highlighted_option.nextSibling){
this.focusOptionNode(this._highlighted_option.nextSibling);
}
}
this.scrollIntoView();
},highlightPrevOption:function(){
if(this._highlighted_option&&this._highlighted_option.previousSibling){
this.focusOptionNode(this._highlighted_option.previousSibling);
}else{
this._highlighted_option=null;
this.hideResultList();
return;
}
this.scrollIntoView();
},itemMouseOver:function(evt){
this.focusOptionNode(evt.target);
dojo.html.addClass(this._highlighted_option,"dojoComboBoxItemHighlight");
},itemMouseOut:function(evt){
this.blurOptionNode();
},fillInTemplate:function(args,frag){
this.comboBoxValue.name=this.name;
this.comboBoxSelectionValue.name=this.name+"_selected";
var _cc0=this.getFragNodeRef(frag);
dojo.html.copyStyle(this.domNode,_cc0);
var _cc1;
if(this.mode=="remote"){
_cc1=dojo.widget.incrementalComboBoxDataProvider;
}else{
if(typeof this.dataProviderClass=="string"){
_cc1=dojo.evalObjPath(this.dataProviderClass);
}else{
_cc1=this.dataProviderClass;
}
}
this.dataProvider=new _cc1();
this.dataProvider.init(this,this.getFragNodeRef(frag));
this.optionsIframe=new dojo.html.BackgroundIframe(this.optionsListWrapper);
this.optionsIframe.size([0,0,0,0]);
},focus:function(){
this.tryFocus();
},openResultList:function(_cc2){
this.clearResultList();
if(!_cc2.length){
this.hideResultList();
}
if((this.autoComplete)&&(_cc2.length)&&(!this._prev_key_backspace)&&(this.textInputNode.value.length>0)){
var cpos=this.getCaretPos(this.textInputNode);
if((cpos+1)>this.textInputNode.value.length){
this.textInputNode.value+=_cc2[0][0].substr(cpos);
this.setSelectedRange(this.textInputNode,cpos,this.textInputNode.value.length);
}
}
var even=true;
while(_cc2.length){
var tr=_cc2.shift();
if(tr){
var td=document.createElement("div");
td.appendChild(document.createTextNode(tr[0]));
td.setAttribute("resultName",tr[0]);
td.setAttribute("resultValue",tr[1]);
td.className="dojoComboBoxItem "+((even)?"dojoComboBoxItemEven":"dojoComboBoxItemOdd");
even=(!even);
this.optionsListNode.appendChild(td);
dojo.event.connect(td,"onmouseover",this,"itemMouseOver");
dojo.event.connect(td,"onmouseout",this,"itemMouseOut");
}
}
this.showResultList();
},onFocusInput:function(){
this._hasFocus=true;
},onBlurInput:function(){
this._hasFocus=false;
this._handleBlurTimer(true,500);
},_handleBlurTimer:function(_cc7,_cc8){
if(this.blurTimer&&(_cc7||_cc8)){
clearTimeout(this.blurTimer);
}
if(_cc8){
this.blurTimer=dojo.lang.setTimeout(this,"checkBlurred",_cc8);
}
},_onMouseOver:function(evt){
if(!this._mouseover_list){
this._handleBlurTimer(true,0);
this._mouseover_list=true;
}
},_onMouseOut:function(evt){
var _ccb=evt.relatedTarget;
if(!_ccb||_ccb.parentNode!=this.optionsListNode){
this._mouseover_list=false;
this._handleBlurTimer(true,100);
this.tryFocus();
}
},_isInputEqualToResult:function(_ccc){
input=this.textInputNode.value;
if(!this.dataProvider.caseSensitive){
input=input.toLowerCase();
_ccc=_ccc.toLowerCase();
}
return (input==_ccc);
},_isValidOption:function(){
tgt=dojo.dom.firstElement(this.optionsListNode);
isValidOption=false;
while(!isValidOption&&tgt){
if(this._isInputEqualToResult(tgt.getAttribute("resultName"))){
isValidOption=true;
}else{
tgt=dojo.dom.nextElement(tgt);
}
}
return isValidOption;
},checkBlurred:function(){
if(!this._hasFocus&&!this._mouseover_list){
this.hideResultList();
if(!this.textInputNode.value.length){
this.setAllValues("","");
return;
}
isValidOption=this._isValidOption();
if(this.forceValidOption&&!isValidOption){
this.setAllValues("","");
return;
}
if(!isValidOption){
this.setSelectedValue("");
}
}
},sizeBackgroundIframe:function(){
var w=dojo.style.getOuterWidth(this.optionsListNode);
var h=dojo.style.getOuterHeight(this.optionsListNode);
if(w==0||h==0){
dojo.lang.setTimeout(this,"sizeBackgroundIframe",100);
return;
}
if(this._result_list_open){
this.optionsIframe.size([0,0,w,h]);
}
},selectOption:function(evt){
var tgt=null;
if(!evt){
evt={target:this._highlighted_option};
}
if(!dojo.dom.isDescendantOf(evt.target,this.optionsListNode)){
if(!this.textInputNode.value.length){
return;
}
tgt=dojo.dom.firstElement(this.optionsListNode);
if(!tgt||!this._isInputEqualToResult(tgt.getAttribute("resultName"))){
return;
}
}else{
tgt=evt.target;
}
while((tgt.nodeType!=1)||(!tgt.getAttribute("resultName"))){
tgt=tgt.parentNode;
if(tgt===document.body){
return false;
}
}
this.textInputNode.value=tgt.getAttribute("resultName");
this.selectedResult=[tgt.getAttribute("resultName"),tgt.getAttribute("resultValue")];
this.setAllValues(tgt.getAttribute("resultName"),tgt.getAttribute("resultValue"));
if(!evt.noHide){
this.hideResultList();
this.setSelectedRange(this.textInputNode,0,null);
}
this.tryFocus();
},clearResultList:function(){
var oln=this.optionsListNode;
while(oln.firstChild){
dojo.event.disconnect(oln.firstChild,"onmouseover",this,"itemMouseOver");
dojo.event.disconnect(oln.firstChild,"onmouseout",this,"itemMouseOut");
oln.removeChild(oln.firstChild);
}
},hideResultList:function(){
if(this._result_list_open){
this._result_list_open=false;
this.optionsIframe.size([0,0,0,0]);
dojo.lfx.fadeHide(this.optionsListNode,this.fadeTime).play();
}
},showResultList:function(){
var _cd2=this.optionsListNode.childNodes;
if(_cd2.length){
var _cd3=this.maxListLength;
if(_cd2.length<_cd3){
_cd3=_cd2.length;
}
with(this.optionsListNode.style){
display="";
height=((_cd3)?(dojo.style.getOuterHeight(_cd2[0])*_cd3):0)+"px";
width=dojo.html.getOuterWidth(this.cbTableNode)-2+"px";
}
if(!this._result_list_open){
dojo.html.setOpacity(this.optionsListNode,0);
dojo.lfx.fadeIn(this.optionsListNode,this.fadeTime).play();
}
this._iframeTimer=dojo.lang.setTimeout(this,"sizeBackgroundIframe",200);
this._result_list_open=true;
}else{
this.hideResultList();
}
},handleArrowClick:function(){
this._handleBlurTimer(true,0);
this.tryFocus();
if(this._result_list_open){
this.hideResultList();
}else{
this.startSearchFromInput();
}
},tryFocus:function(){
try{
this.textInputNode.focus();
}
catch(e){
}
},startSearchFromInput:function(){
this.startSearch(this.textInputNode.value);
},postCreate:function(){
dojo.event.connect(this,"startSearch",this.dataProvider,"startSearch");
dojo.event.connect(this.dataProvider,"provideSearchResults",this,"openResultList");
dojo.event.connect(this.textInputNode,"onblur",this,"onBlurInput");
dojo.event.connect(this.textInputNode,"onfocus",this,"onFocusInput");
var s=dojo.widget.html.stabile.getState(this.widgetId);
if(s){
this.setState(s);
}
}});
dojo.provide("cocoon.forms.CFormsSuggest");
cocoon.forms.CFormsSuggest=function(){
dojo.widget.html.ComboBox.call(this);
this.widgetType="CFormsSuggest";
};
dojo.inherits(cocoon.forms.CFormsSuggest,dojo.widget.html.ComboBox);
dojo.lang.extend(cocoon.forms.CFormsSuggest,{fillInTemplate:function(args,frag){
this.mode="remote";
var node=frag["dojo:"+this.widgetType.toLowerCase()]["nodeRef"];
var form=cocoon.forms.getForm(node);
var _cd9=form["continuation-id"].value;
if(!_cd9){
throw "Cannot find continuation Id";
}
if(!this.dataUrl||this.dataUrl==""){
this.dataUrl="_cocoon/forms/suggest?widget="+node.getAttribute("name")+"&continuation-id="+_cd9+"&filter=%{searchString}";
}
dojo.widget.html.ComboBox.prototype.fillInTemplate.apply(this,arguments);
if(node.value){
this.getData(this,"_cocoon/forms/suggest?widget="+node.getAttribute("name")+"&continuation-id="+_cd9+"&filter="+node.value+"&phase=init",node);
}else{
this.setValue(node.getAttribute("suggestion")?node.getAttribute("suggestion"):node.value);
this.setSelectedValue(node.value);
}
},getData:function(_cda,url,node){
dojo.io.bind({url:url,load:dojo.lang.hitch(this,function(type,data,evt){
if(!dojo.lang.isArray(data)){
var _ce0=[];
for(var key in data){
_ce0.push([data[key],key]);
}
data=_ce0;
}
_cda.setValue(data[0][0]);
_cda.setSelectedValue(node.value);
}),mimetype:"text/json"});
}});
dojo.widget.tags.addParseTreeHandler("dojo:CFormsSuggest");
dojo.widget.manager.registerWidgetPackage("cocoon.forms");
dojo.provide("dojo.widget.InlineEditBox");
dojo.provide("dojo.widget.html.InlineEditBox");
dojo.widget.tags.addParseTreeHandler("dojo:inlineeditbox");
dojo.widget.html.InlineEditBox=function(){
dojo.widget.HtmlWidget.call(this);
this.history=[];
};
dojo.inherits(dojo.widget.html.InlineEditBox,dojo.widget.HtmlWidget);
dojo.lang.extend(dojo.widget.html.InlineEditBox,{templatePath:dojo.uri.dojoUri("src/widget/templates/HtmlInlineEditBox.html"),templateCssPath:dojo.uri.dojoUri("src/widget/templates/HtmlInlineEditBox.css"),widgetType:"InlineEditBox",form:null,editBox:null,edit:null,text:null,textarea:null,submitButton:null,cancelButton:null,mode:"text",minWidth:100,minHeight:200,editing:false,textValue:"",defaultText:"",doFade:false,onSave:function(_ce2,_ce3){
},onUndo:function(_ce4){
},postCreate:function(args,frag){
this.editable=this.getFragNodeRef(frag);
dojo.dom.insertAfter(this.editable,this.form);
dojo.event.connect(this.editable,"onmouseover",this,"mouseover");
dojo.event.connect(this.editable,"onmouseout",this,"mouseout");
dojo.event.connect(this.editable,"onclick",this,"beginEdit");
this.textValue=dojo.string.trim(this.editable.innerHTML);
if(dojo.string.trim(this.textValue).length==0){
this.editable.innerHTML=this.defaultText;
}
},mouseover:function(e){
if(!this.editing){
dojo.html.addClass(this.editable,"editableRegion");
if(this.mode=="textarea"){
dojo.html.addClass(this.editable,"editableTextareaRegion");
}
}
},mouseout:function(e){
if(!this.editing){
dojo.html.removeClass(this.editable,"editableRegion");
dojo.html.removeClass(this.editable,"editableTextareaRegion");
}
},beginEdit:function(e){
if(this.editing){
return;
}
this.mouseout();
this.editing=true;
var ee=this[this.mode.toLowerCase()];
ee.value=dojo.string.trim(this.textValue);
ee.style.fontSize=dojo.style.getStyle(this.editable,"font-size");
ee.style.fontWeight=dojo.style.getStyle(this.editable,"font-weight");
ee.style.fontStyle=dojo.style.getStyle(this.editable,"font-style");
ee.style.width=Math.max(dojo.html.getInnerWidth(this.editable),this.minWidth)+"px";
if(this.mode.toLowerCase()=="textarea"){
ee.style.display="block";
ee.style.height=Math.max(dojo.html.getInnerHeight(this.editable),this.minHeight)+"px";
}else{
ee.style.display="";
}
this.form.style.display="";
this.editable.style.display="none";
ee.select();
this.submitButton.disabled=true;
},saveEdit:function(e){
e.preventDefault();
e.stopPropagation();
var ee=this[this.mode.toLowerCase()];
if((this.textValue!=ee.value)&&(dojo.string.trim(ee.value)!="")){
this.doFade=true;
this.history.push(this.textValue);
this.onSave(ee.value,this.textValue);
this.textValue=ee.value;
this.editable.innerHTML=this.textValue;
}else{
this.doFade=false;
}
this.finishEdit(e);
},cancelEdit:function(e){
if(!this.editing){
return false;
}
this.editing=false;
this.form.style.display="none";
this.editable.style.display="";
return true;
},finishEdit:function(e){
if(!this.cancelEdit(e)){
return;
}
if(this.doFade){
dojo.lfx.highlight(this.editable,dojo.graphics.color.hex2rgb("#ffc"),700).play(300);
}
this.doFade=false;
},setText:function(txt){
var tt=dojo.string.trim(txt);
this.textValue=tt;
this.editable.innerHTML=tt;
},undo:function(){
if(this.history.length>0){
var _cf1=this.history.pop();
this.editable.innerHTML=_cf1;
this.textValue=_cf1;
this.onUndo(_cf1);
}
},checkForValueChange:function(){
var ee=this[this.mode.toLowerCase()];
if((this.textValue!=ee.value)&&(dojo.string.trim(ee.value)!="")){
this.submitButton.disabled=false;
}
}});
dojo.provide("cocoon.forms.*");
dojo.provide("mindquarry.widget.AutoActiveField");
dojo.widget.tags.addParseTreeHandler("dojo:AutoActiveField");
dojo.widget.manager.registerWidgetPackage("mindquarry.widget");
mindquarry.widget.AutoActiveField=function(){
dojo.widget.DomWidget.call(this);
var _cf3=null;
};
dojo.inherits(mindquarry.widget.AutoActiveField,dojo.widget.DomWidget);
dojo.lang.extend(mindquarry.widget.AutoActiveField,{widgetType:"AutoActiveField",isContainer:true,buildRendering:function(args,_cf5,_cf6){
this.domNode=_cf5["dojo:"+this.widgetType.toLowerCase()].nodeRef;
this.cform=_cf6;
if(this.domNode.className.indexOf("use-parent-for-autoactive")>=0){
dojo.event.connect(this.domNode.parentNode,"ondblclick",this,"onClick");
}else{
dojo.event.connect(this.domNode,"ondblclick",this,"onClick");
}
if(this.domNode.className.indexOf("use-parent-id")>=0){
this.activateID=this.domNode.parentNode.id;
}else{
this.activateID=this.domNode.id;
}
},onClick:function(_cf7){
dojo.debug("onClick: domNode="+this.domNode+", activateID="+this.activateID);
if(this.domNode==null){
return true;
}
if(_cf7.target.href){
return false;
}else{
_cf7.preventDefault();
}
if(this.cform==null){
var form=cocoon.forms.getForm(this.domNode);
var _cf9=form.getAttribute("dojoWidgetId");
if(_cf9){
this.cform=dojo.widget.byId(_cf9);
}
}
if(this.cform!=null){
this.cform.submit("ductform.ductforms_activate",{activate:this.activateID});
}
return false;
}});
dojo.provide("mindquarry.widget.ChangePassword");
dojo.widget.tags.addParseTreeHandler("dojo:ChangePassword");
dojo.widget.manager.registerWidgetPackage("mindquarry.widget");
mindquarry.widget.ChangePassword=function(){
dojo.widget.DomWidget.call(this);
var _cfa=null;
};
dojo.inherits(mindquarry.widget.ChangePassword,dojo.widget.DomWidget);
dojo.lang.extend(mindquarry.widget.ChangePassword,{widgetType:"ChangePassword",isContainer:true,postCreate:function(_cfb){
changePasswordInBrowser();
}});
function changePasswordInBrowser(){
var _cfc="changePassword.newPassword:input";
var _cfd=document.getElementById(_cfc).value;
var _cfe=document.getElementById("userid").firstChild.nodeValue;
var _cff=document.getElementById("path-to-webapp-root").href;
var _d00=getHTTPObject();
_d00.open("get",_cff,false,"null","null");
_d00.send(null);
var _d01=getHTTPObject();
_d01.open("get",_cff,false,_cfe,_cfd);
_d01.send(null);
}
dojo.provide("dojo.widget.Select");
dojo.provide("dojo.widget.html.Select");
dojo.widget.defineWidget("dojo.widget.html.Select",dojo.widget.html.ComboBox,{widgetType:"Select",forceValidOption:true,setValue:function(_d02){
this.comboBoxValue.value=_d02;
dojo.widget.html.stabile.setState(this.widgetId,this.getState(),true);
},setLabel:function(_d03){
this.comboBoxSelectionValue.value=_d03;
if(this.textInputNode.value!=_d03){
this.textInputNode.value=_d03;
}
},getLabel:function(){
return this.comboBoxSelectionValue.value;
},getState:function(){
return {value:this.getValue(),label:this.getLabel()};
},onKeyUp:function(evt){
this.setLabel(this.textInputNode.value);
},setState:function(_d05){
this.setValue(_d05.value);
this.setLabel(_d05.label);
},setAllValues:function(_d06,_d07){
this.setValue(_d07);
this.setLabel(_d06);
}});
dojo.provide("mindquarry.widget.IconSelect");
dojo.widget.defineWidget("mindquarry.widget.IconSelect",dojo.widget.html.Select,{iconprefix:"",iconsuffix:".png",name:"",filter:true,size:22,openResultList:function(_d08){
this.clearResultList();
dojo.debug("got things to add to the list: "+_d08.length);
if(!_d08.length){
this.hideResultList();
}
if((this.autoComplete)&&(_d08.length)&&(!this._prev_key_backspace)&&(this.textInputNode.value.length>0)){
var cpos=this.getCaretPos(this.textInputNode);
if((cpos+1)>this.textInputNode.value.length){
this.textInputNode.value+=_d08[0][0].substr(cpos);
this.setSelectedRange(this.textInputNode,cpos,this.textInputNode.value.length);
}
}
var even=true;
while(_d08.length){
var tr=_d08.shift();
if(tr){
var td=document.createElement("div");
td.appendChild(document.createTextNode(tr[0]));
td.setAttribute("resultName",tr[0]);
td.setAttribute("resultValue",tr[1]);
td.className="mindquarrySelect dojoComboBoxItem "+((even)?"dojoComboBoxItemEven":"dojoComboBoxItemOdd");
td.style.backgroundImage="url("+this.iconprefix+"/"+this.size+"/"+this.name+"/"+tr[1]+this.iconsuffix+")";
td.style.backgroundRepeat="no-repeat";
td.style.paddingLeft=(this.size+2)+"px";
td.style.margin="1px";
even=(!even);
this.optionsListNode.appendChild(td);
dojo.event.connect(td,"onmouseover",this,"itemMouseOver");
dojo.event.connect(td,"onmouseout",this,"itemMouseOut");
}
}
this.showResultList();
},setAllValues:function(_d0d,_d0e){
dojo.debug("Calling = setAllValues : "+_d0d+" = "+_d0e);
this.setLabel(_d0d);
this.setValue(_d0e);
},handleArrowClick:function(){
this._handleBlurTimer(true,0);
this.tryFocus();
if(this._result_list_open){
this.hideResultList();
}else{
this.startSearch("");
}
}});
dojo.provide("mindquarry.widget.MindquarryDatePicker");
dojo.widget.tags.addParseTreeHandler("dojo:MindquarryDatePicker");
dojo.widget.manager.registerWidgetPackage("mindquarry.widget");
mindquarry.widget.MindquarryDatePicker=function(){
dojo.widget.html.DatePicker.call(this);
this.widgetType="MindquarryDatePicker";
};
dojo.inherits(mindquarry.widget.MindquarryDatePicker,dojo.widget.html.DatePicker);
dojo.lang.extend(mindquarry.widget.MindquarryDatePicker,{widgetType:"MindquarryDatePicker",isContainer:true,buildRendering:function(args,_d10,_d11){
this.domNode=_d10["dojo:"+this.widgetType.toLowerCase()].nodeRef;
this.cform=_d11;
dojo.event.connect(this.domNode,"onclick",this,"onClick");
},onClicj:function(_d12){
}});
dojo.provide("mindquarry.widget.normalizeName");
var mindquarry=mindquarry||{};
mindquarry.widget=mindquarry.widget||{};
mindquarry.widget.normalizeName=function(name){
var _d14="0123456789_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
var _d15=[];
name=name.split(" ").join("_");
for(var i=0;i<name.length;i++){
var ch=name[i];
if(_d14.indexOf(ch)!=-1){
_d15.push(ch);
}
}
return _d15.join("");
};
dojo.provide("mindquarry.widget.QuickSearch");
dojo.widget.defineWidget("mindquarry.widget.QuickSearch",dojo.widget.HtmlWidget,{widgetType:"QuickSearch",isContainer:false,url:"",maxheight:300,size:"20",searchButton:"Search",searchingStatus:"Searching ...",noResultsStatus:"Search finished without any results.",gotResultsStatus:"Search finished with %{count} results :",untitled:"untitled",templateString:"<div class=\"mindquarry-quicksearch\">"+"<div class=\"search-field\">"+"<form dojoAttachPoint=\"formNode\" dojoOnSubmit=\"searchClick;\" class=\"search_form\">"+"<span>"+"<input name=\"q\" size=\"${this.size}\" class=\"input_box\" dojoAttachPoint=\"widthNode\" autocomplete=\"off\"/>"+"<input name=\"wt\" value=\"mq\" type=\"hidden\"/>"+"<input name=\"fl\" value=\"score\" type=\"hidden\"/>"+"<input type=\"image\" src=\""+dojo.uri.dojoUri("search.png")+"\" value=\"${this.searchButton}\" class=\"btn\" title=\"Search entire system\"/>"+"</span>"+"</form>"+"<div class=\"result-popup\" dojoAttachPoint=\"popupNode\" >"+"<a dojoOnClick=\"closeClick;\" href=\"#\" style=\"background:url('../buttons/close.png');display:block;height:10px;width:10px;float:right;\"></a>"+"<span class=\"result-status\" dojoAttachPoint=\"resultStatus\" style=\"margin-bottom:8px;\"></span>"+"<div dojoAttachPoint=\"resultNode\"></div>"+"</div>"+"</div>",tableBeginHTML:"<table width=\"100%\" class=\"result-table\"><tbody>",tableEndHTML:"</tbody></table>",typeTemplate:"<tr valign=\"top\">"+"<td width=\"25%\" align=\"right\" class=\"task-column\">%{type}</td>"+"<td class=\"file-column\"><table width=\"100%\"><tbody>%{hits}</tbody></table></td>"+"</tr>",hitTemplate:"<tr valign=\"top\"><td width=\"32\"><div style=\"width:30px;height:12px;border:1px solid #666;position:relative;\">"+"<div style=\"background:#ddf;height:10px;width:%{score}%;margin:1px 0;padding:0;\"> </div>"+"<div style=\"position:relative;top:0;right:2px;margin:0;padding:0;color:#444;font-size:10px;\">%{score}</div>"+"</div></td>"+"<td><a href=\"%{uri}\">%{title}</a></td></tr>",formNode:null,widthNode:null,popupNode:null,resultNode:null,resultStatus:null,_busy:false,postCreate:function(){
dojo.html.hide(this.popupNode);
this.popupNode.style.width=dojo.style.getOuterWidth(this.widthNode)-11+"px";
},searchClick:function(evt){
evt.preventDefault();
dojo.debug("QuickSearch - starting search");
if(this._busy){
return;
}
this._busy=true;
this._setStatus(this.searchingStatus);
dojo.dom.removeChildren(this.resultNode);
dojo.html.show(this.popupNode);
dojo.io.bind({url:this.url,mimetype:"text/json",formNode:this.formNode,method:"get",handle:dojo.lang.hitch(this,function(type,data,evt){
if(type=="load"){
if(!data){
return;
}
this._update(data);
this._busy=false;
}else{
if(type=="error"){
dojo.debug("QuickSearch - status request failed");
}
}
})});
},closeClick:function(evt){
evt.preventDefault();
dojo.html.hide(this.popupNode);
},_setStatus:function(_d1d){
this.resultStatus.innerHTML=_d1d;
},_buildResults:function(docs){
var _d1f=[];
for(var type in docs){
var hits=[];
for(var hit in docs[type]){
hits.push(dojo.string.substituteParams(this.hitTemplate,{uri:docs[type][hit].location,title:docs[type][hit].title||this.untitled,score:docs[type][hit].score}));
}
_d1f.push(dojo.string.substituteParams(this.typeTemplate,{type:type,hits:hits.join("")}));
}
return this.tableBeginHTML+_d1f.join("")+this.tableEndHTML;
},_update:function(data){
dojo.debug("QuickSearch - got results: "+data.response.numFound);
if(data.response.numFound>0){
this._setStatus(dojo.string.substituteParams(this.gotResultsStatus,{count:data.response.numFound}));
this.resultNode.innerHTML=this._buildResults(data.response.docs);
if(dojo.style.getOuterHeight(this.resultNode)>this.maxheight){
this.popupNode.style.height=this.maxheight+"px";
}
}else{
this._setStatus(this.noResultsStatus);
}
}});
dojo.provide("dojo.widget.SortableTable");
dojo.widget.tags.addParseTreeHandler("dojo:sortableTable");
dojo.widget.SortableTable=function(){
dojo.widget.Widget.call(this);
this.widgetType="SortableTable";
this.isContainer=false;
this.enableMultipleSelect=false;
this.maximumNumberOfSelections=0;
this.enableAlternateRows=false;
this.minRows=0;
this.defaultDateFormat="%D";
this.data=[];
this.selected=[];
this.columns=[];
this.sortIndex=0;
this.sortDirection=0;
this.valueField="Id";
};
dojo.inherits(dojo.widget.SortableTable,dojo.widget.Widget);
dojo.provide("dojo.widget.html.SortableTable");
dojo.widget.html.SortableTable=function(){
dojo.widget.SortableTable.call(this);
dojo.widget.HtmlWidget.call(this);
this.headClass="";
this.tbodyClass="";
this.headerClass="";
this.headerSortUpClass="selected";
this.headerSortDownClass="selected";
this.rowClass="";
this.rowAlternateClass="alt";
this.rowSelectedClass="selected";
this.columnSelected="sorted-column";
};
dojo.inherits(dojo.widget.html.SortableTable,dojo.widget.HtmlWidget);
dojo.lang.extend(dojo.widget.html.SortableTable,{templatePath:null,templateCssPath:null,getTypeFromString:function(s){
var _d25=s.split("."),i=0,obj=dj_global;
do{
obj=obj[_d25[i++]];
}while(i<_d25.length&&obj);
return (obj!=dj_global)?obj:null;
},compare:function(o1,o2){
for(var p in o1){
if(!(p in o2)){
return false;
}
if(o1[p].valueOf()!=o2[p].valueOf()){
return false;
}
}
return true;
},isSelected:function(o){
for(var i=0;i<this.selected.length;i++){
if(this.compare(this.selected[i],o)){
return true;
}
}
return false;
},removeFromSelected:function(o){
var idx=-1;
for(var i=0;i<this.selected.length;i++){
if(this.compare(this.selected[i],o)){
idx=i;
break;
}
}
if(idx>=0){
this.selected.splice(idx,1);
}
},getSelection:function(){
return this.selected;
},getValue:function(){
var a=[];
for(var i=0;i<this.selected.length;i++){
if(this.selected[i][this.valueField]){
a.push(this.selected[i][this.valueField]);
}
}
return a.join();
},reset:function(){
this.columns=[];
this.data=[];
this.resetSelections(this.domNode.getElementsByTagName("tbody")[0]);
},resetSelections:function(body){
this.selected=[];
var idx=0;
var rows=body.getElementsByTagName("tr");
for(var i=0;i<rows.length;i++){
if(rows[i].parentNode==body){
rows[i].removeAttribute("selected");
if(this.enableAlternateRows&&idx%2==1){
rows[i].className=this.rowAlternateClass;
}else{
rows[i].className="";
}
idx++;
}
}
},getObjectFromRow:function(row){
var _d37=row.getElementsByTagName("td");
var o={};
for(var i=0;i<this.columns.length;i++){
if(this.columns[i].sortType=="__markup__"){
o[this.columns[i].getField()]=_d37[i].innerHTML;
}else{
var text=dojo.html.renderedTextContent(_d37[i]);
var val=new (this.columns[i].getType())(text);
o[this.columns[i].getField()]=val;
}
}
if(dojo.html.hasAttribute(row,"value")){
o[this.valueField]=dojo.html.getAttribute(row,"value");
}
return o;
},setSelectionByRow:function(row){
var o=this.getObjectFromRow(row);
var b=false;
for(var i=0;i<this.selected.length;i++){
if(this.compare(this.selected[i],o)){
b=true;
break;
}
}
if(!b){
this.selected.push(o);
}
},parseColumns:function(node){
this.reset();
var row=node.getElementsByTagName("tr")[0];
var _d42=row.getElementsByTagName("td");
if(_d42.length==0){
_d42=row.getElementsByTagName("th");
}
for(var i=0;i<_d42.length;i++){
var o={field:null,format:null,noSort:false,sortType:"String",dataType:String,sortFunction:null,label:null,align:"left",valign:"middle",getField:function(){
return this.field||this.label;
},getType:function(){
return this.dataType;
}};
if(dojo.html.hasAttribute(_d42[i],"align")){
o.align=dojo.html.getAttribute(_d42[i],"align");
}
if(dojo.html.hasAttribute(_d42[i],"valign")){
o.valign=dojo.html.getAttribute(_d42[i],"valign");
}
if(dojo.html.hasAttribute(_d42[i],"nosort")){
o.noSort=dojo.html.getAttribute(_d42[i],"nosort")=="true";
}
if(dojo.html.hasAttribute(_d42[i],"sortusing")){
var _d45=dojo.html.getAttribute(_d42[i],"sortusing");
var f=this.getTypeFromString(_d45);
if(f!=null&&f!=window&&typeof (f)=="function"){
o.sortFunction=f;
}
}
if(dojo.html.hasAttribute(_d42[i],"field")){
o.field=dojo.html.getAttribute(_d42[i],"field");
}
if(dojo.html.hasAttribute(_d42[i],"format")){
o.format=dojo.html.getAttribute(_d42[i],"format");
}
if(dojo.html.hasAttribute(_d42[i],"dataType")){
var _d47=dojo.html.getAttribute(_d42[i],"dataType");
if(_d47.toLowerCase()=="html"||_d47.toLowerCase()=="markup"){
o.sortType="__markup__";
o.noSort=true;
}else{
var type=this.getTypeFromString(_d47);
if(type){
o.sortType=_d47;
o.dataType=type;
}
}
}
o.label=dojo.html.renderedTextContent(_d42[i]);
this.columns.push(o);
if(dojo.html.hasAttribute(_d42[i],"sort")){
this.sortIndex=i;
var dir=dojo.html.getAttribute(_d42[i],"sort");
if(!isNaN(parseInt(dir))){
dir=parseInt(dir);
this.sortDirection=(dir!=0)?1:0;
}else{
this.sortDirection=(dir.toLowerCase()=="desc")?1:0;
}
}
}
},parseData:function(data){
this.data=[];
this.selected=[];
for(var i=0;i<data.length;i++){
var o={};
for(var j=0;j<this.columns.length;j++){
var _d4e=this.columns[j].getField();
if(this.columns[j].sortType=="__markup__"){
o[_d4e]=String(data[i][_d4e]);
}else{
var type=this.columns[j].getType();
var val=data[i][_d4e];
var t=this.columns[j].sortType.toLowerCase();
if(val){
o[_d4e]=new type(val);
}else{
o[_d4e]=new type();
}
}
}
if(data[i][this.valueField]&&!o[this.valueField]){
o[this.valueField]=data[i][this.valueField];
}
this.data.push(o);
}
},parseDataFromTable:function(_d52){
this.data=[];
this.selected=[];
var rows=_d52.getElementsByTagName("tr");
for(var i=0;i<rows.length;i++){
if(dojo.html.getAttribute(rows[i],"ignoreIfParsed")=="true"){
continue;
}
var o={};
var _d56=rows[i].getElementsByTagName("td");
for(var j=0;j<this.columns.length;j++){
var _d58=this.columns[j].getField();
if(this.columns[j].sortType=="__markup__"){
o[_d58]=_d56[j].innerHTML;
}else{
var type=this.columns[j].getType();
var val=dojo.html.renderedTextContent(_d56[j]);
if(val){
o[_d58]=new type(val);
}else{
o[_d58]=new type();
}
}
}
if(dojo.html.hasAttribute(rows[i],"value")&&!o[this.valueField]){
o[this.valueField]=dojo.html.getAttribute(rows[i],"value");
}
this.data.push(o);
if(dojo.html.getAttribute(rows[i],"selected")=="true"){
this.selected.push(o);
}
}
},showSelections:function(){
var body=this.domNode.getElementsByTagName("tbody")[0];
var rows=body.getElementsByTagName("tr");
var idx=0;
for(var i=0;i<rows.length;i++){
if(rows[i].parentNode==body){
if(dojo.html.getAttribute(rows[i],"selected")=="true"){
rows[i].className=this.rowSelectedClass;
}else{
if(this.enableAlternateRows&&idx%2==1){
rows[i].className=this.rowAlternateClass;
}else{
rows[i].className="";
}
}
idx++;
}
}
},render:function(_d5f){
var data=[];
var body=this.domNode.getElementsByTagName("tbody")[0];
if(!_d5f){
this.parseDataFromTable(body);
}
for(var i=0;i<this.data.length;i++){
data.push(this.data[i]);
}
var col=this.columns[this.sortIndex];
if(!col.noSort){
var _d64=col.getField();
if(col.sortFunction){
var sort=col.sortFunction;
}else{
var sort=function(a,b){
if(a[_d64]>b[_d64]){
return 1;
}
if(a[_d64]<b[_d64]){
return -1;
}
return 0;
};
}
data.sort(sort);
if(this.sortDirection!=0){
data.reverse();
}
}
while(body.childNodes.length>0){
body.removeChild(body.childNodes[0]);
}
for(var i=0;i<data.length;i++){
var row=document.createElement("tr");
dojo.html.disableSelection(row);
if(data[i][this.valueField]){
row.setAttribute("value",data[i][this.valueField]);
}
if(this.isSelected(data[i])){
row.className=this.rowSelectedClass;
row.setAttribute("selected","true");
}else{
if(this.enableAlternateRows&&i%2==1){
row.className=this.rowAlternateClass;
}
}
for(var j=0;j<this.columns.length;j++){
var cell=document.createElement("td");
cell.setAttribute("align",this.columns[j].align);
cell.setAttribute("valign",this.columns[j].valign);
dojo.html.disableSelection(cell);
if(this.sortIndex==j){
cell.className=this.columnSelected;
}
if(this.columns[j].sortType=="__markup__"){
cell.innerHTML=data[i][this.columns[j].getField()];
for(var k=0;k<cell.childNodes.length;k++){
var node=cell.childNodes[k];
if(node&&node.nodeType==dojo.html.ELEMENT_NODE){
dojo.html.disableSelection(node);
}
}
}else{
if(this.columns[j].getType()==Date){
var _d6d=this.defaultDateFormat;
if(this.columns[j].format){
_d6d=this.columns[j].format;
}
cell.appendChild(document.createTextNode(dojo.date.format(data[i][this.columns[j].getField()],_d6d)));
}else{
cell.appendChild(document.createTextNode(data[i][this.columns[j].getField()]));
}
}
row.appendChild(cell);
}
body.appendChild(row);
dojo.event.connect(row,"onclick",this,"onUISelect");
}
var _d6e=parseInt(this.minRows);
if(!isNaN(_d6e)&&_d6e>0&&data.length<_d6e){
var mod=0;
if(data.length%2==0){
mod=1;
}
var _d70=_d6e-data.length;
for(var i=0;i<_d70;i++){
var row=document.createElement("tr");
row.setAttribute("ignoreIfParsed","true");
if(this.enableAlternateRows&&i%2==mod){
row.className=this.rowAlternateClass;
}
for(var j=0;j<this.columns.length;j++){
var cell=document.createElement("td");
cell.appendChild(document.createTextNode("\xa0"));
row.appendChild(cell);
}
body.appendChild(row);
}
}
},onSelect:function(e){
},onUISelect:function(e){
var row=dojo.html.getParentByType(e.target,"tr");
var body=dojo.html.getParentByType(row,"tbody");
if(this.enableMultipleSelect){
if(e.metaKey||e.ctrlKey){
if(this.isSelected(this.getObjectFromRow(row))){
this.removeFromSelected(this.getObjectFromRow(row));
row.removeAttribute("selected");
}else{
this.setSelectionByRow(row);
row.setAttribute("selected","true");
}
}else{
if(e.shiftKey){
var _d75;
var rows=body.getElementsByTagName("tr");
for(var i=0;i<rows.length;i++){
if(rows[i].parentNode==body){
if(rows[i]==row){
break;
}
if(dojo.html.getAttribute(rows[i],"selected")=="true"){
_d75=rows[i];
}
}
}
if(!_d75){
_d75=row;
for(;i<rows.length;i++){
if(dojo.html.getAttribute(rows[i],"selected")=="true"){
row=rows[i];
break;
}
}
}
this.resetSelections(body);
if(_d75==row){
row.setAttribute("selected","true");
this.setSelectionByRow(row);
}else{
var _d78=false;
for(var i=0;i<rows.length;i++){
if(rows[i].parentNode==body){
rows[i].removeAttribute("selected");
if(rows[i]==_d75){
_d78=true;
}
if(_d78){
this.setSelectionByRow(rows[i]);
rows[i].setAttribute("selected","true");
}
if(rows[i]==row){
_d78=false;
}
}
}
}
}else{
this.resetSelections(body);
row.setAttribute("selected","true");
this.setSelectionByRow(row);
}
}
}else{
this.resetSelections(body);
row.setAttribute("selected","true");
this.setSelectionByRow(row);
}
this.showSelections();
this.onSelect(e);
e.stopPropagation();
e.preventDefault();
},onHeaderClick:function(e){
var _d7a=this.sortIndex;
var _d7b=this.sortDirection;
var _d7c=e.target;
var row=dojo.html.getParentByType(_d7c,"tr");
var _d7e="td";
if(row.getElementsByTagName(_d7e).length==0){
_d7e="th";
}
var _d7f=row.getElementsByTagName(_d7e);
var _d80=dojo.html.getParentByType(_d7c,_d7e);
for(var i=0;i<_d7f.length;i++){
if(_d7f[i]==_d80){
if(i!=_d7a){
this.sortIndex=i;
this.sortDirection=0;
_d7f[i].className=this.headerSortDownClass;
}else{
this.sortDirection=(_d7b==0)?1:0;
if(this.sortDirection==0){
_d7f[i].className=this.headerSortDownClass;
}else{
_d7f[i].className=this.headerSortUpClass;
}
}
}else{
_d7f[i].className=this.headerClass;
}
}
this.render();
},postCreate:function(){
var _d82=this.domNode.getElementsByTagName("thead")[0];
if(this.headClass.length>0){
_d82.className=this.headClass;
}
dojo.html.disableSelection(this.domNode);
this.parseColumns(_d82);
var _d83="td";
if(_d82.getElementsByTagName(_d83).length==0){
_d83="th";
}
var _d84=_d82.getElementsByTagName(_d83);
for(var i=0;i<_d84.length;i++){
if(!this.columns[i].noSort){
dojo.event.connect(_d84[i],"onclick",this,"onHeaderClick");
}
if(this.sortIndex==i){
if(this.sortDirection==0){
_d84[i].className=this.headerSortDownClass;
}else{
_d84[i].className=this.headerSortUpClass;
}
}
}
var _d86=this.domNode.getElementsByTagName("tbody")[0];
if(this.tbodyClass.length>0){
_d86.className=this.tbodyClass;
}
this.parseDataFromTable(_d86);
this.render(true);
}});
dojo.provide("mindquarry.widget.SortableHTMLTable");
dojo.widget.tags.addParseTreeHandler("dojo:SortableHTMLTable");
dojo.widget.manager.registerWidgetPackage("mindquarry.widget");
mindquarry.widget.SortableHTMLTable=function(){
dojo.widget.html.SortableTable.call(this);
this.widgetType="SortableHTMLTable";
this.sortValueAttribute="sortValue";
this.enableAlternateRows=true;
this.headerSortUpClass="selected-sortup";
this.headerSortDownClass="selected-sortdown";
};
dojo.inherits(mindquarry.widget.SortableHTMLTable,dojo.widget.html.SortableTable);
dojo.lang.extend(mindquarry.widget.SortableHTMLTable,{widgetType:"SortableHTMLTable",isContainer:false,parseColumns:function(node){
this.reset();
var row=node.getElementsByTagName("tr")[0];
var _d89=row.getElementsByTagName("td");
if(_d89.length==0){
_d89=row.getElementsByTagName("th");
}
for(var i=0;i<_d89.length;i++){
var o={field:null,format:null,noSort:false,sortType:"String",dataType:String,contentType:"String",sortFunction:null,label:null,align:"left",valign:"middle",getField:function(){
return this.field||this.label;
},getType:function(){
return this.dataType;
}};
if(dojo.html.hasAttribute(_d89[i],"align")){
o.align=dojo.html.getAttribute(_d89[i],"align");
}
if(dojo.html.hasAttribute(_d89[i],"valign")){
o.valign=dojo.html.getAttribute(_d89[i],"valign");
}
if(dojo.html.hasAttribute(_d89[i],"nosort")){
o.noSort=dojo.html.getAttribute(_d89[i],"nosort")=="true";
}
if(dojo.html.hasAttribute(_d89[i],"sortusing")){
var _d8c=dojo.html.getAttribute(_d89[i],"sortusing");
var f=this.getTypeFromString(_d8c);
if(f!=null&&f!=window&&typeof (f)=="function"){
o.sortFunction=f;
}
}
if(dojo.html.hasAttribute(_d89[i],"field")){
o.field=dojo.html.getAttribute(_d89[i],"field");
}
if(dojo.html.hasAttribute(_d89[i],"format")){
o.format=dojo.html.getAttribute(_d89[i],"format");
}
if(dojo.html.hasAttribute(_d89[i],"dataType")){
var _d8e=dojo.html.getAttribute(_d89[i],"dataType");
if(_d8e.toLowerCase()=="html"||_d8e.toLowerCase()=="markup"){
o.sortType="__markup__";
o.noSort=true;
}else{
var type=this.getTypeFromString(_d8e);
if(type){
o.sortType=_d8e;
o.dataType=type;
}
}
}
if(dojo.html.hasAttribute(_d89[i],"contentType")){
var _d90=dojo.html.getAttribute(_d89[i],"contentType");
if(_d90.toLowerCase()=="html"||_d90.toLowerCase()=="markup"){
o.contentType="__markup__";
}else{
o.contentType=_d90;
}
}
o.label=dojo.html.renderedTextContent(_d89[i]);
this.columns.push(o);
if(dojo.html.hasAttribute(_d89[i],"sort")){
this.sortIndex=i;
var dir=dojo.html.getAttribute(_d89[i],"sort");
if(!isNaN(parseInt(dir))){
dir=parseInt(dir);
this.sortDirection=(dir!=0)?1:0;
}else{
this.sortDirection=(dir.toLowerCase()=="desc")?1:0;
}
}
}
},parseDataFromTable:function(_d92){
this.data=[];
this.selected=[];
var rows=_d92.getElementsByTagName("tr");
for(var i=0;i<rows.length;i++){
if(dojo.html.getAttribute(rows[i],"ignoreIfParsed")=="true"){
continue;
}
var o={};
var _d96=rows[i].getElementsByTagName("td");
for(var j=0;j<this.columns.length;j++){
var _d98=this.columns[j].getField();
var _d99;
if(this.columns[j].contentType=="__markup__"){
_d99=_d96[j].innerHTML;
}else{
var type=this.columns[j].getType();
var val=dojo.html.renderedTextContent(_d96[j]);
if(val){
_d99=new type(val);
}else{
_d99=new type();
}
}
var _d9c=_d99;
if(dojo.html.hasAttribute(_d96[j],this.sortValueAttribute)){
_d9c=dojo.html.getAttribute(_d96[j],this.sortValueAttribute);
var type=this.columns[j].getType();
var val=dojo.html.renderedTextContent(_d96[j]);
if(val){
_d9c=new type(val);
}else{
_d9c=new type();
}
}
o[_d98]=[_d9c,_d99];
}
if(dojo.html.hasAttribute(rows[i],"value")&&!o[this.valueField]){
o[this.valueField]=dojo.html.getAttribute(rows[i],"value");
}
this.data.push(o);
if(dojo.html.getAttribute(rows[i],"selected")=="true"){
this.selected.push(o);
}
}
},render:function(_d9d){
var data=[];
var body=this.domNode.getElementsByTagName("tbody")[0];
if(!_d9d){
this.parseDataFromTable(body);
}
for(var i=0;i<this.data.length;i++){
data.push(this.data[i]);
}
var col=this.columns[this.sortIndex];
if(!col.noSort){
var _da2=col.getField();
if(col.sortFunction){
var sort=col.sortFunction;
}else{
var sort=function(a,b){
if(a[_da2][0]>b[_da2][0]){
return 1;
}
if(a[_da2][0]<b[_da2][0]){
return -1;
}
return 0;
};
}
data.sort(sort);
if(this.sortDirection!=0){
data.reverse();
}
}
while(body.childNodes.length>0){
body.removeChild(body.childNodes[0]);
}
for(var i=0;i<data.length;i++){
var row=document.createElement("tr");
dojo.html.disableSelection(row);
if(data[i][this.valueField]){
row.setAttribute("value",data[i][this.valueField]);
}
if(this.isSelected(data[i])){
row.className=this.rowSelectedClass;
row.setAttribute("selected","true");
}else{
if(this.enableAlternateRows&&i%2==1){
row.className=this.rowAlternateClass;
}
}
for(var j=0;j<this.columns.length;j++){
var cell=document.createElement("td");
cell.setAttribute("align",this.columns[j].align);
cell.setAttribute("valign",this.columns[j].valign);
dojo.html.disableSelection(cell);
if(this.sortIndex==j){
cell.className=this.columnSelected;
}
if(this.columns[j].contentType=="__markup__"){
cell.innerHTML=data[i][this.columns[j].getField()][1];
for(var k=0;k<cell.childNodes.length;k++){
var node=cell.childNodes[k];
if(node&&node.nodeType==dojo.html.ELEMENT_NODE){
dojo.html.disableSelection(node);
}
}
}else{
if(this.columns[j].getType()==Date){
var _dab=this.defaultDateFormat;
if(this.columns[j].format){
_dab=this.columns[j].format;
}
cell.appendChild(document.createTextNode(dojo.date.format(data[i][this.columns[j].getField()][1],_dab)));
}else{
cell.appendChild(document.createTextNode(data[i][this.columns[j].getField()][1]));
}
}
row.appendChild(cell);
}
body.appendChild(row);
}
var _dac=parseInt(this.minRows);
if(!isNaN(_dac)&&_dac>0&&data.length<_dac){
var mod=0;
if(data.length%2==0){
mod=1;
}
var _dae=_dac-data.length;
for(var i=0;i<_dae;i++){
var row=document.createElement("tr");
row.setAttribute("ignoreIfParsed","true");
if(this.enableAlternateRows&&i%2==mod){
row.className=this.rowAlternateClass;
}
for(var j=0;j<this.columns.length;j++){
var cell=document.createElement("td");
cell.appendChild(document.createTextNode("\xa0"));
row.appendChild(cell);
}
body.appendChild(row);
}
}
}});
dojo.provide("mindquarry.widget.TableExpandLink");
dojo.widget.tags.addParseTreeHandler("dojo:TableExpandLink");
dojo.widget.manager.registerWidgetPackage("mindquarry.widget");
mindquarry.widget.TableExpandLink=function(){
dojo.widget.DomWidget.call(this);
};
dojo.inherits(mindquarry.widget.TableExpandLink,dojo.widget.DomWidget);
dojo.lang.extend(mindquarry.widget.TableExpandLink,{widgetType:"TableExpandLink",isContainer:true,tbody:null,table:null,rownumber:0,contextrow:null,addedrows:new Array(),expanded:false,buildRendering:function(args,_db0,_db1){
this.domNode=_db0["dojo:"+this.widgetType.toLowerCase()].nodeRef;
var _db2=this.domNode;
while(_db2.parentNode){
_db2=_db2.parentNode;
if(_db2.nodeName=="TR"&&this.contextrow==null){
this.contextrow=_db2;
}
if(_db2.nodeName=="TBODY"&&this.tbody==null){
this.tbody=_db2;
}
if(_db2.nodeName=="TABLE"&&this.table==null){
this.table=_db2;
}
}
dojo.event.connect(this.domNode,"onclick",this,"onClick");
},onClick:function(_db3){
_db3.preventDefault();
var href=_db3.target.href;
if(!this.expanded){
if(href.indexOf("?")==-1){
cocoon.ajax.update(href+"?lightbox-request=true",this.contextrow,this.insertRow);
}else{
cocoon.ajax.update(href+"&lightbox-request=true",this.contextrow,this.insertRow);
}
this.expanded=true;
_db3.target.className="progress";
}else{
this.expanded=false;
_db3.target.className="expand";
var _db5=this.getIndent(this.contextrow);
var row=this.contextrow.rowIndex;
var _db7=false;
for(var i=row+1;i<this.table.rows.length;i++){
var _db9=this.getIndent(this.table.rows[i]);
if(_db9>_db5){
_db7=true;
this.table.deleteRow(i);
i--;
}else{
if(_db7==true){
break;
}
}
}
}
return false;
},getIndent:function(_dba){
var _dbb=0;
var _dbc=dojo.html.getClasses(_dba);
for(var i=0;i<_dbc.length;i++){
if(_dbc[i].indexOf("indent")==0){
var _dbe=_dbc[i].substr(6);
if(_dbe>_dbb){
_dbb=_dbe;
}
}
}
return _dbb;
},insertRow:function(_dbf,_dc0){
return cocoon.ajax.insertionHelper.insert(_dbf,_dc0,function(_dc1,_dc2){
var _dc3=_dc1.parentNode.parentNode;
var _dc4=_dc1.rowIndex+1;
var _dc5=_dc1.getElementsByTagName("a")[0].href;
_dc1.getElementsByTagName("a")[0].className="collapse";
var _dc6=_dc5.substr(_dc5.lastIndexOf("?revision="));
var _dc7=0;
var _dc8=dojo.html.getClasses(_dc1);
for(var i=0;i<_dc8.length;i++){
if(_dc8[i].indexOf("indent")==0){
var _dca=_dc8[i].substr(6);
if(_dca>_dc7){
_dc7=_dca;
}
}
}
_dc7++;
var _dcb="url(";
for(var i=0;i<_dc7;i++){
_dcb=_dcb+"../";
}
_dc5=_dc5.substr(0,_dc5.indexOf("?revision="));
var _dcc=_dc5.replace("/browser/","/view/");
for(var i=0;i<_dc2.childNodes.length;i++){
if(_dc2.childNodes[i].nodeName=="DIV"){
var row=_dc2.childNodes[i];
newrow=_dc3.insertRow(_dc4+i);
newrow.className=row.className+" indent"+_dc7;
for(var j=0;j<row.childNodes.length;j++){
var _dcf=newrow.insertCell(j);
_dcf.innerHTML=row.childNodes[j].innerHTML;
_dcf.className=row.childNodes[j].className;
var _dd0=_dcf.getElementsByTagName("a");
for(var k=0;k<_dd0.length;k++){
_dd0[k].style.backgroundImage=_dd0[k].style.backgroundImage.replace(_dcb,"url(");
var _dd2=_dd0[k].href;
var _dd3=false;
if(_dd2.indexOf("/?")!=-1){
_dd2=_dd2.substr(0,_dd2.lastIndexOf("/?"));
_dd3=true;
}else{
_dd2=_dd2.substr(0,_dd2.lastIndexOf("?"));
}
_dd2=_dd2.substr(_dd2.lastIndexOf("/")+1);
if(_dd3){
_dd0[k].href=_dc5+_dd2+"/"+_dc6;
}else{
_dd0[k].href=_dcc+_dd2+_dc6;
}
}
var divs=_dcf.getElementsByTagName("div");
for(var k=0;k<divs.length;k++){
divs[k].style.backgroundImage=divs[k].style.backgroundImage.replace("url(../","url(");
}
}
cocoon.ajax.insertionHelper.parseDojoWidgets(newrow);
}
}
});
}});
dojo.provide("mindquarry.widget.TeamSwitcher");
dojo.widget.tags.addParseTreeHandler("dojo:TeamSwitcher");
dojo.widget.manager.registerWidgetPackage("mindquarry.widget");
mindquarry.widget.TeamSwitcher=function(){
dojo.widget.DomWidget.call(this);
var _dd5=null;
};
dojo.inherits(mindquarry.widget.TeamSwitcher,dojo.widget.DomWidget);
dojo.lang.extend(mindquarry.widget.TeamSwitcher,{widgetType:"TeamSwitcher",isContainer:true,slArea_:null,buildRendering:function(args,_dd7,_dd8){
this.domNode=_dd7["dojo:"+this.widgetType.toLowerCase()].nodeRef;
var list=this.domNode.getElementsByTagName("ul")[0];
var _dda=list.getElementsByTagName("a");
var _ddb=this.domNode.title;
var _ddc=dojo.byId("teamspace-base-link");
this.domNode.title="";
slArea_=document.createElement("div");
slArea_.id="slarea";
var _ddd=list.cloneNode(true);
var _dde=document.createElement("li");
_dde.innerHTML="<a href='"+_ddc.href+"' class='allyourteams'>All your teams</a>";
_ddd.appendChild(_dde);
slArea_.appendChild(_ddd);
slArea_.style.display="none";
var _ddf=slArea_.getElementsByTagName("a");
for(var i=0;i<_ddf.length;i++){
if(_ddf[i].title!=""){
_ddf[i].href=_ddc.href+_ddf[i].title+"/";
_ddf[i].style.backgroundImage="url(/teamspace/"+_ddf[i].title+".22.png)";
}else{
_ddf[i].href=_ddc.href;
}
}
var _de1=false;
var _de2=true;
for(var i=0;i<_dda.length;i++){
_de2=false;
if(_dda[i].title==_ddb){
_dda[i].parentNode.style.display="block";
_de1=true;
}
}
if(!_de1){
var _de3=document.createElement("li");
if(_de2){
_de3.innerHTML="<a href=\"#\">No teams</a>";
}else{
_de3.innerHTML="<a href=\""+_ddc.href+"\" class=\"allyourteams\">All your teams</a>";
}
_de3.style.display="block";
list.appendChild(_de3);
}
if(!_de2){
dojo.event.connect(list,"onclick",this,"onClick");
}
dojo.event.connect(document.body,"onclick",this,"hideSwitcher");
this.domNode.appendChild(slArea_);
},onClick:function(_de4){
_de4.preventDefault();
if(slArea_.style.display!="block"){
slArea_.style.display="block";
}else{
slArea_.style.display="none";
}
},hideSwitcher:function(_de5){
if(slArea_.style.display=="block"){
var _de6=_de5.target;
while(_de6.parentNode){
if(_de6==this.domNode){
return;
}
_de6=_de6.parentNode;
}
slArea_.style.display="none";
}
}});
dojo.provide("mindquarry.widget.ToggleButton");
dojo.widget.tags.addParseTreeHandler("dojo:ToggleButton");
dojo.widget.manager.registerWidgetPackage("mindquarry.widget");
mindquarry.widget.ToggleButton=function(){
dojo.widget.DomWidget.call(this);
var _de7=null;
var _de8;
var _de9=false;
};
dojo.inherits(mindquarry.widget.ToggleButton,dojo.widget.DomWidget);
dojo.lang.extend(mindquarry.widget.ToggleButton,{widgetType:"ToggleButton",isContainer:true,buildRendering:function(args,_deb,_dec){
this.domNode=_deb["dojo:"+this.widgetType.toLowerCase()].nodeRef;
this.checkbox=dojo.dom.getFirstChildElement(this.domNode,"input");
this.parentname=this.domNode.parentNode.id;
if(this.checkbox.type=="radio"){
this.radio=true;
}
dojo.event.connect(this.domNode,"onclick",this,"onClick");
},onClick:function(_ded){
_ded.preventDefault();
this.checkbox.checked=!this.checkbox.checked;
this.adjustClasses(_ded);
cocoon.forms.submitForm(this.domNode,this.parentname);
},adjustClasses:function(_dee){
_dee.preventDefault();
if(this.radio){
var _def=this.domNode.parentNode.childNodes;
for(var i=0;i<_def.length;i++){
if(_def[i].nodeType==1&&_def[i].nodeName=="DIV"){
var _df1=_df1=dojo.dom.getFirstChildElement(_def[i],"input");
if(_df1.checked){
dojo.html.replaceClass(_def[i],"togglebuttonpushed","togglebutton");
}else{
dojo.html.replaceClass(_def[i],"togglebutton","togglebuttonpushed");
}
}
}
}
if(this.checkbox.checked){
dojo.html.replaceClass(this.domNode,"togglebuttonpushed","togglebutton");
}else{
dojo.html.replaceClass(this.domNode,"togglebutton","togglebuttonpushed");
}
}});
dojo.provide("mindquarry.widget.wikiLink");
var mindquarry=mindquarry||{};
mindquarry.widget=mindquarry.widget||{};
mindquarry.widget.wikiLinkListClick=function(){
var _df2=dojo.widget.byType("Editor2")[0];
var _df3;
if(_df2.document.selection){
_df3=_df2.document.selection.createRange().text;
}else{
if(dojo.render.html.mozilla){
_df3=_df2.window.getSelection().toString();
}
}
if(_df3.length){
var _df4=dojo.widget.byId("wikiLinkDialog");
var _df5=document.getElementById("wikiLinkDialogOK");
var _df6=document.getElementById("wikiLinkDialogCancel");
_df4.setCloseControl(_df5);
_df4.setCloseControl(_df6);
_df4.show();
var _df7=document.getElementById("dojo-wiki-link-entry-input");
if(_df7){
_df7.focus();
}
}else{
alert("Please select text to link");
}
};
mindquarry.widget.wikiLinkWordClick=function(){
var _df8=dojo.widget.byType("Editor2")[0];
var _df9;
if(_df8.document.selection){
_df9=_df8.document.selection.createRange().text;
}else{
if(dojo.render.html.mozilla){
_df9=_df8.window.getSelection().toString();
}
}
if(_df9.length){
this.wikiLinkSet(_df9);
}else{
alert("Please select text to link");
}
};
mindquarry.widget.wikiLinkSet=function(href){
var _dfb=dojo.widget.byType("Editor2Toolbar")[0];
if(href&&(href.indexOf("http://")!=0)&&(href.indexOf("https://")!=0)){
href=this.normalizeName(href);
}
if(href){
_dfb.exec("createlink",href);
}
};
dojo.provide("mindquarry.widget.TimerCFormAction");
dojo.widget.tags.addParseTreeHandler("dojo:TimerCFormAction");
dojo.widget.manager.registerWidgetPackage("mindquarry.widget");
mindquarry.widget.TimerCFormAction=function(){
dojo.widget.DomWidget.call(this);
var _dfc=null;
};
dojo.inherits(mindquarry.widget.TimerCFormAction,dojo.widget.DomWidget);
dojo.lang.extend(mindquarry.widget.TimerCFormAction,{widgetType:"TimerCFormAction",isContainer:true,buildRendering:function(args,_dfe,_dff){
this.domNode=_dfe["dojo:"+this.widgetType.toLowerCase()].nodeRef;
this.cform=_dff;
var _e00=dojo.html.getAttribute(this.domNode,"delay");
if(_e00==null){
_e00=60000;
}
var _e01=new dojo.animation.Timer(_e00);
var _e02=this.domNode;
var _e03=this.cform;
_e01.onTick=function(){
if(_e03==null){
var form=cocoon.forms.getForm(_e02);
var _e05=form.getAttribute("dojoWidgetId");
if(_e05){
_e03=dojo.widget.byId(_e05);
}
}
_e03.submit(_e02.name);
};
_e01.start();
}});

