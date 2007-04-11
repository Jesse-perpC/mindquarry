#!/bin/sh
curl -v -T propfind-version-controlled-configuration.xml -H "Content-Type: text/xml" -X PROPFIND  http://localhost:8888/repos/repo1/test.txt | xmllint --nowarning --format -
