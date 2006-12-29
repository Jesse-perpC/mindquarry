/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.search.solr.request;

import java.util.ArrayList;

import org.apache.lucene.document.Fieldable;
import org.apache.solr.schema.SchemaField;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class MultiValueField {
    final SchemaField sfield;

    final ArrayList<Fieldable> fields;

    MultiValueField(SchemaField sfield, Fieldable firstVal) {
        this.sfield = sfield;
        this.fields = new ArrayList<Fieldable>(4);
        this.fields.add(firstVal);
    }
}
