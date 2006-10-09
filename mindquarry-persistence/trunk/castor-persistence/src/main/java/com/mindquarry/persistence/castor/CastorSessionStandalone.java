/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
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
