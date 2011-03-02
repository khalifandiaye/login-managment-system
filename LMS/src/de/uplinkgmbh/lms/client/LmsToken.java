package de.uplinkgmbh.lms.client;

public class LmsToken {
	
	private final String token;
	
	LmsToken( String token ){
		this.token = token;
	}
	
	String getToken(){
		return token;
	}
	
	@Override
	public String toString(){
		return getToken();
	}
}
