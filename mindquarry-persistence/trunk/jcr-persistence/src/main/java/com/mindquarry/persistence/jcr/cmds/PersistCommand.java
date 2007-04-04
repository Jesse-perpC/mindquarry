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
package com.mindquarry.persistence.jcr.cmds;

import com.mindquarry.persistence.api.PersistenceException;
import com.mindquarry.persistence.jcr.JcrNode;
import com.mindquarry.persistence.jcr.JcrSession;
import com.mindquarry.persistence.jcr.Persistence;
import com.mindquarry.persistence.jcr.Pool;
import com.mindquarry.persistence.jcr.model.EntityType;
import com.mindquarry.persistence.jcr.model.Model;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class PersistCommand implements Command {

    private Object entity_;
    private Persistence persistence_;
    private Command writeCommand_;
    
    PersistCommand() {
        writeCommand_ = new WriteCommand(); 
    }
    
    public void initialize(Persistence persistence, Object... objects) {
        entity_ = objects[0];
        persistence_ = persistence;
        writeCommand_.initialize(persistence, objects);
    }
    
    /**
     * @see com.mindquarry.persistence.jcr.mapping.Command#execute(javax.jcr.Session)
     */
    public Object execute(JcrSession session) {
        
        Pool pool = session.getPool();
        if (! pool.containsEntryForEntity(entity_)) {
            JcrEntityFolderNode folderNode = findOrCreateEntityFolder(session);            
            
            if (folderNode.hasEntityNode(entityId())) {
                throw new PersistenceException("the entity: " + entity_ + 
                        " with id: " + entityId() + "already exists.");
            }            
            // here we only create the jcr file node
            folderNode.addEntityNode(entityId());         
        }
        
        return writeEntityIntoFileNode(session);        
    }
    
    protected Object writeEntityIntoFileNode(JcrSession session) { 
        return writeCommand_.execute(session);
    }
    
    protected JcrEntityFolderNode findOrCreateEntityFolder(JcrSession session) {
        JcrNode rootNode = session.getRootNode();
        String name = parentFolderName();
        
        JcrNode folderNode;
        if (rootNode.hasNode(name))
            folderNode = rootNode.getNode(name);
        else
            folderNode = rootNode.addNode(name, "nt:folder");

        return new JcrEntityFolderNode(folderNode, entityType());
    }
    
    private String parentFolderName() {
        return entityType().parentFolder();
    }
    
    protected String entityId() {
        return entityType().getId(entity_);
    }
    
    private EntityType entityType() {
        return getModel().findEntityType(entity_);
    }
    
    private Model getModel() {
        return persistence_.getModel();
    }
}