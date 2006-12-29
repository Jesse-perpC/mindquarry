package com.mindquarry.search.cocoon.filters;

import java.io.InputStream;
import java.io.Reader;

import org.apache.slide.extractor.ExtractorException;

public interface TitleExtractor {
    
    public Reader extract(InputStream stream) throws ExtractorException;

}
