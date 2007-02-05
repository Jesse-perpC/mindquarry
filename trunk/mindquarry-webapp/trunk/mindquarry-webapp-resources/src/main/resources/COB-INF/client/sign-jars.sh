#!/bin/sh

KEYSTORE="$1"

for file in $( find . -name "*.jar" ); do
	jarsigner -keystore $KEYSTORE -storepass mindquarry $file mindquarry 
done