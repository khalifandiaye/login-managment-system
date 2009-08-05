package de.uplinkgmbh.lms.services;

import javax.servlet.http.HttpServletRequest;

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

public class TestService implements Service{

	@Override
	public Wash service( Wash request, HttpServletRequest httpRequest ) throws HandlerException {
		String operation=null;
		try {
			operation = request.getString( "wash.op" );
			
		} catch( WashException e ) {
			
			throw new OperationNotFoundException();
		}
		
		if( ! "test".equals( operation ) )
			throw new OperationNotFoundException();
		
		Wash result = makemyday( request );

		return result;
	}

	public Wash makemyday( Wash request ){
		
		System.out.println( "mein ausgabechen " +request.serialize() );
		
		DefaultWash dw = new DefaultWash();

		try {
			dw.addField( "result", Type.STRING, "Hello World" );
		} catch (DuplicateEntryException e) {
			e.printStackTrace();
		} catch (WrongTypeException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		
		return dw;
	}
}
