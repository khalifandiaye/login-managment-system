package de.uplinkgmbh.lms.services;

import javax.servlet.http.HttpServletRequest;

import de.axone.logging.Log;
import de.axone.logging.Logging;
import de.axone.wash.DefaultWash;
import de.axone.wash.Wash;
import de.axone.wash.Wash.DuplicateEntryException;
import de.axone.wash.Wash.NotFoundException;
import de.axone.wash.Wash.Type;
import de.axone.wash.Wash.WashException;
import de.axone.wash.Wash.WrongTypeException;
import de.axone.wash.handler.Handler.HandlerException;
import de.axone.wash.handler.Handler.OperationNotFoundException;
import de.axone.wash.service.Service;
import de.uplinkgmbh.lms.exceptions.LoginException;
import de.uplinkgmbh.lms.user.Login;

public class LoginService implements Service{
	
	private static Log log = Logging.getLog( LoginService.class );

	@Override
	public Wash service( Wash request, HttpServletRequest httpRequest ) throws HandlerException {
		String operation=null;
		try {
			operation = request.getString( "wash.op" );
			
		} catch( WashException e ) {
			
			throw new OperationNotFoundException();
		}
		
		Wash result = null;
		
		try {
		if( "login".equals( operation ) )	
			result = login( request );
		else if( "logout".equals( operation ) )
			result = logout( request );
		else
			throw new OperationNotFoundException();

		} catch (NotFoundException e) {
			e.printStackTrace();
			result = new DefaultWash();
			try {
				result.addField( "ERROR", Type.STRING, "WASH FIELD NOT FOUND" );
			} catch (DuplicateEntryException e1) {
			} catch (WrongTypeException e1) {
			} catch (NotFoundException e1) {
			}
			
		} catch (WrongTypeException e) {
			e.printStackTrace();
			result = new DefaultWash();
			try {
				result.addField( "ERROR", Type.STRING, "WRONG TYPE" );
			} catch (DuplicateEntryException e1) {
			} catch (WrongTypeException e1) {
			} catch (NotFoundException e1) {
			}
		} catch (DuplicateEntryException e) {
			e.printStackTrace();
			result = new DefaultWash();
			try {
				result.addField( "ERROR", Type.STRING, "DUPLICATE ENTRY" );
			} catch (DuplicateEntryException e1) {
			} catch (WrongTypeException e1) {
			} catch (NotFoundException e1) {
			}
		}
		
		if( result.hasField( "ERROR" ) ){
			try {
				log.error( result.getString( "ERROR" ) );
			} catch (NotFoundException e) {
				e.printStackTrace();
			} catch (WrongTypeException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	
	private Wash login(Wash request) throws NotFoundException, WrongTypeException, DuplicateEntryException{
		
		Wash result = null;

		if( !request.getString( "LOGINNAME" ).equals( "" ) &&
			!request.getString( "PASSWORD" ).equals( "" ) &&
			!request.getString( "APPLICATION" ).equals( "" ) ){
			
			Login l = new Login();
			try {
				l.check( request.getString( "LOGINNAME" ),
						request.getString( "PASSWORD" ),
						request.getString( "APPLICATION" ) );
			} catch (LoginException e) {
				if( LoginException.WRONGPASSWORD == e.status ){
					result = new DefaultWash();
					result.addField( "STATUS", Type.BOOLEAN, false );
					result.addField( "REASON", Type.STRING, "WRONG PASSWORD" );
					return result;
				}else if( LoginException.USERHASNOGROUPORROLE == e.status ){
					result = new DefaultWash();
					result.addField( "STATUS", Type.BOOLEAN, false );
					result.addField( "REASON", Type.STRING, "NO ROLE OR GROUP" );
					return result;
				}else if( LoginException.WRONGAPPLICATION == e.status ){
					result = new DefaultWash();
					result.addField( "STATUS", Type.BOOLEAN, false );
					result.addField( "REASON", Type.STRING, "NO ACTIV USER FOR APPLICATION" );
					return result;
				}else if( LoginException.WRONGUSERNAME == e.status ){
					result = new DefaultWash();
					result.addField( "STATUS", Type.BOOLEAN, false );
					result.addField( "REASON", Type.STRING, "USER NOT EXIST" );
					return result;
				}
			}
			
			String token = l.logIn();
			
			log.info( "Login: "+l.getUsername()+"@"+l.getApplication() );
			if( l.getUser().isActiv() ){
				result = new DefaultWash();
				result.addField( "STATUS", Type.BOOLEAN, true );
				result.addField( "REASON", Type.STRING, "" );
				result.addField( "LMSTOKEN", Type.STRING, token );
			}else{
				result = new DefaultWash();
				result.addField( "STATUS", Type.BOOLEAN, false );
				result.addField( "REASON", Type.STRING, "NOT ACTIV" );
			}
		}else{
			result = new DefaultWash();
			result.addField( "ERROR", Type.STRING, "EMPTY PARAMETERS" );
		
		}
	
		return result;
	} 
	
	private Wash logout(Wash request) throws NotFoundException, WrongTypeException, DuplicateEntryException{
		
		Wash result = null;

		if( !request.getString( "LMSTOKEN" ).equals( "" ) ){
			
			Login l = new Login();
			
			boolean res = l.logOut( request.getString( "LMSTOKEN" ) );
			if( res ){
				result = new DefaultWash();
				result.addField( "STATUS", Type.BOOLEAN, true );
				result.addField( "REASON", Type.STRING, String.valueOf(l.getUser().getLastlogout().getTime()-l.getUser().getLastlogin().getTime()) );
				log.info( "Logout: "+l.getUsername()+"@"+l.getApplication() );
			}else{
				result = new DefaultWash();
				result.addField( "STATUS", Type.BOOLEAN, false );
				result.addField( "REASON", Type.STRING, "WRONG LMSTOKEN" );
			}
			
		}else{
			result = new DefaultWash();
			result.addField( "ERROR", Type.STRING, "EMPTY PARAMETERS" );
		
		}
	
		return result;
	} 
}
