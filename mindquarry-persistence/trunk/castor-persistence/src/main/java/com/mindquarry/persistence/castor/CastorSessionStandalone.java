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
package com.mindquarry.persistence.castor;

import java.io.Reader;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

import com.mindquarry.common.persistence.EntityBase;
import com.mindquarry.persistence.castor.config.PersistenceConfiguration;
import com.mindquarry.persistence.castor.source.JcrSourceResolverBase;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class CastorSessionStandalone extends CastorSession {
    
    public CastorSessionStandalone(final Mapping mapping,
            final PersistenceConfiguration configuration, 
            final JcrSourceResolverBase jcrSourceResolver) {
        
        super(mapping, configuration, jcrSourceResolver);
    }
    
    public void persistFromReader(Reader reader) {
        Unmarshaller unmarshaller = new Unmarshaller();
        try {
            unmarshaller.setMapping(mapping_);
        } catch (MappingException e) {
            throw new CastorPersistenceException(
                    "failure in castor-mapping", e);
        }
        
        EntityBase entity;
        try {
            entity = (EntityBase) unmarshaller.unmarshal(reader);
            createdEntities_.add(entity);
        } catch (MarshalException e) {
            throw new CastorPersistenceException(
                    "could not unmarshal to entity", e);
        } catch (ValidationException e) {
            throw new CastorPersistenceException(
                    "could not unmarshal to entityg", e);
        }
    }
    
    public void deleteFromReader(Reader reader) {
        Unmarshaller unmarshaller = new Unmarshaller();
        try {
            unmarshaller.setMapping(mapping_);
        } catch (MappingException e) {
            throw new CastorPersistenceException(
                    "failure in castor-mapping", e);
        }
        
        EntityBase entity;
        try {
            entity = (EntityBase) unmarshaller.unmarshal(reader);
            deletedEntities_.add(entity);
        } catch (MarshalException e) {
            throw new CastorPersistenceException(
                    "could not unmarshal to entity", e);
        } catch (ValidationException e) {
            throw new CastorPersistenceException(
                    "could not unmarshal to entityg", e);
        }
    }
}
