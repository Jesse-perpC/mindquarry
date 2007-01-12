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
package com.mindquarry.jcr.jackrabbit.xpath;

import java.util.Iterator;

public class SimpleIterator implements Iterator {
	private Object content = null;

	public SimpleIterator() {
		this.content = null;
	}
	public SimpleIterator(Object o) {
		this.content  = o;
	}
	
	public boolean hasNext() {
		return this.content!=null;
	}

	public Object next() {
		return this.content;
	}

	public void remove() {
		this.content = null;
	}

}
