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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.jcr.Node;
import javax.jcr.NodeIterator;

public class JaxenNodeIterator implements NodeIterator {
	private final List nodes;
	private final ListIterator iterator;
	
	public JaxenNodeIterator(Object object) {
		if (object instanceof List) {
			this.nodes = (List) object;
		} else {
			this.nodes = new ArrayList(0);
		}
		this.iterator = nodes.listIterator();
	}

	public Node nextNode() {
		return (Node) next();
	}

	public long getPosition() {
		return this.iterator.nextIndex();
	}

	public long getSize() {
		return this.nodes.size();
	}

	public void skip(long arg0) {
		for (int i=0;i<arg0;i++) {
			next();
		}
	}

	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	public Object next() {
		return this.iterator.next();
	}

	public void remove() {
		this.iterator.remove();
	}
}
