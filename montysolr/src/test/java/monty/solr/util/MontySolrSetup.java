package monty.solr.util;


import java.io.File;


/**
 * Automatic setup for Test classes, do not use this in a production
 * environment! (unless you are really too lazy ;))
 *
 * @author rchyla
 */


public class MontySolrSetup {
    /**
     * Returns the root folder of montysolr
     *
     * @return String
     */
    public static String getMontySolrHome() {
        File base = new File(System.getProperty("user.dir"));
        while (!new File(base, "montysolr/montysolr.md").exists()) {
            base = base.getParentFile();
        }
        return base.getAbsolutePath().replace('\\', '/');
    }

}
