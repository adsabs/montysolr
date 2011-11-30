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

import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
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
import java.util.Arrays;
import java.util.HashSet;

public class TestSemanticTaggerTokenFilter extends MontySolrBaseTokenStreamTestCase {
	
	private String url;
	private SemanticTagger tagger;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		url = "sqlite:///:memory:";
		
		
		tagger = new SemanticTagger(url);
		tagger.configureTagger("czech", 2, "add", "purge");
		
		// fill the db with test data
		PythonMessage message = MontySolrVM.INSTANCE.createMessage(
				"fill_newseman_dictionary")
				.setParam("url", tagger.getName());
		MontySolrVM.INSTANCE.sendMessage(message);
	}
	
	@Override
	public String getModuleName() {
		//return "monty_newseman.tests.bridge.Bridge";
		return "montysolr.java_bridge.SimpleBridge";
	}


	public void testSemanticTokenFilter() throws IOException {
		String text = "velká světová revoluce byla velká říjnová revoluce protože s velkou říjnovou revolucí " +
        "a bez velké říjnové revoluce a ještě velká říjnová revoluce socialistická komunistická " +
        "s velkou extra říjnovou revolucí";
    
	
		StringReader reader = new StringReader(text);
		SemanticTaggerTokenFilter stream = 
			new SemanticTaggerTokenFilter(
				new StopFilter( Version.LUCENE_31,
					new StandardTokenizer(Version.LUCENE_31, reader),
					new HashSet(Arrays.asList(new String[] {"bez", "a"}))
				),
				tagger
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
		
		System.out.println(buf);
		
		assertEquals(buf.toString().trim(),
				"This/1 is/1 a/1 test/1 of/1 the/1 english/1 keyword/1 a1/0 a2/0 a3/0 analyzer/1");
	}
	
}
