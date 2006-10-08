package com.mindquarry.persistence.castor;

import java.util.List;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.apache.avalon.framework.service.ServiceException;

import com.mindquarry.common.persistence.Session;
import com.mindquarry.common.persistence.SessionFactory;

public class CastorPersistenceTest extends CastorPersistenceTestBase {

    public void testConversation() throws ServiceException, NotSupportedException, SystemException {
                
        SessionFactory sessionFactory = (SessionFactory) lookup(SessionFactory.ROLE);
        Session session = sessionFactory.currentSession();
        
        UserEntity user = (UserEntity) session.newEntity(UserEntity.class);
        
        user.setId("bastian");
        
        user.setName("Bastain");
        user.setSurname("Steinert");
        
        TeamspaceEntity teamspace = 
            (TeamspaceEntity) session.newEntity(TeamspaceEntity.class);
        teamspace.setDescription("a great description");
        teamspace.setId("mindquarry");
        teamspace.setName("Mindquarry");
        
        session.commit();
        
        
        session = sessionFactory.currentSession();
        
        List queryResult = session.query("getUserById", new Object[] {"bastian"});
        UserEntity persistentUser = (UserEntity) queryResult.get(0);
        assertEquals("bastian", persistentUser.getId());
        
        session.delete(persistentUser);
        
        session.commit();
        
        
        session = sessionFactory.currentSession();
        
        queryResult = session.query("getUserById", new Object[] {"bastian"});
        assertTrue(queryResult.isEmpty());       
        
        session.commit();
    }
}
