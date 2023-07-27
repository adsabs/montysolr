package org.jython.monty.interfaces;

import java.util.Map;

/*
 * Interface to the pythonic name parser library. We use it
 * to disambiguate author names
 */

public interface JythonNameParser {
    /*
     * Receives an author name; parses it using python name
     * parser and returns a dictionary with these elements:
     *
     *    - first
     *    - middle
     *    - last
     *    - suffix
     *    - title
     */
    Map<String, String> parse_human_name(String authorName);
}
