/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.solr.handler;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.similar.MoreLikeThese;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.FacetParams;
import org.apache.solr.common.params.MoreLikeTheseParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.params.MoreLikeTheseParams.TermStyle;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SimpleFacets;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.DocIterator;
import org.apache.solr.search.DocList;
import org.apache.solr.search.DocListAndSet;
import org.apache.solr.search.QueryParsing;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.util.SolrPluginUtils;

/**
 * Solr MoreLikeThese --
 * 
 * Return similar documents either based on a single document or based on posted text.
 * 
 * @since solr 1.3
 */
public class MoreLikeTheseHandler extends RequestHandlerBase  
{
  // Pattern is thread safe -- TODO? share this with general 'fl' param
  private static final Pattern splitList = Pattern.compile(",| ");
  
  @Override
  public void init(NamedList args) {
    super.init(args);
  }

  @Override
  public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp) throws Exception 
  {
    SolrParams params = req.getParams();
    SolrIndexSearcher searcher = req.getSearcher();
    
    
    MoreLikeTheseHelper mlthese = new MoreLikeTheseHelper( params, searcher );
    List<Query> filters = SolrPluginUtils.parseFilterQueries(req);
    
    // Hold on to the interesting terms if relevant
    TermStyle termStyle = TermStyle.get( params.get( MoreLikeTheseParams.INTERESTING_TERMS ) );
    List<InterestingTerm> interesting = (termStyle == TermStyle.NONE )
      ? null : new ArrayList<InterestingTerm>( mlthese.mlthese.getMaxQueryTerms() );
    
    DocListAndSet mltDocs = null;
    String q = params.get( CommonParams.Q );
    
    // Parse Required Params
    // This will either have a single Reader or valid query
    Reader reader = null;
    try {
      if (q == null || q.trim().length() < 1) {
        Iterable<ContentStream> streams = req.getContentStreams();
        if (streams != null) {
          Iterator<ContentStream> iter = streams.iterator();
          if (iter.hasNext()) {
            reader = iter.next().getReader();
          }
          if (iter.hasNext()) {
            throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,
                "MoreLikeThese does not support multiple ContentStreams");
          }
        }
      }

      // What fields do we need to return
      String fl = params.get(CommonParams.FL);
      int flags = 0;
      if (fl != null) {
        flags |= SolrPluginUtils.setReturnFields(fl, rsp);
      }

      int start = params.getInt(CommonParams.START, 0);
      int rows = params.getInt(CommonParams.ROWS, 10);

      // Find documents MoreLikeThese - either with a reader or a query
      // --------------------------------------------------------------------------------
      if (reader != null) {
        mltDocs = mlthese.getMoreLikeThese(reader, start, rows, filters,
            interesting, flags);
      } else if (q != null) {
        // Matching options
        boolean includeMatch = params.getBool(MoreLikeTheseParams.MATCH_INCLUDE,
            true);
        int matchOffset = params.getInt(MoreLikeTheseParams.MATCH_OFFSET, 0);
        // Find the base match
        Query query = QueryParsing.parseQuery(q, params.get(CommonParams.DF),
            params, req.getSchema());
        DocList match = searcher.getDocList(query, null, null, matchOffset, 1,
            flags); // only get the first one...
        if (includeMatch) {
          rsp.add("match", match);
        }

        mltDocs = mlthese.getMoreLikeThese(match, start, rows, filters, interesting, flags);
        
      } else {
        throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,
            "MoreLikeThese requires either a query (?q=) or text to find similar documents.");
      }

    } finally {
      if (reader != null) {
        reader.close();
      }
    }
    
    if( mltDocs == null ) {
      mltDocs = new DocListAndSet(); // avoid NPE
    }
    rsp.add( "response", mltDocs.docList );
    
  
    if( interesting != null ) {
      if( termStyle == TermStyle.DETAILS ) {
        NamedList<Float> it = new NamedList<Float>();
        for( InterestingTerm t : interesting ) {
          it.add( t.term.toString(), t.boost );
        }
        rsp.add( "interestingTerms", it );
      }
      else {
        List<String> it = new ArrayList<String>( interesting.size() );
        for( InterestingTerm t : interesting ) {
          it.add( t.term.text());
        }
        rsp.add( "interestingTerms", it );
      }
    }
    
    // maybe facet the results
    if (params.getBool(FacetParams.FACET,false)) {
      if( mltDocs.docSet == null ) {
        rsp.add( "facet_counts", null );
      }
      else {
        SimpleFacets f = new SimpleFacets(req, mltDocs.docSet, params );
        rsp.add( "facet_counts", f.getFacetCounts() );
      }
    }

    boolean dbg = req.getParams().getBool(CommonParams.DEBUG_QUERY, false);
    // Copied from StandardRequestHandler... perhaps it should be added to doStandardDebug?
    if (dbg) {
      try {
        NamedList<Object> dbgInfo = SolrPluginUtils.doStandardDebug(req, q, mlthese.getRawMLTQuery(), mltDocs.docList);
        if (null != dbgInfo) {
          if (null != filters) {
            dbgInfo.add("filter_queries",req.getParams().getParams(CommonParams.FQ));
            List<String> fqs = new ArrayList<String>(filters.size());
            for (Query fq : filters) {
              fqs.add(QueryParsing.toString(fq, req.getSchema()));
            }
            dbgInfo.add("parsed_filter_queries",fqs);
          }
          rsp.add("debug", dbgInfo);
        }
      } catch (Exception e) {
        SolrException.logOnce(SolrCore.log, "Exception during debug", e);
        rsp.add("exception_during_debug", SolrException.toStr(e));
      }
    }
  }
  
  public static class InterestingTerm
  {
    public Term term;
    public float boost;
        
    public static Comparator<InterestingTerm> BOOST_ORDER = new Comparator<InterestingTerm>() {
      public int compare(InterestingTerm t1, InterestingTerm t2) {
        float d = t1.boost - t2.boost;
        if( d == 0 ) {
          return 0;
        }
        return (d>0)?1:-1;
      }
    };
  }
  
  /**
   * Helper class for MoreLikeThese that can be called from other request handlers
   */
  public static class MoreLikeTheseHelper 
  { 
    final SolrIndexSearcher searcher;
    final MoreLikeThese mlthese;
    final IndexReader reader;
    final SchemaField uniqueKeyField;
    final boolean needDocSet;
    Map<String,Float> boostFields;
    
    public MoreLikeTheseHelper( SolrParams params, SolrIndexSearcher searcher )
    {
      this.searcher = searcher;
      this.reader = searcher.getReader();
      this.uniqueKeyField = searcher.getSchema().getUniqueKeyField();
      this.needDocSet = params.getBool(FacetParams.FACET,false);
      
      SolrParams required = params.required();
      String[] fields = splitList.split( required.get(MoreLikeTheseParams.SIMILARITY_FIELDS) );
      if( fields.length < 1 ) {
        throw new SolrException( SolrException.ErrorCode.BAD_REQUEST, 
            "MoreLikeThese requires at least one similarity field: "+MoreLikeTheseParams.SIMILARITY_FIELDS );
      }
      
      this.mlthese = new MoreLikeThese( reader ); // TODO -- after LUCENE-896, we can use , searcher.getSimilarity() );
      mlthese.setFieldNames(fields);
      mlthese.setAnalyzer( searcher.getSchema().getAnalyzer() );
      
      // configurable params
      mlthese.setMinTermFreq(       params.getInt(MoreLikeTheseParams.MIN_TERM_FREQ,         MoreLikeThese.DEFAULT_MIN_TERM_FREQ));
      mlthese.setMinDocFreq(        params.getInt(MoreLikeTheseParams.MIN_DOC_FREQ,          MoreLikeThese.DEFAULT_MIN_DOC_FREQ));
      mlthese.setMinWordLen(        params.getInt(MoreLikeTheseParams.MIN_WORD_LEN,          MoreLikeThese.DEFAULT_MIN_WORD_LENGTH));
      mlthese.setMaxWordLen(        params.getInt(MoreLikeTheseParams.MAX_WORD_LEN,          MoreLikeThese.DEFAULT_MAX_WORD_LENGTH));
      mlthese.setMaxQueryTerms(     params.getInt(MoreLikeTheseParams.MAX_QUERY_TERMS,       MoreLikeThese.DEFAULT_MAX_QUERY_TERMS));
      mlthese.setMaxNumTokensParsed(params.getInt(MoreLikeTheseParams.MAX_NUM_TOKENS_PARSED, MoreLikeThese.DEFAULT_MAX_NUM_TOKENS_PARSED));
      mlthese.setBoost(            params.getBool(MoreLikeTheseParams.BOOST, false ) );
      boostFields = SolrPluginUtils.parseFieldBoosts(params.getParams(MoreLikeTheseParams.QF));
    }
    
    private Query rawMLTQuery;
    private Query boostedMLTQuery;
    private BooleanQuery realMLTQuery;
    
    public Query getRawMLTQuery(){
      return rawMLTQuery;
    }
    
    public Query getBoostedMLTQuery(){
      return boostedMLTQuery;
    }
    
    public Query getRealMLTQuery(){
      return realMLTQuery;
    }
    
    private Query getBoostedQuery(Query mltquery) {
      BooleanQuery boostedQuery = (BooleanQuery)mltquery.clone();
      if (boostFields.size() > 0) {
        List clauses = boostedQuery.clauses();
        for( Object o : clauses ) {
          TermQuery q = (TermQuery)((BooleanClause)o).getQuery();
          Float b = this.boostFields.get(q.getTerm().field());
          if (b != null) {
            q.setBoost(b*q.getBoost());
          }
        }
      }
      return boostedQuery;
    }
    
    public DocListAndSet getMoreLikeThese( DocList docs, int start, int rows, List<Query> filters, List<InterestingTerm> terms, int flags ) throws IOException
    {
      rawMLTQuery = mlthese.like(docs);
      boostedMLTQuery = getBoostedQuery( rawMLTQuery );
      if( terms != null ) {
        fillInterestingTermsFromMLTQuery( rawMLTQuery, terms );
      }

      // exclude current document from results
      realMLTQuery = new BooleanQuery();
      realMLTQuery.add(boostedMLTQuery, BooleanClause.Occur.MUST);
      
      DocIterator iterator = docs.iterator();
      while( iterator.hasNext() ) {
          int id = iterator.nextDoc();
    	  Document doc = reader.document(id);
          realMLTQuery.add(
	          new TermQuery(new Term(uniqueKeyField.getName(), uniqueKeyField.getType().storedToIndexed(doc.getFieldable(uniqueKeyField.getName())))), 
	            BooleanClause.Occur.MUST_NOT);
      
      }
      
      DocListAndSet results = new DocListAndSet();
      if (this.needDocSet) {
        results = searcher.getDocListAndSet(realMLTQuery, filters, null, start, rows, flags);
      } else {
        results.docList = searcher.getDocList(realMLTQuery, filters, null, start, rows, flags);
      }
      return results;
    }

    public DocListAndSet getMoreLikeThese( Reader reader, int start, int rows, List<Query> filters, List<InterestingTerm> terms, int flags ) throws IOException
    {
      // analyzing with the first field: previous (stupid) behavior
      rawMLTQuery = mlthese.like(reader, mlthese.getFieldNames()[0]);
      boostedMLTQuery = getBoostedQuery( rawMLTQuery );
      if( terms != null ) {
        fillInterestingTermsFromMLTQuery( boostedMLTQuery, terms );
      }
      DocListAndSet results = new DocListAndSet();
      if (this.needDocSet) {
        results = searcher.getDocListAndSet( boostedMLTQuery, filters, null, start, rows, flags);
      } else {
        results.docList = searcher.getDocList( boostedMLTQuery, filters, null, start, rows, flags);
      }
      return results;
    }

    private void fillInterestingTermsFromMLTQuery( Query query, List<InterestingTerm> terms )
    { 
      List clauses = ((BooleanQuery)query).clauses();
      for( Object o : clauses ) {
        TermQuery q = (TermQuery)((BooleanClause)o).getQuery();
        InterestingTerm it = new InterestingTerm();
        it.boost = q.getBoost();
        it.term = q.getTerm();
        terms.add( it );
      } 
      // alternatively we could use
      // mltquery.extractTerms( terms );
    }
    
    public MoreLikeThese getMoreLikeThese()
    {
      return mlthese;
    }
  }
  
  
  //////////////////////// SolrInfoMBeans methods //////////////////////

  @Override
  public String getVersion() {
    return "$Revision: 1164331 $";
  }

  @Override
  public String getDescription() {
    return "Solr MoreLikeThese";
  }

  @Override
  public String getSourceId() {
    return "$Id: MoreLikeTheseHandler.java 1164331 2011-09-02 02:14:28Z koji $";
  }

  @Override
  public String getSource() {
    return "$URL: http://svn.apache.org/repos/asf/lucene/dev/tags/lucene_solr_3_5_0/solr/core/src/java/org/apache/solr/handler/MoreLikeTheseHandler.java $";
  }

  @Override
  public URL[] getDocs() {
    try {
      return new URL[] { new URL("http://wiki.apache.org/solr/MoreLikeThis") };
    }
    catch( MalformedURLException ex ) { return null; }
  }
}
