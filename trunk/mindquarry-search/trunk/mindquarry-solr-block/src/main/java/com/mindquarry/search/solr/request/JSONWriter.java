/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.search.solr.request;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrQueryResponse;
import org.apache.solr.request.TextResponseWriter;
import org.apache.solr.search.DocList;
import org.apache.solr.util.NamedList;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:your-email-address">your full name</a>
 */
public class JSONWriter extends TextResponseWriter {
    private String namedListStyle;
    private String wrapperFunction;

    private static final String JSON_NL_STYLE="json.nl";
    private static final String JSON_NL_MAP="map";
    private static final String JSON_NL_ARROFARR="arrarr";
    private static final String JSON_NL_ARROFMAP="arrmap";
    private static final String JSON_WRAPPER_FUNCTION="json.wrf";


    public JSONWriter(Writer writer, SolrQueryRequest req, SolrQueryResponse rsp) {
      super(writer, req, rsp);
      namedListStyle = req.getParam(JSON_NL_STYLE);
      namedListStyle = namedListStyle==null ? JSON_NL_MAP : namedListStyle.intern();
      wrapperFunction = req.getParam(JSON_WRAPPER_FUNCTION);
    }
    
    public void writeResponse() throws IOException {
        
    }

    /**
     * @see org.apache.solr.request.TextResponseWriter#writeArray(java.lang.String, java.lang.Object[])
     */
    @Override
    public void writeArray(String arg0, Object[] arg1) throws IOException {
        
    }

    /**
     * @see org.apache.solr.request.TextResponseWriter#writeArray(java.lang.String, java.util.Collection)
     */
    @Override
    public void writeArray(String arg0, Collection arg1) throws IOException {
        
    }

    @Override
    public void writeBool(String arg0, String arg1) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeDate(String arg0, Date arg1) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeDate(String arg0, String arg1) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeDoc(String arg0, Document arg1, Set<String> arg2, float arg3, boolean arg4) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeDocList(String arg0, DocList arg1, Set<String> arg2, Map arg3) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeDouble(String arg0, String arg1) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeFloat(String arg0, String arg1) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeInt(String arg0, String arg1) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeLong(String arg0, String arg1) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeMap(String arg0, Map arg1, boolean arg2, boolean arg3) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeNamedList(String arg0, NamedList arg1) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeNull(String arg0) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeStr(String arg0, String arg1, boolean arg2) throws IOException {
        // TODO Auto-generated method stub
        
    }    
}
