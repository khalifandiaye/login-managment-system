package de.uplinkgmbh.lms.exceptions;

public class LoginException extends Exception{

	private static final long serialVersionUID = 1L;
	public static final int WRONGUSERNAME = 1;
	public static final int WRONGPASSWORD = 2;
	public static final int USERHASNOGROUPORROLE = 3;
	public static final int WRONGAPPLICATION = 4;
	
	public int status = 0;
	
	public LoginException( int status ){
		this.status = status;
	}

}
