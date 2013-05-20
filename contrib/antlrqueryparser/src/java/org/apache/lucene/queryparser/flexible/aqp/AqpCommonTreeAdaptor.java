package org.apache.lucene.queryparser.flexible.aqp;

import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTreeAdaptor;

import org.apache.lucene.queryparser.flexible.aqp.AqpCommonTree;

public class AqpCommonTreeAdaptor extends CommonTreeAdaptor {

  private Map<Integer, String> typeToNameMap;

  public AqpCommonTreeAdaptor(String[] tokenNames) {
    typeToNameMap = computeTypeToNameMap(tokenNames);
  }

  public Object create(Token payload) {
    AqpCommonTree tree = new AqpCommonTree(payload);
    tree.setTypeToNameMap(this.typeToNameMap);
    return tree;
  }

  public Token getToken(Object t) {
    if (t instanceof AqpCommonTree) {
      return ((AqpCommonTree) t).getToken();
    }
    return null; // no idea what to do
  }

  /*
   * translate token types into meaningful names it will be used later on
   */
  public Map<Integer, String> computeTypeToNameMap(String[] tokenNames) {
    Map<Integer, String> m = new HashMap<Integer, String>();
    if (tokenNames == null) {
      return m;
    }
    for (int ttype = Token.MIN_TOKEN_TYPE; ttype < tokenNames.length; ttype++) {
      String name = tokenNames[ttype];
      m.put(new Integer(ttype), name);
    }
    return m;
  }

}
