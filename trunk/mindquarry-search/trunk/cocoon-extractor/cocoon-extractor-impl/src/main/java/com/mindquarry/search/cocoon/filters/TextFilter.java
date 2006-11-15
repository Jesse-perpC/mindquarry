package com.mindquarry.search.cocoon.filters;

import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

public interface TextFilter {
	/**
	 * The keys of the returned map are the field names, the values are
	 * Readers containing the content
	 * @param stream the input stream to filter
	 * @return the extracted data
	 */
	Map<String, Reader> doFilter(InputStream stream);
}
