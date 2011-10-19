#!/bin/bash
set +e



GRAMMAR=${2:-StandardLuceneGrammar}

BINDIR=../../../../../../../../bin

CP=.:/dvt/antlr-142/lib/antlr-3.4-complete.jar:/x/dev/antlr-34/lib/antlr-3.4-complete.jar:$BINDIR

TGTDIR=$BINDIR/org/apache/lucene/queryParser/aqp/parser

TGTFILE=$BINDIR/ast-tree.dot



echo 'Input:' $1


rm -fR $TGTDIR
mkdir -p $TGTDIR
rm $TGTFILE

echo "Regenerating grammar $GRAMMAR..."

java -cp $CP org.antlr.Tool -o $TGTDIR $GRAMMAR.g
java -cp $CP org.antlr.Tool $GRAMMAR.g
cp BuildAST.java $TGTDIR
javac -cp $CP $TGTDIR/*.java



java -cp $CP org.apache.lucene.queryParser.aqp.parser.BuildAST $GRAMMAR "$1" > $TGTFILE

if [ "$1" = "build-only" ];
then
    echo "finished..."
    exit 0
fi

    
if ! grep -q "digraph" $TGTFILE ;
then
    echo "No DOT file generated!"
    exit 1
fi



XDOT=`which xdot`
if [ $XDOT ];
then
  echo "executing: $XDOT $TGTFILE"
  $XDOT $TGTFILE
else
  echo "executing: open -a graphviz $TGTFILE"
  open -a graphviz $TGTFILE
fi
