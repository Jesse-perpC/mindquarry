/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.teamspace.manager;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">your full name</a>
 */
public class LazyLoadList<T> implements List<T> {

    private List<T> proxiedList_;
    private ListLoading<T> listLoader_;
    
    public LazyLoadList(ListLoading<T> listLoader) {
        listLoader_ = listLoader;
    }
    
    private List<T> proxiedList() {
        if (null == proxiedList_) {
            proxiedList_ = listLoader_.load();
        }
        return proxiedList_;
    }

    public boolean add(T o) {
        return proxiedList().add(o);
    }

    public void add(int index, T element) {
        proxiedList().add(index, element);
    }

    public boolean addAll(Collection<? extends T> c) {
        return proxiedList().addAll(c);
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        return proxiedList().addAll(index, c);
    }

    public void clear() {
        proxiedList().clear();
    }

    public boolean contains(Object o) {
        return proxiedList().contains(o);
    }

    public boolean containsAll(Collection<?> c) {
        return proxiedList().containsAll(c);
    }

    public T get(int index) {
        return proxiedList().get(0);
    }

    public int indexOf(Object o) {
        return proxiedList().indexOf(o);
    }

    public boolean isEmpty() {
        return proxiedList().isEmpty();
    }

    public Iterator<T> iterator() {
        return proxiedList().iterator();
    }

    public int lastIndexOf(Object o) {
        return proxiedList().lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return proxiedList().listIterator();
    }

    public ListIterator<T> listIterator(int index) {
        return proxiedList().listIterator(index);
    }

    public boolean remove(Object o) {
        // TODO Auto-generated method stub
        return false;
    }

    public T remove(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean removeAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean retainAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    public T set(int index, T element) {
        // TODO Auto-generated method stub
        return null;
    }

    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    public List<T> subList(int fromIndex, int toIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("hiding")
    public <T> T[] toArray(T[] a) {
        return proxiedList().toArray(a);
    }
}
