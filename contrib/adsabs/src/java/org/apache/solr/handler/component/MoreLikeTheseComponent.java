package org.apache.solr.handler.component;
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


import java.io.IOException;
import java.net.URL;

import org.apache.solr.common.params.MoreLikeTheseParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.handler.MoreLikeTheseHandler;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.search.DocIterator;
import org.apache.solr.search.DocList;
import org.apache.solr.search.DocListAndSet;
import org.apache.solr.search.SolrIndexSearcher;

/**
 * TODO!
 * 
 * @version $Id: MoreLikeTheseComponent.java 1064775 2011-01-28 16:58:46Z koji $
 * @since solr 1.3
 */
public class MoreLikeTheseComponent extends SearchComponent
{
  public static final String COMPONENT_NAME = "mlthese";
  
  @Override
  public void prepare(ResponseBuilder rb) throws IOException
  {
    
  }

  @Override
  public void process(ResponseBuilder rb) throws IOException
  {
    SolrParams p = rb.req.getParams();
    boolean mlthese = p.getBool(MoreLikeTheseParams.MLT, false);
    if( p.getBool( MoreLikeTheseParams.MLT, false ) ) {
      SolrIndexSearcher searcher = rb.req.getSearcher();
      
      DocList sim = getMoreLikeThese( rb, searcher,
          rb.getResults().docList, rb.getFieldFlags() );

      // TODO ???? add this directly to the response?
      rb.rsp.add( "moreLikeThese", sim );
    }
  }

  DocList getMoreLikeThese( ResponseBuilder rb, SolrIndexSearcher searcher,
      DocList docs, int flags ) throws IOException {
    SolrParams p = rb.req.getParams();
    IndexSchema schema = searcher.getSchema();
    MoreLikeTheseHandler.MoreLikeTheseHelper mltHelper 
      = new MoreLikeTheseHandler.MoreLikeTheseHelper( p, searcher );
    
    SimpleOrderedMap<Object> dbg = null;
    if( rb.isDebug() ){
      dbg = new SimpleOrderedMap<Object>();
    }

    int rows = p.getInt( MoreLikeTheseParams.DOC_COUNT, 5 );
    int truncate = p.getInt( MoreLikeTheseParams.INPUT_DOC_LIMIT, 10 );
    DocListAndSet sim = mltHelper.getMoreLikeThese( docs, 0, rows, null, null, flags );
    
    if( dbg != null ){
        SimpleOrderedMap<Object> docDbg = new SimpleOrderedMap<Object>();
        docDbg.add( "rawMLTQuery", mltHelper.getRawMLTQuery().toString() );
        docDbg.add( "boostedMLTQuery", mltHelper.getBoostedMLTQuery().toString() );
        docDbg.add( "realMLTQuery", mltHelper.getRealMLTQuery().toString() );
        SimpleOrderedMap<Object> explains = new SimpleOrderedMap<Object>();
        DocIterator mltIte = sim.docList.iterator();
        while( mltIte.hasNext() ){
          int mltid = mltIte.nextDoc();
          String key = schema.printableUniqueKey( searcher.doc( mltid ) );
          explains.add( key, searcher.explain( mltHelper.getRealMLTQuery(), mltid ) );
        }
        docDbg.add( "explain", explains );
        dbg.add( "debug", docDbg );
      }

    // add debug information
    if( dbg != null ){
      rb.addDebugInfo( "moreLikeThese", dbg );
    }
    return sim.docList;
  }
  
  /////////////////////////////////////////////
  ///  SolrInfoMBean
  ////////////////////////////////////////////

  @Override
  public String getDescription() {
    return "More Like This";
  }

  @Override
  public String getVersion() {
    return "$Revision: 1064775 $";
  }

  @Override
  public String getSourceId() {
    return "$Id: MoreLikeTheseComponent.java 1064775 2011-01-28 16:58:46Z koji $";
  }

  @Override
  public String getSource() {
    return "$URL: http://svn.apache.org/repos/asf/lucene/dev/tags/lucene_solr_3_5_0/solr/core/src/java/org/apache/solr/handler/component/MoreLikeTheseComponent.java $";
  }

  @Override
  public URL[] getDocs() {
    return null;
  }
}
