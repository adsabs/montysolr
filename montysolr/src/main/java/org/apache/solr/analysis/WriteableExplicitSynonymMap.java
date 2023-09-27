package org.apache.solr.analysis;

import org.apache.solr.common.util.StrUtils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class WriteableExplicitSynonymMap extends WriteableSynonymMap {


    @Override
    public void add(String key, Set<String> values) {
        Set<String> masterSet;
        if (containsKey(key)) {
            masterSet = get(key);
        } else {
            masterSet = new LinkedHashSet<String>();
            put(key, masterSet);
        }
        masterSet.addAll(values);
    }

    /*
     * this is much simplified version of synonym rules that
     * supports:
     *
     * token=>token,token\\ tokenb,token
     */
    @Override
    public void populateMap(List<String> rules) {
        for (String rule : rules) {
            List<String> mapping = StrUtils.splitSmart(rule, "=>", false);
            if (mapping.size() != 2) {
                log.error("Invalid Synonym Rule:" + rule);
                continue;
            }
            String key = mapping.get(0).trim().replace("\\,", ",").replace("\\ ", " ");
            Set<String> values = splitValues(mapping.get(1));
            add(key, values);
        }
    }

    @Override
    public String formatEntry(String key, Set<String> values) {
        StringBuffer out = new StringBuffer();
        out.append(key.replace(",", "\\,").replace(" ", "\\ "));
        out.append("=>");
        boolean notFirst = false;
        for (String s : values) {
            if (notFirst) out.append(",");
            out.append(s.replace(",", "\\,").replace(" ", "\\ "));
            notFirst = true;
        }
        out.append("\n");
        return out.toString();
    }

}
