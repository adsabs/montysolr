package org.apache.solr.search;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BitSetQuery;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.MatchNoDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SolrCacheWrapper;
import org.apache.lucene.util.BitSet;
import org.apache.lucene.util.FixedBitSet;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.Base64;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.common.util.ContentStreamBase.StringStream;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.handler.loader.CSVLoader;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.schema.StrField;
import org.apache.solr.schema.TextField;
import org.apache.solr.schema.TrieIntField;
import org.apache.solr.uninverting.UninvertingReader;
import org.apache.solr.update.AddUpdateCommand;
import org.apache.solr.update.processor.UpdateRequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Flexible framework to query SOLR by (a large set)
 * of ID's. This can be used to implement remote/fast
 * bitset operations or to retrieve data that cannot
 * be easily expressed using a query language; typically
 * st like: id:1 OR id:2 OR id:3.............. id:n
 */
public class BitSetQParserPlugin extends QParserPlugin {

	public static final Logger log = LoggerFactory.getLogger(BitSetQParserPlugin.class);
	
	public static String NAME = "bitset";
	private Set<String> allowedFields = new HashSet<String>();
	private static Map<String, String> cacheMapping = new HashMap<String, String>();
	private int maxAllowedGetSize = 5000;

	@SuppressWarnings("rawtypes")
  @Override
	public void init(NamedList args) {

		NamedList defs = (NamedList) args.get("defaults");
		if (defs == null) {
			defs = new NamedList();
		}

		if (defs.get("cache-mapping") != null) {
			for (String s: ((String)defs.get("cache-mapping")).split(",")) {
				String[] parts = s.split(":");
				if (parts.length == 2) {
					cacheMapping.put(parts[0], parts[1]);
				}
				else {
					throw new SolrException(ErrorCode.SERVER_ERROR, "Wrong mapping format: " + s);
				}
			}
		}
		
		if (defs.get("allowed-fields") != null) {
			for (String s: ((String) defs.get("allowed-fields")).split(",")) {
				allowedFields.add(s);
			}
		}
		
		if (defs.get("max-allowed-get-size") != null) {
			maxAllowedGetSize = Integer.parseInt((String) defs.get("max-allowed-get-size"));
		}
	}

	@Override
	public QParser createParser(String qstr, SolrParams localParams,
			SolrParams params, SolrQueryRequest req) {
		
		
		return new QParser(qstr, localParams, params, req) {

			@Override
			public Query parse() {
				List<DataProcessor> processors = new ArrayList<DataProcessor>();
				
				try {
					
					Iterable<ContentStream> streams = req.getContentStreams();
					if (streams != null) {
						for (ContentStream cs: req.getContentStreams()) {
							DataProcessor streamProcessor = getStreamProcessor(cs);
							if (streamProcessor != null) {
								processors.add(streamProcessor);
							}
						}
					}
					
					String data;
					// we also allow passing of data inside normal parametes (useful for testing)
					data = localParams.get(QueryParsing.V);
					if (data != null && data.length() > 0) {
						if (data.length() > maxAllowedGetSize) { // solr loaded it anyway, but at least we are educating people ;)
							throw new SolrException(ErrorCode.FORBIDDEN, "The data you sent is too big for GET requests. Use data streams instead");
						}
						
						StringStream cs = new ContentStreamBase.StringStream(data);
						cs.setContentType("big-query/" + localParams.get("type", "bitset") 
								                           + "-" + localParams.get("encoding", "none")
								                           + "; compression:" + localParams.get("compression", "none")
								                           );
						DataProcessor streamProcessor = getStreamProcessor(cs);
						
						if (streamProcessor != null) {
							processors.add(streamProcessor);
						}
						
					}
					
					if (processors.size() == 0) {
						return new MatchNoDocsQuery();
					}
					
					String[] operator = localParams.get("operator","and").split(",");
					if (operator.length > 1 && operator.length != processors.size()-1) {
						throw new SolrException(ErrorCode.BAD_REQUEST, 
								"There is " + processors.size() + " data streams, but inconsistent number of operators: " + localParams.get("operator","and"));
					}
					
					FixedBitSet topBits = null;
					int i = 0;
					for (DataProcessor processor : processors) {
						
						FixedBitSet bits = processor.getBits();
						
						if (bits == null) {
							if (operator.length > 0) {
								i++;
							}
							continue;
						}
							
						
						if (topBits == null) {
							topBits = bits;
							continue;
						}
						
						String op = operator[i];
						if (op.equals("and")) {
							topBits.and(bits);
						}
						else if (op.equals("or")) {
							topBits.or(bits);
						}
						else if (op.equals("not")) {
							topBits.andNot(bits);
						}
						else if (op.equals("xor")) {
							topBits.xor(bits);
						}
						else {
							throw new SolrException(ErrorCode.BAD_REQUEST, "Unknown bitset operator: " + op);
						}
						
						if (operator.length > 0) {
							i++;
						}
					}
					
					
					if (topBits.cardinality() < 1)
						return new MatchNoDocsQuery();
	
					BitSetQuery q = new BitSetQuery(topBits);
					
					if (localParams.get("uniqueId", null) != null) {
						q.setUUID(UUID.randomUUID().toString());
					}
					
					return q;
				}
				catch (Exception e) {
					throw new SolrException(ErrorCode.SERVER_ERROR, e);
				}

			}

			private DataProcessor getStreamProcessor(ContentStream cs) throws Exception {
				
				// if 'streamId' is set, we may limit ourselves to grabbing only
				// some streams
	      if (localParams.get("streamId", null) != null) {
	      	if (!cs.getContentType().contains(localParams.get("streamId"))) {
	      		return null;
	      	}
	      }
	      String ct = cs.getContentType();
	      if (ct.contains("big-query/csv")) {
	      	DataProcessor p = new DataProcessor(req);
	      	
	      	CSVLoader loader = new CSVLoader();
	        loader.load(req, null, cs, p);
	        return p;
	      }
	      else if (ct.contains("big-query/bitset")) {
	      	
	      	DataProcessor p = new DataProcessor(req) {
	      		@Override
	      		public FixedBitSet getBits() {
	      			// we must harvest lucene docids
	    				LeafReader reader = req.getSearcher().getSlowAtomicReader();
	    				byte[] data;
              
	    				try {
	              data = readBase64String(localParams.get(QueryParsing.V), 
	              		localParams.get("compression", "none"));
              } catch (IOException e1) {
	              throw new SolrException(ErrorCode.BAD_REQUEST, e1);
              }
              
	    				FixedBitSet bits = fromByteArray(data, 
	    						localParams.getBool("little_endian", false)
	    						?	LITTLE_ENDIAN_BIT_MASK : BIG_ENDIAN_BIT_MASK);
	    				
	    				// now, the bitset can contain lucene docids or it can be
	    				// set of integer values that need translation into lucene
	    				// docids; this depends on presence/absence of 'field' param
	    				
	    				if (localParams.get("field", null) == null)
	    				  return bits;
	    				
	    				
    					String fieldName = localParams.get("field");
    					SchemaField field = req.getSchema().getField(fieldName);

    					if (field.multiValued()) {
    						throw new SolrException(ErrorCode.BAD_REQUEST, "I am sorry, you can't use bitset with multi-valued fields");
    					}

    					if (allowedFields.size() > 0 && !allowedFields.contains(fieldName)) {
    						throw new SolrException(ErrorCode.BAD_REQUEST, "I am sorry, you can't search against field " + fieldName + " (reason: field forbidden#!@#!)");
    					}

    					
    					
    					FieldType ftype = field.getType();
  						Class<? extends FieldType> c = ftype.getClass();
  						boolean fieldIsInt = true;
  						if (c.isAssignableFrom(TextField.class) || c.isAssignableFrom(StrField.class)) {
  							fieldIsInt = false;
  						}
  						else if (c.isAssignableFrom(TrieIntField.class)) {
  							//pass
  						}
  						else {
  							throw new SolrException(ErrorCode.BAD_REQUEST, "You make me sad - this field: " + fieldName + " is not indexed as integer :(");
  						}
  						
  						FixedBitSet translatedBitSet = new FixedBitSet(reader.maxDoc());
  						
  						
    					SolrCacheWrapper<SolrCache<Object,Integer>> cacheWrapper = super.getCache(fieldName);
    					if (cacheWrapper != null) { // we are lucky and we have a cache that can translate values for us
    						for (int i = bits.nextSetBit(0); i >= 0 && i < DocIdSetIterator.NO_MORE_DOCS; i = bits.nextSetBit(i+1)) {
    					     if (fieldIsInt) {
    					    	 int v = cacheWrapper.getLuceneDocId(0, i);
    					    	 if (v == -1)
    					    		 continue;
  					    		 translatedBitSet.set(v);
    					     }
    					     else {
    					    	 int v = cacheWrapper.getLuceneDocId(0, Integer.toString(i));
    					    	 if (v == -1)
    					    		 continue;
    					    	 translatedBitSet.set(v);
    					     }
    					  }
    						bits = translatedBitSet;
    					}
    					else {
    					
    						if (!fieldIsInt) {
    							throw new SolrException(ErrorCode.BAD_REQUEST, "You make me sad - this field: " + fieldName + " is not indexed as integer :(");
    						}
    						
    						Map<String, UninvertingReader.Type> mapping = new HashMap();
    		        mapping.put(fieldName, UninvertingReader.Type.INTEGER_POINT);
    		        UninvertingReader uninvertingReader = new UninvertingReader(reader, mapping);
    		        NumericDocValues cache;
                try {
                  cache = uninvertingReader.getNumericDocValues(fieldName);
                } catch (IOException e) {
                  return translatedBitSet;
                }
    		        
	    					
	    					// suckers, we have to translate whateve integer value into a lucene docid
	    					log.warn("We are translating values for a field without a cache: {}. Terrible, terrible idea!", fieldName);
	    					
	    					int docid = 0; // lucene docid
	    					int maxDoc = reader.maxDoc();
	    					int docValue;
	    					while(docid < maxDoc) {
	    						docValue = (int) cache.get(docid);
	    						if (docValue < bits.length() && docValue > 0 && bits.get(docValue)) {
	    							translatedBitSet.set(docid);
	    						}
	    						docid++;
	    					}
    					
	    					bits = translatedBitSet;
    					}
	    				return bits;
	      		}
	      	};
	      	return p;
	      }
	      
	      return null;
      }

		};
	}
	
	
	
	public static class DataProcessor extends UpdateRequestProcessor {
		
		private ArrayList<SolrInputDocument> docs;
		SolrQueryRequest req;

		public void processAdd(AddUpdateCommand cmd) throws IOException {
	    docs.add(cmd.solrDoc);
	  }
		
		public DataProcessor(SolrQueryRequest req) {
	    super(null);
	    docs = new ArrayList<SolrInputDocument>();
	    this.req = req;
    }

		public FixedBitSet getBits() throws ParseException {
			
			if (docs.size() == 0) {
				return new FixedBitSet(0);
			}
			
			FixedBitSet bs = new FixedBitSet(req.getSearcher().maxDoc());
			
			SolrInputDocument d = docs.get(0);
			// for csv, we can assume that every doc has the same fields (?)
			Iterator<SolrInputField> fi = d.iterator();
			
			HashMap<String, SolrCacheWrapper<SolrCache<Object,Integer>>> translators = new HashMap<String, SolrCacheWrapper<SolrCache<Object,Integer>>>();
			
			while (fi.hasNext()) {
				SolrInputField field = fi.next();
				SolrCacheWrapper<SolrCache<Object,Integer>> cache = getCache(field.getName());
				if (cache == null) {
					throw new SolrException(ErrorCode.BAD_REQUEST, "Uff, uff, I have no idea how to map this field (" + field.getName() + ") values into docids! Call 911");
				}
				translators.put(field.getName(), cache);
			}
			
			for (SolrInputDocument doc: docs) {
				for (SolrInputField f: doc.values()) {
					SolrCacheWrapper<SolrCache<Object,Integer>> c = translators.get(f.getName());
					for (Object o: f.getValues()) {
						int v = c.getLuceneDocId(0, o);
						if (v == -1)
							continue;
						bs.set(v);
					}
				}
			}
			return bs;
		}
		
    @SuppressWarnings("unchecked")
    public SolrCacheWrapper<SolrCache<Object, Integer>> getCache(String field) {
			
			SolrCache<Object, Integer> sCache = null;
			if (cacheMapping.containsKey(field)) {
				sCache = (SolrCache<Object, Integer>) req.getSearcher().getCache(cacheMapping.get(field));
			}
			else {
				sCache = (SolrCache<Object, Integer>) req.getSearcher().getCache(field);
			}
			
			
			if (sCache == null) {
				return null;
			}
			
			return new SolrCacheWrapper<SolrCache<Object, Integer>>(sCache) {
        @Override
				public int getLuceneDocId(int sourceDocid, Object sourceValue) {
					// extra checking necessary (we cannot be sure
					// the id will be always correct....
					
					if (sourceValue instanceof String) {
						sourceValue = ((String) sourceValue).toLowerCase().trim();
					}
					
				  Object v = cache.get().get(sourceValue);
				  if (v == null)
				  	return -1;
				  return (Integer) v;
			  }

				@Override
        public int internalHashCode() {
          return this.hashCode();
        }

				@Override
        public String internalToString() {
          return cache.get().toString();
        }
			};
		}
	}
	
	public static class DataStream {

		public DataStream(byte[] data, boolean bool) {
	    // TODO Auto-generated constructor stub
    }
		
	}


	protected byte[] readBase64String(String string, String compression) 
	throws IOException {

		byte[] data;
		try {
			data = decodeBase64(string.trim());
		} catch (Exception e1) {
			throw new SolrException(ErrorCode.BAD_REQUEST, e1);
		}

		if (compression != null && !compression.equals("none")) {
				if (compression.equals("gzip")) {
					data = unGZip(data);
				}
				else if (compression.equals("zip")) {
					data = unZip(data);
				}
				else if ("none".equals(compression)) {
					// do nothing
				}
				else {
					throw new SolrException(ErrorCode.BAD_REQUEST, "Unsupporeted compression: " + compression);
				}
		}
		return data;
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

	protected byte[] doZip(byte[] data) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DeflaterOutputStream zipStream = new DeflaterOutputStream(baos);
		zipStream.write(data);
		zipStream.flush();
		zipStream.close();
		return baos.toByteArray();
	}

	private byte[] unZip(byte[] data) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		InflaterInputStream zipStream = new InflaterInputStream(bais);
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
		byte[] bytes = new byte[(bitSet.length() + 7) / 8];
		for ( int i = bitSet.nextSetBit(0); i >= 0 && i < DocIdSetIterator.NO_MORE_DOCS; i = bitSet.nextSetBit(i+1) ) {
			bytes[i / 8] |= 128 >> (i % 8);
			if (i+1 >= bitSet.length())
			  break;
		}
		return bytes;
	}

	// Returns a bitSet containing the values in bytes. Since I am planning to use
	// python intbitsets and these are (probably) encoded using little endian
	// we must be able to de-construct them properly, however internally, inside
	// Java we should be using big endian
	protected FixedBitSet fromByteArray(byte[] bytes, int[] bitMask) {
		FixedBitSet bs = new FixedBitSet(bytes == null? 0 : bytes.length * 8);
		int s = bytes.length * 8;
		for (int i = 0; i < s; i++) {
			if ((bytes[i/8] & bitMask[i%8]) != 0) // ((bytes[i/8] & (128 >> (i % 8))) != 0) 
				bs.set(i);
		}
		return bs;
	}

	protected FixedBitSet fromByteArray(byte[] bytes) {
		return fromByteArray(bytes, BIG_ENDIAN_BIT_MASK);
	}

	protected int BIG_ENDIAN_BIT_MASK[] =  {0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01}; 
	protected int LITTLE_ENDIAN_BIT_MASK[] = {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80};

}
