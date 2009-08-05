package de.uplinkgmbh.lms.entitys;

import javax.persistence.EntityManager;

import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import de.uplinkgmbh.lms.presistence.MyPersistenceManager;

@Test( groups="Del" )
public class DeleteTests {

	@AfterTest(groups = {"Service"})
	public void delete() throws Exception {
		
		MyPersistenceManager pm = MyPersistenceManager.getInstance();
		EntityManager em = pm.getEntityManager();
		
		Organisation orga = null;
		em.getTransaction().begin();	
		orga = em.find( Organisation.class, 1L );
		em.remove( orga );
		em.getTransaction().commit();
	}
}
