/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package com.mindquarry.search.cocoon.filters.impl;

import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.slide.common.Domain;
import org.apache.slide.extractor.ContentExtractor;
import org.apache.slide.extractor.ExtractorException;

import com.mindquarry.search.cocoon.filters.FilterException;
import com.mindquarry.search.cocoon.filters.TextFilter;

public class SlideTextFilter implements TextFilter {
	private ContentExtractor extractor;
	
	public void setExtractor(ContentExtractor extractor) {
		this.extractor = extractor;   
        Domain.setInitialized(true);
	}

	public Map<String, Reader> doFilter(InputStream stream) throws FilterException {
		Map<String, Reader> contents = new HashMap<String, Reader>();
		try {
            contents.put(TextFilter.CONTENT, this.extractor.extract(stream));
        } catch (ExtractorException e) {
            throw new FilterException(e);
        }
		return contents;
	}

}
