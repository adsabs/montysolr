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
package org.apache.lucene.analysis.synonym;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.util.CharsRefBuilder;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.Arrays;

/**
 * Parser for wordnet prolog format
 * <p>
 * See http://wordnet.princeton.edu/man/prologdb.5WN.html for a description of the format.
 *
 * @lucene.experimental
 */
// TODO: allow you to specify syntactic categories (e.g. just nouns, etc)
public class NewWordnetSynonymParser extends NewSynonymFilterFactory.SynonymParser {
    private final boolean expand;

    public NewWordnetSynonymParser(boolean dedup, boolean expand, Analyzer analyzer) {
        super(dedup, analyzer);
        this.expand = expand;
    }

    @Override
    public void parse(Reader in) throws IOException, ParseException {
        LineNumberReader br = new LineNumberReader(in);
        try {
            String line = null;
            String lastSynSetID = "";
            CharsRef[] synset = new CharsRef[8];
            int synsetSize = 0;

            while ((line = br.readLine()) != null) {
                String synSetID = line.substring(2, 11);

                if (!synSetID.equals(lastSynSetID)) {
                    addInternal(synset, synsetSize);
                    synsetSize = 0;
                }

                if (synset.length <= synsetSize + 1) {
                    synset = Arrays.copyOf(synset, synset.length * 2);
                }

                synset[synsetSize] = parseSynonym(line, new CharsRefBuilder());
                synsetSize++;
                lastSynSetID = synSetID;
            }

            // final synset in the file
            addInternal(synset, synsetSize);
        } catch (IllegalArgumentException e) {
            ParseException ex = new ParseException("Invalid synonym rule at line " + br.getLineNumber(), 0);
            ex.initCause(e);
            throw ex;
        } finally {
            br.close();
        }
    }

    private CharsRef parseSynonym(String line, CharsRefBuilder reuse) throws IOException {
        if (reuse == null) {
            reuse = new CharsRefBuilder();
        }

        int start = line.indexOf('\'') + 1;
        int end = line.lastIndexOf('\'');

        String text = line.substring(start, end).replace("''", "'");
        return analyze(text, reuse);
    }

    private void addInternal(CharsRef[] synset, int size) {
        if (size <= 1) {
            return; // nothing to do
        }

        if (expand) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    add(synset[i], synset[j], false);
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                add(synset[i], synset[0], false);
            }
        }
    }
}
