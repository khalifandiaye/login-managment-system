package de.uplinkgmbh.lms.client;

import java.net.URL;

import de.axone.tools.E;
import de.axone.tools.Str;

public class ClientTest {

	public static void main( String [] args ) throws Exception {
		
		LmsClient client = new LmsClient( new URL( "http://www.cargreen.de/LMS/WashServices" ), "hvm" );
		
		LmsToken token = client.login( "pickl@honda-pickl.de", "coupe" );
		
		E.rr( Str.splitAt( 40, token.toString() ) );
		
		E.rr( client.isAllowed( token, "new", "create", "angebot" ) );
		E.rr( client.isAllowed( token, "x", "create", "angebot" ) );
		E.rr( client.isAllowed( token, "x", "y", "z" ) );
		
		E.rr( client.isAllowed( token, "new", "create", "verkaeufer" ) );
		E.rr( client.isAllowed( token, "x", "create", "verkaeufer" ) );
		E.rr( client.isAllowed( token, "x", "y", "verkaeufer" ) );
		
		E.rr( client.isAllowed( token, null, "create", "verkaeufer" ) );
		E.rr( client.isAllowed( token, null, null, "verkaeufer" ) );
		E.rr( client.isAllowed( token, null, null, null ) );
		
		client.logout( token );
		
		E.rr( "ok" );
		
		//E.rr( Str.splitAt( 40, Tokenaizer.buildAESToken( "test", 123 ) ) );
		
	}
}
