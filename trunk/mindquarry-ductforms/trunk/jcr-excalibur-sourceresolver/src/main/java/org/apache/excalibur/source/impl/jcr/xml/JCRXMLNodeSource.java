/**
 * 
 */
package org.apache.excalibur.source.impl.jcr.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.excalibur.source.impl.jcr.JCRNodeSource;
import org.apache.excalibur.source.impl.jcr.JCRSourceFactory;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * A source for an XML-izable JCR Node.
 *
 */
public class JCRXMLNodeSource extends JCRNodeSource implements XMLizable {

	public JCRXMLNodeSource(JCRSourceFactory factory, Session session,
			String path) throws SourceException {
		super(factory, session, path);
	}

	public JCRXMLNodeSource(JCRSourceFactory factory, Node node)
			throws SourceException {
		super(factory, node);
	}

	public JCRXMLNodeSource(JCRNodeSource parent, Node node)
			throws SourceException {
		super(parent, node);
	}

	public void toSAX(ContentHandler handler) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.jcr.source.JCRNodeSource#getInputStream()
	 */
	public InputStream getInputStream() throws IOException, SourceNotFoundException {
		// TODO Auto-generated method stub
		return super.getInputStream();
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.jcr.source.JCRNodeSource#getOutputStream()
	 */
	public OutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		// if this node is a leaf node with direct content,
		//   write the content directly as in the subclass into
		//   some content property of the node (see JCRSourceOutputStream)
		// else if this node has no children yet, parse xml and create substructure
		//
		// ??? How to overwrite existing XML structure ???
		// a) structure the same, different content
		// b) only a part
		// c) with additional elements
		// d) mixed up
		return super.getOutputStream();
	}

	
}
