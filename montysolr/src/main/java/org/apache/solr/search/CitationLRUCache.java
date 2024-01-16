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

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.apache.commons.lang.NotImplementedException;
import org.apache.lucene.index.*;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.FixedBitSet;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of a cache for second order operations. This cache will first
 * construct a mapping from identifiers to lucene ids. Next, it will read all
 * values from a document field and build in-memory data structure that can be
 * used to tell what documents are related.
 * <p>
 * For the time being, we read whole index into memory to create a citation
 * network, but this implementation should also be capable of holding only
 * partial (the most accessed) citation network in memory. However, the initial
 * mapping (value&lt;-&gt;lucene id) will always be constructed in its entirety.
 */
public class CitationLRUCache<K, V> extends SolrCacheBase implements CitationCache<K, V> {
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
    private String description = "Citation LRU Cache";

    // the main objects
    private LinkedHashMap<K, V> relationships;
    private String[] referenceFields;
    private String[] citationFields;
    private String[] identifierFields = null;

    private int sourceReaderHashCode = 0;

    // If we detect that you are mixing int and text fields
    // we'll treat all values (mappings) as text values
    private boolean treatIdentifiersAsText = false;

    // TODO: i'm planning to add the ability to build the cache
    // incrementally (ie per index segment), but it may
    // not be necessary as we are going to denormalize
    // citation data outside solr and prepare everything there...
    private boolean incremental = false;
    private boolean reuseCache;
    private boolean loadCache = false;
    private boolean dumpCache = false;

    private int maxDocid;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object init(Map args, Object persistence, CacheRegenerator regenerator) {
        super.init(args, regenerator);

        identifierFields = ((String) args.get("identifierFields")).split(",");
        assert (identifierFields != null && identifierFields.length > 0);

        incremental = "true".equals(args.get("incremental"));
        reuseCache = "true".equals(args.get("reuseCache"));
        loadCache = "true".equals(args.get("loadDumpedCache"));
        dumpCache = "true".equals(args.get("dumpCache"));

        citationFields = new String[0];
        referenceFields = new String[0];

        if (args.containsKey("referenceFields") && ((String) args.get("referenceFields")).trim().length() > 0) {
            referenceFields = ((String) args.get("referenceFields")).split(",");
        }
        if (args.containsKey("citationFields") && ((String) args.get("citationFields")).trim().length() > 0) {
            citationFields = ((String) args.get("citationFields")).split(",");
        }

        float sizeInPercent = 1.0f;

        String str = (String) args.get("size");
        if (str != null && str.endsWith("%")) {
            str = str.substring(0, str.length() - 1);
            sizeInPercent = Integer.parseInt(str) / 100f;
        }

        final int limit = str == null ? 1024 : Integer.parseInt(str);
        str = (String) args.get("initialSize");

        final int initialSize = Math.min(str == null ? 1024 : Integer.parseInt(str), limit);
        description = generateDescription(limit, initialSize);

        relationships = new RelationshipLinkedHashMap<K, V>(initialSize, 0.75f, true, limit);

        if (persistence == null) {
            // must be the first time a cache of this type is being created
            persistence = new CumulativeStats();
        }

        stats = (CumulativeStats) persistence;
        return persistence;
    }

    /**
     * @return Returns the description of this cache.
     */
    private String generateDescription(int limit, int initialSize) {
        String description = "CitationLRU Cache(maxSize=" + limit + ", initialSize=" + initialSize;
        if (isAutowarmingOn()) {
            description += ", " + getAutowarmDescription();
        }
        description += ')';
        return description;
    }

    public int size() {
        synchronized (relationships) {
            return relationships.size();
        }
    }

    public boolean treatsIdentifiersAsText() {
        return treatIdentifiersAsText;
    }

    public V put(K key, V value) {
        // System.out.println("put(" + key + "," + value+")");
        synchronized (relationships) {
            if (getState() == State.LIVE) {
                stats.inserts.incrementAndGet();
            }

            // increment local inserts regardless of state???
            // it does make it more consistent with the current size...
            inserts++;
            if (value instanceof Integer && (Integer) value > maxDocid)
                maxDocid = (Integer) value;
            return relationships.put(key, value);
        }
    }

    public V get(K key) {
        synchronized (relationships) {
            V val = relationships.get(key);
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
    }

    /*
     * This method should be used only for very specific purposes of dumping the
     * citation cache (or accessing all elements of the cache). Access to the map is
     * not synchronized, but you are iterating over a copy of data - so yo cannot
     * change it
     *
     * The first comes references, the second are citations
     */
    public Iterator<int[][]> getCitationGraph() {
        return ((RelationshipLinkedHashMap<K, V>) relationships).getRelationshipsIterator();
    }

    public int getCitationsIteratorSize() {
        synchronized (relationships) {
            return ((RelationshipLinkedHashMap<K, V>) relationships).relationshipsDataSize();
        }
    }

    public void insertCitation(int sourceDocid, int targetDocid) {
        ((CitationLRUCache.RelationshipLinkedHashMap<K, V>) relationships).addCitation(sourceDocid, targetDocid);
    }

    public void insertReference(int sourceDocid, int targetDocid) {
        ((CitationLRUCache.RelationshipLinkedHashMap<K, V>) relationships).addReference(sourceDocid, targetDocid);
    }

    public int[] getCitations(K key) {
        synchronized (relationships) {
            V val = relationships.get(key);
            if (val == null)
                return null;

            RelationshipLinkedHashMap<K, V> relMap = (RelationshipLinkedHashMap<K, V>) relationships;
            int[] values = relMap.getCitations((Integer) val);

            if (getState() == State.LIVE) {
                // only increment lookups and hits if we are live.
                lookups++;
                stats.lookups.incrementAndGet();
                if (values != null) {
                    hits++;
                    stats.hits.incrementAndGet();
                }
            }
            return values;
        }
    }

    /*
     * This is a helper method allowing you to retrieve what we have directly using
     * lucene docid
     */
    public int[] getCitations(int docid) {
        synchronized (relationships) {
            RelationshipLinkedHashMap<K, V> relMap = (RelationshipLinkedHashMap<K, V>) relationships;
            int[] val = relMap.getCitations(docid);

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
    }

    public int[] getReferences(K key) {
        synchronized (relationships) {
            V val = relationships.get(key);
            if (val == null)
                return null;

            RelationshipLinkedHashMap<K, V> relMap = (RelationshipLinkedHashMap<K, V>) relationships;
            int[] values = relMap.getReferences((Integer) val);

            if (getState() == State.LIVE) {
                // only increment lookups and hits if we are live.
                lookups++;
                stats.lookups.incrementAndGet();
                if (values != null) {
                    hits++;
                    stats.hits.incrementAndGet();
                }
            }
            return values;
        }
    }

    /*
     * This is a helper method allowing you to retrieve what we have directly using
     * lucene docid
     */
    public int[] getReferences(int docid) {
        synchronized (relationships) {
            RelationshipLinkedHashMap<K, V> relMap = (RelationshipLinkedHashMap<K, V>) relationships;
            int[] val = relMap.getReferences(docid);

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
    }

    public void clear() {
        synchronized (relationships) {
            relationships.clear();
        }
    }

    private boolean isWarming = false;

    private boolean purgeCache;

    public boolean isWarmingOrWarmed() {
        return isWarming;
    }

    public void warm(SolrIndexSearcher searcher, SolrCache<K, V> old) {

        long warmingStartTime = System.nanoTime();
        if (isAutowarmingOn()) {
            isWarming = true;

            boolean buildMe = true;

            if (loadCache && getCacheStorageDir(searcher) != null) {
                CitationCacheReaderWriter ccrw = getCitationCacheReaderWriter(searcher);
                if (CitationCacheReaderWriter.getCacheGeneration(getCacheStorageDir(searcher)) == CitationCacheReaderWriter.getIndexGeneration(searcher)) {
                    log.info("Trying to load persisted cache " + name());
                    try {
                        ccrw.load(this);
                        buildMe = false;
                        log.info("Warming cache done " + name() + " (# entries:" + relationships.size() + "): " + searcher);
                    } catch (IOException e) {
                        log.error("Failed loading persisted cache " + name(), e);
                    }
                } else {
                    //noinspection AutoBoxing
                    log.info("Will not load the cache {} current index generation differs; dump:{} != index:{}",
                            name(), CitationCacheReaderWriter.getCacheGeneration(getCacheStorageDir(searcher)), CitationCacheReaderWriter.getIndexGeneration(searcher));
                }
            }

            if (buildMe) {
                try {
                    log.info("Building cache (" + name() + "): " + searcher);
                    if (this.incremental) {
                        warmIncrementally(searcher, old);
                    } else {
                        warmRebuildEverything(searcher, old);
                    }
                    log.info("Warming cache " + name() + " done (# entries:" + relationships.size() + "): " + searcher);
                } catch (IOException e) {
                    throw new SolrException(ErrorCode.SERVER_ERROR, "Failed to generate initial IDMapping", e);
                }
            }

            sourceReaderHashCode = searcher.hashCode();

            if (dumpCache && buildMe && getCitationCacheReaderWriter(searcher) != null) {
                try {
                    persistCitationCache(searcher);
                } catch (IOException e) {
                    throw new SolrException(ErrorCode.SERVER_ERROR, "Failed to generate initial IDMapping", e);
                }
            }
        }


        warmupTime = TimeUnit.MILLISECONDS.convert(System.nanoTime() - warmingStartTime, TimeUnit.NANOSECONDS);
    }

    private File getCacheStorageDir(SolrIndexSearcher searcher) {
        File f = new File(searcher.getCore().getResourceLoader().getConfigDir());
        try {
            assert f.exists();
            assert f.isDirectory();
            assert f.canWrite();
        } catch (AssertionError ae) {
            return null;
        } catch (Exception e) {
            return null;
        }
        return f;
    }

    private CitationCacheReaderWriter getCitationCacheReaderWriter(SolrIndexSearcher searcher) {
        File confDir = getCacheStorageDir(searcher);
        if (confDir == null)
            return null;
        return new CitationCacheReaderWriter(confDir);
    }

    private void persistCitationCache(SolrIndexSearcher searcher) throws IOException {

        CitationCacheReaderWriter ccrw = getCitationCacheReaderWriter(searcher);
        ccrw.persist(this, CitationCacheReaderWriter.getIndexGeneration(searcher));
        log.info("Persisted {} into {}", name(), ccrw.getTargetDir());
    }


    private void warmRebuildEverything(SolrIndexSearcher searcher, SolrCache<K, V> old) throws IOException {

        List<String> fields = getFields(searcher, this.identifierFields);

        // if (this.referenceFields.length == 0 && this.citationFields.length == 0) {
        // return;
        // }

        // builds the mapping from document ID's to lucene docids
        unInvertedTheDamnThing(searcher, fields, new KVSetter() {
            @Override
            @SuppressWarnings({"unchecked"})
            public void set(int docbase, int docid, Object value) {
                if (treatIdentifiersAsText && value instanceof Integer) {
                    value = Integer.toString((Integer) value);
                }
                put((K) value, (V) Integer.valueOf(docbase + docid));
            }
        });

        if (this.referenceFields.length > 0 || this.citationFields.length > 0) {
            @SuppressWarnings("rawtypes") final RelationshipLinkedHashMap relMap = (RelationshipLinkedHashMap) relationships;
            relMap.initializeCitationCache(searcher.maxDoc()); // TODO: touch only updated fields

            unInvertedTheDamnThing(searcher, getFields(searcher, this.referenceFields), new KVSetter() {
                @Override
                public void set(int docbase, int docid, Object value) {
                    synchronized (relMap) {
                        relMap.addReference(docbase + docid, value);
                    }
                }
            });

            unInvertedTheDamnThing(searcher, getFields(searcher, this.citationFields), new KVSetter() {
                @Override
                public void set(int docbase, int docid, Object value) {
                    synchronized (relMap) {
                        relMap.addCitation(docbase + docid, value);
                    }
                }
            });

            if (this.citationFields.length == 0 && this.referenceFields.length > 0) {
                relMap.inferCitationsFromReferences();
            } else if (this.citationFields.length > 0 && this.referenceFields.length == 0) {
                relMap.inferReferencesFromCitations();
            }
        }

    }

    private void warmIncrementally(SolrIndexSearcher searcher, SolrCache<K, V> old) throws IOException {
        if (regenerator == null)
            return;

        List<String> fields = getFields(searcher, this.identifierFields);
        CitationLRUCache<K, V> other = (CitationLRUCache<K, V>) old;

        // collect ids of documents that need to be reloaded/regenerated during this
        // warmup run
        FixedBitSet toRefresh = new FixedBitSet(searcher.getIndexReader().maxDoc());
        Bits liveDocs = searcher.getSlowAtomicReader().getLiveDocs();

        if (liveDocs == null) { // everything is new, this could be fresh index or merged/optimized index too

            toRefresh.set(0, toRefresh.length());

            // Build the mapping from indexed values into lucene ids
            // this must always be available, so we build it no matter what...
            // XXX: make it update only the necessary IDs (not the whole index)
            unInvertedTheDamnThing(searcher, fields, new KVSetter() {
                @SuppressWarnings("unchecked")
                @Override
                public void set(int docbase, int docid, Object value) {
                    put((K) value, (V) Integer.valueOf(docbase + docid));
                }
            });

        } else {

            for (int i = 0; i < toRefresh.length(); i++) {
                if (liveDocs.get(i)) {
                    toRefresh.set(i);
                }
            }
        }

        // warm entries
        if (isAutowarmingOn()) {
            Object[] keys, vals = null;

            // Don't do the autowarming in the synchronized block, just pull out the keys
            // and values.
            synchronized (other.relationships) {

                int sz = autowarm.getWarmCount(other.relationships.size());

                keys = new Object[sz];
                vals = new Object[sz];

                Iterator<Map.Entry<K, V>> iter = other.relationships.entrySet().iterator();

                // iteration goes from oldest (least recently used) to most recently used,
                // so we need to skip over the oldest entries.
                int skip = other.relationships.size() - sz;
                for (int i = 0; i < skip; i++)
                    iter.next();

                for (int i = 0; i < sz; i++) {
                    Map.Entry<K, V> entry = iter.next();
                    keys[i] = entry.getKey();
                    vals[i] = entry.getValue();
                }
            }

            // autowarm from the oldest to the newest entries so that the ordering will be
            // correct in the new cache.
            for (int i = 0; i < keys.length; i++) {
                try {
                    boolean continueRegen = true;
                    if (isModified(liveDocs, keys[i], vals[i])) {
                        toRefresh.set((Integer) keys[i]);
                    } else {
                        continueRegen = regenerator.regenerateItem(searcher, this, old, keys[i], vals[i]);
                    }
                    if (!continueRegen)
                        break;
                } catch (Throwable e) {
                    SolrException.log(log, "Error during auto-warming of key:" + keys[i], e);
                }
            }
        }
    }

    private List<String> getFields(SolrIndexSearcher searcher, String[] listOfFields) {

        List<String> out = new ArrayList<>();

        IndexSchema schema = searcher.getCore().getLatestSchema();
        if (schema.getUniqueKeyField() == null) {
            throw new SolrException(ErrorCode.FORBIDDEN,
                    "Sorry, your schema is missing unique key and thus you probably have many duplicates. I won't continue");
        }

        for (String f : listOfFields) {
            String fName = f.replace(":sorted", "");
            SchemaField fieldInfo = schema.getField(fName);
            FieldType type = fieldInfo.getType();

            if (type.getNumberType() != null) { // todo:rca -- numeric types have grown since this was conceived...
                synchronized (relationships) {
                    treatIdentifiersAsText = true;
                }
            }

            if (!fieldInfo.stored() && type.getDocValuesFormat().equals(DocValuesType.NONE)) {
                throw new SolrException(ErrorCode.FORBIDDEN,
                        "The field " + f + " cannot be used to build citation cache!");
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
        /*
         * if (!liveDocs.get((Integer) get((K)cacheKey))) { // doc is deleted return
         * true; }
         *
         * for (Integer luceneId: (Integer[]) cacheValue) { if (!liveDocs.get(luceneId)
         * || luceneId == -1) { // some of the linked docs was deleted or unrecognized
         * return true; } }
         */
        return false;
    }

    public void close() {
    }

    /*
     * Reads values from the DocValue and/or FieldCache and calls the setter
     */
    private class Transformer {
        public void process(int docBase, int docid) throws IOException {
            throw new NotImplementedException();
        }
    }

    private class KVSetter {
        @SuppressWarnings({"unchecked"})
        public void set(int docbase, int docid, Object value) {
            throw new NotImplementedException();
        }
    }

    /*
     * Given the set of fields, we'll look inside them and retrieve (into memory)
     * all values
     */
    private void unInvertedTheDamnThing(SolrIndexSearcher searcher, List<String> fields, final KVSetter setter)
            throws IOException {

        IndexSchema schema = searcher.getCore().getLatestSchema();
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
                    log.error("Field " + field + " has no schema entry; skipping it!");
                    continue;
                }

                DocValuesType fType = fi.getDocValuesType();
                final LeafReader unReader = lr;


                switch (fType) {
                    case NUMERIC:
                        transformer = new Transformer() {
                            final NumericDocValues dv = unReader.getNumericDocValues(field);

                            @Override
                            public void process(int docBase, int docId) throws IOException {
                                if (dv.advanceExact(docId)) {
                                    int v = (int) dv.longValue();
                                    setter.set(docBase, docId, v);
                                }
                            }
                        };
                        break;
                    case SORTED_NUMERIC:
                        transformer = new Transformer() {
                            final SortedNumericDocValues dv = unReader.getSortedNumericDocValues(field);

                            @Override
                            public void process(int docBase, int docId) throws IOException {
                                dv.advanceExact(docId);
                                int max = dv.docValueCount();
                                int v;
                                for (int i = 0; i < max; i++) {
                                    v = (int) dv.nextValue();
                                    setter.set(docBase, docId, v);
                                }
                            }
                        };
                        break;
                    case SORTED_SET:
                        transformer = new Transformer() {
                            final SortedSetDocValues dv = unReader.getSortedSetDocValues(field);

                            @Override
                            public void process(int docBase, int docId) throws IOException {
                                if (dv.advanceExact(docId)) {
                                    for (long ord = dv.nextOrd(); ord != SortedSetDocValues.NO_MORE_ORDS; ord = dv.nextOrd()) {
                                        final BytesRef value = dv.lookupOrd(ord);
                                        setter.set(docBase, docId, value.utf8ToString().toLowerCase()); // XXX: even if we apply
                                        // tokenization, doc
                                        // values ignore it
                                    }
                                }
                            }
                        };
                        break;
                    case SORTED:
                        transformer = new Transformer() {
                            final SortedDocValues dv = unReader.getSortedDocValues(field);
                            TermsEnum te;

                            @Override
                            public void process(int docBase, int docId) throws IOException {
                                if (dv.advanceExact(docId)) {
                                    BytesRef v = dv.binaryValue();
                                    if (v.length == 0)
                                        return;
                                    setter.set(docBase, docId, v.utf8ToString().toLowerCase());
                                }
                            }
                        };
                        break;
                    default:
                        throw new IllegalArgumentException("The field " + field + " is of type that cannot be un-inverted");
                }

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

    //////////////////////// SolrInfoMBeans methods //////////////////////

    public String getName() {
        return CitationLRUCache.class.getName();
    }

    public String getDescription() {
        return description;
    }

    public String getSource() {
        return "$URL: http://svn.apache.org/repos/asf/lucene/dev/branches/lucene_solr_4_0/solr/core/src/java/org/apache/solr/search/LRUCache.java $";
    }

    @SuppressWarnings({"rawtypes", "unchecked", "AutoBoxing"})
    public NamedList getStatistics() {
        NamedList lst = new SimpleOrderedMap();
        synchronized (relationships) {
            lst.add("lookups", lookups);
            lst.add("hits", hits);
            lst.add("hitratio", calcHitRatio(lookups, hits));
            lst.add("inserts", inserts);
            lst.add("evictions", evictions);
            lst.add("size", relationships.size());
        }
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

    @Override
    public int hashCode() {
        return referenceFields.hashCode() ^ identifierFields.hashCode() ^ sourceReaderHashCode;
    }

    public String identifierString() {
        StringBuffer out = new StringBuffer();
        out.append("CitationLRUCache(");
        out.append("idfields:");
        out.append(Arrays.toString(identifierFields));
        if (referenceFields.length > 0) {
            out.append(", valfields:");
            out.append(Arrays.toString(referenceFields));
        }
        out.append(")");
        return out.toString();
    }

    public Iterator<Entry<K, V>> getDictionary() {
        return relationships.entrySet().iterator();
    }

    public static class SimpleRegenerator implements CacheRegenerator {
        @SuppressWarnings({"unchecked", "rawtypes"})
        public boolean regenerateItem(SolrIndexSearcher newSearcher, SolrCache newCache, SolrCache oldCache,
                                      Object oldKey, Object oldVal) throws IOException {

            newCache.put(oldKey, oldVal);
            return true;
        }
    }

    /*
     * The main datastructure holding information about the lucene documents.
     *
     * For speed purposes, the data gets loaded into RAM; we have those pieces
     *
     * - mapping: key -> lucene docid - references: docid -> many other docids -
     * citations: docid -> many other docids
     *
     * Until I implement dynamic loading of data, this cache will always grow to the
     * maxdoc size, so that no evictions happen
     */
    @SuppressWarnings("hiding")
    public static class RelationshipLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
        private static final long serialVersionUID = -356203002886265188L;
        int slimit;
        int maxDocSize;
        Int2ObjectMap<IntArrayList> references;
        Int2ObjectMap<IntArrayList> citations;

        public RelationshipLinkedHashMap(int initialSize, float ratio, boolean accessOrder, int limit) {
            super(initialSize, ratio, accessOrder);
            slimit = limit;
            references = new Int2ObjectOpenHashMap<>(0); // just to prevent NPE - normally, is
            citations = new Int2ObjectOpenHashMap<>(0); // initialized in initializeCitationCache
        }

        @SuppressWarnings("rawtypes")
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return false;
            /*
             * if (size() > slimit) { // increment evictions regardless of state. // this
             * doesn't need to be synchronized because it will // only be called in the
             * context of a higher level synchronized block. evictions++;
             * stats.evictions.incrementAndGet(); return true; } return false;
             */
        }

        public int[] getReferences(int docid) {
            if (references.containsKey(docid)) {
                IntArrayList c = references.get(docid);
                if (c != null)
                    return c.toIntArray();
            }
            return null;
        }

        public Iterator<int[][]> getRelationshipsIterator() {
            return new CitationDataIterator();
        }

        public int relationshipsDataSize() {
            return maxDocSize;
        }

        public int[] getCitations(int docid) {
            if (citations.containsKey(docid)) {
                IntArrayList c = citations.get(docid);
                if (c != null)
                    return c.toIntArray();
            }
            return null;
        }

        public void initializeCitationCache(int maxDocSize) {
            references = new Int2ObjectOpenHashMap<>(maxDocSize);
            citations = new Int2ObjectOpenHashMap<>(maxDocSize);

            references.defaultReturnValue(null);
            citations.defaultReturnValue(null);

            this.maxDocSize = maxDocSize;
        }

        public void addReference(int sourceDocid, Object value) {
            // System.out.println("addReference(" + sourceDocid + ", " + value + ")");
            if (this.containsKey(value)) {
                addReference(sourceDocid, (Integer) this.get(value));
            } else {
                log.debug("Would like to add reference " + value + " but cannot map it to the lucene id");
            }
        }

        public void addReference(int sourceDocid, Integer targetDocid) {
            _add(references, sourceDocid, targetDocid);
        }

        public void addReference(int sourceDocId, int targetDocId) {
            _add(references, sourceDocId, targetDocId);
        }

        public void addCitation(int sourceDocid, Object value) {
            // System.out.println("addCitation(" + sourceDocid + ", " + value + ")");
            if (this.containsKey(value)) {
                addCitation(sourceDocid, (Integer) this.get(value));
            } else {
                log.debug("Would like to add citation " + value + " but cannot map it to the lucene id");
            }
        }

        public void addCitation(int sourceDocid, Integer targetDocid) {
            // System.out.println("addCitation(" + sourceDocid + "," + targetDocid+")");
            _add(citations, sourceDocid, targetDocid);
        }

        public void addCitation(int sourceDocId, int targetDocId) {
            _add(citations, sourceDocId, targetDocId);
        }

        private void _add(Int2ObjectMap<IntArrayList> target, int sourceDocid, int targetDocid) {

            // System.out.println("_add(" + sourceDocid + "," + targetDocid+")");

            if (target.get(sourceDocid) == null) {
                IntArrayList pointer = new IntArrayList(1);
                pointer.add(targetDocid);
                target.put(sourceDocid, pointer);
            } else {
                target.get(sourceDocid).add(targetDocid);
            }
        }

        public void inferCitationsFromReferences() {
            for (Int2ObjectMap.Entry<IntArrayList> entry : references.int2ObjectEntrySet()) {
                int i = entry.getIntKey();
                IntArrayList refs = entry.getValue();
                if (refs == null) {
                    continue;
                }

                for (int j = 0; j < refs.size(); j++) {
                    if (refs.getInt(j) == -1)
                        continue;
                    addCitation(refs.getInt(j), i);
                }
            }
        }

        public void inferReferencesFromCitations() {
            for (Int2ObjectMap.Entry<IntArrayList> entry : citations.int2ObjectEntrySet()) {
                int i = entry.getIntKey();
                IntArrayList refs = entry.getValue();
                if (refs == null) {
                    continue;
                }

                for (int j = 0; j < refs.size(); j++) {
                    if (refs.getInt(j) == -1)
                        continue;
                    addReference(refs.getInt(j), i);
                }
            }
        }

        private class CitationDataIterator implements Iterator<int[][]> {
            int cursor = 0; // index of next element to return

            public boolean hasNext() {
                return cursor < maxDocSize;
            }

            public int[][] next() {
                int i = cursor;
                if (i >= maxDocSize)
                    throw new NoSuchElementException();
                int[][] out = new int[2][];

                IntArrayList v1 = references.get(cursor);
                IntArrayList v2 = citations.get(cursor);

                out[0] = v1 != null ? v1.toIntArray() : new int[0];
                out[1] = v2 != null ? v2.toIntArray() : new int[0];

                cursor = i + 1;
                return out;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

        }
    }

    @Override
    public void initializeCitationCache(int maxDocs) {
        relationships = new RelationshipLinkedHashMap<K, V>(maxDocs, 0.75f, true, 1024);
        ((RelationshipLinkedHashMap) relationships).initializeCitationCache(maxDocs);
        if (stats == null)
            stats = new CumulativeStats();
    }

    @Override
    public int getHighestDocid() {
        return maxDocid;
    }

}
