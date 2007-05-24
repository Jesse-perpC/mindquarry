#!/bin/sh
java -cp mindquarry-migration.jar com.mindquarry.jcr.change.ChangeClient -r rmi://localhost:1099/jackrabbit -u admin -p admin -x migrate-task-structure.xsl -f teamspaces $1