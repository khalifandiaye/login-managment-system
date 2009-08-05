import java.util.Locale;

import javax.persistence.EntityManager;

import org.testng.annotations.Test;

import de.uplinkgmbh.lms.entitys.Action;
import de.uplinkgmbh.lms.entitys.Application;
import de.uplinkgmbh.lms.entitys.Groups;
import de.uplinkgmbh.lms.entitys.Role;
import de.uplinkgmbh.lms.entitys.User;
import de.uplinkgmbh.lms.presistence.MyPersistenceManager;


@Test( groups="sysAdmin" )
public class CreateSysAdmin {

	public void createAdmin() throws Exception {
		
		MyPersistenceManager pm = MyPersistenceManager.getInstance();
		EntityManager em = pm.getEntityManager();
		
		Application app = new Application();
		app.setName( "LoginManagmentSystem" );
		em.getTransaction().begin();	
		em.persist( app );
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
		
		Action action = new Action();
		action.setName( "lms admin" );
		action.setAction( ".*" );
		action.setTarget( ".*" );
		action.setState( ".*" );
		action.setRule( "ACCEPT" );
		action.setSort( 0 );
		action.setRole( role );
		
		em.getTransaction().begin();	
		em.persist( action );
		em.getTransaction().commit();
	}
}
