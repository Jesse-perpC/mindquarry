/*
 * Copyright (C) 2006 Mindquarry GmbH, All rights reserved
 */
package com.mindquarry.cocoon.subversion.generator;

import java.io.IOException;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.generation.AbstractGenerator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Implementation of the Subversion generator example to be used with Apache
 * Cocoon.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class SubversionGenerator extends AbstractGenerator {
	private static final String URL = "http://svn.apache.org/repos/asf/cocoon/trunk";

	private AttributesImpl emptyAttr = new AttributesImpl();

	/**
	 * @see org.apache.cocoon.generation.AbstractGenerator#generate()
	 */
	public void generate() throws IOException, SAXException,
			ProcessingException {
		// initialize document
		contentHandler.startDocument();
		contentHandler.startElement("", "repository", "repository", emptyAttr);

		// add URL element
		contentHandler.startElement("", "url", "url", emptyAttr);
		contentHandler.characters(URL.toCharArray(), 0, URL.length());
		contentHandler.endElement("", "url", "url");

		// add svn entries
		contentHandler.startElement("", "entries", "entries", emptyAttr);
		contentHandler.endElement("", "entries", "entries");

		// finish document
		contentHandler.endElement("", "repository", "repository");
		contentHandler.endDocument();
	}
}
