/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.xml.source.helper;

import java.io.IOException;
import java.io.InputStream;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import org.apache.excalibur.source.SourceNotFoundException;

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
    public static InputStream getInputStream(Node node) throws IOException,
            SourceNotFoundException {
        try {
            Node content = node.getNode("jcr:content");
            return content.getProperty("jcr:data").getStream();
        } catch (PathNotFoundException e) {
            throw new SourceNotFoundException("No valid jcr:data property", e);
        } catch (RepositoryException e) {
            throw new IOException("Unable to read from repository: "
                    + e.getLocalizedMessage());
        }
    }
}
