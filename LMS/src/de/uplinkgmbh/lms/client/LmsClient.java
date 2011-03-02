package de.uplinkgmbh.lms.client;

import java.net.URL;

import de.axone.logging.Log;
import de.axone.logging.Logging;
import de.axone.wash.Wash;
import de.axone.wash.Wash.NotFoundException;
import de.axone.wash.Wash.WashException;
import de.axone.wash.Wash.WrongTypeException;
import de.axone.wash.client.WashClient;
import de.axone.wash.client.WashClient.ClientException;

public class LmsClient {
	
	private static final Log log = Logging.getLog( LmsClient.class );
	
	private final WashClient wc;
	private final String application;

	public LmsClient( URL url, String application ){
		
		if( url == null ) throw new IllegalArgumentException( "'url' is null" );
		if( application == null ) throw new IllegalArgumentException( "'application' is null" );
		
		this.wc = new WashClient( url );
		this.application = application;
	}
	
	public LmsToken login( String loginname, String passwd ) throws LmsException {
		
		if( loginname == null ) throw new IllegalArgumentException( "'loginname' is null" );
		if( passwd == null ) throw new IllegalArgumentException( "'passwd' is null" );
		
		LoginWash lw = new LoginWash( loginname, passwd, application );
		
		Wash res = call( "login", "login", lw );
		checkStatus( res, "Cannot log in" );
		
		String token = getString( res, "LMSTOKEN" );
		
		log.info( "Logged in: " + loginname );
		
		return new LmsToken( token );
	}
	
	public void logout( LmsToken token ) throws LmsException {
		
		if( token == null ) throw new IllegalArgumentException( "'token' is null" );
		
		LogoutWash lw = new LogoutWash( token );
		
		Wash res = call( "login", "logout", lw );
		checkStatus( res, "Cannot log out" );
		
		long time = Long.parseLong( getString( res, "REASON" ) );
		
		log.info( "Log out after " + time/1000 + " s" );
	}
	
	public boolean isAllowed( LmsToken token,
			String state, String action, String target ) throws LmsException {
		
		if( token == null ) throw new IllegalArgumentException( "'token' is null" );
		if( state == null ) state = "";
		if( action == null ) action = "";
		if( target == null ) target = "";

		IsAllowedWash lw = new IsAllowedWash( token, state, action, target );

		Wash res = null;

		res = call( "action", "isAllowed", lw );
		checkStatus( res, "Wrong token" );
		
		return getBoolean( res, "PERMISSION" );
	}
	
	// --- PRIVATE ---
	
	private Wash call( String service, String op, Wash wash ) throws LmsException {
		
		try {
			return wc.call( service, op, wash );
		} catch (ClientException e) {
			throw new LmsException( "Cannot connect to LMS via " + wc.getUrl(), e );
		} catch (WashException e) {
			throw new LmsWashException( "Error in wash", wash, e );
		}
	}
	
	private void checkStatus( Wash res, String message ) throws LmsException {
		
		if( ! getBoolean( res, "STATUS" ) ){
			throw new LmsWashException( message, res );
		}
	}
	
	private String getString( Wash res, String name ) throws LmsException {
		
		try {
			return res.getString( name );
		// Generic Server/Wash error
		} catch( NotFoundException e ) {
			throw new LmsWashException( "Result is incomplete (missing " + name + ")", res, e );
		} catch( WrongTypeException e ) {
			throw new LmsWashException( "Some error in result", res, e );
		}
	}
	
	private boolean getBoolean( Wash res, String name ) throws LmsException {
		
		try {
			return res.getBoolean( name );
		// Generic Server/Wash error
		} catch( NotFoundException e ) {
			throw new LmsWashException( "Result is incomplete (missing " + name + ")", res, e );
		} catch( WrongTypeException e ) {
			throw new LmsWashException( "Some error in result", res, e );
		}
	}
	
	public static class LmsLoginException extends LmsWashException {

		public LmsLoginException( String message, Wash wash ) {
			super( message, wash );
		}
		
	}
}
