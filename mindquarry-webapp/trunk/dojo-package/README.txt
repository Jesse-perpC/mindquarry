Upon the first time, run dojo-download.sh/bat which will check out dojo (see below) and then call build.sh/build.bat to create a mindquarry specific dojo package. The compressed dojo.js file will be copied to mindquarry-webapp-resources into COB-INF/scripts/dojo-package.


Checking out the correct dojo version
=====================================

You need to check out dojo 0.3.1

	svn co http://svn.dojotoolkit.org/dojo/tags/release-0.3.1 dojo-0.3.1

and then update the buildscript folder to 0.4.0

	cd dojo-0.3.1/buildscripts
	svn switch http://svn.dojotoolkit.org/dojo/tags/release-0.4.0/buildscripts

before running build.sh/build.bat.

