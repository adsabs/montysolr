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
import org.apache.lucene.queryParser.core.nodes.MatchAllDocsQueryNode;
import org.apache.lucene.queryParser.core.nodes.ModifierQueryNode;
import org.apache.lucene.queryParser.core.nodes.ParametricQueryNode;
import org.apache.lucene.queryParser.core.nodes.ParametricRangeQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.nodes.ModifierQueryNode.Modifier;
import org.apache.lucene.queryParser.core.nodes.ParametricQueryNode.CompareOperator;
import org.apache.lucene.queryParser.core.nodes.QuotedFieldQueryNode;
import org.apache.lucene.queryParser.core.nodes.SlopQueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessorImpl;
import org.apache.lucene.queryParser.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryParser.standard.parser.EscapeQuerySyntaxImpl;
import org.apache.lucene.search.MatchAllDocsQuery;

public class AqpVALUEProcessor extends QueryNodeProcessorImpl implements
		QueryNodeProcessor {

	@Override
	protected QueryNode preProcessNode(QueryNode node)
			throws QueryNodeException {
		return node;
	}

	@Override
	protected QueryNode postProcessNode(QueryNode node)
			throws QueryNodeException {
		if (node instanceof AqpANTLRNode
				&& ((AqpANTLRNode) node).getTokenLabel().equals("VALUE")) {

			AqpANTLRNode valueNode = (AqpANTLRNode) node;

			Float boost = null;
			Float fuzzy = null;

			AqpANTLRNode tModifierNode = valueNode.getChild("TMODIFIER");
			if (tModifierNode != null) {
				AqpANTLRNode boostNode = tModifierNode.getChild("BOOST");
				AqpANTLRNode fuzzyNode = tModifierNode.getChild("FUZZY");

				if (boostNode != null && boostNode.getChildren() != null)
					boost = ((AqpANTLRNode) boostNode.getChildren().get(0))
							.getTokenInputFloat();
				if (fuzzyNode != null && fuzzyNode.getChildren() != null)
					fuzzy = ((AqpANTLRNode) fuzzyNode.getChildren().get(0))
							.getTokenInputFloat();
			}

			List<QueryNode> children = valueNode.getChildren();
			if (tModifierNode != null) {
				children.remove(tModifierNode);
			}
			
			String defaultField = getDefaultFieldName();
			
			QueryNode userInput = createQueryNodeFromInput(defaultField, fuzzy, children);

			if (boost != null) {
				userInput = new BoostQueryNode(userInput, boost);
			}
			
			return userInput;

		}
		return node;
	}

	@Override
	protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
			throws QueryNodeException {
		return children;
	}
	
	private String getDefaultFieldName() throws QueryNodeException {
		QueryConfigHandler queryConfig = getQueryConfigHandler();
		
		String defaultField = null;
		if (queryConfig != null) {
			
			if (queryConfig.hasAttribute(DefaultFieldAttribute.class)) {
				defaultField = queryConfig.getAttribute(
						DefaultFieldAttribute.class).getDefaultField();
				return defaultField;
			}
		}
		throw new QueryNodeException(new MessageImpl(
                QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
                "Configuration error: " + DefaultFieldAttribute.class.toString() + " is missing"));
	}

	private QueryNode createQueryNodeFromInput(String field,
			Float fuzzy, List<QueryNode> valueChildren) throws QueryNodeException {




		AqpANTLRNode subChild;
		
		for (QueryNode child : valueChildren) {
			String tl = ((AqpANTLRNode) child).getTokenLabel();
			if (tl.equals("QNORMAL")) {
				subChild = (AqpANTLRNode) child.getChildren().get(0);
				if (fuzzy != null) {
					AqpANTLRNode inputNode = (AqpANTLRNode) valueChildren.get(0).getChildren().get(0);
					return new FuzzyQueryNode(field, inputNode.getTokenInput(), fuzzy,
							inputNode.getTokenStart(), inputNode.getTokenEnd());
				}
				else {
				    return new FieldQueryNode(field,
						EscapeQuerySyntaxImpl.discardEscapeChar(subChild
								.getTokenInput()), subChild.getTokenStart(),
						subChild.getTokenEnd());
				}
				
			} else if (tl.equals("QPHRASE")) {
				subChild = (AqpANTLRNode) child.getChildren().get(0);
				QueryNode fq = new QuotedFieldQueryNode(field,
						EscapeQuerySyntaxImpl.discardEscapeChar(subChild
								.getTokenInput()), subChild.getTokenStart(),
						subChild.getTokenEnd());
				if (fuzzy!=null) {
					return new SlopQueryNode(fq, fuzzy.intValue());
				}
				return fq;
			} else if (tl.equals("QPHRASETRUNC") || tl.equals("QTRUNCATED")) {
				subChild = (AqpANTLRNode) child.getChildren().get(0);
				QueryNode wq = new WildcardQueryNode(field,
						EscapeQuerySyntaxImpl.discardEscapeChar(subChild
								.getTokenInput()), subChild.getTokenStart(),
						subChild.getTokenEnd());
				if (fuzzy!=null) {
					return new SlopQueryNode(wq, fuzzy.intValue());
				}
				return wq;
			} else if (tl.equals("QANYTHING")) {
				subChild = (AqpANTLRNode) child.getChildren().get(0);
				//if (field.equals("*")) { // this will never happen now, but may change in the future
				//	return new MatchAllDocsQueryNode();
				//}
				
				if (fuzzy!=null) {
					throw new QueryNodeException(new MessageImpl(
							QueryParserMessages.INVALID_SYNTAX,
							valueChildren.toString(), "It makes not sense to use *~" + fuzzy));
				}
				return new FieldQueryNode(field, "*", subChild.getTokenStart(), subChild.getTokenEnd());
				
			} else if (tl.equals("QRANGEIN") || tl.equals("QRANGEEX")) {
				if (fuzzy!=null) {
					throw new QueryNodeException(new MessageImpl(
							QueryParserMessages.INVALID_SYNTAX,
							valueChildren.toString(), "Use of ~ not allowed for parametric queries"));
				}
				
				AqpANTLRNode lowerNode = (AqpANTLRNode) child.getChildren()
						.get(0).getChildren().get(0);
				AqpANTLRNode upperNode = (AqpANTLRNode) child.getChildren()
						.get(1).getChildren().get(0);
				CompareOperator lowerComparator = tl.equals("QRANGEIN") ? CompareOperator.GE
						: CompareOperator.GT;
				CompareOperator upperComparator = (lowerComparator == CompareOperator.GE) ? CompareOperator.LE
						: CompareOperator.LT;

				ParametricQueryNode lowerBound = new ParametricQueryNode(field,
						lowerComparator, lowerNode.getTokenInput(),
						lowerNode.getTokenStart(), lowerNode.getTokenEnd());
				ParametricQueryNode upperBound = new ParametricQueryNode(field,
						upperComparator, upperNode.getTokenInput(),
						upperNode.getTokenStart(), upperNode.getTokenEnd());
				return new ParametricRangeQueryNode(lowerBound, upperBound);
			}
		}
		throw new QueryNodeException(new MessageImpl(
				QueryParserMessages.LUCENE_QUERY_CONVERSION_ERROR,
				valueChildren.toString(), "Don't know how to parse"));
	}

}
