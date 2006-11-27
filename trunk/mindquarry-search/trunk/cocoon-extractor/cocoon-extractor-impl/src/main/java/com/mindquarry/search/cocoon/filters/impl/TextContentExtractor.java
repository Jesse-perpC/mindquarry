/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright 2004 The Apache Software Foundation 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.mindquarry.search.cocoon.filters.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.slide.extractor.AbstractContentExtractor;
import org.apache.slide.extractor.ExtractorException;


/**
 * Content extractor that simply returns the content. 
 */
public class TextContentExtractor extends AbstractContentExtractor{
    
    private static final String CONTENT_TYPE_TEXT = "text/plain,text/html";

    public TextContentExtractor(String uri, String contentType)
    {
        super(uri, contentType);
    }
    public TextContentExtractor(String uri, String contentType, String namespace)
    {
        super(uri, contentType, namespace);
    }

    public Reader extract(InputStream content) throws ExtractorException
    {
        return new InputStreamReader(content);
    }
    
    /* (non-Javadoc)
     * @see org.apache.slide.extractor.Extractor#getContentType()
     */
    public String getContentType() {
        if(super.getContentType()==null){
            return CONTENT_TYPE_TEXT;
        }   
        return super.getContentType();
    }

}
