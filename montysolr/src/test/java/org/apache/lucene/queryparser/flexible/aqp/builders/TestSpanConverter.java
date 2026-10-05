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
package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.queries.spans.SpanNearQuery;
import org.apache.lucene.queries.spans.SpanQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.tests.util.LuceneTestCase;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class TestSpanConverter extends LuceneTestCase {

    @Test
    public void testMultiPhraseAlternativesAndPositionHole() throws Exception {
        try (Directory directory = newDirectory();
             Analyzer analyzer = new WhitespaceAnalyzer();
             IndexWriter writer = new IndexWriter(directory, newIndexWriterConfig(analyzer))) {
            addDocument(writer, "left-match", new String[]{"left", "right"},
                    new int[]{1, 2});
            addDocument(writer, "alternate-match", new String[]{"alternate", "right"},
                    new int[]{1, 2});
            addDocument(writer, "compacted", new String[]{"left", "right"},
                    new int[]{1, 1});
            addDocument(writer, "expanded", new String[]{"left", "right"},
                    new int[]{1, 3});
            addDocument(writer, "too-far", new String[]{"left", "right"},
                    new int[]{1, 4});
            addDocument(writer, "extra-gap", new String[]{"alternate", "right"},
                    new int[]{1, 3});
            addDocument(writer, "reversed", new String[]{"right", "alternate"},
                    new int[]{1, 2});
            writer.commit();

            MultiPhraseQuery.Builder queryBuilder = new MultiPhraseQuery.Builder();
            queryBuilder.add(new Term[]{new Term("body", "left"), new Term("body", "alternate")}, 0);
            queryBuilder.add(new Term[]{new Term("body", "right")}, 2);
            queryBuilder.setSlop(0);
            SpanQuery query = new SpanConverter().getSpanQuery(
                    new SpanConverterContainer(queryBuilder.build(), 0, true));

            try (DirectoryReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = newSearcher(reader);
                TopDocs hits = searcher.search(query, 10);
                assertEquals(2, hits.totalHits.value);
                Set<String> ids = new HashSet<>();
                for (var hit : hits.scoreDocs) {
                    ids.add(searcher.doc(hit.doc).get("id"));
                }
                assertEquals(Set.of("left-match", "alternate-match"), ids);
                PhraseQuery.Builder sloppyPhrase = new PhraseQuery.Builder();
                sloppyPhrase.add(new Term("body", "left"), 0);
                sloppyPhrase.add(new Term("body", "right"), 2);
                sloppyPhrase.setSlop(1);
                SpanQuery sloppySpan = new SpanConverter().getSpanQuery(
                        new SpanConverterContainer(sloppyPhrase.build(), 0, true));
                TopDocs sloppyHits = searcher.search(sloppySpan, 10);
                Set<String> sloppyIds = new HashSet<>();
                for (var hit : sloppyHits.scoreDocs) {
                    sloppyIds.add(searcher.doc(hit.doc).get("id"));
                }
                assertEquals(Set.of("left-match", "compacted", "expanded"), sloppyIds);
            }
        }
    }

    @Test
    public void testMultiplePositionHolesRespectPhraseSlop() throws Exception {
        try (Directory directory = newDirectory();
             Analyzer analyzer = new WhitespaceAnalyzer();
             IndexWriter writer = new IndexWriter(directory, newIndexWriterConfig(analyzer))) {
            addDocument(writer, "exact", new String[]{"first", "middle", "last"},
                    new int[]{1, 2, 2});
            addDocument(writer, "two-contractions", new String[]{"first", "middle", "last"},
                    new int[]{1, 1, 1});
            addDocument(writer, "one-contraction", new String[]{"first", "middle", "last"},
                    new int[]{1, 1, 2});
            addDocument(writer, "one-expansion", new String[]{"first", "middle", "last"},
                    new int[]{1, 2, 3});
            addDocument(writer, "two-adjustments", new String[]{"first", "middle", "last"},
                    new int[]{1, 1, 4});
            writer.commit();

            PhraseQuery.Builder phraseBuilder = new PhraseQuery.Builder();
            phraseBuilder.add(new Term("body", "first"), 0);
            phraseBuilder.add(new Term("body", "middle"), 2);
            phraseBuilder.add(new Term("body", "last"), 4);
            phraseBuilder.setSlop(1);
            PhraseQuery phrase = phraseBuilder.build();
            SpanQuery converted = new SpanConverter().getSpanQuery(
                    new SpanConverterContainer(phrase, 0, true));

            try (DirectoryReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = newSearcher(reader);
                Set<String> phraseIds = new HashSet<>();
                for (var hit : searcher.search(phrase, 10).scoreDocs) {
                    phraseIds.add(searcher.doc(hit.doc).get("id"));
                }
                Set<String> convertedIds = new HashSet<>();
                for (var hit : searcher.search(converted, 10).scoreDocs) {
                    convertedIds.add(searcher.doc(hit.doc).get("id"));
                }
                assertEquals(Set.of("exact", "one-contraction", "one-expansion"), phraseIds);
                assertEquals(phraseIds, convertedIds);
            }
        }
    }


    @Test
    public void testLargePositionSlopUsesBoundedSpanNear() throws Exception {
        PhraseQuery.Builder phraseBuilder = new PhraseQuery.Builder();
        phraseBuilder.add(new Term("body", "first"), 0);
        phraseBuilder.add(new Term("body", "middle"), 2);
        phraseBuilder.add(new Term("body", "last"), 4);
        phraseBuilder.setSlop(1_000_000);

        SpanQuery converted = new SpanConverter().getSpanQuery(
                new SpanConverterContainer(phraseBuilder.build(), 0, true));
        assertTrue(converted instanceof SpanNearQuery);
        assertEquals(5, ((SpanNearQuery) converted).getClauses().length);
    }

    private static void addDocument(IndexWriter writer, String id, String[] terms,
                                    int[] positionIncrements) throws IOException {
        Document document = new Document();
        document.add(new StringField("id", id, Field.Store.YES));
        document.add(new Field("body", new PositionedTokenStream(terms, positionIncrements),
                TextField.TYPE_NOT_STORED));
        writer.addDocument(document);
    }

    private static final class PositionedTokenStream extends TokenStream {
        private final String[] terms;
        private final int[] positionIncrements;
        private int index;
        private final CharTermAttribute term = addAttribute(CharTermAttribute.class);
        private final PositionIncrementAttribute positionIncrement =
                addAttribute(PositionIncrementAttribute.class);

        private PositionedTokenStream(String[] terms, int[] positionIncrements) {
            if (terms.length != positionIncrements.length) {
                throw new IllegalArgumentException("Each term must have a position increment");
            }
            this.terms = terms;
            this.positionIncrements = positionIncrements;
        }

        @Override
        public boolean incrementToken() {
            if (index == terms.length) {
                return false;
            }
            clearAttributes();
            term.append(terms[index]);
            positionIncrement.setPositionIncrement(positionIncrements[index]);
            index++;
            return true;
        }

        @Override
        public void reset() throws IOException {
            super.reset();
            index = 0;
        }
    }
}
