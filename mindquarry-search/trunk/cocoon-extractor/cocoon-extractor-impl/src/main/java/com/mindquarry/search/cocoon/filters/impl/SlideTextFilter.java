/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package com.mindquarry.search.cocoon.filters.impl;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceNotFoundException;
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

}
