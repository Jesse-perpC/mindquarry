package com.mindquarry.search.cocoon.filters;

import java.io.InputStream;
import java.util.Map;

public interface TextFilter {
	boolean canFilter(String mimetype);
	Map doFilter(InputStream stream);
}
