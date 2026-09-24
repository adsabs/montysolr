package org.apache.solr.analysis;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.solr.common.SolrException;
import org.apache.solr.util.DateMathParser;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class DateNormalizerTokenFilter extends TokenFilter {

    private final SimpleDateFormat[] format;
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final DateMathParser dmp;
    private final String offset;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);


    public DateNormalizerTokenFilter(TokenStream input, String incomingFormat, String offset) {
        super(input);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.offset = offset;
        String[] parts = incomingFormat.split("\\|");
        format = new SimpleDateFormat[parts.length];
        for (int i = 0; i < parts.length; i++) {
            format[i] = new SimpleDateFormat(parts[i], Locale.US);
            format[i].setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        dmp = new DateMathParser(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (!input.incrementToken()) {
            return false;
        }
        CharSequence v = normalize(termAtt.toString());
        termAtt.setEmpty().append(v);
        return true;
    }

    private CharSequence normalize(String string) {
        int firstDash = string.indexOf('-');
        int secondDash = firstDash < 0 ? -1 : string.indexOf('-', firstDash + 1);
        int dayEnd = secondDash < 0 ? -1 : string.indexOf('T', secondDash + 1);
        if (dayEnd < 0) {
            dayEnd = string.length();
        }

        int monthEnd = secondDash >= 0 ? secondDash : string.length();
        boolean zeroMonth = firstDash >= 0 && allZeros(string, firstDash + 1, monthEnd);
        boolean zeroDay = secondDash >= 0 && allZeros(string, secondDash + 1, dayEnd);
        boolean normalDate = secondDash >= 0 && !zeroMonth && !zeroDay;

        if (zeroMonth) {
            if (secondDash < 0 || zeroDay) {
                string = string.substring(0, firstDash);
            } else {
                // Preserve a nonzero day after a zero month so invalid values
                // such as yyyy-00-32 are not silently turned into yyyy.
                string = string.substring(0, firstDash) + string.substring(secondDash);
            }
        } else if (zeroDay) {
            string = string.substring(0, secondDash);
        }

        // we allow symbolic date math logic; if parsed
        // then the date will always be moved into the first
        // 30 mins of the day
        try {
            Date date = DateMathParser.parseMath(null, string);
            dmp.setNow(date);
            date = dmp.parseMath("/DAY" + this.offset);
            return sdf.format(date);
        } catch (SolrException e) {
            // pass
        } catch (ParseException e) {
            // pass
        }

        for (SimpleDateFormat f : this.format) {
            try {
                Date date = f.parse(string);
                dmp.setNow(date);
                if (normalDate) { // move the docs with the date specified 30 min into the future (so that unspecified
                    // dates will all cluster in the first x minutes of the day)
                    date = dmp.parseMath(this.offset);
                }
                //else {
                //date = dmp.parseMath("+5MINUTES"); // 00-00 dates are 1 minute after midnight
                //}
                return sdf.format(date);
            } catch (ParseException e) {
                //pass
            }
        }
        return "0000-00-00T00:00:00Z"; // error parsing input data
    }

    private boolean allZeros(String value, int start, int end) {
        if (start >= end) {
            return false;
        }
        for (int i = start; i < end; i++) {
            if (value.charAt(i) != '0') {
                return false;
            }
        }
        return true;
    }


}
