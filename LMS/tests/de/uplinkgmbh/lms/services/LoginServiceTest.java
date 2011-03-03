package de.uplinkgmbh.lms.services;

import static org.junit.Assert.*;

import java.net.MalformedURLException;

import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import de.axone.wash.DefaultWash;
import de.axone.wash.Wash;
import de.axone.wash.Wash.Type;
import de.uplinkgmbh.lms.utils.MyWashClient;

@Test( groups="Service" )
public class LoginServiceTest {

	@AfterTest(groups = {"lms.*"})
	public void login() throws Exception {
		
		MyWashClient mc = null;
		try {
			mc = new MyWashClient();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		DefaultWash lw = new DefaultWash();
		
		lw.addField( "LOGINNAME", Type.STRING, "admin" );
		lw.addField( "PASSWORD", Type.STRING, "admin" );
		lw.addField( "APPLICATION", Type.STRING, "LoginManagmentSystem" );

		Wash res = mc.call( "login", "login", lw );
		assertNotNull( res );
		assertTrue( res.getBoolean( "STATUS" ) );
		assertTrue( res.getString( "REASON" ).equals( "" ) );
		
		String token = res.getString( "LMSTOKEN" );
		
		res = mc.call( "login", "login", lw );
		assertNotNull( res );
		assertTrue( res.getBoolean( "STATUS" ) );
		assertFalse( res.getString( "REASON" ).equals( token ) );
		
		DefaultWash lw2 = new DefaultWash();
		lw2.addField( "LOGINNAME", Type.STRING, "nix" );
		lw2.addField( "PASSWORD", Type.STRING, "tzzwhJm3" );
		lw2.addField( "APPLICATION", Type.STRING, "LoginManagmentSystem" );

		res = mc.call( "login", "login", lw2 );
		assertNotNull( res );
		assertFalse( res.getBoolean( "STATUS" ) );
		assertTrue( res.getString( "REASON" ).equals( "USER NOT EXIST" ) );
		
		DefaultWash lw3 = new DefaultWash();
		lw3.addField( "LOGINNAME", Type.STRING, "admin" );
		lw3.addField( "PASSWORD", Type.STRING, "nix" );
		lw3.addField( "APPLICATION", Type.STRING, "LoginManagmentSystem" );

		res = mc.call( "login", "login", lw3 );
		assertNotNull( res );
		assertFalse( res.getBoolean( "STATUS" ) );
		assertTrue( res.getString( "REASON" ).equals( "WRONG PASSWORD" ) );
		
		DefaultWash lw4 = new DefaultWash();
		lw4.addField( "LOGINNAME", Type.STRING, "admin" );
		lw4.addField( "PASSWORD", Type.STRING, "admin" );
		lw4.addField( "APPLICATION", Type.STRING, "nix" );

		res = mc.call( "login", "login", lw4 );
		assertNotNull( res );
		assertFalse( res.getBoolean( "STATUS" ) );
		assertTrue( res.getString( "REASON" ).equals( "NO ACTIV USER FOR APPLICATION" ) );
		
		DefaultWash lw5 = new DefaultWash();
		lw5.addField( "LOGINNAME", Type.STRING, "" );
		lw5.addField( "PASSWORD", Type.STRING, "tzzwhJm3" );
		lw5.addField( "APPLICATION", Type.STRING, "nix" );

		res = mc.call( "login", "login", lw5 );
		assertNotNull( res );
		assertTrue( res.getString( "ERROR" ).equals( "EMPTY PARAMETERS" ) );
	}
	
	@AfterTest(groups = {"lms.*"})
	public void logout() throws Exception {
		
		MyWashClient mc = null;
		try {
			mc = new MyWashClient();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		DefaultWash lw = new DefaultWash();
		
		lw.addField( "LOGINNAME", Type.STRING, "admin" );
		lw.addField( "PASSWORD", Type.STRING, "admin" );
		lw.addField( "APPLICATION", Type.STRING, "LoginManagmentSystem" );

		Wash res = mc.call( "login", "login", lw );
		assertNotNull( res );
		assertTrue( res.getBoolean( "STATUS" ) );
		assertTrue( res.getString( "REASON" ).equals( "" ) );
		
		String token = res.getString( "LMSTOKEN" );
		
		DefaultWash lw1 = new DefaultWash();
		lw1.addField( "LMSTOKEN", Type.STRING, "gdgfd" );
		
		res = mc.call( "login", "logout", lw1 );
		assertNotNull( res );
		assertFalse( res.getBoolean( "STATUS" ) );
		assertTrue( res.getString( "REASON" ).equals( "WRONG LMSTOKEN" ) );
		
		DefaultWash lw2 = new DefaultWash();
		lw2.addField( "LMSTOKEN", Type.STRING, token );
		
		res = mc.call( "login", "logout", lw2 );
		assertNotNull( res );
		assertTrue( res.getBoolean( "STATUS" ) );
		assertFalse( res.getString( "REASON" ).equals( "" ) );
	}
}
