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


/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class Not<T> implements UnaryPredicate<T> {

    private UnaryPredicate<T> innerPredicate_;
    
    public Not(UnaryPredicate<T> innerPredicate) {
        innerPredicate_ = innerPredicate;
    }
    
    /**
     * @see com.mindquarry.common.fp.UnaryPredicate#execute(java.lang.Object)
     */
    public boolean execute(T input) {
        return ! innerPredicate_.execute(input);
    }

}
