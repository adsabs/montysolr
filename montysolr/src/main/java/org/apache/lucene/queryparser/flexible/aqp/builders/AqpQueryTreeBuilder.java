package org.apache.lucene.queryparser.flexible.aqp.builders;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardQueryBuilder;
import org.apache.lucene.queryparser.flexible.standard.processors.StandardQueryNodeProcessorPipeline;
import org.apache.lucene.search.Query;

/**
 * This query tree builder only defines the necessary methods for
 * debugging. 
 *
 * @see QueryTreeBuilder
 * @see StandardQueryNodeProcessorPipeline
 */
public class AqpQueryTreeBuilder extends QueryTreeBuilder implements
        StandardQueryBuilder {

    public final static String SYNONYMS = "Treat_as_synonyms";

    private boolean debug = false;
    private int counter = 0;

    public AqpQueryTreeBuilder(boolean debug) {
        this.setDebug(debug);
        init();
    }

    public AqpQueryTreeBuilder() {
        init();
    }

    public void setDebug(boolean val) {
        if (val != debug) {
            debug = val;
            init();
        }
        debug = val;
    }

    public boolean isInDebugMode() {
        return debug;
    }

    public void init() {
        throw new IllegalAccessError("AqpQueryTreeBuilder must be subclassed and has the init() method");
    }

    @Override
    public Query build(QueryNode queryNode) throws QueryNodeException {
        this.counter = 0;
        return (Query) super.build(queryNode);
    }


    public void setBuilder(Class<? extends QueryNode> queryNodeClass,
                           QueryBuilder builder) {
        if (this.debug) {
            super.setBuilder(queryNodeClass, new DebuggingNodeBuilder(queryNodeClass,
                    builder));
        } else {
            super.setBuilder(queryNodeClass, builder);
        }
    }

    class DebuggingNodeBuilder implements QueryBuilder {
        private Class<? extends QueryNode> clazz = null;
        private QueryBuilder realBuilder = null;

        DebuggingNodeBuilder(Class<? extends QueryNode> queryNodeClass,
                             QueryBuilder builder) {
            clazz = queryNodeClass;
            realBuilder = builder;
        }

        public Object build(QueryNode queryNode) throws QueryNodeException {
            System.out.println("--------------------------------------------");
            System.out.println("step     " + counter++ + ".");
            System.out.println("builder: " + realBuilder.getClass().getName());
            System.out.println("node:    " + clazz.getName());
            System.out.println(queryNode.toString());
            System.out.println("   -->");
            Object result = realBuilder.build(queryNode);
            if (result != null) {
                System.out.println(result + "  <"
                        + result.getClass().getName() + ">");
            } else {
                System.out.println("null");
            }
            System.out.println("--------------------------------------------");
            return result;
        }

    }

}
