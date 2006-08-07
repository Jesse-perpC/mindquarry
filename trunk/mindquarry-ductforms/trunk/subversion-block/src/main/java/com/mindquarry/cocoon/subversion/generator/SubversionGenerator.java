/*
 * Copyright (C) 2006 Mindquarry GmbH, All rights reserved
 */
package com.mindquarry.cocoon.subversion.generator;

import java.io.IOException;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.generation.AbstractGenerator;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.mindquarry.cocoon.subversion.SubversionBean;

/**
 * Implementation of the Subversion generator example to be used with Apache
 * Cocoon.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class SubversionGenerator extends AbstractGenerator {
	private static final AttributesImpl emptyAttr = new AttributesImpl();

	/**
	 * @see org.apache.cocoon.generation.AbstractGenerator#generate()
	 */
	public void generate() throws IOException, SAXException,
			ProcessingException {
		SubversionBean sb = new SubversionBean();

		// initialize document
		contentHandler.startDocument();
		contentHandler.startElement("", "repository", "repository", emptyAttr);

		// add URL element
		contentHandler.startElement("", "url", "url", emptyAttr);
		contentHandler.characters(sb.getRepositoryUrl().toCharArray(), 0, sb
				.getRepositoryUrl().length());
		contentHandler.endElement("", "url", "url");

		// add svn entries
		contentHandler.startElement("", "entries", "entries", emptyAttr);
		SVNDirEntry[] entries = sb.getRepositoryEntries();
		for (SVNDirEntry entry : entries) {
			contentHandler.startElement("", "entry", "entry", emptyAttr);

			// add name entry element
			contentHandler.startElement("", "name", "name", emptyAttr);
			contentHandler.characters(entry.getName().toCharArray(), 0, entry
					.getName().length());
			contentHandler.endElement("", "name", "name");

			// add author entry element
			contentHandler.startElement("", "author", "author", emptyAttr);
			contentHandler.characters(entry.getAuthor().toCharArray(), 0, entry
					.getAuthor().length());
			contentHandler.endElement("", "author", "author");

			// add revision entry element
			contentHandler.startElement("", "revision", "revision", emptyAttr);
			contentHandler.characters(String.valueOf(entry.getRevision())
					.toCharArray(), 0, String.valueOf(entry.getRevision())
					.length());
			contentHandler.endElement("", "revision", "revision");
			contentHandler.endElement("", "entry", "entry");
		}
		// finish document
		contentHandler.endElement("", "entries", "entries");
		contentHandler.endElement("", "repository", "repository");
		contentHandler.endDocument();
	}
}
