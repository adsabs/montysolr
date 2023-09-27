package org.apache.lucene.analysis.tokenattributes;

import org.apache.lucene.util.Attribute;

public interface DuplicateTermAttribute extends Attribute {

    String getValue();

    void setValue(String val);

    void clear();
}
