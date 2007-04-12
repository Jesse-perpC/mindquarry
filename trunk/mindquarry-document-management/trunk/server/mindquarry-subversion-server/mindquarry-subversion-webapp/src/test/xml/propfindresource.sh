#!/bin/sh
curl -v -T propfindresource.xml -H "Content-Type: text/xml" -X PROPFIND  http://localhost:8888/repos/repo1/software/test.txt | xmllint --nowarning --format -
