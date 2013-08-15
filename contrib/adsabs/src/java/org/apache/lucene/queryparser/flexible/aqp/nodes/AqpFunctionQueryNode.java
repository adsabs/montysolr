package org.apache.lucene.queryparser.flexible.aqp.nodes;

import java.util.ArrayList;
import java.util.List;

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
	
	public AqpFunctionQueryNode(String name, AqpFunctionQueryBuilder builder, List<OriginalInput> values) throws ParseException {
		allocate();
		setLeaf(true);
		originalInput = null;
		funcValues = values;
		this.builder = builder;
		this.name = name;
	}
	
	public AqpFunctionQueryNode(String name, AqpFunctionQueryBuilder builder, AqpANTLRNode node) throws ParseException {
		allocate();
		setLeaf(true);
		
		originalInput = AqpQProcessor.getOriginalInput(node);
		
		if (node.getChildren().size() == 1 && node.getChild("COMMA") != null) {
  		node = node.getChild("COMMA");
  	}
		
		funcValues = new ArrayList<OriginalInput>();
		for (QueryNode n: node.getChildren()) {
			funcValues.add(AqpQProcessor.getOriginalInput((AqpANTLRNode) n));
		}
		
		this.builder = builder;
		this.name = name;
	}

	public String toString() {
		StringBuffer bo = new StringBuffer();
		bo.append("<function name=\"");
		bo.append(this.name == null ? getBuilder().getClass() : this.name);
		bo.append("originalInput=\"" + originalInput + "\"");
		bo.append("\">\n");
		for (QueryNode child: this.getChildren()) {
			bo.append(child.toString());
		}
		bo.append("\n</function>");
		return bo.toString();
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
