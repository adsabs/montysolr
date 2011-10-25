package org.apache.lucene.queryParser.aqp.processors;

import java.util.List;

import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.aqp.config.DefaultFieldAttribute;
import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.config.QueryConfigHandler;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.BoostQueryNode;
import org.apache.lucene.queryParser.core.nodes.FieldQueryNode;
import org.apache.lucene.queryParser.core.nodes.FuzzyQueryNode;
import org.apache.lucene.queryParser.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryParser.core.nodes.ModifierQueryNode.Modifier;
import org.apache.lucene.queryParser.core.nodes.ParametricQueryNode;
import org.apache.lucene.queryParser.core.nodes.ParametricQueryNode.CompareOperator;
import org.apache.lucene.queryParser.core.nodes.ParametricRangeQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryParser.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryParser.standard.parser.EscapeQuerySyntaxImpl;
import org.apache.lucene.queryParser.standard.parser.ParseException;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

public class AqpATOMProcessor extends QueryNodeProcessorImpl implements
		QueryNodeProcessor {

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		return node;
	}

	@Override
	protected QueryNode postProcessNode(QueryNode node)
			throws QueryNodeException {
		
		if (node instanceof AqpANTLRNode && ((AqpANTLRNode) node).getTokenLabel().equals("ATOM")) {
			AqpANTLRNode n = (AqpANTLRNode) node;
			AqpANTLRNode modifierNode = n.getChild("MODIFIER");
			AqpANTLRNode fieldNode = n.getChild("FIELD");
			AqpANTLRNode valueNode = n.getChild("VALUE");
			
			String field = getFieldValue(fieldNode);
			
			Float boost = null;
			Float fuzzy = null;
			
			AqpANTLRNode tModifierNode = valueNode.getChild("TMODIFIER");
			if (tModifierNode!=null) {
				AqpANTLRNode boostNode = tModifierNode.getChild("BOOST");
				AqpANTLRNode fuzzyNode = tModifierNode.getChild("FUZZY");
				
				if (boostNode!=null && boostNode.getChildren()!=null)
					boost = ((AqpANTLRNode)boostNode.getChildren().get(0)).getTokenInputFloat();
				if (fuzzyNode!=null && fuzzyNode.getChildren()!=null)
					fuzzy = ((AqpANTLRNode)fuzzyNode.getChildren().get(0)).getTokenInputFloat();
			}
			
			QueryNode userInput = createQueryNodeFromInput(field, valueNode, fuzzy);
			
			if (boost!=null) {
				userInput = new BoostQueryNode(userInput, boost);
			}
			
			if (modifierNode!=null && modifierNode.getChildren()!=null) {
				String mod = ((AqpANTLRNode) modifierNode.getChildren().get(0)).getTokenInput();
				if (mod.equals("+")) {
					userInput = new ModifierQueryNode(userInput, Modifier.MOD_REQ);
				}
				else if (mod.equals("-")) {
					userInput = new ModifierQueryNode(userInput, Modifier.MOD_NOT);
				}
				else {
					throw new IllegalArgumentException("This processor understands only +/- as modifiers");
				}
			}
			
			return userInput;
			
		}
		return node;
	}


	private QueryNode createQueryNodeFromInput(String field, AqpANTLRNode valueNode, Float fuzzy) 
		throws QueryNodeException {
		
		List<QueryNode> children = valueNode.getChildren();
		AqpANTLRNode tmodifier = valueNode.getChild("TMODIFIER");
		if (tmodifier!=null) {
			children.remove(tmodifier);
		}
		
		AqpANTLRNode subChild;
		
		// TODO: Make FuzzyProcessor which removes invalid nodes
		if (fuzzy!=null) {
			AqpANTLRNode c = (AqpANTLRNode) children.get(0);
			AqpANTLRNode inputNode = (AqpANTLRNode) c.getChildren().get(0);
			return new FuzzyQueryNode(field, 
					inputNode.getTokenInput(), 
					fuzzy, inputNode.getTokenStart(), inputNode.getTokenEnd());
		}
		
		
		
		for (QueryNode child: children) {
			String tl = ((AqpANTLRNode) child).getTokenLabel();
			if (tl.equals("QNORMAL")) {
				subChild = (AqpANTLRNode) child.getChildren().get(0);
				return new FieldQueryNode(field,	
						EscapeQuerySyntaxImpl.discardEscapeChar(subChild.getTokenInput()), 
						subChild.getTokenStart(), subChild.getTokenEnd());
			}
			else if(tl.equals("QPHRASE")) {
				//TODO: so phrase is recognized only as being made of n>1 elements?
				//and these queries are identical? a) invenio b) "Invenio"
				subChild = (AqpANTLRNode) child.getChildren().get(0);
				return new FieldQueryNode(field,	
						EscapeQuerySyntaxImpl.discardEscapeChar(subChild.getTokenInput()), 
						subChild.getTokenStart(), subChild.getTokenEnd());
			}
			else if(tl.equals("QPHRASETRUNC") || tl.equals("QTRUNC")) {
				subChild = (AqpANTLRNode) child.getChildren().get(0);
				return new WildcardQueryNode(field,	
						EscapeQuerySyntaxImpl.discardEscapeChar(subChild.getTokenInput()), 
						subChild.getTokenStart(), subChild.getTokenEnd());
			}
			else if(tl.equals("QRANGEIN") || tl.equals("QRANGEEX")) {
				AqpANTLRNode lowerNode = (AqpANTLRNode) child.getChildren().get(0).getChildren().get(0);
				AqpANTLRNode upperNode = (AqpANTLRNode) child.getChildren().get(0).getChildren().get(1);
				CompareOperator lowerComparator = tl.equals("QRANGEIN") ? CompareOperator.GE : CompareOperator.GT;
				CompareOperator upperComparator = (lowerComparator==CompareOperator.GE) ? CompareOperator.LE : CompareOperator.LT;
				
				ParametricQueryNode lowerBound = new ParametricQueryNode(field, lowerComparator,
						lowerNode.getTokenInput(), lowerNode.getTokenStart(), lowerNode.getTokenEnd());
				ParametricQueryNode upperBound = new ParametricQueryNode(field, upperComparator,
						upperNode.getTokenInput(), upperNode.getTokenStart(), upperNode.getTokenEnd());
				return new ParametricRangeQueryNode(lowerBound, upperBound);
			}
		}
		throw new QueryNodeException(new MessageImpl(
                QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR, valueNode.toString(), 
                "Don't know how to parse"));
	}

	private String getFieldValue(AqpANTLRNode fieldNode) throws QueryNodeException {
		
		if (fieldNode!= null && fieldNode.getTokenInput()!=null) {
			return fieldNode.getTokenInput();
		}
		
		QueryConfigHandler queryConfig = getQueryConfigHandler();

		if (queryConfig != null) {
			
			if (queryConfig.hasAttribute(DefaultFieldAttribute.class)) {
				String defaultField = queryConfig.getAttribute(
						DefaultFieldAttribute.class).getDefaultField();
				return defaultField;
			}
		}
		throw new QueryNodeException(new MessageImpl(
                QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
                "Configuration error: " + DefaultFieldAttribute.class.toString() + " is missing"));
	}

	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
			throws QueryNodeException {
		return children;
	}

}
