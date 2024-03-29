package org.apache.lucene.analysis.core;

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

import org.apache.lucene.analysis.CharacterUtils;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import java.io.IOException;

/**
 * Normalizes token text to lower case.
 * <p>You must specify the required {@link Version}
 * compatibility when creating LowerCaseFilter:
 * <p>
 * As of 3.1, supplementary characters are properly lowercased.
 */
public final class SelectiveLowerCaseFilter extends TokenFilter {
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

    /**
     * Create a new LowerCaseFilter, that normalizes token text to lower case.
     *
     * @param matchVersion version
     * @param in           TokenStream to filter
     */
    public SelectiveLowerCaseFilter(Version matchVersion, TokenStream in) {
        super(in);
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (input.incrementToken()) {
            if (AcronymTokenFilter.termIsAcronym(termAtt.toString())) {
                return true; // skip lowercasing
            }
            CharacterUtils.toLowerCase(termAtt.buffer(), 0, termAtt.length());
            return true;

        } else
            return false;
    }

}
