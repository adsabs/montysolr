package org.apache.lucene.index;

import java.io.IOException;

import org.apache.lucene.store.Directory;

public class MockIndexWriter extends IndexWriter {
  public MockIndexWriter(Directory dir, IndexWriterConfig conf) throws IOException {
    super(dir, conf);
  }
  
  public DirectoryReader getReader() throws IOException {
    return this.getReader(true);
  }
}
