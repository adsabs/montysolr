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
package org.apache.solr.response.transform;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @since v63.1.1.15
 */
public class FieldTransformerFactory extends TransformerFactory {
    int defaultV = 10;

    @Override
    public void init(NamedList args) {
        String defaultValue = (String) args.get("default");
        if (defaultValue != null) {
            defaultV = Integer.getInteger(defaultValue);
        }
    }

    @Override
    public DocTransformer create(String field, SolrParams params, SolrQueryRequest req) {
        return new FieldTransform(params, defaultV);
    }
}

class FieldTransform extends DocTransformer {

    private final SolrParams fields;
    private final int defaultV;

    public FieldTransform(SolrParams params, int defaultV) {
        fields = params;
        this.defaultV = defaultV;
    }

    @Override
    public String getName() {
        return "[fields]";
    }

    @Override
    public void transform(SolrDocument doc, int docid) {
        if (docid >= 0) {
            Iterator<String> it = fields.getParameterNamesIterator();
            Map<String, Collection<Object>> docMap = doc.getFieldValuesMap();
            String key;

            while (it.hasNext()) {
                key = it.next();
                if (docMap.containsKey(key)) {
                    Object v = docMap.get(key);
                    int c = fields.getInt(key, defaultV);

                    if (v instanceof List) {
                        List x = (List) v;
                        while (x.size() > c) {
                            x.remove(x.size() - 1);
                        }
                    }


                }
            }


        }
    }


}


