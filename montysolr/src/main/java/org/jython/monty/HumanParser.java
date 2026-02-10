package org.jython.monty;

import org.jython.monty.interfaces.JythonNameParser;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Pure Java implementation of name parser that replaces the Jython-based implementation.
 * This class parses human names into their constituent parts: title, first, middle, last, and suffix.
 */
public class HumanParser implements JythonNameParser {

    private final Set<String> prefixes;
    private final Set<String> suffixes;
    private final Set<String> titles;
    private final Set<String> puncTitles;
    private final Set<String> conjunctions;
    private final Set<String> suffixesPrefixesTitles;

    // Regex patterns
    private static final Pattern RE_SPACES = Pattern.compile("\\s+");
    private static final Pattern RE_INITIAL = Pattern.compile("^(\\w\\.|[A-Z])?$");
    private static final Pattern RE_NICKNAME = Pattern.compile("\\s*?[\\(\"](.+?)[\\)\"]");

    public HumanParser() {
        this.titles = new HashSet<>(Arrays.asList(
            "dr", "doctor", "miss", "misses", "mr", "mister", "mrs", "ms", "sir", "dame",
            "rev", "madam", "madame", "prof", "professor"
        ));

        this.puncTitles = new HashSet<>(Arrays.asList("hon."));

        this.prefixes = new HashSet<>(Arrays.asList(
            "abu", "bon", "bin", "da", "dal", "de", "del", "der", "di", "dí", "ibn",
            "la", "le", "san", "st", "ste", "van", "vel", "von", "'t"
        ));

        this.suffixes = new HashSet<>(Arrays.asList(
            "esq", "esquire", "jr", "sr", "2", "ii", "iii", "iv", "md", "phd"
        ));

        this.conjunctions = new HashSet<>(Arrays.asList(
            "&", "and", "et", "e", "of", "the", "und", "y"
        ));

        this.suffixesPrefixesTitles = new HashSet<>();
        this.suffixesPrefixesTitles.addAll(this.suffixes);
        this.suffixesPrefixesTitles.addAll(this.prefixes);
        this.suffixesPrefixesTitles.addAll(this.titles);
    }

    @Override
    public Map<String, String> parse_human_name(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new HashMap<>();
        }

        String fixedName = fixName(input);
        return parseFullName(fixedName);
    }

    /**
     * Fix apostrophe-space patterns in names (e.g., "O' Sullivan" -> "O'Sullivan")
     */
    private String fixName(String input) {
        while (input.contains("' ")) {
            int start = input.indexOf("' ");
            int end = start + 2;
            while (end < input.length() && input.charAt(end) == ' ') {
                end++;
            }
            input = input.substring(0, start) + "'" + input.substring(end);
        }
        return input;
    }

    private Map<String, String> parseFullName(String fullName) {
        List<String> titleList = new ArrayList<>();
        List<String> firstList = new ArrayList<>();
        List<String> middleList = new ArrayList<>();
        List<String> lastList = new ArrayList<>();
        List<String> suffixList = new ArrayList<>();

        // Remove nicknames (content in parentheses or quotes)
        fullName = RE_NICKNAME.matcher(fullName).replaceAll("");

        // Collapse multiple spaces
        fullName = RE_SPACES.matcher(fullName.strip()).replaceAll(" ");

        // Split by commas
        String[] parts = fullName.split(",");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].strip();
        }

        if (parts.length == 0 || (parts.length == 1 && parts[0].isEmpty())) {
            return new HashMap<>();
        }

        if (parts.length == 1) {
            // No commas: title first middle middle last suffix
            List<String> pieces = parsePieces(Arrays.asList(parts), 0);

            for (int i = 0; i < pieces.size(); i++) {
                String piece = pieces.get(i);
                String nxt = (i < pieces.size() - 1) ? pieces.get(i + 1) : null;

                // Title must have a next piece, unless it's just a title
                if (isTitle(piece) && (nxt != null || pieces.size() == 1)) {
                    titleList.add(piece);
                    continue;
                }

                // Single piece containing a conjunction should be treated as last name
                if (firstList.isEmpty() && pieces.size() == 1 && pieceContainsConjunction(piece)) {
                    lastList.add(piece);
                    continue;
                }

                // If we have a title and this is the last piece, it's a last name
                if (firstList.isEmpty() && !titleList.isEmpty() && nxt == null) {
                    lastList.add(piece);
                    continue;
                }

                if (firstList.isEmpty()) {
                    firstList.add(piece);
                    continue;
                }

                if (i == pieces.size() - 2 && nxt != null && isSuffix(nxt)) {
                    lastList.add(piece);
                    suffixList.add(nxt);
                    break;
                }

                if (nxt == null) {
                    lastList.add(piece);
                    continue;
                }

                middleList.add(piece);
            }
        } else {
            // Check if second part is a suffix
            if (parts.length > 1 && isSuffix(lc(parts[1].strip()))) {
                // Suffix comma format: title first middle last, suffix [, suffix]
                List<String> pieces = parsePieces(Arrays.asList(parts[0]), 0);

                for (int i = 0; i < pieces.size(); i++) {
                    String piece = pieces.get(i);
                    String nxt = (i < pieces.size() - 1) ? pieces.get(i + 1) : null;

                    if (isTitle(piece) && (nxt != null || pieces.size() == 1)) {
                        titleList.add(piece);
                        continue;
                    }

                    if (firstList.isEmpty()) {
                        firstList.add(piece);
                        continue;
                    }

                    // Check if current piece is second-to-last and next is a suffix
                    if (i == pieces.size() - 2 && nxt != null && isSuffix(nxt)) {
                        lastList.add(piece);
                        suffixList.add(nxt);
                        break;
                    }

                    if (isSuffix(piece)) {
                        suffixList.add(piece);
                        continue;
                    }

                    if (nxt == null) {
                        lastList.add(piece);
                        continue;
                    }

                    middleList.add(piece);
                }

                // Add remaining parts as suffixes after processing parts[0]
                for (int i = 1; i < parts.length; i++) {
                    suffixList.add(parts[i].strip());
                }
            } else {
                // Last name comma format: last, title first middles [,] suffix [,suffix]
                lastList.add(parts[0]);

                List<String> pieces = parsePieces(Arrays.asList(parts[1]), 1);

                for (int i = 0; i < pieces.size(); i++) {
                    String piece = pieces.get(i);
                    String nxt = (i < pieces.size() - 1) ? pieces.get(i + 1) : null;

                    if (isTitle(piece) && (nxt != null || pieces.size() == 1)) {
                        titleList.add(piece);
                        continue;
                    }

                    if (firstList.isEmpty()) {
                        firstList.add(piece);
                        continue;
                    }

                    if (isSuffix(piece)) {
                        suffixList.add(piece);
                        continue;
                    }

                    middleList.add(piece);
                }

                // Add remaining parts as suffixes
                if (parts.length > 2) {
                    for (int i = 2; i < parts.length; i++) {
                        suffixList.add(parts[i].strip());
                    }
                }
            }
        }

        Map<String, String> result = new HashMap<>();
        if (!titleList.isEmpty()) {
            result.put("Title", String.join(" ", titleList));
        }
        if (!firstList.isEmpty()) {
            result.put("First", String.join(" ", firstList));
        }
        if (!middleList.isEmpty()) {
            result.put("Middle", String.join(" ", middleList));
        }
        if (!lastList.isEmpty()) {
            result.put("Last", String.join(" ", lastList));
        }
        if (!suffixList.isEmpty()) {
            result.put("Suffix", String.join(", ", suffixList));
        }

        return result;
    }

    private List<String> parsePieces(List<String> parts, int additionalPartsCount) {
        // Split parts on spaces and remove commas
        List<String> ps = new ArrayList<>();
        for (String part : parts) {
            String[] split = part.split(" +");  // Use regex to split on one or more spaces
            for (String s : split) {
                String stripped = s.strip().replaceAll("^,+|,+$", "");  // Remove leading/trailing commas
                if (!stripped.isEmpty()) {
                    ps.add(stripped);
                }
            }
        }

        // Split pieces that have periods not at the end
        List<String> pieces = new ArrayList<>();
        for (String piece : ps) {
            if (piece.length() > 1 && piece.substring(0, piece.length() - 1).contains(".")) {
                String[] split = piece.split("\\.");
                for (String s : split) {
                    if (!s.isEmpty()) {
                        pieces.add(s + ".");
                    }
                }
            } else {
                pieces.add(piece);
            }
        }

        // Join conjunctions to surrounding pieces (process in reverse order)
        for (int idx = pieces.size() - 1; idx >= 0; idx--) {
            if (!isConjunction(pieces.get(idx))) {
                continue;
            }

            String conj = pieces.get(idx);

            // If single letter conjunction and < 4 root names, skip UNLESS
            // we have exactly 3 pieces with the conjunction in the middle
            if (conj.length() == 1) {
                long rootNameCount = pieces.stream().filter(this::isRootname).count();
                // Special case: if we have exactly 3 pieces and middle is conjunction, join them
                boolean isThreePiecesWithMiddleConj = (pieces.size() == 3 && idx == 1);
                if (!isThreePiecesWithMiddleConj && rootNameCount + additionalPartsCount < 4) {
                    continue;
                }
            }

            if (idx < pieces.size() - 1) {
                // Check if previous piece is also a conjunction
                if (idx > 0 && isConjunction(pieces.get(idx - 1))) {
                    String newPiece = pieces.get(idx) + " " + pieces.get(idx + 1);
                    conjunctions.add(lc(newPiece));
                    pieces.set(idx, newPiece);
                    pieces.remove(idx + 1);
                    continue;
                }

                // Join with surrounding pieces
                if (idx > 0) {
                    String newPiece = pieces.get(idx - 1) + " " + pieces.get(idx) + " " + pieces.get(idx + 1);
                    if (isTitle(pieces.get(idx - 1))) {
                        titles.add(lc(newPiece));
                    }
                    pieces.set(idx - 1, newPiece);
                    pieces.remove(idx);
                    pieces.remove(idx);
                }
            }
        }

        // Join prefixes to following names
        for (int i = pieces.size() - 1; i >= 0; i--) {
            if (isPrefix(pieces.get(i)) && i < pieces.size() - 1) {
                String newPiece = pieces.get(i) + " " + pieces.get(i + 1);
                pieces.set(i, newPiece);
                pieces.remove(i + 1);
            }
        }

        return pieces;
    }

    private String lc(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        return value.toLowerCase().replace(".", "");
    }

    private boolean isAnInitial(String value) {
        return RE_INITIAL.matcher(value).matches();
    }

    private boolean isTitle(String piece) {
        return titles.contains(lc(piece)) || puncTitles.contains(piece.toLowerCase());
    }

    private boolean isConjunction(String piece) {
        return conjunctions.contains(lc(piece)) && !isAnInitial(piece);
    }

    private boolean isPrefix(String piece) {
        return prefixes.contains(lc(piece)) && !isAnInitial(piece);
    }

    private boolean isSuffix(String piece) {
        return suffixes.contains(lc(piece)) && !isAnInitial(piece);
    }

    private boolean isRootname(String piece) {
        return !suffixesPrefixesTitles.contains(lc(piece)) && !isAnInitial(piece);
    }

    private boolean pieceContainsConjunction(String piece) {
        String[] words = piece.split("\\s+");
        for (String word : words) {
            if (conjunctions.contains(lc(word))) {
                return true;
            }
        }
        return false;
    }
}
