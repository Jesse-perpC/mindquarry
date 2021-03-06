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
package com.mindquarry.search.cocoon.filters.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.source.TraversableSource;
import org.apache.slide.common.Domain;
import org.apache.slide.extractor.ContentExtractor;
import org.apache.slide.extractor.ExtractorException;

import com.mindquarry.search.cocoon.filters.FilterException;
import com.mindquarry.search.cocoon.filters.TextFilter;
import com.mindquarry.search.cocoon.filters.TitleExtractor;

public class SlideTextFilter implements TextFilter {
	private ContentExtractor extractor;
    private TitleExtractor titleExtractor;
	
	public void setExtractor(ContentExtractor extractor) {
		this.extractor = extractor;   
        Domain.setInitialized(true);
	}
    
    public void setTitleExtractor(TitleExtractor titleExtractor) {
        this.titleExtractor = titleExtractor;
    }

	public Map<String, Reader> doFilter(Source source) throws FilterException {
		Map<String, Reader> contents = new HashMap<String, Reader>();
		try {
            if (this.extractor != null) {
                contents.put(TextFilter.CONTENT, this.extractor.extract(source.getInputStream()));
            }
            if (this.titleExtractor != null) {
                contents.put(TextFilter.TITLE, this.titleExtractor.extract(source.getInputStream()));
            } else {
                // fallback, use title from the path
                contents.put(TextFilter.TITLE, new StringReader(getTitleFromFilename(source)));
            }
        } catch (ExtractorException e) {
            throw new FilterException(e);
        } catch (SourceNotFoundException e) {
            throw new FilterException(e);
        } catch (IOException e) {
            throw new FilterException(e);
        }
		return contents;
	}

    private String getTitleFromFilename(Source source) {
        String filename;
        if (source instanceof TraversableSource) {
            filename = ((TraversableSource) source).getName();
        } else {
            try {
                URL url = new URL(source.getURI());
                filename = url.getFile();
            } catch (MalformedURLException e) {
                return "";
            }
        }
        
        // the filename might have an extension, remove it
        int dotPos = filename.lastIndexOf("."); 
        if (dotPos >= 0) {
            filename = filename.substring(0, dotPos);
        }

        return filename;
    }
}
