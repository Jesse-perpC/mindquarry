/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package com.mindquarry.jcr.id;

import javax.jcr.Item;
import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.lock.LockException;

import com.mindquarry.jcr.client.JCRClient;

/**
 * Creates unique IDs for certain paths in JCR, eg. to have uniquely named
 * subnodes below a certain path. This is done by storing a special subnode
 * which contains the currently highest ID. Access to this node is synchronized
 * via the JCR locking mechanism. Before using the getNextID() method, you have
 * to call the initializePath() method in a safe environment (not with
 * concurrent access).
 * 
 * <p>
 * The node type <code>id:node</code> must be defined for the repository, eg.
 * via a CND file for Jackrabbit. The type must extend <code>mix:lockable</code>
 * (the standard mixin needed to lock a node) and must have a property
 * <code>id:id</code> of type long, which is mandatory and must get autocreated
 * with a default value of 0. For usage in <code>nt:folder</code> hierarchies,
 * it is best to also extend <code>nt:hierarchyNode</code>.
 * </p>
 * 
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 * 
 */
public class JCRUniqueIDGenerator extends JCRClient {
    
    public final static String ID_NODE = "id:unique";
    
    public final static String ID_NODE_TYPE = "id:node";

    public final static String ID_PROPERTY = "id:id";
    
    //public final static String LOCKABLE_NODE_TYPE = "mix:lockable";

    private final static long LOCK_SLEEP_MILLIS = 10;

    /**
     * Get the next ID which is unique for anything below <code>jcrPath</code>.
     */
    public long getNextID(String jcrPath) throws IDException {
        // sometimes client code is using the string with a source resolver
        // as well, where it must provide the scheme prefix; so we offer it
        // to work with both variants for simplicity of the client code
        if (jcrPath.startsWith("jcr://")) {
            jcrPath = jcrPath.substring("jcr://".length());
        }
        
        Session session;
        try {
            session = login();

            Node node = getLockableIDNode(session, jcrPath);
            long resultID = lockNodeAndGetNextID(session, node, jcrPath);
            
            session.logout();
            
            return resultID;
        } catch (LoginException e) {
            throw new IDException("Login to repository failed.", e);
        } catch (RepositoryException e) {
            throw new IDException("Cannot access repository.", e);
        }
    }
    
    /**
     * This will prepare <code>jcrPath</code> to have a storage node for the
     * unique id. Call this method on initialization time when no concurrent
     * access will hapen.
     */
    public void initializePath(String jcrPath) throws IDException {
        // sometimes client code is using the string with a source resolver
        // as well, where it must provide the scheme prefix; so we offer it
        // to work with both variants for simplicity of the client code
        if (jcrPath.startsWith("jcr://")) {
            jcrPath = jcrPath.substring("jcr://".length());
        }

        Session session;
        try {
            session = login();

            Item item = session.getItem(jcrPath);
            if (!item.isNode()) {
                throw new IDException("Path '" + jcrPath
                        + "' is a property (should be a node)");
            } else {
                // check if it has a subnode containing a unique id
                Node parent = (Node) item;
                if (!parent.hasNode(ID_NODE)) {
                    // create the id node if it does not exist yet
                    parent.addNode(ID_NODE, ID_NODE_TYPE);
                    session.save();
                }
                /*
                // ensure the node is lockable
                if (!node.isNodeType(LOCKABLE_NODE_TYPE)) {
                    node.addMixin(LOCKABLE_NODE_TYPE);
                }
                */
            }
            session.logout();
        } catch (LoginException e) {
            throw new IDException("Login to repository failed.", e);
        } catch (PathNotFoundException e) {
            throw new IDException("Repository path does not exist: " + jcrPath,
                    e);
        } catch (RepositoryException e) {
            throw new IDException("Cannot lookup repository path: " + jcrPath,
                    e);
        }        
    }
    
    /**
     * This will use the give node (which must be lockable) as a container for
     * a unique id. The id will be stored in a property. Before accessing it,
     * the node will be locked or it will wait for taking the lock. After a
     * successful lock, it will return the latest id + 1, this will also be
     * stored as the new value in the property of the node.
     */
    private long lockNodeAndGetNextID(Session session, Node node, String jcrPath) throws IDException {
        // we need to synchronize the access to the node which holds the
        // unique id; thus we lock it before reading and modifying it; if
        // locking is not possible because the node is locked, 
        
        // loop until node.lock() was successful
        while (true) {
            try {
                node.lock(false, false);
                break;
            } catch (LockException e) {
                try {
                    Thread.sleep(LOCK_SLEEP_MILLIS);
                } catch (InterruptedException e1) {
                    // just ignore and go to the beginning of the loop
                }
            /*} catch (UnsupportedRepositoryOperationException e) {
            } catch (AccessDeniedException e) {
            } catch (InvalidItemStateException e) {*/
            } catch (RepositoryException e) {
                throw new IDException("Cannot lock unique id node for " + jcrPath, e);
            }
        }
        
        // the node is locked
        long resultID;
        try {
            // normal increment access
            Property idProp = node.getProperty(ID_PROPERTY);
            resultID = idProp.getLong();
            resultID++;
            idProp.setValue(resultID);

            node.save();
            
        /*} catch (PathNotFoundException e) {
        } catch (ValueFormatException e) {
        } catch (VersionException e) {
        } catch (LockException e) {
        } catch (ConstraintViolationException e) {*/
        } catch (RepositoryException e) {
            throw new IDException("Cannot modify unique id for " + jcrPath, e);
        }
        
        // now remove the lock
        try {
            node.unlock();
        /*} catch (UnsupportedRepositoryOperationException e) {
        } catch (LockException e) {
        } catch (AccessDeniedException e) {
        } catch (InvalidItemStateException e) {*/
        } catch (RepositoryException e) {
            throw new IDException("Cannot unlock unique id node for " + jcrPath, e);
        }
        
        return resultID;
    }

    /**
     * Gets the subnode for <code>jcrPath</code> that contains the current
     * value of the highest id. This is "thread-safe".
     */
    private Node getLockableIDNode(Session session, String jcrPath) throws IDException {
        try {
            Item item = session.getItem(jcrPath);
            if (!item.isNode()) {
                throw new IDException("Path '" + jcrPath
                        + "' is a property (should be a node)");
            } else {
                return ((Node) item).getNode(ID_NODE);
            }
        } catch (PathNotFoundException e) {
            throw new IDException("Repository path does not exist: " + jcrPath,
                    e);
        } catch (RepositoryException e) {
            throw new IDException("Cannot lookup repository path: " + jcrPath,
                    e);
        }
    }
}
