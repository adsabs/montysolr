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
package org.apache.lucene.analysis.core;


import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.FilteringTokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Removes stop words from a token stream.
 */
public final class AqpStopFilter extends FilteringTokenFilter {

    private final CharArraySet stopWords;
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
    private final boolean ignorePosition;
    private int counter = 0;

    /**
     * Constructs a filter which removes words from the input TokenStream that are
     * named in the Set.
     *
     * @param in        Input stream
     * @param stopWords A {@link CharArraySet} representing the stopwords.
     * @see #makeStopSet(java.lang.String...)
     */
    public AqpStopFilter(TokenStream in, CharArraySet stopWords, boolean ignorePosition) {
        super(in);
        this.stopWords = stopWords;
        this.ignorePosition = ignorePosition;
    }

    /**
     * Builds a Set from an array of stop words,
     * appropriate for passing into the StopFilter constructor.
     * This permits this stopWords construction to be cached once when
     * an Analyzer is constructed.
     *
     * @param stopWords An array of stopwords
     * @see #makeStopSet(java.lang.String[], boolean) passing false to ignoreCase
     */
    public static CharArraySet makeStopSet(String... stopWords) {
        return makeStopSet(stopWords, false);
    }

    /**
     * Builds a Set from an array of stop words,
     * appropriate for passing into the StopFilter constructor.
     * This permits this stopWords construction to be cached once when
     * an Analyzer is constructed.
     *
     * @param stopWords A List of Strings or char[] or any other toString()-able list representing the stopwords
     * @return A Set ({@link CharArraySet}) containing the words
     * @see #makeStopSet(java.lang.String[], boolean) passing false to ignoreCase
     */
    public static CharArraySet makeStopSet(List<?> stopWords) {
        return makeStopSet(stopWords, false);
    }

    /**
     * Creates a stopword set from the given stopword array.
     *
     * @param stopWords  An array of stopwords
     * @param ignoreCase If true, all words are lower cased first.
     * @return a Set containing the words
     */
    public static CharArraySet makeStopSet(String[] stopWords, boolean ignoreCase) {
        CharArraySet stopSet = new CharArraySet(stopWords.length, ignoreCase);
        stopSet.addAll(Arrays.asList(stopWords));
        return stopSet;
    }

    /**
     * Creates a stopword set from the given stopword list.
     *
     * @param stopWords  A List of Strings or char[] or any other toString()-able list representing the stopwords
     * @param ignoreCase if true, all words are lower cased first
     * @return A Set ({@link CharArraySet}) containing the words
     */
    public static CharArraySet makeStopSet(List<?> stopWords, boolean ignoreCase) {
        CharArraySet stopSet = new CharArraySet(stopWords.size(), ignoreCase);
        stopSet.addAll(stopWords);
        return stopSet;
    }

    /**
     * Returns the next input Token whose term() is not a stop word.
     */
    @Override
    protected boolean accept() {
        boolean v = stopWords.contains(termAtt.buffer(), 0, termAtt.length());
        counter++;
        if (v && ignorePosition && posIncrAtt.getPositionIncrement() == 1 && counter > 1) {
            posIncrAtt.setPositionIncrement(0);
        }
        return !v;
    }

    @Override
    public void end() throws IOException {
        super.end();
        counter = 0;
    }

}
