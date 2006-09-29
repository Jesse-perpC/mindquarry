package com.mindquarry.conversation.jcr;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.jcr.LoginException;
import javax.jcr.NamespaceException;
import javax.jcr.NamespaceRegistry;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.naming.NamingException;

import junit.framework.TestCase;

import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.core.nodetype.InvalidNodeTypeDefException;
import org.apache.jackrabbit.core.nodetype.NodeTypeDef;
import org.apache.jackrabbit.core.nodetype.NodeTypeManagerImpl;
import org.apache.jackrabbit.core.nodetype.NodeTypeRegistry;
import org.apache.jackrabbit.core.nodetype.compact.CompactNodeTypeDefReader;
import org.apache.jackrabbit.core.nodetype.compact.ParseException;
import org.apache.jackrabbit.rmi.remote.RemoteRepository;
import org.apache.jackrabbit.rmi.server.ServerAdapterFactory;

public class JackRabbitSetupTest extends TestCase {
	private static List<String> projects;

	private static List<String> tags;

	private static List<Hashtable<String, String>> users;

	public static final String MINDQUARRY_JCR_XML_NAMESPACE_PREFIX = "xt";

	public static final String MINDQUARRY_JCR_XML_NAMESPACE_URI = "http://mindquarry.com/ns/cnd/xt";

	public static final String MINDQUARRY_JCR_XML_NODETYPES_FILE = "src/main/resources/node-types.txt";

	static {
		projects = new ArrayList<String>();
		projects.add("Mindquarry");
		projects.add("Cyclr");

		tags = new ArrayList<String>();
		tags.add("dev");
		tags.add("support");

		users = new ArrayList<Hashtable<String, String>>();
		Hashtable<String, String> user = new Hashtable<String, String>();
		user.put("name", "Alexander Saar");
		user.put("mail", "alexander.saar@mindquarry.com");
		users.add(user);
		user = new Hashtable<String, String>();
		user.put("name", "Lars Trieloff");
		user.put("mail", "lars.trieloff@mindquarry.com");
		users.add(user);
	}

	private static final String LOGIN = "alexander.saar";

	private static final String PWD = "mypwd";

	private static final String WORKSPACE = "default";

	private static final String REMOTE_REPO_NAME = "jackrabbit";

	private Repository repo;

	private Registry reg;

	private Session session;

	/**
	 * Initialize Jackrabbit repository.
	 */
	@Override
	protected void setUp() throws Exception {
		repo = new TransientRepository("src/test/resources/repository.xml",
				"target/repository");
		ServerAdapterFactory factory = new ServerAdapterFactory();
		RemoteRepository remoteRepo = factory.getRemoteRepository(repo);

		reg = LocateRegistry.createRegistry(1100);
		reg.rebind(REMOTE_REPO_NAME, remoteRepo);

		session = repo.login(new SimpleCredentials(LOGIN, PWD.toCharArray()),
				WORKSPACE);
	}

	/**
	 * Shutdown Jackrabbit repository.
	 */
	@Override
	protected void tearDown() throws Exception {
		// uncomment this for shutting down RMI repository
		reg.unbind(REMOTE_REPO_NAME);
	}

	/**
	 * Setup repository data for conversation tests.
	 */
	public void testSetupRepositoryData() throws IOException, LoginException,
			RepositoryException, NamingException, ClassCastException,
			NotBoundException, InvalidNodeTypeDefException, ParseException {
		try {
			String login = session.getUserID();
			String repoName = repo.getDescriptor(Repository.REP_NAME_DESC);
			System.out.println("Logged in as " + login + " to a " + repoName
					+ " repository.");

			setupNamespacesAndNodeTypes(session.getWorkspace());
			setupTestContent();
			session.save();

			System.out.println("Please press <enter> to shutdown the server.");
			System.in.read();
		} finally {
			session.logout();
		}
	}

	private void setupTestContent() {
//		String tagRef = null;
//
//		// store project data
//		Node projectsNode = root.addNode("projects");
//		for (String project : projects) {
//			Node node = projectsNode.addNode("project");
//			node.setProperty("name", project);
//
//			// store tag data
//			Node tagsNode = node.addNode("tags");
//			for (String tag : tags) {
//				Node tagNode = tagsNode.addNode("tag");
//				tagNode.setProperty("name", tag);
//				tagNode.addMixin("mix:referenceable");
//
//				if (tag.equals("dev")) {
//					tagRef = tagNode.getUUID();
//				}
//			}
//			// add conversations node
//			Node talkNode = node.addNode("talk");
//			Node convNode = talkNode.addNode("conversation");
//			convNode.setProperty("id", project + "-1");
//
//			Node tags = convNode.addNode("tags");
//			Node tag = tags.addNode("tag");
//			tag.setProperty("reference", session.getNodeByUUID(tagRef));
//
//			convNode.addNode("subscribers");
//			convNode.addNode("contributions");
//		}
//		// store user data
//		Node membersNode = root.addNode("users");
//		for (Hashtable<String, String> user : users) {
//			Node memberNode = membersNode.addNode("user");
//			memberNode.setProperty("name", user.get("name"));
//			memberNode.setProperty("mail", user.get("mail"));
//			memberNode.addMixin("mix:referenceable");
//
//			Node tagsNode = memberNode.addNode("tags");
//			Node tagNode = tagsNode.addNode("tag");
//			tagNode.setProperty("reference", session.getNodeByUUID(tagRef));
//
//			if (user.get("name").equals("Alexander Saar")) {
//				Node convNode = session
//						.getRootNode()
//						.getNode(
//								"projects/project[1]/talk/conversation[1]/subscribers");
//				Node subscriberNode = convNode.addNode("subscriber");
//				subscriberNode.setProperty("reference", memberNode);
//			}
//		}
	}

	private void setupNamespacesAndNodeTypes(Workspace workspace)
			throws ParseException, RepositoryException,
			InvalidNodeTypeDefException, SourceNotFoundException, IOException {
		// register xt:* namespace
		NamespaceRegistry nsRegistry = workspace.getNamespaceRegistry();
		try {
			// check if the namespace already exists
			nsRegistry.getURI(MINDQUARRY_JCR_XML_NAMESPACE_PREFIX);
		} catch (NamespaceException ne) {
			nsRegistry.registerNamespace(MINDQUARRY_JCR_XML_NAMESPACE_PREFIX,
					MINDQUARRY_JCR_XML_NAMESPACE_URI);
		}

		// Get the NodeTypeManager from the Workspace. Note that it must be
		// casted from the generic JCR NodeTypeManager to the
		// Jackrabbit-specific implementation.
		NodeTypeManagerImpl ntmgr = (NodeTypeManagerImpl) workspace
				.getNodeTypeManager();

		// Acquire the NodeTypeRegistry
		NodeTypeRegistry ntreg = ntmgr.getNodeTypeRegistry();

		// register the default node types (xt:element etc.)
		FileInputStream fis = new FileInputStream(MINDQUARRY_JCR_XML_NODETYPES_FILE);
		InputStreamReader reader = new InputStreamReader(fis);

		// Create a CompactNodeTypeDefReader
		CompactNodeTypeDefReader cndReader = new CompactNodeTypeDefReader(
				reader, "");

		// Get the List of NodeTypeDef objects
		List ntdList = cndReader.getNodeTypeDefs();

		for (Iterator i = ntdList.iterator(); i.hasNext();) {
			// Get the NodeTypeDef...
			NodeTypeDef ntd = (NodeTypeDef) i.next();

			// ...and register it
			ntreg.registerNodeType(ntd);
		}
	}
}
