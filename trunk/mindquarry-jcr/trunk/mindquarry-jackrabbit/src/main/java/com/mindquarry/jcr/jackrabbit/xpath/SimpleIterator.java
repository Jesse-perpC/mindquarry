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
