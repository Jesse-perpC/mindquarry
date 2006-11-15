package com.mindquarry.search.cocoon.filters;

import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.slide.extractor.ContentExtractor;
import org.apache.slide.extractor.ExtractorException;

public class SlideTextFilter implements TextFilter {
	private ContentExtractor extractor;
	
	public void setExtractor(ContentExtractor extractor) {
		this.extractor = extractor;
	}

	public Map<String, Reader> doFilter(InputStream stream) throws ExtractorException {
		Map<String, Reader> contents = new HashMap<String, Reader>();
		contents.put(TextFilter.CONTENT, this.extractor.extract(stream));
		return contents;
	}

}
