/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.management;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class MBeanBase {
    public static final String MANAGEMENT_DOMAIN = "mindquarry";

    static {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
//        HtmlAdaptorServer adaptor = new HtmlAdaptorServer();
//        try {
//            server.registerMBean(adaptor, new ObjectName(
//                    "adaptor:proptocol=HTTP,port=8082"));
//            adaptor.setPort(4444);
//            adaptor.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    protected ObjectInstance instance;

    public MBeanBase(String objectName) {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        try {
            instance = server.registerMBean(this, new ObjectName(
                    MANAGEMENT_DOMAIN + ":" + objectName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
