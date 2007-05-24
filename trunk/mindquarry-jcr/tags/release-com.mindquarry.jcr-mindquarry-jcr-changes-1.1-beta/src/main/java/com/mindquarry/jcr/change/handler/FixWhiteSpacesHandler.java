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
package com.mindquarry.jcr.change.handler;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

/**
 * Handler for fixing unicode encoding problems before importing transformed JCR
 * content.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class FixWhiteSpacesHandler implements JcrChangeHandler {
    private static final String WHITE_SPACE_PATTERN = "_x0020_"; //$NON-NLS-1$

    /**
     * @see com.mindquarry.jcr.change.handler.JcrChangeHandler#process(java.io.OutputStream)
     */
    public ByteArrayOutputStream process(ByteArrayInputStream is)
            throws Exception {
        ByteArrayOutputStream result = new ByteArrayOutputStream();

        InputStreamReader isReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isReader);

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.replaceAll(WHITE_SPACE_PATTERN, " "); //$NON-NLS-1$
            result.write(line.getBytes());
        }
        return result;
    }
}
