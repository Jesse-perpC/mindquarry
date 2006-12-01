/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.search.solr.request;

import java.io.IOException;
import java.io.Writer;
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
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrQueryResponse;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.schema.TextField;
import org.apache.solr.search.DocIterator;
import org.apache.solr.search.DocList;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.util.NamedList;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public class JSONResponseWriter {
    protected Writer writer;

    protected IndexSchema schema;

    protected SolrIndexSearcher searcher;

    protected SolrQueryRequest req;

    protected SolrQueryResponse rsp;

    // the default set of fields to return for each document
    protected Set<String> returnFields;

    protected int indentLevel;

    // cache the calendar instance in case we are writing many dates...
    private Calendar cal;

    private final HashMap<String, Float> scoreMap = new HashMap<String, Float>(
            1);

    public void write(Writer writer, SolrQueryRequest req, SolrQueryResponse rsp)
            throws IOException {
        this.writer = writer;
        this.schema = req.getSchema();
        this.searcher = req.getSearcher();
        this.req = req;
        this.rsp = rsp;

        returnFields = rsp.getReturnFields();
        writeResponse();
    }

    public void writeResponse() throws IOException {
        int qtime = (int) (rsp.getEndTime() - req.getStartTime());

        HashMap<String, Integer> header = new HashMap<String, Integer>(1);
        header.put("qtime", qtime); //$NON-NLS-1$

        NamedList nl = new NamedList();
        nl.add("header", header); //$NON-NLS-1$
        nl.addAll(rsp.getValues());

        // give the main response a name if it doesn't have one
        if ((nl.size() > 1) && (nl.getVal(1) instanceof DocList)
                && (nl.getName(1) == null)) {
            nl.setName(1, "response"); //$NON-NLS-1$
        }
        writeNamedList(null, nl);
    }

    /**
     * Represents a NamedList directly as a JSON Object (essentially a Map) more
     * natural but potentially problematic since order is not maintained and
     * keys can't be repeated.
     */
    protected void writeNamedList(String name, NamedList val)
            throws IOException {
        int sz = val.size();
        writer.write('{');
        ++indentLevel;

        // In JSON objects (maps) we can't have null keys or duplicates...
        // map null to "" and append a qualifier to duplicates.
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
            indent(indentLevel);
            writeKey(key, true);
            writeVal(key, val.getVal(i));
        }
        --indentLevel;
        writer.write('}');
    }

    protected void writeKey(String fname, boolean needsEscaping)
            throws IOException {
        writeStr(null, fname, needsEscaping);
        writer.write(':');
    }

    public void writeVal(String name, Object val) throws IOException {
        // if there get to be enough types, perhaps hashing on the type
        // to get a handler might be faster (but types must be exact to do
        // that...)

        // go in order of most common to least common
        if (val == null) {
            writeStr(name, "null", false); //$NON-NLS-1$
        } else if (val instanceof String) {
            writeStr(name, val.toString(), true); // micro-optimization...
            // using toString() avoids a cast first
        } else if (val instanceof Integer) {
            writer.write(val.toString());
        } else if (val instanceof Boolean) {
            writer.write(val.toString());
        } else if (val instanceof Long) {
            writer.write(val.toString());
        } else if (val instanceof Date) {
            writeDate(name, (Date) val);
        } else if (val instanceof Float) {
            // we use float instead of using String because
            // it may need special formatting. same for double.

            writer.write(((Float) val).toString());
        } else if (val instanceof Double) {
            writer.write(((Double) val).toString());
        } else if (val instanceof Document) {
            writeDoc(name, (Document) val, returnFields, 0.0f, false);
        } else if (val instanceof DocList) {
            // requires access to IndexReader
            writeDocList(name, (DocList) val, returnFields, null);
        } else if (val instanceof Map) {
            writeMap(name, (Map) val, false, true);
        } else if (val instanceof NamedList) {
            writeNamedList(name, (NamedList) val);
        } else if (val instanceof Collection) {
            writeArray(name, (Collection) val);
        } else if (val instanceof Object[]) {
            writeArray(name, (Object[]) val);
        } else {
            // default... for debugging only
            writeStr(name, val.getClass().getName() + ':' + val.toString(),
                    true);
        }
    }

    public void writeDocList(String name, DocList ids, Set<String> fields,
            Map otherFields) throws IOException {
        boolean includeScore = false;
        if (fields != null) {
            includeScore = fields.contains("score"); //$NON-NLS-1$
            if ((fields.size() == 0) || (fields.size() == 1 && includeScore)
                    || fields.contains('*')) {
                fields = null; // null means return all stored fields
            }
        }
        int sz = ids.size();
        writer.write('{');
        ++indentLevel;
        writeKey("numFound", false); //$NON-NLS-1$
        writer.write((new Integer(ids.matches())).toString());
        writer.write(',');
        writeKey("start", false); //$NON-NLS-1$
        writer.write((new Integer(ids.offset()).toString()));

        if (includeScore) {
            writer.write(',');
            writeKey("maxScore", false); //$NON-NLS-1$
            writer.write(((Float) ids.maxScore()).toString());
        }
        writer.write(',');
        writeKey("docs", false); //$NON-NLS-1$
        writer.write('[');

        ++indentLevel;
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
            indent(indentLevel);
            writeDoc(null, doc, fields,
                    (includeScore ? iterator.score() : 0.0f), includeScore);
        }
        --indentLevel;
        writer.write(']');

        if (otherFields != null) {
            writeMap(null, otherFields, true, false);
        }
        --indentLevel;
        indent(indentLevel);
        writer.write('}');
    }

    public void writeDoc(String name, Document doc, Set<String> returnFields,
            float score, boolean includeScore) throws IOException {
        Map other = null;
        if (includeScore) {
            other = scoreMap;
            scoreMap.put("score", score); //$NON-NLS-1$
        }
        writeDoc(name, (List<Fieldable>) (doc.getFields()), returnFields, other);
    }

    public void writeDoc(String name, Collection<Fieldable> fields,
            Set<String> returnFields, Map pseudoFields) throws IOException {
        writer.write('{');
        ++indentLevel;

        HashMap<String, MultiValueField> multi = new HashMap<String, MultiValueField>();

        boolean first = true;
        for (Fieldable ff : fields) {
            String fname = ff.name();
            if ((returnFields != null) && (!returnFields.contains(fname))) {
                continue;
            }
            // if the field is multivalued, it may have other values further
            // on... so build up a list for each multi-valued field.
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
                indent(indentLevel);
                writeKey(fname, true);
                
                // sf.write(this, fname, ff);
                writer.write(ff.stringValue());
            }
        }

        for (MultiValueField mvf : multi.values()) {
            if (first) {
                first = false;
            } else {
                writer.write(',');
            }
            indent(indentLevel);
            writeKey(mvf.sfield.getName(), true);

            boolean indentArrElems = false;
            indentArrElems = (mvf.sfield.getType() instanceof TextField);

            writer.write('[');
            boolean firstArrElem = true;
            ++indentLevel;

            for (Fieldable ff : mvf.fields) {
                if (firstArrElem) {
                    firstArrElem = false;
                } else {
                    writer.write(',');
                }
                if (indentArrElems) {
                    ++indentLevel;
                }
                
                // mvf.sfield.write(this, null, ff);
            }
            writer.write(']');
            --indentLevel;
        }
        if (pseudoFields != null && pseudoFields.size() > 0) {
            writeMap(null, pseudoFields, true, first);
        }
        --indentLevel;
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
                    writer.write("\\r"); //$NON-NLS-1$
                    break;
                case '\n':
                    writer.write("\\n"); //$NON-NLS-1$
                    break;
                case '\t':
                    writer.write("\\t"); //$NON-NLS-1$
                    break;
                case '\b':
                    writer.write("\\b"); //$NON-NLS-1$
                    break;
                case '\f':
                    writer.write("\\f"); //$NON-NLS-1$
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

    private void unicodeEscape(Appendable sb, int ch) throws IOException {
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

    public void writeDate(String name, Date val) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (cal == null) {
            cal = Calendar.getInstance(TimeZone.getTimeZone("GMT")); //$NON-NLS-1$
        }
        cal.setTime(val);

        int i = cal.get(Calendar.YEAR);
        sb.append(i);
        sb.append('-');

        i = cal.get(Calendar.MONTH) + 1; // 0 based, so add 1
        if (i < 10) {
            sb.append('0');
        }
        sb.append(i);
        sb.append('-');

        i = cal.get(Calendar.DAY_OF_MONTH);
        if (i < 10) {
            sb.append('0');
        }
        sb.append(i);
        sb.append('T');

        i = cal.get(Calendar.HOUR_OF_DAY); // 24 hour time format
        if (i < 10) {
            sb.append('0');
        }
        sb.append(i);
        sb.append(':');

        i = cal.get(Calendar.MINUTE);
        if (i < 10) {
            sb.append('0');
        }
        sb.append(i);
        sb.append(':');

        i = cal.get(Calendar.SECOND);
        if (i < 10) {
            sb.append('0');
        }
        sb.append(i);

        i = cal.get(Calendar.MILLISECOND);
        if (i != 0) {
            sb.append('.');
            if (i < 100) {
                sb.append('0');
            }
            if (i < 10) {
                sb.append('0');
            }
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
        writeStr(name, sb.toString(), false);
    }

    public void writeArray(String name, Object[] val) throws IOException {
        writeArray(name, Arrays.asList(val));
    }

    public void writeArray(String name, Collection val) throws IOException {
        writer.write('[');
        int sz = val.size();
        ++indentLevel;

        boolean first = true;
        for (Object obj : val) {
            if (first) {
                first = false;
            } else {
                writer.write(',');
            }
            if (sz > 1) {
                indent(indentLevel);
            }
            writeVal(null, obj);
        }
        --indentLevel;
        writer.write(']');
    }

    public void writeMap(String name, Map val, boolean excludeOuter,
            boolean isFirstVal) throws IOException {
        if (!excludeOuter) {
            writer.write('{');
            ++indentLevel;
            isFirstVal = true;
        }
        boolean doIndent = excludeOuter || val.size() > 1;

        for (Map.Entry entry : (Set<Map.Entry>) val.entrySet()) {
            Object obj = entry.getKey();
            String key = obj == null ? null : obj.toString();
            Object tmpVal = entry.getValue();

            if (isFirstVal) {
                isFirstVal = false;
            } else {
                writer.write(',');
            }

            if (doIndent)
                indent(indentLevel);
            writeKey(key, true);
            writeVal(key, tmpVal);
        }
        if (!excludeOuter) {
            --indentLevel;
            writer.write('}');
        }
    }

    // use a combination of tabs and spaces to minimize the size of an indented
    // response.
    private final String[] indentArr = new String[] { "\n", //$NON-NLS-1$
            "\n ", //$NON-NLS-1$
            "\n  ", //$NON-NLS-1$
            "\n\t", //$NON-NLS-1$
            "\n\t ", //$NON-NLS-1$
            "\n\t  ", //$NON-NLS-1$
            "\n\t\t", //$NON-NLS-1$
            "\n\t\t " }; //$NON-NLS-1$

    public void indent(int lev) throws IOException {
        // power-of-two intent array (gratuitous optimization :-)
        String istr = indentArr[lev & (indentArr.length - 1)];
        writer.write(istr);
    }
}
