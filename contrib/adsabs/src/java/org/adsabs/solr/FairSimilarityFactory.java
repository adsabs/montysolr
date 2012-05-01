/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.adsabs.solr;

import org.apache.lucene.search.Similarity;
import org.apache.solr.schema.SimilarityFactory;

/**
 *
 * @author jluker
 */
public class FairSimilarityFactory extends SimilarityFactory {

    @Override
    public Similarity getSimilarity() {
        return new FairSimilarity();
    }

}
