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
package com.mindquarry.dms.xenodot.jackrabbit;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;

import javax.jcr.PropertyType;

import org.apache.jackrabbit.core.NodeId;
import org.apache.jackrabbit.core.PropertyId;
import org.apache.jackrabbit.core.fs.FileSystemResource;
import org.apache.jackrabbit.core.persistence.AbstractPersistenceManager;
import org.apache.jackrabbit.core.persistence.PMContext;
import org.apache.jackrabbit.core.persistence.util.ResourceBasedBLOBStore;
import org.apache.jackrabbit.core.state.ItemStateException;
import org.apache.jackrabbit.core.state.NoSuchItemStateException;
import org.apache.jackrabbit.core.state.NodeReferences;
import org.apache.jackrabbit.core.state.NodeReferencesId;
import org.apache.jackrabbit.core.state.NodeState;
import org.apache.jackrabbit.core.state.PropertyState;
import org.apache.jackrabbit.core.value.BLOBFileValue;
import org.apache.jackrabbit.core.value.InternalValue;
import org.apache.jackrabbit.name.QName;
import org.apache.jackrabbit.util.Text;

/**
 * @author Hagen Overdick
 *
 */
public class PersistenceManager extends AbstractPersistenceManager {
	private Connection database = null;


	@Override
	protected void destroy(NodeState state) throws ItemStateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void destroy(PropertyState state) throws ItemStateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void destroy(NodeReferences refs) throws ItemStateException {
		// TODO Auto-generated method stub
		
	}

	protected void store(NodeState state) throws ItemStateException {
		CallableStatement statement = null;
		ResultSet rs = null;
		
		try {
			statement = database.prepareCall(
				"select jcr.ensure_node(jcr.uuid(?), jcr.uuid(?), jcr.name(?, ?), ?, ?, ?);");
			statement.setString(1, state.getNodeId().toString());
			statement.setString(2, state.getParentId().toString());
			statement.setString(3, state.getNodeTypeName().getNamespaceURI());
			statement.setString(4, state.getNodeTypeName().getLocalName());
			statement.setInt   (5, Integer.parseInt(state.getDefinitionId().toString()));
			
			StringBuffer mixinTypes = new StringBuffer();
			for (Object name : state.getMixinTypeNames()) {
				mixinTypes.append(name.toString());
				mixinTypes.append(' ');
			}
			statement.setString(6, mixinTypes.toString());
			statement.setInt   (7, state.getModCount());
			statement.execute();
		} catch (SQLException sqle) {
			throw new ItemStateException(sqle.getLocalizedMessage(), sqle);
		} finally {
			try {
				statement.close();
			} catch (Exception ignore) {
				ignore.printStackTrace();
			}
		}

		/*
		iter = state.getPropertyNames().iterator();
        while (iter.hasNext()) {
            QName propName = (QName) iter.next();
        }
        
        iter = state.getChildNodeEntries().iterator();
        while (iter.hasNext()) {
            NodeState.ChildNodeEntry entry = (NodeState.ChildNodeEntry) iter.next();
            entry.getName();
            entry.getId().toString();
        }
        */
		
	}

	@Override
	protected void store(PropertyState state) throws ItemStateException {
        String typeName;
        int type = state.getType();
        try {
            typeName = PropertyType.nameFromValue(type);
        } catch (IllegalArgumentException iae) {
            // should never be getting here
            throw new ItemStateException("unexpected property-type ordinal: " + type, iae);
        }
        state.getName();
        state.getParentId().getUUID();
        state.isMultiValued();
        state.getDefinitionId();
        state.getModCount();
        
        InternalValue[] values = state.getValues();
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                InternalValue val = values[i];
                if (val != null) {
                    if (type == PropertyType.BINARY) {
                        BLOBFileValue blobVal = (BLOBFileValue) val.internalValue();
                    } else {
                        val.toString();
                    }
                }
            }
        }
		
	}

	@Override
	protected void store(NodeReferences refs) throws ItemStateException {
		// TODO Auto-generated method stub
		
	}

	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public boolean exists(NodeId id) throws ItemStateException {
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = database.prepareStatement("select jcr.node_exists(?)");
			statement.setString(1, id.toString());
			rs = statement.executeQuery();
			if (rs.next()) {
				boolean result = rs.getBoolean(1);
				return result;
			}
			throw new ItemStateException("This can never happen :-)");
		} catch (SQLException sqle) {
			throw new ItemStateException("Error accessing DB", sqle);
		} finally {
			try {
				rs.close();
				statement.close();
			} catch (Exception ignore) {
				ignore.printStackTrace();
			}
		}
	}

	public boolean exists(PropertyId id) throws ItemStateException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean exists(NodeReferencesId targetId) throws ItemStateException {
		// TODO Auto-generated method stub
		return false;
	}

	public void init(PMContext context) throws Exception {
		// TODO hard coded for now
		Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql:jackrabbit";
		Properties props = new Properties();
		props.setProperty("user","xenodot");
		database = DriverManager.getConnection(url, props);
	}

	public NodeState load(NodeId id) throws NoSuchItemStateException, ItemStateException {
		// TODO Auto-generated method stub
		return null;
	}

	public PropertyState load(PropertyId id) throws NoSuchItemStateException, ItemStateException {
		// TODO Auto-generated method stub
		return null;
	}

	public NodeReferences load(NodeReferencesId id) throws NoSuchItemStateException, ItemStateException {
		// TODO Auto-generated method stub
		return null;
	}
}
