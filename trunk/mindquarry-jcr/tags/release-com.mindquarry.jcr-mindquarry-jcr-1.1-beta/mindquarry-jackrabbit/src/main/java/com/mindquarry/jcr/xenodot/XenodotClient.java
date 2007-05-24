package com.mindquarry.jcr.xenodot;

import java.sql.Connection;

public interface XenodotClient {
    public static final String ROLE = XenodotClient.class.getName();
    
    public Connection getConnection();
}
