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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Properties;

import javax.jcr.PropertyType;

import org.apache.jackrabbit.core.NodeId;
import org.apache.jackrabbit.core.PropertyId;
import org.apache.jackrabbit.core.nodetype.NodeDefId;
import org.apache.jackrabbit.core.nodetype.PropDefId;
import org.apache.jackrabbit.core.persistence.AbstractPersistenceManager;
import org.apache.jackrabbit.core.persistence.PMContext;
import org.apache.jackrabbit.core.state.ItemStateException;
import org.apache.jackrabbit.core.state.NoSuchItemStateException;
import org.apache.jackrabbit.core.state.NodeReferences;
import org.apache.jackrabbit.core.state.NodeReferencesId;
import org.apache.jackrabbit.core.state.NodeState;
import org.apache.jackrabbit.core.state.PropertyState;
import org.apache.jackrabbit.core.state.NodeState.ChildNodeEntry;
import org.apache.jackrabbit.core.value.BLOBFileValue;
import org.apache.jackrabbit.core.value.InternalValue;
import org.apache.jackrabbit.name.QName;

/**
 * @author Hagen Overdick
 * 
 */
public class PersistenceManager extends AbstractPersistenceManager {
    private Connection database = null;

    public PersistenceManager() {
        System.err.println("Started Xenodot " + this.toString());
    }

    private abstract class CallDB {
        protected boolean execute(String sql) throws ItemStateException {
            CallableStatement statement = null;
            try {
                statement = database.prepareCall(sql);
                initStatement(statement);
                System.err.println("CALLDB: " + statement);
                System.err.flush();
                return statement.execute();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
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

    private abstract class QueryDB {
        boolean execValue;

        protected boolean execute(String sql) throws ItemStateException {
            PreparedStatement statement = null;
            ResultSet rs = null;
            try {
                statement = database.prepareCall(sql);
                initStatement(statement);
                System.err.println("QUERYDB: " + statement);
                System.err.flush();
                execValue = statement.execute();
                rs = statement.getResultSet();
                read(rs);
            } catch (SQLException sqle) {
                throw new ItemStateException(sqle.getLocalizedMessage(), sqle);
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    statement.close();
                } catch (Exception ignore) {
                    ignore.printStackTrace();
                }
            }
            return execValue;
        }

        public abstract void initStatement(PreparedStatement statement)
                throws SQLException;

        public abstract void read(ResultSet result) throws SQLException;

    }

    protected synchronized void destroy(final NodeState state)
            throws ItemStateException {
        System.err.println("Destroy Node " + state.getId());
        System.err.flush();
        new CallDB() {
            public void initStatement(CallableStatement statement)
                    throws SQLException {
                statement.setString(1, state.getId().toString());
            }

        }.execute("select jcr.delete_node(jcr.uuid(?));");
    }

    protected synchronized void destroy(final PropertyState state)
            throws ItemStateException {
        System.err.println("Destroy Property " + state.getId());
        System.err.flush();

        new CallDB() {
            public void initStatement(CallableStatement statement)
                    throws SQLException {
                statement.setString(1, state.getParentId().toString());
                statement.setString(2, state.getName().getNamespaceURI());
                statement.setString(3, state.getName().getLocalName());
            }

        }.execute("select jcr.delete_property(jcr.uuid(?), jcr.name(?, ?));");
    }

    protected synchronized void destroy(NodeReferences refs)
            throws ItemStateException {
        System.err.println("Destroy Refs to " + refs.getTargetId());
        System.err.flush();

        // do nothing!
    }

    protected synchronized void store(final NodeState state)
            throws ItemStateException {
        System.err.println("Storing Node " + state.getNodeId() + ", parent is "
                + state.getParentId());
        System.err.flush();

        Iterator temp = state.getChildNodeEntries().iterator();
        while (temp.hasNext()) {
            NodeState.ChildNodeEntry entry = (ChildNodeEntry) temp.next();
            System.err.println("Node " + state.getNodeId() + ", has Child "
                    + entry.getName() + " at pos " + entry.getIndex() + ", id "
                    + entry.getId());
        }
        System.err.println("---");
        System.err.flush();

        new CallDB() {
            public void initStatement(CallableStatement statement)
                    throws SQLException {
                statement.setString(1, state.getNodeId().toString());
                statement.setString(2, state.getParentId().toString());
                statement.setInt(3, Integer.parseInt(state.getDefinitionId()
                        .toString()));
                statement.setString(4, state.getNodeTypeName()
                        .getNamespaceURI());
                statement.setString(5, state.getNodeTypeName().getLocalName());
                statement.setArray(6, new SQLStringArray(state
                        .getMixinTypeNames()));
                statement.setInt(7, state.getModCount());
            }
        }
                .execute("select jcr.ensure_node(jcr.uuid(?), jcr.uuid(?), jcr.ensure_node_def(?, jcr.name(?, ?)), ?, ?);");

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
            }
                    .execute("select jcr.ensure_node_child(jcr.uuid(?), jcr.uuid(?), jcr.name(?, ?));");
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
                    statement.setString(1, entry.getId().toString());
                    statement.setInt(2, entry.getIndex());
                }
            }.execute("select jcr.reorder_node(jcr.uuid(?), ?);");
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

    protected synchronized void store(final PropertyState state)
            throws ItemStateException {
        System.err.println("Storing property " + state.getId());
        System.err.flush();
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
            for (int ii = 0; ii < values.length; ii++) {
                final InternalValue val = values[ii];
                final int pos = ii;
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
                            case PropertyType.BINARY:
                                BLOBFileValue blob = (BLOBFileValue) val
                                        .internalValue();
                                try {
                                    statement.setBinaryStream(5, blob
                                            .getStream(), (int) blob
                                            .getLength());
                                } catch (Exception ee) {
                                    throw new SQLException(ee
                                            .getLocalizedMessage());
                                }
                                break;
                            case PropertyType.BOOLEAN:
                                statement.setBoolean(5, ((Boolean) val
                                        .internalValue()).booleanValue());
                                break;
                            case PropertyType.DATE:
                                Calendar cal = (Calendar) val.internalValue();
                                Date date = new Date(cal.getTimeInMillis());
                                statement.setDate(5, date, cal);
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
                            case PropertyType.REFERENCE:
                                statement.setString(5, val.internalValue().toString());
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

    protected synchronized void store(NodeReferences refs)
            throws ItemStateException {
        System.err.println("Store Refs " + refs.getTargetId());
        System.err.flush();

        // do nothing!
    }

    public synchronized void close() throws Exception {
        database.close();
    }

    public synchronized boolean exists(final NodeId id)
            throws ItemStateException {
        boolean result = new QueryDB() {
            public void read(ResultSet result) throws SQLException {
                if (result.next()) {
                    execValue = result.getBoolean(1);
                }
            }

            public void initStatement(PreparedStatement statement)
                    throws SQLException {
                statement.setString(1, id.toString());
            }

        }.execute("select jcr.node_exists(jcr.uuid(?));");

        System.err.println("Exists Node " + id + " = " + result);
        System.err.flush();
        return result;
    }

    public synchronized boolean exists(final PropertyId id)
            throws ItemStateException {
        boolean result = new QueryDB() {
            public void read(ResultSet result) throws SQLException {
                if (result.next()) {
                    execValue = result.getBoolean(1);
                }
            }

            public void initStatement(PreparedStatement statement)
                    throws SQLException {
                statement.setString(1, id.getParentId().toString());
                statement.setString(2, id.getName().getNamespaceURI());
                statement.setString(3, id.getName().getLocalName());
            }

        }.execute("select jcr.property_exists(jcr.uuid(?), jcr.name(?, ?));");
        System.err.println("Exists Property " + id + " = " + result);
        System.err.flush();
        return result;
    }

    public synchronized boolean exists(final NodeReferencesId targetId)
            throws ItemStateException {
        boolean result = new QueryDB() {

            public void read(ResultSet result) throws SQLException {
                if (result.next()) {
                    execValue = result.getBoolean(1);
                }
            }

            public void initStatement(PreparedStatement statement)
                    throws SQLException {
                statement.setString(1, targetId.getTargetId().toString());
            }

        }.execute("select jcr.reference_exists(jcr.uuid(?));");

        System.err.println("Exists Reference " + targetId + " = " + result);
        System.err.flush();
        return result;

    }

    public synchronized void init(PMContext context) throws Exception {
        // TODO hard coded for now
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql:jackrabbit";
        Properties props = new Properties();
        props.setProperty("user", "xenodot");
        database = DriverManager.getConnection(url, props);
        // database.setAutoCommit(false);
    }

    public synchronized NodeState load(final NodeId id)
            throws NoSuchItemStateException, ItemStateException {
        System.err.println("Load node " + id);
        System.err.flush();
        final NodeState state = createNew(id);
        new QueryDB() {
            public void read(ResultSet result) throws SQLException {
                if (!result.next()) {
                    throw new SQLException("Node not found!");
                }
                System.err.println("Reading node!");
                state.setNodeTypeName(QName.valueOf(result.getString("type")));
                String parentUUID = result.getString("parent_uuid");
                if (parentUUID != null) {
                    state.setParentId(NodeId.valueOf(parentUUID));
                }
                state.setDefinitionId(NodeDefId.valueOf(result
                        .getString("definition_id")));
                state.setModCount(result.getShort("mod_count"));
            }

            public void initStatement(PreparedStatement statement)
                    throws SQLException {
                statement.setString(1, id.toString());
            }
        }.execute("select * from jcr.node(jcr.uuid(?))");

        new QueryDB() {
            public void read(ResultSet result) throws SQLException {
                while (result.next()) {
                    state.addPropertyName(QName.valueOf(result
                            .getString("name")));
                }
            }

            public void initStatement(PreparedStatement statement)
                    throws SQLException {
                statement.setString(1, id.toString());
            }
        }.execute("select name from jcr.property(jcr.uuid(?))");

        new QueryDB() {
            public void read(ResultSet result) throws SQLException {
                while (result.next()) {
                    QName qname = QName.valueOf(result.getString("name"));
                    NodeId nodeId = NodeId.valueOf(result.getString("uuid"));
                    state.addChildNodeEntry(qname, nodeId);
                }
            }

            public void initStatement(PreparedStatement statement)
                    throws SQLException {
                statement.setString(1, id.toString());
            }
        }
                .execute("select name, uuid from jcr.node where parent_uuid = ? order by position;");

        return state;
    }

    public synchronized PropertyState load(PropertyId id)
            throws NoSuchItemStateException, ItemStateException {
        System.err.println("Load Prop " + id);
        System.err.flush();

        final PropertyState state = createNew(id);

        new QueryDB() {
            public void read(ResultSet result) throws SQLException {
                if (!result.next()) {
                    throw new SQLException("Property does not exist!");
                }
                state.setMultiValued(result.getBoolean("is_multi_valued"));
                state.setDefinitionId(PropDefId.valueOf(result
                        .getString("definition_id")));
                state.setModCount(result.getShort("mod_count"));
                state.setType(result.getInt("value_type"));

                // TODO: the type should be factored out to a
                // property_definition table

                ArrayList<InternalValue> values = new ArrayList<InternalValue>();
                do {
                    switch (result.getInt("value_type")) {
                    case PropertyType.STRING:
                        values.add(InternalValue.create(result
                                .getString("string")));
                        break;
                    case PropertyType.LONG:
                        values
                                .add(InternalValue.create(result
                                        .getLong("long")));
                        break;
                    case PropertyType.DOUBLE:
                        values.add(InternalValue.create(result
                                .getDouble("double")));
                        break;
                    case PropertyType.DATE:
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(result.getDate("date"));
                        values.add(InternalValue.create(cal));
                        break;
                    case PropertyType.BOOLEAN:
                        values.add(InternalValue.create(result
                                .getBoolean("boolean")));
                        break;
                    case PropertyType.NAME:
                        values.add(InternalValue.create(new QName(result
                                .getString("value_name_uri"), result
                                .getString("value_name_local"))));
                        break;
                    case PropertyType.REFERENCE:
                        values.add(InternalValue
                                .valueOf(result.getString("reference"),
                                        PropertyType.REFERENCE));
                        break;
                    // TODO
                    case PropertyType.BINARY:
                    case PropertyType.PATH:
                    default:
                        throw new SQLException(
                                "This should never happen! Property type: "
                                        + result.getInt("value_type"));
                    }

                } while (result.next());
                state.setValues((InternalValue[]) values
                        .toArray(new InternalValue[values.size()]));
            }

            public void initStatement(PreparedStatement statement)
                    throws SQLException {
                statement.setString(1, state.getParentId().toString());
                statement.setString(2, state.getName().getNamespaceURI());
                statement.setString(3, state.getName().getLocalName());
            }

        }
                .execute("select * from jcr.property(jcr.uuid(?), jcr.name(?, ?)) order by index;");
        return state;
    }

    public synchronized NodeReferences load(final NodeReferencesId id)
            throws NoSuchItemStateException, ItemStateException {

        System.err.println("Load Refs " + id);
        System.err.flush();

        final NodeReferences refs = new NodeReferences(id);
        refs.clearAllReferences();

        new QueryDB() {
            public void read(ResultSet result) throws SQLException {
                while (result.next()) {
                    refs.addReference(PropertyId.valueOf(result.getString(1)));
                }
            }

            public void initStatement(PreparedStatement statement)
                    throws SQLException {
                statement.setString(1, id.getTargetId().toString());
            }

        }
                .execute("select parent_uuid || '/' || name from jcr.property where reference = ?");
        return refs;
    }
}