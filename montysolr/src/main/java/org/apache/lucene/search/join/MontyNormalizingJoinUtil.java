package org.apache.lucene.search.join;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.DocValues;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.SortedDocValues;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchNoDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorable;
import org.apache.lucene.search.SimpleCollector;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.BytesRefHash;

/**
 * Query-time join, like {@link JoinUtil#createJoinQuery(String, boolean, String, Query,
 * IndexSearcher, ScoreMode)}, but normalizes the collected "from" field values through toField's
 * index analyzer before seeking them against toField's term dictionary.
 *
 * <p>This exists because fields backed by {@code SortableTextField} (e.g. ADS's
 * {@code identifier_string}) store their DocValues from the raw, pre-analysis value, while their
 * postings are built from the fully analyzed value. {@link JoinUtil} collects join values from
 * DocValues and seeks them directly against the term dictionary, so it silently misses matches
 * whenever the field's analyzer chain normalizes the value (case folding, punctuation stripping,
 * etc).
 *
 * <p>Deliberately narrower than {@link JoinUtil}: only single-valued "from" fields and {@link
 * ScoreMode#Avg} are supported, since those are the only combination montysolr's
 * {@code joincitations}/{@code joinreferences} functions need.
 */
public final class MontyNormalizingJoinUtil {

    private MontyNormalizingJoinUtil() {}

    /**
     * @param fromField    single-valued field to collect join values from (read from its
     *                     SortedDocValues, i.e. the raw/unanalyzed value)
     * @param toField      field to seek the normalized join values against (its term dictionary,
     *                     i.e. the analyzed value)
     * @param toAnalyzer   toField's index analyzer, e.g.
     *                     {@code schema.getFieldType(toField).getIndexAnalyzer()}
     * @param fromQuery    query selecting the "from" documents
     * @param fromSearcher searcher used to run fromQuery
     */
    public static Query createJoinQuery(
            String fromField,
            String toField,
            Analyzer toAnalyzer,
            Query fromQuery,
            IndexSearcher fromSearcher)
            throws IOException {

        RawTermsAverageCollector collector = new RawTermsAverageCollector(fromField);
        fromSearcher.search(fromQuery, collector);

        BytesRefHash normalizedTerms = new BytesRefHash();
        float[] normalizedScores = new float[0];
        int[] normalizedScoreCounts = new int[0];

        BytesRefHash rawTerms = collector.collectedTerms;
        float[] rawAverageScores = collector.getAverageScoresPerTerm();
        BytesRef spare = new BytesRef();
        for (int rawTermOrd = 0; rawTermOrd < rawTerms.size(); rawTermOrd++) {
            BytesRef rawValue = rawTerms.get(rawTermOrd, spare);
            BytesRef normalizedValue = analyzeToSingleTerm(toAnalyzer, toField, rawValue.utf8ToString());

            int normalizedTermOrd = normalizedTerms.add(normalizedValue);
            if (normalizedTermOrd < 0) {
                normalizedTermOrd = -normalizedTermOrd - 1;
            } else {
                normalizedScores = ArrayUtil.grow(normalizedScores, normalizedTermOrd + 1);
                normalizedScoreCounts = ArrayUtil.grow(normalizedScoreCounts, normalizedTermOrd + 1);
            }

            int rawCount = collector.scoreCounts[rawTermOrd];
            // Preserve raw-average rounding before combining terms that normalize to the same value.
            normalizedScores[normalizedTermOrd] += rawAverageScores[rawTermOrd] * rawCount;
            normalizedScoreCounts[normalizedTermOrd] += rawCount;
        }

        if (normalizedTerms.size() == 0) {
            return new MatchNoDocsQuery("MontyNormalizingJoinUtil: no join values collected");
        }

        for (int i = 0; i < normalizedTerms.size(); i++) {
            normalizedScores[i] /= normalizedScoreCounts[i];
        }

        return new MontyNormalizingTermsIncludingScoreQuery(
                toField,
                normalizedTerms,
                normalizedScores,
                fromField,
                fromQuery,
                fromSearcher.getTopReaderContext().id());
    }

    private static BytesRef analyzeToSingleTerm(Analyzer analyzer, String field, String value)
            throws IOException {
        try (TokenStream tokenStream = analyzer.tokenStream(field, value)) {
            CharTermAttribute termAttribute = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            if (!tokenStream.incrementToken()) {
                throw new IllegalStateException(
                        "Analyzer for field '" + field + "' produced zero tokens for value: " + value);
            }

            BytesRef result = new BytesRef(termAttribute.toString());
            if (tokenStream.incrementToken()) {
                throw new IllegalStateException(
                        "Analyzer for field '"
                                + field
                                + "' produced multiple tokens for value: "
                                + value
                                + " -- MontyNormalizingJoinUtil requires a single-token"
                                + " (keyword-tokenizer-based) analyzer chain for join fields");
            }

            tokenStream.end();
            return result;
        }
    }

    /** Collects raw (unanalyzed) single-valued term values and averages the query score per term. */
    private static final class RawTermsAverageCollector extends SimpleCollector {

        private final String field;
        final BytesRefHash collectedTerms = new BytesRefHash();
        float[] scoreSums = new float[0];
        int[] scoreCounts = new int[0];

        private SortedDocValues docValues;
        private Scorable scorer;

        RawTermsAverageCollector(String field) {
            this.field = field;
        }

        float[] getAverageScoresPerTerm() {
            float[] averageScores = new float[collectedTerms.size()];
            for (int i = 0; i < averageScores.length; i++) {
                averageScores[i] = scoreSums[i] / scoreCounts[i];
            }
            return averageScores;
        }

        @Override
        public void collect(int doc) throws IOException {
            BytesRef value;
            if (docValues.advanceExact(doc)) {
                value = docValues.lookupOrd(docValues.ordValue());
            } else {
                value = new BytesRef(BytesRef.EMPTY_BYTES);
            }

            int termOrd = collectedTerms.add(value);
            if (termOrd < 0) {
                termOrd = -termOrd - 1;
            } else {
                scoreSums = ArrayUtil.grow(scoreSums, termOrd + 1);
                scoreCounts = ArrayUtil.grow(scoreCounts, termOrd + 1);
            }

            scoreSums[termOrd] += scorer.score();
            scoreCounts[termOrd]++;
        }

        @Override
        protected void doSetNextReader(LeafReaderContext context) throws IOException {
            docValues = DocValues.getSorted(context.reader(), field);
        }

        @Override
        public void setScorer(Scorable scorer) throws IOException {
            this.scorer = scorer;
        }

        @Override
        public org.apache.lucene.search.ScoreMode scoreMode() {
            return org.apache.lucene.search.ScoreMode.COMPLETE;
        }
    }
}
