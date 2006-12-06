/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package com.mindquarry.webapp.transforming;

import java.io.IOException;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.ajax.BrowserUpdateTransformer;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.internal.EnvironmentHelper;
import org.apache.cocoon.transformation.AbstractSAXTransformer;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Transformer that looks for a (browser-update) redirection statement in the
 * XML stream and will redirect the browser to the given uri (HTTP redirect).
 * 
 * <p>
 * This allows you to encode redirect "decisions" in the XML stream, which is
 * more flexible than the rather static <code>&lt;redirect-to /&gt;</code>
 * inside a sitemap that cannot depend on XML content. Typical usage is when
 * block called via the block: protocol wants to tell the callee block that it
 * should do a redirection. This is only possible by encoding this information
 * inside the XML stream.
 * </p>
 * 
 * <p>
 * The namespace and the element is the same as the browser-update with the
 * action <code>redirect</code>, so you can use this transformer as the
 * fallback solution for non-AJAX cases (wrap it inside a
 * <code>AjaxRequestSelector</code>).
 * </p>
 * 
 * <p>
 * The XML this transformer will look for is very simple:
 * 
 * <pre>
 *      &lt;bu:redirect uri=&quot;foobar&quot; /&gt;
 * </pre>
 * 
 * The browser-update namespace for <code>bu</code> is:
 * <code>http://apache.org/cocoon/browser-update/1.0</code>
 * </p>
 * 
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 * 
 */
public class BrowserUpdateRedirectTransformer extends AbstractSAXTransformer {

    public static final String REDIRECT_ELEMENT = "redirect";

    public static final String URI_ATTRIBUTE = "uri";

    private boolean redirected = false;

    public BrowserUpdateRedirectTransformer() {
        super.defaultNamespaceURI = BrowserUpdateTransformer.BU_NSURI;
    }

    /**
     * Called when an element of the browser update namespace starts
     * 
     * @see BrowserUpdateTransformer.BU_NSURI
     */
    public void startTransformingElement(String uri, String name, String raw,
            Attributes attr) throws ProcessingException, IOException,
            SAXException {
        if (REDIRECT_ELEMENT.equals(name)) {
            // redirect the browser to the given URI
            Environment env = EnvironmentHelper.getCurrentEnvironment();
            env.tryResetResponse();
            env.redirect(attr.getValue(URI_ATTRIBUTE), false, false);
            this.redirected = true;
        }
    }

    public void startDocument() throws SAXException {
        // record everything, right from the beginning
        this.redirected = false;
        startSAXRecording();
    }

    public void endDocument() throws SAXException {
        XMLizable xml = endSAXRecording();

        // in case of no redirection, push all recorded events down the pipeline
        if (!this.redirected) {
            this.contentHandler.startDocument();
            xml.toSAX(this.xmlConsumer);
            this.contentHandler.endDocument();
        }
    }
}
