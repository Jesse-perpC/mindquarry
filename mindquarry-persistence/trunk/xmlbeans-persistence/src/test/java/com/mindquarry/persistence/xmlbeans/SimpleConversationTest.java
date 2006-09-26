package com.mindquarry.persistence.xmlbeans;

import com.mindquarry.common.persistence.Persistence;
import com.mindquarry.persistence.xmlbeans.XmlBeansPersistence;
import com.mindquarry.types.conversation.Conversation;

import junit.framework.TestCase;

public class SimpleConversationTest extends TestCase {

    public void testConversation() {
        Persistence persistence = new XmlBeansPersistence();
        
        Conversation conv = (Conversation) persistence.newInstance(Conversation.class);
        conv.setId("newId");
        conv.setName("Conversation Title");
        
        //persistence.persist(conv);
        
        //String greatUuid = "00,1145646412313df14534s";
        //conv = (Conversation) persistence.query("GetById", new Object[] {greatUuid});
    }
}
