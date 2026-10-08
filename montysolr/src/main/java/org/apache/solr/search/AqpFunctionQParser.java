package org.apache.solr.search;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.apache.lucene.queries.function.FunctionQuery;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queries.function.valuesource.ConstValueSource;
import org.apache.lucene.queries.function.valuesource.DoubleConstValueSource;
import org.apache.lucene.queries.function.valuesource.LiteralValueSource;
import org.apache.lucene.queries.function.valuesource.QueryValueSource;
import org.apache.lucene.queryparser.flexible.aqp.NestedParseException;
import org.apache.lucene.queryparser.flexible.aqp.parser.ADSLexer;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpFunctionQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpQProcessor.OriginalInput;
import org.apache.lucene.queryparser.flexible.aqp.util.AqpQueryParserUtil;
import org.apache.lucene.queryparser.flexible.core.builders.QueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.search.Query;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.StrUtils;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.SchemaField;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class AqpFunctionQParser extends FunctionQParser {

    private static final String TAGID = QueryTreeBuilder.QUERY_TREE_BUILDER_TAGID.toLowerCase();

    public AqpFunctionQParser(String qstr, SolrParams localParams,
                              SolrParams params, SolrQueryRequest req) {
        super(qstr, localParams, params, req);
    }

    /**
     * Create a new QParser for parsing an embedded sub-query
     */
    @Override
    public QParser subQuery(String q, String defaultType) throws SyntaxError {
        if (recurseCount++ >= 100) {
            throw new SyntaxError("Infinite Recursion detected parsing query '" + qstr + "'");
        }
        if (defaultType == null && localParams != null) {
            // if not passed, try and get the defaultType from local params
            defaultType = localParams.get(QueryParsing.DEFTYPE);
        }
        if (q != null) {
            q = q.strip();
        }
        QParser nestedParser = getParser(q, defaultType, true, getReq());
        // Resolve the parser first: local parameters can override the requested type.
        if (!(nestedParser instanceof AqpAdsabsQParser)) {
            String input = nestedParser.getString();
            String normalized = normalizeSubquery(input, recurseCount, getReq().getParams());
            if (normalized != input) {
                nestedParser.setString(normalized);
                if (nestedParser.getLocalParams() != null) {
                    ModifiableSolrParams local = new ModifiableSolrParams(nestedParser.getLocalParams());
                    local.set(QueryParsing.V, normalized);
                    nestedParser.setLocalParams(local);
                }
            }
        }
        nestedParser.flags = this.flags;  // TODO: this would be better passed in to the constructor... change to a ParserContext object?
        nestedParser.recurseCount = recurseCount;
        recurseCount--;
        return nestedParser;
    }

    private static String normalizeSubquery(String value, int depth, SolrParams params) throws SyntaxError {
        if (value == null) {
            return null;
        }
        int firstQuote = 0;
        while (firstQuote < value.length()
                && !AqpQueryParserUtil.isTypographicDoubleQuote(value.charAt(firstQuote))) {
            firstQuote++;
        }
        if (firstQuote == value.length() && !value.contains("{!")) {
            return value;
        }
        if (depth >= 100) {
            throw new SyntaxError("Infinite recursion detected in nested query values");
        }

        ADSLexer lexer = new ADSLexer(new ANTLRStringStream(value));
        StringBuilder normalized = null;
        int copiedUntil = 0;
        for (Token token = lexer.nextToken(); token.getType() != Token.EOF; token = lexer.nextToken()) {
            if (token.getType() == ADSLexer.CURLY_QUOTE
                    || token.getType() == ADSLexer.UNTERMINATED_CURLY_PHRASE) {
                throw new SyntaxError("Empty or unterminated typographic quoted phrase");
            }
            CommonToken input = (CommonToken) token;
            boolean phrase = token.getType() == ADSLexer.PHRASE
                    || token.getType() == ADSLexer.PHRASE_ANYTHING;
            String header = null;
            if (phrase) {
                if (!AqpQueryParserUtil.isTypographicDoubleQuote(value.charAt(input.getStartIndex()))) {
                    continue;
                }
            } else if (token.getType() == ADSLexer.LOCAL_PARAMS) {
                SolrParams local = QueryParsing.getLocalParams(token.getText(), params);
                String query = local.get(QueryParsing.V);
                String converted = normalizeSubquery(query, depth + 1, params);
                if (query == converted) {
                    continue;
                }
                StringBuilder options = new StringBuilder("{!");
                for (Iterator<String> names = local.getParameterNamesIterator(); names.hasNext();) {
                    String name = names.next();
                    if (name.equals(QueryParsing.V)) {
                        appendLocalParam(options, name, converted);
                    } else {
                        for (String option : local.getParams(name)) {
                            appendLocalParam(options, name, option);
                        }
                    }
                }
                header = options.append('}').toString();
            } else {
                continue;
            }
            if (normalized == null) {
                normalized = new StringBuilder(value.length());
            }
            normalized.append(value, copiedUntil, input.getStartIndex());
            if (phrase) {
                normalized.append('"').append(value, input.getStartIndex() + 1, input.getStopIndex()).append('"');
            } else {
                normalized.append(header);
            }
            copiedUntil = input.getStopIndex() + 1;
        }
        return normalized == null ? value : normalized.append(value, copiedUntil, value.length()).toString();
    }

    private static void appendLocalParam(StringBuilder output, String name, String value) {
        if (value == null) {
            return;
        }
        output.append(' ').append(name).append("='");
        StrUtils.appendEscapedTextToBuilder(output, value, '\'');
        output.append('\'');
    }

    private int currChild = -1;
    private AqpFunctionQueryNode qNode = null;

    protected boolean canConsume() {
        return currChild + 1 <= qNode.getFuncValues().size() - 1;
    }

    protected OriginalInput consume() {
        currChild++;
        try {
            return qNode.getFuncValues().get(currChild);
        } catch (Exception e) {
            throw new NestedParseException("Function tried to get a new argument, but none is available" + qNode.toString());
        }

    }

    protected String consumeAsString() {
        OriginalInput qn = consume();
        return qn.value;

    }


    @Override
    protected ValueSource parseValueSource(boolean doConsumeDelimiter)
            throws SyntaxError {

        // check if there is a query already built inside our node
        OriginalInput node = consume();
        String input = node.value;


        if (input.charAt(0) == '"' || input.charAt(0) == '\'') {
            return new LiteralValueSource(input);
        } else if (input.charAt(0) == '$') {
            String val = getParam(input);
            if (val == null) {
                throw new SyntaxError("Missing param " + input + " while parsing function '" + val + "'");
            }

            QParser subParser = subQuery(val, "func");
            if (subParser instanceof FunctionQParser) {
                ((FunctionQParser) subParser).setParseMultipleSources(true);
            }
            Query subQuery = subParser.getQuery();
            if (subQuery instanceof FunctionQuery) {
                return ((FunctionQuery) subQuery).getValueSource();
            } else {
                return new QueryValueSource(subQuery, 0.0f);
            }
        } else if (req != null && req.getSchema().getField(input) != null) {
            SchemaField f = req.getSchema().getField(input);
            return f.getType().getValueSource(f, this);
        }

        StrParser p = new StrParser(input);

        try {
            Number num = p.getNumber();

            if (num instanceof Long) {
                return new ValueSourceParser.LongConstValueSource(num.longValue());
            } else if (num instanceof Double) {
                return new DoubleConstValueSource(num.doubleValue());
            } else {
                // shouldn't happen
                return new ConstValueSource(num.floatValue());
            }
        } catch (NumberFormatException e) {
            return new LiteralValueSource(input);
        }


    }


    public void setQueryNode(AqpFunctionQueryNode node) {
        this.qNode = node;
        this.currChild = -1;
        if (node.getOriginalInput() != null) {
            sp = new StrParser(node.getOriginalInput().value);
        }
    }

    public QueryNode getQueryNode() {
        return qNode;
    }


    public String parseId() throws SyntaxError {
        return consumeAsString();
    }


    public int parseInt() throws SyntaxError {
        String val = AqpQueryParserUtil.dequoteDoubleQuoted(consumeAsString());
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            throw new SyntaxError("Expected integer argument instead of: " + val, e);
        }
    }

    public Float parseFloat() throws SyntaxError {
        String str = consumeAsString();
        if (argWasQuoted()) throw new SyntaxError("Expected float instead of quoted string:" + str);
        float value = Float.parseFloat(str);
        return value;
    }

    public double parseDouble() throws SyntaxError {
        String str = consumeAsString();
        if (argWasQuoted()) throw new SyntaxError("Expected double instead of quoted string:" + str);
        double value = Double.parseDouble(str);
        return value;
    }

    public List<ValueSource> parseValueSourceList() throws SyntaxError {
        List<ValueSource> sources = new ArrayList<ValueSource>(3);
        while (canConsume()) {
            sources.add(parseValueSource(true));
        }
        return sources;
    }

    public Query parseNestedQuery() throws SyntaxError {
        OriginalInput node = consume();
        QParser parser = subQuery(node.value, null); // use the default parser
        return parser.getQuery();
    }

    public boolean hasMoreArguments() {
        return canConsume();
    }

}
