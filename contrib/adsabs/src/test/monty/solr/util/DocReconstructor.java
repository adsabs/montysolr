package monty.solr.util;

import java.io.IOException;
import java.util.*;

import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.*;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;

/**
 * 
 * All this code is copied from the Luke project, 
 * http://code.google.com/p/luke/
 * 
 * It is here as a debugging tool (because the Luke is not
 * compatible with the SOLR true at the moment of writing this)
 * We should use the Luke jar
 * 
 *
 */


public class DocReconstructor extends Observable {

	
	private ProgressNotification progress = new ProgressNotification();
	private String[] fieldNames = null;
	private AtomicReader reader = null;
	private int numTerms;
	private Bits live;

	/**
	 * Prepare a document reconstructor.
	 * @param reader IndexReader to read from.
	 * @throws Exception
	 */
	public DocReconstructor(IndexReader reader) throws Exception {
		this(reader, null, -1);
	}

	/**
	 * Prepare a document reconstructor.
	 * @param reader IndexReader to read from.
	 * @param fieldNames if non-null or not empty, data will be collected only from
	 * these fields, otherwise data will be collected from all fields
	 * @param numTerms total number of terms in the index, or -1 if unknown (will
	 * be calculated)
	 * @throws Exception
	 */
	public DocReconstructor(IndexReader reader, String[] fieldNames, int numTerms) throws Exception {
		if (reader == null) {
			throw new Exception("IndexReader cannot be null.");
		}
		if (reader instanceof CompositeReader) {
			this.reader = new SlowCompositeReaderWrapper((CompositeReader)reader);
		} else if (reader instanceof AtomicReader) {
			this.reader = (AtomicReader)reader;
		} else {
			throw new Exception("Unsupported IndexReader class " + reader.getClass().getName());
		}
		if (fieldNames == null || fieldNames.length == 0) {
			// collect fieldNames
			this.fieldNames = (String[])fieldNames(reader, false).toArray(new String[0]);
		} else {
			this.fieldNames = fieldNames;
		}
		if (numTerms == -1) {
			Fields fields = MultiFields.getFields(reader);
			numTerms = 0;
			Iterator<String> fe = fields.iterator();
			String fld = null;
			while (fe.hasNext()) {
				fld = fe.next();
				Terms t = MultiFields.getTerms(reader, fld);
				TermsEnum te = t.iterator(null);
				while (te.next() != null) {
					numTerms++;
				}
			}
			this.numTerms = numTerms;
		}
		live = MultiFields.getLiveDocs(reader);
	}

	/**
	 * Reconstruct document fields.
	 * @param docNum document number. If this document is deleted, but the index
	 * is not optimized yet, the reconstruction process may still yield the
	 * reconstructed field content even from deleted documents.
	 * @return reconstructed document
	 * @throws Exception
	 */
	public Reconstructed reconstruct(int docNum) throws Exception {
		if (docNum < 0 || docNum > reader.maxDoc()) {
			throw new Exception("Document number outside of valid range.");
		}
		Reconstructed res = new Reconstructed();
		if (live != null && !live.get(docNum)) {
			throw new Exception("Document is deleted.");
		} else {
			StoredDocument doc = reader.document(docNum);
			for (int i = 0; i < fieldNames.length; i++) {
				StorableField[] fs = doc.getFields(fieldNames[i]);
				if (fs != null && fs.length > 0) {
					res.getStoredFields().put(fieldNames[i], fs);
				}
			}
		}
		// collect values from unstored fields
		HashSet<String> fields = new HashSet<String>(Arrays.asList(fieldNames));
		// try to use term vectors if available
		progress.maxValue = fieldNames.length;
		progress.curValue = 0;
		progress.minValue = 0;
		TermsEnum te = null;
		DocsAndPositionsEnum dpe = null;
		for (int i = 0; i < fieldNames.length; i++) {
			Terms tvf = reader.getTermVector(docNum, fieldNames[i]);
			if (tvf != null) { // has vectors for this field
				te = tvf.iterator(te);
				progress.message = "Checking term vectors for '" + fieldNames[i] + "' ...";
				progress.curValue = i;
				setChanged();
				notifyObservers(progress);
				List<IntPair> vectors = new TermVectorMapper().map(tvf, te, false, true);
				if (vectors != null) {
					GrowableStringArray gsa = res.getReconstructedFields().get(fieldNames[i]);
					if (gsa == null) {
						gsa = new GrowableStringArray();
						res.getReconstructedFields().put(fieldNames[i], gsa);
					}
					for (IntPair ip : vectors) {
						for (int m = 0; m < ip.positions.length; m++) {
							gsa.append(ip.positions[m], "|", ip.text);
						}
					}
					fields.remove(fieldNames[i]); // got what we wanted
				}
			}
		}
		// this loop collects data only from left-over fields
		// not yet collected through term vectors
		progress.maxValue = fields.size();
		progress.curValue = 0;
		progress.minValue = 0;
		for (String fld : fields) {
			progress.message = "Collecting terms in " + fld + " ...";
			progress.curValue++;
			setChanged();
			notifyObservers(progress);
			Terms terms = MultiFields.getTerms(reader, fld);
			if (terms == null) { // no terms in this field
				continue;
			}
			te = terms.iterator(te);
			while (te.next() != null) {
				DocsAndPositionsEnum newDpe = te.docsAndPositions(live, dpe, 0);
				if (newDpe == null) { // no position info for this field
					break;
				}
				dpe = newDpe;
				int num = dpe.advance(docNum);
				if (num != docNum) { // either greater than or NO_MORE_DOCS
					continue; // no data for this term in this doc
				}
				String term = te.term().utf8ToString();
				GrowableStringArray gsa = (GrowableStringArray)
				res.getReconstructedFields().get(fld);
				if (gsa == null) {
					gsa = new GrowableStringArray();
					res.getReconstructedFields().put(fld, gsa);
				}
				for (int k = 0; k < dpe.freq(); k++) {
					int pos = dpe.nextPosition();
					gsa.append(pos, "|", term);
				}
			}
		}
		progress.message = "Done.";
		progress.curValue = 100;
		setChanged();
		notifyObservers(progress);
		return res;
	}

	/**
	 * This class represents a reconstructed document.
	 * @author ab
	 */
	public static class Reconstructed {
		private Map<String, StorableField[]> storedFields;
		private Map<String, GrowableStringArray> reconstructedFields;

		public Reconstructed() {
			storedFields = new HashMap<String, StorableField[]>();
			reconstructedFields = new HashMap<String, GrowableStringArray>();
		}

		/**
		 * Construct an instance of this class using existing field data.
		 * @param storedFields field data of stored fields
		 * @param reconstructedFields field data of unstored fields
		 */
		public Reconstructed(Map<String, StorableField[]> storedFields,
				Map<String, GrowableStringArray> reconstructedFields) {
			this.storedFields = storedFields;
			this.reconstructedFields = reconstructedFields;
		}

		/**
		 * Get an alphabetically sorted list of field names.
		 */
		public List<String> getFieldNames() {
			HashSet<String> names = new HashSet<String>();
			names.addAll(storedFields.keySet());
			names.addAll(reconstructedFields.keySet());
			ArrayList<String> res = new ArrayList<String>(names.size());
			res.addAll(names);
			Collections.sort(res);
			return res;
		}

		public boolean hasField(String name) {
			return storedFields.containsKey(name) || reconstructedFields.containsKey(name);
		}

		/**
		 * @return the storedFields
		 */
		public Map<String, StorableField[]> getStoredFields() {
			return storedFields;
		}

		/**
		 * @return the reconstructedFields
		 */
		public Map<String, GrowableStringArray> getReconstructedFields() {
			return reconstructedFields;
		}

	}

	public static Collection<String> fieldNames(IndexReader r, boolean indexedOnly) throws IOException {
		AtomicReader reader;
		if (r instanceof CompositeReader) {
			reader = new SlowCompositeReaderWrapper((CompositeReader)r);
		} else {
			reader = (AtomicReader)r;
		}
		Set<String> res = new HashSet<String>();
		FieldInfos infos = reader.getFieldInfos();
		for (FieldInfo info : infos) {
			if (indexedOnly && info.isIndexed()) {
				res.add(info.name);
				continue;
			}
			res.add(info.name);
		}
		return res;
	}

	public class TermVectorMapper {

		public List<IntPair> map(Terms terms, TermsEnum reuse, boolean acceptTermsOnly, boolean convertOffsets) throws IOException {
			TermsEnum te = terms.iterator(reuse);
			DocsAndPositionsEnum dpe = null;
			List<IntPair> res = new ArrayList<IntPair>();
			while (te.next() != null) {
				DocsAndPositionsEnum newDpe = te.docsAndPositions(null, dpe, DocsAndPositionsEnum.FLAG_OFFSETS);
				if (newDpe == null) { // no positions and no offsets - just add terms if allowed
					if (!acceptTermsOnly) {
						return null;
					}
					int freq = (int)te.totalTermFreq();
					if (freq == -1) freq = 0;
					res.add(new IntPair(freq, te.term().utf8ToString()));
					continue;
				}
				dpe = newDpe;
				// term vectors have only one document, number 0
				if (dpe.nextDoc() == DocsEnum.NO_MORE_DOCS) { // oops
					// treat this as no positions nor offsets
					int freq = (int)te.totalTermFreq();
					if (freq == -1) freq = 0;
					res.add(new IntPair(freq, te.term().utf8ToString()));
					continue;
				}
				IntPair ip = new IntPair(dpe.freq(), te.term().utf8ToString());
				for (int i = 0; i < dpe.freq(); i++) {
					int pos = dpe.nextPosition();
					if (pos != -1) {
						if (ip.positions == null) {
							ip.positions = new int[dpe.freq()];
						}
						ip.positions[i] = pos;
					}
					if (dpe.startOffset() != -1) {
						if (ip.starts == null) {
							ip.starts = new int[dpe.freq()];
							ip.ends = new int[dpe.freq()];
						}
						ip.starts[i] = dpe.startOffset();
						ip.ends[i] = dpe.endOffset();
					}
				}
				if (convertOffsets && ip.positions == null) {
					convertOffsets(ip);
				}
				res.add(ip);
			}
			return res;
		}

		private void convertOffsets(IntPair ip) {
			if (ip.starts == null || ip.ends == null) {
				return;
			}
			int[] posArr = new int[ip.starts.length];
			int curPos = 0;
			int maxDelta = 3; // allow 3 characters diff, otherwise insert a skip
			int avgTermLen = 5; // assume this is the avg. term length of missing terms
			for (int m = 0; m < ip.starts.length; m++) {
				int curStart = ip.starts[m];
				if (m > 0) {
					int prevEnd = ip.ends[m - 1];
					int prevStart = ip.starts[m - 1];
					if (curStart == prevStart) {
						curPos--; // overlapping token
					} else {
						if (prevEnd + maxDelta < curStart) { // possibly a gap
							// calculate the number of missing tokens
							int increment = (curStart - prevEnd) / (maxDelta + avgTermLen);
							if (increment == 0) increment++;
							curPos += increment;
						}
					}
				}
				posArr[m] = curPos;
				curPos++;
			}
			ip.positions = posArr;
		}
	}

	public class GrowableStringArray {
		public int INITIAL_SIZE = 20;

		private int size = 0;

		private String[] array = null;

		public int size() {
			return size;
		}

		/**
		 * Sets the value at specified index. If index is outside range the array is automatically
		 * expanded.
		 * @param index where to set the value
		 * @param value
		 */
		public void set(int index, String value) {
			if (array == null) array = new String[INITIAL_SIZE];
			if (array.length < index + 1) {
				String[] newArray = new String[index + INITIAL_SIZE];
				System.arraycopy(array, 0, newArray, 0, array.length);
				array = newArray;
			}
			if (index > size - 1) size = index + 1;
			array[index] = value;
		}

		/**
		 * Appends the separator and value at specified index. If no value exists at the
		 * specified position, this is equivalent to {@link #set(int, String)} - no separator
		 * is appended in that case.
		 * @param index selected position
		 * @param sep separator
		 * @param value value
		 */
		public void append(int index, String sep, String value) {
			String oldVal = get(index);
			if (oldVal == null) {
				set(index, value);
			} else {
				set(index, oldVal + sep + value);
			}
		}

		/**
		 * Return the value at specified index.
		 * @param index
		 * @return
		 */
		public String get(int index) {
			if (array == null || index < 0 || index > array.length - 1) return null;
			return array[index];
		}

		public String toString(String separator) {
			StringBuffer sb = new StringBuffer();
			String sNull = "null";
			int k = 0, m = 0;
			for (int j = 0; j < size(); j++) {
				if (get(j) == null)
					k++;
				else {
					if (sb.length() > 0) sb.append(separator);
					if (m > 0 && m % 5 == 0) sb.append('\n');
					if (k > 0) {
						sb.append(sNull + "_" + k + separator);
						k = 0;
						m++;
					}
					sb.append(get(j));
					m++;
				}
			}
			return sb.toString();
		}
	}


	public static class PairComparator implements java.util.Comparator<IntPair> {
		private boolean ascending;
		private boolean byText;

		public PairComparator(boolean byText, boolean ascending) {
			this.ascending = ascending;
			this.byText = byText;
		}

		public int compare(IntPair h1, IntPair h2) {
			if (byText) {
				return ascending?h1.text.compareTo(h2.text):h2.text.compareTo(h1.text);
			} else {
				if (h1.cnt > h2.cnt) return ascending?-1:1;
				if (h1.cnt < h2.cnt) return ascending?1:-1;
			}
			return 0;
		}
	}
	
	public class IntPair {

		public int[] positions;
		public int[] starts;
		public int[] ends;
		public int cnt = 0;
		public String text = null;

		public IntPair(int cnt, String text) {
			this.cnt = cnt;
			this.text = text;
		}

		public String toString() {
			return cnt + ":'" + text + "'";
		}


	}

	public class ProgressNotification {
		public int curValue = 0;
		public int minValue = 0;
		public int maxValue = 0;
		public boolean aborted = false;
		public String message = null;
	};
	
	public void close() throws IOException {
		reader.close();
	}

}
