/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.apache.solr.search.similarities;

import org.apache.lucene.misc.SweetSpotSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.solr.core.SolrResourceLoader;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaAware;
import org.apache.solr.schema.SimilarityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jluker
 */
//public class CustomSimilarityFactory extends SimilarityFactory {
public class SweetSpotSimilarityFactory extends SimilarityFactory implements SchemaAware {

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

        sim.setLengthNormFactors(min, max, steepness, true);
        sim.setLengthNormFactors(min, max, steepness, true);
        return sim;
    }

    public void inform(IndexSchema schema) {
        // do nothing
    }

}
