package com.mindquarry.persistence.jcr;

import static com.mindquarry.common.lang.StringUtil.concat;

import java.util.HashMap;
import java.util.Map;

import com.mindquarry.persistence.jcr.model.Model;

public class Pool {
        
    private Model model_;
    private Map<String, Entry> store_;
    
    Pool(Persistence persistence) {
        model_ = persistence.getModel();
        store_ = new HashMap<String, Entry>();
    }
    
    public JcrNode nodeByEntity(Object entity) {
        String key = keyFromEntity(entity);
        return store_.get(key).entityNode;
    }
    
    public Object entityByNode(JcrNode entityNode) {
        String key = keyFromNode(entityNode);
        return store_.get(key).entity;
    }
    
    public Object put(Object entity, JcrNode jcrNode) {
        String key = keyFromEntity(entity);
        Entry entry = new Entry(entity, jcrNode);
        return store_.put(key, entry);
    }
    
    public boolean containsEntryForEntity(Object entity) {
        return store_.containsKey(keyFromEntity(entity));
    }
    
    public boolean containsEntryForNode(JcrNode node) {
        return store_.containsKey(keyFromNode(node));
    }
    
    private String keyFromEntity(Object entity) {
        String folderName = model_.entityFolderName(entity);        
        return buildKey(folderName, model_.entityId(entity));
    }
    
    private String keyFromNode(JcrNode jcrEntityNode) {
        JcrNode entityFolder = jcrEntityNode.getParent();
        return buildKey(entityFolder.getName(), jcrEntityNode.getName());
    }
    
    private String buildKey(String folderName, String entityId) {
        return concat(folderName, ":", entityId);
    }
    
    private static class Entry {

        Object entity;        
        JcrNode entityNode;
        
        Entry(Object entity, JcrNode entityNode) {
            this.entity = entity;
            this.entityNode = entityNode;
        }
    }
}
