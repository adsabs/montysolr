package org.apache.solr.highlight;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.uhighlight.UnifiedHighlighter;
import org.apache.solr.common.params.HighlightParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.DocList;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class MaxLengthHighlighter extends UnifiedSolrHighlighter {

    public final String MAX_HIGHLIGHT_CHARACTERS = "hl.maxHighlightCharacters";

    @Override
    public NamedList<Object> doHighlighting(DocList docs, Query query, SolrQueryRequest req, String[] defaultFields) throws IOException {
        final SolrParams params = req.getParams();

        // if highlighting isn't enabled, then why call doHighlighting?
        if (!isHighlightingEnabled(params)) return null;

        int[] docIDs = toDocIDs(docs);

        // fetch the unique keys
        String[] keys = getUniqueKeys(req.getSearcher(), docIDs);

        // query-time parameters
        String[] fieldNames = getHighlightFields(query, req, defaultFields);

        int maxPassages[] = new int[fieldNames.length];
        for (int i = 0; i < fieldNames.length; i++) {
            maxPassages[i] = params.getFieldInt(fieldNames[i], HighlightParams.SNIPPETS, 1);
        }

        UnifiedHighlighter highlighter = getHighlighter(req);
        Map<String, String[]> snippets =
                fieldNames.length == 0
                        ? Collections.emptyMap()
                        : highlighter.highlightFields(fieldNames, query, docIDs, maxPassages);

        int maxLength = params.getInt(MAX_HIGHLIGHT_CHARACTERS, -1);
        if (maxLength >= 0) {
            for (String key : snippets.keySet()) {
                String[] highlights = snippets.get(key);
                if (highlights == null) continue;

                for (int index = 0; index < highlights.length; index++) {
                    if (highlights[index] != null) {
                        if (highlights[index].length() > maxLength) {
                            highlights[index] = null;
                        }
                    }
                }
            }
        }

        return encodeSnippets(keys, fieldNames, snippets);
    }
}
