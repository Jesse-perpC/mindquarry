package com.mindquarry.jcr.excludes;

import junit.framework.TestCase;

import org.apache.cocoon.util.WildcardMatcherHelper;

public class ExcludeMatcherTest extends TestCase {
    private String[] excTemplates = new String[] { "jcr:///users/*", //$NON-NLS-1$
            "jcr:///teamspaces/*/metadata.xml", //$NON-NLS-1$
            "jcr:///teamspaces/*/tasks/filter/*" }; //$NON-NLS-1$

    public void testUsrExclusion() {
        TestCase.assertTrue(checkExclusion("jcr:///users/alexs")); //$NON-NLS-1$
    }
    
    public void testTeamspaceExclusion() {
        TestCase.assertTrue(checkExclusion("jcr:///teamspaces/mindquarry/metadata.xml")); //$NON-NLS-1$
    }
    
    public void testFilterExclusion() {
        TestCase.assertTrue(checkExclusion("jcr:///teamspaces/mindquarry/tasks/filter/1")); //$NON-NLS-1$
    }
    
    public void testTaskExclusion() {
        TestCase.assertFalse(checkExclusion("jcr:///teamspaces/mindquarry/tasks/task1.xml")); //$NON-NLS-1$
    }
    
    public void testWikiExclusion() {
        TestCase.assertFalse(checkExclusion("jcr:///teamspaces/mindquarry/wiki/wiki1.xml")); //$NON-NLS-1$
    }

    private boolean checkExclusion(String term) {
        for (String template : excTemplates) {
            if (WildcardMatcherHelper.match(template, term) != null) {
                return true;
            }
        }
        return false;
    }
}
