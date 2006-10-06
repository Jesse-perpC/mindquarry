package com.mindquarry.persistence.xmlbeans.xml;

import java.util.List;

import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class XMLizableListWrapper implements XMLizable {
    
    private String uri_;
    private String localName_;
    private List<XMLizable> wrappedList_;
    
    public XMLizableListWrapper(String uri, String localName,
            List<XMLizable> xmlizableList) {
        uri_ = uri;
        localName_ = localName;
        wrappedList_ = xmlizableList;
    }

    public void toSAX(ContentHandler contentHandler) throws SAXException  {
        
        contentHandler.startElement(uri_, localName_, localName_, null); 
        
        for (XMLizable item : wrappedList_)
            item.toSAX(contentHandler);
        
        contentHandler.endElement(uri_, localName_, localName_);
    }
}