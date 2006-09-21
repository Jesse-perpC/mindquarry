/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.source.xml.sources.stream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.mindquarry.jcr.source.xml.handler.SAXToJCRNodesConverter;
import com.mindquarry.jcr.source.xml.sources.XMLFileSource;

/**
 * OutputStream to be used for writing to the {@link XMLFileSource}.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class XMLFileOutputStream extends ByteArrayOutputStream {
    private boolean isClosed = false;

    private final Node node;

    private final Session session;

    public XMLFileOutputStream(Node node, Session session) {
        this.node = node;
        this.session = session;
    }

    /**
     * @see java.io.ByteArrayOutputStream#close()
     */
    @Override
    public void close() throws IOException {
        if (!isClosed) {
            super.close();
            isClosed = true;

            try {
                // remove old content
                NodeIterator nit = node.getNode("jcr:content").getNodes();
                while (nit.hasNext()) {
                    nit.nextNode().remove();
                }
                session.save();
                
                // get content
                ByteArrayInputStream is = new ByteArrayInputStream(this
                        .toByteArray());
                
                // parse and store content
                SAXParserFactory saxFactory = SAXParserFactory.newInstance();
                SAXParser parser = saxFactory.newSAXParser();
                parser.parse(is, new SAXToJCRNodesConverter(node));
                session.save();
            } catch (Exception e) {
                throw new IOException("Cannot parse content.");
            }
        }
    }

    public boolean canCancel() {
        return !isClosed;
    }

    public void cancel() throws IOException {
        if (isClosed) {
            throw new IllegalStateException("Cannot cancel: "
                    + "outputstrem is already closed");
        }
    }
}
