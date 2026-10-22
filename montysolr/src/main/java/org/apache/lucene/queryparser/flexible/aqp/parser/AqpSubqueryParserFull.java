package org.apache.lucene.queryparser.flexible.aqp.parser;

import org.apache.lucene.search.*;
import org.apache.solr.search.QParser;
import org.apache.solr.search.SyntaxError;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


public class AqpSubqueryParserFull extends AqpSubqueryParser {

    private QParser parser = null;
    private Class<?>[] queryTypes = null;
    private String originalInput = null;
    private Object reParseContext = null;
    private final ReentrantLock parsingLock = new ReentrantLock();

    public QParser getParser() {
        return parser;
    }

    protected String getOriginalInput() {
        return originalInput;
    }

    protected Object getReParseContext() {
        return reParseContext;
    }

    public Class[] getQtypes() {
        return queryTypes;
    }

    /*
     * Purpose of this method is to remove the Boolean top clause (+)
     * so that results of the query can be easier combined with
     * other clauses, otherwise foo OR bar would be parsed as
     * title:foo (+title:bar)
     */
    public Query simplify(Query query) {
        if (query instanceof BooleanQuery) {
            List<BooleanClause> clauses = ((BooleanQuery) query).clauses();
            if (clauses.size() == 1) {
                Query q = clauses.get(0).getQuery();

                if (q instanceof BoostQuery && ((BoostQuery) q).getBoost() != 1.0)
                    return q;

                if (q.toString().isEmpty()) return null;
                if (q instanceof DisjunctionMaxQuery && ((DisjunctionMaxQuery) q).getDisjuncts().size() == 1) {
                    return ((DisjunctionMaxQuery) q).getDisjuncts().stream().findFirst().get();
                }
                return q;
            }
        }
        return query;
    }

    public Query reParse(Query query, QParser qp, Class<?>... types) throws SyntaxError {
        return reParse(query, qp, null, types);
    }

    protected Query reParse(Query query, QParser qp, Object context, Class<?>... types)
            throws SyntaxError {
        parsingLock.lock();
        try {
            parser = qp;
            queryTypes = types;
            // Traversal mutates the parser string; retain the input for field-specific reparsing.
            originalInput = qp.getString();
            reParseContext = context;
            return swimDeep(query);
        } finally {
            parser = null;
            queryTypes = null;
            originalInput = null;
            reParseContext = null;
            parsingLock.unlock();
        }
    }

    protected boolean isWanted(Query query) {
        for (Class<?> type : queryTypes) {
            if (type.isInstance(query)) {
                return true;
            }
        }
        return false;
    }

    protected Query swimDeep(TermQuery query) throws SyntaxError {
        if (parser != null && queryTypes != null && isWanted(query)) {
            parser.setString(query.toString());
            return parser.parse();
        }
        return query;
    }

    protected Query swimDeep(DisjunctionMaxQuery query) throws SyntaxError {
        List<Query> reparsedDisjuncts = new ArrayList<>(query.getDisjuncts().size());
        for (Query disjunct : query.getDisjuncts()) {
            reparsedDisjuncts.add(swimDeep(disjunct));
        }
        return new DisjunctionMaxQuery(reparsedDisjuncts, query.getTieBreakerMultiplier());
    }

    protected Query swimDeep(BooleanQuery query) throws SyntaxError {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        for (BooleanClause clause : query.clauses()) {
            builder.add(swimDeep(clause.getQuery()), clause.getOccur());
        }
        builder.setMinimumNumberShouldMatch(query.getMinimumNumberShouldMatch());
        return builder.build();
    }

    protected Query swimDeep(BoostQuery query) throws SyntaxError {
        return new BoostQuery(swimDeep(query.getQuery()), query.getBoost());
    }

    protected Query swimDeep(Query query) throws SyntaxError {
        if (query instanceof BooleanQuery) {
            return swimDeep((BooleanQuery) query);
        } else if (query instanceof BoostQuery) {
            return swimDeep((BoostQuery) query);
        } else if (query instanceof DisjunctionMaxQuery) {
            return swimDeep((DisjunctionMaxQuery) query);
        } else if (query instanceof TermQuery) {
            return swimDeep((TermQuery) query);
        }
        return query;
    }

}
