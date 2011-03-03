package de.uplinkgmbh.lms.client;

import de.axone.wash.DefaultWash;

public class LoginWash extends DefaultWash {

	public LoginWash( String loginname, String passwd, String application ) {
		
		try {
			addField( "LOGINNAME", Type.STRING, loginname );
			addField( "PASSWORD", Type.STRING, passwd );
			addField( "APPLICATION", Type.STRING, application );
		} catch( WashException e ){
			e.printStackTrace();
		}
	}
}
