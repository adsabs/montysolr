package org.apache.solr.analysis.author;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.jython.JythonObjectFactory;
import org.jython.monty.interfaces.JythonNameParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/*
 * This filter will call Python library: http://code.google.com/p/python-nameparser/
 * to parse the input string, eg.
 *
 * Doe, Lt. Gen. John A. Kenneth IV
 *
 * is parsed as:
 *
 * <HumanName : [
 *  Title: 'Lt. Gen.'
 *  First: 'John'
 *  Middle: 'A. Kenneth'
 *  Last: 'Doe'
 *  Suffix: 'IV'
 * ]>
 *
 * Input can contain several author names, but these need to be separated
 * by semicolon
 */
public final class PythonicAuthorNormalizerFilter extends TokenFilter {

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
    private final List<String> buffer = new ArrayList<String>();
    private final Pattern multiSpace = Pattern.compile("\\s\\s+");
    private final JythonNameParser jythonParser;

    public PythonicAuthorNormalizerFilter(TokenStream input) {
        super(input);
        JythonObjectFactory factory = new JythonObjectFactory(JythonNameParser.class, "jython_name_parser", "HumanParser");
        this.jythonParser = (JythonNameParser) factory.createObject();
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (buffer.size() > 0) {
            termAtt.setEmpty().append(buffer.remove(0));
            typeAtt.setType(AuthorUtils.AUTHOR_INPUT);
            return true;
        }

        if (!input.incrementToken()) return false;

        String original = termAtt.toString();
        original = multiSpace.matcher(original).replaceAll(" ");

        String newIndividual = null;

        for (String individual : original.split(";")) {

            // skip processing wildcards
            if (individual.indexOf('*') > -1 || individual.indexOf('?') > -1) {
                buffer.add(individual);
                continue;
            }

            Map<String, String> parsedName = jythonParser.parse_human_name(individual);

            if (parsedName != null) {
                if (parsedName.containsKey("Last")) {
                    newIndividual = (parsedName.get("Last") + ","
                            + (parsedName.containsKey("First") ? " " + parsedName.get("First") : "")
                            + (parsedName.containsKey("Middle") ? " " + parsedName.get("Middle") : "")
                    );
                } else {
                    if (parsedName.containsKey("First") && parsedName.containsKey("Middle")) {
                        // should never happen
                        return false;
                        //throw new SolrException(ErrorCode.BAD_REQUEST, "We cannot reliably parse author name: " + individual);
                    } else if (parsedName.containsKey("First")) {
                        newIndividual = (parsedName.get("First") + ","); // we treat it as surname
                    } else {
                        newIndividual = (parsedName.get("Title") + ","); // else it was parsed as title (and since it is the only thing we have, let's take it for surname)
                    }
                }

                String ignSpaceIndividual = individual.replaceAll(" ", "");
                String ignNewSpaceIndividual = newIndividual != null ? newIndividual.replaceAll(" ", "") : "";

                if (newIndividual == null) {
                    // we should ignore this input completely
                } else if (newIndividual.equals(individual)
                        || newIndividual.equals(individual + ",")
                        || ignNewSpaceIndividual.equals(ignSpaceIndividual)
                        || ignNewSpaceIndividual.equals(ignSpaceIndividual + ",")
                ) {
                    buffer.add(newIndividual);  // no modifications, just add original
                } else { // some modifications happened

                    // add original
                    if (individual.indexOf(",") == -1) {
                        buffer.add(individual + ",");
                    } else {
                        buffer.add(individual);
                    }

                    if (newIndividual != null) {
                        buffer.add(newIndividual); // add modified version
                    }

                }

            } else {
                buffer.add(individual);
            }
        }

        if (buffer.size() == 0) {
            return false;
        }

        termAtt.setEmpty().append(buffer.remove(0));
        typeAtt.setType(AuthorUtils.AUTHOR_INPUT);

        return true;
    }
}