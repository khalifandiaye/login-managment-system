package de.uplinkgmbh.lms.entitys;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import de.axone.tools.E;
import de.uplinkgmbh.lms.presistence.MyPersistenceManager;

@Test( groups="lms.entity" )
public class EntityTests {

	@AfterTest(groups = {"Inv"})
	public void entitytest() throws Exception {
		
		Application app = new Application();
		app.setName( "system2.uplink-gmbh.de" );
		
		MyPersistenceManager pm = MyPersistenceManager.getInstance();
		EntityManager em = pm.getEntityManager();

		em.getTransaction().begin();	
		em.persist( app );
		em.getTransaction().commit();
		em.clear();
		
		Groups gr = new Groups();
		gr.setApplication( app );
		gr.setName( "gr" );

		em.getTransaction().begin();	
		em.persist( gr );
		em.getTransaction().commit();
		em.clear();
		
		Groups gr2;
		
		em.getTransaction().begin();	
		gr2 = em.find( Groups.class, gr.getId() );
		em.getTransaction().commit();
		
		assertNotNull( gr2 );
		assertEquals( gr.getName(), gr2.getName() );
		
		em.getTransaction().begin();	
		em.remove( gr2 );
		em.getTransaction().commit();
		em.clear();
		
		Application app2;
		
		em.getTransaction().begin();	
		app2 = em.find( Application.class, app.getId() );
		em.getTransaction().commit();
		
		assertNotNull( app2 );
		assertEquals( app.getName(), app2.getName() );
		
		em.getTransaction().begin();	
		em.remove( app2 );
		em.getTransaction().commit();

		em.clear();
	}
	
	@AfterTest(groups = {"Inv"})
	public void roleListTest() throws Exception {
		
		MyPersistenceManager pm = MyPersistenceManager.getInstance();
		EntityManager em = pm.getEntityManager();
		
		List<Role> rolelist;
		
		em.getTransaction().begin();
		Query q = em.createNamedQuery( "RoleFetchByAppnameAndUserId" );
		q.setParameter( "appname", "LoginManagmentSystem" );
		q.setParameter( "userId", 1L );
		rolelist = (List<Role>) q.getResultList();
		em.getTransaction().commit();
		
		//E.rr( rolelist.size() );
		assertTrue( rolelist.size() == 31 );
		assertTrue( rolelist.get(0).getActionList().get(0).getName().equals( "lms admin" ) );
		
		em.getTransaction().begin();
		q = em.createNamedQuery( "UserFetchByLoginname" );
		q.setParameter( "loginname", "test" );
		User u = (User) q.getSingleResult();
		q = em.createNamedQuery( "RoleFetchByAppnameAndUserId" );
		q.setParameter( "appname", "LoginManagmentSystem" );
		q.setParameter( "userId", u.getId() );
		rolelist = (List<Role>) q.getResultList();
		em.getTransaction().commit();

		assertTrue( rolelist.size() == 2 );
		assertTrue( rolelist.get(0).getName().equals("testgroup") );
		assertTrue( rolelist.get(1).getName().equals("testgroup2") );
		assertFalse( rolelist.get(0).getActionList().get(0).getName().equals( "lms admin" ) );
		assertTrue( rolelist.get(0).getActionList().get(0).getName().equals( "testaction" ) );
		assertTrue( rolelist.get(0).getActionList().get(1).getName().equals( "testaction2" ) );
		assertTrue( rolelist.get(1).getActionList().size() == 0 );
	}
}
