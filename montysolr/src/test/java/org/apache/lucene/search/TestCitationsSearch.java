package org.apache.lucene.search;

import monty.solr.util.MontySolrAbstractTestCase;
import monty.solr.util.MontySolrSetup;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.util.BytesRefBuilder;
import org.apache.solr.legacy.LegacyNumericUtils;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.CitationCache;
import org.apache.solr.search.SolrIndexSearcher;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@SuppressWarnings({"rawtypes", "unchecked"})
public class TestCitationsSearch extends MontySolrAbstractTestCase {

    private final boolean debug = false;
    private SolrQueryRequest tempReq;

    @BeforeClass
    public static void beforeClass() throws Exception {
        makeResourcesVisible(Thread.currentThread().getContextClassLoader(), MontySolrSetup.getMontySolrHome() + "/contrib/examples/adsabs/server/solr/collection1/conf",
                MontySolrSetup.getSolrHome() + "/example/solr/collection1/conf");

        System.setProperty("solr.allow.unsafe.resourceloading", "true");
        schemaString = MontySolrSetup.getMontySolrHome()
                + "/contrib/adsabs/src/test-files/solr/collection1/conf/"
                + "schema-citations-transformer.xml";

        configString = MontySolrSetup.getMontySolrHome()
                + "/contrib/adsabs/src/test-files/solr/collection1/conf/"
                + "citation-cache-solrconfig.xml";

        initCore(configString, schemaString, MontySolrSetup.getSolrHome()
                + "/example/solr");
    }


    @Override
    public void setUp() throws Exception {
        if (debug) {
            // TODO: set the codec new File(TEMP_DIR,"index-citations")
        }
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        if (tempReq != null) {
            tempReq.close();
        }
        super.tearDown();
    }

    public HashMap<Integer, int[]> createRandomDocs(int start, int numDocs) throws IOException {
        Random randomSeed = new Random();

        int[] randData = new int[numDocs / 10];
        for (int i = 0; i < randData.length; i++) {
            randData[i] = Math.abs(randomSeed.nextInt(numDocs) - start);
        }


        int x = 0;
        int[][] randi = new int[numDocs - start][];
        for (int i = 0; i < numDocs - start; i++) {
            int howMany = randomSeed.nextInt(6);
            randi[i] = new int[howMany];
            for (int j = 0; j < howMany; j++) {
                if (x >= randData.length) {
                    x = 0;
                }
                randi[i][j] = randData[x++];
            }
        }

        HashMap<Integer, int[]> data = new HashMap<Integer, int[]>(randi.length);
        List<String> thisDoc = new ArrayList<String>();

        for (int k = 0; k < randi.length; k++) {
            thisDoc.clear();
            thisDoc.add("id");
            thisDoc.add(String.valueOf(k + start));
            thisDoc.add("bibcode");
            thisDoc.add("b" + (k + start));
            thisDoc.add("year");
            if (k % 2 == 0) {
                thisDoc.add("2000");
            } else {
                thisDoc.add("1995");
            }
            int[] row = new int[randi[k].length];

            x = 0;
            for (int v : randi[k]) {
                row[x] = v + start;
                thisDoc.add("reference");
                thisDoc.add("b" + (v + start));
                thisDoc.add("ireference");
                thisDoc.add(String.valueOf(v + start));
                x++;
            }
            assertU(adoc(thisDoc.toArray(new String[thisDoc.size()])));
            data.put(k + start, row);
            if (debug) System.out.println(thisDoc);
        }


        if (debug) System.out.println("Created random docs: " + start + " - " + numDocs);
        return data;
    }


    public void testCitesCollector() throws Exception {

        int maxHits = 1000;
        int maxHitsFound = new Float(maxHits * 0.3f).intValue();
        createRandomDocs(0, new Float(maxHits * 0.4f).intValue());
        assertU(commit("waitSearcher", "true")); // closes the writer, create a new segment

        createRandomDocs(new Float(maxHits * 0.3f).intValue(), new Float(maxHits * 0.7f).intValue());
        assertU(commit("waitSearcher", "true")); // closes the writer, create a new segment

        createRandomDocs(new Float(maxHits * 0.71f).intValue(), new Float(maxHits * 1.0f).intValue());
        assertU(commit("waitSearcher", "true")); // closes the writer, create a new segment

        createRandomDocs(0, new Float(maxHits * 0.2f).intValue());
        assertU(commit("waitSearcher", "true")); // closes the writer, create a new segment

        // get the cache
        tempReq = req("test");
        SolrIndexSearcher searcher = tempReq.getSearcher();

        final CitationCache cache = (CitationCache) searcher.getCache("citations-cache-from-references");
        assert cache != null;

        @SuppressWarnings("rawtypes")
        SolrCacheWrapper citationsWrapper = new SolrCacheWrapper.CitationsCache(cache);

        @SuppressWarnings("rawtypes")
        SolrCacheWrapper referencesWrapper = new SolrCacheWrapper.ReferencesCache(cache);


        // invert ourselves - this is what we expect to find
        HashMap<Integer, int[]> references = reconstructCitationCache(searcher);
        HashMap<Integer, int[]> citations = invert(references);

        for (Entry<Integer, int[]> es : references.entrySet()) {
            int docid = es.getKey();
            int[] docids = es.getValue();
            for (int reference : docids) {
                List<Integer> a = Arrays.stream(citations.get(reference)).boxed().collect(Collectors.toList());
                List<Integer> b = Arrays.stream(citationsWrapper.getLuceneDocIds(reference)).boxed().collect(Collectors.toList());
                assertTrue(a.contains(docid));
                assertTrue(b.contains(docid));
                assertEquals(a, b);
            }
        }

        for (Entry<Integer, int[]> es : citations.entrySet()) {
            int docid = es.getKey();
            int[] docids = es.getValue();
            for (int reference : docids) {
                List<Integer> a = Arrays.stream(references.get(reference)).boxed().collect(Collectors.toList());
                List<Integer> b = Arrays.stream(referencesWrapper.getLuceneDocIds(reference)).boxed().collect(Collectors.toList());
                Collections.sort(a);
                Collections.sort(b);
                assertTrue(a.contains(docid));
                assertTrue(b.contains(docid));
                assertEquals(docid + " produced diff cache results", a, b);
            }
        }

        // to collect the measurements data
        Map<Integer, Integer> histogram = new HashMap<Integer, Integer>();

        SecondOrderCollectorCites coll = new SecondOrderCollectorCites(referencesWrapper, new String[]{"reference"});
        coll.searcherInitialization(searcher, null);


        // run 2nd order through the whole index (no IO error should happen)
        searcher.search(new SecondOrderQuery(new MatchAllDocsQuery(), coll), 10);


        ScoreDoc[] hits;
        for (Integer i = 0; i < maxHits; i++) {

            // int field types must be searched with bytes value (not strings)
            BytesRefBuilder br = new BytesRefBuilder();
            LegacyNumericUtils.intToPrefixCoded(i, 0, br);

            ScoreDoc[] doc = searcher.search(new TermQuery(new Term("id", br.get().utf8ToString())), 1000).scoreDocs;

            if (doc.length == 0) // that's ok, some docs are missing
                continue;

            Document document = searcher.getIndexReader().document(doc[0].doc);
            assertEquals("Not found : " + i, 1, doc.length);
            int docid = doc[0].doc;

            // references(id:X)
            if (debug)
                System.out.println(i + " cites : " + join(references.get(docid)) + " -> " + join(referencesWrapper.getLuceneDocIds(docid)));
            hits = searcher.search(new SecondOrderQuery(new TermQuery(new Term("id", br.get().utf8ToString())),
                    new SecondOrderCollectorCites(referencesWrapper, new String[]{"reference"})), maxHitsFound).scoreDocs;
            hitsEquals(docid, references, hits);
            hits = searcher.search(new SecondOrderQuery(new TermQuery(new Term("id", br.get().utf8ToString())),
                    new SecondOrderCollectorCitesRAM(referencesWrapper), false), maxHitsFound).scoreDocs;
            hitsEquals(docid, references, hits);

            // citations(id:X)
            if (debug)
                System.out.println(i + " cited-by : " + join(citations.get(docid)) + " -> " + join(citationsWrapper.getLuceneDocIds(docid)));
            hits = searcher.search(new SecondOrderQuery(new TermQuery(new Term("id", br.get().utf8ToString())),
                    new SecondOrderCollectorCitedBy(citationsWrapper), false), maxHitsFound).scoreDocs;
            hitsEquals(docid, citations, hits);

            if (!histogram.containsKey(hits.length)) {
                histogram.put(hits.length, 0);
            }
            histogram.put(hits.length, histogram.get(hits.length) + 1);

            // reference:X + year:195
            Builder builder = new BooleanQuery.Builder();
            builder.add(new BooleanClause(new TermQuery(new Term("reference", document.get("bibcode"))), Occur.MUST));
            builder.add(new BooleanClause(new TermQuery(new Term("year", "1995")), Occur.MUST));
            Query expected = builder.build();

            builder = new BooleanQuery.Builder();
            builder.add(new BooleanClause(new SecondOrderQuery(new TermQuery(new Term("id", br.get().utf8ToString())),
                    new SecondOrderCollectorCitedBy(citationsWrapper), false), Occur.MUST));
            builder.add(new BooleanClause(new TermQuery(new Term("year", "1995")), Occur.MUST));
            Query seeked = builder.build();

            ScoreDoc[] hitsA = searcher.search(expected, maxHitsFound).scoreDocs;
            ScoreDoc[] hitsB = searcher.search(seeked, maxHitsFound).scoreDocs;

            if (debug) {
                System.out.print("hitsA: ");
                prn(hitsA);
                System.out.print("hitsB: ");
                prn(hitsB);
            }
            assertScoreDocsEqual(hitsA, hitsB);

            if (i % 5000 == 0 && debug) {
                System.out.println("Done: " + i);
            }
        }


        int sum = 0;
        for (Entry<Integer, Integer> x : histogram.entrySet()) {
            if (debug) System.out.println(x.getKey() + " : " + x.getValue());
            sum += x.getValue();
        }
        if (debug) System.out.println(sum);

    }

    private void assertScoreDocsEqual(ScoreDoc[] a, ScoreDoc[] b) {
        ArrayList<Integer> hitsA = new ArrayList<Integer>();
        ArrayList<Integer> hitsB = new ArrayList<Integer>();
        for (ScoreDoc d : a) {
            hitsA.add(d.doc);
        }
        for (ScoreDoc d : a) {
            hitsB.add(d.doc);
        }
        assertEquals(hitsA, hitsB);
    }

    private void prn(ScoreDoc[] hits) {
        for (ScoreDoc doc : hits) {
            System.out.print(doc);
            System.out.print(", ");
        }
        System.out.println();
    }

    private String join(int[] l) {
        if (l == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int s : l) {
            if (sb.length() > 0) sb.append(",");
            sb.append(s);
        }
        return sb.toString();
    }


    private HashMap<Integer, int[]> reconstructCitationCache(SolrIndexSearcher searcher)
            throws IOException {
        Map<String, Integer> bibcodeToDocid = new HashMap<String, Integer>();
        Map<String, String[]> references = new HashMap<String, String[]>();

        searcher.search(new MatchAllDocsQuery(), new SimpleCollector() {

            private LeafReaderContext context;

            @Override
            public boolean needsScores() {
                return false;
            }

            @Override
            protected void doSetNextReader(LeafReaderContext context) throws IOException {
                this.context = context;
            }

            @Override
            public void collect(int doc) throws IOException {
                Document d = searcher.doc(doc + this.context.docBase);
                bibcodeToDocid.put(d.get("bibcode"), doc + this.context.docBase);
                references.put(d.get("bibcode"), d.getValues("reference"));
            }
        });
        HashMap<Integer, int[]> out = new HashMap<Integer, int[]>();
        for (Entry<String, String[]> es : references.entrySet()) {
            int docid = bibcodeToDocid.get(es.getKey());
            Set<Integer> docids = new HashSet<Integer>();
            String[] refs = es.getValue();
            for (int i = 0; i < refs.length; i++) {
                if (bibcodeToDocid.get(refs[i]) == null)
                    continue;
                docids.add(bibcodeToDocid.get(refs[i]));
            }
            out.put(docid, Arrays.stream(docids.toArray(new Integer[docids.size()])).mapToInt(Integer::intValue).toArray());
        }
        return out;
    }


    private HashMap<Integer, int[]> invert(HashMap<Integer, int[]> cites) {
        HashMap<Integer, List<Integer>> result = new HashMap<Integer, List<Integer>>(cites.size());
        for (Entry<Integer, int[]> e : cites.entrySet()) {
            for (int paperId : e.getValue()) {
                if (!result.containsKey(paperId)) {
                    result.put(paperId, new ArrayList<Integer>());
                }
                result.get(paperId).add(e.getKey());
            }
        }
        HashMap<Integer, int[]> out = new HashMap<Integer, int[]>();
        for (Entry<Integer, List<Integer>> e : result.entrySet()) {
            List<Integer> list = e.getValue();
            int[] ret = new int[list.size()];
            for (int i = 0; i < ret.length; i++)
                ret[i] = list.get(i);
            out.put(e.getKey(), ret);
        }
        return out;
    }

    private boolean hitsEquals(int docid, HashMap<Integer, int[]> cache, ScoreDoc[] hits) throws IOException {
        int[] links = cache.get(docid);

        ArrayList<Integer> result = new ArrayList<Integer>();
        for (ScoreDoc d : hits) {
            result.add(d.doc);
        }
        ArrayList<Integer> expected = new ArrayList<Integer>();
        if (links != null) {
            for (int r : links) {
                expected.add(r);
            }
        }
        Collections.sort(expected);
        Collections.sort(result);


        assertEquals(docid + " differs", expected, result);
        return true;
    }


    // Uniquely for Junit 3
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestCitationsSearch.class);
    }
}