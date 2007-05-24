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
package com.mindquarry.common.fp;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class FP {

    public static <I, O> List<O> collect(UnaryFunction<I, O> f, List<I> list) {
        List<O> result = new LinkedList<O>();
        for (I i : list) 
            result.add(f.execute(i));
        return result;
    }
    
    public static <T> List<T> select(UnaryPredicate<T> p, List<T> list) {
        List<T> result = new LinkedList<T>();
        for (T o : list) {
            if (p.execute(o))
                result.add(o);
        }
        return result;
    }
    
    public static <T> UnaryPredicate<T> not(UnaryPredicate<T> innerPredicate) {
        return new Not<T>(innerPredicate);
    }
    
    public static <I, K> Map<K, I> mapBy(UnaryFunction<I, K> f, List<I> list) {
        Map<K, I> result = new HashMap<K, I>();
        for (I i : list) 
            result.put(f.execute(i), i);
        return result;
    }
}
