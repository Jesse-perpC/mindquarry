/*
 * Copyright (C) 2006 Mindquarry GmbH, All rights reserved
 */
package com.mindquarry.cocoon.subversion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * Implementation Bean for the Subversion Block example to be used with Apache
 * Cocoon.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class SubversionBean {
	private static final String URL = "http://svn.apache.org/repos/asf/cocoon/trunk";

	public String getRepositoryUrl() {
		return (URL);
	}

	public SVNDirEntry[] getRepositoryEntries() {
		ArrayList<SVNDirEntry> result = new ArrayList<SVNDirEntry>();
		
		/*
         * for DAV (over http and https)
         */
        DAVRepositoryFactory.setup();
		/*
		 * setup for SVN (over svn and svn+ssh)
		 */
		SVNRepositoryFactoryImpl.setup();

		String name = "";
		String password = "";

		SVNRepository repository = null;
		try {
			/*
			 * Creates an instance of SVNRepository to work with the repository.
			 * All user's requests to the repository are relative to the
			 * repository location used to create this SVNRepository. SVNURL is
			 * a wrapper for URL strings that refer to repository locations.
			 */
			repository = SVNRepositoryFactory.create(SVNURL
					.parseURIEncoded(URL));
		} catch (SVNException svne) {
			/*
			 * Perhaps a malformed URL is the cause of this exception
			 */
			System.err
					.println("Error while creating an SVNRepository for location '"
							+ URL + "': " + svne.getMessage());
		}

		/*
		 * User's authentication information is provided via an
		 * ISVNAuthenticationManager instance. SVNWCUtil creates a default
		 * usre's authentication manager given user's name and password.
		 */
		ISVNAuthenticationManager authManager = SVNWCUtil
				.createDefaultAuthenticationManager(name, password);

		/*
		 * Sets the manager of the user's authentication information that will
		 * be used to authenticate the user to the server (if needed) during
		 * operations handled by the SVNRepository.
		 */
		repository.setAuthenticationManager(authManager);

		/*
		 * Sets the manager of the user's authentication information that will
		 * be used to authenticate the user to the server (if needed) during
		 * operations handled by the SVNRepository.
		 */
		repository.setAuthenticationManager(authManager);
		try {
			/*
			 * Checks up if the specified path/to/repository part of the URL
			 * really corresponds to a directory. If doesn't the program exits.
			 * SVNNodeKind is that one who says what is located at a path in a
			 * revision. -1 means the latest revision.
			 */
			SVNNodeKind nodeKind = repository.checkPath("", -1);
			if (nodeKind == SVNNodeKind.NONE) {
				System.err.println("There is no entry at '" + URL + "'.");
			} else if (nodeKind == SVNNodeKind.FILE) {
				System.err.println("The entry at '" + URL
						+ "' is a file while a directory was expected.");
			}
			
			/*
			 * Displays the repository tree at the current path - "" (what means
			 * the path/to/repository directory)
			 */
			String path = "";
			Collection entries = repository.getDir(path, -1, null,
					(Collection) null);
			Iterator iterator = entries.iterator();
			while (iterator.hasNext()) {
				result.add((SVNDirEntry) iterator.next());
			}
		} catch (SVNException svne) {
			System.err.println("error while listing entries: "
					+ svne.getMessage());
		}
		return (result.toArray(new SVNDirEntry[0]));
	}
}
