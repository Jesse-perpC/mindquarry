package com.mindquarry.cocoon.subversion;

import junit.framework.TestCase;

import org.tmatesoft.svn.core.SVNDirEntry;

public class SubversionBeanTest extends TestCase {
	public void testGetRepositoryEntries() {
		SubversionBean sb = new SubversionBean();
		SVNDirEntry[] entries = sb.getRepositoryEntries();
		for (SVNDirEntry entry : entries) {
			System.out.println(entry);
		}
	}
}
