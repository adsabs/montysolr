package org.apache.lucene.search;

/*
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

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.ToStringUtils;
import org.apache.lucene.util.Bits;

import java.util.BitSet;
import java.util.Set;
import java.io.IOException;

/**
 * A query that matches all documents and filters them using a bitset filter
 *
 */
public class BitSetQuery extends Query {

	private BitSet seekedDocs;

	public BitSetQuery(BitSet docs) {
		super();
		seekedDocs = docs;
	}

	private class MatchAllScorer extends Scorer {
		final float score;
		private int doc = -1;
		private int targetDoc = -1;
		private final int maxDoc;
		private final Bits liveDocs;
		private final BitSet seekedDocs;
		private int docBase;

		MatchAllScorer(IndexReader reader, Bits liveDocs, Weight w, float score, BitSet seekedDocs, int docBase) {
			super(w);
			this.liveDocs = liveDocs;
			this.score = score;
			maxDoc = reader.maxDoc();
			this.seekedDocs = seekedDocs;
			this.docBase = docBase;
			this.targetDoc = docBase;
		}

		@Override
		public int docID() {
			return doc;
		}

		@Override
		public int nextDoc() throws IOException {
			while (true) {
				targetDoc = seekedDocs.nextSetBit(targetDoc);
				if (targetDoc == -1 || targetDoc-docBase >= maxDoc) {
					doc = NO_MORE_DOCS;
					return doc;
				}

				doc = targetDoc - docBase;
				targetDoc++;

				if(liveDocs != null && doc < maxDoc && !liveDocs.get(doc)) {
					continue;
				}

				return doc;
			}
		}

		@Override
		public float score() {
			return score;
		}

		@Override
		public float freq() {
			return 1;
		}

		@Override
		public int advance(int target) throws IOException {
			doc = target-1;
			return nextDoc();
		}
	}

	private class MatchAllDocsWeight extends Weight {
		private float queryWeight;
		private float queryNorm;

		public MatchAllDocsWeight(IndexSearcher searcher) {
		}

		@Override
		public String toString() {
			return "weight(" + BitSetQuery.this + ")";
		}

		@Override
		public Query getQuery() {
			return BitSetQuery.this;
		}

		@Override
		public float getValueForNormalization() {
			queryWeight = getBoost();
			return queryWeight * queryWeight;
		}

		@Override
		public void normalize(float queryNorm, float topLevelBoost) {
			this.queryNorm = queryNorm * topLevelBoost;
			queryWeight *= this.queryNorm;
		}

		@Override
		public Scorer scorer(AtomicReaderContext context, boolean scoreDocsInOrder,
				boolean topScorer, Bits acceptDocs) throws IOException {
			return new MatchAllScorer(context.reader(), acceptDocs, this, queryWeight, seekedDocs, context.docBase);
		}

		@Override
		public Explanation explain(AtomicReaderContext context, int doc) {
			// explain query weight
			Explanation queryExpl = new ComplexExplanation
			(true, queryWeight, "BitSetQuery, product of:");
			if (getBoost() != 1.0f) {
				queryExpl.addDetail(new Explanation(getBoost(),"boost"));
			}
			queryExpl.addDetail(new Explanation(queryNorm,"queryNorm"));

			return queryExpl;
		}
	}

	@Override
	public Weight createWeight(IndexSearcher searcher) {
		return new MatchAllDocsWeight(searcher);
	}

	@Override
	public void extractTerms(Set<Term> terms) {
	}

	@Override
	public String toString(String field) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("BitSetQuery(");
		buffer.append("size=" + seekedDocs.cardinality());
		buffer.append(")");
		buffer.append(ToStringUtils.boost(getBoost()));
		return buffer.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof MatchAllDocsQuery))
			return false;
		MatchAllDocsQuery other = (MatchAllDocsQuery) o;
		return this.getBoost() == other.getBoost();
	}

	@Override
	public int hashCode() {
		return Float.floatToIntBits(getBoost()) ^ 0x1AA71190;
	}
}
