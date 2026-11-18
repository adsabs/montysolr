package org.apache.lucene.queryparser.flexible.aqp.processors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.queryparser.flexible.aqp.config.AqpAdsabsQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.aqp.nodes.AqpAdsabsRegexQueryNode;
import org.apache.lucene.queryparser.flexible.aqp.parser.AqpStandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.queryparser.flexible.core.nodes.*;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.ConfigurationKeys;
import org.apache.lucene.queryparser.flexible.standard.nodes.PrefixWildcardQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.RegexpQueryNode;
import org.apache.lucene.queryparser.flexible.standard.nodes.WildcardQueryNode;
import org.apache.lucene.search.TopTermsRewrite;
import org.apache.solr.analysis.author.AuthorCreateQueryVariationsFilter;
import org.apache.solr.analysis.author.AuthorNormalizeFilter;
import org.apache.solr.analysis.author.AuthorUtils;
import org.apache.solr.analysis.author.PythonicAuthorNormalizerFilter;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Looks at the QueryNode(s) and if they are author searches,
 * it adds to them some ADS specific post-analysis logic
 *
 * @see AqpFieldMapperProcessor
 * @see QueryConfigHandler
 */
public class AqpAdsabsExpandAuthorSearchProcessor extends AqpQueryNodeProcessorImpl {

    private Map<String, int[]> fields;

    public AqpAdsabsExpandAuthorSearchProcessor() {
        // empty constructor
    }

    @Override
    public QueryNode process(QueryNode queryTree) throws QueryNodeException {
        if (getQueryConfigHandler().has(AqpAdsabsQueryConfigHandler.ConfigurationKeys.AUTHOR_FIELDS)) {
            fields = getQueryConfigHandler().get(AqpAdsabsQueryConfigHandler.ConfigurationKeys.AUTHOR_FIELDS);
            return super.process(queryTree);
        }
        return queryTree;
    }

    @Override
    protected QueryNode preProcessNode(QueryNode node)
            throws QueryNodeException {
        return node;
    }

    @Override
    protected QueryNode postProcessNode(QueryNode node)
            throws QueryNodeException {

        if (node.getTag(AqpAdsabsAnalyzerProcessor.ORIGINAL_VALUE) != null) {
            String origValue = (String) node.getTag(AqpAdsabsAnalyzerProcessor.ORIGINAL_VALUE);

            for (String normalized : normalizeAuthorName(origValue)) {
                NameInfo nameInfo = new NameInfo(normalized);
                int[] visitedTextNodes = new int[]{0};
                node = expandNodes(node, nameInfo, visitedTextNodes);
            }

        }
        return node;
    }

    @Override
    protected List<QueryNode> setChildrenOrder(List<QueryNode> children)
            throws QueryNodeException {
        return children;
    }

    private QueryNode expandNodes(QueryNode node, NameInfo origNameInfo, int[] visitedTextNodes) throws QueryNodeException {

        ArrayList<QueryNode> collector = new ArrayList<QueryNode>();

        if (!node.isLeaf()) {
            List<QueryNode> children = node.getChildren();
            boolean changed = false;
            for (int i = 0; i < children.size(); i++) {
                doExpansion(origNameInfo, children.get(i), collector, visitedTextNodes);

                // Insert expansions immediately after their source, then skip them.

                if (collector.size() > 0) {
                    changed = true;
                    children.addAll(i + 1, collector);
                    i += collector.size();
                    collector.clear();
                }
            }

            if (changed)
                node.set(children);

        } else {
            // now expand the parent
            doExpansion(origNameInfo, node, collector, visitedTextNodes);
        }


        if (collector.size() > 0) {
            collector.add(0, node);
            return new GroupQueryNode(new BooleanQueryNode(collector));
        }

        return node;
    }

    private void doExpansion(NameInfo origNameInfo, QueryNode node, List<QueryNode> parentChildren, int[] visitedTextNodes)
            throws QueryNodeException {

        if (node instanceof TextableQueryNode) {

            visitedTextNodes[0] = visitedTextNodes[0] + 1; // Expand synonyms only at the first text node.

            if (node instanceof FuzzyQueryNode || node instanceof RegexpQueryNode
                    || node instanceof WildcardQueryNode) {
                return;
            }

            FieldQueryNode fqn = ((FieldQueryNode) node);
            if (fields.containsKey(fqn.getFieldAsString())) {

                // Keep the full analyzed author literal, but do not add optional
                // synonym/prefix/regex clauses for names beyond the existing
                // six-part author-variation budget. Long collaborations and
                // citation-style author strings must not exhaust Boolean clauses.
                if (origNameInfo.noOfParts > AuthorCreateQueryVariationsFilter.MAX_NAME_PARTS) {
                    return;
                }

                if (visitedTextNodes[0] == 1 && !isLongForm(origNameInfo.origName)) {
                    try {
                        String[] synonyms = getSynonyms(origNameInfo.origName);
                        if (synonyms != null) {
                            for (String syn : synonyms) {
                                parentChildren.add(new FieldQueryNode(fqn.getField(), syn, fqn.getBegin(), fqn.getEnd()));
                            }
                        }
                    } catch (IOException e) {
                        throw new QueryNodeException(new MessageImpl("Wonky, wonky, bong, bong - synonym expansion failed..." + e.getMessage()));
                    }
                }

                String v = fqn.getTextAsString();
                String[] nameParts = AuthorUtils.splitName(fqn.getTextAsString());

                if (nameParts.length == 1) { // the new name is just surname

                    if (nameParts.length < origNameInfo.noOfParts) return; // do nothing

                    if (origNameInfo.containsOnlySurname) { // orig was lone surname
                        parentChildren.add(new PrefixWildcardQueryNode(fqn.getField(), v + "*", fqn.getBegin(), fqn.getEnd()));
                    } else {
                        // do nothing
                    }
                } else { // new name has several parts
                    if (nameParts.length < origNameInfo.noOfParts) return; // do nothing

                    if (origNameInfo.containsOnlySurname) { // orig was lone surname
                        // we could extract the surname and search for "surname, *" but i have decided against it
                        // the surname probably comes from the synonym expansion and if it was there, it can contain initials
                        parentChildren.add(new PrefixWildcardQueryNode(fqn.getField(), v + " *", fqn.getBegin(), fqn.getEnd()));
                    } else {
                        if (origNameInfo.lastPartWasAcronym) { // orig name had only initial at the end
                            if (nameParts[nameParts.length - 1].length() == 1) { // allow broader search only if the expanded form also has initial
                                parentChildren.add(new PrefixWildcardQueryNode(fqn.getField(), v + "*", fqn.getBegin(), fqn.getEnd()));
                            } else {
                                parentChildren.add(new PrefixWildcardQueryNode(fqn.getField(), v + " *", fqn.getBegin(), fqn.getEnd()));
                            }
                        } else {
                            if (isTwoLetterGivenNameQuery(origNameInfo, nameParts)) {
                                // The broader prefix covers the space prefix unless a term limit
                                // can discard its matches. Avoid executing both for complete rewrites.
                                if (hasBoundedPrefixRewrite(fqn.getFieldAsString())) {
                                    parentChildren.add(new PrefixWildcardQueryNode(fqn.getField(), v + " *", fqn.getBegin(), fqn.getEnd()));
                                }
                                parentChildren.add(new PrefixWildcardQueryNode(fqn.getField(), v + "*", fqn.getBegin(), fqn.getEnd()));
                            } else {
                                parentChildren.add(new PrefixWildcardQueryNode(fqn.getField(), v + " *", fqn.getBegin(), fqn.getEnd()));
                            }
                        }
                    }

                    // special regular expression cases, only happens if the new name and the original
                    // have initial somewhere in the middle (and both at the same position)
                    if (hasAlignedMiddleInitial(nameParts, origNameInfo.parts)) {
                        StringBuffer nn = new StringBuffer();
                        nn.append(nameParts[0]);
                        for (int i = 1; i < nameParts.length - 1 && i < origNameInfo.parts.length; i++) {
                            if (nameParts[i].length() == 1 && origNameInfo.parts[i].length() == 1) {
                                nn.append(" " + nameParts[i] + "[^ ]*");
                                parentChildren.add(new AqpAdsabsRegexQueryNode(fqn.getField(), nn.toString(), fqn.getBegin(), fqn.getEnd()));
                            } else {
                                nn.append(" " + nameParts[i]);
                            }
                        }

                        nn.append(" " + nameParts[nameParts.length - 1]);

                        if (nameParts.length - 1 < origNameInfo.parts.length && nameParts[nameParts.length - 1].length() == 1 && origNameInfo.parts[nameParts.length - 1].length() == 1) {
                            parentChildren.add(new AqpAdsabsRegexQueryNode(fqn.getField(), nn + ".*", fqn.getBegin(), fqn.getEnd()));
                        } else {
                            parentChildren.add(new AqpAdsabsRegexQueryNode(fqn.getField(), nn.toString(), fqn.getBegin(), fqn.getEnd()));
                            parentChildren.add(new AqpAdsabsRegexQueryNode(fqn.getField(), nn + " .*", fqn.getBegin(), fqn.getEnd()));
                        }

                    }

                }

                return;
            }
        }

        if (!node.isLeaf()) expandNodes(node, origNameInfo, visitedTextNodes);
    }

    private boolean hasBoundedPrefixRewrite(String field) {
        Map<String, String> fieldMap = getQueryConfigHandler().get(
                AqpStandardQueryConfigHandler.ConfigurationKeys.FIELD_MAPPER_POST_ANALYSIS);
        if (fieldMap != null && fieldMap.containsKey(field)) {
            field = fieldMap.get(field);
        }

        String rewriteMethod = getConfigVal("aqp.qprefix.scoring." + field);
        if (rewriteMethod != null && !rewriteMethod.isEmpty()) {
            return "topterms".equals(rewriteMethod)
                    || "topterms_blended".equals(rewriteMethod)
                    || "topterms_boosted".equals(rewriteMethod);
        }

        return getQueryConfigHandler().get(ConfigurationKeys.MULTI_TERM_REWRITE_METHOD)
                instanceof TopTermsRewrite<?>;
    }

    private boolean isTwoLetterGivenNameQuery(NameInfo origNameInfo, String[] nameParts) {
        if (origNameInfo.parts.length != 2 || nameParts.length != 2) {
            return false;
        }

        String originalGivenName = origNameInfo.parts[1];
        String givenName = nameParts[1];
        return isTwoLetterName(originalGivenName) && isTwoLetterName(givenName);
    }

    private boolean isTwoLetterName(String name) {
        return name.length() == 2
                && Character.isLetter(name.charAt(0))
                && Character.isLetter(name.charAt(1));
    }

    private boolean hasAlignedMiddleInitial(String[] expandedParts, String[] originalParts) {
        for (int i = 1; i < expandedParts.length - 1 && i < originalParts.length; i++) {
            if (expandedParts[i].length() == 1 && originalParts[i].length() == 1 && i + 1 < originalParts.length) {
                return true;
            }
        }
        return false;
    }

    private String[] getSynonyms(String origInput) throws IOException {
        Analyzer analyzer = getQueryConfigHandler().get(ConfigurationKeys.ANALYZER);
        TokenStream source = null;
        try {
            source = analyzer.tokenStream("author_short_name_rage", new StringReader(origInput));
            source.reset();

            CharTermAttribute termAtt = source.getAttribute(CharTermAttribute.class);

            List<String> synonyms = new ArrayList<String>();
            while (source.incrementToken()) {
                synonyms.add(termAtt.toString());
            }

            if (synonyms.size() < 2) { // the first one is the original
                return null;
            }
            synonyms.remove(0);

            return synonyms.toArray(new String[synonyms.size()]);

        } finally {
            if (source != null)
                source.close();
        }

    }

    private boolean isLongForm(String name) {
        String[] parts = name.split(" ");
        int longParts = 0;
        for (int i = 1; i < parts.length; i++) {
            if (parts[i].length() > 1)
                longParts++;
        }
        return parts.length == longParts;
    }

    /*
     * This is a part of the first part of the author chain tokenizer;
     * but it is very important, because without it, the search
     * may be *slightly* different. So, whenever you update the tokenizer
     * chain, you should always review also this method
     */

    Analyzer authorNameAnalyzer = new Analyzer() {
        @Override
        public TokenStreamComponents createComponents(String fieldName) {
            Tokenizer source = new KeywordTokenizer();
            TokenStream filter = new PythonicAuthorNormalizerFilter(source);
            filter = new AuthorNormalizeFilter(filter);
            return new TokenStreamComponents(source, filter);
        }
    };

    private List<String> normalizeAuthorName(String input) throws QueryNodeException {

        try {
            TokenStream ts = authorNameAnalyzer.tokenStream("foo", input);
            ts.reset();
            List<String> out = new ArrayList<String>();
            CharTermAttribute termAtt;
            while (ts.incrementToken()) {
                termAtt = ts.getAttribute(CharTermAttribute.class);
                out.add(termAtt.toString());
            }
            ts.close();
            return out;
        } catch (IOException e) {
            throw new QueryNodeException(new MessageImpl("Error parsing: " + input, e));
        }


    }

    class NameInfo {
        public String origName;
        public boolean lastPartWasAcronym;
        public int noOfParts;
        public String[] parts;
        public boolean containsOnlySurname = false;

        public NameInfo(String name) {
            // lone surnames get always expanded
            if (name.endsWith(",") || !name.contains(",")) containsOnlySurname = true;

            // whether to add a space, ie. Kurtz, Michael J -> Kurtz, Michael J*
            // but Kurtz, Michael Julian -> Kurtz, Michael Julian *
            parts = AuthorUtils.splitName(name);
            lastPartWasAcronym = parts[parts.length - 1].length() == 1;

            noOfParts = parts.length;
            origName = name;
        }
    }

    static final class ReusableStringReader extends Reader {
        private int pos = 0, size = 0;
        private String s = null;

        void setValue(String s) {
            this.s = s;
            this.size = s.length();
            this.pos = 0;
        }

        @Override
        public int read() {
            if (pos < size) {
                return s.charAt(pos++);
            } else {
                s = null;
                return -1;
            }
        }

        @Override
        public int read(char[] c, int off, int len) {
            if (pos < size) {
                len = Math.min(len, size - pos);
                s.getChars(pos, pos + len, c, off);
                pos += len;
                return len;
            } else {
                s = null;
                return -1;
            }
        }

        @Override
        public void close() {
            pos = size; // this prevents NPE when reading after close!
            s = null;
        }

        @Override
        public void reset() {
            this.size = s.length();
            this.pos = 0;
        }
    }

}
