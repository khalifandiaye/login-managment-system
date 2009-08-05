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
import de.uplinkgmbh.lms.utils.MyWashClient;

@Test( groups="Service" )
public class OrganisationServiceTest {
	
	@BeforeClass
	public static void before() throws Exception {
		
		MyPersistenceManager pm = MyPersistenceManager.getInstance();
		EntityManager em = pm.getEntityManager();
		
		Application app = new Application();
		app.setName( "OrganisationTest" );
		
		em.getTransaction().begin();
		em.persist( app );
		em.getTransaction().commit();
		
		User user = new User();
		user.setActiv( true );
		user.setCity( "Neumarkt i.d.Opf." );
		user.setCountry( new Locale( "de_DE" ) );
		user.setEmail( "heini2@gmx.de" );
		user.setFax( "108373487 222" );
		user.setFirstname( "Heinrich" );
		user.setLanguage( new Locale( "de" ) );
		user.setMobile( "0171 74628222" );
		user.setLoginname( "heini2@gmx.de" );
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
		user2.setEmail( "maxl2@web.de" );
		user2.setFax( "1083 345322" );
		user2.setFirstname( "Maximilian" );
		user2.setLanguage( new Locale( "de" ) );
		user2.setMobile( "0171 74628222" );
		user2.setLoginname( "maxl2@web.de" );
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
		group.setName( "OrganisationTestAppAdmins" );
		group.getUserList().add( user );
		app.getGroupList().add( group );
		em.getTransaction().begin();
		em.persist( user );
		em.persist( user2 );
		em.persist( group );
		em.getTransaction().commit();
		
		Action ac = new Action();
		ac.setName( "organisationtest" );
		ac.setSort( 0 );
		ac.setState( "APPLICATION" );
		ac.setAction( "organisation.*" );
		ac.setTarget( "OrganisationTest" );
		ac.setRule( "ACCEPT" );
		
		Action ac2 = new Action();
		ac2.setName( "organisationtest" );
		ac2.setSort( 0 );
		ac2.setState( "APPLICATION" );
		ac2.setAction( "organisation.xxx*" );
		ac2.setTarget( "OrganisationTest" );
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
		
		lw.addField( "LOGINNAME", Type.STRING, "heini2@gmx.de" );
		lw.addField( "PASSWORD", Type.STRING, "test" );
		lw.addField( "APPLICATION", Type.STRING, "OrganisationTest" );

		Wash res = mc.call( "login", "login", lw );
		
		assertTrue( res.getBoolean( "STATUS" ) );
		assertTrue( res.getString( "REASON" ).equals( "" ) );
		
		String token = res.getString( "LMSTOKEN" );
		
		DefaultWash lw2 = new DefaultWash();
		
		lw2.addField( "LOGINNAME", Type.STRING, "maxl2@web.de" );
		lw2.addField( "PASSWORD", Type.STRING, "test" );
		lw2.addField( "APPLICATION", Type.STRING, "OrganisationTest" );

		res = mc.call( "login", "login", lw2 );
		assertNotNull( res );
		assertTrue( res.getBoolean( "STATUS" ) );
		assertTrue( res.getString( "REASON" ).equals( "" ) );
		
		String token2 = res.getString( "LMSTOKEN" );
		
		assertFalse( token.equals( token2 ) );
		
		//getOrgas
		
		Wash request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token );
		Wash result = null;
		result = mc.call( "orga", "getOrgas", request );

		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
		
		assertTrue( result.getLong( "SIZE" ) == 2L );

		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token2 );
		result = null;
		result = mc.call( "orga", "getOrgas", request );

		assertFalse( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "WRONG PERMISSION" ) );
		
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, "fggeee" );
		result = null;
		result = mc.call( "orga", "getOrgas", request );

		assertFalse( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "WRONG LMSTOKEN" ) );
		
		// newOrga
		
		/*
		 * LMSTOKEN	string		ORGANAME	string	ORGAURL	string	ORGAPHONE	string	
		 * ORGAFAX	string	ORGACITY	string	ORGASTATE	string	ORGACOUNTRY	string	
		 * ORGAZIP	string	ORGASTREET	string	ORGASTREETNR	string	ORGAWASHSTORE	string
		 */
		request = new DefaultWash();
		request.addField( "LMSTOKEN", Type.STRING, token );
		request.addField( "ORGANAME", Type.STRING, "testorga" );
		request.addField( "ORGAURL", Type.STRING, "url.irgendwas" );
		request.addField( "ORGAPHONE", Type.STRING, "0243453 2222" );
		request.addField( "ORGAFAX", Type.STRING, "04324 23444" );
		request.addField( "ORGACITY", Type.STRING, "testcity" );
		request.addField( "ORGASTATE", Type.STRING, "teststate" );
		request.addField( "ORGACOUNTRY", Type.STRING, "de_DE" );
		request.addField( "ORGAZIP", Type.STRING, "322222" );
		request.addField( "ORGASTREET", Type.STRING, "teststreet" );
		request.addField( "ORGASTREETNR", Type.STRING, "232" );
		request.addField( "ORGAWASHSTORE", Type.STRING, "" );
		result = null;
		result = mc.call( "orga", "newOrga", request );

		assertFalse( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "MALFROMEDURL" ) );
		
		request.setString( "ORGAURL", "http://test.de" );
		request.setString( "LMSTOKEN", "435tereeww" );
		result = null;
		result = mc.call( "orga", "newOrga", request );

		assertFalse( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "WRONG LMSTOKEN" ) );
		
		request.setString( "LMSTOKEN", token2 );
		result = null;
		result = mc.call( "orga", "newOrga", request );

		assertFalse( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "WRONG PERMISSION" ) );
		
		request.setString( "LMSTOKEN", token );
		result = null;
		result = mc.call( "orga", "newOrga", request );

		assertTrue( result.getBoolean( "STATUS" ) );
		assertTrue( result.getString( "REASON" ).equals( "" ) );
	}
	
}
