/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package com.mindquarry.webapp.transforming;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.components.source.SourceUtil;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.ServiceableTransformer;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceValidity;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 *
 */
public class ClearAndGenerateTransformer extends ServiceableTransformer {

    /** The input source */
    protected Source inputSource;

    /**
     * Recycle this component.
     * All instance variables are set to <code>null</code>.
     */
    public void recycle() {
        if (null != this.inputSource) {
            super.resolver.release(this.inputSource);
            this.inputSource = null;
        }
        super.recycle();
    }

    /* (non-Javadoc)
     * @see org.apache.cocoon.sitemap.SitemapModelComponent#setup(org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
     */
    public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par)
    throws ProcessingException, SAXException, IOException {

        super.setup(resolver, objectModel, src, par);
        try {
            this.inputSource = super.resolver.resolveURI(src);
        } catch (SourceException se) {
            throw SourceUtil.handle("Error during resolving of '" + src + "'.", se);
        }

    }

    /**
     * Generate the unique key.
     * This key must be unique inside the space of this component.
     *
     * @return The generated key hashes the src
     */
    public Serializable getKey() {
        return this.inputSource.getURI();
    }

    /**
     * Generate the validity object.
     *
     * @return The generated validity object or <code>null</code> if the
     *         component is currently not cacheable.
     */
    public SourceValidity getValidity() {
        return this.inputSource.getValidity();
    }

    /**
     * At the end of the document (after we have silently ignored all events, so
     * that the preceeding transformer could do its work without disturbance),
     * we start to inject the events from our source..
     */
    public void endDocument()
    throws SAXException {
        try {
            try {
                if (getLogger().isDebugEnabled()) {
                    getLogger().debug("Source " + super.source +
                                      " resolved to " + this.inputSource.getURI());
                }
                SourceUtil.parse(this.manager, this.inputSource, super.xmlConsumer);
            } catch (SAXException e) {
                SourceUtil.handleSAXException(this.inputSource.getURI(), e);
            }
        } catch (ProcessingException e) {
            throw new SAXException("ClearAndGenerateTransformer: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new SAXException("ClearAndGenerateTransformer: " + e.getMessage(), e);
        }
    }


    /**
     * Ignore all other SAX events.
     */

    public void setDocumentLocator(Locator locator) {
    }

    public void startDocument()
    throws SAXException {
    }

    public void startPrefixMapping(String prefix, String uri)
    throws SAXException {
    }

    public void endPrefixMapping(String prefix)
    throws SAXException {
    }

    public void startElement(String uri, String loc, String raw, Attributes a)
    throws SAXException {
    }

    public void endElement(String uri, String loc, String raw)
    throws SAXException {
    }

    public void characters(char c[], int start, int len)
    throws SAXException {
    }

    public void ignorableWhitespace(char c[], int start, int len)
    throws SAXException {
    }

    public void processingInstruction(String target, String data)
    throws SAXException {
    }

    public void skippedEntity(String name)
    throws SAXException {
    }

    public void startDTD(String name, String publicId, String systemId)
    throws SAXException {
    }

    public void endDTD()
    throws SAXException {
    }

    public void startEntity(String name)
    throws SAXException {
    }

    public void endEntity(String name)
    throws SAXException {
    }

    public void startCDATA()
    throws SAXException {
    }

    public void endCDATA()
    throws SAXException {
    }

    public void comment(char ch[], int start, int len)
    throws SAXException {
    }
}
