#!/bin/bash
set +e

# this is just a helper utility to test complex queries (when some escape chars are involved
# and which are screwed by ant). The grammar must be built by ant first!

QUERY=$1

GRAMMAR=${2:-StandardLuceneGrammar}

RULE=${3:-mainQ}

BINDIR=../../../../build/contrib/antlrqueryparser

CP=.:/dvt/antlr-142/lib/antlr-3.4-complete.jar:/x/dev/antlr-34/lib/antlr-3.4-complete.jar:$BINDIR:$BINDIR/classes/test:$BINDIR/classes/java

echo $BINDIR
echo $CP

TGTDIR=$BINDIR/org/apache/lucene/queryParser/aqp/parser

TGTFILE=$BINDIR/ast-tree.dot



echo 'Input:' $QUERY


rm -fR $TGTDIR
mkdir -p $TGTDIR
rm $TGTFILE

#echo "Regenerating grammar $GRAMMAR..."

#java -cp $CP org.antlr.Tool -o $TGTDIR $GRAMMAR.g
#java -cp $CP org.antlr.Tool $GRAMMAR.g
#cp BuildAST.java $TGTDIR
#javac -cp $CP $TGTDIR/*.java



#if [ "$1" = "build-only" ];
#then
#    echo "finished..."
#    exit 0
#fi

java -cp $CP org.apache.lucene.queryParser.aqp.parser.BuildAST $GRAMMAR "$QUERY" $RULE tree
java -cp $CP org.apache.lucene.queryParser.aqp.parser.BuildAST $GRAMMAR "$QUERY" $RULE > $TGTFILE
    
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
