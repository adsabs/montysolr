package org.apache.solr.handler.dataimport;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.Before;

public class TestFacetHierarchy {
	
	HashMap<String,Object> testRow;

	@Before
	public void setUp() {
		final List<String> a = new ArrayList<String>() {{ add("foo"); add("bar"); add("baz"); }};
		final List<String> aa = new ArrayList<String>() {{ add("foo-foo"); add("bar-bar"); add("baz-baz"); }};
		
		final String b = "bleh";
		final String bb = "bleh-blah";
		
		final List<String> c = new ArrayList<String>() {{ add("Smith, J"); add("Jones, C"); }};
		final List<String> cc = new ArrayList<String>() {{ add("Smith, John"); add("Jones, Chuck"); }};
		
		final List<String> d = new ArrayList<String>() {{ add("foo"); add("bar"); }};
		final List<String> dd = new ArrayList<String>() {{ add("foo"); }};
		
		final List<String> e = new ArrayList<String>() {{ add("foo"); add("bar"); }};
		final List<String> ee = new ArrayList<String>() {{ add("foo"); add(""); }};
		
		final String f = "bar";
		final String ff = "";
		
		final List<String> n = null;
		final List<String> nn = null;
		
		testRow = new HashMap<String,Object>() {{
			put("a", a); 
			put("aa", aa);
			put("b", b);
			put("bb", bb);
			put("c", c);
			put("cc", cc);
			put("d", d);
			put("dd", dd);
			put("e", e);
			put("ee", ee);
			put("f", f);
			put("ff", ff);
			put("n", n);
			put("nn", nn);
		}};
	}
	
	@Test
	public void testFacetGeneration() {
		
	  // multi-valued
		FacetHierarchy fh = new FacetHierarchyMV("a_hier", new String[] {"a", "aa"});
		fh.addFacets(testRow);
		List<String> newFacets = (List<String>) testRow.get("a_hier");
		assertArrayEquals(
				new String[] { "0/foo","0/bar","0/baz","1/foo/foo-foo","1/bar/bar-bar","1/baz/baz-baz"},
				newFacets.toArray());
		
		// single valued
		fh = new FacetHierarchy("b_hier", new String[] {"b", "bb"});
		fh.addFacets(testRow);
		newFacets = (List<String>) testRow.get("b_hier");
		assertArrayEquals(
				new String[] { "0/bleh", "1/bleh/bleh-blah" },
				newFacets.toArray());
		
		// authors
		fh = new FacetHierarchyMV("c_hier", new String[] {"c", "cc"});
    fh.addFacets(testRow);
    newFacets = (List<String>) testRow.get("c_hier");
    assertArrayEquals(
        new String[] { "0/Smith, J", "0/Jones, C", "1/Smith, J/Smith, John", "1/Jones, C/Jones, Chuck" },
        newFacets.toArray());
    
    
    fh = new FacetHierarchy("author_hier", new String[] {"author"});
    testRow.clear();
    testRow.put("author", "Kurtz, Michael J.");
    fh.addFacets(testRow);
    newFacets = (List<String>) testRow.get("author_hier");
    assertArrayEquals(
        new String[] { "0/Kurtz, M", "0/Kurtz, M/Kurtz, Michael J."  },
        newFacets.toArray());
	}
	
	@Test(expected=ClassCastException.class)
	public void testFacetGenerationMixedMultiSingleValued() {
		FacetHierarchy fh = new FacetHierarchy("ab_hier", new String[] {"b", "aa"});
		fh.addFacets(testRow);
	}
	
	
	@Test
	public void testFacetGenerationBadInput() {
		
		FacetHierarchy fh = new FacetHierarchyMV("d_hier", new String[] {"d", "dd"});
		try {
			fh.addFacets(testRow);
		} catch (RuntimeException e) {
			assertEquals("source value arrays have inconsistent length", e.getMessage());
		}

		fh = new FacetHierarchyMV("n_hier", new String[] {"n", "nn"});
		try {
			fh.addFacets(testRow);
		} catch (RuntimeException e) {
			assertEquals("source was null", e.getMessage());
		}
		
		fh = new FacetHierarchyMV("e_hier", new String[] {"e", "ee"});
		try {
			fh.addFacets(testRow);
		} catch (RuntimeException e) {
			assertEquals("source value array contains empty strings", e.getMessage());
		}
		
		fh = new FacetHierarchy("e_hier", new String[] {"f", "ff"});
		try {
			fh.addFacets(testRow);
		} catch (RuntimeException e) {
			assertEquals("source value array contains empty strings", e.getMessage());
		}
	}
}
