package de.uplinkgmbh.lms.user;

import static org.junit.Assert.*;

import org.testng.annotations.Test;

import de.uplinkgmbh.lms.exceptions.LoginException;
import de.uplinkgmbh.lms.webtemplate.Context;

@Test( groups="lms.authorisation" )
public class AuthorisationsCheckerTests {

	public void test() throws Exception {
		
		Login login = new Login();
		try{
			login.check( "admin", "admin", Context.getSingelton().getApplicationname() );
		}catch( LoginException e ){
			assertFalse( LoginException.WRONGPASSWORD == e.status );
			assertFalse( LoginException.USERHASNOGROUPORROLE == e.status );
			assertFalse( LoginException.WRONGAPPLICATION == e.status );
			assertFalse( LoginException.WRONGUSERNAME == e.status );
		}
		
		String AESToken = login.logIn();
		
		Login login2 = new Login();
		try{
			login2.check( "test", "test", Context.getSingelton().getApplicationname() );
		}catch( LoginException e ){
			assertFalse( LoginException.WRONGPASSWORD == e.status );
			assertFalse( LoginException.USERHASNOGROUPORROLE == e.status );
			assertFalse( LoginException.WRONGAPPLICATION == e.status );
			assertFalse( LoginException.WRONGUSERNAME == e.status );
		}
		
		String AESToken2 = login2.logIn();
		//long time = System.nanoTime();
		assertTrue( AuthorizationsChecker.isAllowed( AESToken, "ADMIN", "DOALL", "Site.News.1" ) );
		assertTrue( AuthorizationsChecker.isAllowed( AESToken, "ADMIN", "DO1", "Site.News.1" ) );
		assertTrue( AuthorizationsChecker.isAllowed( AESToken, "GOOD", "DOALL", "Site.News.1" ) );
		
		assertFalse( AuthorizationsChecker.isAllowed( AESToken2, "ADMIN", "DOALL", "Site.News.1" ) );
		assertTrue( AuthorizationsChecker.isAllowed( AESToken2, "NEXT", "DOTEST", "Site.News.1" ) );
		assertFalse( AuthorizationsChecker.isAllowed( AESToken2, "NEXT", "DOTEST", "Site.1" ) );
		assertTrue( AuthorizationsChecker.isAllowed( AESToken2, "EDITOR", "DOTEST2", "Site.1" ) );
		//E.rr( "Time: "+ ((System.nanoTime()-time)/1000000.0) +" ms" );
		
	}
}
