/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
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
