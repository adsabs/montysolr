package org.apache.lucene.queryParser.aqp.processors;

import org.apache.lucene.messages.MessageImpl;
import org.apache.lucene.queryParser.aqp.AqpCommonTree;
import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.QueryNodeException;
import org.apache.lucene.queryParser.core.messages.QueryParserMessages;
import org.apache.lucene.queryParser.core.nodes.QueryNode;
import org.apache.lucene.queryParser.core.processors.QueryNodeProcessor;
import org.apache.lucene.queryParser.standard.config.BoostAttribute;

public class AqpAdslabsQPOSITIONProcessor extends AqpQProcessorPost implements
		QueryNodeProcessor {
	
	@Override
	public boolean nodeIsWanted(AqpANTLRNode node) {
		if (node.getTokenLabel().equals("QPOSITION")) {
			return true;
		}
		return false;
	}
	
	@Override
	public QueryNode createQNode(AqpANTLRNode node) throws QueryNodeException {
		AqpANTLRNode subChild = (AqpANTLRNode) node.getChildren().get(0);
		String input = subChild.getTokenInput();
		
		if (input.equals("^~")) {
			throw new QueryNodeException(new MessageImpl(
	                QueryParserMessages.INVALID_SYNTAX,
	                "It is not allowed to use ^~, especially the carat (^) must be accompanied by a number, eg. word^0.5~"));
		}
		
		Integer start = 0;
		Integer end = null;
		
		if (input.startsWith("^")) {
			input = input.substring(1, input.length());
		}
		
		if (input.endsWith("$")) {
			input = input.substring(1, input.length());
			end = -1;
		}
		
		AqpCommonTree tree = node.getTree();
		
		AqpANTLRNode semicolonNode = new AqpANTLRNode(tree);
		semicolonNode.setTokenName("SEMICOLON");
		semicolonNode.setTokenLabel(";");
		
		/*
		AqpANTLRNode commaNode = new AqpANTLRNode(tree);
		semicolonNode.setTokenName("COMMA");
		semicolonNode.setTokenLabel(",");
		*/
		
		
		AqpANTLRNode author = new AqpANTLRNode(tree);
		author.setTokenName("QNORMAL");
		author.setTokenInput(input);
		semicolonNode.add(getChain(author));
		
		AqpANTLRNode startNode = new AqpANTLRNode(tree);
		startNode.setTokenName("QNORMAL");
		startNode.setTokenInput(String.valueOf(start));
		semicolonNode.add(getChain(startNode));
		
		if (end != null) {
			AqpANTLRNode endNode = new AqpANTLRNode(tree);
			endNode.setTokenName("QNORMAL");
			endNode.setTokenInput(String.valueOf(end));
			semicolonNode.add(getChain(endNode));
		}
		
		AqpANTLRNode funcNode = new AqpANTLRNode(tree);
		funcNode.setTokenName("QFUNC");
		funcNode.setTokenLabel("pos(");
		
		funcNode.add(semicolonNode);
		
		return funcNode;
	}
	
	protected AqpANTLRNode getChain(AqpANTLRNode finalNode) {
		
		AqpCommonTree tree = finalNode.getTree();
		
		AqpANTLRNode modifierNode = new AqpANTLRNode(tree);
		modifierNode.setTokenName("MODIFIER");
		modifierNode.setTokenLabel("MODIFIER");
		
		AqpANTLRNode tmodifierNode = new AqpANTLRNode(tree);
		tmodifierNode.setTokenName("TMODIFIER");
		tmodifierNode.setTokenLabel("TMODIFIER");
		
		AqpANTLRNode fieldNode = new AqpANTLRNode(tree);
		fieldNode.setTokenName("FIELD");
		fieldNode.setTokenLabel("FIELD");
		
		AqpANTLRNode qNode = new AqpANTLRNode(tree);
		qNode.setTokenName("QNORMAL");
		qNode.setTokenLabel("QNORMAL");
		
		
		modifierNode.add(tmodifierNode);
		tmodifierNode.add(fieldNode);
		fieldNode.add(qNode);
		qNode.add(finalNode);
		
		return modifierNode;
	}
	
}
