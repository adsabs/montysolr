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

        RawTermsAvgCollector collector = new RawTermsAvgCollector(fromField);
        fromSearcher.search(fromQuery, collector);

        BytesRefHash normalizedTerms = new BytesRefHash();
        float[] normScoreSums = new float[0];
        int[] normScoreCounts = new int[0];

        BytesRefHash rawTerms = collector.collectedTerms;
        float[] rawAvgScores = collector.getAverageScoresPerTerm();
        BytesRef spare = new BytesRef();
        for (int rawOrd = 0; rawOrd < rawTerms.size(); rawOrd++) {
            BytesRef rawValue = rawTerms.get(rawOrd, spare);
            BytesRef normValue = analyzeToSingleTerm(toAnalyzer, toField, rawValue.utf8ToString());

            int normOrd = normalizedTerms.add(normValue);
            if (normOrd < 0) {
                normOrd = -normOrd - 1;
            } else {
                normScoreSums = ArrayUtil.grow(normScoreSums, normOrd + 1);
                normScoreCounts = ArrayUtil.grow(normScoreCounts, normOrd + 1);
            }

            int rawCount = collector.scoreCounts[rawOrd];
            normScoreSums[normOrd] += rawAvgScores[rawOrd] * rawCount;
            normScoreCounts[normOrd] += rawCount;
        }

        if (normalizedTerms.size() == 0) {
            return new MatchNoDocsQuery("MontyNormalizingJoinUtil: no join values collected");
        }

        for (int i = 0; i < normalizedTerms.size(); i++) {
            normScoreSums[i] /= normScoreCounts[i];
        }

        return new MontyNormalizingTermsIncludingScoreQuery(
                toField,
                normalizedTerms,
                normScoreSums,
                fromField,
                fromQuery,
                fromSearcher.getTopReaderContext().id());
    }

    private static BytesRef analyzeToSingleTerm(Analyzer analyzer, String field, String value)
            throws IOException {
        try (TokenStream ts = analyzer.tokenStream(field, value)) {
            CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);
            ts.reset();
            if (!ts.incrementToken()) {
                throw new IllegalStateException(
                        "Analyzer for field '" + field + "' produced zero tokens for value: " + value);
            }
            BytesRef result = new BytesRef(termAtt.toString());
            if (ts.incrementToken()) {
                throw new IllegalStateException(
                        "Analyzer for field '"
                                + field
                                + "' produced multiple tokens for value: "
                                + value
                                + " -- MontyNormalizingJoinUtil requires a single-token"
                                + " (keyword-tokenizer-based) analyzer chain for join fields");
            }
            ts.end();
            return result;
        }
    }

    /** Collects raw (unanalyzed) single-valued term values and averages the query score per term. */
    private static final class RawTermsAvgCollector extends SimpleCollector {

        private final String field;
        final BytesRefHash collectedTerms = new BytesRefHash();
        float[] scoreSums = new float[0];
        int[] scoreCounts = new int[0];

        private SortedDocValues docValues;
        private Scorable scorer;

        RawTermsAvgCollector(String field) {
            this.field = field;
        }

        float[] getAverageScoresPerTerm() {
            float[] avg = new float[collectedTerms.size()];
            for (int i = 0; i < avg.length; i++) {
                avg[i] = scoreSums[i] / scoreCounts[i];
            }
            return avg;
        }

        @Override
        public void collect(int doc) throws IOException {
            BytesRef value;
            if (docValues.advanceExact(doc)) {
                value = docValues.lookupOrd(docValues.ordValue());
            } else {
                value = new BytesRef(BytesRef.EMPTY_BYTES);
            }
            int ord = collectedTerms.add(value);
            if (ord < 0) {
                ord = -ord - 1;
            } else {
                scoreSums = ArrayUtil.grow(scoreSums, ord + 1);
                scoreCounts = ArrayUtil.grow(scoreCounts, ord + 1);
            }
            scoreSums[ord] += scorer.score();
            scoreCounts[ord]++;
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
