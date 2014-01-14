package org.apache.solr.handler.dataimport;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.Before;

@SuppressWarnings({ "serial", "unchecked","rawtypes" })
public class TestFacetHierarchy {
	

	@Before
	public void setUp() {
		
	}
	
	private void doTestNormalType(Object[] input, String...expected) {
		doTest(null, false, input, expected);
	}
	
	private void doTestJoinType(Object[] input, String...expected) {
		doTest(null, true, input, expected);
	}
	
	private void doGenericTest(String fieldsDefinition, Map input, String...expected) {
		HashMap<String, Object> testRow = new HashMap<String, Object>();
		testRow.put("id", 1);
		testRow.putAll(input);
		
		FacetHierarchy fh = _getFacetTransformer(fieldsDefinition);
		fh.addFacets(testRow);
		//System.out.println(testRow);
		_compareResults(testRow, expected);
	}
	
	private void doTest(String[] inputFields, Boolean isJoinType, Object[] input, String...expected) {
		
		HashMap<String, Object> testRow = new HashMap<String, Object>();
		testRow.put("id", 1);
		if (inputFields == null) {
			inputFields = new String[input.length];
			for (int i=0; i<input.length; i++) {
				testRow.put("input" + i, input[i]);
				inputFields[i] = "input" + i;
			}
		}
		else {
			for (int i=0; i<input.length; i++) {
				testRow.put(inputFields[i], input[i]);
			}
		}
		
		StringBuffer fieldsDefinition = new StringBuffer();
		boolean firstPassed = false;
		for (String f: inputFields) {
			fieldsDefinition.append(firstPassed ? (isJoinType ? "+" : ",") : "");
			fieldsDefinition.append(f);
			firstPassed = true;
		}
		
		FacetHierarchy fh = _getFacetTransformer(fieldsDefinition.toString());
		fh.addFacets(testRow);
		//System.out.println(testRow);
		_compareResults(testRow, expected);
		
	}
	
	private FacetHierarchy _getFacetTransformer(String facetDefinition) {
		FacetHierarchy fh = null;
		if (facetDefinition.contains("+")) {
			fh = new FacetHierarchyMV("output", facetDefinition.split("\\+"));
		} else {
			fh = new FacetHierarchy("output", facetDefinition.split("\\,"));
		}
		return fh;
	}
	
	private void _compareResults(HashMap<String, Object> testRow,
      String[] expected) {
		List<String> result = (List<String>) testRow.get("output");
		if (expected == null) {
			assertTrue("The facets were generated", result == null);
		}
		else if (expected.length == 0) {
			assertTrue("The facets were generated", result == null || result.toArray().length == expected.length);
		}
		else {
			assertTrue("The facets were not generated", result != null);
			//System.out.println(result);
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
				new String[]{}
				);
		doTestJoinType(
				new Object[] {Arrays.asList("foo", "bar"), null, Arrays.asList("woo", "too")},
				new String[]{}
				);
		doTestJoinType(
				new Object[] {Arrays.asList("foo", "bar"), Arrays.asList("woo", "too"), Arrays.asList("zap" )},
				"0/foo", "1/foo/woo", "2/foo/woo/zap"
				);
		
		// nested values
		doGenericTest(
				"input:key1+input:key2",
				new HashMap() {{
					put("input", new ArrayList() {{
						add(new HashMap<String, String>(){{put("key1","foo");put("key2","bar");}});
					}}); 
				}},
				"0/foo", "1/foo/bar"
				);
		
		doGenericTest(
				"input:key1+input:key2",
				new HashMap() {{
					put("input", new ArrayList() {{
						add(new HashMap<String, String>(){{put("key1","foo");put("key2","bar");}});
						add(new HashMap<String, String>(){{put("key1","boo");put("key2","baz");}});
					}}); 
				}},
				"0/foo", "1/foo/bar", "0/boo", "1/boo/baz"
				);
		doGenericTest(
				"input:key1,input:key2",
				new HashMap() {{
					put("input", new ArrayList() {{
						add(new HashMap<String, String>(){{put("key1","foo");put("key2","bar");}});
						add(new HashMap<String, String>(){{put("key1","boo");put("key2","baz");}});
					}}); 
				}},
				"0/Foo,", "0/Boo,", "0/Bar,", "0/Baz,"
				);
		
		// the values are collected properly, the facet component will get ['foo', 'bar']
		// however the facet treats each independently (there is an assumption that the 
		// name is already concatenated. This is an example of complexity we are facing
		// if we are doing the transformation at the index time. I'd like to move this
		// logic out of solr; and just let solr index data (instead of transforming them)
		doGenericTest(
				"input:key1:key2,inputx:key1:key2",
				new HashMap() {{
					put("input", new ArrayList() {{
						add(new HashMap<String, String>(){{put("key1","foo");put("key2","bar");}});
						add(new HashMap<String, String>(){{put("key1","boo");put("key2","baz");}});
					}}); 
					put("inputx", new ArrayList() {{
						add(new HashMap<String, String>(){{put("key1","foo");put("key2","bar");}});
						add(new HashMap<String, String>(){{put("key1","boo");put("key2","baz");}});
					}});
				}},
				"0/Foo,", "0/Bar,", "0/Boo,", "0/Baz,", "0/Foo,", "0/Bar,", "0/Boo,", "0/Baz,"
				);
		doGenericTest( // list of lists
				"input:key1+input:key2",
				new HashMap() {{
					put("input", new ArrayList() {{
						add(new ArrayList() {{
							add(new HashMap<String, String>(){{put("key1","foo");put("key2","bar");}});
						}});
						add(new ArrayList() {{
							add(new HashMap<String, String>(){{put("key1","foo");put("key2","bar");}});
						}});
					}}); 
				}},
				"0/foo", "1/foo/bar", "0/foo", "1/foo/bar"
				);
		
		doGenericTest(
				"input:key1+input2:key1",
				new HashMap() {{
					put("input", new ArrayList() {{
						add(new HashMap<String, String>(){{put("key1","foo");put("key2","bar");}});
					}});
					put("input2", new ArrayList() {{
						add(new HashMap<String, String>(){{put("key1","boo");put("key2","baz");}});
					}});
				}},
				"0/foo", "1/foo/boo"
				);
		
		doGenericTest(
				"input:key1:key2,inputx:key1:key2",
				new HashMap() {{
					put("input", new ArrayList() {{
						add(new HashMap<String, String>(){{put("key1","foo");put("key2","bar");}});
					}});
					put("inputx", new ArrayList() {{
						add(new HashMap<String, String>(){{put("key1","boo");put("key2","baz");}});
					}});
				}},
				"0/Foo,", "0/Bar,", "0/Boo,", "0/Baz,"
				);
		
		
	}
	
	@Test
	public void testFunnyInputs() {
		
		doTestNormalType(
				new Object[] {Arrays.asList("'Smialkowski, A")},
				"0/Smialkowski, A", "1/Smialkowski, A/Smialkowski, A"
				);
		// no second level repeat
		doTestNormalType(
				new Object[] {Arrays.asList("\"Scientific Team Of v. N. Sukachev If Climatic Changes Of Northern Asia. \"")},
				"0/Scientific Team Of V N Sukachev If Climatic Changes Of Northern Asia,"
				);
		
		doTestNormalType(
				new Object[] {Arrays.asList("\"Aek\" Thanatipanonda, T")},
				"0/Aek Thanatipanonda, T", "1/Aek Thanatipanonda, T/Aek Thanatipanonda, T"
				);
		
		doTestNormalType(
				new Object[] {Arrays.asList("'t Hooft, A")},
				"0/T Hooft, A", "1/T Hooft, A/T Hooft, A"
				);
		
		doTestNormalType(
				new Object[] {Arrays.asList("(Bill) Peterson, W")},
				"0/Bill Peterson, W", "1/Bill Peterson, W/Bill Peterson, W"
				);
		
		doTestNormalType(
				new Object[] {Arrays.asList("(Jan van der Laan, D")},
				"0/Jan Van Der Laan, D", "1/Jan Van Der Laan, D/Jan Van Der Laan, D"
				);		
		
		doTestNormalType(
				new Object[] {Arrays.asList("(Jan) van der Laan, D")},
				"0/Jan Van Der Laan, D", "1/Jan Van Der Laan, D/Jan Van Der Laan, D"
				);
		
		// this is correct, there is O at the start!
		doTestNormalType(
				new Object[] {Arrays.asList("--O Renault, P")},
				"0/O Renault, P", "1/O Renault, P/O Renault, P"
				);
		
	}
	
	
}
