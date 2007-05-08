#!/bin/sh
java -cp mindquarry-migration.jar com.mindquarry.jcr.change.ChangeClient -r rmi://localhost:1099/jackrabbit -u admin -p admin -x migrate-1.0M2-to-1.1.xsl -f teamspaces $1