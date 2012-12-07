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
	public void testFacetGenerationMultiValued() {
		
		FacetHierarchy fh = new FacetHierarchy("a_hier", new String[] {"a", "aa"}, true);
		fh.addFacets(testRow);
		List<String> newFacets = (List<String>) testRow.get("a_hier");
		assertArrayEquals(
				new String[] { "0/foo","0/bar","0/baz","1/foo/foo-foo","1/bar/bar-bar","1/baz/baz-baz"},
				newFacets.toArray());
	}
	
	@Test
	public void testFacetGenerationSingleValued() {
		FacetHierarchy fh = new FacetHierarchy("b_hier", new String[] {"b", "bb"}, false);
		fh.addFacets(testRow);
		List<String> newFacets = (List<String>) testRow.get("b_hier");
		assertArrayEquals(
				new String[] { "0/bleh", "1/bleh/bleh-blah" },
				newFacets.toArray());
	}
	
	@Test(expected=ClassCastException.class)
	public void testFacetGenerationMixedMultiSingleValued() {
		FacetHierarchy fh = new FacetHierarchy("ab_hier", new String[] {"b", "aa"}, false);
		fh.addFacets(testRow);
	}
	
	@Test
	public void testFacetGenerationAuthors() {
		FacetHierarchy fh = new FacetHierarchy("c_hier", new String[] {"c", "cc"}, true);
		fh.addFacets(testRow);
		List<String> newFacets = (List<String>) testRow.get("c_hier");
		assertArrayEquals(
				new String[] { "0/Smith, J", "0/Jones, C", "1/Smith, J/Smith, John", "1/Jones, C/Jones, Chuck" },
				newFacets.toArray());
	}
	
	@Test
	public void testFacetGenerationBadInput() {
		
		FacetHierarchy fh = new FacetHierarchy("d_hier", new String[] {"d", "dd"}, true);
		try {
			fh.addFacets(testRow);
		} catch (RuntimeException e) {
			assertEquals("source value arrays have inconsistent length", e.getMessage());
		}
	}	
	
	@Test
	public void testFacetGenerationBadInput2() {
		FacetHierarchy fh = new FacetHierarchy("n_hier", new String[] {"n", "nn"}, true);
		try {
			fh.addFacets(testRow);
		} catch (RuntimeException e) {
			assertEquals("source was null", e.getMessage());
		}
	}
	
	@Test
	public void testFacetGenerationBadInput3() {
		FacetHierarchy fh = new FacetHierarchy("e_hier", new String[] {"e", "ee"}, true);
		try {
			fh.addFacets(testRow);
		} catch (RuntimeException e) {
			assertEquals("source value array contains empty strings", e.getMessage());
		}
	}
	
	@Test
	public void testFacetGenerationBadInput4() {
		FacetHierarchy fh = new FacetHierarchy("e_hier", new String[] {"f", "ff"}, false);
		try {
			fh.addFacets(testRow);
		} catch (RuntimeException e) {
			assertEquals("source value array contains empty strings", e.getMessage());
		}
	}
}
