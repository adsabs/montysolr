package org.apache.lucene.queryParser.aqp.util;

import org.apache.lucene.queryParser.aqp.nodes.AqpANTLRNode;
import org.apache.lucene.queryParser.core.nodes.FieldQueryNode;
import org.apache.lucene.queryParser.core.nodes.QueryNode;

public class AqpUtils {
	
	public enum Modifier {
		PLUS,
		MINUS,
		UNKNOWN;
	}
	
	public enum Operator {
		AND,
		OR,
		NOT,
		WITH,
		PARAGRAPH;
	}
	
	public static String getFirstChildInputString(QueryNode node) {
		return getFirstChildInputString((AqpANTLRNode) node);
	}
	public static String getFirstChildInputString(AqpANTLRNode node) {
		if (node!= null && node.getChildren()!=null) {
			return ((AqpANTLRNode) node.getChildren().get(0)).getTokenInput();
		}
		return null;
	}
	
	public static Float getFirstChildInputFloat(AqpANTLRNode node) {
		if (node!= null && node.getChildren()!=null) {
			return Float.valueOf(((AqpANTLRNode) node.getChildren().get(0)).getTokenInput());
		}
		return null;
	}
	
	public static Modifier getFirstChildInputModifier(AqpANTLRNode node) {
		if (node!= null && node.getChildren()!=null) {
			String val = ((AqpANTLRNode) node.getChildren().get(0)).getTokenName();
			if (val.equals("PLUS")) {
				return Modifier.PLUS;
			}
			else if (val.equals("MINUS")) {
				return Modifier.MINUS;
			}
			else {
				return Modifier.UNKNOWN;
			}
		}
		return null;
	}
	
	public static void applyFieldToAllChildren(String field, QueryNode node) {
		
		if (node instanceof FieldQueryNode) {
			((FieldQueryNode) node).setField(field);
		}
		if (node.getChildren()!=null) {
			for (QueryNode child : node.getChildren()) {
				applyFieldToAllChildren(field, child);
			}
		}
	}
}
