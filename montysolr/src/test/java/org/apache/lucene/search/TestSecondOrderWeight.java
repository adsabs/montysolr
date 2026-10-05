package org.apache.lucene.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.function.FunctionScoreQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.tests.util.LuceneTestCase;
import org.junit.Test;

import java.io.IOException;

public class TestSecondOrderWeight extends LuceneTestCase {

    @Test
    public void testCollectorResultsAreNotRestrictedToInnerQueryMatches() throws Exception {
        try (Directory directory = newDirectory();
             Analyzer analyzer = new WhitespaceAnalyzer();
             IndexWriter writer = new IndexWriter(directory, newIndexWriterConfig(analyzer))) {
            addDocument(writer, "seed-one", "seed first");
            addDocument(writer, "seed-only", "seed second");
            addDocument(writer, "external", "not-seed");

            try (DirectoryReader reader = DirectoryReader.open(writer)) {
                IndexSearcher searcher = newSearcher(reader);
                int externalDoc = findDoc(searcher, "external");
                int seedOnlyDoc = findDoc(searcher, "seed-only");
                SecondOrderQuery query = new SecondOrderQuery(
                        new TermQuery(new Term("body", "seed")),
                        new EmitDocCollector(externalDoc, 4.25f));

                assertFalse(searcher.explain(query.getQuery(), externalDoc).isMatch());

                TopDocs results = searcher.search(query, reader.maxDoc());
                assertEquals(1, results.scoreDocs.length);
                assertEquals(externalDoc, results.scoreDocs[0].doc);
                assertEquals(4.25f, results.scoreDocs[0].score, 0.0f);

                Explanation externalExplanation = searcher.explain(query, externalDoc);
                assertTrue("collector result must explain as a match", externalExplanation.isMatch());
                assertEquals(4.25f, externalExplanation.getValue().floatValue(), 0.0f);

                Explanation seedExplanation = searcher.explain(query, seedOnlyDoc);
                assertFalse("inner-only document is not a second-order result", seedExplanation.isMatch());
            }
        }
    }

    @Test
    public void testFunctionScoreMultipliesCollectedScore() throws Exception {
        try (Directory directory = newDirectory();
             Analyzer analyzer = new WhitespaceAnalyzer();
             IndexWriter writer = new IndexWriter(directory, newIndexWriterConfig(analyzer))) {
            addDocument(writer, "seed", "seed");
            addDocument(writer, "external", "not-seed");

            try (DirectoryReader reader = DirectoryReader.open(writer)) {
                IndexSearcher searcher = newSearcher(reader);
                int externalDoc = findDoc(searcher, "external");
                SecondOrderQuery secondOrder = new SecondOrderQuery(
                        new TermQuery(new Term("body", "seed")),
                        new EmitDocCollector(externalDoc, 4.25f));

                int seedDoc = findDoc(searcher, "seed");
                Query boosted = FunctionScoreQuery.boostByValue(
                        secondOrder, DoubleValuesSource.constant(3.0d));

                TopDocs results = searcher.search(boosted, reader.maxDoc());
                assertEquals(1, results.scoreDocs.length);
                assertEquals(externalDoc, results.scoreDocs[0].doc);
                assertEquals(12.75f, results.scoreDocs[0].score, 0.0001f);

                Explanation boostedExplanation = searcher.explain(boosted, externalDoc);
                assertTrue(boostedExplanation.isMatch());
                assertEquals(12.75f, boostedExplanation.getValue().floatValue(), 0.0001f);

                Explanation seedExplanation = searcher.explain(boosted, seedDoc);
                assertFalse("inner-only document must remain a no-match through FunctionScoreQuery",
                        seedExplanation.isMatch());

                Query boostedByQuery = new BoostQuery(
                        new SecondOrderQuery(new TermQuery(new Term("body", "seed")),
                                new EmitDocCollector(externalDoc, 4.25f)),
                        3.0f);
                TopDocs boostResults = searcher.search(boostedByQuery, reader.maxDoc());
                assertEquals(1, boostResults.scoreDocs.length);
                assertEquals(externalDoc, boostResults.scoreDocs[0].doc);
                assertEquals(12.75f, boostResults.scoreDocs[0].score, 0.0001f);
                Explanation boostExplanation = searcher.explain(boostedByQuery, externalDoc);
                assertTrue(boostExplanation.isMatch());
                assertEquals(12.75f, boostExplanation.getValue().floatValue(), 0.0001f);
                assertFalse("inner-only document must remain a no-match through BoostQuery",
                        searcher.explain(boostedByQuery, seedDoc).isMatch());
            }
        }
    }

    @Test
    public void testAgrestiCoullScoresRemainNonnegative() throws Exception {
        try (Directory directory = newDirectory();
             Analyzer analyzer = new WhitespaceAnalyzer();
             IndexWriter writer = new IndexWriter(directory, newIndexWriterConfig(analyzer))) {
            addDocument(writer, "seed", "seed");
            addDocument(writer, "external", "not-seed");

            try (DirectoryReader reader = DirectoryReader.open(writer)) {
                IndexSearcher searcher = newSearcher(reader);
                int externalDoc = findDoc(searcher, "external");
                SecondOrderQuery query = new SecondOrderQuery(
                        new TermQuery(new Term("body", "seed")),
                        new EmitDocCollector(externalDoc, 1.0f));
                query.setFinalValueType(SecondOrderCollector.FinalValueType.AGRESTI_COULL);

                TopDocs results = searcher.search(query, reader.maxDoc());
                assertEquals(1, results.scoreDocs.length);
                assertEquals(externalDoc, results.scoreDocs[0].doc);
                assertEquals(0.0f, results.scoreDocs[0].score, 0.0f);
                Explanation explanation = searcher.explain(query, externalDoc);
                assertTrue(explanation.isMatch());
                assertEquals(0.0f, explanation.getValue().floatValue(), 0.0f);
            }
        }
    }


    private static void addDocument(IndexWriter writer, String id, String body) throws IOException {
        Document document = new Document();
        document.add(new StringField("id", id, Field.Store.YES));
        document.add(new TextField("body", body, Field.Store.NO));
        writer.addDocument(document);
    }

    private static int findDoc(IndexSearcher searcher, String id) throws IOException {
        TopDocs hits = searcher.search(new TermQuery(new Term("id", id)), 1);
        assertEquals(1, hits.scoreDocs.length);
        return hits.scoreDocs[0].doc;
    }

    private static final class EmitDocCollector extends AbstractSecondOrderCollector {
        private final int outputDoc;
        private final float outputScore;

        private EmitDocCollector(int outputDoc, float outputScore) {
            this.outputDoc = outputDoc;
            this.outputScore = outputScore;
        }

        @Override
        public void collect(int doc) {
            hits.add(new CollectorDoc(outputDoc, outputScore));
        }

        @Override
        public ScoreMode scoreMode() {
            return ScoreMode.COMPLETE_NO_SCORES;
        }

        @Override
        public SecondOrderCollector copy() {
            return new EmitDocCollector(outputDoc, outputScore);
        }

        @Override
        public int hashCode() {
            return 31 * outputDoc + Float.floatToIntBits(outputScore);
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof EmitDocCollector)) {
                return false;
            }
            EmitDocCollector that = (EmitDocCollector) other;
            return outputDoc == that.outputDoc
                    && Float.floatToIntBits(outputScore) == Float.floatToIntBits(that.outputScore);
        }
    }
}
