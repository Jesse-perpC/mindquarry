/*
 * Copyright (C) 2006-2007 Mindquarry GmbH, All Rights Reserved
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

import java.util.List;

import org.apache.excalibur.source.SourceException;

/**
 * A source that is versionable and saves changes 
 * amongst their lifecycle. 
 *  
 * @author 
 * <a href="bastian(dot)steinert(at)mindquarry(dot)com">Bastian Steinert</a>
 */
public interface ChangeableSource extends RevisableSource {

	/**
	 * get a list of changes from specified start revision until
	 * the revision of this source
	 * 
	 * @param startRevision (must be 0 or higher)
	 * @param nMaxChanges - number of maximum change items to return
	 * 						(if 0 no limit)
	 * @return a list of change items
	 * @throws SourceException
	 */
	List<Change> changesFrom(long startRevision, long nMaxChanges)
			throws SourceException;
}
