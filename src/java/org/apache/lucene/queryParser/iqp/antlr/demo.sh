#!/bin/bash

echo 'Input is:' $1

CP=.:/dvt/antlr-142/lib/antlr-3.4-complete.jar:/x/dev/antlr-34/lib/antlr-3.4-complete.jar

rm ast-tree.dot
java -cp $CP org.antlr.Tool StandardLuceneGrammar.g
javac -cp $CP *.java
java -cp $CP Demo "$1" > ast-tree.dot

XDOT=`which xdot`
if [ $XDOT ];
then
  echo "executing: $XDOT ast-tree.dot"
  $XDOT ast-tree.dot 
else
  echo "executing: open -a graphviz ast-tree.dot"
  open -a graphviz ast-tree.dot
fi
