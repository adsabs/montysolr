package org.apache.lucene.queryparser.flexible.aqp.processors;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpImmutableGroupQueryNode;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.BooleanQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.BoostQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.GroupQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryparser.flexible.standard.nodes.BooleanModifierNode;

/**
 * Optimizes the query by removing the superfluous GroupQuery nodes. We harvest
 * all parameters from fuzzy, boost, and modifier nodes and apply those that are
 * closest to the actual query.
 * 
 * <br/>
 * Example:
 * 
 * <pre>
 * this (+(-(+(-(that thus))^0.1))^0.3)
 * </pre>
 * 
 * Will be optimized into (when DEFOP = AND):
 * 
 * <pre>
 * +field:this -((+field:that +field:thus)^0.1)
 * </pre>
 * 
 * 
 */
public class AqpGroupQueryOptimizerProcessor extends QueryNodeProcessorImpl
    implements QueryNodeProcessor {

  @Override
  protected QueryNode preProcessNode(QueryNode node) throws QueryNodeException {
    if (node instanceof GroupQueryNode && !(node instanceof AqpImmutableGroupQueryNode)) {
      QueryNode immediateChild = node.getChildren().get(0);
      ClauseData data = harvestData(immediateChild);
      QueryNode changedNode = data.getLastChild();

      if (data.getLevelsDeep() > 0) {
        boolean modified = false;
        if (data.getBoost() != null) {
          changedNode = new BoostQueryNode(changedNode, data.getBoost());
          modified = true;
        }
        if (data.getModifier() != null) {
          changedNode = new ModifierQueryNode(changedNode, data.getModifier());
          modified = true;
          /*
           * Why was I doing this? Firstly, it is buggy, the second branch
           * always executes - why am i creating new BooleanNode?
           * List<QueryNode> children = new ArrayList<QueryNode>(); if
           * (children.size() == 1) { return children.get(0); } else {
           * children.add(new ModifierQueryNode(node, data.getModifier())); node
           * = new BooleanQueryNode(children); }
           */
        }
        /*
         * if (modified && node.getParent()==null) { List<QueryNode> children =
         * new ArrayList<QueryNode>(); children.add(node); changedNode = new
         * BooleanQueryNode(children); }
         */
        return changedNode;
      }
      return immediateChild;
    }
    return node;
  }

  @Override
  protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {
  	// also detect the situations when the default operator included modifier
  	// but user supplied modifer before the clause, eg. 
  	// value -field:(somethig something)
  	// we should honour the closest modifier, but it is not caught in the
  	// pre-processor, because that one is looking only at groups, and
  	// this will be one level above the group; but after it was simplified
  	// we can see it here, it will be chain of modifier>modifier>modifier...
  	if (node instanceof ModifierQueryNode && node.getChildren().size() == 1 
  			&& node.getChildren().get(0) instanceof ModifierQueryNode) {
  		return node.getChildren().get(0);
  	}
    return node;
  }

  @Override
  protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
      throws QueryNodeException {
    return children;
  }

  /*
   * methods below not used now, but might be added - it tries to compact
   * consecutive CLAUSE nodes into one clause, taking only the last
   * modifier/tmodifier values
   */
  private ClauseData harvestData(QueryNode clauseNode) {
    ClauseData data = new ClauseData();
    harvestData(clauseNode, data);
    return data;
  }

  private void harvestData(QueryNode node, ClauseData data) {

    if (node instanceof ModifierQueryNode) {
      data.setModifier(((ModifierQueryNode) node).getModifier());
    } else if (node instanceof BoostQueryNode) {
      data.setBoost(((BoostQueryNode) node).getValue());
    } else if (node instanceof GroupQueryNode) {
      data.addLevelsDeep();
    } else {
      data.setLastChild(node);
      return; // break processing
    }
    if (!node.isLeaf() && node.getChildren().size() == 1) {
      harvestData(node.getChildren().get(0), data);
    }

  }

  class ClauseData {
    private ModifierQueryNode.Modifier modifier;
    private Float boost;
    private QueryNode lastValidNode;
    private boolean keepOutmost = true; // change this to false if you want that
    // modifiers that are closer to the clause are applied to ti
    private int levelsDeep = 0;

    ClauseData() {
    }

    ClauseData(ModifierQueryNode.Modifier mod, Float boost) {
      this.modifier = mod;
      this.boost = boost;
    }

    public ModifierQueryNode.Modifier getModifier() {
      return modifier;
    }

    public void setModifier(ModifierQueryNode.Modifier modifier) {
      if (keepOutmost && this.modifier != null) {
        return;
      }
      this.modifier = modifier;
    }

    public Float getBoost() {
      return boost;
    }

    public void setBoost(Float boost) {
      if (keepOutmost && this.boost != null) {
        return;
      }
      this.boost = boost;
    }

    public QueryNode getLastChild() {
      return lastValidNode;
    }

    public void setLastChild(QueryNode lastNonClause) {
      this.lastValidNode = lastNonClause;
    }

    public int getLevelsDeep() {
      return levelsDeep;
    }

    public void addLevelsDeep() {
      this.levelsDeep++;
    }
  }

}
