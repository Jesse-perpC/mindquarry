#!/bin/sh
curl -v -T propfindresource.xml -H "Content-Type: text/xml" -X PROPFIND  http://localhost:8889/repo/software/test.txt | xmllint --nowarning --format -
