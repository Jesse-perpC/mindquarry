package com.mindquarry.common.fp;

import static org.junit.Assert.*;

import org.junit.Test;

public class BeanPropertiesPredicateTest {

    @Test
    public void testBeanPredicate() {
        TestBean testBean = new TestBean();
        testBean.foo = "bar";        
        assertTrue(new BeanPropertiesPredicate().where("foo", "bar").execute(testBean));
        assertFalse(new BeanPropertiesPredicate().where("foo", "").execute(testBean));
        assertFalse(new BeanPropertiesPredicate().where("other", "").execute(testBean));        
    }
    
    public static class TestBean {
        public String foo;
        
        public String getFoo() {
            return foo;
        }
        
        public void setFoo(String value) {
            this.foo = value;
        }
    }
}
