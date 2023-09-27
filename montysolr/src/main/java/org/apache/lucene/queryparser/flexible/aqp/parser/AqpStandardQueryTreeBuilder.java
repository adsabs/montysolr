package org.apache.lucene.queryparser.flexible.aqp.parser;

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

import org.apache.lucene.queryparser.flexible.aqp.builders.AqpFieldQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpQueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.builders.AqpSlowFuzzyQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.nodes.SlowFuzzyQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.*;
import org.apache.lucene.queryparser.flexible.standard.builders.*;
import org.apache.lucene.queryparser.flexible.standard.nodes.*;

/**
 * This query tree builder provides configuration for the standard
 * lucene syntax.
 *
 * @see AqpStandardLuceneParser
 */
public class AqpStandardQueryTreeBuilder extends AqpQueryTreeBuilder implements
        StandardQueryBuilder {


    public void init() {
        setBuilder(GroupQueryNode.class, new GroupQueryNodeBuilder());
        setBuilder(FieldQueryNode.class, new AqpFieldQueryNodeBuilder());
        setBuilder(BooleanQueryNode.class, new BooleanQueryNodeBuilder());
        setBuilder(SlowFuzzyQueryNode.class, new AqpSlowFuzzyQueryNodeBuilder());
        setBuilder(FuzzyQueryNode.class, new FuzzyQueryNodeBuilder());
        setBuilder(PointRangeQueryNode.class, new PointRangeQueryNodeBuilder());
        setBuilder(BoostQueryNode.class, new BoostQueryNodeBuilder());
        setBuilder(ModifierQueryNode.class, new ModifierQueryNodeBuilder());
        setBuilder(WildcardQueryNode.class, new WildcardQueryNodeBuilder());
        setBuilder(TokenizedPhraseQueryNode.class, new PhraseQueryNodeBuilder());
        setBuilder(MatchNoDocsQueryNode.class, new MatchNoDocsQueryNodeBuilder());
        setBuilder(PrefixWildcardQueryNode.class,
                new PrefixWildcardQueryNodeBuilder());
        setBuilder(TermRangeQueryNode.class, new TermRangeQueryNodeBuilder());
        setBuilder(RegexpQueryNode.class, new RegexpQueryNodeBuilder());
        setBuilder(SlopQueryNode.class, new SlopQueryNodeBuilder());
        setBuilder(MultiPhraseQueryNode.class, new MultiPhraseQueryNodeBuilder());
        setBuilder(MatchAllDocsQueryNode.class, new MatchAllDocsQueryNodeBuilder());
        setBuilder(BooleanQueryNode.class, new BooleanQueryNodeBuilder());
        setBuilder(SynonymQueryNode.class, new SynonymQueryNodeBuilder());
    }


}
