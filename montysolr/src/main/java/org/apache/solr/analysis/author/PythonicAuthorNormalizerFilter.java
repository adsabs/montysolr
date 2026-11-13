package org.apache.solr.analysis.author;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.BytesRef;
import org.jython.monty.HumanParser;
import org.jython.monty.interfaces.JythonNameParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/*
 * This filter uses the Java HumanParser to parse the input string, eg.
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
    private final PayloadAttribute payloadAtt = addAttribute(PayloadAttribute.class);
    // Each buffered name has a source-name payload at the same index.
    private final List<String> bufferedSourceNames = new ArrayList<String>();
    private final List<String> bufferedNames = new ArrayList<String>();
    private final Pattern multiSpace = Pattern.compile("\\s\\s+");
    private final JythonNameParser nameParser;

    public PythonicAuthorNormalizerFilter(TokenStream input) {
        super(input);
        this.nameParser = new HumanParser();
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (bufferedNames.size() > 0) {
            emitBuffered();
            return true;
        }

        if (!input.incrementToken()) return false;

        String original = termAtt.toString();
        original = multiSpace.matcher(original).replaceAll(" ");

        String newIndividual = null;

        for (String individual : original.split(";")) {
            String sourceName = individual.trim();
            if (!sourceName.contains(",")) {
                sourceName += ",";
            }

            // skip processing wildcards
            if (individual.contains("*") || individual.contains("?")) {
                addBuffered(individual, sourceName);
                continue;
            }

            Map<String, String> parsedName = nameParser.parse_human_name(individual);

            if (parsedName != null && !parsedName.isEmpty()) {
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
                    addBuffered(newIndividual, sourceName);
                } else { // some modifications happened

                    // add original
                    if (!individual.contains(",")) {
                        addBuffered(individual + ",", sourceName);
                    } else {
                        addBuffered(individual, sourceName);
                    }

                    if (newIndividual != null) {
                        addBuffered(newIndividual, sourceName); // add modified version
                    }
                }

            } else {
                addBuffered(individual, sourceName);
            }
        }

        if (bufferedNames.size() == 0) {
            return false;
        }

        emitBuffered();
        return true;
    }

    private void addBuffered(String name, String originalName) {
        bufferedNames.add(name);
        bufferedSourceNames.add(originalName);
    }

    private void emitBuffered() {
        termAtt.setEmpty().append(bufferedNames.remove(0));
        payloadAtt.setPayload(new BytesRef(bufferedSourceNames.remove(0)));
        typeAtt.setType(AuthorUtils.AUTHOR_INPUT);
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        bufferedNames.clear();
        bufferedSourceNames.clear();
    }
}
