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
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Set;

import javax.jcr.PropertyType;

import org.apache.jackrabbit.core.NodeId;
import org.apache.jackrabbit.core.PropertyId;
import org.apache.jackrabbit.core.nodetype.NodeDefId;
import org.apache.jackrabbit.core.nodetype.PropDefId;
import org.apache.jackrabbit.core.persistence.PMContext;
import org.apache.jackrabbit.core.persistence.PersistenceManager;
import org.apache.jackrabbit.core.state.ChangeLog;
import org.apache.jackrabbit.core.state.ItemState;
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
import org.apache.jackrabbit.uuid.UUID;

/**
 * @author Hagen Overdick
 * 
 */
public class XenodotPersistenceManager implements PersistenceManager {
    private Connection database = null;

    public XenodotPersistenceManager() {
        System.err.println("Started Xenodot " + this.toString());
    }

    public NodeState createNew(NodeId id) {
        return new NodeState(id, null, null, NodeState.STATUS_NEW, false);
    }

    public PropertyState createNew(PropertyId id) {
        return new PropertyState(id, PropertyState.STATUS_NEW, false);
    }
    
    public Connection getConnection() {
        return database;
    }

    protected abstract class CallDB {
        public boolean execute(String sql) throws ItemStateException {
            CallableStatement statement = null;
            try {
                statement = database.prepareCall(sql);
                initStatement(statement);
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

    protected abstract class QueryDB {
        boolean execValue;

        public boolean execute(String sql) throws ItemStateException {
            PreparedStatement statement = null;
            ResultSet rs = null;
            try {
                statement = database.prepareCall(sql);
                initStatement(statement);
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

    public void close() throws Exception {
        database.commit();
        database.close();
    }

    public boolean exists(final NodeId id) throws ItemStateException {
        boolean result = new QueryDB() {
            public void read(ResultSet result) throws SQLException {
                if (result.next()) {
                    execValue = result.getBoolean(1);
                }
            }

            public void initStatement(PreparedStatement statement)
                    throws SQLException {
                statement.setLong(1, id.getUUID().getMostSignificantBits());
                statement.setLong(2, id.getUUID().getLeastSignificantBits());
            }

        }.execute("select jackrabbit.exists(jackrabbit.uuid(?, ?));");
        return result;
    }

    public boolean exists(final PropertyId propertyId) throws ItemStateException {
        boolean result = new QueryDB() {
            public void read(ResultSet result) throws SQLException {
                if (result.next()) {
                    execValue = result.getBoolean(1);
                }
            }

            public void initStatement(PreparedStatement statement)
                    throws SQLException {
                statement.setLong(1, propertyId.getParentId().getUUID().getMostSignificantBits());
                statement.setLong(2, propertyId.getParentId().getUUID().getLeastSignificantBits());
                statement.setString(3, propertyId.getName().getNamespaceURI());
                statement.setString(4, propertyId.getName().getLocalName());
            }

        }.execute("select jackrabbit.exists(jackrabbit.uuid(?, ?), xenodot.name(?, ?));");
        return result;
    }

    public boolean exists(final NodeReferencesId id) throws ItemStateException {
        return new QueryDB() {
            public void read(ResultSet result) throws SQLException {
                execValue = result.next();
            }
        
            public void initStatement(PreparedStatement statement) throws SQLException {
                statement.setLong(1, id.getTargetId().getUUID().getMostSignificantBits());
                statement.setLong(2, id.getTargetId().getUUID().getLeastSignificantBits());
            }
        }.execute("select uuid_msb, uuid_lsb, name_ns, name_local from jackrabbit.property_values where reference_uuid_msb = ? and reference_uuid_lsb = ?;");
    }

    public void init(PMContext context) throws Exception {
        // TODO hard coded for now
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql:xenodot";
        Properties props = new Properties();
        props.setProperty("user", "xenodot");
        database = DriverManager.getConnection(url, props);
        database.setAutoCommit(false);
    }

    public NodeState load(final NodeId nodeId) throws NoSuchItemStateException,
            ItemStateException {
        final NodeState state = createNew(nodeId);
        
        new QueryDB() {
            public void read(ResultSet result) throws SQLException {
                if (result.next()) {
                    if (result.getString("parent_uuid_msb") != null) {
                        state.setParentId(new NodeId(new UUID(result.getLong("parent_uuid_msb"), result.getLong("parent_uuid_lsb"))));
                    }
                    state.setModCount(result.getShort("mod_count"));
                    state.setNodeTypeName(new QName(result.getString("type_name_ns"), result.getString("type_name_local")));
                    state.setDefinitionId(NodeDefId.valueOf(result.getString("definition")));
                }
            }
        
            public void initStatement(PreparedStatement statement) throws SQLException {
                statement.setLong(1, nodeId.getUUID().getMostSignificantBits());
                statement.setLong(2, nodeId.getUUID().getLeastSignificantBits());
            }
        }.execute("select * from jackrabbit.nodes where uuid_msb = ? and uuid_lsb = ?;");
        
        new QueryDB(){
                public void read(ResultSet result) throws SQLException {
                    while (result.next()) {
                        state.addChildNodeEntry(
                                new QName(result.getString("name_ns"), result.getString("name_local")),
                                new NodeId(new UUID(result.getLong("child_uuid_msb"), result.getLong("child_uuid_lsb")))
                        );
                    }
                }
        
                public void initStatement(PreparedStatement statement) throws SQLException {
                    statement.setLong(1, nodeId.getUUID().getMostSignificantBits());
                    statement.setLong(2, nodeId.getUUID().getLeastSignificantBits());
                }
        
        }.execute("select * from jackrabbit.children where uuid_msb = ? and uuid_lsb = ? order by position;");
        
        new QueryDB(){
            public void read(ResultSet result) throws SQLException {
                while (result.next()) {
                    QName propName = new QName(result.getString("name_ns"), result.getString("name_local"));
                    state.addPropertyName(propName);
                }
            }
    
            public void initStatement(PreparedStatement statement) throws SQLException {
                statement.setLong(1, nodeId.getUUID().getMostSignificantBits());
                statement.setLong(2, nodeId.getUUID().getLeastSignificantBits());
            }
    
        }.execute("select * from jackrabbit.properties where uuid_msb = ? and uuid_lsb = ?;");
        
        final Set<QName> mixins = new HashSet<QName>();
        new QueryDB() {
            public void read(ResultSet result) throws SQLException {
                while (result.next()) {
                    mixins.add(new QName(result.getString("ns"), result.getString("local_name")));
                }
            }
        
            public void initStatement(PreparedStatement statement) throws SQLException {
                statement.setLong(1, nodeId.getUUID().getMostSignificantBits());
                statement.setLong(2, nodeId.getUUID().getLeastSignificantBits());
            }
        
        }.execute("select ns, local_name from xenodot.mixins(jackrabbit.node(?, ?));");
        state.setMixinTypeNames(mixins);

        return state;
    }

    public PropertyState load(final PropertyId propertyId)
            throws NoSuchItemStateException, ItemStateException {
        final PropertyState state = createNew(propertyId);
        
        new QueryDB() {
            public void read(ResultSet result) throws SQLException {
                if (result.next()) {
                    state.setDefinitionId(PropDefId.valueOf(result.getString("definition")));
                    state.setModCount(result.getShort("mod_count"));
                    state.setType(result.getInt("property_type"));
                    if (result.getInt("position") > 0) {
                        state.setMultiValued(true);
                    } else {
                        state.setMultiValued(false);
                    }

                    ArrayList<InternalValue> values = new ArrayList<InternalValue>();
                    do {
                        switch (result.getInt("property_type")) {
                        case PropertyType.STRING:
                            values.add(InternalValue.create(result.getString("value_string")));
                            break;
                        case PropertyType.LONG:
                            values.add(InternalValue.create(result.getLong("value_long")));
                            break;
                        case PropertyType.DOUBLE:
                            values.add(InternalValue.create(result.getDouble("value_double")));
                            break;
                        case PropertyType.DATE:
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(result.getDate("value_date"));
                            values.add(InternalValue.create(cal));
                            break;
                        case PropertyType.BOOLEAN:
                            values.add(InternalValue.create(result.getBoolean("value_boolean")));
                            break;
                        case PropertyType.NAME:
                            values.add(InternalValue.create(
                                       new QName(result.getString("value_name_ns"),
                                                 result.getString("value_name_local")))
                                       );
                            break;
                        case PropertyType.REFERENCE:
                            values.add(InternalValue.create(
                                    new UUID(result.getLong("reference_uuid_msb"),
                                             result.getLong("reference_uuid_ksb"))));
                            break;
                        case PropertyType.BINARY:
                            try {
                                    values.add(InternalValue.create(result.getBinaryStream("value_binary")));
                                } catch (IOException ioe) {
                                    throw new SQLException(ioe.getLocalizedMessage());
                                }
                            break;
                        case PropertyType.PATH:
                            throw new SQLException("Must implement reading path");
                        default:
                            state.setMultiValued(true); // empty array
                        }
                    } while (result.next());
                    state.setValues((InternalValue[]) values.toArray(new InternalValue[values.size()]));
                }
            }
        
            public void initStatement(PreparedStatement statement) throws SQLException {
                statement.setLong(  1, propertyId.getParentId().getUUID().getMostSignificantBits());
                statement.setLong(  2, propertyId.getParentId().getUUID().getLeastSignificantBits());
                statement.setString(3, propertyId.getName().getNamespaceURI());
                statement.setString(4, propertyId.getName().getLocalName());
            }        
        }.execute("select * from jackrabbit.property_values where uuid_msb = ? and uuid_lsb = ? and name_ns = ? and name_local = ?;");
        
        return state;
    }

    public NodeReferences load(final NodeReferencesId id)
            throws NoSuchItemStateException, ItemStateException {
        final NodeReferences refs = new NodeReferences(id);
        new QueryDB() {
            public void read(ResultSet result) throws SQLException {
                while (result.next()) {
                    refs.addReference(
                            new PropertyId(new NodeId(new UUID(result.getLong("uuid_msb"), result.getLong("uuid_lsb"))),
                                           new QName(result.getString("name_ns"), result.getString("name_local")))
                    );
                }
            }
        
            public void initStatement(PreparedStatement statement) throws SQLException {
                statement.setLong(1, id.getTargetId().getUUID().getMostSignificantBits());
                statement.setLong(2, id.getTargetId().getUUID().getLeastSignificantBits());
            }
        }.execute("select uuid_msb, uuid_lsb, name_ns, name_local from jackrabbit.property_values where reference_uuid_msb = ? and reference_uuid_lsb = ?;");
        return refs;
    }

    public synchronized void store(ChangeLog changeLog) throws ItemStateException {
        StorageHelper helper = new StorageHelper();
        helper.prepare(changeLog.addedStates());
        helper.prepare(changeLog.modifiedStates());

        try {
            Iterator<Node> nodes = helper.getNodes();
            while (nodes.hasNext()) {
                final Node node = nodes.next();
                new CallDB() {
                    public void initStatement(CallableStatement statement)
                            throws SQLException {
                        if (node.getParentId() != null) {
                            statement.setLong(1, node.getParentId().getMostSignificantBits());
                            statement.setLong(2, node.getParentId().getLeastSignificantBits());
                        } else {
                            statement.setNull(1, Types.BIGINT);
                            statement.setNull(2, Types.BIGINT);
                        }
                        statement.setLong(  3, node.getId().getMostSignificantBits());
                        statement.setLong(  4, node.getId().getLeastSignificantBits());
                        if (node.getName() == null) {
                            statement.setNull(5, Types.VARCHAR);
                            statement.setNull(6, Types.VARCHAR);
                        } else {
                            statement.setString(5, node.getName().getNamespaceURI());
                            statement.setString(6, node.getName().getLocalName());
                        }
                        statement.setInt(   7, node.getDefinition());
                    }
                }.execute("select jackrabbit.ensure_node(jackrabbit.node(?,?), jackrabbit.uuid(?,?), xenodot.name(?,?), ?)");
            }
            
            nodes = helper.getNodes();
            while (nodes.hasNext()) {
                final Node node = nodes.next();
                new CallDB() {
                    public void initStatement(CallableStatement statement) throws SQLException {
                        statement.setLong(  1, node.getId().getMostSignificantBits());
                        statement.setLong(  2, node.getId().getLeastSignificantBits());
                        statement.setInt(   3, node.getPosition());
                        if (node.getType() == null) {
                            statement.setString(4, "nt");
                            statement.setString(5, "unrestricted");
                        } else {
                            statement.setString(4, node.getType().getNamespaceURI());
                            statement.setString(5, node.getType().getLocalName());
                        }
                        statement.setInt(   6, node.getModCount());
                    }
                }.execute("select jackrabbit.ensure_node(jackrabbit.node(?,?), ?, xenodot.name(?,?), ?)");
                final Set mixinNames = node.getMixinNames();
                new CallDB() {
                    public void initStatement(CallableStatement statement) throws SQLException {
                        statement.setLong(1, node.getId().getMostSignificantBits());
                        statement.setLong(2, node.getId().getLeastSignificantBits());
                        statement.setInt( 3, mixinNames.size());
                    }
                }.execute("select xenodot.prepare_mixins(jackrabbit.node(?,?), ?)");
                final Iterator mixinIterator = mixinNames.iterator();
                while (mixinIterator.hasNext()) {
                    new CallDB() {
                        public void initStatement(CallableStatement statement) throws SQLException {
                            QName mixinName = (QName) mixinIterator.next();
                            statement.setLong(1, node.getId().getMostSignificantBits());
                            statement.setLong(2, node.getId().getLeastSignificantBits());
                            statement.setString(3, mixinName.getNamespaceURI());
                            statement.setString(4, mixinName.getLocalName());
                        }
                    }.execute("select xenodot.add_mixin(jackrabbit.node(?,?), xenodot.name(?,?))");
                }
            }
            
            Iterator<PropertyState> props = helper.getProperties();
            while (props.hasNext()) {
                final PropertyState prop = props.next();

                String sql = "select id from jackrabbit.ensure_property(jackrabbit.node(?,?), xenodot.name(?,?), ?, ?, ?, xenodot.value";
                if (prop.isMultiValued()) {
                    sql += "(0));";
                } else if (prop.getType() == PropertyType.NAME) {
                    sql += "_name(?,?));";
                } else if (prop.getType() == PropertyType.REFERENCE) {
                    sql += "_reference(?,?));";
                } else {
                    sql += "_" + PropertyType.nameFromValue(prop.getType())
                            + "(?));";
                }

                new QueryDB() {
                    public void initStatement(PreparedStatement statement)
                            throws SQLException {
                        statement.setLong(  1, prop.getParentId().getUUID().getMostSignificantBits());
                        statement.setLong(  2, prop.getParentId().getUUID().getLeastSignificantBits());
                        statement.setString(3, prop.getName().getNamespaceURI());
                        statement.setString(4, prop.getName().getLocalName());
                        statement.setInt(   5, prop.getDefinitionId().hashCode());
                        statement.setInt(   6, prop.getModCount());
                        statement.setInt(   7, prop.getType());
                        if (!prop.isMultiValued()) {
                            setValue(statement, prop.getValues()[0], 8);
                        }
                    }

                    public void read(ResultSet result) throws SQLException {
                        if (result.next() && prop.isMultiValued()) {
                            final int propId = result.getInt(1);
                            try {
                                new CallDB() {
                                    public void initStatement(
                                            CallableStatement statement)
                                            throws SQLException {
                                        statement.setInt(1, propId);
                                        statement.setInt(2, prop.getValues().length);
                                    }
                                }.execute("delete from xenodot.property_array where property_id = ? and position > ?;");

                                String sql = "select xenodot.set_property(xenodot.property(?), ?, xenodot.value_";
                                if (prop.getType() == PropertyType.NAME) {
                                    sql += "name(?,?));";
                                } else if (prop.getType() == PropertyType.REFERENCE) {
                                    sql += "reference(?, ?));";
                                } else {
                                    sql += PropertyType.nameFromValue(prop.getType()) + "(?));";
                                }
                                for (int pos = 0; pos < prop.getValues().length; pos++) {
                                    final InternalValue val = prop.getValues()[pos];
                                    final int offset = pos + 1;
                                    new CallDB() {
                                        public void initStatement(
                                                CallableStatement statement)
                                                throws SQLException {
                                            statement.setInt(1, propId);
                                            statement.setInt(2, offset);
                                            setValue(statement, val, 3);
                                        }
                                    }.execute(sql);
                                }
                            } catch (ItemStateException iteme) {
                                throw new SQLException(iteme
                                        .getLocalizedMessage());
                            }
                        }
                    }
                }.execute(sql);
                
                Iterator delIterator = changeLog.deletedStates();
                while (delIterator.hasNext()) {
                    ItemState state = (ItemState) delIterator.next();
                    if (!state.isNode()) {
                        final PropertyState propState = (PropertyState) state;
                        new CallDB() {
                            public void initStatement(CallableStatement statement) throws SQLException {
                                statement.setLong(  1, propState.getParentId().getUUID().getMostSignificantBits());
                                statement.setLong(  2, propState.getParentId().getUUID().getLeastSignificantBits());
                                statement.setString(3, propState.getName().getNamespaceURI());
                                statement.setString(4, propState.getName().getLocalName());
                            }
                        }.execute("select xenodot.delete(jackrabbit.node(?, ?), xenodot.name(?, ?));");
                    }
                }
                delIterator = changeLog.deletedStates();
                while (delIterator.hasNext()) {
                    ItemState state = (ItemState) delIterator.next();
                    if (state.isNode()) {
                        final NodeState nodeState = (NodeState) state;
                        new CallDB() {
                            public void initStatement(CallableStatement statement) throws SQLException {
                                statement.setLong(1, nodeState.getNodeId().getUUID().getMostSignificantBits());
                                statement.setLong(2, nodeState.getNodeId().getUUID().getLeastSignificantBits());
                            }
                        }.execute("select xenodot.delete(jackrabbit.node(?, ?));");
                    }
                }
            }
            database.commit();
        } catch (ItemStateException iteme) {
            try {
                database.rollback();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
            iteme.printStackTrace();
            throw iteme;
        } catch (SQLException sqle) {
            try {
                database.rollback();
            } catch (SQLException sqle2) {
                sqle2.printStackTrace();
            }
            sqle.printStackTrace();
            throw new ItemStateException("Error storing", sqle);
        }
    }

    private void setValue(PreparedStatement statement, InternalValue val,
            int offset) throws SQLException {
        switch (val.getType()) {
        case PropertyType.STRING:
            statement.setString(offset, val.toString());
            break;
        case PropertyType.BINARY:
            BLOBFileValue blob = (BLOBFileValue) val.internalValue();
            try {
                statement.setBinaryStream(offset, blob.getStream(), (int) blob
                        .getLength());
            } catch (Exception ee) {
                throw new SQLException(ee.getLocalizedMessage());
            }
            break;
        case PropertyType.BOOLEAN:
            statement.setBoolean(offset, ((Boolean) val.internalValue())
                    .booleanValue());
            break;
        case PropertyType.DATE:
            Calendar cal = (Calendar) val.internalValue();
            Date date = new Date(cal.getTimeInMillis());
            statement.setDate(offset, date, cal);
            break;
        case PropertyType.DOUBLE:
            statement.setDouble(offset, ((Double) val.internalValue())
                    .doubleValue());
            break;
        case PropertyType.LONG:
            statement.setLong(offset, ((Long) val.internalValue()));
            break;
        case PropertyType.NAME:
            QName name = (QName) val.internalValue();
            statement.setString(offset, name.getNamespaceURI());
            statement.setString(offset + 1, name.getLocalName());
            break;
        case PropertyType.REFERENCE:
            UUID reference = (UUID) val.internalValue();
            statement.setLong(offset, reference.getMostSignificantBits());
            statement.setLong(offset+1, reference.getLeastSignificantBits());
            break;
        default:
            throw new IllegalStateException("Must implement "
                    + PropertyType.nameFromValue(val.getType()));
        }
    }

    private class StorageHelper {
        private HashMap<UUID, Node> nodeMap = new LinkedHashMap<UUID, Node>();

        private ArrayList<PropertyState> properties = new ArrayList<PropertyState>();

        private Iterator<Node> getNodes() {
            return nodeMap.values().iterator();
        }

        private Iterator<PropertyState> getProperties() {
            return properties.iterator();
        }

        private void prepare(Iterator anIterator) {
            while (anIterator.hasNext()) {
                ItemState state = (ItemState) anIterator.next();
                if (state.isNode()) {
                    prepareNode((NodeState) state);
                } else {
                    properties.add((PropertyState) state);
                }
            }
        }

        private void prepareNode(NodeState nodeState) {
            Node node = get(nodeState.getNodeId());
            node.setParentId(nodeState.getParentId());
            node.setDefinition(nodeState.getDefinitionId().hashCode());
            node.setType(nodeState.getNodeTypeName());
            node.setModCount(nodeState.getModCount());
            node.setMixinNames(nodeState.getMixinTypeNames());
            Iterator children = nodeState.getChildNodeEntries().iterator();
            for (int pos = 1; children.hasNext(); pos++) {
                ChildNodeEntry child = (ChildNodeEntry) children.next();
                Node childNode = get(child.getId());
                childNode.setName(child.getName());
                childNode.setPosition(pos);
                childNode.setParentId(nodeState.getNodeId());
            }
        }

        private synchronized Node get(NodeId anId) {
            Node result = nodeMap.get(anId.getUUID());
            if (result == null) {
                result = new Node(anId.getUUID());
                nodeMap.put(anId.getUUID(), result);
            }
            return result;
        }
    }

    private class Node {
        private UUID nodeId;

        private UUID parentId;

        private QName name;
        
        private QName type;
        
        private int modCount = 0;

        private int position = 0;

        private int definition;
        
        private Set mixinNames = new HashSet();

        public Node(UUID anId) {
            nodeId = anId;
        }

        public UUID getId() {
            return nodeId;
        }

        public void setParentId(NodeId anId) {
            if (anId != null) {
                parentId = anId.getUUID();
            }
        }

        public UUID getParentId() {
            return parentId;
        }

        public void setPosition(int aPos) {
            if (aPos != 0) {
                position = aPos;
            }
        }

        public int getPosition() {
            return position;
        }

        public void setName(QName aName) {
            if (aName != null) {
                name = aName;
            }
        }

        public QName getName() {
            return name;
        }

        public void setDefinition(int aDef) {
            if (aDef != 0) {
                definition = aDef;
            }
        }

        public int getDefinition() {
            return definition;
        }

        public int getModCount() {
            return modCount;
        }

        public void setModCount(int modCount) {
            this.modCount = modCount;
        }

        public QName getType() {
            return type;
        }

        public void setType(QName type) {
            if (type != null) {
                this.type = type;
            }
        }

        public Set getMixinNames() {
            return mixinNames;
        }

        public void setMixinNames(Set mixinNames) {
            this.mixinNames = mixinNames;
        }
    };
}