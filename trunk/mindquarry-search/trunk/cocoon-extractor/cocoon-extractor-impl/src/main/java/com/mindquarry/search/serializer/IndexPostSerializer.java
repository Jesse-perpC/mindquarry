/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.search.serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.cocoon.CascadingIOException;
import org.apache.cocoon.serialization.AbstractTextSerializer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Add summary documentation here.
 *
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class IndexPostSerializer extends AbstractTextSerializer {
    DOMResult res = new DOMResult();
    /**
     * Set the configurations for this serializer.
     */
    public void configure(Configuration conf)
    throws ConfigurationException {
        super.configure( conf );
        this.format.put(OutputKeys.METHOD,"xml");
    }

    /**
     * Set the {@link OutputStream} where the requested resource should
     * be serialized.
     */
    public void setOutputStream(OutputStream out) throws IOException {
        super.setOutputStream(out);
        try {
            TransformerHandler handler = this.getTransformerHandler();
            handler.getTransformer().setOutputProperties(this.format);
            res = new DOMResult();
            handler.setResult(res);
            //handler.setResult(new StreamResult(this.output));
            this.setContentHandler(handler);
            this.setLexicalHandler(handler);
        } catch (Exception e) {
            final String message = "Cannot set XMLSerializer outputstream"; 
            throw new CascadingIOException(message, e);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        
        Node node = this.res.getNode();
        Element root =  node.getOwnerDocument().getDocumentElement();
        String action =root.getAttribute("action");
        
        NodeList children = root.getChildNodes();
        
        if (true) { //should resolve the action and find out wether it is a
            //cycle through all children of the root element
            for(int i=0;i<children.getLength();i++) {
                Node child = children.item(i);
                //for every child element...
                if (child instanceof Element) {

                    //block source which can be posted by creating a new servlet request
                    HttpClient client = new HttpClient();
                    PostMethod pMethod = new PostMethod(action);
                    pMethod.setDoAuthentication(true);
                    pMethod.addRequestHeader("accept", "text/xml"); //$NON-NLS-1$ //$NON-NLS-2$
                    
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    TransformerFactory tf = TransformerFactory.newInstance();
                    try {
                        tf.newTransformer().transform(new DOMSource(child), new StreamResult(baos));
                        pMethod.setRequestEntity(new ByteArrayRequestEntity(baos.toByteArray()));
                        client.executeMethod(pMethod);
                        
                    } catch (TransformerConfigurationException e) {
                        getLogger().error("Failed to configure transformer prior to posting", e);
                    } catch (TransformerException e) {
                        getLogger().error("Failed to transform prior to posting", e);
                    } catch (HttpException e) {
                        getLogger().error("Failed to post", e);
                    } catch (IOException e) {
                        getLogger().error("Something went wrong", e);
                    }
                    
                    //pMethod.setRequestEntity();
                }
            }
        }
    }
}
