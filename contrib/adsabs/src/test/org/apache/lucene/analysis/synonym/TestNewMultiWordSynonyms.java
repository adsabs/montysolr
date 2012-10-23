package org.apache.lucene.analysis.synonym;

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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.analysis.util.StringMockResourceLoader;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.synonym.NewSynonymFilterFactory.SynonymParser;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.CharsRef;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;


public class TestNewMultiWordSynonyms extends BaseTokenStreamTestCase {
  
  private StringMockResourceLoader getSyn() {
    return new StringMockResourceLoader(
        "hubble\0space\0telescope,HST,hs telescope\n" +
        "foo\0bar,foo ba,fu ba,foobar\n" +
        "foo\0baz,fu ba");
  }
  
  
  
  
  /*
   * This parser is useful if you want to index multi-token synonyms (as one token)
   * as well as their components. Ie. "hubble space telescope was..." will be 
   * indexed as
   * 0: hubble|hubble space telescope|HST
   * 1: space
   * 2: telescope
   */
  public static class TestParserReplaceNullsInclOrig extends NewSynonymFilterFactory.SynonymBuilderFactory {
    protected SynonymParser getParser(Analyzer analyzer) {
      return new NewSolrSynonymParser(true, true, analyzer) {
        @Override
        public void add(CharsRef input, CharsRef output, boolean includeOrig) {
          super.add(input, NewSynonymFilterFactory.replaceNulls(output), true);
        }
      };
    }
  }
  
  /**
   * @since solr 1.4
   */
  public void testMultiWordSynonyms() throws IOException {
    SynonymFilterFactory factory = new SynonymFilterFactory();
    Map<String,String> args = new HashMap<String,String>();
    args.put("synonyms", "synonyms.txt");
    factory.setLuceneMatchVersion(TEST_VERSION_CURRENT);
    factory.init(args);
    factory.inform(new StringMockResourceLoader("a b c,d"));
    TokenStream ts = factory.create(new MockTokenizer(new StringReader("a e"), MockTokenizer.WHITESPACE, false));
    // This fails because ["e","e"] is the value of the token stream
    assertTokenStreamContents(ts, new String[] { "a", "e" });
  }
  

  
  public void testMultiWordSynonymsReplaceNullsCustomInclOrigAnalyzer() throws IOException {
    String O = TypeAttribute.DEFAULT_TYPE;
    String S = SynonymFilter.TYPE_SYNONYM;
    
    NewSynonymFilterFactory factory = new NewSynonymFilterFactory();
    Map<String,String> args = new HashMap<String,String>();
    args.put("synonyms", "synonyms.txt");
    args.put("tokenizerFactory", "org.apache.lucene.analysis.core.KeywordTokenizerFactory");
    args.put("builderFactory", NewSynonymFilterFactory.BestEffortIgnoreCase.class.getName());
    
    factory.setLuceneMatchVersion(TEST_VERSION_CURRENT);
    factory.init(args);
    factory.inform(getSyn());
    
    
    TokenStream ts = factory.create(new MockTokenizer(new StringReader("foo hubble space telescope"), MockTokenizer.WHITESPACE, false));
    assertTokenStreamContents(ts, new String[] { "foo", "hubble", "hubble space telescope", "HST", "hs telescope", "space", "telescope" },
        new int[]    {0, 4, 4, 4, 4,11,17}, //startOffset
        new int[]    {3,10,26,26,26,16,26}, //endOffset
        new String[] {O, O, S, S, S, O, O}, //type
        new int[]    {1, 1, 0, 0, 0, 1, 1}  //posIncr
    );
    
    // test ignoreCase=true
    ts = factory.create(new MockTokenizer(new StringReader("hst"), MockTokenizer.WHITESPACE, false));
    assertTokenStreamContents(ts, new String[] { "hubble space telescope", "HST", "hs telescope"},
        new int[]    {0, 0, 0},
        new int[]    {3, 3, 3},
        new String[] {S, S, S},
        new int[]    {1, 0, 0}
    );
    
    ts = factory.create(new MockTokenizer(new StringReader("some foo bar"), MockTokenizer.WHITESPACE, false));
    assertTokenStreamContents(ts, new String[] { "some", "foo", "foo bar", "foo ba", "fu ba", "foobar", "bar" },
        new int[]    {0, 5, 5, 5, 5, 5, 9}, //startOffset
        new int[]    {4, 8,12,12,12,12,12}, //endOffset
        new String[] {O, O, S, S, S, S, O}, //type
        new int[]    {1, 1, 0, 0, 0, 0, 1}  //posIncr
    );
    
    ts = factory.create(new MockTokenizer(new StringReader("some foobar"), MockTokenizer.WHITESPACE, false));
    assertTokenStreamContents(ts, new String[] { "some", "foo bar", "foo ba", "fu ba", "foobar"},
        new int[]    {0, 5, 5, 5, 5, 5}, //startOffset
        new int[]    {4,11,11,11,11,11}, //endOffset
        new String[] {O, S, S, S, S, S}, //type
        new int[]    {1, 1, 0, 0, 0, 1}  //posIncr
    );
    
  }
  
  public void testMultiWordSynonymsReplaceNullsInclOrig() throws IOException {
    String O = TypeAttribute.DEFAULT_TYPE;
    String S = SynonymFilter.TYPE_SYNONYM;
    
    NewSynonymFilterFactory factory = new NewSynonymFilterFactory();
    Map<String,String> args = new HashMap<String,String>();
    args.put("synonyms", "synonyms.txt");
    args.put("ignoreCase", "true");
    args.put("tokenizerFactory", "org.apache.lucene.analysis.core.KeywordTokenizerFactory");
    args.put("builderFactory", TestParserReplaceNullsInclOrig.class.getName());
    
    factory.setLuceneMatchVersion(TEST_VERSION_CURRENT);
    factory.init(args);
    factory.inform(getSyn());
    
    
    TokenStream ts = factory.create(new MockTokenizer(new StringReader("foo hubble space telescope"), MockTokenizer.WHITESPACE, false));
    assertTokenStreamContents(ts, new String[] { "foo", "hubble", "hubble space telescope", "hst", "hs telescope", "space", "telescope" },
        new int[]    {0, 4, 4, 4, 4,11,17}, //startOffset
        new int[]    {3,10,26,26,26,16,26}, //endOffset
        new String[] {O, O, S, S, S, O, O}, //type
        new int[]    {1, 1, 0, 0, 0, 1, 1}  //posIncr
    );
    
    
    ts = factory.create(new MockTokenizer(new StringReader("hst"), MockTokenizer.WHITESPACE, false));
    assertTokenStreamContents(ts, new String[] { "hst", "hubble space telescope", "hst", "hs telescope"},
        new int[]    {0, 0, 0, 0},
        new int[]    {3, 3, 3, 3},
        new String[] {O, S, S, S},
        new int[]    {1, 0, 0, 0}
    );
    
    ts = factory.create(new MockTokenizer(new StringReader("some foo bar"), MockTokenizer.WHITESPACE, false));
    assertTokenStreamContents(ts, new String[] { "some", "foo", "foo bar", "foo ba", "fu ba", "foobar", "bar" },
        new int[]    {0, 5, 5, 5, 5, 5, 9}, //startOffset
        new int[]    {4, 8,12,12,12,12,12}, //endOffset
        new String[] {O, O, S, S, S, S, O}, //type
        new int[]    {1, 1, 0, 0, 0, 0, 1}  //posIncr
    );
    
    ts = factory.create(new MockTokenizer(new StringReader("some foobar"), MockTokenizer.WHITESPACE, false));
    assertTokenStreamContents(ts, new String[] { "some", "foobar", "foo bar", "foo ba", "fu ba", "foobar"},
        new int[]    {0, 5, 5, 5, 5, 5, 5}, //startOffset
        new int[]    {4,11,11,11,11,11,11}, //endOffset
        new String[] {O, O, S, S, S, S, S}, //type
        new int[]    {1, 1, 0, 0, 0, 0, 1}  //posIncr
    );
    
  }
  
  
  public void testMultiWordSynonymsNullReplaced() throws IOException {
    String O = TypeAttribute.DEFAULT_TYPE;
    String S = SynonymFilter.TYPE_SYNONYM;
    
    NewSynonymFilterFactory factory = new NewSynonymFilterFactory();
    Map<String,String> args = new HashMap<String,String>();
    args.put("synonyms", "synonyms.txt");
    args.put("ignoreCase", "false");
    args.put("tokenizerFactory", "org.apache.lucene.analysis.core.KeywordTokenizerFactory");
    args.put("builderFactory", NewSynonymFilterFactory.MultiTokenReplaceNulls.class.getName());
    
    factory.setLuceneMatchVersion(TEST_VERSION_CURRENT);
    factory.init(args);
    factory.inform(getSyn());
    
    
    TokenStream ts = factory.create(new MockTokenizer(new StringReader("foo hubble space telescope"), MockTokenizer.WHITESPACE, false));
    assertTokenStreamContents(ts, new String[] { "foo", "hubble space telescope", "HST", "hs telescope" },
        new int[]    {0, 4, 4, 4}, //startOffset
        new int[]    {3,26,26,26}, //endOffset
        new String[] {O, S, S, S}, //type
        new int[]    {1, 1, 0, 0}  //posIncr
    );
    
    
    ts = factory.create(new MockTokenizer(new StringReader("HST"), MockTokenizer.WHITESPACE, false));
    assertTokenStreamContents(ts, new String[] { "hubble space telescope", "HST", "hs telescope"},
        new int[]    {0, 0, 0},
        new int[]    {3, 3, 3},
        new String[] {S, S, S},
        new int[]    {1, 0, 0}
    );
    
    ts = factory.create(new MockTokenizer(new StringReader("some foo bar"), MockTokenizer.WHITESPACE, false));
    assertTokenStreamContents(ts, new String[] { "some", "foo bar", "foo ba", "fu ba", "foobar" },
        new int[]    {0, 5, 5, 5, 5}, //startOffset
        new int[]    {4,12,12,12,12}, //endOffset
        new String[] {O, S, S, S, S}, //type
        new int[]    {1, 1, 0, 0, 0}  //posIncr
    );
    
    ts = factory.create(new MockTokenizer(new StringReader("some foobar"), MockTokenizer.WHITESPACE, false));
    assertTokenStreamContents(ts, new String[] { "some", "foo bar", "foo ba", "fu ba", "foobar"},
        new int[]    {0, 5, 5, 5, 5, 5}, //startOffset
        new int[]    {4,11,11,11,11,11}, //endOffset
        new String[] {O, S, S, S, S, S}, //type
        new int[]    {1, 1, 0, 0, 0, 1}  //posIncr
    );
    
  }
  
  
  
  public void testMultiWordSynonymsDefault() throws IOException {
    String O = TypeAttribute.DEFAULT_TYPE;
    String S = SynonymFilter.TYPE_SYNONYM;
    
    NewSynonymFilterFactory factory = new NewSynonymFilterFactory();
    Map<String,String> args = new HashMap<String,String>();
    args.put("synonyms", "synonyms.txt");
    args.put("tokenizerFactory", "org.apache.lucene.analysis.core.KeywordTokenizerFactory");
    
    factory.setLuceneMatchVersion(TEST_VERSION_CURRENT);
    factory.init(args);
    factory.inform(getSyn());
    
    
    TokenStream ts = factory.create(new MockTokenizer(new StringReader("foo hubble space telescope"), MockTokenizer.WHITESPACE, false));
    assertTokenStreamContents(ts, new String[] { "foo", "hubble", "HST", "hs telescope", "space", "telescope" },
        new int[]    {0, 4, 4, 4,11,17}, //startOffset
        new int[]    {3,10,26,26,16,26}, //endOffset
        new String[] {O, S, S, S, S, S}, //type
        new int[]    {1, 1, 0, 0, 1, 1}  //posIncr
    );
    
    
    ts = factory.create(new MockTokenizer(new StringReader("HST"), MockTokenizer.WHITESPACE, false));
    assertTokenStreamContents(ts, new String[] { "hubble", "HST", "hs telescope", "space", "telescope" },
        new int[]    {0, 0, 0, 0, 0},
        new int[]    {3, 3, 3, 3, 3},
        new String[] {S, S, S, S, S},
        new int[]    {1, 0, 0, 1, 1}
    );
    
    ts = factory.create(new MockTokenizer(new StringReader("some foo bar"), MockTokenizer.WHITESPACE, false));
    assertTokenStreamContents(ts, new String[] { "some", "foo", "foo ba", "fu ba", "foobar", "bar" },
        new int[]    {0, 5, 5, 5, 5, 9}, //startOffset
        new int[]    {4, 8,12,12,12,12}, //endOffset
        new String[] {O, S, S, S, S, S}, //type
        new int[]    {1, 1, 0, 0, 0, 1}  //posIncr
    );
    
    ts = factory.create(new MockTokenizer(new StringReader("some foobar"), MockTokenizer.WHITESPACE, false));
    assertTokenStreamContents(ts, new String[] { "some", "foo", "foo ba", "fu ba", "foobar", "bar"},
        new int[]    {0, 5, 5, 5, 5, 5}, //startOffset
        new int[]    {4,11,11,11,11,11}, //endOffset
        new String[] {O, S, S, S, S, S}, //type
        new int[]    {1, 1, 0, 0, 0, 1}  //posIncr
    );
    
  }
  
  /*
   * The default behaviour but the original tokens are emitted
   * before the synonyms
   */
  public void testMultiWordSynonymsInclOrig() throws IOException {
    String O = TypeAttribute.DEFAULT_TYPE;
    String S = SynonymFilter.TYPE_SYNONYM;
    
    NewSynonymFilterFactory factory = new NewSynonymFilterFactory();
    Map<String,String> args = new HashMap<String,String>();
    args.put("synonyms", "synonyms.txt");
    args.put("ignoreCase", "true");
    args.put("tokenizerFactory", "org.apache.lucene.analysis.core.KeywordTokenizerFactory");
    args.put("builderFactory", NewSynonymFilterFactory.AlwaysIncludeOriginal.class.getName());
    
    factory.setLuceneMatchVersion(TEST_VERSION_CURRENT);
    factory.init(args);
    factory.inform(getSyn());
    
    
    TokenStream ts = factory.create(new MockTokenizer(new StringReader("foo hubble space telescope"), MockTokenizer.WHITESPACE, false));
    assertTokenStreamContents(ts, new String[] { "foo", "hubble", "hubble", "hst", "hs telescope", "space", "space", "telescope", "telescope" },
    		new int[]    {0, 4, 4, 4, 4,11,11,17,17}, //startOffset
    		new int[]    {3,10,10,26,26,16,16,26,26}, //endOffset
    		new String[] {O, O, S, S, S, O, S, O, S}, //type
    		new int[]    {1, 1, 0, 0, 0, 1, 0, 1, 0}  //posIncr
    );
    
    
    ts = factory.create(new MockTokenizer(new StringReader("hst"), MockTokenizer.WHITESPACE, false));
    assertTokenStreamContents(ts, new String[] { "hst", "hubble", "hst", "hs telescope", "space", "telescope" },
    		new int[]    {0, 0, 0, 0, 0, 0},
    		new int[]    {3, 3, 3, 3, 3, 3},
    		new String[] {O, S, S, S, S, S},
    		new int[]    {1, 0, 0, 0, 1, 1}
    );
    
    ts = factory.create(new MockTokenizer(new StringReader("some foo bar"), MockTokenizer.WHITESPACE, false));
    assertTokenStreamContents(ts, new String[] { "some", "foo", "foo", "foo ba", "fu ba", "foobar", "bar", "bar" },
    		new int[]    {0, 5, 5, 5, 5, 5, 9, 9}, //startOffset
    		new int[]    {4, 8, 8,12,12,12,12,12}, //endOffset
    		new String[] {O, O, S, S, S, S, O, S}, //type
    		new int[]    {1, 1, 0, 0, 0, 0, 1, 0}  //posIncr
    );
    
    ts = factory.create(new MockTokenizer(new StringReader("some foobar"), MockTokenizer.WHITESPACE, false));
    assertTokenStreamContents(ts, new String[] { "some", "foobar", "foo", "foo ba", "fu ba", "foobar", "bar"},
    		new int[]    {0, 5, 5, 5, 5, 5, 5}, //startOffset
    		new int[]    {4,11,11,11,11,11,11}, //endOffset
    		new String[] {O, O, S, S, S, S, S}, //type
    		new int[]    {1, 1, 0, 0, 0, 0, 1}  //posIncr
    );
    
  }
}
