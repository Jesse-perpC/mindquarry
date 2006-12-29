/**
 * Copyright (C) 2005 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.persistence.castor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.excalibur.source.ModifiableSource;
import org.apache.excalibur.source.ModifiableTraversableSource;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.source.TraversableSource;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

import com.mindquarry.common.persistence.EntityBase;
import com.mindquarry.common.persistence.PersistenceException;
import com.mindquarry.common.persistence.Session;
import com.mindquarry.persistence.castor.config.PersistenceConfiguration;
import com.mindquarry.persistence.castor.source.JcrSourceResolverBase;

/**
 * Add summary documentation here.
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class CastorSession extends AbstractLogEnabled implements Session {

    protected final Mapping mapping_;
    private final PersistenceConfiguration configuration_;    
    private final JcrSourceResolverBase jcrSourceResolver_;
    
    protected Set<EntityBase> createdEntities_;
    protected Set<EntityBase> updatedEntities_;
    private Set<EntityBase> queriedEntities_;
    protected Set<EntityBase> deletedEntities_;
    
    public CastorSession(final Mapping mapping,
            final PersistenceConfiguration configuration, 
            final JcrSourceResolverBase jcrSourceResolver) {
        
        mapping_ = mapping;
        configuration_ = configuration;
        jcrSourceResolver_ = jcrSourceResolver;
        
        createdEntities_ = new HashSet<EntityBase>();
        updatedEntities_ = new HashSet<EntityBase>();
        queriedEntities_ = new HashSet<EntityBase>();
        deletedEntities_ = new HashSet<EntityBase>();
    }
    
    public Object newEntity(Class entityClazz) {
        validateEntityClazz(entityClazz);
        try {
            EntityBase result = (EntityBase) entityClazz.newInstance();
            createdEntities_.add(result);
            return result;
        } catch (InstantiationException e) {
            throw CastorPersistenceException.newInstanceFailed(entityClazz, e);
        } catch (IllegalAccessException e) {
            throw CastorPersistenceException.newInstanceFailed(entityClazz, e);
        }
    }

    public void persist(Object object) {
        EntityBase entity = validateEntity(object);
        ModifiableSource source = resolveJcrSource(entity);
        if (source.exists()) {
            throw new PersistenceException("an entity with id: " + 
                    entity.getId() + " already exists.");
        }
        createdEntities_.add(entity);      
    }

    public void update(Object object) {
        EntityBase entity = validateEntity(object);
        updatedEntities_.add(entity);
    }
    
    public boolean delete(Object object) {
        EntityBase entity = validateEntity(object);            
        deletedEntities_.add(entity);
        return true;
    }
    
    public void commit() {
        
        Iterator<EntityBase> createdEntitiesIt = createdEntities_.iterator();
        while (createdEntitiesIt.hasNext()) {
            EntityBase entity = createdEntitiesIt.next();
            ModifiableSource source = resolveJcrSource(entity);
            writeToSource(entity, source);
            createdEntitiesIt.remove();
        }
        
        Iterator<EntityBase> updatedEntitiesIt = updatedEntities_.iterator();
        while (updatedEntitiesIt.hasNext()) {
            EntityBase entity = updatedEntitiesIt.next();
            ModifiableSource source = resolveJcrSource(entity);
            if (! source.exists()) {
                getLogger().error("could not update entity " + entity +
                        "with id: " + entity.getId() +  ". It does yet exist.");
            }
            writeToSource(entity, source);
            updatedEntitiesIt.remove();
        }
        
        Iterator<EntityBase> deletedEntitiesIt = deletedEntities_.iterator();
        while (deletedEntitiesIt.hasNext()) {
            EntityBase entity = deletedEntitiesIt.next();
            ModifiableSource source = resolveJcrSource(entity);
            try {
                source.delete();
                deletedEntitiesIt.remove();
            } catch (SourceException e) {
                throw new CastorPersistenceException(
                     "could not delete xml content from jcr source", e);
            }
        }
    }

    public List<Object> query(String queryKey, Object[] params) {
        
        List<Object> result = new LinkedList<Object>();
        
        String query = findQuery(queryKey);
        String preparedQuery = prepareQuery(query, params);
        
        TraversableSource source = resolveJcrQuerySource(preparedQuery);
        
        if (source.exists()) {            
            if (source.isCollection()) {
                for (Object child : getChildren(source)) {
                    Source childSource = (Source) child;
                    EntityBase entity = readFromSource(childSource);
                    queriedEntities_.add(entity);
                    result.add(entity);
                }
            } else {
                EntityBase entity = readFromSource(source);
                queriedEntities_.add(entity);
                result.add(entity);
            }            
        }
        return result;
    }
    
    private void writeToSource(EntityBase entity, ModifiableSource source) {
        
        OutputStreamWriter sourceWriter = null;
        try {            
            sourceWriter = new OutputStreamWriter(source.getOutputStream());
            
            Marshaller marshaller = new Marshaller(sourceWriter);
            marshaller.setMapping(mapping_);

            marshaller.marshal(entity);
            
            sourceWriter.flush();
        } catch (IOException e) {
            throw new CastorPersistenceException(
                    "could not write xml content to jcr source", e);
        } catch (MarshalException e) {
            throw new CastorPersistenceException(
                    "could not write xml content to jcr source", e);
        } catch (ValidationException e) {
            throw new CastorPersistenceException(
                    "could not write xml content to jcr source", e);
        } catch (MappingException e) {
            throw new CastorPersistenceException(
                    "could not write xml content to jcr source. " +
                    "the castor mapping seems to be invalid", e);
        } finally {
            try {
                if (null != sourceWriter)
                    sourceWriter.close();
            } catch (IOException e) {
                getLogger().info(
                        "could not close stream of source " + source, e);
            }
        }
    }
    
    private EntityBase readFromSource(Source source) {
        
        InputStream sourceIn = loadSourceContent(source);
        InputStreamReader sourceReader = new InputStreamReader(sourceIn);
        
        try {            
            Unmarshaller unmarshaller = new Unmarshaller(mapping_);            
            return (EntityBase) unmarshaller.unmarshal(sourceReader);
        } catch (MarshalException e) {
            throw new CastorPersistenceException(
                    "could not write xml content to jcr source", e);
        } catch (ValidationException e) {
            throw new CastorPersistenceException(
                    "could not write xml content to jcr source", e);
        } catch (MappingException e) {
            throw new CastorPersistenceException(
                    "could not write xml content to jcr source", e);
        } finally {
            try {
                sourceReader.close();
            } catch (IOException e) {
                getLogger().info(
                        "could not close stream of source " + source, e);
            }
        }
    }
    
    private InputStream loadSourceContent(Source source) {
        try {
            return source.getInputStream();
        } catch (SourceNotFoundException e) {
            throw new CastorPersistenceException(
                    "could load content from source: " + source.getURI(), e);
        } catch (IOException e) {
            throw new CastorPersistenceException(
                    "could load content from source: " + source.getURI(), e);
        }
    }
    
    private EntityBase validateEntity(Object object) {
        Class clazz = object.getClass();
        validateEntityClazz(clazz);
        return (EntityBase) object;
    }
    
    private Class<? extends EntityBase> validateEntityClazz(Class<?> clazz) {
        assert isConfiguredEntityClazz(clazz) : clazz + " is not " +
                "configured as entity class within mindquarry-persistence.xml";
        return clazz.asSubclass(EntityBase.class);
    }
    
    private boolean isConfiguredEntityClazz(Class clazz) {
        if (configuration_.existsEntityClazz(clazz))
            return true;
        else 
            return false;
    }
    
    private String findQuery(String queryKey) {
        String result = configuration_.query(queryKey);
        if (null == result)
            throw new CastorPersistenceException(
                    "a query with key: " + queryKey + " is not defined " + 
                    "within the persistence configuration file");
        return result;
    }
    
    private Collection getChildren(TraversableSource parentSource) {
        try {
            return parentSource.getChildren();
        } catch (SourceException e) {
            throw new CastorPersistenceException(
                    "could not get children from source: " + 
                    parentSource.getURI(), e);
        }
    }

    private String prepareQuery(String query, Object[] params) {
        return new QueryPreparer(query, params).prepare();
    }
    
    private TraversableSource resolveJcrQuerySource(String query) {
        Source source = jcrSourceResolver_.resolveJcrSource(query);
        return (TraversableSource) source;
    }
    
    private ModifiableTraversableSource resolveJcrSource(EntityBase entity) {
        String entityPath = buildEntityPath(entity);
        Source source = jcrSourceResolver_.resolveJcrSource(entityPath);
        return (ModifiableTraversableSource) source;
    }
    
    private String buildEntityPath(EntityBase entity) {
        Class entityClazz = entity.getClass();
        String entityPathTemplate = configuration_.entityPath(entityClazz);
        return entityPathTemplate.replace("{$id}", entity.getId());
    }
}
