package org.adsabs.solr.metrics.memory;

import com.sun.management.ThreadMXBean;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.handler.component.SearchComponent;

import java.io.IOException;
import java.lang.management.ManagementFactory;

public class RequestMemoryEndComponent extends SearchComponent {
    private static final ThreadMXBean THREAD_MX_BEAN =
            (ThreadMXBean) ManagementFactory.getThreadMXBean();

    @Override
    public void prepare(ResponseBuilder rb) throws IOException {
        // no-op
    }

    @Override
    public void process(ResponseBuilder rb) throws IOException {
        Long startAlloc = (Long) rb.req.getContext().get(RequestMemoryStartComponent.ALLOC_START_KEY);
        if (startAlloc == null) return;

        long allocated = THREAD_MX_BEAN.getCurrentThreadAllocatedBytes() - startAlloc;
        rb.rsp.add("allocatedBytes", allocated);
        rb.rsp.addHttpHeader("X-Allocated-Bytes", String.valueOf(allocated));
    }

    @Override
    public String getDescription() {
        return "Reports allocation delta at the end of a request.";
    }
}
