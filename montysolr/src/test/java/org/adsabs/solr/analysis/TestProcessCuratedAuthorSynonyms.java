package org.adsabs.solr.analysis;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class TestProcessCuratedAuthorSynonyms {

    @Test
    public void testParseGroups() {
        String inputString = "Stern, Carolyn\nGrant, Carolyn\n\nMiller, Bill\nMiller, William\n";
        InputStream is = new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_8));
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(is));
        ArrayList<List<String>> groups = ProcessCuratedAuthorSynonyms.parseGroups(inputReader);
        ArrayList<List<String>> expected = new ArrayList<List<String>>() {{
            add(new ArrayList<String>() {{
                add("Stern, Carolyn");
                add("Grant, Carolyn");
            }});
            add(new ArrayList<String>() {{
                add("Miller, Bill");
                add("Miller, William");
            }});
        }};
        assertEquals(groups, expected);
    }

    @Test
    public void testTransformGroup1() {
        List<String> group = new ArrayList<String>() {{
            add("STERN, CAROLYN");
            add("GRANT, CAROLYN");
        }};
        HashMap<String, HashSet<String>> transformed = ProcessCuratedAuthorSynonyms.transformGroup(group);
        HashMap<String, HashSet<String>> expected = new HashMap<String, HashSet<String>>() {{
            put("STERN, CAROLYN", new HashSet<String>() {{
                addAll(Arrays.asList("GRANT, C", "GRANT, CAROLYN\\b.*", "GRANT,", "GRANT, CAROLYN"));
            }});
            put("GRANT, CAROLYN", new HashSet<String>() {{
                addAll(Arrays.asList("STERN, C", "STERN, CAROLYN", "STERN, CAROLYN\\b.*", "STERN,"));
            }});
        }};
        assertEquals(expected, transformed);
    }

    @Test
    public void testTransformGroup2() {
        List<String> group = new ArrayList<String>() {{
            add("MÜLLER, WILLIAM");
            add("MÜLLER, BILL");
        }};
        HashMap<String, HashSet<String>> transformed = ProcessCuratedAuthorSynonyms.transformGroup(group);
        HashMap<String, HashSet<String>> expected = new HashMap<String, HashSet<String>>() {{
            put("MUELLER, BILL", new HashSet<String>() {{
                addAll(Arrays.asList("MÜLLER,", "MUELLER, WILLIAM\\b.*", "MULLER, BILL", "MUELLER, WILLIAM", "MULLER, B", "MULLER, WILLIAM\\b.*", "MÜLLER, WILLIAM\\b.*", "MÜLLER, W", "MULLER, BILL\\b.*", "MUELLER, W", "MULLER, WILLIAM", "MUELLER,", "MÜLLER, BILL\\b.*", "MÜLLER, BILL", "MULLER,", "MULLER, W", "MÜLLER, B", "MÜLLER, WILLIAM"));
            }});
            put("MULLER, BILL", new HashSet<String>() {{
                addAll(Arrays.asList("MUELLER, BILL", "MÜLLER,", "MUELLER, WILLIAM\\b.*", "MUELLER, B", "MÜLLER, W", "MUELLER, WILLIAM", "MÜLLER, WILLIAM", "MULLER, WILLIAM\\b.*", "MÜLLER, WILLIAM\\b.*", "MUELLER, W", "MULLER, WILLIAM", "MUELLER,", "MÜLLER, BILL\\b.*", "MÜLLER, BILL", "MULLER,", "MULLER, W", "MÜLLER, B", "MUELLER, BILL\\b.*"));
            }});
            put("MUELLER, WILLIAM", new HashSet<String>() {{
                addAll(Arrays.asList("MUELLER, BILL", "MÜLLER,", "MUELLER, B", "MULLER, BILL", "MULLER, B", "MULLER, WILLIAM\\b.*", "MÜLLER, WILLIAM\\b.*", "MÜLLER, W", "MULLER, BILL\\b.*", "MÜLLER, WILLIAM", "MULLER, WILLIAM", "MUELLER,", "MÜLLER, BILL\\b.*", "MÜLLER, BILL", "MULLER,", "MULLER, W", "MÜLLER, B", "MUELLER, BILL\\b.*"));
            }});
            put("MULLER, WILLIAM", new HashSet<String>() {{
                addAll(Arrays.asList("MUELLER, BILL", "MÜLLER,", "MUELLER, B", "MULLER, BILL", "MUELLER, WILLIAM", "MULLER, B", "MUELLER, WILLIAM\\b.*", "MÜLLER, WILLIAM\\b.*", "MÜLLER, W", "MULLER, BILL\\b.*", "MUELLER, W", "MÜLLER, WILLIAM", "MUELLER,", "MÜLLER, BILL\\b.*", "MÜLLER, BILL", "MULLER,", "MÜLLER, B", "MUELLER, BILL\\b.*"));
            }});
            put("MÜLLER, BILL", new HashSet<String>() {{
                addAll(Arrays.asList("MUELLER, BILL", "MÜLLER,", "MUELLER, WILLIAM\\b.*", "MUELLER, B", "MULLER, BILL", "MUELLER, WILLIAM", "MÜLLER, WILLIAM", "MULLER, B", "MULLER, WILLIAM\\b.*", "MÜLLER, WILLIAM\\b.*", "MÜLLER, W", "MULLER, BILL\\b.*", "MUELLER, W", "MULLER, WILLIAM", "MUELLER,", "MULLER,", "MULLER, W", "MUELLER, BILL\\b.*"));
            }});
            put("MÜLLER, WILLIAM", new HashSet<String>() {{
                addAll(Arrays.asList("MUELLER, BILL", "MÜLLER,", "MUELLER, WILLIAM\\b.*", "MUELLER, B", "MULLER, BILL", "MUELLER, WILLIAM", "MULLER, B", "MULLER, WILLIAM\\b.*", "MULLER, BILL\\b.*", "MUELLER, W", "MULLER, WILLIAM", "MUELLER,", "MÜLLER, BILL\\b.*", "MÜLLER, BILL", "MULLER,", "MULLER, W", "MÜLLER, B", "MUELLER, BILL\\b.*"));
            }});
        }};

        assertEquals(expected, transformed);
    }
}
