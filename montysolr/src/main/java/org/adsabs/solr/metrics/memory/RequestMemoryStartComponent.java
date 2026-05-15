package org.adsabs.solr.metrics.memory;

import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.handler.component.SearchComponent;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import com.sun.management.ThreadMXBean;

public class RequestMemoryStartComponent extends SearchComponent {
    static final String ALLOC_START_KEY = "allocation.tracking.start";
    private static final ThreadMXBean THREAD_MX_BEAN =
            (ThreadMXBean) ManagementFactory.getThreadMXBean();

    static {
        THREAD_MX_BEAN.setThreadAllocatedMemoryEnabled(true);
    }

    @Override
    public void prepare(ResponseBuilder rb) throws IOException {
        rb.req.getContext().put(ALLOC_START_KEY,
                THREAD_MX_BEAN.getCurrentThreadAllocatedBytes());
    }

    @Override
    public void process(ResponseBuilder rb) throws IOException {
        // no-op
    }

    @Override
    public String getDescription() {
        return "Records allocated bytes at the start of a request.";
    }
}
