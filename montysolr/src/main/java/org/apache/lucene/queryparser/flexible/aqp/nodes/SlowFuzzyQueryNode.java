package org.apache.lucene.queryparser.flexible.aqp.nodes;

import org.apache.lucene.queryparser.flexible.core.nodes.FuzzyQueryNode;

/**
 * Exactly the same as FuzzyQueryNode but it will be transformed into a
 * SlowFuzzyQuery
 */
public class SlowFuzzyQueryNode extends FuzzyQueryNode {

    public SlowFuzzyQueryNode(CharSequence field, CharSequence term,
                              float minSimilarity, int begin, int end) {
        super(field, term, minSimilarity, begin, end);
    }

}
