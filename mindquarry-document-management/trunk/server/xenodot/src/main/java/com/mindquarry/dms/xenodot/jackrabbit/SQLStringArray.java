package com.mindquarry.dms.xenodot.jackrabbit;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public class SQLStringArray implements Array {
    final StringBuffer values;
    
    public SQLStringArray(Set aSet) {
        values = new StringBuffer("{");
        for (Object name : aSet) {
            values.append('"');
            values.append(name.toString().replaceAll("\"", "\\\""));
            values.append('"');
        }
        values.append("}");
    }

    public Object getArray() throws SQLException {
        return null;
    }

    public Object getArray(Map<String, Class<?>> map) throws SQLException {
        return null;
    }

    public Object getArray(long index, int count) throws SQLException {
        return null;
    }

    public Object getArray(long index, int count, Map<String, Class<?>> map)
            throws SQLException {
        return null;
    }

    public int getBaseType() throws SQLException {
        return 0;
    }

    public String getBaseTypeName() throws SQLException {
        return "text";
    }

    public ResultSet getResultSet() throws SQLException {
        return null;
    }

    public ResultSet getResultSet(Map<String, Class<?>> map)
            throws SQLException {
        return null;
    }

    public ResultSet getResultSet(long index, int count) throws SQLException {
        return null;
    }

    public ResultSet getResultSet(long index, int count,
            Map<String, Class<?>> map) throws SQLException {
        return null;
    }
    
    public String toString() {
        return values.toString();
    }

}