package org.jython.monty.interfaces;

import java.util.Map;

/*
 * Contract for the Java author-name parser used to disambiguate author names.
 */

public interface JythonNameParser {
    /*
     * Receives an author name and returns its parsed components in a map:
     *
     *    - First
     *    - Middle
     *    - Last
     *    - Suffix
     *    - Title
     */
    Map<String, String> parse_human_name(String authorName);
}
