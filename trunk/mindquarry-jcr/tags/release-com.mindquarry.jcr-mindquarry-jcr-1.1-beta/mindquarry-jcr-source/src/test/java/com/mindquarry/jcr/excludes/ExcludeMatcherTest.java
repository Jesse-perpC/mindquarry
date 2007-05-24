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
package com.mindquarry.jcr.excludes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.cocoon.util.WildcardMatcherHelper;
import org.junit.Test;

public class ExcludeMatcherTest {
    private static String[] excTemplates = new String[] { "jcr:///users/*", //$NON-NLS-1$
            "jcr:///teamspaces/*/metadata.xml", //$NON-NLS-1$
            "jcr:///teamspaces/*/tasks/filter/*" }; //$NON-NLS-1$;

    @Test
    public void testUsrExclusion() {
        assertTrue(checkExclusion("jcr:///users/alexs")); //$NON-NLS-1$
    }

    @Test
    public void testTeamspaceExclusion() {
        assertTrue(checkExclusion("jcr:///teamspaces/mindquarry/metadata.xml")); //$NON-NLS-1$
    }

    @Test
    public void testFilterExclusion() {
        assertTrue(checkExclusion("jcr:///teamspaces/mindquarry/tasks/filter/1")); //$NON-NLS-1$
    }

    @Test
    public void testTaskExclusion() {
        assertFalse(checkExclusion("jcr:///teamspaces/mindquarry/tasks/task1.xml")); //$NON-NLS-1$
    }

    @Test
    public void testWikiExclusion() {
        assertFalse(checkExclusion("jcr:///teamspaces/mindquarry/wiki/wiki1.xml")); //$NON-NLS-1$
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
