package de.uplinkgmbh.lms.client;

public class LmsException extends Exception {

	public LmsException( String message, Throwable cause ) {
		super( message, cause );
	}

	public LmsException( String message ) {
		super( message );
	}

	public LmsException( Throwable cause ) {
		super( cause );
	}

}
