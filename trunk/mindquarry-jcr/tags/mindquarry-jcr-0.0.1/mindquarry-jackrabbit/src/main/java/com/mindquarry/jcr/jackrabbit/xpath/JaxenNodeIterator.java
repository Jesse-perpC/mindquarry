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
