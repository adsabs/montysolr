#!/bin/bash

echo 'Input is:' $1

CP=.:/dvt/antlr-142/lib/antlr-3.4-complete.jar:/x/dev/antlr-34/lib/antlr-3.4-complete.jar

rm ast-tree.dot
java -cp $CP org.antlr.Tool StandardLuceneGrammar.g
javac -cp $CP *.java
java -cp $CP Demo "$1" > ast-tree.dot

#xdot ast-tree.dot
open -a graphviz ast-tree.dot
