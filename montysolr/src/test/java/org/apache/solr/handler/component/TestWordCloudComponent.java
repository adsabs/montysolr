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
package org.apache.solr.handler.component;

import monty.solr.util.MontySolrAbstractTestCase;
import monty.solr.util.MontySolrSetup;
import monty.solr.util.SolrTestSetup;
import org.apache.lucene.util.LuceneTestCase.SuppressCodecs;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

@SuppressCodecs({"Lucene3x", "SimpleText"})
public class TestWordCloudComponent extends MontySolrAbstractTestCase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = "solr/collection1/conf/schema-minimal.xml";

        configString = "solr/collection1/conf/solrconfig-wordcloud.xml";

        SolrTestSetup.initCore(configString, schemaString);
    }


    public void createIndex() {
        assertU(adoc("id", "1", "recid", "1", "text", "who"));
        assertU(adoc("id", "2", "recid", "2", "text", "is stopword"));
        assertU(adoc("id", "3", "recid", "3", "text", "able"));
        assertU(adoc("id", "4", "recid", "4", "text", "to stopword"));
        assertU(adoc("id", "5", "recid", "5", "text", "exchange"));
        assertU(commit("waitSearcher", "true"));

        assertU(adoc("id", "16", "recid", "16", "text", "liberty"));
        assertU(adoc("id", "17", "recid", "17", "text", "for stopword"));
        assertU(adoc("id", "18", "recid", "18", "text", "safety"));
        assertU(adoc("id", "19", "recid", "19", "text", "deserves"));
        assertU(adoc("id", "20", "recid", "20", "text", "neither who"));
        assertU(commit("waitSearcher", "true"));
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        createIndex();
    }

    @Test
    public void test() throws Exception {

        assertQ(req("q", "*:*", "wordcloud", "true", "wordcloud.fl", "id,text", "indent", "true"),
                "//lst[@name='wordcloud']/lst[@name='text']/lst[@name='tf']/int[@name='liberty']='1'",
                "//lst[@name='wordcloud']/lst[@name='text']/lst[@name='idf']/double[@name='liberty']='1.0'"
        );

    }
}


