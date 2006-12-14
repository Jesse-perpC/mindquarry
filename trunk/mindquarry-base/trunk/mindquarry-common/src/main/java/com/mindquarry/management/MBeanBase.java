/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.management;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import com.sun.jdmk.comm.HtmlAdaptorServer;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class MBeanBase {
    public static final String MANAGEMENT_DOMAIN = "mindquarry";

    public static final int HTML_ADAPTOR_PORT = 4444;

    static {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        HtmlAdaptorServer adaptor = new HtmlAdaptorServer();
        try {
            server.registerMBean(adaptor, new ObjectName(
                    "adaptor:proptocol=HTTP,port=" + HTML_ADAPTOR_PORT)); //$NON-NLS-1$
            adaptor.setPort(HTML_ADAPTOR_PORT);
            adaptor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected ObjectInstance instance;

    public MBeanBase(String objectName) {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        try {
            instance = server.registerMBean(this, new ObjectName(
                    MANAGEMENT_DOMAIN + ":" + objectName)); //$NON-NLS-1$
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
