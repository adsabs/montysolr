package org.apache.solr.handler.dataimport;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.Before;

public class TestFacetHierarchy {
	

	@Before
	public void setUp() {
		
	}
	
	private void doTestNormalType(Object[] input, String...expected) {
		doTest(false, input, expected);
	}
	
	private void doTestJoinType(Object[] input, String...expected) {
		doTest(true, input, expected);
	}
	
	private void doTest(Boolean isJoinType, Object[] input, String...expected) {
		
		HashMap<String, Object> testRow = new HashMap<String, Object>();
		testRow.put("id", 1);
		String[] inputFields = new String[input.length];
		for (int i=0; i<input.length; i++) {
			testRow.put("input" + i, input[i]);
			inputFields[i] = "input" + i;
		}
		
		if (isJoinType) {
			FacetHierarchy fh = new FacetHierarchyMV("output", inputFields);
			fh.addFacets(testRow);
		}
		else {
			FacetHierarchy fh = new FacetHierarchy("output", inputFields);
			fh.addFacets(testRow);
		}
		
		
		List<String> result = (List<String>) testRow.get("output");
		if (expected == null) {
			assertTrue("The facets were generated", result == null);
		}
		else {
			assertTrue("The facets were not generated", result != null);
			assertArrayEquals(
					expected,
					result.toArray());
		}
		
	}
	
	@Test
	public void testFacetGeneration() {
		
		// test single-string inut
		doTestNormalType(
				new Object[] {"Kurtz, Michael J"},
				"0/Kurtz, M", "1/Kurtz, M/Kurtz, Michael J"
				);
		
		// test multi-valued field
		doTestNormalType(
				new Object[] {Arrays.asList("Kurtz, Michael J")},
				"0/Kurtz, M", "1/Kurtz, M/Kurtz, Michael J"
				);
		doTestNormalType(
				new Object[] {Arrays.asList("Kurtz, Michael. J.")},
				"0/Kurtz, M", "1/Kurtz, M/Kurtz, Michael J"
				);
		doTestNormalType(
				new Object[] {Arrays.asList("Kurtz, M        J")},
				"0/Kurtz, M", "1/Kurtz, M/Kurtz, M J"
				);
		doTestNormalType(
				new Object[] {Arrays.asList("t' Hooft, van X")},
				"0/T Hooft, V", "1/T Hooft, V/T Hooft, Van X"
				);
		// we want to have him in the second level too
		doTestNormalType(
				new Object[] {Arrays.asList("Moser, B")},
				"0/Moser, B", "1/Moser, B/Moser, B"
				);
		
		// now test value concatenation
		doTestJoinType(
				new Object[] {Arrays.asList("foo", "bar"), Arrays.asList("woo", "too")},
				"0/foo", "1/foo/woo", "0/bar", "1/bar/too"
				);
		doTestJoinType(
				new Object[] {Arrays.asList("foo", "bar"), Arrays.asList("woo", "too"), Arrays.asList("zap", "tap")},
				"0/foo", "1/foo/woo", "2/foo/woo/zap",
				"0/bar", "1/bar/too", "2/bar/too/tap"
				);
		
		// unbalanced case, should get at least st
		doTestJoinType(
				new Object[] {Arrays.asList("foo", "bar", "baz"), Arrays.asList("woo", "too")},
				"0/foo", "1/foo/woo", "0/bar", "1/bar/too"
				);
		doTestJoinType(
				new Object[] {Arrays.asList("foo", "bar"), Arrays.asList("woo")},
				"0/foo", "1/foo/woo"
				);
		doTestJoinType(
				new Object[] {Arrays.asList("foo", "bar"), null},
				null
				);
		doTestJoinType(
				new Object[] {Arrays.asList("foo", "bar"), null, Arrays.asList("woo", "too")},
				null
				);
		doTestJoinType(
				new Object[] {Arrays.asList("foo", "bar"), Arrays.asList("woo", "too"), Arrays.asList("zap" )},
				"0/foo", "1/foo/woo", "2/foo/woo/zap"
				);
		
	}
	
	
}
