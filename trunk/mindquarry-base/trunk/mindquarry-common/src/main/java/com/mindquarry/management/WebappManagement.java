/**
 * Copyright (C) 2006 Mindquarry GmbH, All Rights Reserved
 */
package com.mindquarry.management;

import java.lang.management.ManagementFactory;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class WebappManagement extends MBeanBase implements
        WebappManagementMBean {
    public static final String OBJECT_NAME = "domain=webapp,type=ManagementService";

    public WebappManagement() {
        super(OBJECT_NAME);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.mindquarry.management.WebappManagementMBean#getUptime()
     */
    public Long getUptime() {
        return ManagementFactory.getRuntimeMXBean().getUptime();
    }
}
