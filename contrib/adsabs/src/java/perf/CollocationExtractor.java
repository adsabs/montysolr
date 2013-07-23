package monty.solr.perf;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.PriorityQueue;

/**
 * See:
 * http://issues.apache.org/jira/browse/LUCENE-474
 * 
 * @author iprovalov (some clean up, refactoring, unit tests, ant/maven, etc...)
 * 
 * Class used to find collocated terms in an index created with TermVector support
 * 
 * @author MAHarwood
 */
public class CollocationExtractor {
	static int DEFAULT_MAX_NUM_DOCS_TO_ANALYZE = 1200;
	static int maxNumDocsToAnalyze = DEFAULT_MAX_NUM_DOCS_TO_ANALYZE;
	String fieldName = "contents";
	static float DEFAULT_MIN_TERM_POPULARITY = 0.0002f;
	float minTermPopularity = DEFAULT_MIN_TERM_POPULARITY;
	static float DEFAULT_MAX_TERM_POPULARITY = 1f;
	float maxTermPopularity = DEFAULT_MAX_TERM_POPULARITY;
	int numCollocatedTermsPerTerm = 20;
	IndexReader reader;
	int slopSize = 5;

	TermFilter filter = new TermFilter();

	public CollocationExtractor(IndexReader reader) {
		this.reader = reader;
	}


	public void extract(CollocationIndexer logger) throws IOException {
		Term term = null;
		TermEnum te = reader.terms(new Term(fieldName, ""));
		while (te.next()) {
			term = te.term();
			if (!fieldName.equals(term.field())) {
				break;
			}
			processTerm(term, logger, slopSize);
		}
	}

	// called for every term in the index
	void processTerm(Term term, CollocationIndexer logger, int slop)
			throws IOException {
		if (!filter.processTerm(term.text())) {
			return;
		}
		TermEnum te = reader.terms(term);
		int numDocsForTerm = Math.min(te.docFreq(), maxNumDocsToAnalyze);
		int totalNumDocs = reader.numDocs();
		float percent = (float) numDocsForTerm / (float) totalNumDocs;

		isTermTooPopularOrNotPopularEnough(term, percent);

		// get a list of all the docs with this term
		TermDocs td = reader.termDocs(term);

		HashMap<String, CollocationScorer> phraseTerms = new HashMap<String, CollocationScorer>();
		int MAX_TERMS_PER_DOC = 10000;
		BitSet termPos = new BitSet(MAX_TERMS_PER_DOC);

		int numDocsAnalyzed = 0;
		// for all docs that contain this term
		while (td.next()) {
			numDocsAnalyzed++;
			if (numDocsAnalyzed > maxNumDocsToAnalyze) {
				break;
			}
			int docId = td.doc();

			// get TermPositions for matching doc
			Terms tpv = reader.getTermVector(docId, fieldName);
			TermsEnum terms = tpv.iterator(null);
			termPos.clear();
			int index = recordAllPositionsOfTheTermInCurrentDocumentBitset(
					term, termPos, tpv, terms);

			BytesRef t;
			// now look at all OTHER terms in this doc and see if they are
			// positioned in a pre-defined
			// sized window around the current term
			while((t = terms.next()) != null) {
				if (j == index) {
					continue;
				}
				if (!filter.processTerm(terms[j])) {
					continue;
				}

				int[] pos = tpv.getTermPositions(j);
				boolean matchFound = false;
				for (int k = 0; ((k < pos.length) && (!matchFound)); k++) {
					int startpos = Math.max(0, pos[k] - slop);
					int endpos = pos[k] + slop;
					matchFound = populateHashMapWithPhraseTerms(term,
							numDocsForTerm, totalNumDocs, phraseTerms, termPos,
							terms, j, matchFound, startpos, endpos);
				}
			}
		}// end docs loop

		sortTopTermsAndAddToCollocationsIndexForThisTerm(logger, phraseTerms);
	}

	private boolean populateHashMapWithPhraseTerms(Term term,
			int numDocsForTerm, int totalNumDocs, HashMap<String, CollocationScorer> phraseTerms,
			BitSet termPos, String[] terms, int j, boolean matchFound,
			int startpos, int endpos) throws IOException {
		for (int prevpos = startpos; (prevpos <= endpos) && (!matchFound); prevpos++) {
			if (termPos.get(prevpos)) {
				// Add term to hashmap containing co-occurence
				// counts for this term
				CollocationScorer pt = (CollocationScorer) phraseTerms.get(terms[j]);
				if (pt == null) {
					TermEnum otherTe = reader.terms(new Term(fieldName,
							terms[j]));
					int numDocsForOtherTerm = Math.min(otherTe.docFreq(),
							maxNumDocsToAnalyze);
					float otherPercent = (float) numDocsForOtherTerm
							/ (float) totalNumDocs;

					// check other term is not too rare or frequent
					if (otherPercent < minTermPopularity) {
						System.out.println(term.text() + " not poluar enough "
								+ otherPercent);

						matchFound = true;
						continue;
					}
					if (otherPercent > maxTermPopularity) {
						System.out.println(term.text() + " too popular "
								+ otherPercent);
						matchFound = true;
						continue;
					}

					pt = new CollocationScorer(term.text(), terms[j],
							numDocsForOtherTerm, numDocsForTerm);
					phraseTerms.put(pt.coincidentalTerm, pt);
				}
				pt.coIncidenceDocCount++;
				matchFound = true;
			}
		}
		return matchFound;
	}

	private int recordAllPositionsOfTheTermInCurrentDocumentBitset(Term term,
			BitSet termPos, TermPositionVector tpv, String[] terms) {
		// first record all of the positions of the term in a bitset which
		// represents terms in the current doc.
		int index = Arrays.binarySearch(terms, term.text());
		if (index >= 0) {
			int[] pos = tpv.getTermPositions(index);
			// remember all positions of the term in this doc
			for (int j = 0; j < pos.length; j++) {
				termPos.set(pos[j]);
			}
		}
		return index;
	}

	private void sortTopTermsAndAddToCollocationsIndexForThisTerm(
			CollocationIndexer collocationIndexer, HashMap<String, CollocationScorer> phraseTerms) throws IOException {
		TopTerms topTerms = new TopTerms(numCollocatedTermsPerTerm);
		for (CollocationScorer pt: phraseTerms.values()) {
			topTerms.insertWithOverflow(pt);
		}
		CollocationScorer[] tops = new CollocationScorer[topTerms.size()];
		int tp = tops.length - 1;
		while (topTerms.size() > 0) {
			CollocationScorer top = (CollocationScorer) topTerms.pop();
			tops[tp--] = top;
		}
		for (int j = 0; j < tops.length; j++) {
			collocationIndexer.indexCollocation(tops[j]);
		}
	}

	private void isTermTooPopularOrNotPopularEnough(Term term, float percent) {
		// check term is not too rare or frequent
		if (percent < minTermPopularity) {
			System.out.println(term.text() + " not popular enough " + percent);
			return;
		}
		if (percent > maxTermPopularity) {
			System.out.println(term.text() + " too popular " + percent);
			return;
		}
	}

	static class TopTerms extends PriorityQueue<Object> {
		public TopTerms(int size) {
			initialize(size);
		}

		protected boolean lessThan(Object a, Object b) {
			CollocationScorer pta = (CollocationScorer) a;
			CollocationScorer ptb = (CollocationScorer) b;
			return pta.getScore() < ptb.getScore();
		}
	}

	public static int getMaxNumDocsToAnalyze() {
		return maxNumDocsToAnalyze;
	}

	public static void setMaxNumDocsToAnalyze(int maxNumDocsToAnalyze) {
		CollocationExtractor.maxNumDocsToAnalyze = maxNumDocsToAnalyze;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public float getMaxTermPopularity() {
		return maxTermPopularity;
	}

	public void setMaxTermPopularity(float maxTermPopularity) {
		this.maxTermPopularity = maxTermPopularity;
	}

	public float getMinTermPopularity() {
		return minTermPopularity;
	}

	public void setMinTermPopularity(float minTermPopularity) {
		this.minTermPopularity = minTermPopularity;
	}

	public int getNumCollocatedTermsPerTerm() {
		return numCollocatedTermsPerTerm;
	}

	public void setNumCollocatedTermsPerTerm(int numCollocatedTermsPerTerm) {
		this.numCollocatedTermsPerTerm = numCollocatedTermsPerTerm;
	}

	public int getSlopSize() {
		return slopSize;
	}

	public void setSlopSize(int slopSize) {
		this.slopSize = slopSize;
	}
}