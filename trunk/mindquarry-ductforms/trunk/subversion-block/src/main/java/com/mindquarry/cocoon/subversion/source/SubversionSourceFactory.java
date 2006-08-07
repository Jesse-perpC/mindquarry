/*
 * Copyright (C) 2006 Mindquarry GmbH, All rights reserved
 */
package com.mindquarry.cocoon.subversion.source;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceFactory;

/**
 * Implementation of the Subversion source factory example.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class SubversionSourceFactory implements SourceFactory {
	/**
	 * @see org.apache.excalibur.source.SourceFactory#getSource(java.lang.String,
	 *      java.util.Map)
	 */
	public Source getSource(String location, Map parameters)
			throws IOException, MalformedURLException {
		return (new SubversionSource());
	}

	/**
	 * @see org.apache.excalibur.source.SourceFactory#release(org.apache.excalibur.source.Source)
	 */
	public void release(Source source) {

	}
}
