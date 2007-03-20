package com.mindquarry.jcr.xml.source;

import java.net.URL;

import com.mindquarry.dms.xenodot.jackrabbit.XenodotPersistenceManager;

public class XenodotTestBase extends JCRSourceTestBase {
    
    protected void prepare() throws Exception {
        Class klass = getClass();
        while (klass != Object.class) {
            try {
                String className = klass.getName();
                String xtestResourceName = className.replace('.', '/') + ".xtest";
                URL xtestResource = getClass().getClassLoader().getResource(xtestResourceName);
                this.prepare(xtestResource.openStream());
                return;
            } catch (Exception exp) {
                klass = klass.getSuperclass();
            }
        }
        throw new IllegalStateException("Should not get here!");
    }

    protected void setUp() throws Exception {
        // TODO: Right now XenodotPersistenceManger is hardcoded, so this will work
        System.err.println("Start cleaning Xenodot in setUp()");
        XenodotPersistenceManager manager = new XenodotPersistenceManager();
        manager.init(null);
        manager.getConnection().prepareCall("delete from xenodot.property;").execute();            
        manager.getConnection().prepareCall("delete from xenodot.value where reference_id is not null;").execute();        
        manager.getConnection().prepareCall("delete from xenodot.node;").execute();
        manager.close();
        System.err.println("Done cleaning Xenodot in setUp()");
        System.err.println("Now calling super.setUp()");
        super.setUp();
        System.err.println("Done calling super.setUp()");
    }
}
