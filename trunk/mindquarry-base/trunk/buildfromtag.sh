#!/bin/sh
basedir=`pwd`
a=0
echo "Usage:"
echo "  sh buildfromtag [your build command]"
echo "  then type or pipe the SVN tag locations line by line"
echo ""
echo "Example:"
echo "  sh buildfromtag mvn deploy < tags.for.release-1.8"
echo ""
echo ""
while read tag; do
  a=$(($a+1));
  echo ""
  echo "  Project" $a;
  echo "  checking out " $tag
  svn co $tag
  dir="`echo $tag | sed -e 's/.*\///'`"
  echo "  changing to dir " $dir
  cd $dir
  echo "  Building"
  echo $@
  echo "  Going back to start directory"
  cd $basedir
done
