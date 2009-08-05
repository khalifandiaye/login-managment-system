package de.uplinkgmbh.lms.entitys;

import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import de.uplinkgmbh.lms.presistence.MyPersistenceManager;

@Test( groups="Inv" )
public class CreateTestInv {

	//@AfterGroups(groups = {"admin"})
	public void createInv() throws Exception {
		
		MyPersistenceManager pm = MyPersistenceManager.getInstance();
		EntityManager em = pm.getEntityManager();
		
		Application app;
		
		em.getTransaction().begin();
		Query q = em.createNamedQuery( "ApplicationFetchByName" );
		q.setParameter( "name", "LoginManagmentSystem" );
		app = (Application) q.getSingleResult();
		em.getTransaction().commit();
	
		Groups group = new Groups();
		group.setApplication( app );
		group.setName( "testInv" );
		app.getGroupList().add( group );
		em.getTransaction().begin();	
		em.persist( group );
		em.getTransaction().commit();
		
		Role role = new Role();
		role.setName( "testgroup" );
		role.setSort( 1 );
		role.setApplication( app );
		role.getGroupList().add( group );
		app.getRoleList().add( role );
		em.getTransaction().begin();	
		em.persist( role );
		em.getTransaction().commit();
		
		Role role2 = new Role();
		role2.setName( "testgroup2" );
		role2.setSort( 3 );
		role2.setApplication( app );
		role2.getGroupList().add( group );
		app.getRoleList().add( role2 );
		em.getTransaction().begin();	
		em.persist( role2 );
		em.getTransaction().commit();
		
		User admin = new User();
		admin.setActiv( true );
		admin.setFirstname( "testuser" );
		admin.setSurename( "Testuser" );
		admin.getGroupList().add( group );
		admin.getRoleList().add( role );
		admin.getRoleList().add( role2 );
		admin.setLanguage( Locale.GERMAN );
		admin.setLogincounter( 0 );
		admin.setLoginname( "test" );
		admin.setPassword( "test" );
		admin.setTemplate( false );
		admin.setCountry( Locale.GERMANY );
		em.getTransaction().begin();	
		em.persist( admin );
		em.getTransaction().commit();
		
		Organisation orga;
		
		em.getTransaction().begin();	
		q = em.createNamedQuery( "OrgaFetchByName" );
		q.setParameter( "name", "LoginManagmentSystem" );
		orga = (Organisation) q.getSingleResult();
		em.getTransaction().commit();
		admin.setOrganisation( orga );
		
		Action action = new Action();
		action.setName( "testaction" );
		action.setAction( "DOTEST" );
		action.setTarget( "Site.News.*" );
		action.setState( "NEXT" );
		action.setRule( "ACCEPT" );
		action.setSort( 1 );
		action.setRole( role );
		
		Action action2 = new Action();
		action2.setName( "testaction2" );
		action2.setAction( "DOTEST2" );
		action2.setTarget( "Site.*" );
		action2.setState( "EDITOR" );
		action2.setRule( "ACCEPT" );
		action2.setSort( 2 );
		action2.setRole( role );
		
		em.getTransaction().begin();	
		em.persist( action );
		em.persist( action2 );
		em.refresh( role );
		em.getTransaction().commit();
	}
}
