package org.apache.lucene.newseman;

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
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.newseman.TestSemanticTaggerTokenFilter.TestFilter;
import org.apache.lucene.util.Version;

import java.io.StringReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class TestSemanticTaggerTokenFilter extends BaseTokenStreamTestCase {

	public static final class TestFilter extends TokenFilter {
		private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
		private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
		private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);

		/**
		 * Filter which discards the token 'stop' and which expands the token
		 * 'phrase' into 'phrase1 phrase2' and token 'keyword' into 'a1 a2 a3'
		 */
		public TestFilter(TokenStream in) {
			super(in);
		}

		boolean inPhrase = false;
		int savedStart = 0, savedEnd = 0;
		private List<String> stack = new ArrayList<String>();

		@Override
		public boolean incrementToken() throws IOException {
			if (inPhrase) {
				inPhrase = false;
				clearAttributes();
				termAtt.setEmpty().append("phrase2");
				offsetAtt.setOffset(savedStart, savedEnd);
				return true;
			} else if (stack.size() > 0) {
				termAtt.setEmpty().append((String) stack.remove(0));
				posIncrAtt.setPositionIncrement(0);
				return true;
			} else {
				while (input.incrementToken()) {
					if (termAtt.toString().equals("phrase")) {
						inPhrase = true;
						savedStart = offsetAtt.startOffset();
						savedEnd = offsetAtt.endOffset();
						termAtt.setEmpty().append("phrase1");
						offsetAtt.setOffset(savedStart, savedEnd);
						return true;
					} else if (termAtt.toString().equals("keyword")) {
						stack.add("a1");
						stack.add("a2");
						stack.add("a3");
						return true;
					} else if (!termAtt.toString().equals("stop")) {
						return true;
					}
				}
			}
			return false;
		}
	}

	private Set<Object> inValidTokens = new HashSet<Object>();

	@Override
	public void setUp() throws Exception {
		super.setUp();
	}

	public void testDefaults() throws IOException {
		StringReader reader = new StringReader(
				"This is a test of the english keyword analyzer");
		TestFilter stream = new TestFilter(new WhitespaceTokenizer(
				Version.LUCENE_31, reader));
		assertTrue(stream != null);
		CharTermAttribute termAtt = stream
				.getAttribute(CharTermAttribute.class);
		

		StringBuffer buf = new StringBuffer();
		while (stream.incrementToken()) {
			buf.append(termAtt.toString());
			buf.append(" ");
		}
		assertEquals(buf.toString().trim(),
				"This is a test of the english keyword a1 a2 a3 analyzer");

	}

	public void testStopList() throws IOException {
		StringReader reader = new StringReader(
				"This is a test of the stop keyword analyzer");
		TestFilter stream = new TestFilter(new WhitespaceTokenizer(
				Version.LUCENE_31, reader));
		assertTrue(stream != null);
		CharTermAttribute termAtt = stream
				.getAttribute(CharTermAttribute.class);

		StringBuffer buf = new StringBuffer();
		while (stream.incrementToken()) {
			buf.append(termAtt.toString());
			buf.append(" ");
		}
		assertEquals(buf.toString().trim(),
				"This is a test of the keyword a1 a2 a3 analyzer");
	}

	public void testStopListPositions() throws IOException {
		StringReader reader = new StringReader(
				"This is a test of the english stop keyword analyzer");
		TestFilter stream = new TestFilter(new StandardTokenizer(
				Version.LUCENE_31, reader));
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
