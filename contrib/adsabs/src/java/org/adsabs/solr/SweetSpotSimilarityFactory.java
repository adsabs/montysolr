/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.adsabs.solr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.lucene.search.Similarity;
import org.apache.solr.schema.SimilarityFactory;
import org.apache.lucene.misc.SweetSpotSimilarity;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jluker
 */
//public class CustomSimilarityFactory extends SimilarityFactory {
public class SweetSpotSimilarityFactory extends SimilarityFactory {

    public static final Logger log = LoggerFactory.getLogger(SolrResourceLoader.class);

    @Override
    public Similarity getSimilarity() {
        SweetSpotSimilarity sim = new SweetSpotSimilarity();

        int max = this.params.getInt("max");
        int min = this.params.getInt("min");
        float steepness = this.params.getFloat("steepness");

        log.info("max: " + max);
        log.info("min: " + min);
        log.info("steepness: " + steepness);

        // hardcoded field settings for now
        sim.setLengthNormFactors("body", min, max, steepness, true);
        sim.setLengthNormFactors("body_syn", min, max, steepness, true);
        return sim;
    }

}
