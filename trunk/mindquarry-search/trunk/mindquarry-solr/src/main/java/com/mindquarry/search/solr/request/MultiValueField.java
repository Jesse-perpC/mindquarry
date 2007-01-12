/*
 * Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
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
