package de.uplinkgmbh.lms.user;

import static org.junit.Assert.*;

import org.testng.annotations.Test;

import de.axone.tools.E;
import de.uplinkgmbh.lms.exceptions.LoginException;
import de.uplinkgmbh.lms.webtemplate.Context;

@Test( groups="lms.login" )
public class LoginTest {

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
			login.check( "admin_", "admin", Context.getSingelton().getApplicationname() );
		}catch( LoginException e ){
			assertFalse( LoginException.WRONGPASSWORD == e.status );
			assertFalse( LoginException.USERHASNOGROUPORROLE == e.status );
			assertFalse( LoginException.WRONGAPPLICATION == e.status );
			assertTrue( LoginException.WRONGUSERNAME == e.status );
			assertFalse( login.isAllowed() );
		}
		
		try{
			login.check( "admin", "admin", Context.getSingelton().getApplicationname()+"_" );
		}catch( LoginException e ){
			E.rr( e.status );
			assertFalse( LoginException.WRONGPASSWORD == e.status );
			assertFalse( LoginException.USERHASNOGROUPORROLE == e.status );
			assertTrue( LoginException.WRONGAPPLICATION == e.status );
			assertFalse( LoginException.WRONGUSERNAME == e.status );
			assertFalse( login.isAllowed() );
		}
		
		try{
			login.check( "admin", "admin", Context.getSingelton().getApplicationname() );
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
