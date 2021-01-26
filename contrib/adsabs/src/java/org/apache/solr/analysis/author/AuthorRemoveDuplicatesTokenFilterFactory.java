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


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;
import org.apache.solr.common.util.StrUtils;

/**
 * Factory for {@link RemoveDuplicatesTokenFilter}.
 * <pre class="prettyprint">
 * &lt;fieldType name="text_rmdup" class="solr.TextField" positionIncrementGap="100"&gt;
 *   &lt;analyzer&gt;
 *     &lt;tokenizer class="solr.WhitespaceTokenizerFactory"/&gt;
 *     &lt;filter class="solr.RemoveDuplicatesTokenFilterFactory"/&gt;
 *   &lt;/analyzer&gt;
 * &lt;/fieldType&gt;</pre>
 *
 * @since 3.1
 */
public class AuthorRemoveDuplicatesTokenFilterFactory extends TokenFilterFactory {
  
  private List<String> tokenTypes = null;

  /** Creates a new RemoveDuplicatesTokenFilterFactory */
  public AuthorRemoveDuplicatesTokenFilterFactory(Map<String,String> args) {
    super(args);
    if (args.containsKey("tokenTypes")) {
      tokenTypes = StrUtils.splitSmart(args.remove("tokenTypes"), ",", false);
    }
    if (!args.isEmpty()) {
      throw new IllegalArgumentException("Unknown parameters: " + args);
    }
  }
  
  @Override
  public AuthorRemoveDuplicatesTokenFilter create(TokenStream input) {
    Map<String, Integer> inputTypes = new HashMap<String, Integer>();
    if (tokenTypes != null) {
      int i = tokenTypes.size();
      for (String s: tokenTypes) {
        inputTypes.put(s, i--);
      }      
    }
    return new AuthorRemoveDuplicatesTokenFilter(input, inputTypes);
  }
}
