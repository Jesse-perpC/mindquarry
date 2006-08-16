/*
 * Copyright (C) 2006 Mindquarry GmbH, All rights reserved
 */
package com.mindquarry.cocoon.subversion.source;

import java.io.IOException;
import java.io.InputStream;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.source.SourceValidity;

/**
 * Implementation of the Subversion source example.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class SubversionSource implements Source {
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
}
