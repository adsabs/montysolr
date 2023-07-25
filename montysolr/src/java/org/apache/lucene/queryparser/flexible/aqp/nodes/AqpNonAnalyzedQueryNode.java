package org.apache.lucene.queryparser.flexible.aqp.nodes;

/**
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

import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QuotedFieldQueryNode;

/**
 * The node represents a query that will not be processed
 * by an analyzer. It will be served to the search engine as it is
 * 
 * <p>
 * 
 * Example: e(+)
 */
public class AqpNonAnalyzedQueryNode extends QuotedFieldQueryNode {

  private static final long serialVersionUID = 6921391439471630844L;

  /**
   * @param field
   *          - field name
   * @param text
   *          - the query
   * @param begin
   *          - position in the query string
   * @param end
   *          - position in the query string
   */
  public AqpNonAnalyzedQueryNode(CharSequence field, CharSequence text,
      int begin, int end) {
    super(field, text, begin, end);
  }

  public AqpNonAnalyzedQueryNode(FieldQueryNode fqn) {
    this(fqn.getField(), fqn.getText(), fqn.getBegin(), fqn.getEnd());
  }

  @Override
  public String toString() {
    return "<nonAnalyzed field='" + this.field + "' term='" + this.text + "'/>";
  }

  @Override
  public AqpNonAnalyzedQueryNode cloneTree() throws CloneNotSupportedException {
    AqpNonAnalyzedQueryNode clone = (AqpNonAnalyzedQueryNode) super.cloneTree();

    // nothing to do here

    return clone;
  }

}
