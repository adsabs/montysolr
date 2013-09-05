package org.apache.lucene.queryparser.flexible.aqp.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.queryparser.flexible.aqp.builders.AqpFunctionQueryBuilder;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessor;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessor.OriginalInput;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.OpaqueQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNodeImpl;
import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;
import org.apache.lucene.queryparser.flexible.standard.parser.ParseException;

/**
 * This QNode receives all the children of the QFUNC node, so typically
 *                            QFUNC
 *                              |
 *                            /   \
 *                        fName   DEFOP
 *                                  |
 *                                COMMA
 *                                  |
 *                             /    |     \
 *                          ...  MODIFIER  ...
 *                                  |
 *                              TMODIFIER
 *                                  |
 *                                FIELD
 *                                  |
 *                                Q<node>
 *                                  |
 *                                <value>
 *                                
 * This node carries within itself a 'builder' which is responsible for
 * turning the node into a Query at *build* time. Ie. after the processors
 * finished running (that is the step 2).
 * 
 * @author rchyla
 *
 */
public class AqpFunctionQueryNode extends QueryNodeImpl implements QueryNode {

	private static final long serialVersionUID = 751068795564006998L;
	private AqpFunctionQueryBuilder builder = null;
	private String name = null;
	private OriginalInput originalInput = null;
	private List<OriginalInput> funcValues;
	private Set<String> nodesToCount = new HashSet<String>(Arrays.asList("QRANGEIN", "QRANGEEX", "QFUNC"));
	
	public AqpFunctionQueryNode(String name, AqpFunctionQueryBuilder builder, List<OriginalInput> values) throws ParseException {
		allocate();
		setLeaf(true);
		originalInput = values.get(0);
		funcValues = values;
		this.builder = builder;
		this.name = name;
	}
	
	public AqpFunctionQueryNode(String name, AqpFunctionQueryBuilder builder, AqpANTLRNode node) throws ParseException {
		allocate();
		setLeaf(true);
		
		
		originalInput = AqpQProcessor.getOriginalInput(node, nodesToCount);
		funcValues = new ArrayList<OriginalInput>();
		
		if (!node.isLeaf()) {
			AqpANTLRNode container = new AqpANTLRNode(node.getTree());
			ArrayList<QueryNode> children = new ArrayList<QueryNode>();
  		for (QueryNode n: node.getChildren()) {
  			int l = ((AqpANTLRNode) n).hasTokenName("QDELIMITER", 0);
  			if (l > 0 && l < 4) { // MODIFIER/TMODIFIER/FIELD/QDELIMITER
  				container.set(children);
  				funcValues.add(AqpQProcessor.getOriginalInput((AqpANTLRNode) container, nodesToCount));
  				children.clear();
  				continue;
  			}
  			else {
  				children.add(n);
  			}
			}
  		
  		if (children.size() > 0) {
				container.set(children);
				funcValues.add(AqpQProcessor.getOriginalInput((AqpANTLRNode) container, nodesToCount));
				children.clear();
			}
  		
  	}
		else {
			funcValues.add(AqpQProcessor.getOriginalInput((AqpANTLRNode) node, nodesToCount));
		}
		
		this.builder = builder;
		this.name = name;
		trimValues();
	}

	public AqpFunctionQueryNode(String funcName, AqpFunctionQueryBuilder builder, OriginalInput origInput, ArrayList<OriginalInput> values) {
		allocate();
		setLeaf(true);
		this.originalInput = origInput;
		funcValues = values;
		this.name = funcName;
		this.builder = builder;
		trimValues();
  }

	public String toString() {
		StringBuffer bo = new StringBuffer();
		bo.append("<function name=\"");
		bo.append(this.name == null ? getBuilder().getClass() : this.name);
		bo.append(" originalInput=\"" + originalInput + "\"");
		bo.append("\">\n");
		for (OriginalInput child: this.getFuncValues()) {
			bo.append("<funcValue>" + child.toString() + "</funcValue>\n");
		}
		bo.append("</function>");
		return bo.toString();
	}
	
	private void trimValues() {
		for (OriginalInput oi: funcValues) {
			oi.value = oi.value.trim();
		}
	}
	
	public QueryNode getChild() {
		return getChildren().get(0);
	}

	public CharSequence toQueryString(EscapeQuerySyntax escapeSyntaxParser) {
		return getBuilder().toString();
	}
	
	public QueryBuilder getBuilder() {
		return builder;
	}
	
	public String getName() {
		return name;
	}
	
	public Boolean canBeAnalyzed() {
	  return false;
	}
	
	public List<OriginalInput> getFuncValues() {
		return funcValues;
	}
	
	public OriginalInput getOriginalInput() {
		return originalInput;
	}
}
