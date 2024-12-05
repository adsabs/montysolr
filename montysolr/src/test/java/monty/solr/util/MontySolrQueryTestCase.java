package monty.solr.util;

import org.apache.lucene.queryparser.flexible.aqp.AqpTestAbstractCase;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.Query;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QueryParsing;
import org.apache.solr.search.SyntaxError;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MontySolrQueryTestCase extends MontySolrAbstractTestCase {

    protected static AqpTestAbstractCase tp = new AqpTestAbstractCase() {
        @Override
        public void setUp() throws Exception {
            super.setUp();
        }

        @Override
        public void tearDown() throws Exception {
            super.tearDown();
        }
    };

    private int idValue = 0;


    @Override
    public void setUp() throws Exception {
        super.setUp();
        tp.setUp();
    }

    @Override
    public void tearDown() throws Exception {

        tp.tearDown();
        super.tearDown();

    }


    public QParser getParser(SolrQueryRequest req) throws SyntaxError, InstantiationException, IllegalAccessException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
        SolrParams params = req.getParams();
        String query = params.get(CommonParams.Q);
        String defType = params.get(QueryParsing.DEFTYPE);
        QParser qParser = QParser.getParser(query, defType, req);

        // if of type AqpQueryParser - set the debug
        try {
            Class<? extends QParser> clazz = qParser.getClass();
            Method getParser = clazz.getDeclaredMethod("getParser");
            if (getParser != null) {
                Method setDebug = getParser.getReturnType().getDeclaredMethod("setDebug", boolean.class);

                Object[] arglist = new Object[1];
                arglist[0] = tp.debugParser;

                Object parser = getParser.invoke(qParser);
                setDebug.invoke(parser, arglist);
            }
        } catch (Exception e) {
            // pass
        }
        return qParser;

    }

    public static SolrQueryRequest req(String... q) {
        boolean clean = true;
        for (String x : q) {
            if (q.equals("debugQuery")) {
                clean = false;
                break;
            }
        }
        if (clean) {
            String[] nq = new String[q.length + 2];
            int i = 0;
            for (; i < q.length; i++) {
                nq[i] = q[i];
            }
            nq[i++] = "debugQuery";
            nq[i++] = tp.debugParser ? "true" : "false";
            q = nq;
        }
        return SolrTestCaseJ4.req(q);
    }

    public Query assertQueryEquals(SolrQueryRequest req, String expected, Class<?> clazz)
            throws Exception {

        QParser qParser = getParser(req);
        String query = req.getParams().get(CommonParams.Q);
        Query q = qParser.parse();

        String actual = q.toString("field");
        if (BooleanQuery.class.equals(clazz) || DisjunctionMaxQuery.class.equals(clazz)) {
            // TODO: Make a custom toString implementation to canonicalize the order of clauses
            return q;
        } else if (!actual.equals(expected)) {
            tp.debugFail(query, expected, actual);
        }

        if (clazz != null) {
            if (!q.getClass().isAssignableFrom(clazz)) {
                tp.debugFail(actual, expected, "Query is not: " + clazz + " but: " + q.getClass() + q);
            }
        }

        return q;
    }

    public Query assertQueryContains(SolrQueryRequest req, String expected, Class<?> clazz)
            throws Exception {

        QParser qParser = getParser(req);
        String query = req.getParams().get(CommonParams.Q);
        Query q = qParser.parse();

        String actual = q.toString("field");
        if (BooleanQuery.class.equals(clazz) || DisjunctionMaxQuery.class.equals(clazz)) {
            // TODO: Make a custom toString implementation to canonicalize the order of clauses
            return q;
        } else if (!actual.contains(expected)) {
            tp.debugFail(query, expected, actual);
        }

        if (clazz != null) {
            if (!q.getClass().isAssignableFrom(clazz)) {
                tp.debugFail(actual, expected, "Query is not: " + clazz + " but: " + q.getClass() + q);
            }
        }

        return q;
    }

    public Query assertQueryNotContains(SolrQueryRequest req, String expected, Class<?> clazz)
            throws Exception {

        QParser qParser = getParser(req);
        String query = req.getParams().get(CommonParams.Q);
        Query q = qParser.parse();

        String actual = q.toString("field");
        if (BooleanQuery.class.equals(clazz) || DisjunctionMaxQuery.class.equals(clazz)) {
            // TODO: Make a custom toString implementation to canonicalize the order of clauses
            return q;
        } else if (actual.contains(expected)) {
            tp.debugFail(query, expected, actual);
        }

        if (clazz != null) {
            if (!q.getClass().isAssignableFrom(clazz)) {
                tp.debugFail(actual, expected, "Query is not: " + clazz + " but: " + q.getClass() + q);
            }
        }

        return q;
    }

    private boolean isDisjunctionQueryEqual(String generated, String expected) {
        // Remove the outer parentheses and split on the disjunction operator "|"
        String[] generatedClauses = Arrays.stream(generated.substring(1, generated.length() - 1).split("\\|"))
                .map(String::trim).toArray(String[]::new);
        String[] expectedClauses = Arrays.stream(expected.substring(1, expected.length() - 1).split("\\|"))
                .map(String::trim).toArray(String[]::new);

        if (generatedClauses.length != expectedClauses.length) {
            return false;
        }

        Set<String> expectedSet = new HashSet<>(Arrays.asList(expectedClauses));
        return expectedSet.containsAll(Arrays.asList(generatedClauses));
    }

    public void assertQueryParseException(SolrQueryRequest req) throws Exception {
        try {
            getParser(req).parse();
        } catch (SyntaxError expected) {
            return;
        }
        tp.debugFail("ParseException expected, not thrown");
    }

    public void setDebug(boolean v) {
        tp.setDebug(v);
    }

    /*
     * This is only for printing/debugging, DO NOT use this for testing!!!
     *
     * It will only work if the field(s) are indexed with stored positions
     * i.e.
     *
     * <field name="title" ..... termVectors="true" termPositions="true"/>
     *
     * Also, the codec used must NOT be SimpleTextCodec
     */
    public void dumpDoc(Integer docId, String... fields) throws Exception {
        throw new Exception("Disabled");
    }


    public String addDocs(String[] fields, String... values) {
        ArrayList<String> vals = new ArrayList<String>(Arrays.asList(values));
        String[] fieldsVals = new String[fields.length * (values.length * 2)];
        int i = 0;
        for (String f : fields) {
            for (String v : values) {
                fieldsVals[i++] = f;
                fieldsVals[i++] = v;
            }
        }
        return addDocs(fieldsVals);
    }

    public String addDocs(String... fieldsAndValues) {
        ArrayList<String> fVals = new ArrayList<String>(Arrays.asList(fieldsAndValues));
        if (!fVals.contains("id") || fVals.indexOf("id") % 2 == 1) {
            fVals.add("id");
            fVals.add(Integer.toString(incrementId()));
        }
        if (!fVals.contains("bibcode") || fVals.indexOf("bibcode") % 2 == 1) {
            fVals.add("bibcode");
            String bibc = ("AAAAA........" + idValue);
            fVals.add(bibc.substring(bibc.length() - 13));
        }
        String[] newVals = new String[fVals.size()];
        for (int i = 0; i < fVals.size(); i++) {
            newVals[i] = fVals.get(i);
        }
        return adoc(newVals);
    }

    public int incrementId() {
        return idValue++;
    }


    public static String[] formatSynonyms(String[] strings) {
        String[] newLines = new String[strings.length];
        int nl = 0;
        for (String line : strings) {
            StringBuilder out = new StringBuilder();
            String[] kv = line.split("=>");
            for (int i = 0; i < kv.length; i++) {
                if (i > 0) out.append("=>");
                String[] names = kv[i].split(";");
                for (int j = 0; j < names.length; j++) {
                    if (j > 0) out.append(",");
                    out.append(names[j].trim().replace(" ", "\\ ").replace(",", "\\,"));
                }
            }
            newLines[nl++] = out.toString();
        }
        return newLines;
    }

}
