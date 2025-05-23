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
import org.apache.solr.core.SolrCore;
import org.apache.solr.metrics.SolrMetricsContext;
import org.apache.solr.schema.*;
import org.apache.solr.uninverting.UninvertingReader.Type;
import org.apache.solr.util.IOFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mapdb.*;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of a cache for second order operations using MapDB for storage
 * instead of in-memory structures. This cache will first construct a mapping from
 * identifiers to lucene ids. Next, it will read all values from a document field
 * and build a persistent data structure that can be used to tell what documents
 * are related.
 * <p>
 * This implementation uses MapDB to store the citation network on disk rather than
 * keeping it entirely in memory, allowing it to work with limited memory resources
 * and in a Solr Cloud sharded environment.
 */
public class CitationMapDBCache<K, V> extends SolrCacheBase implements CitationCache<K, V> {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static class CumulativeStats {
        AtomicLong lookups = new AtomicLong();
        AtomicLong hits = new AtomicLong();
        AtomicLong inserts = new AtomicLong();
        AtomicLong evictions = new AtomicLong();
    }

    private CumulativeStats stats;

    private long lookups;
    private long hits;
    private long inserts;
    private long evictions;

    private long warmupTime = 0;
    private String description = "Citation MapDB Cache";

    private DB db;
    private ConcurrentNavigableMap<K, V> identifierToDocIdMap;
    private Set<IntIntPair> citations;
    private Set<IntIntPair> references;
    private HTreeMap<Integer, int[]> citationCache;
    private HTreeMap<Integer, int[]> referenceCache;

    private String[] referenceFields;
    private String[] citationFields;
    private String[] identifierFields = null;

    private int maxDocid = 0;
    private String dbPath;

    private boolean treatIdentifiersAsText = false;

    private boolean incremental = false;
    private boolean loadCache = false;
    private boolean dumpCache = false;
    private boolean readOnly = false;
    private boolean isLeader = false;
    private SolrCore core = null;

    @SuppressWarnings({"unchecked"})
    public Object init(Map args, Object persistence, CacheRegenerator regenerator) {
        super.init(args, regenerator);

        identifierFields = ((String) args.get("identifierFields")).split(",");
        assert identifierFields.length > 0;

        incremental = "true".equals(args.get("incremental"));
        boolean reuseCache = "true".equals(args.get("reuseCache"));
        loadCache = "true".equals(args.get("loadDumpedCache"));
        dumpCache = "true".equals(args.get("dumpCache"));

        core = (SolrCore) args.get("searcher.getCore()");

        if (core != null && core.getCoreDescriptor().getCloudDescriptor() != null) {
            isLeader = core.getCoreDescriptor().getCloudDescriptor().isLeader();
            log.info("CitationMapDBCache: isLeader={}", isLeader);
        } else {
            // If not in SolrCloud mode, or can't determine, assume we're the leader
            isLeader = true;
            log.info("CitationMapDBCache: Not in cloud mode or unable to determine leader status, assuming leadership");
        }

        dbPath = (String) args.get("dbPath");
        if (dbPath == null) {
            dbPath = System.getProperty("java.io.tmpdir") + "/solr-citation-cache-" + name();
        }

        citationFields = new String[0];
        referenceFields = new String[0];

        if (args.containsKey("referenceFields") && !((String) args.get("referenceFields")).trim().isEmpty()) {
            referenceFields = ((String) args.get("referenceFields")).split(",");
        }
        if (args.containsKey("citationFields") && !((String) args.get("citationFields")).trim().isEmpty()) {
            citationFields = ((String) args.get("citationFields")).split(",");
        }

        String str = (String) args.get("size");
        final int limit = str == null ? 1024 : Integer.parseInt(str);
        str = (String) args.get("initialSize");

        final int initialSize = Math.min(str == null ? 1024 : Integer.parseInt(str), limit);
        description = generateDescription(limit, initialSize);

        initializeDatabase();

        if (persistence == null) {
            persistence = new CumulativeStats();
        }

        stats = (CumulativeStats) persistence;
        return persistence;
    }

    private void initializeDatabase() {
        try {
            File dbFile = new File(dbPath);
            if (!dbFile.getParentFile().exists()) {
                dbFile.getParentFile().mkdirs();
            }

            if (db != null) {
                try {
                    db.close();
                } catch (Exception e) {
                    log.warn("Error closing existing MapDB database", e);
                }
            }

            if (!isLeader) {
                log.info("Node is not the shard leader, opening database in read-only mode: {}", dbPath);
                readOnly = true;
            } else {
                log.info("Node is the shard leader, opening database in read-write mode: {}", dbPath);
                readOnly = false;
            }

            DBMaker.Maker dbMaker = DBMaker.fileDB(dbFile)
                .fileMmapEnable()
                .fileMmapPreclearDisable()
                .cleanerHackEnable()
                .closeOnJvmShutdown();

            if (readOnly) {
                dbMaker = dbMaker.readOnly();
                log.info("Opening MapDB database in read-only mode");
            }

            db = dbMaker.make();

            if (readOnly) {
                //noinspection unchecked
                identifierToDocIdMap = db.<K, V>treeMap("identifierToDocId")
                    .keySerializer(Serializer.JAVA)
                    .valueSerializer(Serializer.JAVA)
                    .open();

                @SuppressWarnings("unchecked")
                Set<IntIntPair> citationsSet = (Set<IntIntPair>) db.hashSet("citations")
                    .serializer(Serializer.JAVA)
                    .open();
                citations = citationsSet;

                @SuppressWarnings("unchecked")
                Set<IntIntPair> referencesSet = (Set<IntIntPair>) db.hashSet("references")
                    .serializer(Serializer.JAVA)
                    .open();
                references = referencesSet;

                citationCache = db.<Integer, int[]>hashMap("citationCache")
                    .keySerializer(Serializer.INTEGER)
                    .valueSerializer(Serializer.INT_ARRAY)
                    .open();

                referenceCache = db.<Integer, int[]>hashMap("referenceCache")
                    .keySerializer(Serializer.INTEGER)
                    .valueSerializer(Serializer.INT_ARRAY)
                    .open();

                log.info("Successfully opened MapDB database in read-only mode");
            } else {
                //noinspection unchecked
                identifierToDocIdMap = db.<K, V>treeMap("identifierToDocId")
                    .keySerializer(Serializer.JAVA)
                    .valueSerializer(Serializer.JAVA)
                    .counterEnable()
                    .createOrOpen();

                @SuppressWarnings("unchecked")
                Set<IntIntPair> citationsSet = (Set<IntIntPair>) db.hashSet("citations")
                    .serializer(Serializer.JAVA)
                    .createOrOpen();
                citations = citationsSet;

                @SuppressWarnings("unchecked")
                Set<IntIntPair> referencesSet = (Set<IntIntPair>) db.hashSet("references")
                    .serializer(Serializer.JAVA)
                    .createOrOpen();
                references = referencesSet;

                citationCache = db.hashMap("citationCache")
                    .keySerializer(Serializer.INTEGER)
                    .valueSerializer(Serializer.INT_ARRAY)
                    .createOrOpen();

                referenceCache = db.hashMap("referenceCache")
                    .keySerializer(Serializer.INTEGER)
                    .valueSerializer(Serializer.INT_ARRAY)
                    .createOrOpen();
            }

            // Check if we loaded an existing database with citation/reference pairs
            // but empty caches (which can happen during persistence)
            boolean hasData = !identifierToDocIdMap.isEmpty();
            boolean hasPairs = !citations.isEmpty() || !references.isEmpty();
            boolean needsRebuild = hasPairs && (citationCache.isEmpty() || referenceCache.isEmpty());

            if (hasData && needsRebuild && !readOnly) {
                log.info("Found existing citation/reference pairs but empty caches. Rebuilding caches...");
                rebuildCaches();
            }
        } catch (Exception e) {
            log.error("Failed to initialize MapDB for citation cache", e);
            throw new SolrException(ErrorCode.SERVER_ERROR, "Failed to initialize MapDB for citation cache", e);
        }
    }

    /**
     * @return Returns the description of this cache.
     */
    private String generateDescription(int limit, int initialSize) {
        String description = "CitationMapDB Cache(maxSize=" + limit + ", initialSize=" + initialSize;
        if (isAutowarmingOn()) {
            description += ", " + getAutowarmDescription();
        }
        description += ')';
        return description;
    }

    public int size() {
        return identifierToDocIdMap.size();
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

        // If the value is an integer (docId), clear any associated citation/reference caches for it
        if (val instanceof Integer) {
            int docId = (Integer) val;
            citationCache.remove(docId);
            referenceCache.remove(docId);
        }

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

    /**
         * Simple class for storing integer pairs for citations and references
         */
        private record IntIntPair(int source, int target) implements java.io.Serializable {
            @Serial
            private static final long serialVersionUID = 1L;

    }

    public void insertCitation(int sourceDocid, int targetDocid) {
        log.debug("Inserting citation - document {} cites document {}", sourceDocid, targetDocid);

        // Check if this citation pair already exists
        IntIntPair pair = new IntIntPair(sourceDocid, targetDocid);

        if (citations.contains(pair)) {
            log.debug("Skipping duplicate citation pair: {} -> {}", sourceDocid, targetDocid);
            return;  // Skip duplicate citation pair
        }

        citations.add(pair);

        // Citations are stored in the target document
        int[] existingCitations = citationCache.get(targetDocid);
        int[] newCitations;

        if (existingCitations == null) {
            newCitations = new int[1];
            newCitations[0] = sourceDocid;
        } else {
            // Check if the citation already exists in the array (double-check)
            boolean found = false;
            for (int citationDocid : existingCitations) {
                if (citationDocid == sourceDocid) {
                    found = true;
                    break;
                }
            }

            if (found) {
                log.debug("Skipping duplicate citation in array: {} -> {}", sourceDocid, targetDocid);
                return; // Skip duplicate entries
            }

            // Add the citation
            newCitations = Arrays.copyOf(existingCitations, existingCitations.length + 1);
            newCitations[existingCitations.length] = sourceDocid;
        }

        citationCache.put(targetDocid, newCitations);
        db.commit();
    }

    public void insertReference(int sourceDocid, int targetDocid) {
        IntIntPair pair = new IntIntPair(sourceDocid, targetDocid);

        // First check if the pair already exists to avoid duplicates
        if (references.contains(pair)) {
            return; // Already exists, nothing to add
        }

        references.add(pair);

        // Update reference cache - sourceDocid references targetDocid
        int[] existingReferences = referenceCache.get(sourceDocid);
        int[] newReferences;
        if (existingReferences == null) {
            newReferences = new int[1];
            newReferences[0] = targetDocid;
        } else {
            // Double-check if reference already exists
            boolean found = false;
            for (int referenceDocid : existingReferences) {
                if (referenceDocid == targetDocid) {
                    found = true;
                    break;
                }
            }

            if (found) {
                return; // Already exists, nothing to add
            }

            // Add the new reference
            newReferences = Arrays.copyOf(existingReferences, existingReferences.length + 1);
            newReferences[existingReferences.length] = targetDocid;
        }

        referenceCache.put(sourceDocid, newReferences);
        db.commit();
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
        int[] citations = citationCache.get(docid);

        if (getState() == State.LIVE) {
            // only increment lookups and hits if we are live.
            lookups++;
            stats.lookups.incrementAndGet();
            if (citations != null && citations.length > 0) {
                hits++;
                stats.hits.incrementAndGet();
            }
        }

        // For consistency with the original implementation
        if (citations != null && citations.length == 0) {
            return null;
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
        int[] references = referenceCache.get(docid);

        if (getState() == State.LIVE) {
            // only increment lookups and hits if we are live.
            lookups++;
            stats.lookups.incrementAndGet();
            if (references != null && references.length > 0) {
                hits++;
                stats.hits.incrementAndGet();
            }
        }

        // For consistency with the original implementation
        if (references != null && references.length == 0) {
            return null;
        }

        return references;
    }

    /**
     * Add a reference from source document to target value
     * Will look up the target value's document ID
     */
    private void addReference(int sourceDocid, Object value) {
        if (identifierToDocIdMap.containsKey(value)) {
            Integer targetDocid = (Integer) identifierToDocIdMap.get(value);
            insertReference(sourceDocid, targetDocid);
        } else {
            log.debug("Would like to add reference to {} but cannot map it to a lucene id", value);
        }
    }

    /**
     * Add a citation from source document to target value
     * Will look up the target value's document ID
     */
    private void addCitation(int sourceDocid, Object value) {
        if (identifierToDocIdMap.containsKey(value)) {
            Integer targetDocid = (Integer) identifierToDocIdMap.get(value);
            insertCitation(sourceDocid, targetDocid);
        } else {
            log.debug("Would like to add citation to {} but cannot map it to a lucene id", value);
        }
    }

    /**
     * Infer citations based on references
     */
    private void inferCitationsFromReferences() {
        log.info("Inferring citations from references");
        // Create a temporary collection to avoid concurrent modification
        List<IntIntPair> referencesToProcess = new ArrayList<>(references);

        // Process references to create citations (reverse the relationship)
        for (IntIntPair ref : referencesToProcess) {
            // For example, if document 0 references document 1
            // then document 1 is cited by document 0
            insertCitation(ref.source, ref.target);
        }

        // Normal implementation for other cases
        // Clear any existing citations
        citations.clear();
        citationCache.clear();

        // For each reference pair, create corresponding citation pair
        for (IntIntPair pair : references) {
            int sourceDoc = pair.source();
            int targetDoc = pair.target();

            // Add citation from sourceDoc to targetDoc
            int[] existingCitations = citationCache.get(targetDoc);
            if (existingCitations == null) {
                existingCitations = new int[]{sourceDoc};
            } else {
                boolean found = false;
                for (int existingSource : existingCitations) {
                    if (existingSource == sourceDoc) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    int[] newCitations = Arrays.copyOf(existingCitations, existingCitations.length + 1);
                    newCitations[existingCitations.length] = sourceDoc;
                    existingCitations = newCitations;
                }
            }

            citationCache.put(targetDoc, existingCitations);
            citations.add(new IntIntPair(sourceDoc, targetDoc));
        }

        db.commit();
    }

    /**
     * Infer references based on citations
     */
    private void inferReferencesFromCitations() {
        log.info("Inferring references from citations");
        // Create a temporary collection to avoid concurrent modification
        List<IntIntPair> citationsToProcess = new ArrayList<>(citations);

        // Process citations to create references (reverse the relationship)
        for (IntIntPair cite : citationsToProcess) {
            // For example, if document 0 cites document 1
            // then document 0 references document 1
            insertReference(cite.source, cite.target);
        }
        db.commit();
    }

    /**
     * Rebuild citation and reference caches from stored pairs
     */
    private void rebuildCaches() {
        log.info("Rebuilding citation and reference caches from stored pairs");

        // Clear existing caches but not the pairs
        citationCache.clear();
        referenceCache.clear();

        // First rebuild references from reference pairs
        for (IntIntPair pair : references) {
            int sourceDoc = pair.source();
            int targetDoc = pair.target();

            // Add to reference cache
            int[] existingReferences = referenceCache.get(sourceDoc);
            if (existingReferences == null) {
                existingReferences = new int[]{targetDoc};
            } else {
                boolean found = false;
                for (int existingTarget : existingReferences) {
                    if (existingTarget == targetDoc) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    int[] newReferences = Arrays.copyOf(existingReferences, existingReferences.length + 1);
                    newReferences[existingReferences.length] = targetDoc;
                    existingReferences = newReferences;
                }
            }

            referenceCache.put(sourceDoc, existingReferences);
        }

        // Then rebuild citations from citation pairs
        for (IntIntPair pair : citations) {
            int sourceDoc = pair.source();
            int targetDoc = pair.target();

            // Add to citation cache
            int[] existingCitations = citationCache.get(targetDoc);
            if (existingCitations == null) {
                existingCitations = new int[]{sourceDoc};
            } else {
                boolean found = false;
                for (int existingSource : existingCitations) {
                    if (existingSource == sourceDoc) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    int[] newCitations = Arrays.copyOf(existingCitations, existingCitations.length + 1);
                    newCitations[existingCitations.length] = sourceDoc;
                    existingCitations = newCitations;
                }
            }

            citationCache.put(targetDoc, existingCitations);
        }

        db.commit();
    }

public void clear() {
    // Check if this node is the leader
    if (!isLeader) {
        log.info("This node is not the shard leader. Clear operation will be ignored as only the leader can modify the database.");
        log.info("The database is already in read-only mode, so this is just an informational message.");
        return;
    }

    log.info("Clearing cache state as shard leader");
    identifierToDocIdMap.clear();
    citations.clear();
    references.clear();
    citationCache.clear();
    referenceCache.clear();
    db.commit();
    log.info("Cache state cleared and committed");
}

    private boolean isWarming = false;

    public boolean isWarmingOrWarmed() {
        return isWarming;
    }

public void warm(SolrIndexSearcher searcher, SolrCache<K, V> old) {
    long warmingStartTime = System.nanoTime();
    if (isAutowarmingOn()) {
        isWarming = true;

        boolean buildMe = true;
        long indexGeneration = getIndexGeneration(searcher);

        // Check if we can reuse the existing cache based on index generation
        if (loadCache) {
            long cachedGeneration = getCachedIndexGeneration();
            if (cachedGeneration == indexGeneration) {
                log.info("Using existing persisted cache {} with matching index generation: {}", name(), indexGeneration);
                // MapDB keeps the data persisted on disk
                // Just make sure everything is loaded properly into memory structures
                buildMe = false;
                log.info("Warming cache {} done (# entries:{}): {}", name(), size(), searcher);
            } else {
                log.info("Will not reuse the cache {} as index generation differs; cached:{} != index:{}",
                        name(), cachedGeneration, indexGeneration);

                // Clear the existing data since we need to rebuild
                clear();
            }
        }

        if (buildMe) {
            try {
                log.info("Building cache ({}): {}", name(), searcher);
                if (this.incremental) {
                    warmIncrementally(searcher, old);
                } else {
                    warmRebuildEverything(searcher, old);
                }
                log.info("Warming cache {} done (# entries:{}): {}", name(), size(), searcher);

                // Persist the cache if requested
                if (dumpCache) {
                    persistCache(indexGeneration);
                }
            } catch (IOException e) {
                throw new SolrException(ErrorCode.SERVER_ERROR, "Failed to generate initial citation mapping", e);
            }
        }
    }

    warmupTime = TimeUnit.MILLISECONDS.convert(System.nanoTime() - warmingStartTime, TimeUnit.NANOSECONDS);
}

/**
 * Gets the index generation from the searcher
 */
private long getIndexGeneration(SolrIndexSearcher searcher) {
    long generation = -1;
    try {
        generation = searcher.getIndexReader().getIndexCommit().getGeneration();
    } catch (IOException e) {
        log.warn("Failed to get index generation", e);
    }
    return generation;
}

/**
 * Gets the path where the MapDB index generation file is stored
 */
private File getIndexGenerationFile() {
    return new File(dbPath + ".generation");
}

/**
 * Gets the cached generation number from the MapDB generation file
 */
private long getCachedIndexGeneration() {
    File generationFile = getIndexGenerationFile();
    if (!generationFile.exists()) {
        return -2;
    }

    try (DataInputStream input = new DataInputStream(new FileInputStream(generationFile))) {
        return input.readLong();
    } catch (IOException e) {
        log.warn("Failed to read cached index generation", e);
        return -2;
    }
}

/**
 * Stores the index generation to a separate file
 */
private void storeIndexGeneration(long generation) {
    File generationFile = getIndexGenerationFile();
    try (DataOutputStream output = new DataOutputStream(new FileOutputStream(generationFile))) {
        output.writeLong(generation);
    } catch (IOException e) {
        log.warn("Failed to write index generation", e);
    }
}

/**
 * Persist the current state of the cache to disk using MapDB's commit mechanism
 */
private void persistCache(long indexGeneration) {
    log.info("Persisting cache {} to {}", name(), dbPath);

    // Make sure changes are committed to disk
    db.commit();

    // Store the index generation for consistency checking
    storeIndexGeneration(indexGeneration);

    log.info("Persisted {} to {}", name(), dbPath);
}

    private void warmRebuildEverything(SolrIndexSearcher searcher, SolrCache<K, V> old) throws IOException {
        List<String> fields = getFields(searcher, this.identifierFields);

        // Clear existing data
        clear();

        // Initialize citation cache
        initializeCitationCache(searcher.maxDoc());

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

        if (this.referenceFields.length > 0 || this.citationFields.length > 0) {
            // Process reference fields
            unInvertedTheDamnThing(searcher, getFields(searcher, this.referenceFields), new KVSetter() {
                @Override
                public void set(int docbase, int docid, Object value) {
                    addReference(docbase + docid, value);
                }
            });

            // Process citation fields
            unInvertedTheDamnThing(searcher, getFields(searcher, this.citationFields), new KVSetter() {
                @Override
                public void set(int docbase, int docid, Object value) {
                    addCitation(docbase + docid, value);
                }
            });

            // Infer missing relationships
            if (this.citationFields.length == 0 && this.referenceFields.length > 0) {
                inferCitationsFromReferences();
            } else if (this.citationFields.length > 0 && this.referenceFields.length == 0) {
                inferReferencesFromCitations();
            }
        }

        // Commit all changes
        db.commit();
    }

    private void warmIncrementally(SolrIndexSearcher searcher, SolrCache<K, V> old) throws IOException {
        if (regenerator == null)
            return;

        List<String> fields = getFields(searcher, this.identifierFields);
        CitationMapDBCache<K, V> other = (CitationMapDBCache<K, V>) old;

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
        } else {
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
                    log.error("Error during auto-warming of key:{}", entry.getKey(), e);
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

            out.add(fName);
        }
        return out;
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
    public String getName() {
        return CitationMapDBCache.class.getName();
    }

    @Override
    public String getDescription() {
        return description;
    }

    public String getSource() {
        return "$URL$";
    }

    @Override
    public void initializeCitationCache(int maxDocs) {
        if (identifierToDocIdMap.isEmpty() &&
            citations.isEmpty() &&
            references.isEmpty() &&
            citationCache.isEmpty() &&
            referenceCache.isEmpty()) {
            log.info("Initializing empty citation cache");
        } else {
            log.info("Reusing existing citation cache data: {} identifiers, {} citation pairs, {} reference pairs",
                    identifierToDocIdMap.size(), citations.size(), references.size());
            return;
        }

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
        return 0;
    }

    @Override
    public void setMaxSize(int i) {
        // Not needed for MapDB implementation
    }

    @Override
    public int getMaxRamMB() {
        return 0;
    }

    @Override
    public void setMaxRamMB(int i) {
        // Not needed for MapDB implementation
    }

    private static class Transformer {
        public void process(int docBase, int docid) throws IOException {
            throw new NotImplementedException();
        }
    }

    private static class KVSetter {
        public void set(int docbase, int docid, Object value) {
            throw new NotImplementedException();
        }
    }

    private void unInvertedTheDamnThing(SolrIndexSearcher searcher, List<String> fields, final KVSetter setter)
            throws IOException {

        List<LeafReaderContext> leaves = searcher.getIndexReader().getContext().leaves();

        Bits liveDocs;
        LeafReader lr;
        Transformer transformer;
        for (LeafReaderContext leave : leaves) {
            int docBase = leave.docBase;
            liveDocs = leave.reader().getLiveDocs();
            lr = leave.reader();
            FieldInfos fInfo = lr.getFieldInfos();
            for (final String field : fields) {

                FieldInfo fi = fInfo.fieldInfo(field);

                if (fi == null) {
                    log.error("Field {} has no schema entry; skipping it!", field);
                    continue;
                }

                DocValuesType fType = fi.getDocValuesType();
                final LeafReader unReader;

                if (fType.equals(DocValuesType.NONE)) {
                    Class<? extends DocValuesType> c = fType.getClass();
                    continue;
                } else {
                    unReader = lr;
                }

                transformer = switch (fType) {
                    case NUMERIC -> new Transformer() {
                        final NumericDocValues dv = unReader.getNumericDocValues(field);

                        @Override
                        public void process(int docBase, int docId) throws IOException {
                            if (dv.advanceExact(docId)) {
                                int v = (int) dv.longValue();
                                setter.set(docBase, docId, v);
                            }
                        }
                    };
                    case SORTED_NUMERIC -> new Transformer() {
                        final SortedNumericDocValues dv = unReader.getSortedNumericDocValues(field);

                        @Override
                        public void process(int docBase, int docId) throws IOException {
                            if (dv.advanceExact(docId)) {
                                int max = dv.docValueCount();
                                int v;
                                for (int i = 0; i < max; i++) {
                                    v = (int) dv.nextValue();
                                    setter.set(docBase, docId, v);
                                }
                            }
                        }
                    };
                    case SORTED_SET -> new Transformer() {
                        final SortedSetDocValues dv = unReader.getSortedSetDocValues(field);
                        final int errs = 0;

                        @Override
                        public void process(int docBase, int docId) throws IOException {
                            if (dv.advanceExact(docId)) {
                                int count = dv.docValueCount();
                                for (int i = 0; i < count; i++) {
                                    long ord = dv.nextOrd();
                                    final BytesRef value = dv.lookupOrd(ord);
                                    setter.set(docBase, docId, value.utf8ToString().toLowerCase()); // XXX: even if we apply tokenization, doc values ignore it
                                }
                            }
                        }
                    };
                    case SORTED -> new Transformer() {
                        final SortedDocValues dv = unReader.getSortedDocValues(field);
                        TermsEnum te;

                        @Override
                        public void process(int docBase, int docId) throws IOException {
                            if (dv.advanceExact(docId)) {
                                int v = dv.ordValue();
                                final BytesRef value = dv.lookupOrd(v);
                                setter.set(docBase, docId, value.utf8ToString().toLowerCase());
                            }
                        }
                    };
                    default ->
                        throw new IllegalArgumentException("The field " + field + " is of type that cannot be un-inverted");
                };

                int i = 0;
                while (i < lr.maxDoc()) {
                    if (liveDocs != null && !(i < liveDocs.length() && liveDocs.get(i))) {
                        i++;
                        continue;
                    }
                    transformer.process(docBase, i);
                    i++;
                }
            }
        }
    }

    public static class SimpleRegenerator implements CacheRegenerator {
        @SuppressWarnings({"unchecked"})
        public boolean regenerateItem(SolrIndexSearcher newSearcher, SolrCache newCache, SolrCache oldCache,
                                      Object oldKey, Object oldVal) {

            newCache.put(oldKey, oldVal);
            return true;
        }
    }
}
