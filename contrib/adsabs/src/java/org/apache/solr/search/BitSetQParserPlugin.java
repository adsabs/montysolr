package org.apache.solr.search;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BitSetQuery;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.Query;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.Base64;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.SchemaField;

public class BitSetQParserPlugin extends QParserPlugin {
	
	public static String NAME = "bitset";
	private String defaultField = null;
	private List<String> allowedTypes = Arrays.asList("int", "tint");
	
	@Override
	public void init(NamedList args) {

		NamedList defs = (NamedList) args.get("defaults");
		if (defs == null) {
			defs = new NamedList();
		}
		
		if (defs.get("defaultField") != null) {
			defaultField = (String) args.get("defaultField");
		}

	}

	@Override
	public QParser createParser(String qstr, SolrParams localParams,
	    SolrParams params, SolrQueryRequest req) {
		
		return new QParser(qstr, localParams, params, req) {

			@Override
      public Query parse() throws ParseException {
        
        // we must harvest lucene docids
    	  AtomicReader reader = req.getSearcher().getAtomicReader();
    	  BitSet bits = readBase64String(localParams.get(QueryParsing.V), localParams.get("compression"));
    	  
    	  if (bits.cardinality() < 1)
    	  	return null;
    	  
    	  // TODO: add a mapper that can translate from a string 
    	  // field into lucene ids
    	  if (localParams.get("field", defaultField) != null) {
    	  	String fieldName = localParams.get("field", defaultField);
    	  	SchemaField field = req.getSchema().getField(fieldName);
    	  	
    	  	if (field.multiValued()) {
    	  		throw new ParseException("I am sorry, you can't use bitset with multi-valued fields");
    	  	}
    	  	//else if (!allowedTypes.contains(field.getType().getTypeName())) {
    	  	//	throw new ParseException("I am sorry, you can't search against field " + fieldName + " because it is NOT an integer");
    	  	//}
    	  	
    	  	BitSet translatedBitSet = new BitSet(reader.maxDoc());
    	  	int[] cache;
          try {
	          cache = FieldCache.DEFAULT.getInts(reader, fieldName, false);
          } catch (IOException e) {
	          throw new ParseException("Cannot get a cache for field: " + fieldName);
          }
    	  	int i = 0; // lucene docid
    	  	for (int docValue: cache) {
    	  		if (bits.get(docValue)) {
    	  			translatedBitSet.set(i);
    	  		}
    	  		i++;
    	  	}
    	  	
    	  	bits = translatedBitSet;
    	  	
    	  }
    	  
    	  if (bits.cardinality() < 1)
    	  	return null;
    	  
    	  return new BitSetQuery(bits);
        
      }
    };
	}

	
	protected BitSet readBase64String(String string, String compression) throws ParseException {
    byte[] data;
    try {
      data = decodeBase64(string.trim());
    } catch (Exception e1) {
    	throw new ParseException(e1.getMessage());
    }
    
    if (compression != null && compression.equals("gzip")) {
      try {
        data = unGZip(data);
      } catch (Exception e) {
      	throw new ParseException(e.getMessage());
      }
    }
    return fromByteArray(data);
  }
	
	protected String encodeBase64(byte[] data) throws Exception {
		return Base64.byteArrayToBase64(data, 0, data.length);
	}

	protected byte[] decodeBase64(String data) throws Exception {
		return Base64.base64ToByteArray(data);
	}
	
	protected byte[] doGZip(byte[] data) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPOutputStream zipStream = new GZIPOutputStream(baos);
		zipStream.write(data);
		zipStream.flush();
		zipStream.close();
		return baos.toByteArray();
	}
	
	protected byte[] unGZip(byte[] data) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		GZIPInputStream zipStream = new GZIPInputStream(bais);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		byte[] buffer = new byte[1024];
		int len;
    while ((len = zipStream.read(buffer)) > 0) {
    	baos.write(buffer, 0, len);
    }
		bais.close();
		zipStream.close();
		return baos.toByteArray();
	}
	
	
	protected byte[] toByteArray(BitSet bitSet) {
	  // java6 doesn't have toByteArray()
    byte[] bytes = new byte[(bitSet.length() + 7) / 8];
    for ( int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i+1) ) {
        bytes[i / 8] |= 128 >> (i % 8);
    }
    return bytes;
	}
	
	// Returns a bitSet containing the values in bytes.
	protected BitSet fromByteArray(byte[] bytes) {
    BitSet bs = new BitSet(bytes == null? 0 : bytes.length * 8);
    int s = bs.size();
    for (int i = 0; i < s; i++) {
        if (isBitOn(i, bytes))
            bs.set(i);
    }
		return bs;
	}
	
	protected int BIT_MASK[] =  {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80};

  protected boolean isBitOn(int bit, byte[] bytes) {
      int size = bytes == null ? 0 : bytes.length*8;
      if (bit >= size)
          return false;
      return (bytes[bit/8] & BIT_MASK[bit%8]) != 0;
  }
}
