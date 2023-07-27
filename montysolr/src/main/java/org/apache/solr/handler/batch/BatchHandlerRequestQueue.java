package org.apache.solr.handler.batch;

import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.params.SolrParams;

import java.util.*;
import java.util.Map.Entry;

public class BatchHandlerRequestQueue {
    private final Map<String, BatchHandlerRequestData> tbdQueue = Collections.synchronizedMap(new LinkedHashMap<String, BatchHandlerRequestData>());
    private final Map<String, BatchHandlerRequestData> failedQueue = Collections.synchronizedMap(new LinkedHashMap<String, BatchHandlerRequestData>());
    private Integer queuedIn = 0;
    private Integer queuedOut = 0;
    private final Map<String, Integer> jobs = Collections.synchronizedMap(new LinkedHashMap<String, Integer>());
    private final Map<String, String> failedJobs = Collections.synchronizedMap(new LinkedHashMap<String, String>());

    private volatile boolean stopped;

    public BatchHandlerRequestData getNext() {
        for (Entry e : tbdQueue.entrySet()) {
            return tbdQueue.get(e.getKey());
        }
        return null;
    }

    public BatchHandlerRequestData pop() {
        for (Entry e : tbdQueue.entrySet()) {
            queuedOut++;
            return tbdQueue.remove(e.getKey());
        }
        return null;
    }

    public void registerFailedBatch(BatchProvider provider, BatchHandlerRequestData data) {
        String jobid = data.getReqParams().get("jobid");

        if (!failedQueue.containsKey(data.url)) {
            BatchHandlerRequestData rd = new BatchHandlerRequestData(provider, data.getReqParams());
            rd.setMsg(data.getMsg());
            failedQueue.put(rd.url, rd);
            failedJobs.put(jobid, rd.url);
        }
        jobs.put(jobid, -jobs.get(jobid));
    }

    public void registerNewBatch(BatchProvider handler, SolrParams params) {
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
        return tbdQueue.size() > 0 && !stopped;
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
        jobs.put(jobid, jobs.get(jobid) != null ? jobs.get(jobid) + 1 : 1);
    }

    public void decreaseJobCounter(String jobid) {
        jobs.put(jobid, jobs.get(jobid) - 1);
    }

    public int getTbdQueueSize() {
        return tbdQueue.size();
    }

    public int getFailedQueueSize() {
        return failedQueue.size();
    }

    public int getTotalQueueSize() {
        return queuedIn;
    }

    public int getTotalFinishedSize() {
        return queuedOut;
    }

    public List<String> getQueueDetails(int howMany, int type) {
        ArrayList<String> out = new ArrayList<String>();
        Map<String, BatchHandlerRequestData> queue = tbdQueue;
        if (type == 0) {
            queue = failedQueue;
        }
        int i = 0;
        for (BatchHandlerRequestData d : queue.values()) {
            out.add(d.toString());
            i++;
            if (i >= howMany) {
                break;
            }
        }
        return out;
    }

    public Object getErrorMessage(String jobid) {
        if (!failedJobs.containsKey(jobid)) {
            return "No message for jobid: " + jobid;
        }
        return failedQueue.get(failedJobs.get(jobid)).getMsg();
    }
}
