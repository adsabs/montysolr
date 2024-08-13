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
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.util.ResourceLoader;
import org.apache.lucene.util.ResourceLoaderAware;
import org.apache.lucene.analysis.TokenFilterFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Modified StopFilterFactory, because the 'wise' people of Lucene:
 * - removed ability to 'not count' gaps
 * - and also, in their infinite wisdom, made the option private
 * (so the poor devs have not other chance then to fork)
 */

public class AqpStopFilterFactory extends TokenFilterFactory implements ResourceLoaderAware {
    public static final String FORMAT_WORDSET = "wordset";
    public static final String FORMAT_SNOWBALL = "snowball";

    private CharArraySet stopWords;
    private final String stopWordFiles;
    private final String format;
    private final boolean ignoreCase;
    private final boolean ignorePosition;

    /**
     * Creates a new StopFilterFactory
     */
    public AqpStopFilterFactory(Map<String, String> args) {
        super(args);
        stopWordFiles = get(args, "words");
        format = get(args, "format", (null == stopWordFiles ? null : FORMAT_WORDSET));
        ignoreCase = getBoolean(args, "ignoreCase", false);
        ignorePosition = getBoolean(args, "ignorePosition", true);
        if (!args.isEmpty()) {
            throw new IllegalArgumentException("Unknown parameters: " + args);
        }
    }

    @Override
    public void inform(ResourceLoader loader) throws IOException {
        if (stopWordFiles != null) {
            if (FORMAT_WORDSET.equalsIgnoreCase(format)) {
                stopWords = getWordSet(loader, stopWordFiles, ignoreCase);
            } else if (FORMAT_SNOWBALL.equalsIgnoreCase(format)) {
                stopWords = getSnowballWordSet(loader, stopWordFiles, ignoreCase);
            } else {
                throw new IllegalArgumentException("Unknown 'format' specified for 'words' file: " + format);
            }
        } else {
            if (null != format) {
                throw new IllegalArgumentException("'format' can not be specified w/o an explicit 'words' file: " + format);
            }
            stopWords = new CharArraySet(EnglishAnalyzer.ENGLISH_STOP_WORDS_SET, ignoreCase);
        }
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public CharArraySet getStopWords() {
        return stopWords;
    }

    @Override
    public TokenStream create(TokenStream input) {
        AqpStopFilter stopFilter = new AqpStopFilter(input, stopWords, ignorePosition);
        return stopFilter;
    }
}
