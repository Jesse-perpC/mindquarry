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
package com.mindquarry.persistence.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Each entity class that should be managed by the persistence layer
 * has to be annotated with this 'Entity' annotation.  
 * The mandatory parentFolder property defines a name for a folder
 * within the jcr layer that should contains the documents for the entity;
 * persisting an entity (an instance of an entity class) is transforming
 * the entity into a virtual document by using JCR.
 * Each entity class is required to declare an Id annotated 
 * property. The value of this property is used to name and identify
 * the document within the parentFolder.
 * 
 * 
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
    String parentFolder();
    boolean asComposite() default false;
}
