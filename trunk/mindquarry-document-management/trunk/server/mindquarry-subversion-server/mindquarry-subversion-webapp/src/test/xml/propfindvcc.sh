#!/bin/sh
curl -v -T propfindvcc.xml -H "Content-Type: text/xml" -X PROPFIND  http://localhost:8888/repos/repo1/!svn/vcc/default | xmllint --nowarning --format -
