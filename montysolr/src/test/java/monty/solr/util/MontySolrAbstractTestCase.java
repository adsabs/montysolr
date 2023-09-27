package monty.solr.util;

import org.apache.commons.io.FileUtils;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.servlet.DirectSolrConnection;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class MontySolrAbstractTestCase extends SolrTestCaseJ4 {

    @BeforeClass
    public static void beforeClassMontySolrTestCase() throws Exception {
        envInit();
    }

    @AfterClass
    public static void afterClassMontySolrTestCase() throws Exception {
    }

    protected List<File> tempFiles;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        tempFiles = new ArrayList<File>();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        for (File f : tempFiles) {
            Files.delete(f.toPath());
        }
    }

    /**
     * This must be called first, so that we make sure the Python
     * interpreter is loaded
     */
    public static void envInit() throws Exception {
        //System.setProperty("storeAll", "true");

    }

    /**
     * @see MontySolrTestCaseJ4#getFile
     */
    public static File getFile(String name) {
        return MontySolrTestCaseJ4.getFile(name);
    }


    public EmbeddedSolrServer getEmbeddedServer() {
        return new EmbeddedSolrServer(h.getCoreContainer(), h.getCore().getName());
    }

    public DirectSolrConnection getDirectServer() {
        return new DirectSolrConnection(h.getCore());
    }

    public static File createTempFile(String... lines) throws IOException {
        File tmpFile = File.createTempFile("montySolr-unittest", null);
        if (lines.length > 0) {
            //FileOutputStream fi = FileUtils.openOutputStream(tmpFile);
            StringBuffer out = new StringBuffer();
            for (String l : lines) {
                out.append(l + "\n");
            }
            FileUtils.writeStringToFile(tmpFile, out.toString(), "UTF-8");
        }
        return tmpFile;
    }

    public static File duplicateFile(File origFile) throws IOException {
        Path tmpFile = createTempFile();
        FileUtils.copyFile(origFile, tmpFile.toFile());
        return tmpFile.toFile();
    }

    public static int replaceInFile(File target, String toFind, String replacement) throws IOException {
        return replaceInFile(target, Pattern.compile(toFind), replacement);
    }

    public static int replaceInFile(File target, Pattern toFind, String replacement) throws IOException {
        int matches = 0;
        String contents = FileUtils.readFileToString(target);
        Matcher matcher = toFind.matcher(contents);
        while (matcher.find()) {
            matches++;
        }
        matcher.reset();
        contents = matcher.replaceAll(replacement);
        FileUtils.writeStringToFile(target, contents);
        return matches;
    }

    public static File duplicateModify(File sourceFile, String... searchReplace) {
        assert searchReplace.length % 2 == 0;
        File newFile;
        try {
            newFile = duplicateFile(sourceFile);
            for (int i = 0; i < searchReplace.length; i = i + 2) {
                replaceInFile(newFile, searchReplace[i], searchReplace[i + 1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
        return newFile;
    }

}
