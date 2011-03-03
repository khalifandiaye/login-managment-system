package de.uplinkgmbh.lms.services;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.axone.wash.DefaultWash;
import de.axone.wash.Wash;
import de.axone.wash.Wash.Type;
import de.uplinkgmbh.lms.entitys.Action;
import de.uplinkgmbh.lms.entitys.Application;
import de.uplinkgmbh.lms.entitys.Groups;
import de.uplinkgmbh.lms.entitys.Role;
import de.uplinkgmbh.lms.entitys.User;
import de.uplinkgmbh.lms.presistence.MyPersistenceManager;
import de.uplinkgmbh.lms.utils.MyWashClient;

@Test( groups="Service" )
public class ActionServiceTest {
	
	@BeforeClass
	public static void before() throws Exception {
		
		MyPersistenceManager pm = MyPersistenceManager.getInstance();
		EntityManager em = pm.getEntityManager();
		
		Application app = new Application();
		app.setName( "ActionTest" );
		
		em.getTransaction().begin();
		em.persist( app );
		em.getTransaction().commit();
		
		User user = new User();
		user.setActiv( true );
		user.setCity( "Neumarkt i.d.Opf." );
		user.setCountry( new Locale( "de_DE" ) );
		user.setEmail( "heini3@gmx.de" );
		user.setFax( "108373487 222" );
		user.setFirstname( "Heinrich" );
		user.setLanguage( new Locale( "de" ) );
		user.setMobile( "0171 74628222" );
		user.setLoginname( "heini3@gmx.de" );
		user.setPassword( "test" );
		user.setPhonepriv( "0933 4346566" );
		user.setPhonework( "0933 4342322" );
		user.setState( "Bavaria" );
		user.setStreet( "Freisinger Gass" );
		user.setStreetnr( "12b" );
		user.setSurename( "Müller" );
		user.setTemplate( true );
		user.setZip( "47722" );
		
		User user2 = new User();
		user2.setActiv( true );
		user2.setCity( "Neumarkt i.d.Opf." );
		user2.setCountry( new Locale( "de_DE" ) );
		user2.setEmail( "maxl3@web.de" );
		user2.setFax( "1083 345322" );
		user2.setFirstname( "Maximilian" );
		user2.setLanguage( new Locale( "de" ) );
		user2.setMobile( "0171 74628222" );
		user2.setLoginname( "maxl3@web.de" );
		user2.setPassword( "test" );
		user2.setPhonepriv( "0933 1111111" );
		user2.setPhonework( "0933 2222222" );
		user2.setState( "Bavaria" );
		user2.setStreet( "HauzuaStrass" );
		user2.setStreetnr( "1" );
		user2.setSurename( "Hinterhofer" );
		user2.setTemplate( true );
		user2.setZip( "76654" );
		
		Groups group = new Groups();
		group.setApplication( app );
		group.setName( "ActionTestAppAdmins" );
		group.getUserList().add( user );
		app.getGroupList().add( group );
		em.getTransaction().begin();
		em.persist( user );
		em.persist( user2 );
		em.persist( group );
		em.getTransaction().commit();
		
		Action ac = new Action();
		ac.setName( "actiontest" );
		ac.setSort( 0 );
		ac.setState( "APPLICATION" );
		ac.setAction( "action.*" );
		ac.setTarget( "ActionTest" );
		ac.setRule( "ACCEPT" );
		
		Action ac1 = new Action();
		ac1.setName( "actiontest1" );
		ac1.setSort( 1 );
		ac1.setState( "test" );
		ac1.setAction( "test" );
		ac1.setTarget( "ActionTest" );
		ac1.setRule( "DENY" );
		
		Action ac2 = new Action();
		ac2.setName( "actiontest" );
		ac2.setSort( 0 );
		ac2.setState( "APPLICATION" );
		ac2.setAction( "action.xxx*" );
		ac2.setTarget( "ActionTest" );
		ac2.setRule( "ACCEPT" );
		
		Role role = new Role();
		role.setName( "AppAdmin" );
		role.setSort( 1 );
		role.setApplication( app );
		role.getGroupList().add( group );
		app.getRoleList().add( role );
		role.getActionList().add( ac );
		role.getActionList().add( ac1 );
		role.getActionList().add( ac2 );
		ac.setRole( role );
		ac1.setRole( role );
		ac2.setRole( role );
		
		Role role2 = new Role();
		role2.setName( "Autor" );
		role2.setSort( 1 );
		role2.setApplication( app );
		role2.getUserList().add( user2 );
		app.getRoleList().add( role2 );
		role2.getActionList().add( ac2 );
		ac2.setRole( role2 );
		
		Action ac10 = new Action();
		ac10.setName( "ac10" );
		ac10.setSort( 1 );
		ac10.setState( "Autor" );
		ac10.setAction( "create" );
		ac10.setTarget( "Artikel" );
		ac10.setRule( "ACCEPT" );
		
		Action ac11 = new Action();
		ac11.setName( "ac11" );
		ac11.setSort( 2 );
		ac11.setState( "Lektor" );
		ac11.setAction( "create" );
		ac11.setTarget( "Artikel" );
		ac11.setRule( "ACCEPT" );
		
		Action ac12 = new Action();
		ac12.setName( "ac12" );
		ac12.setSort( 3 );
		ac12.setState( "Autor" );
		ac12.setAction( "delete" );
		ac12.setTarget( "Artikel" );
		ac12.setRule( "ACCEPT" );
		
		Action ac13 = new Action();
		ac13.setName( "ac13" );
		ac13.setSort( 4 );
		ac13.setState( "Publisher" );
		ac13.setAction( "create" );
		ac13.setTarget( "Artikel" );
		ac13.setRule( "ACCEPT" );
		
		Action ac14 = new Action();
		ac14.setName( "ac14" );
		ac14.setSort( 5 );
		ac14.setState( "Autor" );
		ac14.setAction( "change" );
		ac14.setTarget( "Artikel" );
		ac14.setRule( "ACCEPT" );
		
		Action ac15 = new Action();
		ac15.setName( "ac15" );
		ac15.setSort( 6 );
		ac15.setState( "Autor" );
		ac15.setAction( "change" );
		ac15.setTarget( "Page" );
		ac15.setRule( "ACCEPT" );
		
		Role role3 = new Role();
		role3.setName( "getWildcardActions_Test" );
		role3.setSort( 2 );
		role3.setApplication( app );
		role3.getUserList().add( user2 );
		app.getRoleList().add( role3 );
		role3.getActionList().add( ac10 );
		role3.getActionList().add( ac11 );
		role3.getActionList().add( ac12 );
		role3.getActionList().add( ac13 );
		role3.getActionList().add( ac14 );
		role3.getActionList().add( ac15 );
		ac10.setRole( role3 );
		ac11.setRole( role3 );
		ac12.setRole( role3 );
		ac13.setRole( role3 );
		ac14.setRole( role3 );
		ac15.setRole( role3 );
		
		em.getTransaction().begin();
		em.persist( ac );
		em.persist( ac1 );
		em.persist( ac2 );
		em.persist( ac10 );
		em.persist( ac11 );
		em.persist( ac12 );
		em.persist( ac13 );
		em.persist( ac14 );
		em.persist( role );
		em.persist( role2 );
		em.persist( role3 );
		em.getTransaction().commit();
		
	}

	@Test
	public void services() throws Exception {
		
		//getActions
		
		MyWashClient mc = null;
		try {
			mc = new MyWashClient();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		DefaultWash lw = new DefaultWash();
		
		lw.addField( "LOGINNAME", Type.STRING, "heini3@gmx.de" );
		lw.addField( "PASSWORD", Type.STRING, "test" );
		lw.addField( "APPLICATION", Type.STRING, "ActionTest" );

		Wash res = mc.call( "login", "login", lw );
		
		assertTrue( res.getBoolean( "STATUS" ) );
		assertTrue( res.getString( "REASON" ).equals( "" ) );
		
		String token = res.getString( "LMSTOKEN" );
		
		DefaultWash lw2 = new DefaultWash();
		
		lw2.addField( "LOGINNAME", Type.STRING, "maxl3@web.de" );
		lw2.addField( "PASSWORD", Type.STRING, "test" );
		lw2.addField( "APPLICATION", Type.STRING, "ActionTest" );

		res = mc.call( "login", "login", lw2 );
		assertNotNull( res );
		assertTrue( res.getBoolean( "STATUS" ) );
		assertTrue( res.getString( "REASON" ).equals( "" ) );
		
		String token2 = res.getString( "LMSTOKEN" );
		
		assertFalse( token.equals( token2 ) );
		
		Wash request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token );
		Wash result = null;
		result = mc.call( "action", "getActions", request );

		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		
		assertTrue( result.getLong( "SIZE" ) == 2L );
		
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, "dsds" );
		result = null;
		result = mc.call( "action", "getActions", request );

		assertFalse( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "WRONG LMSTOKEN" ) );
		
		// isAllowed
		
		/*
		 * ac.setState( "APPLICATION" );
			ac.setAction( "action.*" );
			ac.setTarget( "ActionTest" );
		 */
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token );
		request.addField( "STATE", Type.STRING, "APPLICATION" );
		request.addField( "ACTION", Type.STRING, "action.blaa" );
		request.addField( "TARGET", Type.STRING, "ActionTest" );
		result = null;
		result = mc.call( "action", "isAllowed", request );

		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		assertTrue( result.getBoolean( "PERMISSION" ) );
		
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token );
		request.addField( "STATE", Type.STRING, "APPLICATION" );
		request.addField( "ACTION", Type.STRING, "nix" );
		request.addField( "TARGET", Type.STRING, "ActionTest" );
		result = null;
		result = mc.call( "action", "isAllowed", request );

		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		assertFalse( result.getBoolean( "PERMISSION" ) );
		
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token );
		request.addField( "STATE", Type.STRING, "nix" );
		request.addField( "ACTION", Type.STRING, "action.blaa" );
		request.addField( "TARGET", Type.STRING, "ActionTest" );
		result = null;
		result = mc.call( "action", "isAllowed", request );

		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		assertFalse( result.getBoolean( "PERMISSION" ) );
		
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token );
		request.addField( "STATE", Type.STRING, "APPLICATION" );
		request.addField( "ACTION", Type.STRING, "action.blaa" );
		request.addField( "TARGET", Type.STRING, "nix" );
		result = null;
		result = mc.call( "action", "isAllowed", request );

		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		assertFalse( result.getBoolean( "PERMISSION" ) );
		
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token );
		request.addField( "STATE", Type.STRING, "nix" );
		request.addField( "ACTION", Type.STRING, "nix" );
		request.addField( "TARGET", Type.STRING, "nix" );
		result = null;
		result = mc.call( "action", "isAllowed", request );

		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		assertFalse( result.getBoolean( "PERMISSION" ) );
		
		
		// getWildcardActions test
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token2 );
		request.addField( "STATE", Type.STRING, null );
		request.addField( "ACTION", Type.STRING, "create" );
		request.addField( "TARGET", Type.STRING, "Artikel" );
		result = null;
		result = mc.call( "action", "getWildcardActions", request );

		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		assertTrue( result.getLong( "SIZE" ) == 3 );
		assertTrue( result.getString( "Action-0.NAME" ).equals( "ac10" ) );
		assertTrue( result.getString( "Action-1.NAME" ).equals( "ac11" ) );
		assertTrue( result.getString( "Action-2.NAME" ).equals( "ac13" ) );

		// 2
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token2 );
		request.addField( "STATE", Type.STRING, "Autor" );
		request.addField( "ACTION", Type.STRING, null );
		request.addField( "TARGET", Type.STRING, "Artikel" );
		result = null;
		result = mc.call( "action", "getWildcardActions", request );

		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		assertTrue( result.getLong( "SIZE" ) == 3 );
		assertTrue( result.getString( "Action-0.NAME" ).equals( "ac10" ) );
		assertTrue( result.getString( "Action-1.NAME" ).equals( "ac12" ) );
		assertTrue( result.getString( "Action-2.NAME" ).equals( "ac14" ) );
		
		MyPersistenceManager pm = MyPersistenceManager.getInstance();
		EntityManager em = pm.getEntityManager();
		em.getTransaction().begin();
		Query query = em.createNamedQuery( "ActionFetchByName" );
		query.setParameter( "name", "ac12" );
		Action a = (Action) query.getSingleResult();
		a.setRule( "DENY" );
		em.merge( a );
		em.getTransaction().commit();
		
		// 3
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token2 );
		request.addField( "STATE", Type.STRING, "Autor" );
		request.addField( "ACTION", Type.STRING, null );
		request.addField( "TARGET", Type.STRING, "Artikel" );
		result = null;
		result = mc.call( "action", "getWildcardActions", request );

		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		assertTrue( result.getLong( "SIZE" ) == 1 );
		assertTrue( result.getString( "Action-0.NAME" ).equals( "ac10" ) );
		
		em.getTransaction().begin();
		query = em.createNamedQuery( "ActionFetchByName" );
		query.setParameter( "name", "ac13" );
		a = (Action) query.getSingleResult();
		a.setRule( "DENY" );
		em.merge( a );
		em.getTransaction().commit();
		
		// 4
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token2 );
		request.addField( "STATE", Type.STRING, null );
		request.addField( "ACTION", Type.STRING, "create" );
		request.addField( "TARGET", Type.STRING, "Artikel" );
		result = null;
		result = mc.call( "action", "getWildcardActions", request );

		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		assertTrue( result.getLong( "SIZE" ) == 2 );
		assertTrue( result.getString( "Action-0.NAME" ).equals( "ac10" ) );
		assertTrue( result.getString( "Action-1.NAME" ).equals( "ac11" ) );
		
		em.clear();
		em.close();
	}
	
}
