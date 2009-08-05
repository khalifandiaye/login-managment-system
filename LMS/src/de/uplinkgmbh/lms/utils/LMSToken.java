package de.uplinkgmbh.lms.utils;

import java.io.Serializable;
import java.util.Date;

public class LMSToken implements Serializable{

	private static final long serialVersionUID = 1L;
	public String application;
	public Date genDate = new Date( System.currentTimeMillis() );
	public long userId;
	public static String magicString = "Magic string for LMS token";
	
}
