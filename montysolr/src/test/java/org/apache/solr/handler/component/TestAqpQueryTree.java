package org.apache.solr.handler.component;

import monty.solr.util.MontySolrQueryTestCase;
import monty.solr.util.SolrTestSetup;
import org.junit.BeforeClass;

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

public class TestAqpQueryTree extends MontySolrQueryTestCase {


    @BeforeClass
    public static void beforeClass() throws Exception {
        schemaString = "solr/collection1/conf/schema-minimal.xml";

        configString = "solr/collection1/conf/solrconfig-qtree.xml";

        SolrTestSetup.initCore(configString, schemaString);
    }

    public void test() throws Exception {
        //SolrRequestHandler qtreeHandler = h.getCore().getRequestHandler("/qtree");

        String s = "{name:\"OPERATOR\", label:\"DEFOP\", children: [" +
                "    {name:\"MODIFIER\", label:\"MODIFIER\", children: [" +
                "        {name:\"TMODIFIER\", label:\"TMODIFIER\", children: [" +
                "            {name:\"FIELD\", label:\"FIELD\", children: [" +
                "                {name:\"TERM_NORMAL\", input:\"title\", start:0, end:4}," +
                "                {name:\"QNORMAL\", label:\"QNORMAL\", children: [" +
                "                    {name:\"TERM_NORMAL\", input:\"joe\", start:6, end:8}]" +
                "                }]" +
                "            }]" +
                "        }]" +
                "    }," +
                "    {name:\"MODIFIER\", label:\"MODIFIER\", children: [" +
                "        {name:\"TMODIFIER\", label:\"TMODIFIER\", children: [" +
                "            {name:\"FIELD\", label:\"FIELD\", children: [" +
                "                {name:\"QNORMAL\", label:\"QNORMAL\", children: [" +
                "                    {name:\"TERM_NORMAL\", input:\"doe\", start:10, end:12}]" +
                "                }]" +
                "            }]" +
                "        }]" +
                "    }]" +
                "}";

        s = "\\n{\\\"name\\\":\\\"OPERATOR\\\", \\\"label\\\":\\\"DEFOP\\\", \\\"children\\\": [\\n    {\\\"name\\\":\\\"MODIFIER\\\", \\\"label\\\":\\\"MODIFIER\\\", \\\"children\\\": [\\n        {\\\"name\\\":\\\"TMODIFIER\\\", \\\"label\\\":\\\"TMODIFIER\\\", \\\"children\\\": [\\n            {\\\"name\\\":\\\"FIELD\\\", \\\"label\\\":\\\"FIELD\\\", \\\"children\\\": [\\n                {\\\"name\\\":\\\"TERM_NORMAL\\\", \\\"input\\\":\\\"title\\\", \\\"start\\\":0, \\\"end\\\":4},\\n                {\\\"name\\\":\\\"QNORMAL\\\", \\\"label\\\":\\\"QNORMAL\\\", \\\"children\\\": [\\n                    {\\\"name\\\":\\\"TERM_NORMAL\\\", \\\"input\\\":\\\"joe\\\", \\\"start\\\":6, \\\"end\\\":8}]\\n                }]\\n            }]\\n        }]\\n    },\\n    {\\\"name\\\":\\\"MODIFIER\\\", \\\"label\\\":\\\"MODIFIER\\\", \\\"children\\\": [\\n        {\\\"name\\\":\\\"TMODIFIER\\\", \\\"label\\\":\\\"TMODIFIER\\\", \\\"children\\\": [\\n            {\\\"name\\\":\\\"FIELD\\\", \\\"label\\\":\\\"FIELD\\\", \\\"children\\\": [\\n                {\\\"name\\\":\\\"QNORMAL\\\", \\\"label\\\":\\\"QNORMAL\\\", \\\"children\\\": [\\n                    {\\\"name\\\":\\\"TERM_NORMAL\\\", \\\"input\\\":\\\"doe\\\", \\\"start\\\":10, \\\"end\\\":12}]\\n                }]\\n            }]\\n        }]\\n    }]\\n}";
        String response = h.query(req("qt", "/qtree", "q", "title:joe doe", "wt", "json"));

        assert response.contains(s);

        s = "&lt;astOPERATOR label=\"DEFOP\" name=\"OPERATOR\" type=\"35\" &gt;\n" +
                "    &lt;astMODIFIER label=\"MODIFIER\" name=\"MODIFIER\" type=\"30\" &gt;\n" +
                "        &lt;astTMODIFIER label=\"TMODIFIER\" name=\"TMODIFIER\" type=\"66\" &gt;\n" +
                "            &lt;astFIELD label=\"FIELD\" name=\"FIELD\" type=\"19\" &gt;";

        response = h.query(req("qt", "/qtree", "q", "title:joe doe", "wt", "xml"));

        assert response.contains(s);

        response = h.query(req("qt", "/qtree", "q", "title:\"joe doe\"", "wt", "json"));
        //System.out.println(response);

    }
}
