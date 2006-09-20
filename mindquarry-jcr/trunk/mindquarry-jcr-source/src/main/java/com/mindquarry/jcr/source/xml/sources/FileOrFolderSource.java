/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.source.xml.sources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import javax.jcr.Session;

import org.apache.excalibur.source.ModifiableTraversableSource;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceNotFoundException;

import com.mindquarry.jcr.source.xml.JCRXMLSourceFactory;

/**
 * Source for a node that represents a file (nt:file) or a folder (nt:folder).
 * 
 * @author <a
 *         href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">Alexander
 *         Klimetschek</a>
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class FileOrFolderSource extends AbstractJCRNodeSource implements
		ModifiableTraversableSource {
    /**
     * Default constructor.
     */
	public FileOrFolderSource(JCRXMLSourceFactory factory, Session session,
			String path) throws SourceException {
		super(factory, session, path);
	}

	// =========================================================================
	// Source interface
	// =========================================================================

    /**
     * @see org.apache.excalibur.source.Source#getInputStream()
     */
    public InputStream getInputStream() throws IOException,
            SourceNotFoundException {
        return null;
    }
    
	/**
	 * @see org.apache.excalibur.source.Source#getContentLength()
	 */
	public long getContentLength() {
		return -1;
	}

	// =========================================================================
	// ModifiableTraversableSource interface
	// =========================================================================

	/** 
	 * @see org.apache.excalibur.source.ModifiableTraversableSource#makeCollection()
	 */
	public void makeCollection() throws SourceException {
	}

	// =========================================================================
	// ModifiableSource interface
	// =========================================================================

	/** 
	 * @see org.apache.excalibur.source.ModifiableSource#canCancel(java.io.OutputStream)
	 */
	public boolean canCancel(OutputStream stream) {
		return false;
	}

	/**
	 * @see org.apache.excalibur.source.ModifiableSource#cancel(java.io.OutputStream)
	 */
	public void cancel(OutputStream stream) throws IOException {
		
	}

	/**
	 * @see org.apache.excalibur.source.ModifiableSource#delete()
	 */
	public void delete() throws SourceException {
		
	}

	/**
	 * @see org.apache.excalibur.source.ModifiableSource#getOutputStream()
	 */
	public OutputStream getOutputStream() throws IOException {
		return null;
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
