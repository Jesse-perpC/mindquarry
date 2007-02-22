/*
 * Copyright (C) 2006-2007 MindQuarry GmbH, All Rights Reserved
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
package com.mindquarry.jcr.xml.source.helper;

import java.io.IOException;
import java.io.InputStream;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import org.apache.excalibur.source.SourceNotFoundException;

import com.mindquarry.jcr.xml.source.JCRConstants;

/**
 * Source helper for nodes that represents a file (nt:file) with binary
 * content.
 * 
 * @author <a href="mailto:lars(dot)trieloff(at)mindquarry(dot)com">Lars
 *         Trieloff</a>
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class FileSourceHelper {
    // =========================================================================
    // Source interface delegates
    // =========================================================================

    /**
     * Delegate for getInputStream.
     */
    public static InputStream getInputStream(Node content) throws IOException,
            SourceNotFoundException {
        try {
            return content.getProperty(JCRConstants.JCR_DATA).getStream();
        } catch (PathNotFoundException e) {
            throw new SourceNotFoundException("No valid jcr:data property", e);
        } catch (RepositoryException e) {
            throw new IOException("Unable to read from repository: "
                    + e.getLocalizedMessage());
        }
    }
}
