package org.apache.lucene.search;

import java.util.List;
import java.util.Map;

import org.apache.lucene.index.IndexReader;

public interface SecondOrderCollector {
    public void setSubReaderRanges(int[][] ranges);
    public List<ScoreDoc> getSubReaderScoreDocs(IndexReader reader);
    public int hashCode();
    public boolean equals(Object o);
    public int getSubReaderDocBase(IndexReader reader);
}
