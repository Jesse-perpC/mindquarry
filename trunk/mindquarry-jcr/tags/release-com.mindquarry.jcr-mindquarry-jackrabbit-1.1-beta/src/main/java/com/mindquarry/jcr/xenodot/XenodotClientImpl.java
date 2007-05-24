package com.mindquarry.jcr.xenodot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class XenodotClientImpl implements XenodotClient {
    
    private String uri;
    
    public Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            Properties props = new Properties();
            props.setProperty("user", "xenodot");
            Connection connection = DriverManager.getConnection(uri, props);
            connection.setAutoCommit(false);
            return connection;
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
