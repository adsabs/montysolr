#!/bin/bash

GRAMMAR=${1:-StandardLuceneGrammar}

BINDIR=../../../../../../../../bin/

CP=.:/dvt/antlr-142/lib/antlr-3.4-complete.jar:/x/dev/antlr-34/lib/antlr-3.4-complete.jar:$BINDIR

./demo.sh build-only $GRAMMAR


java -cp $CP org.antlr.gunit.Interp $GRAMMAR.gunit 

