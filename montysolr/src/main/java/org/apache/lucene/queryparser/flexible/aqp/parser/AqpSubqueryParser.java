package org.apache.lucene.queryparser.flexible.aqp.parser;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.lucene.queryparser.flexible.core.config.QueryConfigHandler;
import org.apache.lucene.search.Query;
import org.apache.solr.search.FunctionQParser;
import org.apache.solr.search.SyntaxError;

public class AqpSubqueryParser {


    public Query parse(FunctionQParser fp, QueryConfigHandler config) throws SyntaxError {
        return this.parse(fp);
    }

    public Query parse(FunctionQParser fp) throws SyntaxError {
        throw new NotImplementedException();
    }


}
