/*
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
package org.apache.solr.analysis.author;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A TokenFilter which filters out Tokens at the same position and Term text as the previous token in the stream.
 * It recognizes author query variations and replaces them with the same terms (that were generated as synonyms,
 * or transliterations)
 */
public final class AuthorRemoveDuplicatesTokenFilter extends TokenFilter {
  private Map<String, Map<String, AttributeSource.State>> cache = null;
  private Iterator<Entry<String, Map<String, State>>> iterator = null; 
  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
  private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
  private AttributeSource.State finalState;
  private Map<String, Integer> inputTypes;
  
  /**
   * Create a new CachingTokenFilter around <code>input</code>. As with
   * any normal TokenFilter, do <em>not</em> call reset on the input; this filter
   * will do it normally.
   * @param inputTypes 
   */
  public AuthorRemoveDuplicatesTokenFilter(TokenStream input, Map<String, Integer> inputTypes) {
    super(input);
    this.inputTypes = inputTypes;
  }

  @Override
  public void reset() throws IOException {
    input.reset();
    if (cache != null) {
      cache.clear();
    }
    else {
      cache = new HashMap<String, Map<String, State>>();
    }
    iterator = null;
  }

  /** The first time called, it'll read and cache all tokens from the input. */
  @Override
  public final boolean incrementToken() throws IOException {
    if (iterator == null) {
      fillCache();
      iterator = cache.entrySet().iterator();      
    }
    
    
    if (!iterator.hasNext()) {
      // the cache is exhausted, return false
      return false;
    }
    restoreTypedState(iterator.next());
    return true;
  }

  private void restoreTypedState(Entry<String, Map<String, State>> state) {
    Map<String, State> types = state.getValue();
    State saved = null;
    int currState = -1;
    
    // only one state will be resurrected (the one with highest priority)
    for (Entry<String, State> s: types.entrySet()) {
      Integer p = inputTypes.getOrDefault(s.getKey(), -1);
      if (p > currState || saved == null) {
        saved = s.getValue();
        currState = p;
      }
    }
    restoreState(saved);
  }

  @Override
  public final void end() {
    if (finalState != null) {
      restoreState(finalState);
    }
  }
  

  private void fillCache() throws IOException {
    while (input.incrementToken()) {
      
      String term = termAtt.toString();
      
      if (!cache.containsKey(term)) {
        cache.put(term, new HashMap<String, AttributeSource.State>());
      }
      Map<String, State> types = cache.get(term);
      types.put(typeAtt.type(), captureState());
    }
    // capture final state
    input.end();
    finalState = captureState();
  }

}
