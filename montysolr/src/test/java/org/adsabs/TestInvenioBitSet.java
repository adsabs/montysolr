package org.adsabs;

import org.apache.lucene.tests.util.LuceneTestCase;

import java.io.IOException;
import java.util.zip.DataFormatException;

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

public class TestInvenioBitSet extends LuceneTestCase {
    public void test() throws DataFormatException, IOException {

        // intbitset([1, 5, 6, 260]).fastdump()
        byte[] bytes = new byte[]{0x78, (byte) 0x9c, 0x4b, 0x62, (byte) 0xc0, 0xf, 0x4, (byte) 0xd0, (byte) 0xf8, 0x0, 0x13, (byte) 0x90, 0x0, 0x73};

        //for (byte b: bytes) {
        //  System.out.println((int) b + " " + (String.format("%02X ", b) + (int)(b & 0xff)));
        //}

        InvenioBitSet res = InvenioBitSet.fastLoad(bytes);
        check(res, 1, 5, 6, 260);

        // load uncompressed byte stream
        res = new InvenioBitSet(new byte[]{98, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        check(res, 1, 5, 6, 260);

        InvenioBitSet res2 = InvenioBitSet.fastLoad(res.fastDump());
        check(res2, 1, 5, 6, 260);

        assertEquals("eJxLYsAPBAAM0wBz", res.toBase64());

    }

    private void check(InvenioBitSet res, int... expected) {
        for (int x : expected) {
            assertTrue("Expected " + x + " bitset to be set", res.get(x));
        }
        assertEquals("Number of bits should be: " + expected.length, res.cardinality(), expected.length);
    }
}
