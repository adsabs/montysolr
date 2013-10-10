package org.apache.solr.handler.batch;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.params.SolrParams;

public class BatchHandlerRequestQueue {
	Map<String, BatchHandlerRequestData>tbdQueue = Collections.synchronizedMap(new LinkedHashMap<String, BatchHandlerRequestData>());
	Map<String, BatchHandlerRequestData>failedQueue = Collections.synchronizedMap(new LinkedHashMap<String, BatchHandlerRequestData>());
	Integer queuedIn = 0;
	Integer queuedOut = 0;
	Map<String, Integer> jobs = Collections.synchronizedMap(new LinkedHashMap<String, Integer>());

	private volatile boolean stopped;

	public BatchHandlerRequestData getNext() {
		for (Entry e: tbdQueue.entrySet()) {
			return tbdQueue.get(e.getKey());
		}
		return null;
	}
	public BatchHandlerRequestData pop() {
		for (Entry e: tbdQueue.entrySet()) {
			queuedOut++;
			return tbdQueue.remove(e.getKey());
		}
		return null;
	}

	public void registerFailedBatch(BatchProvider provider, BatchHandlerRequestData data) {
		String jobid = data.getReqParams().get("jobid");

		if (!failedQueue.containsKey(data.url)) {
			BatchHandlerRequestData rd = new BatchHandlerRequestData(provider, data.getReqParams());
			failedQueue.put(rd.url, rd);
		}
		jobs.put(jobid, -jobs.get(jobid));
	}

	public void registerNewBatch(BatchProvider handler, SolrParams params)  {
		String jobid = params.get("jobid");
		if (isJobidFailed(jobid)) {
			throw new SolrException(ErrorCode.BAD_REQUEST, "A job with jobid=" + jobid + " is marked as failed, I denounce obedience");
		}

		BatchHandlerRequestData rd = new BatchHandlerRequestData(handler, params);
		if (!tbdQueue.containsKey(rd.url)) {
			queuedIn++;
			tbdQueue.put(rd.url, rd);
			increaseJobCounter(jobid);
		}
	}

	public void registerFinishedBatch(BatchHandlerRequestData data) {
		String jobid = data.getReqParams().get("jobid");
		decreaseJobCounter(jobid);
		assert jobs.get(jobid) >= 0; // should never happen
	}

	public boolean hasMore() {
		return tbdQueue.size() > 0 && stopped==false;
	}

	public void stop() {
		stopped = true;
	}

	public void start() {
		stopped = false;
	}

	public void reset() {
		tbdQueue.clear();
		failedQueue.clear();
		queuedIn = 0;
		queuedOut = 0;
	}


	public void reInsert(BatchHandlerRequestData rd) {
		if (!tbdQueue.containsKey(rd.url)) {
			queuedIn++;
			tbdQueue.put(rd.url, rd);
		}
	}

	public boolean isStopped() {
		return stopped;
	}
	public boolean isJobidRegistered(String jobid) {
		return jobs.containsKey(jobid);
	}
	public boolean isJobidFinished(String jobid) {
		return jobs.containsKey(jobid) && jobs.get(jobid) == 0;
	}
	public boolean isJobidFailed(String jobid) {
		return jobs.containsKey(jobid) && jobs.get(jobid) < 0;
	}
	public boolean isJobidRunning(String jobid) {
		return jobs.containsKey(jobid) && jobs.get(jobid) > 0;
	}
	private void increaseJobCounter(String jobid) {
		jobs.put(jobid, jobs.get(jobid)!=null ? jobs.get(jobid)+1 : 1);
	}
	public void decreaseJobCounter(String jobid) {
		jobs.put(jobid, jobs.get(jobid)-1);
	}
}
