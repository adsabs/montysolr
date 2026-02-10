package org.jython.monty;

import org.apache.lucene.tests.util.LuceneTestCase;
import org.jython.monty.interfaces.JythonNameParser;

import java.util.Map;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class TestJythonNameParser extends LuceneTestCase {

    private JythonNameParser parser;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        parser = new HumanParser();
    }

    @Override
    public void tearDown() throws Exception {
        System.clearProperty("python.cachedir.skip");
        System.clearProperty("python.console.encoding");
        super.tearDown();
    }

    // Basic name parsing tests

    public void testSimpleFirstLast() {
        Map<String, String> result = parser.parse_human_name("John Smith");
        assertEquals("John", result.get("First"));
        assertEquals("Smith", result.get("Last"));
        assertNull(result.get("Middle"));
    }

    public void testFirstMiddleLast() {
        Map<String, String> result = parser.parse_human_name("John Michael Smith");
        assertEquals("John", result.get("First"));
        assertEquals("Michael", result.get("Middle"));
        assertEquals("Smith", result.get("Last"));
    }

    public void testMultipleMiddleNames() {
        Map<String, String> result = parser.parse_human_name("John Michael David Smith");
        assertEquals("John", result.get("First"));
        assertEquals("Michael David", result.get("Middle"));
        assertEquals("Smith", result.get("Last"));
    }

    // Title tests

    public void testTitleWithName() {
        Map<String, String> result = parser.parse_human_name("Dr. John Smith");
        assertEquals("Dr.", result.get("Title"));
        assertEquals("John", result.get("First"));
        assertEquals("Smith", result.get("Last"));
    }

    public void testMultipleTitles() {
        Map<String, String> result = parser.parse_human_name("Dr. Prof. John Smith");
        assertEquals("Dr. Prof.", result.get("Title"));
        assertEquals("John", result.get("First"));
        assertEquals("Smith", result.get("Last"));
    }

    public void testMrAndMrs() {
        Map<String, String> result = parser.parse_human_name("Mr. and Mrs. Smith");
        assertEquals("Mr. and Mrs.", result.get("Title"));
        assertEquals("Smith", result.get("Last"));
    }

    // Suffix tests

    public void testSuffixJr() {
        Map<String, String> result = parser.parse_human_name("John Smith Jr.");
        assertEquals("John", result.get("First"));
        assertEquals("Smith", result.get("Last"));
        assertEquals("Jr.", result.get("Suffix"));
    }

    public void testSuffixPhd() {
        Map<String, String> result = parser.parse_human_name("John Smith PhD");
        assertEquals("John", result.get("First"));
        assertEquals("Smith", result.get("Last"));
        assertEquals("PhD", result.get("Suffix"));
    }

    public void testSuffixCommaFormat() {
        Map<String, String> result = parser.parse_human_name("Smith, John, Jr.");
        assertEquals("Smith", result.get("Last"));
        assertEquals("John", result.get("First"));
        assertEquals("Jr.", result.get("Suffix"));
    }

    public void testMultipleSuffixes() {
        Map<String, String> result = parser.parse_human_name("John Smith Jr., PhD");
        assertEquals("John", result.get("First"));
        assertEquals("Smith", result.get("Last"));
        assertEquals("Jr., PhD", result.get("Suffix"));
    }

    // Prefix tests (von, van, de, etc.)

    public void testPrefixVon() {
        Map<String, String> result = parser.parse_human_name("Ludwig von Beethoven");
        assertEquals("Ludwig", result.get("First"));
        assertEquals("von Beethoven", result.get("Last"));
    }

    public void testPrefixVan() {
        Map<String, String> result = parser.parse_human_name("Vincent van Gogh");
        assertEquals("Vincent", result.get("First"));
        assertEquals("van Gogh", result.get("Last"));
    }

    public void testPrefixDeLa() {
        Map<String, String> result = parser.parse_human_name("Diego de la Vega");
        assertEquals("Diego", result.get("First"));
        assertEquals("de la Vega", result.get("Last"));
    }

    public void testPrefixVanDer() {
        Map<String, String> result = parser.parse_human_name("Jan van der Berg");
        assertEquals("Jan", result.get("First"));
        assertEquals("van der Berg", result.get("Last"));
    }

    // Comma format tests (last, first)

    public void testCommaFormatSimple() {
        Map<String, String> result = parser.parse_human_name("Smith, John");
        assertEquals("Smith", result.get("Last"));
        assertEquals("John", result.get("First"));
    }

    public void testCommaFormatWithMiddle() {
        Map<String, String> result = parser.parse_human_name("Smith, John Michael");
        assertEquals("Smith", result.get("Last"));
        assertEquals("John", result.get("First"));
        assertEquals("Michael", result.get("Middle"));
    }

    public void testCommaFormatWithTitle() {
        Map<String, String> result = parser.parse_human_name("Smith, Dr. John");
        assertEquals("Smith", result.get("Last"));
        assertEquals("Dr.", result.get("Title"));
        assertEquals("John", result.get("First"));
    }

    // Conjunction tests

    public void testConjunctionAnd() {
        Map<String, String> result = parser.parse_human_name("John and Jane Smith");
        assertEquals("John and Jane", result.get("First"));
        assertEquals("Smith", result.get("Last"));
    }

    public void testSpanishYConjunction() {
        Map<String, String> result = parser.parse_human_name("García y López");
        assertEquals("García y López", result.get("Last"));
    }

    // Custom prefix handling (including 't)

    public void testCustomTPrefix() {
        Map<String, String> result = parser.parse_human_name("Jan 't Hooft");
        assertEquals("Jan", result.get("First"));
        assertEquals("'t Hooft", result.get("Last"));
    }

    // fix_name method tests (handling "' " - apostrophe with space)

    public void testFixNameApostropheSpace() {
        Map<String, String> result = parser.parse_human_name("John O' Sullivan");
        assertEquals("John", result.get("First"));
        assertEquals("O'Sullivan", result.get("Last"));
    }

    public void testFixNameApostropheMultipleSpaces() {
        Map<String, String> result = parser.parse_human_name("Mary O'  Connor");
        assertEquals("Mary", result.get("First"));
        assertEquals("O'Connor", result.get("Last"));
    }

    public void testFixNameApostropheSpaceD() {
        Map<String, String> result = parser.parse_human_name("Jean d' Arc");
        assertEquals("Jean", result.get("First"));
        assertTrue(result.get("Last").contains("Arc"));
    }

    // Initials tests

    public void testFirstInitial() {
        Map<String, String> result = parser.parse_human_name("J. Smith");
        assertEquals("J.", result.get("First"));
        assertEquals("Smith", result.get("Last"));
    }

    public void testFirstMiddleInitials() {
        Map<String, String> result = parser.parse_human_name("J. K. Rowling");
        assertEquals("J.", result.get("First"));
        assertEquals("K.", result.get("Middle"));
        assertEquals("Rowling", result.get("Last"));
    }

    public void testInitialsNoPeriods() {
        Map<String, String> result = parser.parse_human_name("J K Smith");
        assertEquals("J", result.get("First"));
        assertEquals("Smith", result.get("Last"));
    }

    // Suffix exclusions ('v' and 'i' removed from suffixes)

    public void testVNotTreatedAsSuffix() {
        Map<String, String> result = parser.parse_human_name("John v Smith");
        assertTrue(result.get("First").contains("John"));
        assertTrue(result.get("Last").contains("Smith"));
    }

    public void testINotTreatedAsSuffix() {
        Map<String, String> result = parser.parse_human_name("John i Smith");
        assertTrue(result.get("First").contains("John"));
        assertTrue(result.get("Last").contains("Smith"));
    }

    // Unicode and special characters

    public void testUnicodeSpanishName() {
        Map<String, String> result = parser.parse_human_name("José García");
        assertEquals("José", result.get("First"));
        assertEquals("García", result.get("Last"));
    }

    public void testUnicodeGermanName() {
        Map<String, String> result = parser.parse_human_name("Jürgen Müller");
        assertEquals("Jürgen", result.get("First"));
        assertEquals("Müller", result.get("Last"));
    }

    public void testUnicodeFrenchName() {
        Map<String, String> result = parser.parse_human_name("François Dupont");
        assertEquals("François", result.get("First"));
        assertEquals("Dupont", result.get("Last"));
    }

    // Edge cases

    public void testSingleName() {
        Map<String, String> result = parser.parse_human_name("Madonna");
        assertNotNull(result.get("First"));
    }

    public void testTitleOnly() {
        Map<String, String> result = parser.parse_human_name("Dr.");
        assertEquals("Dr.", result.get("Title"));
    }

    public void testEmptyString() {
        Map<String, String> result = parser.parse_human_name("");
        assertTrue(result.isEmpty());
    }

    public void testWhitespaceOnly() {
        Map<String, String> result = parser.parse_human_name("   ");
        assertTrue(result.isEmpty());
    }

    public void testExtraWhitespace() {
        Map<String, String> result = parser.parse_human_name("John    Smith");
        assertEquals("John", result.get("First"));
        assertEquals("Smith", result.get("Last"));
    }

    // Complex real-world examples

    public void testComplexAcademicName() {
        Map<String, String> result = parser.parse_human_name("Dr. Jean-Claude van Damme Jr., PhD");
        assertEquals("Dr.", result.get("Title"));
        assertTrue(result.get("First").contains("Jean-Claude"));
        assertTrue(result.get("Last").contains("van Damme"));
        assertTrue(result.get("Suffix").contains("Jr."));
        assertTrue(result.get("Suffix").contains("PhD"));
    }

    public void testHyphenatedLastName() {
        Map<String, String> result = parser.parse_human_name("Mary Smith-Jones");
        assertEquals("Mary", result.get("First"));
        assertEquals("Smith-Jones", result.get("Last"));
    }

    public void testHyphenatedFirstName() {
        Map<String, String> result = parser.parse_human_name("Jean-Pierre Dubois");
        assertEquals("Jean-Pierre", result.get("First"));
        assertEquals("Dubois", result.get("Last"));
    }

    public void testScottishMacName() {
        Map<String, String> result = parser.parse_human_name("John MacDonald");
        assertEquals("John", result.get("First"));
        assertEquals("MacDonald", result.get("Last"));
    }

    public void testIrishOName() {
        Map<String, String> result = parser.parse_human_name("Patrick O'Brien");
        assertEquals("Patrick", result.get("First"));
        assertEquals("O'Brien", result.get("Last"));
    }

    // Astronomy/academic author name formats

    public void testAstronomyFormatWithInitials() {
        Map<String, String> result = parser.parse_human_name("Smith, J. M.");
        assertEquals("Smith", result.get("Last"));
        assertEquals("J.", result.get("First"));
        assertEquals("M.", result.get("Middle"));
    }

    public void testAstronomyFormatFullName() {
        Map<String, String> result = parser.parse_human_name("Einstein, Albert");
        assertEquals("Einstein", result.get("Last"));
        assertEquals("Albert", result.get("First"));
    }

    public void testAstronomyFormatWithPrefix() {
        Map<String, String> result = parser.parse_human_name("van Gogh, Vincent");
        assertEquals("van Gogh", result.get("Last"));
        assertEquals("Vincent", result.get("First"));
    }

    // Original tests preserved

    public void test() {
        Map<String, String> res = parser.parse_human_name("Jimmi Hendrix");
        assertEquals("Jimmi", res.get("First"));
        assertEquals("Hendrix", res.get("Last"));

        for (int i = 0; i < 100; i++) {
            res = parser.parse_human_name("Jimmi Hendrix");
            if (i % 100 == 0) {
                //System.out.println(i);
            }
        }

        res = parser.parse_human_name("Stephen M'   Donald");
        assertEquals("Stephen", res.get("First"));
        assertEquals("M'Donald", res.get("Last"));
    }

    public void testUnicodeName() {
        String name = "^\uD835\uDDE6\uD835\uDDFD\uD835\uDDF2\uD835\uDDFF\uD835\uDDF4\uD835\uDDF2\uD835\uDDF9, D";
        Map<String, String> parsedName = parser.parse_human_name(name);
        assertNotNull(parsedName);
    }

    public void testParentheticalName() {
        String name = "(Helling,+Ch)";
        Map<String, String> parsedName = parser.parse_human_name(name);
        assertNotNull(parsedName);
    }

}
