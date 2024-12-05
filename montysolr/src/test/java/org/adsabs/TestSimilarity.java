package org.adsabs;

import org.apache.lucene.tests.util.LuceneTestCase;
import org.apache.lucene.util.SmallFloat;
import org.junit.Test;

public class TestSimilarity extends LuceneTestCase {

    @Test
    public void test() {
        int x = 0;
        float step = 0.1f;
        float max = 10.0f;

        for (float i = 0.0f; i < max; i = i + step) {
            byte v = SmallFloat.floatToByte315(i);
            float w = SmallFloat.byte315ToFloat(v);
            if (i == w) {
                System.out.println("i=" + i + " v=" + v + " w=" + w + "                      x=" + x);
                x = 0;
            } else {
                x++;
                System.out.println("i=" + i + " v=" + v + " w=" + w);
            }
        }

    }

}
