package de.uplinkgmbh.lms.client;

import de.axone.wash.DefaultWash;

public class IsAllowedWash extends DefaultWash {

	public IsAllowedWash( LmsToken token, String state, String action, String target ) {
		
		try {
			addField( "LMSTOKEN", Type.STRING, token.getToken() );
			addField( "STATE", Type.STRING, state );
			addField( "ACTION", Type.STRING, action );
			addField( "TARGET", Type.STRING, target );
		} catch( WashException e ){
			e.printStackTrace();
		}
	}
}
