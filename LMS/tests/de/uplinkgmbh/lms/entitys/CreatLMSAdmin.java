package de.uplinkgmbh.lms.entitys;
import java.net.URL;
import java.util.Locale;

import javax.persistence.EntityManager;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import de.uplinkgmbh.lms.presistence.MyPersistenceManager;


@Test( groups="admin" )
public class CreatLMSAdmin {

	public void createAdmin() throws Exception {
		
		MyPersistenceManager pm = MyPersistenceManager.getInstance();
		EntityManager em = pm.getEntityManager();
		
		Application app = new Application();
		app.setName( "LoginManagmentSystem" );
		em.getTransaction().begin();	
		em.persist( app );
		em.getTransaction().commit();
		
		Application app2 = new Application();
		app2.setName( "test.system.de" );
		em.getTransaction().begin();	
		em.persist( app2 );
		em.getTransaction().commit();
		
		Groups group = new Groups();
		group.setApplication( app );
		group.setName( "admin" );
		app.getGroupList().add( group );
		em.getTransaction().begin();	
		em.persist( group );
		em.getTransaction().commit();
		
		Role role = new Role();
		role.setName( "admins" );
		role.setSort( 0 );
		role.setApplication( app );
		role.getGroupList().add( group );
		app.getRoleList().add( role );
		em.getTransaction().begin();	
		em.persist( role );
		em.getTransaction().commit();
		
		Role role2 = new Role();
		role2.setName( "test.system.de" );
		role2.setSort( 0 );
		role2.setApplication( app );
		app.getRoleList().add( role2 );
		em.getTransaction().begin();	
		em.persist( role );
		em.getTransaction().commit();
		
		User admin = new User();
		admin.setActiv( true );
		admin.setFirstname( "Admin" );
		admin.setSurename( "Admin" );
		admin.getGroupList().add( group );
		//admin.getRoleList().add( role );
		admin.setLanguage( Locale.GERMAN );
		admin.setLogincounter( 0 );
		admin.setLoginname( "admin" );
		admin.setPassword( "admin" );
		admin.setTemplate( false );
		admin.setCountry( Locale.GERMANY );
		em.getTransaction().begin();	
		em.persist( admin );
		em.getTransaction().commit();
		
		User user = new User();
		user.setActiv( true );
		user.setFirstname( "Alexander" );
		user.setSurename( "Kratzer" );
		user.getRoleList().add( role2 );
		user.setLanguage( Locale.GERMAN );
		user.setLogincounter( 0 );
		user.setLoginname( "a.kratzer" );
		user.setPassword( "passwort" );
		user.setTemplate( false );
		user.setCountry( Locale.GERMANY );
		user.setCity( "Regensburg" );
		user.setEmail( "a.kratzer@uplink-gmbh.de" );
		user.setState( "Bavaria" );
		user.setStreet( "Friedenstraﬂe" );
		user.setStreetnr( "2" );
		user.setZip( "92334" );
		user.setWashstore( "ball wash" );
		em.getTransaction().begin();	
		em.persist( user );
		em.getTransaction().commit();
		
		for( int i = 0; i < 30; i++ ){
			User user1 = new User();
			user1.setLoginname( "testuser_"+i );
			user1.setPassword( "testuserpass_"+i );
			em.getTransaction().begin();	
			em.persist( user1 );
			em.getTransaction().commit();
		}
		
		for( int i = 0; i < 30; i++ ){
			Role r2 = new Role();
			r2.setName( "admins_"+i );
			r2.setSort( i+1 );
			r2.setApplication( app );
			r2.getGroupList().add( group );
			//r2.getUserList().add( admin );
			app.getRoleList().add( r2 );
			em.getTransaction().begin();	
			em.persist( r2 );
			em.getTransaction().commit();
		}
		
		for( int i = 0; i < 10; i++ ){
			Role r2 = new Role();
			r2.setName( "admins_t_"+i );
			r2.setSort( i+1 );
			r2.setApplication( app );
			//r2.getUserList().add( admin );
			app.getRoleList().add( r2 );
			em.getTransaction().begin();	
			em.persist( r2 );
			em.getTransaction().commit();
		}
		
		Organisation orga = new Organisation();
		orga.setName( "LoginManagmentSystem" );
		orga.getUserList().add( admin );
		orga.setCountry( new Locale( "de_DE" ) );
		admin.setOrganisation( orga );
		em.getTransaction().begin();	
		em.persist( orga );
		em.getTransaction().commit();
		
		Organisation orga2 = new Organisation();
		orga2.setName( "Orga 2" );
		orga2.setCountry( new Locale( "de_DE" ) );
		orga2.setState( "Zombiland" );
		orga2.setCity( "Ghost Town" );
		orga2.setFax( "12345" );
		orga2.setPhone( "345678" );
		orga2.setStreet( "Elm Street" );
		orga2.setStreetnr( "666" );
		orga2.setUrl( new URL( "http://www.666.de" ) );
		orga2.setZip( "6666" );
		user.setOrganisation( orga2 );
		em.getTransaction().begin();
		em.persist( orga2 );
		em.getTransaction().commit();
		
		Action action = new Action();
		action.setName( "lms admin" );
		action.setAction( ".*" );
		action.setTarget( ".*" );
		action.setState( ".*" );
		action.setRule( "ACCEPT" );
		action.setSort( 0 );
		action.setRole( role );
		
		Action action2 = new Action();
		action2.setName( "app admin" );
		action2.setAction( "SHOW" );
		action2.setTarget( "Application" );
		action2.setState( "APPADMIN" );
		action2.setRule( "ACCEPT" );
		action2.setSort( 0 );
		action2.setRole( role2 );
		
		Action action3 = new Action();
		action3.setName( "test.system.de admin" );
		action3.setAction( "DOALL" );
		action3.setTarget( "test.system.de" );
		action3.setState( "ADMIN" );
		action3.setRule( "ACCEPT" );
		action3.setSort( 0 );
		action3.setRole( role2 );
		
		em.getTransaction().begin();	
		em.persist( action );
		em.persist( action2 );
		em.persist( action3 );
		em.refresh( role );
		em.merge( user );
		em.getTransaction().commit();
		
		//E.rr( role.getUserList().get( 0 ).getLoginname() );
		
		for( int i = 0; i < 33; i++ ){
			app = new Application();
			app.setName( "LoginManagmentSystem_"+i );
			em.getTransaction().begin();	
			em.persist( app );
			em.getTransaction().commit();
		}
		
	}
}
