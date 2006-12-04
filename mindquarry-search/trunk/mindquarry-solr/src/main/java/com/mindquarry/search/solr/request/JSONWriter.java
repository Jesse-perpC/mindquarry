/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.search.solr.request;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.solr.request.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrQueryResponse;
import org.apache.solr.request.TextResponseWriter;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.schema.TextField;
import org.apache.solr.search.DocIterator;
import org.apache.solr.search.DocList;
import org.apache.solr.util.NamedList;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public class JSONWriter extends TextResponseWriter {
    // cache the calendar instance in case we are writing many dates...
    private Calendar cal;

    private String namedListStyle;

    private String wrapperFunction;

    private static final String JSON_NL_STYLE = "json.nl"; //$NON-NLS-1$

    private static final String JSON_NL_MAP = "map"; //$NON-NLS-1$

    private static final String JSON_NL_ARROFARR = "arrarr"; //$NON-NLS-1$

    private static final String JSON_NL_ARROFMAP = "arrmap"; //$NON-NLS-1$

    private static final String JSON_WRAPPER_FUNCTION = "json.wrf"; //$NON-NLS-1$

    public JSONWriter(Writer writer, SolrQueryRequest req, SolrQueryResponse rsp) {
        super(writer, req, rsp);

        SolrParams params = req.getParams();
        namedListStyle = params.get(JSON_NL_STYLE);
        namedListStyle = namedListStyle == null ? JSON_NL_MAP : namedListStyle
                .intern();
        wrapperFunction = params.get(JSON_WRAPPER_FUNCTION);
    }

    public void writeResponse() throws IOException {
        int qtime = (int) (rsp.getEndTime() - req.getStartTime());

        HashMap<String, Integer> header = new HashMap<String, Integer>(1);
        header.put("qtime", qtime); //$NON-NLS-1$

        NamedList nl = new NamedList();
        nl.add("header", header); //$NON-NLS-1$
        nl.addAll(rsp.getValues());

        // give the main response a name if it doesn't have one
        if (nl.size() > 1 && nl.getVal(1) instanceof DocList
                && nl.getName(1) == null) {
            nl.setName(1, "response"); //$NON-NLS-1$
        }
        if (wrapperFunction != null) {
            writer.write(wrapperFunction + "("); //$NON-NLS-1$
        }
        writeNamedList(null, nl);
        if (wrapperFunction != null) {
            writer.write(")"); //$NON-NLS-1$
        }
    }

    protected void writeKey(String fname, boolean needsEscaping)
            throws IOException {
        writeStr(null, fname, needsEscaping);
        writer.write(':');
    }

    // Represents a NamedList directly as a JSON Object (essentially a Map)
    // more natural but potentially problematic since order is not maintained
    // and keys
    // can't be repeated.
    protected void writeNamedListAsMap(String name, NamedList val)
            throws IOException {
        int sz = val.size();
        writer.write('{');
        incLevel();

        // In JSON objects (maps) we can't have null keys or duplicates...
        // map null to "" and append a qualifier to duplicates.
        //
        // a=123,a=456 will be mapped to {a=1,a__1=456}
        // Disad: this is ambiguous since a real key could be called a__1
        //
        // Another possible mapping could aggregate multiple keys to an array:
        // a=123,a=456 maps to a=[123,456]
        // Disad: this is ambiguous with a real single value that happens to be
        // an array
        //
        // Both of these mappings have ambiguities.
        HashMap<String, Integer> repeats = new HashMap<String, Integer>(4);

        boolean first = true;
        for (int i = 0; i < sz; i++) {
            String key = val.getName(i);
            if (key == null)
                key = ""; //$NON-NLS-1$

            if (first) {
                first = false;
                repeats.put(key, 0);
            } else {
                writer.write(',');

                Integer repeatCount = repeats.get(key);
                if (repeatCount == null) {
                    repeats.put(key, 0);
                } else {
                    String newKey = key;
                    int newCount = repeatCount;
                    do { // avoid generated key clashing with a real key
                        newKey = key + ' ' + (++newCount);
                        repeatCount = repeats.get(newKey);
                    } while (repeatCount != null);

                    repeats.put(key, newCount);
                    key = newKey;
                }
            }

            indent();
            writeKey(key, true);
            writeVal(key, val.getVal(i));
        }

        decLevel();
        writer.write('}');
    }

    // Represents a NamedList directly as an array of JSON objects...
    // NamedList("a"=1,"b"=2,null=3) => [{"a":1},{"b":2},3]
    protected void writeNamedListAsArrMap(String name, NamedList val)
            throws IOException {
        int sz = val.size();
        indent();
        writer.write('[');
        incLevel();

        boolean first = true;
        for (int i = 0; i < sz; i++) {
            String key = val.getName(i);

            if (first) {
                first = false;
            } else {
                writer.write(',');
            }

            indent();

            if (key == null) {
                writeVal(null, val.getVal(i));
            } else {
                writer.write('{');
                writeKey(key, true);
                writeVal(key, val.getVal(i));
                writer.write('}');
            }

        }

        decLevel();
        writer.write(']');
    }

    // Represents a NamedList directly as an array of JSON objects...
    // NamedList("a"=1,"b"=2,null=3) => [["a",1],["b",2],[null,3]]
    protected void writeNamedListAsArrArr(String name, NamedList val)
            throws IOException {
        int sz = val.size();
        indent();
        writer.write('[');
        incLevel();

        boolean first = true;
        for (int i = 0; i < sz; i++) {
            String key = val.getName(i);

            if (first) {
                first = false;
            } else {
                writer.write(',');
            }

            indent();

            /*******************************************************************
             * * if key is null, just write value??? if (key==null) {
             * writeVal(null,val.getVal(i)); } else {
             ******************************************************************/

            writer.write('[');
            incLevel();
            writeStr(null, key, true);
            writer.write(',');
            writeVal(key, val.getVal(i));
            decLevel();
            writer.write(']');

        }

        decLevel();
        writer.write(']');
    }

    public void writeNamedList(String name, NamedList val) throws IOException {
        if (namedListStyle == JSON_NL_ARROFMAP) {
            writeNamedListAsArrMap(name, val);
        } else if (namedListStyle == JSON_NL_ARROFARR) {
            writeNamedListAsArrArr(name, val);
        } else {
            writeNamedListAsMap(name, val);
        }
    }

    public void writeDoc(String name, Collection<Fieldable> fields,
            Set<String> returnFields, Map pseudoFields) throws IOException {
        writer.write('{');
        incLevel();

        HashMap<String, MultiValueField> multi = new HashMap<String, MultiValueField>();

        boolean first = true;

        for (Fieldable ff : fields) {
            String fname = ff.name();
            if (returnFields != null && !returnFields.contains(fname)) {
                continue;
            }

            // if the field is multivalued, it may have other values further
            // on... so
            // build up a list for each multi-valued field.
            SchemaField sf = schema.getField(fname);
            if (sf.multiValued()) {
                MultiValueField mf = multi.get(fname);
                if (mf == null) {
                    mf = new MultiValueField(sf, ff);
                    multi.put(fname, mf);
                } else {
                    mf.fields.add(ff);
                }
            } else {
                // not multi-valued, so write it immediately.
                if (first) {
                    first = false;
                } else {
                    writer.write(',');
                }
                indent();
                writeKey(fname, true);
                sf.write(this, fname, ff);
            }
        }

        for (MultiValueField mvf : multi.values()) {
            if (first) {
                first = false;
            } else {
                writer.write(',');
            }

            indent();
            writeKey(mvf.sfield.getName(), true);

            boolean indentArrElems = false;
            if (doIndent) {
                // heuristic... TextField is probably the only field type likely
                // to be long enough
                // to warrant indenting individual values.
                indentArrElems = (mvf.sfield.getType() instanceof TextField);
            }

            writer.write('[');
            boolean firstArrElem = true;
            incLevel();

            for (Fieldable ff : mvf.fields) {
                if (firstArrElem) {
                    firstArrElem = false;
                } else {
                    writer.write(',');
                }
                if (indentArrElems)
                    indent();
                mvf.sfield.write(this, null, ff);
            }
            writer.write(']');
            decLevel();
        }

        if (pseudoFields != null && pseudoFields.size() > 0) {
            writeMap(null, pseudoFields, true, first);
        }

        decLevel();
        writer.write('}');
    }

    // reusable map to store the "score" pseudo-field.
    // if a Doc can ever contain another doc, this optimization would have to
    // go.
    private final HashMap scoreMap = new HashMap(1);

    public void writeDoc(String name, Document doc, Set<String> returnFields,
            float score, boolean includeScore) throws IOException {
        Map other = null;
        if (includeScore) {
            other = scoreMap;
            scoreMap.put("score", score);
        }
        writeDoc(name, (List<Fieldable>) (doc.getFields()), returnFields, other);
    }

    public void writeDocList(String name, DocList ids, Set<String> fields,
            Map otherFields) throws IOException {
        boolean includeScore = false;
        if (fields != null) {
            includeScore = fields.contains("score");
            if (fields.size() == 0 || (fields.size() == 1 && includeScore)
                    || fields.contains("*")) {
                fields = null; // null means return all stored fields
            }
        }

        int sz = ids.size();

        writer.write('{');
        incLevel();
        writeKey("numFound", false);
        writeInt(null, ids.matches());
        writer.write(',');
        writeKey("start", false);
        writeInt(null, ids.offset());

        if (includeScore) {
            writer.write(',');
            writeKey("maxScore", false);
            writeFloat(null, ids.maxScore());
        }
        writer.write(',');
        // indent();
        writeKey("docs", false);
        writer.write('[');

        incLevel();
        boolean first = true;

        DocIterator iterator = ids.iterator();
        for (int i = 0; i < sz; i++) {
            int id = iterator.nextDoc();
            Document doc = searcher.doc(id);

            if (first) {
                first = false;
            } else {
                writer.write(',');
            }
            indent();
            writeDoc(null, doc, fields,
                    (includeScore ? iterator.score() : 0.0f), includeScore);
        }
        decLevel();
        writer.write(']');

        if (otherFields != null) {
            writeMap(null, otherFields, true, false);
        }

        decLevel();
        indent();
        writer.write('}');
    }

    public void writeStr(String name, String val, boolean needsEscaping)
            throws IOException {
        writer.write('"');
        // it might be more efficient to use a stringbuilder or write substrings
        // if writing chars to the stream is slow.
        if (needsEscaping) {

            /*
             * http://www.ietf.org/internet-drafts/draft-crockford-jsonorg-json-04.txt
             * All Unicode characters may be placed within the quotation marks
             * except for the characters which must be escaped: quotation mark,
             * reverse solidus, and the control characters (U+0000 through
             * U+001F).
             */

            for (int i = 0; i < val.length(); i++) {
                char ch = val.charAt(i);
                switch (ch) {
                case '"':
                case '\\':
                    writer.write('\\');
                    writer.write(ch);
                    break;
                case '\r':
                    writer.write("\\r");
                    break;
                case '\n':
                    writer.write("\\n");
                    break;
                case '\t':
                    writer.write("\\t");
                    break;
                case '\b':
                    writer.write("\\b");
                    break;
                case '\f':
                    writer.write("\\f");
                    break;
                // case '/':
                default: {
                    if (ch <= 0x1F) {
                        unicodeEscape(writer, ch);
                    } else {
                        writer.write(ch);
                    }
                }
                }
            }
        } else {
            writer.write(val);
        }
        writer.write('"');
    }

    public void writeMap(String name, Map val, boolean excludeOuter,
            boolean isFirstVal) throws IOException {
        if (!excludeOuter) {
            writer.write('{');
            incLevel();
            isFirstVal = true;
        }

        boolean doIndent = excludeOuter || val.size() > 1;

        for (Map.Entry entry : (Set<Map.Entry>) val.entrySet()) {
            Object e = entry.getKey();
            String k = e == null ? null : e.toString();
            Object v = entry.getValue();

            if (isFirstVal) {
                isFirstVal = false;
            } else {
                writer.write(',');
            }

            if (doIndent)
                indent();
            writeKey(k, true);
            writeVal(k, v);
        }

        if (!excludeOuter) {
            decLevel();
            writer.write('}');
        }
    }

    public void writeArray(String name, Object[] val) throws IOException {
        writeArray(name, Arrays.asList(val));
    }

    public void writeArray(String name, Collection val) throws IOException {
        writer.write('[');
        int sz = val.size();
        incLevel();
        boolean first = true;
        for (Object o : val) {
            if (first) {
                first = false;
            } else {
                writer.write(',');
            }
            if (sz > 1)
                indent();
            writeVal(null, o);
        }
        decLevel();
        writer.write(']');
    }

    //
    // Primitive types
    //
    public void writeNull(String name) throws IOException {
        writeStr(name, "null", false);
    }

    public void writeInt(String name, String val) throws IOException {
        writer.write(val);
    }

    public void writeLong(String name, String val) throws IOException {
        writer.write(val);
    }

    public void writeBool(String name, String val) throws IOException {
        writer.write(val);
    }

    public void writeFloat(String name, String val) throws IOException {
        writer.write(val);
    }

    public void writeDouble(String name, String val) throws IOException {
        writer.write(val);
    }

    // TODO: refactor this out to a DateUtils class or something...
    public void writeDate(String name, Date val) throws IOException {
        // using a stringBuilder for numbers can be nice since
        // a temporary string isn't used (it's added directly to the
        // builder's buffer.

        StringBuilder sb = new StringBuilder();
        if (cal == null)
            cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.setTime(val);

        int i = cal.get(Calendar.YEAR);
        sb.append(i);
        sb.append('-');
        i = cal.get(Calendar.MONTH) + 1; // 0 based, so add 1
        if (i < 10)
            sb.append('0');
        sb.append(i);
        sb.append('-');
        i = cal.get(Calendar.DAY_OF_MONTH);
        if (i < 10)
            sb.append('0');
        sb.append(i);
        sb.append('T');
        i = cal.get(Calendar.HOUR_OF_DAY); // 24 hour time format
        if (i < 10)
            sb.append('0');
        sb.append(i);
        sb.append(':');
        i = cal.get(Calendar.MINUTE);
        if (i < 10)
            sb.append('0');
        sb.append(i);
        sb.append(':');
        i = cal.get(Calendar.SECOND);
        if (i < 10)
            sb.append('0');
        sb.append(i);
        i = cal.get(Calendar.MILLISECOND);
        if (i != 0) {
            sb.append('.');
            if (i < 100)
                sb.append('0');
            if (i < 10)
                sb.append('0');
            sb.append(i);

            // handle canonical format specifying fractional
            // seconds shall not end in '0'. Given the slowness of
            // integer div/mod, simply checking the last character
            // is probably the fastest way to check.
            int lastIdx = sb.length() - 1;
            if (sb.charAt(lastIdx) == '0') {
                lastIdx--;
                if (sb.charAt(lastIdx) == '0') {
                    lastIdx--;
                }
                sb.setLength(lastIdx + 1);
            }
        }
        sb.append('Z');
        writeDate(name, sb.toString());
    }

    /**
     * @see org.apache.solr.request.TextResponseWriter#writeDate(java.lang.String, java.lang.String)
     */
    public void writeDate(String name, String val) throws IOException {
        writeStr(name, val, false);
    }

    protected static void unicodeEscape(Appendable sb, int ch)
            throws IOException {
        String str = Integer.toHexString(ch & 0xffff);
        switch (str.length()) {
        case 1:
            sb.append("\\u000"); //$NON-NLS-1$
            break;
        case 2:
            sb.append("\\u00"); //$NON-NLS-1$
            break;
        case 3:
            sb.append("\\u0"); //$NON-NLS-1$
            break;
        default:
            sb.append("\\u"); //$NON-NLS-1$
            break;
        }
        sb.append(str);
    }
}
