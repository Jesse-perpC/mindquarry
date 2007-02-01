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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;

import javax.jcr.PropertyType;

import org.apache.jackrabbit.core.NodeId;
import org.apache.jackrabbit.core.PropertyId;
import org.apache.jackrabbit.core.persistence.AbstractPersistenceManager;
import org.apache.jackrabbit.core.persistence.PMContext;
import org.apache.jackrabbit.core.state.ItemStateException;
import org.apache.jackrabbit.core.state.NoSuchItemStateException;
import org.apache.jackrabbit.core.state.NodeReferences;
import org.apache.jackrabbit.core.state.NodeReferencesId;
import org.apache.jackrabbit.core.state.NodeState;
import org.apache.jackrabbit.core.state.PropertyState;
import org.apache.jackrabbit.core.value.InternalValue;
import org.apache.jackrabbit.name.QName;

/**
 * @author Hagen Overdick
 * 
 */
public class PersistenceManager extends AbstractPersistenceManager {
    private Connection database = null;

    private abstract class CallDB {
        protected boolean execute(String sql) throws ItemStateException {
            CallableStatement statement = null;
            try {
                statement = database.prepareCall(sql);
                initStatement(statement);
                return statement.execute();
            } catch (SQLException sqle) {
                throw new ItemStateException(sqle.getLocalizedMessage(), sqle);
            } finally {
                try {
                    statement.close();
                } catch (Exception ignore) {
                    ignore.printStackTrace();
                }
            }
        }

        public abstract void initStatement(CallableStatement statement)
                throws SQLException;
    }

    private interface QueryDB {

    }

    @Override
    protected void destroy(NodeState state) throws ItemStateException {
        System.err.println("destroy:" + state.getId());

        // TODO Auto-generated method stub

    }

    @Override
    protected void destroy(PropertyState state) throws ItemStateException {
        System.err.println("destroy:" + state.getId());
        // TODO Auto-generated method stub

    }

    @Override
    protected void destroy(NodeReferences refs) throws ItemStateException {
        System.err.println("destroy:" + refs.getId());
        // TODO Auto-generated method stub

    }

    protected void store(final NodeState state) throws ItemStateException {
        System.err.println("store!" + state.getId());

        new CallDB() {
            public void initStatement(CallableStatement statement)
                    throws SQLException {
                statement.setString(1, state.getNodeId().toString());
                statement.setString(2, state.getParentId() != null ? state
                        .getParentId().toString() : state.getNodeId()
                        .toString());
                statement.setString(3, state.getNodeTypeName()
                        .getNamespaceURI());
                statement.setString(4, state.getNodeTypeName().getLocalName());
                statement.setInt(5, Integer.parseInt(state.getDefinitionId()
                        .toString()));

                StringBuffer mixinTypes = new StringBuffer();
                for (Object name : state.getMixinTypeNames()) {
                    mixinTypes.append(name.toString());
                    mixinTypes.append(' ');
                }
                statement.setString(6, mixinTypes.toString());
            }
        }
                .execute("select jcr.ensure_node(jcr.uuid(?), jcr.uuid(?), jcr.name(?, ?), ?, ?);");

        Iterator iter = state.getRemovedChildNodeEntries().iterator();
        while (iter.hasNext()) {
            final NodeState.ChildNodeEntry entry = (NodeState.ChildNodeEntry) iter
                    .next();
            new CallDB() {
                public void initStatement(CallableStatement statement)
                        throws SQLException {
                    statement.setString(1, entry.getId().toString());
                }
            }.execute("delete from jcr.node_head where id = jcr.uu_id(?);");
        }

        iter = state.getAddedChildNodeEntries().iterator();
        while (iter.hasNext()) {
            final NodeState.ChildNodeEntry entry = (NodeState.ChildNodeEntry) iter
                    .next();
            new CallDB() {
                public void initStatement(CallableStatement statement)
                        throws SQLException {
                    statement.setString(1, entry.getId().toString());
                    statement.setString(2, state.getId().toString());
                    statement.setString(3, entry.getName().getNamespaceURI());
                    statement.setString(4, entry.getName().getLocalName());
                }
            }.execute("insert into jcr.node_head(id, parent_id, name_id)"
                    + "values(jcr.uu_id(?), jcr.uu_id(?), jcr.name_id(?, ?));");
        }

        iter = new Iterator() {
            final Iterator reordered = state.getReorderedChildNodeEntries()
                    .iterator();

            final Iterator added = state.getAddedChildNodeEntries().iterator();

            public void remove() {
            }

            public Object next() {
                return reordered.hasNext() ? reordered.next() : added.next();
            }

            public boolean hasNext() {
                return reordered.hasNext() || added.hasNext();
            }

        };

        while (iter.hasNext()) {
            final NodeState.ChildNodeEntry entry = (NodeState.ChildNodeEntry) iter
                    .next();
            new CallDB() {
                public void initStatement(CallableStatement statement)
                        throws SQLException {
                    statement.setInt(1, entry.getIndex());
                    statement.setString(2, entry.getId().toString());
                }
            }
                    .execute("update jcr.node_head set position = ? where id = jcr.uu_id(?)");
        }

        iter = state.getRemovedPropertyNames().iterator();
        while (iter.hasNext()) {
            final QName propName = (QName) iter.next();
            new CallDB() {
                public void initStatement(CallableStatement statement)
                        throws SQLException {
                    statement.setString(1, state.getId().toString());
                    statement.setString(2, propName.getNamespaceURI());
                    statement.setString(3, propName.getLocalName());
                }
            }.execute("delete from jcr.property_head"
                    + "where     parent_id = jcr.uu_id(?)"
                    + "      and name_id   = jcr.name_id(?, ?)");
        }
    }

    protected void store(final PropertyState state) throws ItemStateException {
        new CallDB() {
            public void initStatement(CallableStatement statement)
                    throws SQLException {
                statement.setString(1, state.getParentId().toString());
                statement.setString(2, state.getName().getNamespaceURI());
                statement.setString(3, state.getName().getLocalName());
                statement.setBoolean(4, state.isMultiValued());
                statement.setInt(5, Integer.parseInt(state.getDefinitionId()
                        .toString()));
            }
        }
                .execute("select jcr.ensure_property(jcr.uuid(?), jcr.name(?, ?), ?, ?);");
        InternalValue[] values = state.getValues();
        if (values != null) {
            System.err.println(values.length);
            for (int ii = 0; ii < values.length; ii++) {
                final InternalValue val = values[ii];
                final int pos = ii;
                System.err.println("Prop " + ii + ": " + val.toString());
                if (val != null) {
                    new CallDB() {
                        public void initStatement(CallableStatement statement)
                                throws SQLException {
                            statement.setString(1, state.getParentId()
                                    .toString());
                            statement.setString(2, state.getName()
                                    .getNamespaceURI());
                            statement.setString(3, state.getName()
                                    .getLocalName());
                            statement.setInt(4, pos);
                            switch (val.getType()) {
                            case PropertyType.STRING:
                                statement.setString(5, val.toString());
                                break;
                            // case PropertyType.BINARY:
                            // (BLOBFileValue) val.internalValue();
                            // statement.setBlob(3, x)
                            // break;
                            case PropertyType.BOOLEAN:
                                statement.setBoolean(5, ((Boolean) val
                                        .internalValue()).booleanValue());
                                break;
                            case PropertyType.DATE:
                                statement
                                        .setDate(5, (Date) val.internalValue());
                                break;
                            case PropertyType.DOUBLE:
                                statement.setDouble(5, ((Double) val
                                        .internalValue()).doubleValue());
                                break;
                            case PropertyType.LONG:
                                statement.setLong(5, ((Long) val
                                        .internalValue()));
                                break;
                            case PropertyType.NAME:
                                QName name = (QName) val.internalValue();
                                statement.setString(5, name.getNamespaceURI());
                                statement.setString(6, name.getLocalName());
                                break;
                            default:
                                throw new IllegalStateException(
                                        "Must implement "
                                                + PropertyType
                                                        .nameFromValue(val
                                                                .getType()));
                            }
                        }
                    }
                            .execute("select jcr.ensure_property_value(jcr.uuid(?), jcr.name(?,?), ?,"
                                    + (val.getType() == PropertyType.NAME ? "jcr.value_name(?,?));"
                                            : "jcr.value_"
                                                    + PropertyType
                                                            .nameFromValue(val
                                                                    .getType())
                                                    + "(?));"));
                }
            }
        }
    }

    @Override
    protected void store(NodeReferences refs) throws ItemStateException {
        System.err.println("store:" + refs.toString());
        // TODO Auto-generated method stub

    }

    public void close() throws Exception {
        // TODO Auto-generated method stub

    }

    public boolean exists(NodeId id) throws ItemStateException {
        System.err.println("exists?" + id.toString());
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = database.prepareStatement("select jcr.node_exists(?)");
            statement.setString(1, id.toString());
            rs = statement.executeQuery();
            if (rs.next()) {
                boolean result = rs.getBoolean(1);
                System.err.println("Result is " + result);
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
        System.err.println("exists:" + id.toString());
        // TODO Auto-generated method stub
        return false;
    }

    public boolean exists(NodeReferencesId targetId) throws ItemStateException {
        System.err.println("exists" + targetId.toString());
        // TODO Auto-generated method stub
        return false;
    }

    public void init(PMContext context) throws Exception {
        // TODO hard coded for now
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql:jackrabbit";
        Properties props = new Properties();
        props.setProperty("user", "xenodot");
        database = DriverManager.getConnection(url, props);
    }

    public NodeState load(NodeId id) throws NoSuchItemStateException,
            ItemStateException {
        System.err.println("load" + id.toString());
        // TODO Auto-generated method stub
        return null;
    }

    public PropertyState load(PropertyId id) throws NoSuchItemStateException,
            ItemStateException {
        System.err.println("load" + id.toString());
        // TODO Auto-generated method stub
        return null;
    }

    public NodeReferences load(NodeReferencesId id)
            throws NoSuchItemStateException, ItemStateException {
        System.err.println("load" + id.toString());
        // TODO Auto-generated method stub
        return null;
    }
}
