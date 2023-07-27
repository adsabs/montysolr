package org.apache.solr.handler.batch;

import org.apache.lucene.search.BitSetQuery;
import org.apache.lucene.util.FixedBitSet;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.CitationCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class BatchProviderDumpBibcodes extends BatchProvider {

    public static final Logger log = LoggerFactory.getLogger(BatchProviderDumpBibcodes.class);
    private final BatchProviderDumpAnalyzedFields internalWorker = new BatchProviderDumpAnalyzedFields();

    @Override
    public void run(SolrQueryRequest locReq, BatchHandlerRequestQueue queue) throws Exception {
        SolrParams params = locReq.getParams();
        String jobid = params.get("jobid");
        String workDir = params.get("#workdir");
        String cacheName = params.get("cache_name", "citations-cache");

        File input = new File(workDir + "/" + jobid + ".input");
        if (!input.canRead()) {
            throw new SolrException(ErrorCode.BAD_REQUEST, "No input data available, bark bark - " + input);
        }

        // we must harvest lucene docids

        // normally, we should load the requested list of bibcodes
        // because it will be always smaller than the whole index
        // but since we are using the bibcodes in the second-order
        // search, the lookup cache is already available

        CitationCache<Object, Integer> cache = (CitationCache<Object, Integer>) locReq.getSearcher().getCache(cacheName);

        if (cache == null) {
            throw new SolrException(ErrorCode.SERVER_ERROR, "Cannot find cache: " + cacheName);
        }

        FixedBitSet bits = new FixedBitSet(locReq.getSearcher().maxDoc());


        // construct a filter
        BufferedReader br = new BufferedReader(new FileReader(input));
        String line;
        Integer docid;
        while ((line = br.readLine()) != null) {
            line = line.toLowerCase().trim();
            docid = cache.get(line);
            if (docid != null) {
                bits.set(docid);
            }
        }
        br.close();

        if (bits.cardinality() <= 0) {
            log.warn("The input is an empty set (handler: {})", this.getName());
        }


        ModifiableSolrParams mParams = new ModifiableSolrParams(locReq.getParams());
        mParams.set(CommonParams.Q, "custom ids");
        locReq.setParams(mParams);
        internalWorker.setQuery(new BitSetQuery(bits));
        internalWorker.run(locReq, queue);
    }


    @Override
    public String getDescription() {
        return "Receives bibcodes and dumps selected fields to disk in JSON format";
    }
}
