/**
 * Copyright (C) 2006 MindQuarry GmbH, All Rights Reserved
 */
package com.mindquarry.jcr.xml.source.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.excalibur.source.ModifiableTraversableSource;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceNotFoundException;

import com.mindquarry.jcr.xml.source.AbstractJCRNodeSource;
import com.mindquarry.jcr.xml.source.JCRSourceFactory;
import com.mindquarry.jcr.xml.source.helper.stream.XMLFileOutputStream;

/**
 * Source for a node that represents a file (nt:file) or a folder (nt:folder).
 * 
 * @author <a
 *         href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">Alexander
 *         Klimetschek</a>
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class FileSourceHelper {
    // =========================================================================
    // Source interface
    // =========================================================================

    /**
     * @param node 
     * @see org.apache.excalibur.source.Source#getInputStream()
     */
    public static InputStream getInputStream(Node node) throws IOException,
            SourceNotFoundException {
    	try {
			Node content = node.getNode("jcr:content");
			return content.getProperty("jcr:data").getStream();
		} catch (PathNotFoundException e) {
			throw new SourceNotFoundException("No valid jcr:data property", e);
		} catch (RepositoryException e) {
			throw new IOException("Unable to read from repository: " + e.getLocalizedMessage());
		}
    }


    // =========================================================================
    // ModifiableSource interface
    // =========================================================================

    /**
     * @see org.apache.excalibur.source.ModifiableSource#canCancel(java.io.OutputStream)
     */
    public static boolean canCancel(OutputStream stream) {
        return false;
    }

    /**
     * @see org.apache.excalibur.source.ModifiableSource#cancel(java.io.OutputStream)
     */
    public static void cancel(OutputStream stream) throws IOException {
    }

}
