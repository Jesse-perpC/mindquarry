package com.mindquarry.persistence.xmlbeans.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.custommonkey.xmlunit.Diff;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mindquarry.common.xml.EntityXmlizer;
import com.mindquarry.common.xml.EntityXmlizerFactory;
import com.mindquarry.persistence.xmlbeans.XmlBeansPersistenceTestBase;
import com.mindquarry.types.teamspace.Teamspace;
import com.mindquarry.types.teamspace.TeamspaceDocument;
import com.mindquarry.types.user.Email;
import com.mindquarry.types.user.User;

public class XmlBeansEntityXmlizerTest extends XmlBeansPersistenceTestBase {

    private static final String CONTENT_FILE = "/com/mindquarry/persistence/xmlbeans/xml/TestContent.xml";
    
    public void testXmlBeansEntityXmlizer() throws Exception {
        
        ByteArrayOutputStream actualOut = new ByteArrayOutputStream();
        
        SAXTransformerFactory factory = 
            (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        TransformerHandler handler = factory.newTransformerHandler();
        handler.setResult(new StreamResult(System.out));
        
        handler.startDocument();
        
        User user = makeUser("bastian");
        Teamspace teamspace = makeTeamspace();
        
        String xmlizerFactoryName = EntityXmlizerFactory.class.getName();
        EntityXmlizerFactory xmlizerFactory = 
            (EntityXmlizerFactory) lookup(xmlizerFactoryName);
        
        EntityXmlizer userXmlizer = xmlizerFactory.newEntityXmlizer(user);
        EntityXmlizer teamspaceXmlizer = xmlizerFactory.newEntityXmlizer(teamspace);
        
        //teamspaceXmlizer.beforeEndEntityElement(userXmlizer);
        teamspaceXmlizer.toSax(handler);
        
        handler.endDocument();
        
//        InputStream actualIn = new ByteArrayInputStream(actualOut.toByteArray());
//        InputStream expected = getClass().getResourceAsStream(CONTENT_FILE);
//        
//        isXmlEqual(expected, actualIn);
    }
    
    private boolean isXmlEqual(InputStream expected, InputStream actual)
        throws SAXException, IOException, ParserConfigurationException {
        
        InputSource expectedSource = new InputSource(expected);
        InputSource actualSource = new InputSource(actual);
        Diff xmlDiff = new Diff(expectedSource, actualSource);
        return xmlDiff.similar();
    }
    
    private User makeUser(String userId) {
        User result = User.Factory.newInstance();
        
        result.setId(userId);
        result.setPassword("password");
        
        result.setName("firstname");
        result.setSurname("lastname");
        
        Email email = result.addNewEmail();
        email.setAddress("firstname.lastname@mindquarry.com");
        email.setIsConversationRecipient(true);
        
        return result;
    }
    
    private Teamspace makeTeamspace() {
        Teamspace teamspace = Teamspace.Factory.newInstance();
        teamspace.setDescription("a great description");
        teamspace.setId("mindquarry");
        teamspace.setName("Mindquarry");
        
        return teamspace;
    }
}
