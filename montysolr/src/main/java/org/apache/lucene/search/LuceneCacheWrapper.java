package org.apache.lucene.search;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.lucene.index.*;
import org.apache.solr.uninverting.UninvertingReader;
import org.apache.solr.uninverting.UninvertingReader.Type;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LuceneCacheWrapper<T> implements CacheWrapper {

    String str;
    int hashCode;
    public Reference<T> cache;

    public LuceneCacheWrapper(Reference<T> cache) {
        this.cache = cache;
        this.str = internalToString(); // we cache these values because cache may disappear
        this.hashCode = internalHashCode();
    }

    @Override
    public void init() {
        // do nothing
    }

    @Override
    public String toString() {
        return this.str;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public int internalHashCode() {
        return cache.get().hashCode();
    }

    @Override
    public void close() {
        cache.clear();
    }

    @Override
    public String internalToString() {
        throw new NotImplementedException();
    }

    public float getFloat(int docid) {
        throw new NotImplementedException();
    }

    public static LuceneCacheWrapper<NumericDocValues> getFloatCache(
            String fieldName,
            Type type,
            LeafReader reader)
            throws IOException {

        Map<String, UninvertingReader.Type> mapping = new HashMap<String, UninvertingReader.Type>();
        mapping.put(fieldName, type);
        LeafReader uninvertingReader = UninvertingReader.wrap(reader, mapping::get);
        NumericDocValues values = uninvertingReader.getNumericDocValues(fieldName);

        // this to get cache indexed by docid (total)
        //NumericDocValues values = getNumericValues(reader, fieldName);
        if (values == null)
            values = DocValues.emptyNumeric();

        final String fName = fieldName;
        LuceneCacheWrapper<NumericDocValues> newCache = new LuceneCacheWrapper<NumericDocValues>(new SoftReference<NumericDocValues>(values)) {
            @Override
            public String internalToString() {
                return "float[] " + fName;
            }

            @Override
            public float getFloat(int docid) {
                NumericDocValues ref = this.cache.get();
                try {
                    if (ref.advanceExact(docid)) {
                        return Float.intBitsToFloat((int) ref.longValue());
                    }
                } catch (IOException e) {
                    // TODO:rca - propagate instead?
                    e.printStackTrace();
                }
                return 0.0f;
            }
        };

        return newCache;

    }

    /**
     * Returns a NumericDocValues for a reader's docvalues (potentially merging on-the-fly)
     * <p>
     * This is a slow way to access numeric values. Instead, access them per-segment
     * with {@link LeafReader#getNumericDocValues(String)}
     * </p>
     */
    private static NumericDocValues getNumericValues(final IndexReader r, final String field) throws IOException {
        final List<LeafReaderContext> leaves = r.leaves();
        final int size = leaves.size();
        if (size == 0) {
            return null;
        } else if (size == 1) {
            //return leaves.get(0).reader().getNumericDocValues(field);
        }

        boolean anyReal = false;
        final NumericDocValues[] values = new NumericDocValues[size];
        final int[] starts = new int[size + 1];
        for (int i = 0; i < size; i++) {
            LeafReaderContext context = leaves.get(i);
            NumericDocValues v = context.reader().getNumericDocValues(field);
            if (v == null) {
                v = DocValues.emptyNumeric();
            } else {
                anyReal = true;
            }
            values[i] = v;
            starts[i] = context.docBase;
        }
        starts[size] = r.maxDoc();

        if (!anyReal) {
            return null;
        } else {
            return new NumericDocValues() {
                private int currLeaf = 0;

                @Override
                public long longValue() throws IOException {
                    return values[currLeaf].longValue();
                }

                @Override
                public boolean advanceExact(int target) throws IOException {
                    int subIndex = ReaderUtil.subIndex(target, starts);
                    currLeaf = subIndex;
                    return values[subIndex].advanceExact(target - starts[subIndex]);
                }

                @Override
                public int docID() {
                    return NO_MORE_DOCS;
                }

                @Override
                public int nextDoc() throws IOException {
                    throw new IOException("Only use advance/advanceExact + logValue methods");
                }

                @Override
                public int advance(int target) throws IOException {
                    int subIndex = ReaderUtil.subIndex(target, starts);
                    currLeaf = subIndex;
                    return values[subIndex].advance(target - starts[subIndex]);
                }

                @Override
                public long cost() {
                    // TODO Auto-generated method stub
                    return 0;
                }


            };
        }
    }

}
