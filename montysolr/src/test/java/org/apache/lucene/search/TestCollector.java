package org.apache.lucene.search;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.MockIndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.LuceneTestCase;

import java.io.IOException;
import java.util.Random;

public class TestCollector extends LuceneTestCase {

    protected String idField;
    private Directory directory;
    private IndexReader reader;
    private IndexSearcher searcher;
    private MockIndexWriter writer;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        // all of these tried
        // directory = newDirectory();
        // writer = new RandomIndexWriter(random, directory);
        // directory = newFSDirectory(new File(TEMP_DIR,"index-citations"));
        // directory = SimpleFSDirectory.open(new
        // File(TEMP_DIR,"index-citations"));
        // directory = NIOFSDirectory.open(new
        // File(TEMP_DIR,"index-citations"));
        directory = new RAMDirectory();
        reOpenWriter(OpenMode.CREATE);
        writer.deleteAll();
        writer.commit();
        reader = writer.getReader();
        searcher = newSearcher(reader);
    }

    @Override
    public void tearDown() throws Exception {
        writer.close();
        reader.close();
        directory.close();
        super.tearDown();
    }

    private void reOpenWriter(OpenMode mode) throws
            IOException {
        if (writer != null)
            writer.close();
        writer = new MockIndexWriter(directory, newIndexWriterConfig(
                new WhitespaceAnalyzer()).setOpenMode(mode));
    }

    private void reOpenSearcher() throws IOException {
        reader.close();
        reader = writer.getReader();
        searcher = newSearcher(reader);

    }

    public int[][] createRandomDocs(int start, int numDocs) throws IOException {
        Random randomSeed = new Random();

        int[] randData = new int[numDocs / 10];
        for (int i = 0; i < randData.length; i++) {
            randData[i] = randomSeed.nextInt(numDocs) - start;
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

        Document doc;
        for (int k = 0; k < randi.length; k++) {
            doc = new Document();
            doc.add(newField("id", String.valueOf(k + start),
                    StringField.TYPE_STORED));
            doc.add(newField("text_id", "x" + (k + start),
                    StringField.TYPE_STORED));
            for (int v : randi[k]) {
                doc.add(newField("reference", String.valueOf(v + start),
                        StringField.TYPE_STORED));
                doc.add(newField("text_reference", "x" + (v + start),
                        StringField.TYPE_STORED));
            }
            writer.addDocument(doc);
        }

        System.out.println("Created random docs: " + start + " - " + numDocs);
        return randi;
    }

    public void testCollector() throws Exception {

        writer.commit();

        int maxHits = 10000;

        int[][] data = createRandomDocs(0, new Float(maxHits * 0.4f).intValue());
        reOpenWriter(OpenMode.APPEND); // close the writer, create a new segment
        createRandomDocs(new Float(maxHits * 0.4f).intValue(), new Float(
                maxHits * 0.7f).intValue());

        writer.commit();
        reOpenWriter(OpenMode.APPEND); // close the writer, create a new segment
        createRandomDocs(new Float(maxHits * 0.7f).intValue(), new Float(
                maxHits * 1.0f).intValue());

        writer.commit();
        reOpenWriter(OpenMode.APPEND); // close the writer, create a new segment
        reOpenSearcher();

        // without this, errors happen (occassionally, not always)
        // writer.optimize(true);

        /*
         * this doesn't help writer.commit(); reOpenWriter(OpenMode.APPEND);
         * Thread.sleep(1000);
         */

        searcher.search(new MatchAllDocsQuery(), new SimpleCollector() {
            Scorer scorer;
            IndexReader reader;
            int docBase;
            int visited = 0;

            @Override
            public void setScorer(Scorer scorer) throws IOException {
                this.scorer = scorer;
            }

            @Override
            public void collect(int doc) throws IOException {
                visited++;
                float score = scorer.score();
                Document d = reader.document(doc);
                // d.getValues("text_reference");
            }

            @Override
            public void doSetNextReader(LeafReaderContext context)
                    throws IOException {
                this.reader = context.reader();
                this.docBase = context.docBase;
            }

            @Override
            public boolean needsScores() {
                return false;
            }


        });

    }

    // Uniquely for Junit 3
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestCollector.class);
    }
}