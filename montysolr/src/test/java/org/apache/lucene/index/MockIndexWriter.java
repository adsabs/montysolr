package org.apache.lucene.index;

import org.apache.lucene.store.Directory;

import java.io.IOException;

public class MockIndexWriter extends IndexWriter {
    public MockIndexWriter(Directory dir, IndexWriterConfig conf) throws IOException {
        super(dir, conf);
    }

    public DirectoryReader getReader() throws IOException {
        return this.getReader(true, true);
    }
}
