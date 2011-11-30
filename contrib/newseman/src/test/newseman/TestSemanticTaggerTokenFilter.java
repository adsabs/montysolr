package newseman;

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

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import newseman.SemanticTaggerTokenFilter;
import newseman.SemanticTagger;
import newseman.MontySolrBaseTokenStreamTestCase;

import invenio.montysolr.jni.MontySolrVM;
import invenio.montysolr.jni.PythonMessage;

import java.io.StringReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class TestSemanticTaggerTokenFilter extends MontySolrBaseTokenStreamTestCase {
	
	private String url;
	private SemanticTagger tagger;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		url = "sqlite:///:memory:";
		tagger = new SemanticTagger(url);
		
		MontySolrVM.INSTANCE.evalCommand("import sys;sys.path.append(\'" 
				+ this.getMontySolrHome() + "/contrib/newseman/src/python\')");
		
		
		SemanticTagger tagger = new SemanticTagger(this.url);
		
		// fill the db with test data
		PythonMessage message = MontySolrVM.INSTANCE.createMessage(
				"fill_newseman_dictionary")
				.setParam("url", tagger.getName());
	}


	public void testSemanticTokenFilter() throws IOException {
		StringReader reader = new StringReader(
				"This is a test of the english stop keyword analyzer");
		SemanticTaggerTokenFilter stream = 
			new SemanticTaggerTokenFilter(
				new StopFilter( Version.LUCENE_31,
					new StandardTokenizer(Version.LUCENE_31, reader),
					new HashSet(Arrays.asList(new String[] {"stop", "the"}))
				)
			);
		
		assertTrue(stream != null);
		CharTermAttribute termAtt = stream
				.getAttribute(CharTermAttribute.class);
		PositionIncrementAttribute posIncrAtt = stream
		.getAttribute(PositionIncrementAttribute.class);

		StringBuffer buf = new StringBuffer();
		while (stream.incrementToken()) {
			buf.append(termAtt.toString());
			buf.append("/");
			buf.append(posIncrAtt.getPositionIncrement());
			buf.append(" ");
		}
		assertEquals(buf.toString().trim(),
				"This/1 is/1 a/1 test/1 of/1 the/1 english/1 keyword/1 a1/0 a2/0 a3/0 analyzer/1");
	}
	
}
