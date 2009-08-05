package de.uplinkgmbh.lms.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import de.axone.tools.E;
import de.axone.wash.DefaultWash;
import de.axone.wash.Wash;
import de.axone.wash.Wash.Type;
import de.uplinkgmbh.lms.entitys.Action;
import de.uplinkgmbh.lms.entitys.Application;
import de.uplinkgmbh.lms.entitys.Groups;
import de.uplinkgmbh.lms.entitys.Role;
import de.uplinkgmbh.lms.entitys.User;
import de.uplinkgmbh.lms.presistence.MyPersistenceManager;
import de.uplinkgmbh.lms.utils.LMSToken;
import de.uplinkgmbh.lms.utils.MyWashClient;
import de.uplinkgmbh.lms.utils.Tokenaizer;

@Test( groups="Service" )
public class UserServiceTest {
	
	@BeforeClass
	public static void before() throws Exception {
		
		MyPersistenceManager pm = MyPersistenceManager.getInstance();
		EntityManager em = pm.getEntityManager();
		
		Application app = new Application();
		app.setName( "UserTest" );
		
		em.getTransaction().begin();
		em.persist( app );
		em.getTransaction().commit();
		
		User user = new User();
		user.setActiv( true );
		user.setCity( "Neumarkt i.d.Opf." );
		user.setCountry( new Locale( "de_DE" ) );
		user.setEmail( "heini@gmx.de" );
		user.setFax( "108373487 222" );
		user.setFirstname( "Heinrich" );
		user.setLanguage( new Locale( "de" ) );
		user.setMobile( "0171 74628222" );
		user.setLoginname( "heini@gmx.de" );
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
		user2.setEmail( "maxl@web.de" );
		user2.setFax( "1083 345322" );
		user2.setFirstname( "Maximilian" );
		user2.setLanguage( new Locale( "de" ) );
		user2.setMobile( "0171 74628222" );
		user2.setLoginname( "maxl@web.de" );
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
		group.setName( "UserTestAppAdmins" );
		group.getUserList().add( user );
		app.getGroupList().add( group );
		em.getTransaction().begin();
		em.persist( user );
		em.persist( user2 );
		em.persist( group );
		em.getTransaction().commit();
		
		Action ac = new Action();
		ac.setName( "usertest" );
		ac.setSort( 0 );
		ac.setState( "APPLICATION" );
		ac.setAction( "user.*" );
		ac.setTarget( "UserTest" );
		ac.setRule( "ACCEPT" );
		
		Action ac2 = new Action();
		ac2.setName( "usertest" );
		ac2.setSort( 0 );
		ac2.setState( "APPLICATION" );
		ac2.setAction( "user.xxx*" );
		ac2.setTarget( "UserTest" );
		ac2.setRule( "ACCEPT" );
		
		Role role = new Role();
		role.setName( "AppAdmin" );
		role.setSort( 1 );
		role.setApplication( app );
		role.getGroupList().add( group );
		app.getRoleList().add( role );
		role.getActionList().add( ac );
		role.getActionList().add( ac2 );
		ac.setRole( role );
		ac2.setRole( role );
		
		Role role2 = new Role();
		role2.setName( "Autor" );
		role2.setSort( 1 );
		role2.setApplication( app );
		role2.getUserList().add( user2 );
		app.getRoleList().add( role2 );
		role2.getActionList().add( ac2 );
		ac2.setRole( role2 );
		
		em.getTransaction().begin();
		em.persist( ac );
		em.persist( ac2 );
		em.persist( role );
		em.persist( role2 );
		em.getTransaction().commit();
		
	}

	@Test
	public void services() throws Exception {
		
		//getTemplateUsers
		
		MyWashClient mc = null;
		try {
			mc = new MyWashClient();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		DefaultWash lw = new DefaultWash();
		
		lw.addField( "LOGINNAME", Type.STRING, "heini@gmx.de" );
		lw.addField( "PASSWORD", Type.STRING, "test" );
		lw.addField( "APPLICATION", Type.STRING, "UserTest" );

		Wash res = mc.call( "login", "login", lw );
		
		assertTrue( res.getBoolean( "STATUS" ) );
		assertTrue( res.getString( "REASON" ).equals( "" ) );
		
		String token = res.getString( "LMSTOKEN" );
		
		Wash request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token );
		Wash result = null;
		result = mc.call( "user", "getTemplateUsers", request );
		
		assertNotNull( result );
		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getLong( "SIZE" ) == 6L );
		
		DefaultWash lw2 = new DefaultWash();
		
		lw2.addField( "LOGINNAME", Type.STRING, "maxl@web.de" );
		lw2.addField( "PASSWORD", Type.STRING, "test" );
		lw2.addField( "APPLICATION", Type.STRING, "UserTest" );

		res = mc.call( "login", "login", lw2 );
		assertNotNull( res );
		assertTrue( res.getBoolean( "STATUS" ) );
		assertTrue( res.getString( "REASON" ).equals( "" ) );
		
		String token2 = res.getString( "LMSTOKEN" );
		
		assertFalse( token.equals( token2 ) );
		
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token2 );
		result = null;
		result = mc.call( "user", "getTemplateUsers", request );
		
		assertFalse( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "WRONG PERMISSION" ) );
		
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, "dsfdswdf" );
		result = null;
		result = mc.call( "user", "getTemplateUsers", request );
		
		assertFalse( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "WRONG LMSTOKEN" ) );
		
		
		//getUsers
		
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token );
		result = null;
		result = mc.call( "user", "getUsers", request );

		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		
		assertTrue( result.getLong( "SIZE" ) == 2L );
		
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token2 );
		result = null;
		result = mc.call( "user", "getUsers", request );

		assertFalse( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "WRONG PERMISSION" ) );
		
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, "deswd" );
		result = null;
		result = mc.call( "user", "getUsers", request );

		assertFalse( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "WRONG LMSTOKEN" ) );
		
		// getLoginUser
		
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token );
		result = null;
		result = mc.call( "user", "getLoginUser", request );

		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		assertTrue( result.getString( "LOGINNAME" ).equals( "heini@gmx.de" ) );
		
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, "dsds" );
		result = null;
		result = mc.call( "user", "getLoginUser", request );

		assertFalse( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "WRONG LMSTOKEN" ) );
		
		// newUser

		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token );
		request.addField( "TEMPLATELOGINNAME", Type.STRING, "maxl@web.de" );
		request.addField( "LOGINNAME", Type.STRING, "logo" );
		request.addField( "PASSWORD", Type.STRING, "blaa" );
		request.addField( "FIRSTNAME", Type.STRING, "fname" );
		request.addField( "SURENAME", Type.STRING, "sname" );
		request.addField( "EMAIL", Type.STRING, "test@test.ue" );
		request.addField( "PHONEPRIV", Type.STRING, "992323" );
		request.addField( "PHONEWORK", Type.STRING, "434222" );
		request.addField( "MOBILE", Type.STRING, "01717843483" );
		request.addField( "FAX", Type.STRING, "323442" );
		request.addField( "CITY", Type.STRING, "Berlin" );
		request.addField( "STATE", Type.STRING, "Preissn" );
		request.addField( "ZIP", Type.STRING, "93322" );
		request.addField( "STREET", Type.STRING, "Testweg" );
		request.addField( "STREETNR", Type.STRING, "12c" );
		request.addField( "COUNTRY", Type.STRING, "DE_de" );
		request.addField( "LANGUAGE", Type.STRING, "de" );
		request.addField( "WASHSTORE", Type.STRING, "" );
		request.addField( "ORGANAME", Type.STRING, null );
		result = null;
		result = mc.call( "user", "newUser", request );
		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		
		result = null;
		result = mc.call( "user", "newUser", request );
		assertFalse( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "LOGINNAME USED BY OTHER USER" ) );
		
		request.setString( "LMSTOKEN", "fdf" );
		request.setString( "LOGINNAME", "logo1" );
		result = null;
		result = mc.call( "user", "newUser", request );
		assertFalse( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "WRONG LMSTOKEN" ) );
		
		request.setString( "LMSTOKEN", token );
		request.setString( "TEMPLATELOGINNAME", "dd" );
		request.setString( "LOGINNAME", "logo2" );
		result = null;
		result = mc.call( "user", "newUser", request );
		assertFalse( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "WRONG TEMPLATE" ) );
		
		request.setString( "LMSTOKEN", token );
		request.setString( "TEMPLATELOGINNAME", "" );
		request.setString( "LOGINNAME", "logo3" );
		result = null;
		result = mc.call( "user", "newUser", request );
		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		
		request.setString( "LMSTOKEN", token2 );
		request.setString( "TEMPLATELOGINNAME", "" );
		request.setString( "LOGINNAME", "logo4" );
		result = null;
		result = mc.call( "user", "newUser", request );
		assertFalse( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "WRONG PERMISSION" ) );
		
		request.setString( "LMSTOKEN", token );
		request.setString( "TEMPLATELOGINNAME", "" );
		request.setString( "ORGANAME", "ddd" );
		request.setString( "LOGINNAME", "logo5" );
		result = null;
		result = mc.call( "user", "newUser", request );
		assertFalse( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "WRONG ORGANISATION" ) );
		
		request.setString( "LMSTOKEN", token );
		request.setString( "TEMPLATELOGINNAME", "" );
		request.setString( "ORGANAME", "LoginManagmentSystem" );
		request.setString( "LOGINNAME", "logo6" );
		result = null;
		result = mc.call( "user", "newUser", request );
		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		
		//getUsers
		
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token );
		result = null;
		result = mc.call( "user", "getUsers", request );

		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		assertTrue( result.getLong( "SIZE" ) == 3L );
		
		// logo newUserIn
		Wash lw3 = new DefaultWash();
		lw3.addField( "LOGINNAME", Type.STRING, "logo" );
		lw3.addField( "PASSWORD", Type.STRING, "blaa" );
		lw3.addField( "APPLICATION", Type.STRING, "UserTest" );

		res = mc.call( "login", "login", lw3 );
		
		assertTrue( res.getBoolean( "STATUS" ) );
		assertTrue( res.getString( "REASON" ).equals( "" ) );
		
		String token3 = res.getString( "LMSTOKEN" );
		
		// removeUserFromApplication
		
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, "dsdf" );
		result = null;
		result = mc.call( "user", "removeUserFromApplication", request );

		assertFalse( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "WRONG LMSTOKEN" ) );
		
		MyPersistenceManager pm = MyPersistenceManager.getInstance();
		EntityManager em = pm.getEntityManager();
		
		Application app = new Application();
		app.setName( "UpdateUserTest" );
		
		em.getTransaction().begin();
		em.persist( app );
		em.getTransaction().commit();
		
		LMSToken t0 = Tokenaizer.restoreLMSToken( token3.getBytes() );
		User logo = null;
		em.getTransaction().begin();
		logo = em.find( User.class, t0.userId );
		Role r = new Role();
		r.setName( "updateUser" );
		r.getUserList().add( logo );
		r.setApplication( app );
		em.persist( r );
		em.getTransaction().commit();
		
		
		// remove logo from app
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token3 );
		result = null;
		result = mc.call( "user", "removeUserFromApplication", request );

		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		
		logo = null;
		em.getTransaction().begin();
		logo = em.find( User.class, t0.userId );
		em.getTransaction().commit();
		
		// weil wie oben getan der logo user auch in einer anderen app benutzt wird darf er nicht weg sein
		// aber aus der anwendung verschwinden. Wie eins weiter unten size 2 zeigt.
		assertTrue( logo != null );
		
		//getUsers
		
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token );
		result = null;
		result = mc.call( "user", "getUsers", request );

		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		assertTrue( result.getLong( "SIZE" ) == 2L );
		
		// updateUser
		
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token );
		request.addField( "TEMPLATELOGINNAME", Type.STRING, "maxl@web.de" );
		request.addField( "LOGINNAME", Type.STRING, "logonext" );
		request.addField( "PASSWORD", Type.STRING, "blaa" );
		request.addField( "FIRSTNAME", Type.STRING, "fname" );
		request.addField( "SURENAME", Type.STRING, "sname" );
		request.addField( "EMAIL", Type.STRING, "test@test.ue" );
		request.addField( "PHONEPRIV", Type.STRING, "992323" );
		request.addField( "PHONEWORK", Type.STRING, "434222" );
		request.addField( "MOBILE", Type.STRING, "01717843483" );
		request.addField( "FAX", Type.STRING, "323442" );
		request.addField( "CITY", Type.STRING, "Berlin" );
		request.addField( "STATE", Type.STRING, "Preissn" );
		request.addField( "ZIP", Type.STRING, "93322" );
		request.addField( "STREET", Type.STRING, "Testweg" );
		request.addField( "STREETNR", Type.STRING, "12c" );
		request.addField( "COUNTRY", Type.STRING, "DE_de" );
		request.addField( "LANGUAGE", Type.STRING, "de" );
		request.addField( "WASHSTORE", Type.STRING, "" );
		request.addField( "ORGANAME", Type.STRING, null );
		result = null;
		result = mc.call( "user", "newUser", request );
		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		
		// logo newUserIn
		lw3 = new DefaultWash();
		lw3.addField( "LOGINNAME", Type.STRING, "logonext" );
		lw3.addField( "PASSWORD", Type.STRING, "blaa" );
		lw3.addField( "APPLICATION", Type.STRING, "UserTest" );

		res = mc.call( "login", "login", lw3 );
		
		assertTrue( res.getBoolean( "STATUS" ) );
		assertTrue( res.getString( "REASON" ).equals( "" ) );
		
		token3 = res.getString( "LMSTOKEN" );
		
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token3 );
		request.addField( "LOGINNAME", Type.STRING, "logoUpdate" );
		request.addField( "PASSWORD", Type.STRING, "blaa" );
		request.addField( "FIRSTNAME", Type.STRING, "fname" );
		request.addField( "SURENAME", Type.STRING, "sname" );
		request.addField( "EMAIL", Type.STRING, "test@test.ue" );
		request.addField( "PHONEPRIV", Type.STRING, "992323" );
		request.addField( "PHONEWORK", Type.STRING, "434222" );
		request.addField( "MOBILE", Type.STRING, "01717843483" );
		request.addField( "FAX", Type.STRING, "323442" );
		request.addField( "CITY", Type.STRING, "Berlin" );
		request.addField( "STATE", Type.STRING, "Preissn" );
		request.addField( "ZIP", Type.STRING, "93322" );
		request.addField( "STREET", Type.STRING, "Testweg" );
		request.addField( "STREETNR", Type.STRING, "12c" );
		request.addField( "COUNTRY", Type.STRING, "DE_de" );
		request.addField( "LANGUAGE", Type.STRING, "de" );
		request.addField( "WASHSTORE", Type.STRING, "" );
		request.addField( "ORGANAME", Type.STRING, null );
		result = null;
		result = mc.call( "user", "updateUser", request );

		E.rr( result.serialize() );
		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		
		// logo logout
		lw3 = new DefaultWash();
		lw3.addField( "LMSTOKEN", Type.STRING, token3 );
	
		res = mc.call( "login", "logout", lw3 );
		
		assertTrue( res.getBoolean( "STATUS" ) );

		// logoUpdate login
		lw3 = new DefaultWash();
		lw3.addField( "LOGINNAME", Type.STRING, "logoUpdate" );
		lw3.addField( "PASSWORD", Type.STRING, "blaa" );
		lw3.addField( "APPLICATION", Type.STRING, "UserTest" );

		res = mc.call( "login", "login", lw3 );
		
		assertTrue( res.getBoolean( "STATUS" ) );
		assertTrue( res.getString( "REASON" ).equals( "" ) );
		
		String token4 = res.getString( "LMSTOKEN" );
		
		assertFalse( token3.equals( token4 ) );
		
		LMSToken t1 = Tokenaizer.restoreLMSToken( token3.getBytes() );
		LMSToken t2 = Tokenaizer.restoreLMSToken( token4.getBytes() );
		
		assertTrue( t1.userId == t2.userId );
		
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token );
		result = null;
		result = mc.call( "user", "getUsers", request );

		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		assertTrue( result.getLong( "SIZE" ) == 3L );
		
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token4 );
		result = null;
		result = mc.call( "user", "getLoginUser", request );

		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		assertTrue( result.getString( "LOGINNAME" ).equals( "logoUpdate" ) );
	}
	
}
