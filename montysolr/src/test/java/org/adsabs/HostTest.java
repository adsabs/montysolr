package org.adsabs;

import org.apache.solr.security.AllowListUrlChecker;
import org.junit.Ignore;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;

public class HostTest {

    @Test
    @Ignore
    public void testHostParses() throws MalformedURLException {
        String host = "http://simbak.cfa.harvard.edu:80/adsnull/solr/collection1";
        AllowListUrlChecker checker = new AllowListUrlChecker(new ArrayList<>());

        checker.checkAllowList(Collections.singletonList(host));
    }
}
