package de.uplinkgmbh.lms.user;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import de.uplinkgmbh.lms.exceptions.LoginException;
import de.uplinkgmbh.lms.webtemplate.Context;

@Test( groups="lms.login" )
public class LoginTest {

	@AfterTest(groups = {"Inv"})
	public void login() throws Exception {
		
		Login login = new Login();
		try{
			login.check( "admin", "passwd", Context.getSingelton().getApplicationname() );
		}catch( LoginException e ){
			assertTrue( LoginException.WRONGPASSWORD == e.status );
			assertFalse( LoginException.USERHASNOGROUPORROLE == e.status );
			assertFalse( LoginException.WRONGAPPLICATION == e.status );
			assertFalse( LoginException.WRONGUSERNAME == e.status );
			assertFalse( login.isAllowed() );
		}
		
		try{
			login.check( "admin_", "passwd", Context.getSingelton().getApplicationname() );
		}catch( LoginException e ){
			assertFalse( LoginException.WRONGPASSWORD == e.status );
			assertFalse( LoginException.USERHASNOGROUPORROLE == e.status );
			assertFalse( LoginException.WRONGAPPLICATION == e.status );
			assertTrue( LoginException.WRONGUSERNAME == e.status );
			assertFalse( login.isAllowed() );
		}
		
		try{
			login.check( "admin", "passwd", Context.getSingelton().getApplicationname()+"_" );
		}catch( LoginException e ){
			assertFalse( LoginException.WRONGPASSWORD == e.status );
			assertFalse( LoginException.USERHASNOGROUPORROLE == e.status );
			assertTrue( LoginException.WRONGAPPLICATION == e.status );
			assertFalse( LoginException.WRONGUSERNAME == e.status );
			assertFalse( login.isAllowed() );
		}
		
		try{
			login.check( "admin", "passwdf", Context.getSingelton().getApplicationname() );
		}catch( LoginException e ){
			assertFalse( LoginException.WRONGPASSWORD == e.status );
			assertFalse( LoginException.USERHASNOGROUPORROLE == e.status );
			assertFalse( LoginException.WRONGAPPLICATION == e.status );
			assertFalse( LoginException.WRONGUSERNAME == e.status );
		}
		assertTrue( login.isAllowed() );
		
		assertNotNull( login.logIn() );
		long counter = login.getUser().getLogincounter();
		login.logIn();
		assertTrue( counter == login.getUser().getLogincounter()-1 );
		login.logOut();
	
	}
}
