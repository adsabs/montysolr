package org.apache.solr.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.miscellaneous.RemoveDuplicatesTokenFilterFactory;
import org.apache.lucene.analysis.miscellaneous.TrimFilterFactory;
import org.apache.lucene.analysis.pattern.PatternReplaceFilterFactory;
import org.apache.lucene.analysis.util.TokenFilterFactory;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexableField;
import org.apache.solr.analysis.DiagnoseFilterFactory;
import org.apache.solr.common.SolrException;

/**
 * StrField with configurable tokenizer chain.
 * 
 * @author rchyla
 *
 */
public class AqpStrField extends StrField {

  public AqpStrField() {
    Map<String, String> replArgs = new HashMap<String, String>();
    replArgs.put("pattern", "(doi:|arxiv:|\\-)");
    
    List<TokenFilterFactory> filters = new ArrayList<TokenFilterFactory>();
    filters.add(new DiagnoseFilterFactory(new HashMap<String, String>()));
    filters.add(new RemoveDuplicatesTokenFilterFactory(new HashMap<String, String>()));
    filters.add(new TrimFilterFactory(new HashMap<String, String>()));
    filters.add(new PatternReplaceFilterFactory(replArgs));
    filters.add(new LowerCaseFilterFactory(new HashMap<String, String>()));
    filters.add(new ASCIIFoldingFilterFactory(new HashMap<String, String>()));
    
    TokenFilterFactory[] filterArr = new TokenFilterFactory[filters.size()];
    filters.toArray(filterArr);
    
  }
  
  
  @Override
  public boolean isTokenized() {
    return true;
  }
  
  protected boolean supportsAnalyzers() {
    return true;
  }
  /**
  @Override
  public Analyzer getIndexAnalyzer() {
    return indexAnalyzer;
  }

  @Override
  public Analyzer getQueryAnalyzer() {
    return queryAnalyzer;
  }
  
  @Override
  public String toInternal(String val) {
    // - used in delete when a Term needs to be created.
    // - used by the default getTokenizer() and createField()
    return val;
  }
  **/
  
  @Override
  public IndexableField createField(SchemaField field, Object value) {
    if (!field.indexed() && !field.stored()) {
      return null;
    }
    
    String val;
    try {
      val = toInternal(value.toString());
    } catch (RuntimeException e) {
      throw new SolrException( SolrException.ErrorCode.SERVER_ERROR, "Error while creating field '" + field + "' from value '" + value + "'", e);
    }
    if (val==null) return null;

    // TODO:rca - verify the field gets indexed properly
    return createField(field.getName(), val, field);
  }
  
  /**
  @Override
  public List<IndexableField> createFields(SchemaField field, Object value, float boost) {
    //if (!field.multiValued())
    //  return super.createFields(field, value, boost);
    
    List<IndexableField> output = new ArrayList<IndexableField>();
    for (String v: value.toString().split("\\s")) {
      IndexableField fval = createField(field, v, boost);
  
      if (field.hasDocValues()) {
        IndexableField docval;
        final BytesRef bytes = new BytesRef(v.toString());
        if (field.multiValued()) {
          docval = new SortedSetDocValuesField(field.getName(), bytes);
        } else {
          docval = new SortedDocValuesField(field.getName(), bytes);
        }
  
        // Only create a list of we have 2 values...
        if (fval != null) {
          List<IndexableField> fields = new ArrayList<>(2);
          fields.add(fval);
          fields.add(docval);
          return fields;
        }
  
        fval = docval;
      }
      output.add(fval);
    }
    return output;
  }
  **/
}

