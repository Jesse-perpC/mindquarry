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

import org.apache.lucene.document.Document;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class DocWithScore {
    private Document doc;
    
    private float score;

    public DocWithScore(Document doc, float score) {
        this.doc = doc;
        this.score = score;
    }

    /**
     * Getter for doc.
     *
     * @return the doc
     */
    public Document getDoc() {
        return doc;
    }

    /**
     * Setter for doc.
     *
     * @param doc the doc to set
     */
    public void setDoc(Document doc) {
        this.doc = doc;
    }

    /**
     * Getter for score.
     *
     * @return the score
     */
    public float getScore() {
        return score;
    }

    /**
     * Setter for score.
     *
     * @param score the score to set
     */
    public void setScore(float score) {
        this.score = score;
    }
}
