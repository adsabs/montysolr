#!/bin/bash


BINDIR=../../../../../../../../bin/

CP=.:/dvt/antlr-142/lib/antlr-3.4-complete.jar:/x/dev/antlr-34/lib/antlr-3.4-complete.jar:$BINDIR

./demo.sh build-only


java -cp $CP org.antlr.gunit.Interp StandardLuceneGrammar.gunit 

