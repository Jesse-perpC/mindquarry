/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.search.solr.request;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrQueryResponse;
import org.apache.solr.search.DocIterator;
import org.apache.solr.search.DocList;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class QuickSearchSearchJSONWriter extends JSONWriter {
    public QuickSearchSearchJSONWriter(Writer writer, SolrQueryRequest req,
            SolrQueryResponse rsp) {
        super(writer, req, rsp);
    }

    public void writeDocList(String name, DocList ids, Set<String> fields,
            Map otherFields) throws IOException {
        boolean includeScore = false;
        if (fields != null) {
            includeScore = fields.contains("score"); //$NON-NLS-1$
            if (fields.size() == 0 || (fields.size() == 1 && includeScore)
                    || fields.contains("*")) { //$NON-NLS-1$
                fields = null; // null means return all stored fields
            }
        }
        int sz = ids.size();

        writer.write('{');
        incLevel();
        writeKey("numFound", false); //$NON-NLS-1$
        writeInt(null, ids.matches());
        writer.write(',');
        writeKey("start", false); //$NON-NLS-1$
        writeInt(null, ids.offset());

        if (includeScore) {
            writer.write(',');
            writeKey("maxScore", false); //$NON-NLS-1$
            writeFloat(null, ids.maxScore());
        }
        writer.write(',');
        writeKey("docs", false); //$NON-NLS-1$
        writer.write('{');

        incLevel();
        HashMap<String, List<DocWithScore>> sets = new HashMap<String, List<DocWithScore>>();

        DocIterator iterator = ids.iterator();
        for (int i = 0; i < sz; i++) {
            int id = iterator.nextDoc();
            Document doc = searcher.doc(id);

            for (Fieldable ff : (List<Fieldable>) doc.getFields()) {
                String fname = ff.name();
                String fval = ff.stringValue();

                if (fname.equals("type")) { //$NON-NLS-1$
                    String type = fval;
                    if(type.equals("wiki")) { //$NON-NLS-1$
                        type = type.replaceFirst("w", "W"); //$NON-NLS-1$ //$NON-NLS-2$
                    } else if(type.equals("tasks")) { //$NON-NLS-1$
                        type = type.replaceFirst("t", "T"); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                    if (!sets.containsKey(type)) {
                        sets.put(type, new ArrayList<DocWithScore>());
                    }
                    if (includeScore) {
                        sets.get(type).add(
                                new DocWithScore(doc, iterator.score()));
                    } else {
                        sets.get(type).add(new DocWithScore(doc, 0.0f));
                    }
                }
            }
        }
        boolean firstSet = true;

        Set<String> keys = sets.keySet();
        Iterator<String> kIt = keys.iterator();
        while (kIt.hasNext()) {
            String key = kIt.next();
            List<DocWithScore> docs = sets.get(key);

            if (firstSet) {
                firstSet = false;
            } else {
                writer.write(',');
            }
            indent();
            writeKey(key, false);
            writer.write('[');
            incLevel();

            boolean firstDoc = true;
            for (Iterator iter = docs.iterator(); iter.hasNext();) {
                DocWithScore doc = (DocWithScore) iter.next();

                if (firstDoc) {
                    firstDoc = false;
                } else {
                    writer.write(',');
                }
                writer.write('{');

                boolean firstEntry = true;
                for (Fieldable ff : (List<Fieldable>) doc.getDoc().getFields()) {
                    String fname = ff.name();

                    if ((fname.equals("location")) || (fname.equals("title"))) { //$NON-NLS-1$ //$NON-NLS-2$
                        if (firstEntry) {
                            firstEntry = false;
                        } else {
                            writer.write(',');
                        }
                        indent();
                        writeKey(fname, false);
                        if (fname.equals("location")) { //$NON-NLS-1$
                            writeStr(null,
                                    transformToWebPath(ff.stringValue()), true);
                        } else {
                            writeStr(null, ff.stringValue(), true);
                        }
                    }
                }
                // write score
                if (includeScore) {
                    writer.write(',');
                    indent();
                    writeKey("score", false); //$NON-NLS-1$

                    Float score = doc.getScore() * 100;
                    int absScore = score.intValue();
                    writeInt(null, absScore);
                }
                indent();
                writer.write('}');
            }
            writer.write(']');
        }
        decLevel();
        indent();
        writer.write('}');

        if (otherFields != null) {
            writeMap(null, otherFields, true, false);
        }
        decLevel();
        indent();
        writer.write('}');
    }

    private String transformToWebPath(String loc) {
        if (loc.startsWith("jcr:///")) { //$NON-NLS-1$
            loc = loc.substring(7);
            if ((loc.startsWith("teamspaces")) && (loc.contains("/wiki"))) { //$NON-NLS-1$ //$NON-NLS-2$
                loc = getWebPath(loc.substring(loc.indexOf('/') + 1));
            } else if ((loc.startsWith("teamspaces")) //$NON-NLS-1$
                    && (loc.contains("/tasks"))) { //$NON-NLS-1$
                loc = getWebPath(loc.substring(loc.indexOf('/') + 1));
            }
        }
        return loc;
    }

    private String getWebPath(String loc) {
        String[] parts = loc.split("/"); //$NON-NLS-1$
        loc = '/' + parts[1] + '/' + parts[0];
        for (int i = 2; i < parts.length; i++) {
            loc += '/' + parts[i];
        }
        // remove .xml suffix if necessary
        if (loc.endsWith(".xml")) { //$NON-NLS-1$
            loc = loc.substring(0, loc.indexOf(".xml")); //$NON-NLS-1$
        }
        return loc;
    }
}
