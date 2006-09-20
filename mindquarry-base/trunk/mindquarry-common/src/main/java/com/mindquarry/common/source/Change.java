/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.common.source;

import java.util.Date;

/**
 *
 * @author 
 * <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public class Change {

    private final Date date_;
    private final String author_;
    private final String message_;
    private final String revision_;
    private String[] changedPaths_ = null;
    private final RevisedPath[] affectedPaths_;

	/**
	 * @param date
	 * @param author
	 * @param message
	 * @param revision
	 * @param changedPaths
	 */
	public Change(final Date date, final String author, final String message, 
            final String revision, final RevisedPath[] changedPaths) {
		
		super();
		date_ = date;
		author_ = author;
		message_ = message;
		revision_ = revision;
		affectedPaths_ = changedPaths;
	}

	public String getAuthor() {
		return author_;
	}

	public String[] getChangedPaths() {
		if (changedPaths_ == null) {
		    changedPaths_ = new String[affectedPaths_.length];
        }
		for (int i = 0; i < affectedPaths_.length; i++) {
			changedPaths_[i] = affectedPaths_[i].getPath();
		}
		return changedPaths_;
	}
	
	public RevisedPath[] getRevisedPaths() {
		return this.affectedPaths_;
	}

	public Date getDate() {
		return date_;
	}

	public String getMessage() {
		return message_;
	}

	public String getRevision() {
		return revision_;
	}
}


