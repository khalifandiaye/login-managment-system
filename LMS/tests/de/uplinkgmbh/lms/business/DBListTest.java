package de.uplinkgmbh.lms.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

import de.axone.tools.E;
import de.uplinkgmbh.lms.entitys.Application;
import de.uplinkgmbh.lms.presistence.MyPersistenceManager;

@Test( groups="lms.business.DBList" )
public class DBListTest {

	@AfterSuite(groups = {"Inv"})
	public void listTest() throws Exception {
		
		DBList dbl = new DBList();
		
		String sql = "SELECT app " +
		"FROM Application app ";
		
		String sqlCount = "SELECT count( app ) " +
		"FROM Application app ";
		
		dbl.setQuery( sql, sqlCount );
		
		dbl.execute();
		
		assertNotNull( dbl.getResultList() );
		assertEquals( 39, dbl.getResultList().size() );
		assertTrue( dbl.getMaxCount() == 39 );
		
		Application app = new Application();
		app.setName( "www.test.de" );
		Application app2 = new Application();
		app2.setName( "www.test2.de" );
		Application app3 = new Application();
		app3.setName( "www.test3.de" );
		
		MyPersistenceManager pm = MyPersistenceManager.getInstance();
		EntityManager em = pm.getEntityManager();

		em.getTransaction().begin();	
		em.persist( app );
		em.persist( app2 );
		em.persist( app3 );
		em.getTransaction().commit();
		
		dbl.getQuery().setMaxResults( 2 );
		dbl.execute();
		
		assertNotNull( dbl.getResultList() );
		assertEquals( 2, dbl.getResultList().size() );
		//E.rr( dbl.getMaxCount() );
		assertTrue( dbl.getMaxCount() == 42 );
		
		DBList dbl2 = new DBList();
		dbl2.setNamedQuery( "AllApplication", "AllApplicationCount" );
		dbl2.getQuery().setMaxResults( 2 );
		dbl2.execute();
		
		assertNotNull( dbl2.getResultList() );
		assertEquals( 2, dbl2.getResultList().size() );
		//E.rr( dbl2.getMaxCount() );
		assertTrue( dbl2.getMaxCount() == 42 );
				
		em.getTransaction().begin();	
		em.remove( app );
		em.remove( app2 );
		em.remove( app3 );
		em.getTransaction().commit();
		em.clear();
		
		/*
		for( Object obj : dbl.getResultList() ){
			E.rr( "dbl "+((Application)obj).getName() );
		}
		for( Object obj : dbl2.getResultList() ){
			E.rr( "dbl2 "+((Application)obj).getName() );
		}
		*/
	}
}
