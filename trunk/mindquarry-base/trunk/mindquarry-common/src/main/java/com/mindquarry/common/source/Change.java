/*
 * Copyright (C) 2006-2007 MindQuarry GmbH, All Rights Reserved
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
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


