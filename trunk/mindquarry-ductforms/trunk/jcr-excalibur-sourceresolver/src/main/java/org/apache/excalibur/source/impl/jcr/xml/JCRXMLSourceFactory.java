/**
 * 
 */
package org.apache.excalibur.source.impl.jcr.xml;

import java.util.HashSet;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.excalibur.source.impl.jcr.JCRNodeSource;
import org.apache.excalibur.source.impl.jcr.JCRSourceFactory;
import org.apache.excalibur.source.SourceException;

/**
 * This implementation extends <code>JCRSourceFactory</code> to provide
 * XML-izable <code>JCRXMLNodeSource</code>s.
 *
 */
public class JCRXMLSourceFactory extends JCRSourceFactory {

	/**
	 * A hash containing all XML elements that should be versionized
	 */
	protected HashSet versionizedElements;
	
	public void configure(Configuration config) throws ConfigurationException {
		this.versionizedElements = new HashSet();
		
        Configuration[] children = config.getChildren();

        for (int i = 0; i < children.length; i++) {
            Configuration child = children[i];
            String name = child.getName();

            if ("versionize".equals(name)) {
            	this.versionizedElements.add(child.getAttribute("element"));
            }
        }
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.jcr.source.JCRSourceFactory#createSource(org.apache.cocoon.jcr.source.JCRNodeSource, javax.jcr.Node)
	 */
	public JCRNodeSource createSource(JCRNodeSource parent, Node node) throws SourceException {
		return new JCRXMLNodeSource(parent, node);
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.jcr.source.JCRSourceFactory#createSource(javax.jcr.Session, java.lang.String)
	 */
	public JCRNodeSource createSource(Session session, String path) throws SourceException {
		return new JCRXMLNodeSource(this, session, path);
	}
	
	

}
