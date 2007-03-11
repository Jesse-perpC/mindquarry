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
package com.mindquarry.persistence.jcr.mapping.trafo;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import com.mindquarry.persistence.jcr.JcrPersistenceInternalException;
import com.mindquarry.persistence.jcr.mapping.model.EntityClass;
import com.mindquarry.persistence.jcr.mapping.model.Property;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class EntityTransformer implements Transformer {
    
    private EntityClass entityClazz_;
    
    public Object fromJcr(Node entityNode) {
        /*
         * String name = entityNode.parent().name();
         * entityClazz = model_.entityClass(name);
         * entity = entityClazz.createNewEntity();
         * 
         * NodeIterator nodeIt = entityNode.getNodes();
         * while(nodeIt.hasNext()) {
         *   childNode = nodeIt.next();
         *   transformer = entityClazz.getPropertyTransformer(childNode.name());
         *   propertyValue = transformer.fromJcr(childNode);
         *   entity.setProperty(name, propertyValue); 
         * }
         * 
         */
        return null;
    }

    public void toJcr(Object entity, Node parentNode) {
        try {
            toJcrInternal(entity, parentNode);
        } catch (ItemExistsException e) {
            throw new JcrPersistenceInternalException(e);
        } catch (PathNotFoundException e) {
            throw new JcrPersistenceInternalException(e);
        } catch (VersionException e) {
            throw new JcrPersistenceInternalException(e);
        } catch (ConstraintViolationException e) {
            throw new JcrPersistenceInternalException(e);
        } catch (LockException e) {
            throw new JcrPersistenceInternalException(e);
        } catch (RepositoryException e) {
            throw new JcrPersistenceInternalException(e);
        }
    }
    
    private void toJcrInternal(Object entity, Node parentNode) 
        throws ItemExistsException, PathNotFoundException, VersionException, 
        ConstraintViolationException, LockException, RepositoryException {
        
        Node entityNode = parentNode.addNode(id(entity));
        for (Property property : entityClazz_.getNonIdProperties()) {
            Node propertyNode = entityNode.addNode(property.getName());
            Object propertyValue = property.getValue(entity);
            property.getContentTransformer().toJcr(propertyValue, propertyNode);
        }
    }
    
    private String id(Object entity) {
        return entityClazz_.getIdProperty().getValue(entity);
    }
}
