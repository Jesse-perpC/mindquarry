cd dojo-0.3.1\buildscripts

rem run dojo packager with mindquarry specific profile
ant -DprofileFile=../../mindquarry+cocoon.profile.js clean release

rem copy the packaged and compressed dojo.js into the resource block
cp ..\release\dojo\dojo.js ..\..\..\mindquarry-webapp-resources\src\main\resources\COB-INF\scripts\dojo-package\dojo.js
cd ..\..
