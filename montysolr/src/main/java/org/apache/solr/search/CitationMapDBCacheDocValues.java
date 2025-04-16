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

package org.apache.solr.search;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.lucene.index.*;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.FixedBitSet;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.metrics.SolrMetricsContext;
import org.apache.solr.schema.*;
import org.apache.solr.util.IOFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mapdb.*;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of a cache for second order operations using MapDB for storage.
 * This cache will first construct a mapping from identifiers to lucene ids.
 * Next, it will rely on docvalues to resolve relations, with the relationships
 * stored in MapDB rather than in memory.
 * <p>
 * This implementation uses MapDB to store the citation network on disk rather than
 * keeping it entirely in memory, allowing it to work with limited memory resources
 * and in a Solr Cloud sharded environment.
 */
public class CitationMapDBCacheDocValues<K, V> extends SolrCacheBase implements CitationCache<K, V> {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /*
     * An instance of this class will be shared across multiple instances of an
     * LRUCache at the same time. Make sure everything is thread safe.
     */
    private static class CumulativeStats {
        AtomicLong lookups = new AtomicLong();
        AtomicLong hits = new AtomicLong();
        AtomicLong inserts = new AtomicLong();
        AtomicLong evictions = new AtomicLong();
    }

    private CumulativeStats stats;

    // per instance stats. The synchronization used for the map will also be
    // used for updating these statistics (and hence they are not AtomicLongs
    private long lookups;
    private long hits;
    private long inserts;
    private long evictions;

    private long warmupTime = 0;
    private String description = "Citation MapDB DocValues Cache";

    // MapDB database
    private DB db;
    private ConcurrentNavigableMap<K, V> identifierToDocIdMap;
    private HTreeMap<Integer, int[]> citationCache;
    private HTreeMap<Integer, int[]> referenceCache;

    private SolrIndexSearcher searcher = null;
    private List<String> referenceFields;
    private List<String> citationFields;
    private String[] identifierFields = null;
    private int maxDocid = 0;

    private String dbPath;

    // If we detect that you are mixing int and text fields
    // we'll treat all values (mappings) as text values
    private boolean treatIdentifiersAsText = false;

    // Configuration options
    private boolean incremental = false;

    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object init(Map args, Object persistence, CacheRegenerator regenerator) {
        super.init(args, regenerator);

        identifierFields = ((String) args.get("identifierFields")).split(",");
        assert (identifierFields != null && identifierFields.length > 0);

        incremental = "true".equals(args.get("incremental"));
        "true".equals(args.get("reuseCache"));

        // Get path for MapDB files
        dbPath = (String) args.get("dbPath");
        if (dbPath == null) {
            dbPath = System.getProperty("java.io.tmpdir") + "/solr-citation-docvalues-cache-" + name();
        }

        citationFields = new ArrayList<>();
        referenceFields = new ArrayList<>();

        if (args.containsKey("referenceFields") && ((String) args.get("referenceFields")).trim().length() > 0) {
            String[] refs = ((String) args.get("referenceFields")).split(",");
            for (String ref : refs) {
                referenceFields.add(ref);
            }
        }
        if (args.containsKey("citationFields") && ((String) args.get("citationFields")).trim().length() > 0) {
            String[] cites = ((String) args.get("citationFields")).split(",");
            for (String cite : cites) {
                citationFields.add(cite);
            }
        }

        String str = (String) args.get("size");
        final int limit = str == null ? 1024 : Integer.parseInt(str);
        str = (String) args.get("initialSize");

        final int initialSize = Math.min(str == null ? 1024 : Integer.parseInt(str), limit);
        description = generateDescription(limit, initialSize);

        // Initialize MapDB
        initializeDatabase();

        if (persistence == null) {
            // must be the first time a cache of this type is being created
            persistence = new CumulativeStats();
        }

        stats = (CumulativeStats) persistence;
        return persistence;
    }

    /**
     * Initialize the MapDB database and collections
     */
    private void initializeDatabase() {
        try {
            File dbFile = new File(dbPath);
            if (!dbFile.getParentFile().exists()) {
                dbFile.getParentFile().mkdirs();
            }

            // Close existing DB if it's open
            if (db != null) {
                try {
                    db.close();
                } catch (Exception e) {
                    log.warn("Error closing existing MapDB database", e);
                }
            }

            // Open/create the database file
            db = DBMaker.fileDB(dbFile)
                .fileMmapEnable()                // Use memory-mapped files for better performance
                .fileMmapPreclearDisable()       // Disable clearing of unused parts for better performance
                .cleanerHackEnable()             // Use special hack to allow file to be deleted on Windows
                .closeOnJvmShutdown()            // Close the database on JVM shutdown
                .make();

            // Create/open the maps and sets
            @SuppressWarnings("unchecked")
            BTreeMap<K, V> treeMap = (BTreeMap<K, V>) db.treeMap("identifierToDocId")
                .keySerializer(Serializer.JAVA)
                .valueSerializer(Serializer.JAVA)
                .counterEnable()
                .createOrOpen();
            identifierToDocIdMap = treeMap;

            citationCache = db.hashMap("citationCache")
                .keySerializer(Serializer.INTEGER)
                .valueSerializer(Serializer.INT_ARRAY)
                .createOrOpen();

            referenceCache = db.hashMap("referenceCache")
                .keySerializer(Serializer.INTEGER)
                .valueSerializer(Serializer.INT_ARRAY)
                .createOrOpen();
        } catch (Exception e) {
            log.error("Failed to initialize MapDB for citation cache", e);
            throw new SolrException(ErrorCode.SERVER_ERROR, "Failed to initialize MapDB for citation cache", e);
        }
    }

    /**
     * @return Returns the description of this cache.
     */
    private String generateDescription(int limit, int initialSize) {
        String description = "CitationMapDBDocValues Cache(maxSize=" + limit + ", initialSize=" + initialSize;
        if (isAutowarmingOn()) {
            description += ", " + getAutowarmDescription();
        }
        description += ')';
        return description;
    }

    public int size() {
        return identifierToDocIdMap.size();
    }

    public boolean treatsIdentifiersAsText() {
        return treatIdentifiersAsText;
    }

    public V put(K key, V value) {
        if (getState() == State.LIVE) {
            stats.inserts.incrementAndGet();
        }

        // increment local inserts regardless of state
        inserts++;
        if (value instanceof Integer && (Integer) value > maxDocid) {
            maxDocid = (Integer) value;
        }

        V oldValue = identifierToDocIdMap.put(key, value);
        db.commit(); // Commit changes to make them durable
        return oldValue;
    }

    public V get(K key) {
        V val = identifierToDocIdMap.get(key);
        if (getState() == State.LIVE) {
            // only increment lookups and hits if we are live.
            lookups++;
            stats.lookups.incrementAndGet();
            if (val != null) {
                hits++;
                stats.hits.incrementAndGet();
            }
        }
        return val;
    }

    @Override
    public V remove(K k) {
        V val = identifierToDocIdMap.remove(k);
        db.commit();
        return val;
    }

    @Override
    public V computeIfAbsent(K k, IOFunction<? super K, ? extends V> ioFunction) throws IOException {
        // Implemented similarly to a normal HashMap computeIfAbsent
        V val = get(k);
        if (val == null) {
            val = ioFunction.apply(k);
            if (val != null) {
                put(k, val);
            }
        }
        return val;
    }

    /*
     * This method should be used only for very specific purposes of dumping the
     * citation cache (or accessing all elements of the cache).
     *
     * The first comes references, the second are citations
     */
    public Iterator<int[][]> getCitationGraph() {
        return new CitationGraphIterator();
    }

    /**
     * A class that iterates through the citation graph.
     * Returns pairs of arrays: [0] references, [1] citations for each document
     */
    private class CitationGraphIterator implements Iterator<int[][]> {
        private final Iterator<Integer> docIds;

        public CitationGraphIterator() {
            docIds = citationCache.getKeys().iterator();
        }

        @Override
        public boolean hasNext() {
            return docIds.hasNext();
        }

        @Override
        public int[][] next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            int docId = docIds.next();
            int[][] result = new int[2][];

            // Get references for this document
            result[0] = referenceCache.get(docId);
            if (result[0] == null) {
                result[0] = new int[0];
            }

            // Get citations for this document
            result[1] = citationCache.get(docId);
            if (result[1] == null) {
                result[1] = new int[0];
            }

            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public int getCitationsIteratorSize() {
        return citationCache.size();
    }

    public int[] getCitations(K key) {
        V val = get(key);
        if (val == null) {
            return null;
        }

        int docid = (Integer) val;
        return getCitations(docid);
    }

    /*
     * This is a helper method allowing you to retrieve what we have directly using
     * lucene docid
     */
    public int[] getCitations(int docid) {
        if (searcher != null) {
            // Process on-demand using DocValues
            try {
                processDocValueData(searcher, docid);
            } catch (IOException e) {
                log.error("Error processing DocValues for citations", e);
                return null;
            }
        }

        int[] citations = citationCache.get(docid);

        if (getState() == State.LIVE) {
            // only increment lookups and hits if we are live.
            lookups++;
            stats.lookups.incrementAndGet();
            if (citations != null) {
                hits++;
                stats.hits.incrementAndGet();
            }
        }

        return citations;
    }

    public int[] getReferences(K key) {
        V val = get(key);
        if (val == null) {
            return null;
        }

        int docid = (Integer) val;
        return getReferences(docid);
    }

    /*
     * This is a helper method allowing you to retrieve what we have directly using
     * lucene docid
     */
    public int[] getReferences(int docid) {
        if (searcher != null) {
            // Process on-demand using DocValues
            try {
                processDocValueData(searcher, docid);
            } catch (IOException e) {
                log.error("Error processing DocValues for references", e);
                return null;
            }
        }

        int[] references = referenceCache.get(docid);

        if (getState() == State.LIVE) {
            // only increment lookups and hits if we are live.
            lookups++;
            stats.lookups.incrementAndGet();
            if (references != null) {
                hits++;
                stats.hits.incrementAndGet();
            }
        }

        return references;
    }

    /**
     * Process DocValue data for a specific document ID
     * This implements lazy-loading of the citation/reference data
     */
    private void processDocValueData(SolrIndexSearcher searcher, int docId) throws IOException {
        if (searcher == null) return;

        // Skip if we already have complete data for this document
        // Note: We need to check both caches separately as a document might have citations but no references
        boolean hasCitations = citationCache.containsKey(docId);
        boolean hasReferences = referenceCache.containsKey(docId);
        
        // Process if either cache is missing data
        if (hasCitations && hasReferences) {
            return;
        }

        // Get the LeafReader context for this document
        List<LeafReaderContext> leaves = searcher.getLeafContexts();
        LeafReaderContext leafContext = null;
        int docBase = 0;
        int localDocId = docId;

        for (LeafReaderContext ctx : leaves) {
            if (localDocId >= ctx.docBase && localDocId < ctx.docBase + ctx.reader().maxDoc()) {
                leafContext = ctx;
                docBase = ctx.docBase;
                localDocId = docId - docBase;
                break;
            }
        }

        if (leafContext == null) {
            return;
        }

        // Process reference fields
        if (!referenceFields.isEmpty()) {
            List<Integer> refs = new ArrayList<>();
            for (String field : referenceFields) {
                // Get field values
                SortedSetDocValues docValues = leafContext.reader().getSortedSetDocValues(field);
                if (docValues != null && docValues.advanceExact(localDocId)) {
                    long ord;
                    while ((ord = docValues.nextOrd()) != -1) {
                        BytesRef bytesRef = docValues.lookupOrd(ord);
                        String value = bytesRef.utf8ToString();
                        // Look up the referenced document ID
                        if (identifierToDocIdMap.containsKey(value)) {
                            Integer refId = (Integer) identifierToDocIdMap.get(value);
                            refs.add(refId);
                        }
                    }
                }
            }

            if (!refs.isEmpty()) {
                // Convert to int array and store in cache
                int[] refsArray = new int[refs.size()];
                for (int i = 0; i < refs.size(); i++) {
                    refsArray[i] = refs.get(i);
                }
                referenceCache.put(docId, refsArray);

                // Update citations for each referenced document
                for (int refId : refsArray) {
                    int[] existingCitations = citationCache.get(refId);
                    if (existingCitations == null) {
                        existingCitations = new int[]{docId};
                    } else {
                        // Check if citation already exists
                        boolean found = false;
                        for (int citationId : existingCitations) {
                            if (citationId == docId) {
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            int[] newCitations = Arrays.copyOf(existingCitations, existingCitations.length + 1);
                            newCitations[existingCitations.length] = docId;
                            existingCitations = newCitations;
                        }
                    }
                    citationCache.put(refId, existingCitations);
                }
            }
        }

        // Process citation fields (if any)
        if (!citationFields.isEmpty()) {
            List<Integer> cites = new ArrayList<>();
            for (String field : citationFields) {
                // Get field values
                SortedSetDocValues docValues = leafContext.reader().getSortedSetDocValues(field);
                if (docValues != null && docValues.advanceExact(localDocId)) {
                    long ord;
                    while ((ord = docValues.nextOrd()) != -1) {
                        BytesRef bytesRef = docValues.lookupOrd(ord);
                        String value = bytesRef.utf8ToString();
                        // Look up the cited document ID
                        if (identifierToDocIdMap.containsKey(value)) {
                            Integer citeId = (Integer) identifierToDocIdMap.get(value);
                            cites.add(citeId);
                        }
                    }
                }
            }

            if (!cites.isEmpty()) {
                // Convert to int array and store in cache
                int[] citesArray = new int[cites.size()];
                for (int i = 0; i < cites.size(); i++) {
                    citesArray[i] = cites.get(i);
                }
                citationCache.put(docId, citesArray);

                // Update references for each cited document
                for (int citeId : citesArray) {
                    int[] existingRefs = referenceCache.get(citeId);
                    if (existingRefs == null) {
                        existingRefs = new int[]{docId};
                    } else {
                        // Check if reference already exists
                        boolean found = false;
                        for (int refId : existingRefs) {
                            if (refId == docId) {
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            int[] newRefs = Arrays.copyOf(existingRefs, existingRefs.length + 1);
                            newRefs[existingRefs.length] = docId;
                            existingRefs = newRefs;
                        }
                    }
                    referenceCache.put(citeId, existingRefs);
                }
            }
        }

        // Commit to make the changes durable
        db.commit();
    }

    public void insertCitation(int sourceDocid, int targetDocid) {
        // Update citation cache
        int[] existingCitations = citationCache.get(targetDocid);
        int[] newCitations;
        if (existingCitations == null) {
            newCitations = new int[1];
            newCitations[0] = sourceDocid;
        } else {
            // Check if citation already exists
            for (int citationDocid : existingCitations) {
                if (citationDocid == sourceDocid) {
                    return; // Already exists, nothing to add
                }
            }

            // Add the new citation
            newCitations = Arrays.copyOf(existingCitations, existingCitations.length + 1);
            newCitations[existingCitations.length] = sourceDocid;
        }

        citationCache.put(targetDocid, newCitations);
        db.commit();
    }

    public void insertReference(int sourceDocid, int targetDocid) {
        // Update reference cache
        int[] existingReferences = referenceCache.get(sourceDocid);
        int[] newReferences;
        if (existingReferences == null) {
            newReferences = new int[1];
            newReferences[0] = targetDocid;
        } else {
            // Check if reference already exists
            for (int referenceDocid : existingReferences) {
                if (referenceDocid == targetDocid) {
                    return; // Already exists, nothing to add
                }
            }

            // Add the new reference
            newReferences = Arrays.copyOf(existingReferences, existingReferences.length + 1);
            newReferences[existingReferences.length] = targetDocid;
        }

        referenceCache.put(sourceDocid, newReferences);
        db.commit();
    }

    public void clear() {
        identifierToDocIdMap.clear();
        citationCache.clear();
        referenceCache.clear();
        db.commit();
    }

    private boolean isWarming = false;

    public boolean isWarmingOrWarmed() {
        return isWarming;
    }

    /**
     * Set the searcher associated with this cache
     */
    public void setSearcher(SolrIndexSearcher searcher) {
        this.searcher = searcher;
        
        // Process and load alternate identifiers map automatically
        loadAlternateIdentifiers(searcher);
    }
    
    /**
     * Load alternate identifiers using DocValues to properly map them
     * to primary identifiers in identifierToDocIdMap
     */
    @SuppressWarnings("unchecked")
    private void loadAlternateIdentifiers(SolrIndexSearcher searcher) {
        if (searcher == null) return;
        
        try {
            List<LeafReaderContext> leaves = searcher.getLeafContexts();
            try {
            } catch (Exception e) {
                // No alternate_bibcode field defined, nothing to do
                return;
            }
            
            // Map to store alternate to primary identifier mappings
            Map<K, V> alternateMap = new HashMap<>();
            
            // Process all leaf readers
            for (LeafReaderContext ctx : leaves) {
                LeafReader reader = ctx.reader();
                int maxDoc = reader.maxDoc();
                int docBase = ctx.docBase;
                
                // Get alternate_bibcode DocValues if available
                SortedSetDocValues alternateDV = reader.getSortedSetDocValues("alternate_bibcode");
                if (alternateDV == null) continue;
                
                // For each document
                for (int i = 0; i < maxDoc; i++) {
                    // Skip deleted docs
                    if (reader.getLiveDocs() != null && !reader.getLiveDocs().get(i)) {
                        continue;
                    }
                    
                    // Find the document's primary identifier
                    String primaryIdentifier = null;
                    int luceneDocId = docBase + i;
                    
                    // Use reverse lookup to find primary identifier for this doc
                    for (Entry<K, V> entry : identifierToDocIdMap.entrySet()) {
                        if (entry.getValue().equals(luceneDocId)) {
                            primaryIdentifier = entry.getKey().toString();
                            break;
                        }
                    }
                    
                    if (primaryIdentifier == null) {
                        continue;
                    }
                    
                    // Get all alternate identifiers for this document
                    if (alternateDV.advanceExact(i)) {
                        long ord;
                        while ((ord = alternateDV.nextOrd()) != -1) {
                            BytesRef bytesRef = alternateDV.lookupOrd(ord);
                            String alternateId = bytesRef.utf8ToString();
                            
                            // Add alternate->primary mapping
                            K altKey = safeStrToKey(alternateId);
                            alternateMap.put(altKey, (V)(Integer)luceneDocId);
                        }
                    }
                }
            }
            
            // Now add all alternate identifiers to the main map
            for (Entry<K, V> entry : alternateMap.entrySet()) {
                identifierToDocIdMap.put(entry.getKey(), entry.getValue());
            }
            
            db.commit(); // Commit the changes to make them durable
            
        } catch (Exception e) {
            log.error("Error loading alternate identifiers", e);
        }
    }
    
    
    public void warm(SolrIndexSearcher searcher, SolrCache<K, V> old) {
        long warmingStartTime = System.nanoTime();
        if (isAutowarmingOn()) {
            isWarming = true;
            try {
                log.info("Building cache (" + name() + "): " + searcher);
                if (this.incremental) {
                    warmIncrementally(searcher, old);
                } else {
                    warmRebuildEverything(searcher, old);
                }
                log.info("Warming cache " + name() + " done (# entries:" + size() + "): " + searcher);
            } catch (IOException e) {
                throw new SolrException(ErrorCode.SERVER_ERROR, "Failed to generate initial IDMapping", e);
            }
            searcher.hashCode();
        }
        warmupTime = TimeUnit.MILLISECONDS.convert(System.nanoTime() - warmingStartTime, TimeUnit.NANOSECONDS);
    }

    private void warmRebuildEverything(SolrIndexSearcher searcher, SolrCache<K, V> old) throws IOException {
        setSearcher(searcher);
        List<String> fields = getFields(searcher, this.identifierFields);

        // builds the mapping from document ID's to lucene docids
        unInvertedTheDamnThing(searcher, fields, new KVSetter() {
            @Override
            @SuppressWarnings({"unchecked"})
            public void set(int docbase, int docid, Object value) {
                if (treatIdentifiersAsText && value instanceof Integer) {
                    value = Integer.toString((Integer) value);
                }
                put((K) value, (V) (Integer) (docbase + docid));
            }
        });

        // Store reference and citation field information
        this.referenceFields = getFields(searcher, getReferenceFieldsArray());
        this.citationFields = getFields(searcher, getCitationFieldsArray());
    }

    private void warmIncrementally(SolrIndexSearcher searcher, SolrCache<K, V> old) throws IOException {
        if (regenerator == null)
            return;

        setSearcher(searcher);
        List<String> fields = getFields(searcher, this.identifierFields);
        CitationMapDBCacheDocValues<K, V> other = (CitationMapDBCacheDocValues<K, V>) old;

        // collect ids of documents that need to be reloaded/regenerated during this
        // warmup run
        FixedBitSet toRefresh = new FixedBitSet(searcher.getIndexReader().maxDoc());

        Bits liveDocs = searcher.getSlowAtomicReader().getLiveDocs();

        if (liveDocs == null) { // everything is new
            toRefresh.set(0, toRefresh.length());

            // Build the mapping from indexed values into lucene ids
            unInvertedTheDamnThing(searcher, fields, new KVSetter() {
                @SuppressWarnings("unchecked")
                @Override
                public void set(int docbase, int docid, Object value) {
                    put((K) value, (V) (Integer) (docbase + docid));
                }
            });
        } else if (liveDocs != null) {
            // Handle deleted or updated documents
            for (Entry<K, V> entry : other.identifierToDocIdMap.entrySet()) {
                Integer luceneId = (Integer) entry.getValue();
                if (luceneId <= liveDocs.length() && !liveDocs.get(luceneId)) {
                    // Doc was either deleted or updated - mark for refresh
                }
            }

            for (int i = 0; i < toRefresh.length(); i++) {
                if (liveDocs.get(i)) {
                    toRefresh.set(i);
                }
            }
        }

        // Store reference and citation field information
        this.referenceFields = getFields(searcher, getReferenceFieldsArray());
        this.citationFields = getFields(searcher, getCitationFieldsArray());

        // Autowarm entries
        if (isAutowarmingOn() && old != null) {
            Map<K, V> itemsToWarm = new HashMap<>();

            // Calculate number of items to warm
            int sz = autowarm.getWarmCount(other.size());

            // Get a slice of the entries from the old cache to warm
            int i = 0;
            for (Entry<K, V> entry : other.identifierToDocIdMap.entrySet()) {
                if (i >= other.size() - sz) {
                    itemsToWarm.put(entry.getKey(), entry.getValue());
                }
                i++;
            }

            // Process autowarm items
            for (Entry<K, V> entry : itemsToWarm.entrySet()) {
                try {
                    boolean continueRegen = true;
                    if (isModified(liveDocs, entry.getKey(), entry.getValue())) {
                        toRefresh.set((Integer) entry.getValue());
                    } else {
                        continueRegen = regenerator.regenerateItem(searcher, this, old, entry.getKey(), entry.getValue());
                    }
                    if (!continueRegen) {
                        break;
                    }
                } catch (Throwable e) {
                    log.error("Error during auto-warming of key:" + entry.getKey(), e);
                }
            }
        }
    }

    private List<String> getFields(SolrIndexSearcher searcher, String[] listOfFields) {
        List<String> out = new ArrayList<String>();

        IndexSchema schema = searcher.getCore().getLatestSchema();
        if (schema.getUniqueKeyField() == null) {
            throw new SolrException(ErrorCode.FORBIDDEN,
                "Sorry, your schema is missing unique key and thus you probably have many duplicates. I won't continue");
        }

        for (String f : listOfFields) {
            String fName = f.replace(":sorted", "");
            SchemaField fieldInfo = schema.getField(fName);
            FieldType type = fieldInfo.getType();

            if (type.getNumberType() != null) {
                treatIdentifiersAsText = true;
            }

            if (!fieldInfo.stored() && "NONE".equals(type.getDocValuesFormat().toString())) {
                throw new SolrException(ErrorCode.FORBIDDEN,
                    "The field " + f + " cannot be used to build citation cache!");
            }
            out.add(fName);
        }
        return out;
    }

    private String[] getReferenceFieldsArray() {
        return referenceFields.toArray(new String[0]);
    }

    private String[] getCitationFieldsArray() {
        return citationFields.toArray(new String[0]);
    }

    /*
     * Checks whether the cache needs to be rebuilt for this document, eg. if the
     * key points to a deleted document or if one of the values point at a deleted
     * document
     */
    private boolean isModified(Bits liveDocs, Object cacheKey, Object cacheValue) {
        // Implement logic to detect if document needs to be refreshed
        return false;
    }

    @Override
    public void initializeMetrics(SolrMetricsContext solrMetricsContext, String s) {
        // Metrics initialization can be implemented as needed
    }

    @Override
    public SolrMetricsContext getSolrMetricsContext() {
        return null;
    }

    @Override
    public void initializeCitationCache(int maxDocs) {
        clear();
    }

    @Override
    public int getHighestDocid() {
        return maxDocid;
    }

    @Override
    public Iterator<Entry<K, V>> getDictionary() {
        return identifierToDocIdMap.entrySet().iterator();
    }

    @Override
    public String getName() {
        return CitationMapDBCacheDocValues.class.getName();
    }

    @Override
    public String getDescription() {
        return description;
    }
    
    /**
     * Helper method to safely cast a String to key type K
     * @param str String to convert to key type
     * @return The string converted to key type K
     */
    @SuppressWarnings("unchecked")
    private K safeStrToKey(String str) {
        // If K is a String type, simple cast is sufficient
        // If treatIdentifiersAsText is true, we know K must be String
        if (treatIdentifiersAsText) {
            return (K)str;
        }
        
        // Otherwise, try our best to determine the type
        // This is necessary for test handling of alternate bibcodes
        try {
            // Check if any existing key is a String
            for (K key : identifierToDocIdMap.keySet()) {
                if (key instanceof String) {
                    return (K)str;
                }
                break;
            }
        } catch (Exception e) {
            // Fall back to generic cast
            log.debug("Error determining key type", e);
        }
        
        return (K)str;
    }

    public String getSource() {
        return "$URL$";
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public NamedList getStatistics() {
        NamedList lst = new SimpleOrderedMap();
        lst.add("lookups", lookups);
        lst.add("hits", hits);
        lst.add("hitratio", calcHitRatio(lookups, hits));
        lst.add("inserts", inserts);
        lst.add("evictions", evictions);
        lst.add("size", size());
        lst.add("warmupTime", warmupTime);

        long clookups = stats.lookups.get();
        long chits = stats.hits.get();
        lst.add("cumulative_lookups", clookups);
        lst.add("cumulative_hits", chits);
        lst.add("cumulative_hitratio", calcHitRatio(clookups, chits));
        lst.add("cumulative_inserts", stats.inserts.get());
        lst.add("cumulative_evictions", stats.evictions.get());

        return lst;
    }

    @Override
    public String toString() {
        return name() + getStatistics().toString();
    }

    public void close() {
        if (db != null) {
            try {
                db.close();
            } catch (Exception e) {
                log.warn("Error closing MapDB database", e);
            }
        }
    }

    @Override
    public int getMaxSize() {
        return 0; // MapDB implementation doesn't need a max size limit
    }

    @Override
    public void setMaxSize(int i) {
        // Not needed for MapDB implementation
    }

    @Override
    public int getMaxRamMB() {
        return 0; // MapDB doesn't use RAM limits
    }

    @Override
    public void setMaxRamMB(int i) {
        // Not needed for MapDB implementation
    }

    /**
     * Reads values from the DocValue and/or FieldCache and calls the setter
     */
    private class KVSetter {
        public void set(int docbase, int docid, Object value) {
            throw new NotImplementedException();
        }
    }

    /**
     * Given a set of fields, extract all values from them
     */
    private void unInvertedTheDamnThing(SolrIndexSearcher searcher, List<String> fields, final KVSetter setter)
        throws IOException {
        List<LeafReaderContext> leaves = searcher.getIndexReader().getContext().leaves();

        Bits liveDocs;
        LeafReader lr;

        for (LeafReaderContext leaf : leaves) {
            int docBase = leaf.docBase;
            liveDocs = leaf.reader().getLiveDocs();
            lr = leaf.reader();
            FieldInfos fInfo = lr.getFieldInfos();

            for (final String field : fields) {
                FieldInfo fi = fInfo.fieldInfo(field);

                if (fi == null) {
                    log.error("Field " + field + " has no schema entry; skipping it!");
                    continue;
                }

                DocValuesType fType = fi.getDocValuesType();

                if (fType.equals(DocValuesType.NONE)) {
                    // Skip fields without DocValues for DocValues implementation
                    log.warn("Field " + field + " has no DocValues; skipping it!");
                    continue;
                }

                switch (fType) {
                    case NUMERIC:
                        NumericDocValues numericValues = lr.getNumericDocValues(field);
                        if (numericValues != null) {
                            for (int i = 0; i < lr.maxDoc(); i++) {
                                if (liveDocs != null && !liveDocs.get(i)) {
                                    continue; // Skip deleted docs
                                }
                                if (numericValues.advanceExact(i)) {
                                    int v = (int) numericValues.longValue();
                                    setter.set(docBase, i, v);
                                }
                            }
                        }
                        break;

                    case SORTED_NUMERIC:
                        SortedNumericDocValues sortedNumericValues = lr.getSortedNumericDocValues(field);
                        if (sortedNumericValues != null) {
                            for (int i = 0; i < lr.maxDoc(); i++) {
                                if (liveDocs != null && !liveDocs.get(i)) {
                                    continue; // Skip deleted docs
                                }
                                if (sortedNumericValues.advanceExact(i)) {
                                    int count = sortedNumericValues.docValueCount();
                                    for (int j = 0; j < count; j++) {
                                        int v = (int) sortedNumericValues.nextValue();
                                        setter.set(docBase, i, v);
                                    }
                                }
                            }
                        }
                        break;

                    case SORTED_SET:
                        SortedSetDocValues sortedSetValues = lr.getSortedSetDocValues(field);
                        if (sortedSetValues != null) {
                            for (int i = 0; i < lr.maxDoc(); i++) {
                                if (liveDocs != null && !liveDocs.get(i)) {
                                    continue; // Skip deleted docs
                                }
                                if (sortedSetValues.advanceExact(i)) {
                                    long ord;
                                    while ((ord = sortedSetValues.nextOrd()) != -1) {
                                        BytesRef value = sortedSetValues.lookupOrd(ord);
                                        setter.set(docBase, i, value.utf8ToString().toLowerCase());
                                    }
                                }
                            }
                        }
                        break;

                    case SORTED:
                        SortedDocValues sortedValues = lr.getSortedDocValues(field);
                        if (sortedValues != null) {
                            for (int i = 0; i < lr.maxDoc(); i++) {
                                if (liveDocs != null && !liveDocs.get(i)) {
                                    continue; // Skip deleted docs
                                }
                                if (sortedValues.advanceExact(i)) {
                                    int ord = sortedValues.ordValue();
                                    BytesRef value = sortedValues.lookupOrd(ord);
                                    setter.set(docBase, i, value.utf8ToString().toLowerCase());
                                }
                            }
                        }
                        break;

                    default:
                        log.warn("Unsupported DocValues type: " + fType + " for field: " + field);
                }
            }
        }
    }
}
