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
package com.mindquarry.search.cocoon.filters;

import java.io.Reader;
import java.util.Map;

import org.apache.excalibur.source.Source;

public interface TextFilter {
	/**
	 * Use this for the full text
	 */
	public static final String CONTENT = "content";
    
    /**
     * Use this for the title, which will be used in search list results etc.
     */
    public static final String TITLE = "title";
    
//	public static final String ABSTRACT = "abstract";
//	public static final String AUTHOR = "author";
	
	/**
	 * The keys of the returned map are the field names, the values are
	 * Readers containing the content
	 * @param stream the input stream to filter
	 * @return the extracted data
	 */
	Map<String, Reader> doFilter(Source source) throws FilterException;
}
