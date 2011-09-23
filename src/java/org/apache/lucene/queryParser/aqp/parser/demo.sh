#!/bin/bash
set +e

echo 'Input is:' $1


BINDIR=../../../../../../../../bin/

CP=.:/dvt/antlr-142/lib/antlr-3.4-complete.jar:/x/dev/antlr-34/lib/antlr-3.4-complete.jar:$BINDIR

TGTDIR=$BINDIR/org/apache/lucene/queryParser/aqp/parser

TGTFILE=$BINDIR/ast-tree.dot



rm -fR $TGTDIR
mkdir -p $TGTDIR
rm $TGTFILE

echo "regenerating grammar..."

java -cp $CP org.antlr.Tool -o $TGTDIR StandardLuceneGrammar.g
javac -cp $CP $TGTDIR/*.java
cp BuildAST.java $BINDIR
javac -cp $CP $BINDIR/BuildAST.java
java -cp $CP BuildAST "$1" > $BINDIR/ast-tree.dot

if [ "$1" = "build-only" ];
then
    echo "finished..."
    exit 0
fi


XDOT=`which xdot`
if [ $XDOT ];
then
  echo "executing: $XDOT $BINDIR/ast-tree.dot"
  $XDOT ast-tree.dot 
else
  echo "executing: open -a graphviz $BINDIR/ast-tree.dot"
  open -a graphviz $BINDIR/ast-tree.dot
fi
