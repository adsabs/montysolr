package org.apache.solr.handler.batch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;

public class BatchProviderDumpTermFreqs extends BatchProvider {
	public void run(SolrQueryRequest req, BatchHandlerRequestQueue queue) throws Exception {

		SolrCore core = req.getCore();
		SolrParams params = req.getParams();
		IndexSchema schema = core.getSchema();
	  String jobid = params.get("jobid");
	  String workDir = params.get("#workdir");
	  
		final HashSet<String> fieldsToLoad = new HashSet<String>();

		String[] fields = params.getParams("fields");
		for (String f: fields) {
			for (String ff: f.split("( |,)")) {
				SchemaField field = schema.getFieldOrNull(ff);
				if (field==null || !field.indexed()) {
					throw new SolrException(ErrorCode.BAD_REQUEST, "We cannot dump fields that do not exist or are not indexed: " + ff);
				}
				fieldsToLoad.add(ff);
			}
		}

		File jobFile = new File(workDir + "/" + params.get("jobid"));
		final BufferedWriter out = new BufferedWriter(new FileWriter(jobFile), 1024*256);
		out.write("term");
		out.write("\t");
		out.write("termFreq");
		out.write("\t");
		out.write("docFreq");

		DirectoryReader ir = req.getSearcher().getIndexReader();
		TermsEnum reuse = null;
		int processed = 0;
		for (String f: fieldsToLoad) {

			out.write("\n\n# " + f + "\n");

			Terms te = MultiFields.getTerms(ir, f);
			if (te == null) {
				out.write("# term stats is not available for this field");
				continue;
			}
			reuse = te.iterator(reuse);

			BytesRef term;
			while((term = reuse.next()) != null) {
				out.write(term.utf8ToString());
				out.write("\t");
				out.write(Long.toString(reuse.totalTermFreq()));
				out.write("\t");
				out.write(Long.toString(reuse.docFreq()));
				out.write("\n");

				processed++;
				if (processed % 10000 == 0) {
					if(queue.isStopped()) { // inside, because queue is synchronized
						throw new IOException("Collector interrupted - stopping");
					}
				}
			}
		}
		out.close();

	}
}
