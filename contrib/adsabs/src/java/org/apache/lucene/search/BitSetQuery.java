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

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.AssertingScorer.IteratorState;
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
	private String uuid;

	public BitSetQuery(BitSet docs) {
		super();
		seekedDocs = docs;
	}

	private class MatchAllScorer extends Scorer {
	  final int maxDoc;
	  final Bits liveDocs;
		final float score;
		final BitSet seekedDocs;
		int docBase;
    private DocIdSetIterator it;

		MatchAllScorer(IndexReader reader, Bits liveDocs, Weight w, float score, BitSet seekedDocs, int docBase) {
			super(w);
			this.liveDocs = liveDocs;
			this.score = score;
			this.seekedDocs = seekedDocs;
			this.docBase = docBase;
			this.maxDoc = reader.maxDoc();
		}


		@Override
		public float score() {
			return score;
		}

		@Override
		public int freq() {
			return 1;
		}


    @Override
    public DocIdSetIterator iterator() {
      
      // arghhh! they made so many changes but never removed the Scorer.docId
      // so we are, like idiots, forced to keep the iterator hanging around. WTF?!!
      
      it = new DocIdSetIterator() {
        
        int doc = -1;
        int targetDoc = -1;
        
        @Override
        public int advance(int target) throws IOException {
          doc = target-1;
          return nextDoc();
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
        public long cost() {
          return seekedDocs.cardinality();
        }
      };
      
      return it;
    }


    @Override
    public int docID() {
      return it.docID();
    }
	}

	private class MatchAllDocsWeight extends Weight {
		private float queryWeight;
		private float queryNorm;

		public MatchAllDocsWeight(IndexSearcher searcher, Query query) {
		  super(query);
		}

		@Override
		public String toString() {
			return "weight(" + BitSetQuery.this + ")";
		}

		@Override
		public float getValueForNormalization() {
			return 1.0f;
		}

		@Override
		public void normalize(float queryNorm, float topLevelBoost) {
			this.queryNorm = queryNorm * topLevelBoost;
			queryWeight *= this.queryNorm;
		}


		@Override
		public Explanation explain(LeafReaderContext context, int doc) {
			return Explanation.match(1.0f, "bitset query match");
		}

    @Override
    public void extractTerms(Set<Term> terms) {
      // do nothing
    }

    @Override
    public Scorer scorer(LeafReaderContext context) throws IOException {
      return new MatchAllScorer(context.reader(), context.reader().getLiveDocs(),
          this, queryWeight, seekedDocs, context.docBase);
    }
	}

	@Override
	public Weight createWeight(IndexSearcher searcher, boolean needsScore) {
		return new MatchAllDocsWeight(searcher, this);
	}


	@Override
	public String toString(String field) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("BitSetQuery(");
		buffer.append("size=" + seekedDocs.cardinality());
		buffer.append(")");
		return buffer.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof MatchAllDocsQuery))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		if (uuid != null) {
			return uuid.hashCode();
		}
		return seekedDocs.hashCode() ^ 0x1AA71190;
	}
	
	public void setUUID(String uuid) {
		this.uuid = uuid;
	}
	
	public String getUUID(String uuid) {
		return uuid;
	}
}
