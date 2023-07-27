package org.apache.lucene.queryparser.flexible.aqp.nodes;

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

import org.apache.lucene.queryparser.flexible.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.GroupQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.ModifierQueryNode.Modifier;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;

import java.util.List;

/**
 * A {@link AqpBooleanQueryNode} represents base boolean operation performed on
 * a list of nodes. It will apply the @{link ModifierQueryNode} to the clauses.
 * The normal behaviour is not to override the ModifierQueryNode values, if
 * already present.
 */
public class AqpBooleanQueryNode extends BooleanQueryNode {

    private static final long serialVersionUID = -5974910790857168198L;

    protected String operator = "DEFOP";
    protected boolean overrideModifiers = false;

    /**
     * @param clauses
     *          - the query nodes to be op'ed
     */
    public AqpBooleanQueryNode(List<QueryNode> clauses) {
        super(clauses);

    }

    @Override
    public String toString() {
        if (getChildren() == null || getChildren().size() == 0)
            return "<boolean operation='" + operator + "'/>";
        StringBuilder sb = new StringBuilder();
        sb.append("<boolean operation='" + operator + "'>");
        for (QueryNode child : getChildren()) {
            sb.append("\n");
            sb.append(child.toString());

        }
        sb.append("\n</boolean>");
        return sb.toString();
    }

    @Override
    public CharSequence toQueryString(EscapeQuerySyntax escapeSyntaxParser) {
        if (getChildren() == null || getChildren().size() == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        String filler = "";
        for (QueryNode child : getChildren()) {
            sb.append(filler).append(child.toQueryString(escapeSyntaxParser));
            filler = " " + operator + " ";
        }

        // in case is root or the parent is a group node avoid parenthesis
        if ((getParent() != null && getParent() instanceof GroupQueryNode)
                || isRoot())
            return sb.toString();
        else
            return "( " + sb + " )";
    }

    public void applyModifier(List<QueryNode> clauses, Modifier mod) {
        for (int i = 0; i < clauses.size(); i++) {
            QueryNode child = clauses.get(i);

            if (child instanceof ModifierQueryNode || child instanceof GroupQueryNode) {
                if (overrideModifiers) {
                    clauses
                            .set(i,
                                    new ModifierQueryNode(((ModifierQueryNode) child).getChild(),
                                            mod));
                }
            } else {
                clauses.set(i, new ModifierQueryNode(child, mod));
            }
        }
    }

    public void setOverrideModifiers(boolean val) {
        this.overrideModifiers = val;
    }

    public void setOperator(String op) {
        operator = op;
    }

    public String getOperator() {
        return operator;
    }
}
