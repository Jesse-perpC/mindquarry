/*
 * Coypright (c) 2006 Mindquarry GmbH
 */
package com.mindquarry.source.jcr.xml.sources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.jcr.NodeIterator;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.TraversableSource;

import com.mindquarry.source.jcr.xml.JCRXMLSourceFactory;

/**
 * Source implementation for XPath query results.
 * 
 * @author <a
 *         href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">Alexander
 *         Klimetschek</a>
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class QueryResultSource implements TraversableSource {
	/** The factory that created this Source */
	protected final JCRXMLSourceFactory factory;

	/** The node pointed to by this source (can be null) */
	protected NodeIterator nit;

	/**
	 * Default contructor. Takes the source factory that has created this source
	 * and a node iterator containing the result nodes as input.
	 * 
	 * @param factory
	 *            the source factory
	 * @param nit
	 *            the node iterator
	 */
	public QueryResultSource(JCRXMLSourceFactory factory, NodeIterator nit) {
		this.factory = factory;
		this.nit = nit;
	}

	// =========================================================================
	// Source interface
	// =========================================================================

	/**
	 * @see org.apache.excalibur.source.Source#exists()
	 */
	public boolean exists() {
		return false;
	}

	/**
	 * @see org.apache.excalibur.source.Source#getContentLength()
	 */
	public long getContentLength() {
		return 0;
	}

	/**
	 * @see org.apache.excalibur.source.Source#getInputStream()
	 */
	public InputStream getInputStream() throws IOException,
			SourceNotFoundException {
		return null;
	}

	/**
	 * @see org.apache.excalibur.source.Source#getLastModified()
	 */
	public long getLastModified() {
		return 0;
	}

	/**
	 * @see org.apache.excalibur.source.Source#getMimeType()
	 */
	public String getMimeType() {
		return null;
	}

	/**
	 * @see org.apache.excalibur.source.Source#getScheme()
	 */
	public String getScheme() {
		return null;
	}

	/**
	 * @see org.apache.excalibur.source.Source#getURI()
	 */
	public String getURI() {
		return null;
	}

	/**
	 * @see org.apache.excalibur.source.Source#getValidity()
	 */
	public SourceValidity getValidity() {
		return null;
	}

	/**
	 * @see org.apache.excalibur.source.Source#refresh()
	 */
	public void refresh() {
	}

	// =========================================================================
	// TraversableSource interface
	// =========================================================================

	/**
	 * @see org.apache.excalibur.source.TraversableSource#getChild(java.lang.String)
	 */
	public Source getChild(String name) throws SourceException {
		return null;
	}

	/**
	 * @see org.apache.excalibur.source.TraversableSource#getChildren()
	 */
	public Collection getChildren() throws SourceException {
		return null;
	}

	/**
	 * @see org.apache.excalibur.source.TraversableSource#getName()
	 */
	public String getName() {
		return null;
	}

	/**
	 * @see org.apache.excalibur.source.TraversableSource#getParent()
	 */
	public Source getParent() throws SourceException {
		return null;
	}

	/**
	 * @see org.apache.excalibur.source.TraversableSource#isCollection()
	 */
	public boolean isCollection() {
		return false;
	}
}
