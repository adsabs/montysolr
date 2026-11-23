package org.apache.lucene.queryparser.flexible.aqp.builders;

import org.apache.lucene.queries.spans.SpanQuery;
import org.apache.lucene.queryparser.flexible.aqp.AqpAdsabsQueryTreeBuilder;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsIdentifierNode;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpNonAnalyzedQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpSubqueryParserFull;
import org.apache.lucene.queryparser.flexible.aqp.processors.AqpPointRangeQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldQueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldableNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNodeImpl;
import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;
import org.apache.lucene.queryparser.flexible.core.processors.QueryNodeProcessorPipeline;
import org.apache.lucene.queryparser.flexible.standard.builders.PointRangeQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardQueryBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.TermRangeQueryNodeBuilder;
import org.apache.lucene.queryparser.flexible.standard.config.PointsConfig;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.ConfigurationKeys;
import org.apache.lucene.queryparser.flexible.standard.nodes.PointRangeQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.TermRangeQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.processors.OpenRangeQueryNodeProcessor;
import org.apache.lucene.queryparser.flexible.standard.processors.PointQueryNodeProcessor;
import org.apache.lucene.search.*;
import org.apache.solr.common.SolrException;
import org.apache.solr.query.SolrRangeQuery;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.NumberType;
import org.apache.solr.schema.PreAnalyzedField;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.AlignedFieldQuery;
import org.apache.solr.search.AqpAdsabsQParser;
import org.apache.solr.search.FunctionQParser;
import org.apache.solr.search.QParser;
import org.apache.solr.search.SyntaxError;

import java.text.NumberFormat;
import java.time.Instant;
import java.util.Locale;

/** Parses the two slot-local predicates and the optional stored-ordinal distance. */
final class SameFieldSubQueryParser extends AqpSubqueryParserFull {
    @Override
    public Query parse(FunctionQParser fp) throws SyntaxError {
        IndexSchema schema = fp.getReq().getSchema();
        Query left = parseMembershipQuery(fp, schema);
        Query right = parseMembershipQuery(fp, schema);
        int slop = fp.hasMoreArguments() ? fp.parseInt() : 0;
        if (fp.hasMoreArguments()) {
            throw new SyntaxError("same() expects two field queries and optional slop");
        }
        if (slop < 0) {
            throw new SyntaxError("same() slop must be nonnegative");
        }
        return new AlignedFieldQuery(left, right, getSlotField(schema, left), getSlotField(schema, right), slop);
    }

    private static Query parseMembershipQuery(FunctionQParser fp, IndexSchema schema) throws SyntaxError {
        QParser subParser = fp.subQuery(fp.parseId(), "aqp");
        if (!(subParser instanceof AqpAdsabsQParser aqp)) {
            return subParser.getQuery();
        }
        configureSchemaQueries(aqp, schema);
        try {
            // Request-level scoring must remain outside the slot predicate.
            return aqp.getParser().parse(aqp.getString(), null);
        } catch (QueryNodeException e) {
            throw new SyntaxError(e);
        }
    }

    private static void configureSchemaQueries(AqpAdsabsQParser aqp, IndexSchema schema) {
        if (!(aqp.getParser().getQueryBuilder() instanceof AqpAdsabsQueryTreeBuilder tree)) {
            return;
        }
        if (aqp.getParser().getQueryNodeProcessor() instanceof QueryNodeProcessorPipeline pipeline) {
            for (int i = 0; i < pipeline.size(); i++) {
                if (pipeline.get(i) instanceof PointQueryNodeProcessor) {
                    pipeline.set(i, new SlotPointQueryProcessor());
                } else if (pipeline.get(i) instanceof AqpPointRangeQueryNodeProcessor) {
                    pipeline.add(i, new OpenRangeQueryNodeProcessor());
                    break;
                }
            }
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.ROOT);
        aqp.getParser().getQueryConfigHandler().addFieldConfigListener(config -> {
            SchemaField field = schema.getFieldOrNull(config.getField());
            if (field == null) {
                return;
            }
            if (field.getType() instanceof PreAnalyzedField) {
                throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, preAnalyzedMessage(field));
            }
            Class<? extends Number> width = switch (field.getType().getNumberType()) {
                case INTEGER -> Integer.class;
                case LONG -> Long.class;
                case FLOAT -> Float.class;
                case DOUBLE -> Double.class;
                case null, default -> null;
            };
            if (width != null && field.getType().isPointField()) {
                config.set(ConfigurationKeys.POINTS_CONFIG, new PointsConfig(numberFormat, width));
            }
        });

        var termBuilder = new AqpFieldQueryNodeBuilder();
        var identifierBuilder = new AqpAdsabsIdentifierNodeBuilder();
        StandardQueryBuilder schemaTermBuilder = node -> {
            Query nativeQuery = nativeTerm(aqp, schema, (FieldQueryNode) node);
            return nativeQuery != null ? nativeQuery : termBuilder.build(node);
        };
        tree.setBuilder(FieldQueryNode.class, schemaTermBuilder);
        tree.setBuilder(AqpNonAnalyzedQueryNode.class, schemaTermBuilder);
        tree.setBuilder(AqpAdsabsIdentifierNode.class, node -> {
            Query nativeQuery = nativeTerm(aqp, schema, (FieldQueryNode) node);
            return nativeQuery != null ? nativeQuery : identifierBuilder.build(node);
        });

        tree.setBuilder(SlotExistsNode.class, node -> {
            SchemaField field = schema.getField(((SlotExistsNode) node).getField().toString());
            return field.getType().getExistenceQuery(aqp, field);
        });

        var termRangeBuilder = new TermRangeQueryNodeBuilder();
        tree.setBuilder(TermRangeQueryNode.class, node -> {
            var range = (TermRangeQueryNode) node;
            SchemaField field = schema.getFieldOrNull(range.getField().toString());
            if (usesNativeTerms(field)) {
                return field.getType().getRangeQuery(aqp, field,
                        openBound(range.getLowerBound()), openBound(range.getUpperBound()),
                        range.isLowerInclusive(), range.isUpperInclusive());
            }
            return termRangeBuilder.build(node);
        });

        var pointBuilder = new PointRangeQueryNodeBuilder();
        tree.setBuilder(PointRangeQueryNode.class, node -> {
            var range = (PointRangeQueryNode) node;
            SchemaField field = schema.getFieldOrNull(range.getField().toString());
            if (field == null || !field.getType().isPointField()) {
                return pointBuilder.build(node);
            }
            NumberType type = field.getType().getNumberType();
            return field.getType().getRangeQuery(aqp, field,
                    externalBound(type, range.getLowerBound().getValue()),
                    externalBound(type, range.getUpperBound().getValue()),
                    range.isLowerInclusive(), range.isUpperInclusive());
        });
    }

    private static String openBound(FieldQueryNode bound) {
        String text = bound.getTextAsString();
        return text.isEmpty() ? null : text;
    }

    private static Query nativeTerm(QParser parser, IndexSchema schema, FieldQueryNode node) {
        if (node.getParent() instanceof TermRangeQueryNode) {
            return null;
        }
        SchemaField field = schema.getFieldOrNull(node.getFieldAsString());
        return usesNativeTerms(field) ? field.getType().getFieldQuery(parser, field, node.getTextAsString()) : null;
    }

    private static boolean usesNativeTerms(SchemaField field) {
        return field != null && (field.getType().isPointField() || (!field.indexed() && field.hasDocValues()));
    }

    private static String externalBound(NumberType type, Number value) {
        if (value == null) {
            return null;
        }
        return type == NumberType.DATE ? Instant.ofEpochMilli(value.longValue()).toString() : value.toString();
    }

    private static SchemaField getSlotField(IndexSchema schema, Query query) throws SyntaxError {
        String name = prepareFieldPredicate(schema, query);
        if (name == null) {
            throw new SyntaxError("same() requires a physical field in each clause");
        }
        SchemaField field = schema.getFieldOrNull(name);
        if (field == null || !field.stored() || !field.multiValued()) {
            throw new SyntaxError("same() requires stored multi-valued fields: " + name);
        }
        if (!field.indexed() && !field.hasDocValues()) {
            throw new SyntaxError("same() requires searchable fields: " + name);
        }
        if (field.getType() instanceof PreAnalyzedField) {
            throw new SyntaxError(preAnalyzedMessage(field));
        }
        return field;
    }

    private static String preAnalyzedMessage(SchemaField field) {
        return "same() cannot reconstruct pre-analyzed field values: " + field.getName();
    }

    private static String prepareFieldPredicate(IndexSchema schema, Query query) throws SyntaxError {
        switch (query) {
            case MatchAllDocsQuery ignored -> { return null; }
            case MatchNoDocsQuery ignored -> { return null; }
            case SpanQuery span -> { return span.getField(); }
            case SolrRangeQuery range -> { return range.getField(); }
            case TermQuery term -> { return term.getTerm().field(); }
            case MultiTermQuery multiTerm -> { return multiTermField(schema, multiTerm); }
            case FieldExistsQuery exists -> { return exists.getField(); }
            case PhraseQuery phrase -> { return phrase.getField(); }
            case IndexOrDocValuesQuery dual -> {
                return mergeFields(prepareFieldPredicate(schema, dual.getIndexQuery()),
                        prepareFieldPredicate(schema, dual.getRandomAccessQuery()));
            }
            case BoostQuery boost -> { return prepareFieldPredicate(schema, boost.getQuery()); }
            case ConstantScoreQuery constant -> { return prepareFieldPredicate(schema, constant.getQuery()); }
            case BooleanQuery bool -> {
                String field = null;
                for (BooleanClause clause : bool) {
                    field = mergeFields(field, prepareFieldPredicate(schema, clause.getQuery()));
                }
                return field;
            }
            case DisjunctionMaxQuery disjunction -> {
                String field = null;
                for (Query disjunct : disjunction) {
                    field = mergeFields(field, prepareFieldPredicate(schema, disjunct));
                }
                return field;
            }
            default -> { return getLeafField(query); }
        }
    }

    @SuppressWarnings("deprecation")
    private static String multiTermField(IndexSchema schema, MultiTermQuery query) {
        String name = query.getField();
        SchemaField field = schema.getFieldOrNull(name);
        if (field != null && !field.indexed() && field.hasDocValues()) {
            // The operand query is still private to this parse. Use the same
            // doc-values rewrite for document candidates and stored slots.
            query.setRewriteMethod(MultiTermQuery.DOC_VALUES_REWRITE);
        }
        return name;
    }

    private static String getLeafField(Query query) throws SyntaxError {
        // Some Lucene field predicates (e.g. doc-values ranges) have no public class.
        // Require the query to report its own field; operators like topn() only visit their seed.
        class FieldVisitor extends QueryVisitor {
            String field;
            boolean seenQuery;
            boolean multiField;

            @Override
            public boolean acceptField(String name) {
                multiField |= field != null && !field.equals(name);
                field = name;
                return true;
            }

            @Override
            public void visitLeaf(Query leaf) {
                seenQuery |= leaf == query;
            }

            @Override
            public void consumeTerms(Query leaf, org.apache.lucene.index.Term... terms) {
                seenQuery |= leaf == query;
            }

            @Override
            public QueryVisitor getSubVisitor(BooleanClause.Occur occur, Query parent) {
                return parent == query && (query instanceof MultiPhraseQuery || query instanceof SynonymQuery)
                        ? this : EMPTY_VISITOR;
            }
        }
        FieldVisitor visitor = new FieldVisitor();
        query.visit(visitor);
        if (!visitor.seenQuery || visitor.field == null || visitor.multiField) {
            throw new SyntaxError("same() requires single-field predicates");
        }
        return visitor.field;
    }

    private static String mergeFields(String first, String second) throws SyntaxError {
        if (first != null && second != null && !first.equals(second)) {
            throw new SyntaxError("same() does not support virtual or multi-field queries");
        }
        return first != null ? first : second;
    }

    /** A bare field wildcard is a presence test, not a numeric literal. */
    private static final class SlotPointQueryProcessor extends PointQueryNodeProcessor {
        @Override
        protected QueryNode postProcessNode(QueryNode node) throws QueryNodeException {
            if (node instanceof WildcardQueryNode wildcard && "*".equals(wildcard.getTextAsString())) {
                return new SlotExistsNode(wildcard.getField());
            }
            return super.postProcessNode(node);
        }
    }

    private static final class SlotExistsNode extends QueryNodeImpl implements FieldableNode {
        private CharSequence field;

        private SlotExistsNode(CharSequence field) {
            this.field = field;
            setLeaf(true);
        }

        @Override
        public CharSequence getField() {
            return field;
        }

        @Override
        public void setField(CharSequence field) {
            this.field = field;
        }

        @Override
        public CharSequence toQueryString(EscapeQuerySyntax escapeSyntax) {
            return field + ":*";
        }
    }
}
