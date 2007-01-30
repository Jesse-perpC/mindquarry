#!/bin/sh
java -jar lib/mindquarry-jcr-changes-1.0-M1-SNAPSHOT.jar -r rmi://localhost:1099/jackrabbit -u admin -p admin -x xslt/change.xsl -f teamspaces -d