package org.apache.solr.search;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.zip.DeflaterInputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BitSetQuery;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.Query;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.Base64;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.common.util.ContentStreamBase.StringStream;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.handler.RequestHandlerUtils;
import org.apache.solr.handler.loader.ContentStreamLoader;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.BitSetQParserPlugin.DataProcessor;
import org.apache.solr.update.processor.UpdateRequestProcessor;

/*
 * Implementation note: I wanted to make this more flexible and use
 * the SOLR stream readers. But I'm pressed for time, so I cannot 
 * spend much energy here...
 * 
 * This plugin will do following:
 * 
 * 	1. read data
 *  2. decode data into bitsets
 *  3. create bitset query
 */
public class BitSetQParserPlugin extends QParserPlugin {

	public static String NAME = "bitset";
	private String defaultField = null;
	private List<String> allowedFields = new ArrayList<String>();

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

			private int maxAllowedGetSize;

			@Override
			public Query parse() throws ParseException {
				
				localParams = req.getParams();
				
				List<DataProcessor> processors = new ArrayList<DataProcessor>();
				
				Iterable<ContentStream> streams = req.getContentStreams();
				if (streams != null) {
					for (ContentStream cs: req.getContentStreams()) {
						DataProcessor streamProcessor = getStreamProcessor(localParams, cs);
						if (streamProcessor != null) {
							processors.add(streamProcessor);
						}
					}
				}
				
				String data;
				// we also allow passing of data inside normal parametes (useful for testing)
				if (localParams.get("f", null) != null) {
					data = localParams.get(localParams.get("f"), null);
					if (data != null) {
						if (data.length() > maxAllowedGetSize) { // solr loaded it anyway, but at least we are educating people ;)
							throw new SolrException(ErrorCode.FORBIDDEN, "The data you sent is too big for GET requests. Use data streams instead");
						}
						StringStream cs = new ContentStreamBase.StringStream(data);
						cs.setContentType("big-query/" + localParams.get("type", "bitset") 
								                           + "-" + localParams.get("encoding", "none")
								                           + "; compression:" + localParams.get("compression", "none")
								                           );
						DataProcessor streamProcessor = getStreamProcessor(localParams, cs);
						
						if (streamProcessor != null) {
							processors.add(streamProcessor);
						}
						
					}
				}
				
				if (processors.size() == 0) {
					return null;
				}
				
				String[] operator = localParams.get("operator","and").split(",");
				if (operator.length > 1 && operator.length != processors.size()-1) {
					throw new SolrException(ErrorCode.BAD_REQUEST, 
							"There is " + processors.size() + " data strams, but inconsistent number of operators: " + localParams.get("operator","and"));
				}
				
				BitSet topBits = null;
				int i = 0;
				for (DataProcessor processor : processors) {
					
					BitSet bits = processor.getBits();
					
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
					return null;

				return new BitSetQuery(topBits);

			}

			private DataProcessor getStreamProcessor(SolrParams localParams,
          ContentStream cs) {
	      // TODO Auto-generated method stub
	      return null;
      }

			private DataProcessor getStreamProcessor(SolrParams localParams,
          String contentType, String sourceInfo) {
	      // TODO Auto-generated method stub
	      return null;
      }

			private Object getProcessor(SolrQueryRequest req, String contentType) {
	      // TODO Auto-generated method stub
	      return null;
      }
		};
	}
	
	
	public static class DataProcessor {
		
		public BitSet getBits() {
			return new BitSet();
		}
	}
	/*
	protected BitSet convertDataStream(DataStream data, SolrQueryRequest req) {
		
		localParams = req.getParams()
		bits = fromByteArray(data, isLittleEndian ? LITTLE_ENDIAN_BIT_MASK : BIG_ENDIAN_BIT_MASK);
		

		// TODO: add a mapper that can translate from a string 
		// field into lucene ids
		if (localParams.get("field", defaultField) != null) {
			String fieldName = localParams.get("field", defaultField);
			SchemaField field = req.getSchema().getField(fieldName);

			if (field.multiValued()) {
				throw new ParseException("I am sorry, you can't use bitset with multi-valued fields");
			}

			if (allowedFields.size() > 0 && !allowedFields.contains(fieldName)) {
				throw new ParseException("I am sorry, you can't search against field " + fieldName + " (reason: #!@#!)");
			}

			BitSet translatedBitSet = new BitSet(reader.maxDoc());
			int[] cache;
			try {
				cache = FieldCache.DEFAULT.getInts(reader, fieldName, false);
			} catch (IOException e) {
				throw new ParseException("Cannot get a cache for field: " + fieldName + "\n" + e.getMessage());
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
	  return null;
  }

	*/
	
	public static class DataStream {

		public DataStream(byte[] data, boolean bool) {
	    // TODO Auto-generated constructor stub
    }
		
	}

	protected DataStream readData(SolrQueryRequest req) throws ParseException {
		// read the data - it can be either in the content stream xor directly
		// in the open value (perhaps encoded, compressed etc)
		
		SolrParams params = req.getParams();
		Iterable<ContentStream> streams = req.getContentStreams();
		byte[] data = null;
		
		if (streams != null) {
			for (ContentStream stream : streams) {
				String ct = stream.getContentType();
				if (ct != null && ct.toLowerCase().contains("bigquery/csv")) {
					//pass
				}
			}
			if (data == null) {
				throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "empty content stream");
			}

		} 
		else {
			
			// the data is inside the normal SOLR parameter
			// they must always be base64 encoded (that is
			// safety mechanism)
			
			AtomicReader reader = req.getSearcher().getAtomicReader();
			data = readBase64String(params.get(QueryParsing.V), 
					params.get("compression", "none"));

			if (data == null) {
				throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "missing data");
			}
		}
		
		return new DataStream(data, params.getBool("little_endian", false));
  }

	protected byte[] readBase64String(String string, String compression) 
	throws ParseException {

		byte[] data;
		try {
			data = decodeBase64(string.trim());
		} catch (Exception e1) {
			throw new ParseException(e1.getMessage());
		}

		if (compression != null && !compression.equals("none")) {
			try {
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
					throw new ParseException("Unsupporeted compression: " + compression);
				}
			} catch (Exception e) {
				throw new ParseException(e.getMessage());
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
		DeflaterInputStream zipStream = new DeflaterInputStream(bais);
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

	// Returns a bitSet containing the values in bytes. Since I am planning to use
	// python intbitsets and these are (probably) encoded using little endian
	// we must be able to de-construct them properly, however internally, inside
	// Java we should be using big endian
	protected BitSet fromByteArray(byte[] bytes, int[] bitMask) {
		BitSet bs = new BitSet(bytes == null? 0 : bytes.length * 8);
		int s = bytes.length * 8;
		for (int i = 0; i < s; i++) {
			if ((bytes[i/8] & bitMask[i%8]) != 0) // ((bytes[i/8] & (128 >> (i % 8))) != 0) 
				bs.set(i);
		}
		return bs;
	}

	protected BitSet fromByteArray(byte[] bytes) {
		return fromByteArray(bytes, BIG_ENDIAN_BIT_MASK);
	}

	protected int BIG_ENDIAN_BIT_MASK[] =  {0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01}; 
	protected int LITTLE_ENDIAN_BIT_MASK[] = {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80};

}
