/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
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
