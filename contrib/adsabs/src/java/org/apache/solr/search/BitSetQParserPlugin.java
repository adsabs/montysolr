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
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.Base64;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.handler.RequestHandlerUtils;
import org.apache.solr.handler.loader.ContentStreamLoader;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.update.processor.UpdateRequestProcessor;

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

			@Override
			public Query parse() throws ParseException {
				
				/*
				// read the data - it can be either in the content stream xor directly
				// in the open value (perhaps encoded, compressed etc)



		      Iterable<ContentStream> streams = req.getContentStreams();
		      if (streams == null) {
		      	// we must harvest lucene docids
						AtomicReader reader = req.getSearcher().getAtomicReader();
						BitSet bits = readBase64String(localParams.get(QueryParsing.V), 
								localParams.get("compression", "none"),
								localParams.getBool("little_endian", false));
						
						if (data == null) {
		        	throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "missing data");
		        }
						
		      } else {
		        for (ContentStream stream : streams) {
		          documentLoader.load(req, rsp, stream, processor);
		        }
		        if (data == null) {
		        	throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "empty content stream");
		        }
		      }
			*/
				
			// we must harvest lucene docids
				AtomicReader reader = req.getSearcher().getAtomicReader();
				BitSet bits = readBase64String(localParams.get(QueryParsing.V), 
						localParams.get("compression", "none"),
						localParams.getBool("little_endian", false));

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

				if (bits.cardinality() < 1)
					return null;

				return new BitSetQuery(bits);

			}
		};
	}


	protected BitSet readBase64String(String string, String compression, boolean isLittleEndian) 
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
			} catch (Exception e) {
				throw new ParseException(e.getMessage());
			}
		}

		return fromByteArray(data, isLittleEndian ? LITTLE_ENDIAN_BIT_MASK : BIG_ENDIAN_BIT_MASK);
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
