package de.uplinkgmbh.lms.client;

import de.axone.wash.Wash;

public class LmsWashException extends LmsException {
	
	private final Wash wash;

	public LmsWashException( String message, Wash wash, Throwable cause ) {
		super( message, cause );
		this.wash = wash;
	}
	public LmsWashException( String message, Wash wash ) {
		super( message );
		this.wash = wash;
	}
	
	public Wash getWash(){
		return wash;
	}

}
