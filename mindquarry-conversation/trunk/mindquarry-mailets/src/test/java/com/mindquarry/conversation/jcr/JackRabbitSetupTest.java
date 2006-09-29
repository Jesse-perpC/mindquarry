package com.mindquarry.conversation.jcr;

import java.util.GregorianCalendar;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.jcr.jackrabbit.JCRTestBaseStandalone;
import com.mindquarry.persistence.xmlbeans.XmlBeansSessionFactoryStandalone;
import com.mindquarry.types.tag.Tag;
import com.mindquarry.types.teamspace.Teamspace;
import com.mindquarry.types.user.Email;
import com.mindquarry.types.user.User;

public class JackRabbitSetupTest extends JCRTestBaseStandalone {
	public void testSetupRepositoryData() throws Exception {
		XmlBeansSessionFactoryStandalone factory = new XmlBeansSessionFactoryStandalone();
		factory.initialize();
		Session persistSession = factory.currentSession();

		User lars = (User) persistSession.newEntity(User.class);
		lars.setId("lars.trieloff");
		lars.setPassword("mypwd");
		lars.setName("Lars");
		lars.setSurname("Trieloff");
		Email email = lars.addNewEmail();
		email.setAddress("lars.trieloff@mindquarry.com");
		email.setIsConversationRecipient(true);
		persistSession.persist(lars);

		User alex = (User) persistSession.newEntity(User.class);
		alex.setId("alexander.saar");
		alex.setPassword("mypwd");
		alex.setName("Alexander");
		alex.setSurname("Saar");
		email = alex.addNewEmail();
		email.setAddress("alexander.saar@mindquarry.com");
		email.setIsConversationRecipient(true);
		persistSession.persist(alex);
		
		Teamspace mindquarry = (Teamspace) persistSession.newEntity(Teamspace.class);
		mindquarry.setDescription("You know what Mindwuarry is");
		mindquarry.setId("mindquarry");
		mindquarry.setName("Mindquarry");
		persistSession.persist(mindquarry);
		
		Teamspace cyclr = (Teamspace) persistSession.newEntity(Teamspace.class);
		cyclr.setDescription("You also know what Cyclr is");
		cyclr.setId("cyclr");
		cyclr.setName("Cyclr");
		persistSession.persist(cyclr);
		
//		Tag dev = (Tag) persistSession.newEntity(Tag.class);
//		dev.setId("dev");
//		dev.setCreated(new GregorianCalendar());
		//dev.setCreator();

		// String tagRef = null;
		//
		// // store project data
		// Node projectsNode = session.getRootNode().addNode("projects");
		// for (String project : projects) {
		// Node node = projectsNode.addNode("project");
		// node.setProperty("name", project);
		//
		// // store tag data
		// Node tagsNode = node.addNode("tags");
		// for (String tag : tags) {
		// Node tagNode = tagsNode.addNode("tag");
		// tagNode.setProperty("name", tag);
		// tagNode.addMixin("mix:referenceable");
		//
		// if (tag.equals("dev")) {
		// tagRef = tagNode.getUUID();
		// }
		// }
		// // add conversations node
		// Node talkNode = node.addNode("talk");
		// Node convNode = talkNode.addNode("conversation");
		// convNode.setProperty("id", project + "-1");
		//
		// Node tags = convNode.addNode("tags");
		// Node tag = tags.addNode("tag");
		// tag.setProperty("reference", session.getNodeByUUID(tagRef));
		//
		// convNode.addNode("subscribers");
		// convNode.addNode("contributions");
		// }
		// // store user data
		// Node membersNode = root.addNode("users");
		// for (Hashtable<String, String> user : users) {
		// Node memberNode = membersNode.addNode("user");
		// memberNode.setProperty("name", user.get("name"));
		// memberNode.setProperty("mail", user.get("mail"));
		// memberNode.addMixin("mix:referenceable");
		//
		// Node tagsNode = memberNode.addNode("tags");
		// Node tagNode = tagsNode.addNode("tag");
		// tagNode.setProperty("reference", session.getNodeByUUID(tagRef));
		//
		// if (user.get("name").equals("Alexander Saar")) {
		// Node convNode = session
		// .getRootNode()
		// .getNode(
		// "projects/project[1]/talk/conversation[1]/subscribers");
		// Node subscriberNode = convNode.addNode("subscriber");
		// subscriberNode.setProperty("reference", memberNode);
		// }
		// }
		System.out.println("Please press <enter> to shutdown the server.");
		System.in.read();

	}
}
