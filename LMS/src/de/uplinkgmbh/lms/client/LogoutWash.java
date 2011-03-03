package de.uplinkgmbh.lms.client;

import de.axone.wash.DefaultWash;

public class LogoutWash extends DefaultWash {

	public LogoutWash( LmsToken token ) {
		
		try {
			addField( "LMSTOKEN", Type.STRING, token.getToken() );
		} catch( WashException e ){
			e.printStackTrace();
		}
	}
}
