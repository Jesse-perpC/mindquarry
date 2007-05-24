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
package com.mindquarry.persistence.jcr;

import static com.mindquarry.common.lang.ReflectionUtil.invoke;

import javax.jcr.Node;
import javax.jcr.Property;


/**
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public class JcrProperty {

    private Property property_;
    private JcrSession session_;
    
    public JcrProperty(Property property, JcrSession session) {
        property_ = property;
        session_ = session;
    }

    /*
    public boolean getBoolean() throws ValueFormatException, RepositoryException {
        // TODO Auto-generated method stub
        return false;
    }

    public Calendar getDate() throws ValueFormatException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    public PropertyDefinition getDefinition() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    public double getDouble() throws ValueFormatException, RepositoryException {
        // TODO Auto-generated method stub
        return 0;
    }

    public long getLength() throws ValueFormatException, RepositoryException {
        // TODO Auto-generated method stub
        return 0;
    }

    public long[] getLengths() throws ValueFormatException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    public long getLong() throws ValueFormatException, RepositoryException {
        // TODO Auto-generated method stub
        return 0;
    }
    */
    
    public JcrNode getNode() {
        Object result = invoke("getNode", property_);
        return new JcrNode((Node) result, session_);
    }
    
    /*
    public InputStream getStream() throws ValueFormatException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }
*/
    
    public String getString() {
        Object result = invoke("getString", property_);
        return (String) result;
    }

/*
    public int getType() throws RepositoryException {
        // TODO Auto-generated method stub
        return 0;
    }

    public Value getValue() throws ValueFormatException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    public Value[] getValues() throws ValueFormatException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    public void setValue(Value value) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        // TODO Auto-generated method stub
        
    }

    public void setValue(Value[] values) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        // TODO Auto-generated method stub
        
    }

    public void setValue(String value) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        // TODO Auto-generated method stub
        
    }

    public void setValue(String[] values) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        // TODO Auto-generated method stub
        
    }

    public void setValue(InputStream value) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        // TODO Auto-generated method stub
        
    }

    public void setValue(long value) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        // TODO Auto-generated method stub
        
    }

    public void setValue(double value) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        // TODO Auto-generated method stub
        
    }

    public void setValue(Calendar value) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        // TODO Auto-generated method stub
        
    }

    public void setValue(boolean value) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        // TODO Auto-generated method stub
        
    }

    public void setValue(Node value) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        // TODO Auto-generated method stub
        
    }

    public void accept(ItemVisitor visitor) throws RepositoryException {
        // TODO Auto-generated method stub
        
    }

    public Item getAncestor(int depth) throws ItemNotFoundException, AccessDeniedException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    public int getDepth() throws RepositoryException {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getName() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }*/

    public JcrNode getParent() {
        Object result = invoke("getParent", property_);
        return new JcrNode((Node) result, session_);
    }

    /*
    public String getPath() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    public Session getSession() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isModified() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isNew() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isNode() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isSame(Item otherItem) throws RepositoryException {
        // TODO Auto-generated method stub
        return false;
    }

    public void refresh(boolean keepChanges) throws InvalidItemStateException, RepositoryException {
        // TODO Auto-generated method stub
        
    }

    public void remove() throws VersionException, LockException, ConstraintViolationException, RepositoryException {
        // TODO Auto-generated method stub
        
    }

    public void save() throws AccessDeniedException, ItemExistsException, ConstraintViolationException, InvalidItemStateException, ReferentialIntegrityException, VersionException, LockException, NoSuchNodeTypeException, RepositoryException {
        // TODO Auto-generated method stub
        
    }
    */
}
