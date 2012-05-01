/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.adsabs.solr;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.DefaultSimilarity;

/**
 *
 * @author jluker
 */
public class FairSimilarity extends DefaultSimilarity {

    public float computeNorm(String field, FieldInvertState state) {
        final int numTerms;
        if (discountOverlaps)
            numTerms = state.getLength() - state.getNumOverlap();
         else
        numTerms = state.getLength();
        return state.getBoost() * ((float) (1.0 / numTerms));
    }

    public float tf(float freq) {
        return freq;
    }
}
