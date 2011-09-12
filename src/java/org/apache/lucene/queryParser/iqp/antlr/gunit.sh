#!/bin/bash


CP=.:/dvt/antlr-142/lib/antlr-3.4-complete.jar:/x/dev/antlr-34/lib/antlr-3.4-complete.jar


java -cp $CP org.antlr.Tool StandardLuceneGrammar.g
javac -cp $CP *.java

java -cp $CP org.antlr.gunit.Interp StandardLuceneGrammar.gunit 


